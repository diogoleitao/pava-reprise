package pt.ist.ap.labs;

public class Get implements Command {

	@Override
	public void execute() {
		if (Shell.variables.isEmpty())
			System.out.println("No instances stored.");
		else {
			Shell.lastResult = Shell.variables.get(Shell.command[1]);
			if (Shell.lastResult == null)
				System.out.println("No class stored with name '" + Shell.command[1] + "'.");
			else
				Shell.printResult();
		}
	}
}
