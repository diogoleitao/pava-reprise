package ist.meic.pa.extensions;

import java.lang.reflect.Modifier;

import ist.meic.pa.Storage;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;
import javassist.Translator;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;

/**
 * Extended Translator class used to instrument the classes about to be
 * profiled. This extension to the profiler does not instrument (and,
 * consequently, does not profile) classes whose methods have the
 * IgnoreAutoboxing annotation.
 */
public class ExtendedTranslator implements Translator {

	@Override
	public void start(ClassPool pool) throws NotFoundException, CannotCompileException {
	}

	/**
	 * The onLoad method checks if the function being loaded belongs to this
	 * project's package, or to Javassist's, and, if not, calls the
	 * instrumentClass method with that class.
	 */
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
			} catch (ClassNotFoundException e) {
				throw new RuntimeException(e);
			}
		}
	}

	/**
	 * Checks the input class for autoboxing operations and instruments it if
	 * such operations are found. If a call to the valueOf() or any of the
	 * *Value() methods are found and the IgnoredAutoboxing annotation is not
	 * present in the method's declaration, that call is replaced by a block of
	 * code containing that same call followed by a call to the
	 * updateAutoboxingCounter() method, if the types are compatible. The
	 * compatibility is checked with the checkTypeCompatibility() method.
	 * 
	 * This method also looks for the main() method of the instrumented class
	 * and adds the printOutput() method to it at the end, so that when the
	 * input program is finished, the information gathered by the profiler is
	 * printed.
	 */
	private void instrumentClass(CtClass ctClass) throws CannotCompileException, ClassNotFoundException {
		for (CtMethod method : ctClass.getDeclaredMethods()) {
			if (!method.hasAnnotation(IgnoreAutoboxing.class)) {
				final String methodName = method.getLongName();
				method.instrument(new ExprEditor() {
					@Override
					public void edit(MethodCall methodCall) throws CannotCompileException {
						try {
							CtClass[] parameterTypes = methodCall.getMethod().getParameterTypes();
							String parameterClassName = "";
							String codeTemplate =
									"{" +
											"$_ = $proceed($$);" +
											"if (ist.meic.pa.Storage.checkTypeCompatibility($_, $0)) {" +
												"ist.meic.pa.Storage.updateAutoboxingCounter(\"%s\");" +
											"}" +
									"}";
							String autoboxingMethodName = methodCall.getMethod().getLongName();
							String incompleteKey = methodName + Storage.SEPARATOR;
							String boxed = " boxed ";
							String unboxed = " unboxed ";
	
							if (autoboxingMethodName.contains("valueOf")) {
								parameterClassName = getWrapperType(parameterTypes[0].getName());
								String finalKey = incompleteKey + parameterClassName + Storage.SEPARATOR + boxed;
								methodCall.replace(String.format(codeTemplate, finalKey, finalKey));
	
							} else if (autoboxingMethodName.contains("Value")) {
								String methodCallName = methodCall.getMethodName();
								String primitiveType = methodCallName.substring(0, methodCallName.indexOf("Value"));
								parameterClassName = getWrapperType(primitiveType);
								String finalKey = incompleteKey + parameterClassName + Storage.SEPARATOR + unboxed;
								methodCall.replace(String.format(codeTemplate, finalKey, finalKey));
							}
						} catch (NotFoundException e) {
							throw new RuntimeException(e);
						}
					}
				});
	
				if (methodName.contains("main(java.lang.String[])"))
					method.insertAfter("ist.meic.pa.Storage.printOutput();");
			}
		}
	}

	/**
	 * Translates the primitive type designation, obtained from an autoboxing
	 * operation, to its wrapper correspondent.
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
			return "java.lang.Boolean";
		else if (primitiveType.equals("char"))
			return "java.lang.Character";
		else if (primitiveType.equals("byte"))
			return "java.lang.Byte";
		else
			return "";
	}
}
