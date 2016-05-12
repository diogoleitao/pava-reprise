package ist.meic.pa.GenericFunctions;

import java.util.Arrays;

/**
 * The Class Utils.
 */
public class Utils {

	/**
	 * An alternative representation to the native toString() method
	 *
	 * @param obj:
	 *            the obj
	 * @return the object
	 */
	public static Object listify(Object obj) {
		if (obj instanceof Object[])
			return Arrays.deepToString((Object[]) obj);
		return obj;
	}

	/**
	 * Returns the types of the arguments.
	 *
	 * @param args:
	 *            the arguments
	 * @return the types from arguments
	 */
	public static Object getTypesFromArgs(Object[] args) {
		Object[] argsTypes = new Object[args.length];

		for (int i = 0; i < args.length; i++)
			argsTypes[i] = args[i].getClass();

		return listify(argsTypes);
	}
}
