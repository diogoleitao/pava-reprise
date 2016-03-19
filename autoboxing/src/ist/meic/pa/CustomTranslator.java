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

	private void instrumentClass(final CtClass ctClass) throws CannotCompileException {
		for (final CtMethod method : ctClass.getDeclaredMethods()) {
			final String methodName = method.getLongName();
			//System.out.println(methodName);
			method.instrument(new ExprEditor() {
				@Override
				public void edit(MethodCall methodCall) throws CannotCompileException {
					try {
						CtClass[] parameterTypes = methodCall.getMethod().getParameterTypes();
						String parameterClassName = "";
						String codeTemplate = "";
						String autoboxingMethodName = methodCall.getMethod().getLongName();
						String incompleteKey = methodName + Storage.getInstance().SEPARATOR;
						String boxed = " boxed ";
						String unboxed = " unboxed ";
						
						if (autoboxingMethodName.contains("valueOf")) {
							parameterClassName = getWrapperType(parameterTypes[0].getName());
							incompleteKey += parameterClassName + Storage.getInstance().SEPARATOR;
							//Storage.getInstance().addAutoboxingMethod(incompleteKey + boxed);

							codeTemplate = "{ist.meic.pa.Storage.getInstance().addAutoboxingMethod(\"%s\");"
									+" ist.meic.pa.Storage.getInstance().updateAutoboxingCounter(\"%s\");}";
							method.insertAfter(String.format(codeTemplate, incompleteKey + boxed, incompleteKey + boxed));

						} else if (autoboxingMethodName.contains("Value")) {
							String methodCallName = methodCall.getMethodName();
							parameterClassName = getWrapperType(methodCallName.substring(0, methodCallName.indexOf("Value")));
							incompleteKey += parameterClassName + Storage.getInstance().SEPARATOR;
							//Storage.getInstance().addAutoboxingMethod(incompleteKey + unboxed);

							codeTemplate = "{ist.meic.pa.Storage.getInstance().addAutoboxingMethod(\"%s\");"
									+" ist.meic.pa.Storage.getInstance().updateAutoboxingCounter(\"%s\");}";
							method.insertAfter(String.format(codeTemplate, incompleteKey + unboxed, incompleteKey + unboxed));
						}
						
						if (methodName.contains("main(java.lang.String[])")) {
							codeTemplate = "{ist.meic.pa.Storage.getInstance().printOutput();}";
							method.insertAfter(codeTemplate);
						}
					} catch (NotFoundException e) {
						throw new RuntimeException(e);
					}
				}
			});
		}
	}

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
