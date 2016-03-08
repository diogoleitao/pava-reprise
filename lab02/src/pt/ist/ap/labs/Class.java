package pt.ist.ap.labs;

public class Class implements Command {

	@Override
	public void execute() {
		try {
			Shell.lastResult = java.lang.Class.forName(Shell.command[1]);
			Shell.printResult();
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}
}
