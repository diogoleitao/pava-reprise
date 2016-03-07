package pt.ist.ap.labs;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Scanner;

public class Program {
	public static Scanner sc;
	public static String[] command;

	public static String lastClassUsed = Object.class.getName();
	public static Object lastInstanceUsed = new Object();
	public static HashMap<String, Object> variables = new HashMap<String, Object>();

	public static void main(String[] args) {
		while (true) {
			System.out.println("Command:>");

			sc = new Scanner(System.in);
			command = sc.nextLine().split(" ");
			sc.close();

			try {
				Command commandToExecute = (Command) java.lang.Class.forName("pt.ist.ap.labs." + command[0]).newInstance();
				commandToExecute.execute();
			} catch (InstantiationException | IllegalAccessException e) {
				System.out.println(e.getMessage());
			} catch (ClassNotFoundException e) {
				System.out.println("Trying generic command: " + command[0]);
				try {
					if (command.length > 1)
						System.out.println(findMethod(lastInstanceUsed.getClass(), command[0], ((Object) command[1]).getClass()).invoke(lastInstanceUsed, (Object) command[1]));
					else
						System.out.println(findMethod(lastInstanceUsed.getClass(), command[0], null).invoke(lastInstanceUsed));
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException e1) {
					System.out.println(e1.getMessage());
				}
			}
		}
	}

	public static Method findMethod(java.lang.Class<?> type, String name, java.lang.Class<?> argType) throws NoSuchMethodException {
		try {
			if (argType == null)
				return type.getMethod(name);
			else
				return type.getMethod(name, argType);
		} catch (NoSuchMethodException e) {
			if (argType == Object.class)
				throw new NoSuchMethodException(name);
			else
				return findMethod(type, name, argType.getSuperclass());
		}
	}
}
