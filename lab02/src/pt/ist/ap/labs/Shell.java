package pt.ist.ap.labs;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Scanner;

public class Shell {
	public static Scanner sc;
	public static String[] command;

	public static Object lastResult = null;
	public static Object[] arrayResult = null;
	public static Method currentMethod = null;
	public static HashMap<String, Object> variables = new HashMap<String, Object>();

	public static void main(String[] args) {
		while (true) {
			System.out.print("Command:> ");

			sc = new Scanner(System.in);
			command = sc.nextLine().split(" ");

			try {
				Command commandToExecute = (Command) java.lang.Class.forName("pt.ist.ap.labs." + command[0]).newInstance();
				commandToExecute.execute();
			} catch (InstantiationException | IllegalAccessException e) {
				System.out.println(e.getMessage());
			} catch (ClassNotFoundException e) {
				System.out.println("Trying generic command: " + command[0]);
				try {
					if (command.length > 1) {
						Object[] parsedObject = new Object[] { command[2] };						
						java.lang.Class<?>[] arguments = new java.lang.Class<?>[] { null };

						if (command[1].equals("int"))
							arguments = new java.lang.Class<?> [] { int.class };
						else if (command[1].equals("String"))
							arguments = new java.lang.Class<?> [] { String.class };
						else
							arguments = new java.lang.Class<?> [] { Object.class };

						currentMethod = lastResult.getClass().getMethod(command[0], arguments);
						lastResult = currentMethod.invoke(lastResult, parsedObject);
						printResult();
					} else {
						currentMethod = lastResult.getClass().getMethod(command[0], (java.lang.Class<?>[]) null);
						lastResult = currentMethod.invoke(lastResult);
						printResult();
					}
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e1) {
					System.out.println(e1.getMessage());
				} catch (NoSuchMethodException e1) {
					System.out.println("No such method " + command[0]);
				}
			}
		}
	}

	public static void printResult() {
		if (lastResult.getClass().isArray()) {
			arrayResult = (Object[]) lastResult;
			for (int i = 0; i < arrayResult.length; i++)
				System.out.println(arrayResult[i]);
		} else
			System.out.println(lastResult);
	}
}
