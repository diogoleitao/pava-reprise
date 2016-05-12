package ist.meic.pa.GenericFunctions;

import java.util.Arrays;

/**
 * The Class Utils.
 */
public class Utils {
	
	/**
	 * Prints the object in an Array.
	 *
	 * @param obj the obj
	 */
	public static void println(Object obj) {
		if (obj instanceof Object[])
			System.out.println(Arrays.deepToString((Object[]) obj));
		else
			System.out.println(obj);
	}

	/**
	 * Returns the object in an Array.
	 *
	 * @param obj the obj
	 * @return the object
	 */
	public static Object listify(Object obj) {
		if (obj instanceof Object[])
			return Arrays.deepToString((Object[]) obj);
		return obj;
	}

	/**
	 * Gets the type from an invoked call method.
	 *
	 * @param args the arguments
	 * @return the types from arguments
	 */
	public static Object getTypesFromArgs(Object[] args) {
		Object[] argsTypes = new Object[args.length];

		for (int i = 0; i < args.length; i++)
			argsTypes[i] = args[i].getClass();

		return listify(argsTypes);
	}
}
