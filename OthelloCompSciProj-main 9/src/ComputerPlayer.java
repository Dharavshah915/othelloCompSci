import java.util.Random;

public class ComputerPlayer {

    private Board board;

    public ComputerPlayer(Board board) {
        this.board = board;
    }

    /**
     * Chooses a random valid move from the available moves on the board.
     *
     * @return The randomly selected valid move as a Coordinate object.
     */
    public Coordinate chooseMove() {
        Random random = new Random();
        int sizeMoves = board.getEveryValidMove().size();
        int randomIndex = random.nextInt(sizeMoves);
        return board.getEveryValidMove().get(randomIndex);
    }
}
