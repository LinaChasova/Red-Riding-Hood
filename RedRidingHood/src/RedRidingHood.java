/**
 * Created by AlinaCh on 12.09.2017.
 */
public class RedRidingHood {

    private int basket; //the amount of berries in the basket
    private Coordinate girl;    //current location of the girl
    static final String name = "R";  //name of the girl on the map
    int cost;   //for A* algorithm
    int g;  //for A* algorithm
    RedRidingHood parent;   //parent for A* algorithm
    Coordinate woodcutter;  //known location of the woodcutter
    Coordinate granny;  //known location of the granny
    Coordinate wolf;    //known location of the wolf
    Coordinate bear;    //known location of the bear
    Coordinate workplace;   //known location of the cutter's work
    Coordinate house;   //known location of the cutter's house

    /**
     * default initializer for thr Red Riding Hood object
     * @param c_g known location of the granny
     * @param w known location of the woodcutter's work
     * @param h known location of the woodcutter's house
     */
    RedRidingHood(Coordinate c_g, Coordinate w, Coordinate h) {
        basket = 6;
        girl = new Coordinate(0,0);
        granny = c_g;
        workplace = w;
        house = h;
        cost = 0;
        g = 0;
    }

    /**
     * copy constructor with new coordinate initializing
     * @param new_c new coordinate
     * @param girl object of the Red Riding Hood which should be copied
     */
    RedRidingHood(Coordinate new_c, RedRidingHood girl) {
        this.basket = girl.basket;
        this.girl = new_c;
        this.woodcutter = girl.woodcutter;
        this.granny = girl.granny;
        this.wolf = girl.wolf;
        this.bear = girl.bear;
        this.workplace = girl.workplace;
        this.house = girl.house;
    }

    /**
     * the copy constructor which copies the red riding hood object
     * @param girl
     */
    RedRidingHood(RedRidingHood girl) {
        this.basket = girl.basket;
        this.girl = new Coordinate(girl.getLocation());
        this.granny = new Coordinate(girl.granny);
        this.workplace = new Coordinate(girl.workplace);
        this.house = new Coordinate(girl.house);
    }

    /**
     * @return the location of the girl
     */
    public Coordinate getLocation() {
            return girl;
    }

    /**
     * makes a step up
     * @return girl with new coordinate
     */
    public RedRidingHood up() {
        return new RedRidingHood(this.girl.up(), this);
    }

    /**
     * makes step down
     * @return girl with new coordinate
     */
    public RedRidingHood down() {
        return new RedRidingHood(this.girl.down(), this);
    }

    /**
     * makes step left
     * @return girl with new coordinate
     */
    public RedRidingHood left() {
        return new RedRidingHood(this.girl.left(), this);
    }

    /**
     * makes step right
     * @return girl with new coordinate
     */
    public RedRidingHood right() {
        return new RedRidingHood(this.girl.right(), this);
    }

    /**
     * loses berries when meets bear
     * @return
     */
    public void loseBerries() {
        basket -= 2;
    }

    /**
     * @return the amount of berries in the basket
     */
    public int getBerries() {
        return this.basket;
    }

    /**
     * @return whether Red Riding Hood lost all her berries
     */
    public boolean lostAllBerries() {
        return this.getBerries() <= 0;
    }

    /**
     * takes berries back from the woodcutter
     */
    public void gainBerries() {
        basket = 6;
    }
}
