package ap.labs;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Scanner;

public class Program {

	public static String lastClassUsed = "";
	public static Object lastInstanceUsed = null;
	public static HashMap<String, Object> variables = new HashMap<String, Object>();

	public static void main(String[] args) {
		while (true) {
			System.out.println("Command:>");

			Scanner sc = new Scanner(System.in);
			String[] command = sc.nextLine().split(" ");

			if (command[0].equals("Class")) {
				try {
					lastClassUsed = command[1];
					lastInstanceUsed = Class.forName(lastClassUsed).newInstance();
					System.out.println("class " + lastClassUsed);
				} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
					System.out.println(e.getMessage());
				}
			} else if (command[0].equals("Set")) {
				if (lastClassUsed.equals(""))
					System.out.println("No class defined previously. Invoke Class command first.");
				else {
					variables.put(command[1], lastInstanceUsed);
					System.out.println("Saved name for object of type: class " + lastClassUsed);
				}
			} else if (command[0].equals("Get")) {
				if (variables.isEmpty())
					System.out.println("No instances stored.");
				else {
					lastInstanceUsed = variables.get(command[1]);
					if (lastInstanceUsed == null)
						System.out.println("No object stored with name " + command[1]);
				}
			} else if (command[0].equals("Index")) {
				int index = Integer.parseInt(command[1]);
				if (lastInstanceUsed.getClass().isArray()) {
					Object[] array = (Object[]) lastInstanceUsed;
					lastInstanceUsed = array[index];
				}
			} else {
				System.out.println("Trying generic command: " + command[0]);
				try {
					if (command.length > 1)
						findMethod(lastInstanceUsed.getClass(), command[0], ((Object)command[1]).getClass()).invoke(lastInstanceUsed, (Object) command[1]);
					else
						findMethod(lastInstanceUsed.getClass(), command[0], null).invoke(lastInstanceUsed, (Object) null);
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException e) {
					System.out.println(e.getMessage());
				}
			}
		}
	}
	
	public static Method findMethod(Class<?> type, String name, Class<?> argType) throws NoSuchMethodException {
		try {
			return type.getMethod(name, argType);
		} catch (NoSuchMethodException e) {
			if (argType == Object.class) 
				throw new NoSuchMethodException(name);
			else
				return findMethod(type, name, argType.getSuperclass());
		}
	}
}
