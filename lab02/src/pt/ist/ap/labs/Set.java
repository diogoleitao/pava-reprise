package pt.ist.ap.labs;

public class Set implements Command {

	@Override
	public void execute() {
		Shell.variables.put(Shell.command[1], Shell.lastResult);
		System.out.println("Saved name for object of type: " + Shell.lastResult);
	}
}
