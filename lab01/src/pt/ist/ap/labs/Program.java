package pt.ist.ap.labs;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Scanner;

public class Program {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		String classname = sc.nextLine();
		sc.close();

		try {
			Class<?> klass = Class.forName(classname);
			Class<?>[] interfaceName = klass.getInterfaces();
			Object[] obj = new Object[0];

			for (Class<?> c1 : interfaceName) {
				if (c1.getName().equals("pt.ist.ap.labs.Message")) {
					for (Method method : klass.getMethods()) {
						if (method.getName().equals("say"))
							method.invoke(klass.newInstance(), obj);
					}
				}
			}

		} catch (ClassNotFoundException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | InstantiationException e) {
			e.printStackTrace();
		}
	}
}
