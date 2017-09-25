import java.util.Random;

/**
 * Created by AlinaCh on 12.09.2017.
 * Class for the coordinates of the agents
 */
public class Coordinate {

    int x; //x coordinate
    int y; //y coordinate
    private Random random = new Random();   //helps to create random location of agents

    /**
     * default initializer of the coordinates of the agents,
     * default random creates integer number from 0 to 9
     */
    Coordinate() {
        this.x = random.nextInt(9);
        this.y = random.nextInt(9);
    }

    /**
     * special constructor, which takes two arguments
     * @param x the x coordinate
     * @param y the y coordinate
     */
    Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * copy constructor of the coordinate object
     * @param other
     */
    Coordinate(Coordinate other) {
        this.x = other.x;
        this.y = other.y;
    }

    /**
     * the override method of the typical method equals
     * @param other takes other coordinate as an argument
     * @return whether this coordinates is equal
     */
    public boolean equals(Coordinate other) {
        return this.x == other.x && this.y == other.y;
    }

    /**
     * @return the up coordinate
     */
    public Coordinate up() {
        return new Coordinate(this.x, this.y + 1);
    }

    /**
     * @return the down coordinate
     */
    public Coordinate down() {
        return new Coordinate(this.x, this.y - 1);
    }

    /**
     * @return the left coordinate
     */
    public Coordinate left() {
        return new Coordinate(this.x - 1, this.y);
    }

    /**
     * @return the right coordinate
     */
    public Coordinate right() {
        return new Coordinate(this.x + 1, this.y);
    }

    /**
     * @return the up right (diagonal) coordinate
     */
    public Coordinate upRight() {
        return new Coordinate(this.x + 1, this.y + 1);
    }

    /**
     * @return the up left (diagonal) coordinate
     */
    public Coordinate upLeft() {
        return new Coordinate(this.x - 1, this.y + 1);
    }

    /**
     * @return the down right (diagonal) coordinate
     */
    public Coordinate downRight() {
        return new Coordinate(this.x + 1, this.y - 1);
    }

    /**
     * @return the down left (diagonal) coordinate
     */
    public Coordinate downLeft() {
        return new Coordinate(this.x - 1, this.y - 1);
    }

}
