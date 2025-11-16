public class Main {
	public static void main(String[] args) {
		UserInterface frame=new UserInterface();
		frame.setResizable(false);
		Colors.restoreColors(0);
		UserInterface.updateInputBoxes();
		frame.setVisible(true);
	}
}