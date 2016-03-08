package pt.ist.ap.labs;

public class Quit implements Command {

	@Override
	public void execute() {
		System.exit(0);
	}
}
