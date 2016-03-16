package ist.meic.pa;

import java.lang.reflect.Modifier;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;
import javassist.Translator;
import javassist.expr.ExprEditor;

public class CustomTranslator implements Translator {

	@Override
	public void start(ClassPool pool) throws NotFoundException, CannotCompileException {}

	@Override
	public void onLoad(ClassPool pool, String ctClass) throws NotFoundException, CannotCompileException {
		CtClass cc = pool.get(ctClass);
		cc.setModifiers(Modifier.PUBLIC);

		boolean javassist = ctClass.contains("javassist");
		boolean profiler = ctClass.contains("ist.meic.pa");

		if (!javassist && !profiler) {
			try {
				instrumentClass(cc);
			} catch (Throwable e) {
				System.err.println("Error while instrumenting class " + ctClass);
			}
		}
	}

	private void instrumentClass(CtClass ctClass) throws CannotCompileException {
		for (CtMethod method : ctClass.getMethods()) {
			method.instrument(new ExprEditor() {
				// TODO: detect boxing
				final String methodTemplate = "ist.meic.pa.BoxingProfiler.addBoxingMethod(" + method.getLongName() + ");";
				
				// TODO: detect unboxing
				final String methodTemplate2 = "ist.meic.pa.BoxingProfiler.addUnboxingMethod(" + method.getLongName() + ");";
			});
		}
	}
}
