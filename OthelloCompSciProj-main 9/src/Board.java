import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Board {
    private Coordinate coordinate;
    private int width;
    private int height;
    public BoardSquare[][] grid;
    public List<Coordinate> validMoves;

    /**
     * Describes a board
     * @param coordinate of top left of the entire board
     * @param width of the entire board
     * @param height of the entire board
     * @param NumberOfHorizontalSquares on the board
     * @param NumberOfVerticalSquares on the board
     */
    public Board(Coordinate coordinate, int width, int height, int NumberOfHorizontalSquares, int NumberOfVerticalSquares) {
        this.coordinate = coordinate;
        this.width = width;
        this.height = height;

        grid = new BoardSquare[NumberOfHorizontalSquares][NumberOfVerticalSquares]; // create nested array of desired dimensions

        int BoardSquareWidth = width / NumberOfHorizontalSquares;  //width of one square for them to fit in the desired dimensions
        int BoardSquareHeight = height / NumberOfVerticalSquares;  //length of one square for them to fit in the desired dimensions
        int distanceXNeededforNewBoardSquare = 0;
        int distanceYNeededForNewBoardSquare = 0;
        for (int x = 0; x < NumberOfHorizontalSquares; x++) {
            for (int y = 0; y < NumberOfVerticalSquares; y++) {
                distanceXNeededforNewBoardSquare = BoardSquareWidth * x; //x position of new BoardSquare
                distanceYNeededForNewBoardSquare = BoardSquareWidth * y; // y position of new BoardSquare
                Coordinate CoordinateOfNewSquare = new Coordinate(coordinate.x + distanceXNeededforNewBoardSquare, coordinate.y + distanceYNeededForNewBoardSquare);
                grid[x][y] = new BoardSquare(CoordinateOfNewSquare, BoardSquareWidth, BoardSquareHeight);
            }
        }
        grid[NumberOfHorizontalSquares/2][NumberOfVerticalSquares/2].setBoardSquareState(1);
        grid[NumberOfHorizontalSquares/2 -1][NumberOfVerticalSquares/2].setBoardSquareState(2);
        grid[NumberOfHorizontalSquares/2][NumberOfVerticalSquares/2 -1].setBoardSquareState(2);
        grid[NumberOfHorizontalSquares/2-1][NumberOfVerticalSquares/2-1].setBoardSquareState(1);

        validMoves = new ArrayList<>();
        updateValidMoves(1);
    }

    /**
     * make every square on board blank
     */
    public void reset() {
        for (int x = 0; x < grid.length; x++) {
            for (int y = 0; y < grid[0].length; y++) {
                grid[x][y].setBoardSquareState(0);
            }
        }
        updateValidMoves(1);
    }

    /**
     * get grid
     * @return grid
     */
    public BoardSquare[][] getGrid() {
        return grid;
    }

    /**
     * get valid moves
     * @return valid moves
     */
    public List<Coordinate> getEveryValidMove() {
        return validMoves;
    }

    /**
     * takes player's selection and plays the move
     * @param coordinate of square chosen
     * @param player "which player"
     */
    public void playMove(Coordinate coordinate, int player) {
        grid[coordinate.x][coordinate.y].setBoardSquareState(player);
        List<Coordinate> changeCellCoordinates = getStateChangesForAllDirections(coordinate, player);
        for (Coordinate swapCoordinate : changeCellCoordinates) {
            grid[swapCoordinate.x][swapCoordinate.y].setBoardSquareState(player);
        }
        if (player == 1){
            updateValidMoves(2);
        }
        else{
            updateValidMoves(1);
        }
    }

    /**
     * converts position of cursor to a square on the board
     * @param mouseCoordinate position of cursor
     * @return where on the board the mouse was clicked
     */
    public Coordinate convertMouseToGridPosition(Coordinate mouseCoordinate) {
        int gridX = mouseCoordinate.x / grid[0][0].width; //to get a whole number of the x cord
        int gridY = mouseCoordinate.y / grid[0][0].height; // to get a whole number for the y cord
        return new Coordinate(gridX, gridY);
    }

    /**
     * check if clicked square can actually be played
     * @param coordinate
     * @return
     */
    public boolean isValidMove(Coordinate coordinate) {
        return getEveryValidMove().contains(coordinate);
    }

    /**
     *
     * @param stillValidMoves
     * @return
     */
    public int getWinner(boolean stillValidMoves) {
        int[] counts = new int[3];
        for (int y = 0; y < grid[0].length; y++) {
            for (int x = 0; x < grid.length; x++) {
                counts[grid[x][y].getBoardSquareState()]++;
            }
        }

        if (stillValidMoves && counts[0] > 0) {
            return 0;
        } else if (counts[1] == counts[2]) {
            return 3;

        } else {
            return counts[1] > counts[2] ? 1 : 2;
        }
    }

    /**
     * draws board
     * @param g graphics object
     */
    public void paint(Graphics g) {
        drawGridLines(g);
        for (int x = 0; x < grid.length; x++) {
            for (int y = 0; y < grid[0].length; y++) {
                grid[x][y].paint(g);
            }
        }
    }

    /**
     * draws grid lines
     * @param g graphics object
     */
    private void drawGridLines(Graphics g) {
        g.setColor(Color.BLACK);
        // Draw vertical lines
        int y2 = coordinate.y+height;
        int y1 = coordinate.y;
        for(int x = 0; x < grid.length+1; x++)
            g.drawLine(coordinate.x+x * grid[0][0].width, y1, coordinate.x+x * grid[0][0].width, y2);

        // Draw horizontal lines
        int x2 = coordinate.x+ width;
        int x1 = coordinate.x;
        for(int y = 0; y < grid[0].length+1; y++)
            g.drawLine(x1, coordinate.y+y * grid[0][0].height, x2, coordinate.y+y * grid[0][0].height);
    }

    /**
     * updates validMoves
     * @param playerID
     */
    public void updateValidMoves(int playerID) {
        // Remove all highlighted elements so they are not valid moves visually
        for(Coordinate validMove : validMoves) {
            grid[validMove.x][validMove.y].setHighlight(false);
        }
        validMoves.clear();
        for (int x = 0; x < grid.length; x++) {
            for (int y = 0; y < grid[0].length; y++) {
                if (grid[x][y].getBoardSquareState() == 0 && getStateChangesForAllDirections(new Coordinate(x,y),playerID).size()>0) {
                    validMoves.add(new Coordinate(x, y));
                }
            }
        }
//        }
        // Visually update all valid move positions to show with a highlight
        for(Coordinate validMove : validMoves) {
            grid[validMove.x][validMove.y].setHighlight(true);
        }
    }

    /**
     * check which squares if any require their state to be changed based off of the move played in one direction
     * @param coordinate "move played"
     * @param playerID "which player"
     * @param direction "check in which direction;
     * @return all squares that need to be changed;
     */
    private List<Coordinate> getStateChangesForMoveInDirection(Coordinate coordinate, int playerID, Coordinate direction) {
        List<Coordinate> result = new ArrayList<>();
        Coordinate movingPos = new Coordinate(coordinate);
        int otherPlayer;
        if (playerID == 1) {
            otherPlayer = 2;
        }
        else{
            otherPlayer = 1;
        }
        movingPos.add(direction);
        // Keep moving while there are positions that would be changed.
        while(inBounds(movingPos) && grid[movingPos.x][movingPos.y].getBoardSquareState() == otherPlayer) {
            result.add(new Coordinate(movingPos));
            movingPos.add(direction);
        }
        // If the end position is off the board, or the end playerID does not match the player, that
        // means that the move would not give any valid switches in this direction.
        if(!inBounds(movingPos) || grid[movingPos.x][movingPos.y].getBoardSquareState() != playerID) {
            result.clear();
        }
        return result;
    }

    /**
     * check which squares if any require their state to be changed based off of the move played in all diresctions
     * @param coordinate "move played"
     * @param playerID "which player"
     * @return all squares that need to be changed;
     */
    public List<Coordinate> getStateChangesForAllDirections(Coordinate coordinate, int playerID) {
        List<Coordinate> result = new ArrayList<>();
        result.addAll(getStateChangesForMoveInDirection(coordinate, playerID, new Coordinate(0,1))); //up
        result.addAll(getStateChangesForMoveInDirection(coordinate, playerID, new Coordinate(1,1))); //top right
        result.addAll(getStateChangesForMoveInDirection(coordinate, playerID, new Coordinate(1,0))); //right
        result.addAll(getStateChangesForMoveInDirection(coordinate, playerID, new Coordinate(1,-1))); //bottom right
        result.addAll(getStateChangesForMoveInDirection(coordinate, playerID, new Coordinate(0,-1))); // down
        result.addAll(getStateChangesForMoveInDirection(coordinate, playerID, new Coordinate(-1,-1))); //bottom left
        result.addAll(getStateChangesForMoveInDirection(coordinate, playerID, new Coordinate(-1,0))); // left
        result.addAll(getStateChangesForMoveInDirection(coordinate, playerID, new Coordinate(-1,1))); // top left

        return result;
    }

    /**
     * check if still on board;
     * @param coordinate that needs to be checked
     * @return boolean
     */
    private boolean inBounds(Coordinate coordinate) {
        return !(coordinate.x < 0 || coordinate.y < 0 || coordinate.x >= grid.length || coordinate.y >= grid[0].length);
    }
}