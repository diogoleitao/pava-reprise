package ist.meic.pa;

import java.lang.reflect.InvocationTargetException;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.Loader;
import javassist.NotFoundException;
import javassist.Translator;

public class BoxingProfiler {

	public static void main(String[] args) {
		// SETUP OBJECTS
		Translator translator = new CustomTranslator();
		ClassPool pool = ClassPool.getDefault();

		try {
			pool.insertClassPath("./classes");

			try {
				Loader classLoader = new Loader(pool);
				classLoader.addTranslator(pool, translator);

				try {
					// CLEAN UP ARGS[]
					String[] newArgs = new String[args.length - 1];
					System.arraycopy(args, 1, newArgs, 0, newArgs.length);

					// EXECUTE MAIN METHOD
					Class<?> runnableClass;
					runnableClass = classLoader.loadClass(args[0]); 

					try {
						Object instance = runnableClass.newInstance();
						instance.getClass().getMethod("main", new Class[] { String[].class }).invoke(null, new Object[] { newArgs });
					} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | NoSuchMethodException e) {
						System.err.println("Main method could not be invoked from " + args[0] + " class with " + ((newArgs.length == 0) ? "no arguments" : "arguments " + newArgs.toString()) +  ".");
						return;
					} catch (InvocationTargetException e) {
						System.err.println(e.getTargetException());
					}
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
					System.err.println("Class " + args[0] + " not found.");
					return;
				}
			} catch (CannotCompileException e) {
				System.err.println("Cannot compile instrumented classes.");
				return;
			}
		} catch (NotFoundException e) {
			System.err.println("Classpath to be added to ClassPool not found.");
			return;
		}
	}
}
