package ist.meic.pa.GenericFunctions;

import java.util.Arrays;

/**
 * The Class Utils.
 */
public class Utils {
	
	/**
	 * Println.
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
	 * Listify.
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
	 * Gets the types from args.
	 *
	 * @param args the args
	 * @return the types from args
	 */
	public static Object getTypesFromArgs(Object[] args) {
		Object[] argsTypes = new Object[args.length];

		for (int i = 0; i < args.length; i++)
			argsTypes[i] = args[i].getClass();

		return listify(argsTypes);
	}
}
