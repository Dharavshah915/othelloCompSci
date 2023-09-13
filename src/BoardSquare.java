import java.awt.*;

public class BoardSquare {
    Coordinate coordinate;
    int width;
    int height;
    private int BoardSquareState;
    private boolean highlight;

    /**
     * Describes a singular square on the board
     * @param coordinate of the top left corner
     * @param width
     * @param height
     */
    public BoardSquare(Coordinate coordinate, int width, int height) {
        this.coordinate = coordinate;
        this.width = width;
        this.height = height;
        setBoardSquareState(0);
    }

    /**
     * Set new state for a BoardSquare
     * @param newState as either 0,1,2,3
     *                 0 - empty
     *                 1 - black
     *                 2 - white
     */
    public void setBoardSquareState(int newState) {
        this.BoardSquareState = newState;
    }

    /**
     * get state of a BoardSquare
     * @return state of BoardSquare
     */
    public int getBoardSquareState() {
        return BoardSquareState;
    }

    /**
     * Sets the highlight state of the GamePanel.
     *
     * @param highlight true to enable highlighting, false to disable highlighting
     */
    public void setHighlight(boolean highlight) {
        this.highlight = highlight;
    }

    /**
     * Draws either a White or Black oval as necessary.
     *
     * @param g Reference to the Graphics object for rendering.
     */
    public void paint(Graphics g) {
        if(highlight) {
            g.setColor(new Color(255, 187, 22, 203));
            g.fillRect(coordinate.x, coordinate.y, width, height);
        }
        if(BoardSquareState == 0) return;
        g.setColor(BoardSquareState == 1 ? Color.BLACK : Color.WHITE);
        g.fillOval(coordinate.x, coordinate.y, width, height);
    }
}
