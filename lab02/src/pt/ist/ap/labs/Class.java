package pt.ist.ap.labs;

public class Class implements Command {

	@Override
	public void execute() {
		try {
			Program.lastClassUsed = Program.command[1];
			Program.lastInstanceUsed = java.lang.Class.forName(Program.lastClassUsed).newInstance();
			System.out.println(Program.lastInstanceUsed.getClass());
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}
}
