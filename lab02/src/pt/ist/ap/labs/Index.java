package pt.ist.ap.labs;

public class Index implements Command {

	@Override
	public void execute() {
		if (Shell.lastResult.getClass().isArray()) {
			Shell.arrayResult = (Object[]) Shell.lastResult;
			Shell.lastResult = Shell.arrayResult[Integer.parseInt(Shell.command[1])];
			Shell.printResult();
		} else
			System.out.println("Current object is not an array.");
	}
}
