public class Coordinate {

    public int x;
    public int y;

    /**
     * describes a coordinate
     * @param x
     * @param y
     */
    public Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * describes a coordinate that is the same as another coordinate
     * @param coordinateOfOther
     */
    public Coordinate(Coordinate coordinateOfOther) {
        this.x = coordinateOfOther.x;
        this.y = coordinateOfOther.y;
    }

    /**
     * get x value of coordinate
     * @return x value
     */
    public int getX(){
        return this. x;
    }

    /**
     * get y value of coordinate
     * @return y value
     */
    public int getY(){
        return this.y;
    }

    /**
     * set new coordinate values
     * @param x
     * @param y
     */
    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * add this to otherCoordinate
     * @param otherCoordinate
     */
    public void add(Coordinate otherCoordinate) {
        this.x += otherCoordinate.x;
        this.y += otherCoordinate.y;
    }

    /**
     * Checks if this is equal to another coordinate
     * @param o
     * @return
     */
    public boolean equals(Object o) {
        if (getClass() != o.getClass()) {
            return false;
        }
        Coordinate coordinate = (Coordinate) o;  // change type
        return x == coordinate.x && y == coordinate.y;
    }

    /**
     * presents coordinate as a string
     * @return string form of coordinate
     */
    public String toString() {
        return "("+ x +","+ y +")";
    }
}