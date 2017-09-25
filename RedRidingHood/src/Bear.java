import java.util.LinkedList;

/**
 * Created by AlinaCh on 12.09.2017.
 */
public class Bear {

    private Coordinate bear;    //the location of the bear
    private LinkedList<Coordinate> detectionrange;  //detection range of the bear for the red riding hood
    static final String name = "B"; //the name of the agent on the map

    /**
     * default constructor of the bear
     * creates the random location and adds to the list the detection range coordinates
     */
    Bear() {
        bear = new Coordinate();
        detectionrange = new LinkedList<>();
        createRange();
    }

    /**
     * constructor for testing special maps
     * @param x
     * @param y
     */
    Bear(int x, int y) {
        bear = new Coordinate(x, y);
        detectionrange = new LinkedList<>();
        createRange();
    }

    /**
     * copy constructor of the Bear object
     * @param other
     */
    Bear(Bear other) {
        this.bear = new Coordinate(other.getLocation());
        this.detectionrange = new LinkedList<>();
        createRange();
    }

    /**
     * creates range of the bear
     */
    private void createRange() {
        detectionrange.add(bear);
        detectionrange.add(bear.up());
        detectionrange.add(bear.down());
        detectionrange.add(bear.left());
        detectionrange.add(bear.right());
        detectionrange.add(bear.downLeft());
        detectionrange.add(bear.downRight());
        detectionrange.add(bear.upLeft());
        detectionrange.add(bear.upRight());
    }

    /**
     * @return the detection range
     */
    public LinkedList<Coordinate> getBearRange()
    {
        return detectionrange;
    }

    /**
     * @return the location of the bear
     */
    public Coordinate getLocation() {
        return bear;
    }

    /**
     * eats berries of the girl
     * @param g the girl
     * @return the girl with new amount of berries
     */
    public RedRidingHood eatBerries(RedRidingHood g) {
        if (checkForGirl(g.getLocation())) {
            g.loseBerries();
            return g;
        }
        return g;
    }

    /**
     * checks for Red Riding Hood
     * @param g girl
     * @return returns whether girl in the bear detection range
     */
    public boolean checkForGirl(Coordinate g) {
        for (Coordinate b : this.getBearRange()) {
            if (g.equals(b))
                return true;
        }
        return false;
    }
}
