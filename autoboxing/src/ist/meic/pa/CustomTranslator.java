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
		for (CtMethod method : ctClass.getDeclaredMethods()) {
			method.instrument(new ExprEditor() {
				@Override
				public void edit(MethodCall methodCall) {
					try {
						String methodName = methodCall.getMethodName();
						CtClass[] parameterTypes = methodCall.getMethod().getParameterTypes();
						String parameterClassName = "";
						if (parameterTypes.length > 0)
							parameterClassName = getWrapperType(parameterTypes[0].getName());

						if (methodName.contains("valueOf")) {
							Storage.addBoxingMethod(methodName, parameterClassName);
							//System.err.println(methodName + " " + parameterClassName);
						} else if (methodName.contains("Value")) {
							Storage.addUnboxingMethod(methodName, parameterClassName);
							//System.err.println(methodName + " " + parameterClassName);
						}
					} catch (NotFoundException e) {
						throw new RuntimeException(e);
					}
				}
			});
		}
	}

	private String getWrapperType(String primitiveType) {
		return "";
	}
}
