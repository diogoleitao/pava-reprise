package pt.ist.ap.labs;

public class Index implements Command {

	@Override
	public void execute() {
		if (Program.lastInstanceUsed.getClass().isArray()) {
			Object[] array = (Object[]) Program.lastInstanceUsed;
			Program.lastInstanceUsed = array[Integer.parseInt(Program.command[1])];
		} else
			System.out.println("Current object is not an array.");
	}
}
