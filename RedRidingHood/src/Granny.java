/**
 * Created by AlinaCh on 12.09.2017.
 */
public class Granny {

    private Coordinate granny;  //location of the granny
    static final String name = "G"; //name of the agent on the map

    /**
     * default constructor of the granny
     */
    Granny() {
        granny = new Coordinate();
    }

    /**
     * special constructor for testing maps
     * @param x
     * @param y
     */
    Granny(int x, int y) {
        granny = new Coordinate(x, y);
    }

    /**
     * copy constructor which copies Granny object
     * @param other
     */
    Granny(Granny other) {
        this.granny = new Coordinate(other.getLocation());
    }

    /**
     * @return the location of the granny
     */
    public Coordinate getLocation() {
        return granny;
    }

    /**
     * @param g girl
     * @return whether the Red Riding Hood gave granny basket with 6 berries
     */
    public boolean tookBerries(RedRidingHood g) {
        return g.getLocation().equals(this.getLocation()) && g.getBerries() == 6;
    }
}
