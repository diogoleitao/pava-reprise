package ist.meic.pa.GenericFunctions;

import java.util.Arrays;

public class Utils {
	public static void println(Object obj) {
		if (obj instanceof Object[])
			System.out.println(Arrays.deepToString((Object[]) obj));
		else
			System.out.println(obj);
	}

	public static Object listify(Object obj) {
		if (obj instanceof Object[])
			return Arrays.deepToString((Object[]) obj);
		else
			return obj;
	}

	public static Object getTypesFromArgs(Object[] args) {
		Object[] argsTypes = new Object[args.length];

		for (int i = 0; i < args.length; i++)
			argsTypes[i] = args[i].getClass();

		return listify(argsTypes);
	}
}
