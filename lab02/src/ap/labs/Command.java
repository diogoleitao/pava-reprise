package ap.labs;

public class Command {

	public static Object getInstance(Object className){
		try {
			Object obj = Class.forName((String) className).newInstance();
			System.out.println(obj.getClass());
			return obj;
		} catch (InstantiationException e) {
			System.out.println("Instantiation Error");
			return null;
		} catch (IllegalAccessException e) {
			System.out.println("Illegal Access Error");
			return null;
		} catch (ClassNotFoundException e) {
			System.out.println("Class Not Found Error");
			return null;
		}	
	}
	
	public static void savedObject(Object className){
		System.out.println("Saved name for object of type: " + className.getClass());
	}
	
	public static void selectObject(Object className){
		
	}
	
	
}
