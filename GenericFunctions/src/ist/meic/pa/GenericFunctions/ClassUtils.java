package ist.meic.pa.GenericFunctions;

import java.util.Arrays;

public class ClassUtils {
	public static void println(Object obj) {
		if (obj instanceof Object[])
			System.out.println(Arrays.deepToString((Object[]) obj));
		else
			System.out.println(obj);
	}

}
