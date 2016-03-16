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
				System.err.println("Error while instrumenting class " + ctClass);
			}
		}
	}

	private void instrumentClass(CtClass ctClass) throws CannotCompileException {
		for (CtMethod method : ctClass.getDeclaredMethods()) {
			method.instrument(new ExprEditor() {
				@Override
				public void edit(MethodCall methodCall) {
					try {
						String methodName = methodCall.getMethodName();
						CtClass[] parameterTypes = methodCall.getMethod().getParameterTypes();
						String parameterClassName = parameterTypes[0].toClass().getName();

						if (methodName.contains("valueOf")) {
							Storage.addBoxingMethod(methodName, parameterClassName);
							System.err.println(methodName + " " + parameterClassName);
						} else if (methodName.contains("Value")) {
							Storage.addUnboxingMethod(methodName, parameterClassName);
							System.err.println(methodName + " " + parameterClassName);
						}

					} catch (NotFoundException | CannotCompileException e) {
						// TODO: deal with the exceptions properly
						e.printStackTrace();
					}
				}
			});
		}
	}
}
