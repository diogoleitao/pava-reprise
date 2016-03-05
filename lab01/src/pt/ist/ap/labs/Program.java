package pt.ist.ap.labs;

import java.lang.Class;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Scanner;

public class Program {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//String classname = args[0];
		
		Scanner sc = new Scanner(System.in);
		String classname = sc.nextLine();
		sc.close();
		
		try {
			Class<?> c = Class.forName(classname);
			Object[] obj = new Object[0];
			/*Message m = (Message) c.newInstance();
			m.say();*/
			
			Class[] interfaceName = c.getInterfaces();
			
			for(Class c1 : interfaceName){
				if(c1.getName().equals("pt.ist.ap.labs.Message")){
					Method[] methodsName = c.getMethods();
					for(Method m1 : methodsName){
						if(m1.getName().equals("say")){
							m1.invoke(c.newInstance(), obj);
							
						}
					}
				}
			}
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("Class not Found");
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			System.out.println("Instantiation Error");
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			System.out.println("Illegal Access Error");
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			System.out.println("Illegal Argument Error");
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			System.out.println("Invocation Target Error");
		}
		
	}

}
