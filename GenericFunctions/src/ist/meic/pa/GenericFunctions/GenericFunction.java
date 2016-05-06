package ist.meic.pa.GenericFunctions;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;

import ist.meic.pa.tests.Test3;

public class GenericFunction {
	private String name;
	private static String EXCEPTION_MESSAGE = "No methods for generic function %s with args %s of classes %s.";
	private ArrayList<GFMethod> mainMethods = new ArrayList<GFMethod>();
	private ArrayList<GFMethod> befores = new ArrayList<GFMethod>();
	private ArrayList<GFMethod> afters = new ArrayList<GFMethod>();

	public GenericFunction(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void addMethod(GFMethod gfm) {
		mainMethods.add(gfm);
	}

	public void addAfterMethod(GFMethod gfm) {
		afters.add(gfm);
	}

	public void addBeforeMethod(GFMethod gfm) {
		befores.add(gfm);
	}

	public Object call(Object... args) {
		ArrayList<Class<?>> parameterTypes = new ArrayList<Class<?>>();
		
		for (Object arg : args)
			parameterTypes.add(arg.getClass());
		EffectiveMethod effectiveMethod = StandardCombination.computeEffectiveMethod(befores, mainMethods, afters,
				new ArrayList<Object>(Arrays.asList(args)));
		Object returnedObject = null;
		for (GFMethod before : effectiveMethod.getBefores()) {
			Method[] declaredMethods = before.getClass().getDeclaredMethods();
			for (Method method : declaredMethods) {
				method.setAccessible(true);
				if (method.getName().equals("call"))
					try {
						method.invoke(before, args);
					} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
						
					}
			}
			break;
		}

		for(Method method : effectiveMethod.getMainMethod().getClass().getDeclaredMethods()) {
			method.setAccessible(true);
			if (method.getName().equals("call"))
				try {
					returnedObject = method.invoke(effectiveMethod.getMainMethod(), args);
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					
				}
		}

		for (GFMethod after : effectiveMethod.getAfters()) {
			Method[] declaredMethods = after.getClass().getDeclaredMethods();
			for (Method method : declaredMethods) {
				method.setAccessible(true);
				if (method.getName().equals("call"))
					try {
						method.invoke(after, args);
					} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
						
					}
			}
			break;
		}

		return returnedObject;

	}

	private Object listify(Object obj) {
		if (obj instanceof Object[]) {
			return Arrays.deepToString((Object[]) obj);
		} else {
			return obj;
		}
	}
}
