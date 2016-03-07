package pt.ist.ap.labs;

public class Get implements Command {

	@Override
	public void execute() {
		if (Program.variables.isEmpty())
			System.out.println("No instances stored.");
		else {
			Program.lastInstanceUsed = Program.variables.get(Program.command[1]);
			if (Program.lastInstanceUsed == null)
				System.out.println("No object stored with name '" + Program.command[1] + "'.");
			else {
				Program.lastClassUsed = Program.lastInstanceUsed.getClass().toGenericString();
				System.out.println(Program.lastInstanceUsed.getClass());
			}
		}
	}
}
