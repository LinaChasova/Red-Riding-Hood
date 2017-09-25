import java.util.Random;

/**
 * Created by AlinaCh on 12.09.2017.
 */
public class Woodcutter {

    public Coordinate house;    //location of the woodcutter's house
    public Coordinate work; //location of the woodcutter's work
    private Coordinate cutter;   //location of the woodcutter (either at work or at home)
    static final String name = "C"; //the name of the agent on the map
    public String n_house = "H";    //the name of the woodcutter's house on the map, if he is at work
    public String n_work = "F"; //the name of the woodcutter's work on the map, if he is at home
    private Random random = new Random();   //helps to generate random location of the woodcutter

    /**
     * default constructor of the object woodcutter
     * creates the random location for the house and work
     * creates the random current location
     */
    Woodcutter() {
        int location;
        work = new Coordinate();
        house = new Coordinate();
        while (work.equals(house))
            house = new Coordinate();
        location = random.nextInt(2);
        if (location == random.nextInt(2))
            cutter = work;
        else
            cutter = house;
    }

    /**
     * special constructor for testing maps
     * @param hx
     * @param hy
     * @param wx
     * @param wy
     */
    Woodcutter(int hx, int hy, int wx, int wy) {
        work = new Coordinate(wx, wy);
        house = new Coordinate(hx, hy);
        cutter = work;
    }

    /**
     * copy constructor which copies Woodcutter
     * @param other
     */
    Woodcutter(Woodcutter other) {
        this.house = new Coordinate(other.house);
        this.cutter = new Coordinate(other.getLocation());
        this.work = new Coordinate(other.work);
    }

    /**
     * @return the location of the woodcutter
     */
    public Coordinate getLocation() {
        return cutter;
    }

    /**
     * returns Red Riding Hood all her berries back
     * @param g girl
     * @return girl with the full amount of berries
     */
    public RedRidingHood giveBerries(RedRidingHood g) {
        if (g.getLocation().equals(this.getLocation()) && g.getBerries() > 0) {
            g.gainBerries();
            g.woodcutter = cutter;
        }
        return g;
    }
}
