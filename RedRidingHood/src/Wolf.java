import java.util.LinkedList;

/**
 * Created by AlinaCh on 12.09.2017.
 */
public class Wolf {

    private Coordinate wolf;    //the location of the wolf
    private LinkedList<Coordinate> detectionrange;  //detection range of the wolf for the red riding hood
    static final String name = "W"; //the name of the agent on the map

    /**
     * default constructor of the wolf
     * creates the random location and adds to the list the detection range coordinates
     */
    Wolf() {
        wolf = new Coordinate();
        detectionrange = new LinkedList<>();
        createRange();
    }

    /**
     * constructor for testing special maps
     * @param x
     * @param y
     */
    Wolf(int x, int y) {
        wolf = new Coordinate(x, y);
        detectionrange = new LinkedList<>();
        createRange();
    }

    /**
     * copy constructor which copies the Wolf object
     * @param other
     */
    Wolf(Wolf other) {
        this.wolf = new Coordinate(other.wolf);
        this.detectionrange = new LinkedList<>();
        createRange();
    }

    /**
     * creates range of the wolf
     */
    private void createRange() {
        detectionrange.add(wolf);
        detectionrange.add(wolf.up());
        detectionrange.add(wolf.down());
        detectionrange.add(wolf.left());
        detectionrange.add(wolf.right());
    }

    /**
     * @return the detection range
     */
    public LinkedList<Coordinate> getWolfRange() {
        return detectionrange;
    }

    /**
     * @return the location of the wolf
     */
    public Coordinate getLocation() {
        return wolf;
    }

    /**
     * kills the Red Riding Hood if she is the wolf detection range
     * @param g girl
     * @return if the girl is dead
     */
    public boolean killGirl(RedRidingHood g) {
        return checkForGirl(g.getLocation());
    }

    /**
     * checks for Red Riding Hood
     * @param g girl
     * @return returns whether girl in the wolf detection range
     */
    public boolean checkForGirl(Coordinate g) {
        for (Coordinate w : this.getWolfRange()) {
            if (g.equals(w))
                return true;
        }
        return false;
    }
}
