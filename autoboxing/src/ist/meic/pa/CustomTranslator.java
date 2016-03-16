package ist.meic.pa;

import java.lang.reflect.Modifier;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CodeConverter;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;
import javassist.Translator;
import javassist.runtime.Cflow;

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
			Cflow cflow = new Cflow();
			method.useCflow("valueOf");
			//CodeConverter converter = new CodeConverter();
			//converter.replaceNew(ctClass, null);
			System.out.println(cflow.value());

			final String boxingTemplate =
					"{"
							+ "ist.meic.pa.BoxingProfiler.addBoxingMethod(" +
							method.getLongName() + ", " +
							ctClass.getName() + ");"
					+ "}";

			final String uboxingTemplate =
					"{"
							+ "ist.meic.pa.BoxingProfiler.addUnboxingMethod(" +
							method.getLongName() + ", " +
							ctClass.getName() + ");"
					+ "}";
		}
	}
}
