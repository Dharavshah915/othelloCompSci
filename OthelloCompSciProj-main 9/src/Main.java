import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        Main game = new Main();
    }

    private BoardInterface boardInterface;

    /**
     * Constructs the main application window for the Othello game.
     * Initializes the JFrame, sets the title, and adds the BoardInterface component.
     * The window is set to be non-resizable, and the default close operation is set to exit the application.
     */
    public Main() {
        JFrame frame = new JFrame("Othello Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        boardInterface = new BoardInterface();
        frame.getContentPane().add(boardInterface);
        frame.pack();
        frame.setVisible(true);
    }
}