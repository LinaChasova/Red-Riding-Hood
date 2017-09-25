import java.io.FileNotFoundException;
import java.io.PrintWriter;

/**
 * Created by AlinaCh on 12.09.2017.
 */
public class Map {

    private String[][] map; //the two-dimensional map of strings, which represents the location of all agents
    RedRidingHood girl; //object for the Red Riding Hood agent
    Bear bear; //object for the Bear agent
    Wolf wolf;  //object for the Wolf agent
    Granny granny;  //object for the Granny agent
    Woodcutter woodcutter; // object for the Woodcutter agent
    boolean levelgame;  //level of the game

    /**
     * default creation of the map
     * prints the map after creation
     */
    Map() {
        map = new String[9][9];
        createMap();
        addsToMap();
    }

    /**
     * Copy constructor of the map,
     * creates the complete copy of the generated map
     * @param another map from which to copy
     */
    Map(Map another) {
        this.map = new String[9][9];
        this.girl = new RedRidingHood(another.girl);
        this.bear = new Bear(another.bear); //object for the Bear agent
        this.wolf = new Wolf(another.wolf);  //object for the Wolf agent
        this.granny = new Granny(another.granny);  //object for the Granny agent
        this.woodcutter = new Woodcutter(another.woodcutter); // object for the Woodcutter agent
        this.levelgame = another.levelgame;
        addsToMap();
    }

    /**
     * special constructor to create specialized map for testing
     * @param b bear
     * @param w wolf
     * @param g granny
     * @param wc woodcutter
     */
    Map(Bear b, Wolf w, Granny g, Woodcutter wc) {
        map = new String[9][9];
        this.bear = b;
        this.wolf = w;
        this.granny = g;
        this.woodcutter = wc;
        createRRH();
        addsToMap();
        levelgame = true;
        printMap();
    }

    /**
     * creates bear, wolf, woodcutter, granny and red riding hood
     */
    private void createMap() {
        createBear();
        createWolf();
        createWoodcutter();
        createGranny();
        createRRH();
        levelgame = true;
    }

    /**
     * adds all agents to the map
     */
    private void addsToMap() {
        addToMap(bear.getLocation(), bear.name);
        addToMap(wolf.getLocation(), wolf.name);
        addToMap(woodcutter.work, woodcutter.n_work);
        addToMap(woodcutter.house, woodcutter.n_house);
        addToMap(woodcutter.getLocation(), woodcutter.name);
        addToMap(granny.getLocation(), granny.name);
        addToMap(girl.getLocation(), girl.name);
    }

    /**
     * creates bear, checks if its detection range intersects with red riding hood
     */
    private void createBear() {
        bear = new Bear();
        while (bear.checkForGirl(new Coordinate(0,0)))
            bear = new Bear();
    }

    /**
     * creates wolf, checks if its detection range intersects with red riding hood
     * checks if wolf is in the same cell with bear
     */
    private void createWolf() {
        wolf = new Wolf();
        while (wolf.getLocation().equals(bear.getLocation()) ||wolf.checkForGirl(new Coordinate(0,0)))
            wolf = new Wolf();
    }

    /**
     * creates woodcutter, its work and house
     * checks if all this coordinates intersects with detection ranges for red riding hood
     * of wolf and bear, and whether it is in the same cell as red riding hood
     */
    private void createWoodcutter() {
        woodcutter = new Woodcutter();
        while (bear.checkForGirl(woodcutter.work)
                || bear.checkForGirl(woodcutter.house)
                || wolf.checkForGirl(woodcutter.work)
                || wolf.checkForGirl(woodcutter.house)
                || woodcutter.house.equals(new Coordinate(0,0))
                || woodcutter.work.equals(new Coordinate(0,0))) {
            woodcutter = new Woodcutter();
        }
    }

    /**
     * creates granny
     * checks if she intersects with detection ranges for red riding hood
     * of wolf and bear, and whether it is in the same cell as red riding hood
     * or woodcutter's work or woodcutter's house
     */
    private void createGranny() {
        granny = new Granny();
        while (bear.checkForGirl(granny.getLocation())
                || wolf.checkForGirl(granny.getLocation())
                || granny.getLocation().equals(new Coordinate(0,0))
                || granny.getLocation().equals(woodcutter.house)
                || granny.getLocation().equals(woodcutter.work))
            granny = new Granny();
    }

    /**
     * creates red riding hood
     */
    private void createRRH() {
        girl = new RedRidingHood(granny.getLocation(), woodcutter.work, woodcutter.house);
    }

    /**
     * method to add coordinate to the map
     * @param check coordinate to be added
     * @param s the "name" of the agent
     */
    public void addToMap(Coordinate check, String s) {
        if (checkBorder(check))
            map[check.x][check.y] = s;
    }

    /**
     * @param check coordinate to check
     * @return whether the coordinate is not out of borders of the map
     */
    public boolean checkBorder(Coordinate check) {
        return check.x >= 0 && check.x < 9 && check.y >= 0 && check.y < 9;
    }

    /**
     * @param check the coordinate to check
     * @return whether the red riding hood was already in this cell
     */
    public boolean wasThere(Coordinate check) {
        return map[check.x][check.y] == "R";
    }

    /**
     * @param check coordinate
     * @return the contents of the cell
     */
    public String getName(Coordinate check) {
        return map[check.x][check.y];
    }

    /**
     * clears all the path after the algorithm
     */
    public void clearPath() {
        for (int i = 0; i < 9; i++)
            for (int j = 0; j < 9; j++)
                if (map[i][j] == "R")
                    map[i][j] = ".";
    }

    /**
     * prints the generated map
     */
    public void printMap(){
        for (int i = 0; i < 11; i++) {
            if (i == 0)
                System.out.println("  012345678");
            else if (i == 1)
                System.out.println(" ----------");
            else {
                for (int j = 0; j < 11; j++) {
                    if (j == 0)
                        System.out.print(i - 2);
                    else if (j == 1)
                        System.out.print("|");
                    else if (j > 1) {
                        if (map[j - 2][i - 2] != null)
                            System.out.print(map[j - 2][i - 2]);
                        else
                            System.out.print("o");
                    }
                }
                System.out.println();
            }
        }
    }

    /**
     * count steps of the algorithm to the granny
     * @return
     */
    public int countSteps() {
        int count = 0;
        for (int i = 0; i < 9; i++)
            for (int j = 0; j < 9; j++) {
                if (map[i][j] == "R")
                    count++;
            }
         return count;
    }
}
