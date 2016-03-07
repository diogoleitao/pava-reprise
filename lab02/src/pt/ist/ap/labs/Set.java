package pt.ist.ap.labs;

public class Set implements Command {

	@Override
	public void execute() {
		Program.variables.put(Program.command[1], Program.lastInstanceUsed);
		System.out.println("Saved name for object of type: " + Program.lastInstanceUsed.getClass());
	}
}
