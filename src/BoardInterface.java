import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class BoardInterface extends JPanel implements MouseListener {
    private static final int P_Height = 700;
    private static final int P_Width = 700;

    private Board board;
    public enum PlayingState {WhiteTurn, BlackTurn,Draw, WhiteWin, BlackWin}

    private PlayingState gameState;
    private String gameStateString;

    private ComputerPlayer cpuMode;
    private ComputerPlayer testMode;

    public static final Color lightGreen  = new Color(162, 255, 0, 61);

    public static final Color LIGHT_GREEN = lightGreen;

    private JButton automatedGameButton;

    public BoardInterface() {
        setPreferredSize(new Dimension(P_Width, P_Height));
        setBackground(LIGHT_GREEN);

        board = new Board(new Coordinate(0, 0), P_Width, P_Height, 8, 8);
        setGameState(PlayingState.BlackTurn);
        selectComputerMode();
        addMouseListener(this);

        automatedGameButton = new JButton("Run Game Test");
        automatedGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                autoGame();
            }
        });
        add(automatedGameButton);

    }

    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        board.paint(g);
        drawGameState(g);
    }

    /**
     * Invoked when a mouse button is pressed on the GamePanel.
     * Handles the logic for executing a move, checking the game result, and triggering the second player's move (if applicable).
     * The method is triggered only when it is the WhiteTurn or BlackTurn state.
     * It converts the mouse coordinates to a grid position and executes the move on the board.
     * After executing the move, it checks the game result and updates the state accordingly.
     * If it is the second player's turn and the second player is a computer, it triggers the second player's move.
     * Finally, it repaints the GamePanel to update the display.
     *
     * @param e the MouseEvent object representing the mouse press event
     */
    @Override
    public void mousePressed(MouseEvent e) {
        if (gameState == PlayingState.WhiteTurn || gameState == PlayingState.BlackTurn) {
            Coordinate gridPosition = board.convertMouseToGridPosition(new Coordinate(e.getX(), e.getY()));
            executeMove(gridPosition);
            checkEndResult(true);

            // will run when its player 2's turn and player 2 is the computer
            secondPlayerMove();
        }
        repaint();
    }

    /**
     * Executes a move on the board based on the given grid position.
     * If the grid position is not a valid move, it prints an error message.
     * If it is the BlackTurn state, it plays the move as player 1 (black) and updates the game state to WhiteTurn.
     * If it is the WhiteTurn state, it plays the move as player 2 (white) and updates the game state to BlackTurn.
     *
     * @param gridPosition the Coordinate representing the grid position of the move
     */

    public void executeMove(Coordinate gridPosition) {
        if (!board.isValidMove(gridPosition)) {
            System.out.println("Invalid move, please select a highlighted square");
        } else if (gameState == PlayingState.BlackTurn) {
            board.playMove(gridPosition, 1);
            setGameState(PlayingState.WhiteTurn);
        } else if (gameState == PlayingState.WhiteTurn) {
            board.playMove(gridPosition, 2);
            setGameState(PlayingState.BlackTurn);
        }
    }


    /**
     * Sets the game state to the specified new state and updates the game state string accordingly.
     * If there are valid moves available, it updates the game state string to indicate the current player's turn.
     * If there are no valid moves available, it checks the end result of the game and updates the game state string accordingly.
     * If the game is still ongoing, it updates the valid moves for the next player's turn.
     *
     * @param newState the new PlayingState to set
     */
    public void setGameState(PlayingState newState) {
        gameState = newState;
        int validMovesCount = board.getEveryValidMove().size();

        if (validMovesCount > 0) {
            gameStateString = (gameState == PlayingState.BlackTurn) ? "Black Turn" : "White Turn";
        } else {
            if (gameState == PlayingState.BlackTurn || gameState == PlayingState.WhiteTurn) {
                board.updateValidMoves((gameState == PlayingState.BlackTurn) ? 2 : 1);
                if (board.getEveryValidMove().size() > 0) {
                    setGameState((gameState == PlayingState.BlackTurn) ? PlayingState.WhiteTurn : PlayingState.BlackTurn);
                } else {
                    checkEndResult(false);
                }
            } else if (gameState == PlayingState.WhiteWin) {
                gameStateString = "White Wins!";
            } else if (gameState == PlayingState.BlackWin) {
                gameStateString = "Black Wins!";
            } else if (gameState == PlayingState.Draw) {
                gameStateString = "Draw Game";
            }
        }
    }

    /**
     * Checks the end result of the game based on the current board state.
     * If the parameter checkWin is true, it checks for a winner and updates the game state accordingly.
     * If the parameter checkWin is false, it checks for a draw and updates the game state accordingly.
     * The result is determined by invoking the board's getWinner method, which returns:
     * - 1 if player 1 (black) wins
     * - 2 if player 2 (white) wins
     * - 3 if the game ends in a draw
     *
     * @param checkWin a boolean indicating whether to check for a winner or a draw
     */
    private void checkEndResult(boolean checkWin) {
        int result = board.getWinner(checkWin);
        if (result == 1) {
            setGameState(PlayingState.BlackWin);
        } else if (result == 2) {
            setGameState(PlayingState.WhiteWin);
        } else if (result == 3) {
            setGameState(PlayingState.Draw);
        }
    }


    /**
     * Draws the current game state on the game board.
     *
     * @param g The Graphics object used for drawing.
     */
    private void drawGameState(Graphics g) {
        g.setColor(Color.LIGHT_GRAY);
        g.setFont(new Font("SansSerif", Font.BOLD, 40));
        int strWidth = g.getFontMetrics().stringWidth(gameStateString);
        g.drawString(gameStateString, P_Width/2 - strWidth/2, P_Height -45);
    }

    /**
     * Executes a move for the second player (if it is the computer) while it is their turn.
     * Uses the cpuMode object to choose the move to make.
     */
    private void secondPlayerMove(){
        while (gameState == PlayingState.WhiteTurn && cpuMode != null){
            executeMove(cpuMode.chooseMove());
            checkEndResult(true);
        }
    }


    /**
     * Plays the entire game automatically by making moves for both players until the game ends.
     * Uses the testMode object of ComputerPlayer to choose moves.
     */
    private void autoGame(){
        testMode = new ComputerPlayer(board);
        while (board.getEveryValidMove().size() > 0) {
            if (gameState == PlayingState.BlackTurn) {
                executeMove(testMode.chooseMove());
            } else {
                executeMove(testMode.chooseMove());
            }
        }
        repaint();
    }


    /**
     * Prompts the user to select the game mode: 2 Players or 1 Player.
     * Sets the cpuMode object of ComputerPlayer based on the user's choice.
     */
    private void selectComputerMode() {
        String[] options = new String[]{"2 Players", "1 Player"};
        String message = "Select the game mode:";
        int playerChoice = JOptionPane.showOptionDialog(null, message,
                "Choose player type",
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
                null, options, options[0]);
        switch (playerChoice) {
            case 0:
                cpuMode = null;
                break;
            case 1:
                cpuMode = new ComputerPlayer(board);
                break;
        }
    }
}
