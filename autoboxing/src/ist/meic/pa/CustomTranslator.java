package ist.meic.pa;

import java.lang.reflect.Modifier;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;
import javassist.Translator;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;

/**
 * The Class CustomTranslator.
 */
public class CustomTranslator implements Translator {

	@Override
	public void start(ClassPool pool) throws NotFoundException, CannotCompileException {
	}

	@Override
	public void onLoad(ClassPool pool, String ctClass) throws NotFoundException, CannotCompileException {
		CtClass cc = pool.get(ctClass);
		cc.setModifiers(Modifier.PUBLIC);

		boolean javassist = ctClass.contains("javassist");
		boolean profiler = ctClass.contains("ist.meic.pa");

		if (!javassist && !profiler) {
			try {
				instrumentClass(cc);
			} catch (CannotCompileException e) {
				throw new CannotCompileException(e);
			}
		}
	}

	/**
	 * Instrument class.
	 *
	 * @param ctClass the ct class
	 * @throws CannotCompileException the cannot compile exception
	 */
	private void instrumentClass(final CtClass ctClass) throws CannotCompileException {
		for (CtMethod method : ctClass.getDeclaredMethods()) {
			final String methodName = method.getLongName();

			method.instrument(new ExprEditor() {
				@Override
				public void edit(MethodCall methodCall) throws CannotCompileException {
					try {
						CtClass[] parameterTypes = methodCall.getMethod().getParameterTypes();
						String parameterClassName = "";
						String codeTemplate = "";
						String autoboxingMethodName = methodCall.getMethod().getLongName();

						if (autoboxingMethodName.contains("valueOf")) {
							codeTemplate = "{ist.meic.pa.Storage.updateBoxingCounter(\"%s\");}";
							methodCall.getMethod().insertBefore(String.format(codeTemplate, methodName));

							parameterClassName = getWrapperType(parameterTypes[0].getName());
							Storage.addBoxingMethod(methodName, parameterClassName);
						} else if (autoboxingMethodName.contains("Value")) {
							codeTemplate = "{ist.meic.pa.Storage.updateUnboxingCounter(\"%s\");}";
							methodCall.getMethod().insertBefore(String.format(codeTemplate, methodName));

							String methodCallName = methodCall.getMethodName();
							parameterClassName = getWrapperType(methodCallName.substring(0, methodCallName.indexOf("Value")));

							Storage.addUnboxingMethod(methodName, parameterClassName);
						}
					} catch (NotFoundException e) {
						throw new RuntimeException(e);
					}
				}
			});
		}
	}

	/**
	 * Gets the wrapper type.
	 *
	 * @param primitiveType the primitive type
	 * @return the wrapper type
	 */
	private String getWrapperType(String primitiveType) {
		if (primitiveType.equals("int"))
			return "java.lang.Integer";
		else if (primitiveType.equals("float"))
			return "java.lang.Float";
		else if (primitiveType.equals("double"))
			return "java.lang.Double";
		else if (primitiveType.equals("long"))
			return "java.lang.Long";
		else if (primitiveType.equals("short"))
			return "java.lang.Short";
		else if (primitiveType.equals("boolean"))
			return "java.lang.Booelan";
		else if (primitiveType.equals("char"))
			return "java.lang.String";
		else if (primitiveType.equals("byte"))
			return "java.lang.Byte";
		else
			return "";
	}
}
