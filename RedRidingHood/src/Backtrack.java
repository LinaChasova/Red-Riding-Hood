import java.io.FileNotFoundException;
import java.util.LinkedList;

/**
 * Created by AlinaCh on 12.09.2017.
 */
public class Backtrack {

    public Map map; //environment of the game
    boolean nfound; //if granny is found on the first try
    private RedRidingHood girllast; //last position of the girl
    private RedRidingHood nearbear; //last position near bear

    /**
     * special constructor which takes generated map and plays the game
     * if the algorithm does not find the granny on the first try
     * it takes the last position of the red riding hood near the bear
     * and tries to go through him
     * if it is successful it goes to find woodcutter (tries work at the beginning, then house)
     * if she gains back her berries red riding hood goes to find granny
     * @param m
     */
    Backtrack(Map m) throws FileNotFoundException {
        map = new Map(m);
        nfound = true;
        nfound = algorithm(map.girl.granny);
        if (!nfound) {
            if (map.levelgame) {
                if (nearbear != null) {
                    map.clearPath();
                    map.printMap();
                    girllast = new RedRidingHood(nearbear.getLocation(), girllast);
                    goThroughBear();
                } else
                    map.levelgame = false;
            }
            if (!girllast.lostAllBerries() && map.levelgame) {
                map.girl = girllast;
                System.out.println(map.girl.getBerries());
                nfound = algorithm(map.girl.house);
                System.out.println(map.girl.getBerries());
                if (!nfound || map.girl.getBerries() < 6) {
                    map.girl = girllast;
                    nfound = algorithm(map.girl.workplace);
                }
                if (map.girl.getBerries() == 6) {
                    map.girl = girllast;
                    nfound = algorithm(map.girl.granny);
                }
                if (nfound)
                    System.out.println("You win!");
                else
                    System.out.println("You lose!");
            } else
                System.out.println("You lose");
        } else {
            map.printMap();
            System.out.println("You win!");
        }
        System.out.println("It took " + map.countSteps() + " steps");
    }

    /**
     * the backtracking algorithm
     * returns to the last position if there's no more steps
     * @param find the agent to be found
     * @return whether the algorithm is successful or not
     */
    public boolean algorithm(Coordinate find) {
        RedRidingHood rrh = map.girl;   //helps to return to the last position
        girllast = map.girl;    //last entered position
        //adds to the map the move of the girl
        map.addToMap(map.girl.getLocation(), map.girl.name);
        //if finds granny and has full basket, returns true
        if (map.granny.tookBerries(map.girl))
            return true;
        //if finds the object returns true
        if (map.girl.getLocation().equals(find))
            return true;
        map.girl = map.bear.eatBerries(map.girl);   //if goes through bear loses berries
        map.girl = map.woodcutter.giveBerries(map.girl);    //if goes through woodcutter gains her berries back
        //if girl is on the wolf detection range, wolf kills her
        if (map.wolf.killGirl(map.girl)) {
            map.levelgame = false;
            return false;
        }
        //if girl lost all of her berries, she loses
        if (map.girl.lostAllBerries()) {
            map.levelgame = false;
            return false;
        }
        //if red riding hood can go up she goes
        if (checkPoint(map.girl.up().getLocation())) {
            map.girl = map.girl.up();
            if (algorithm(find))
                return true;
        }
        //the list of all possible steps (up, down, left, right)
        LinkedList<RedRidingHood> neighbour = new LinkedList<>();
        neighbour.add(map.girl.up());
        neighbour.add(map.girl.right());
        neighbour.add(map.girl.down());
        neighbour.add(map.girl.left());

        for (RedRidingHood g : neighbour) {
            if (checkPoint(g.getLocation())) {
                map.girl = g;
                if (algorithm(find))
                    return true;
            }
        }
        //if there's no more step to take, put flag, that girl knows this cell
        map.addToMap(map.girl.getLocation(), ".");
        map.girl = rrh; //return to the last step
        //if she died or lost all of her berries return false
        if(!map.levelgame)
            return false;
        //if there's a dead end
        return false;
    }

    /**
     * method to take risk
     * the red riding hood finds on which side of her is the bear
     * than goes there and moves perpendicularly either "up" or "down"
     */
    public void goThroughBear() {
        if (map.checkBorder(girllast.right().getLocation())
                && map.bear.checkForGirl(girllast.right().getLocation())) {
            goRight();
        } else if (map.checkBorder(girllast.left().getLocation())
                && map.bear.checkForGirl(girllast.left().getLocation())) {
            goLeft();
        } else if (map.checkBorder(girllast.up().getLocation())
                && map.bear.checkForGirl(girllast.up().getLocation())) {
            goUp();
        } else if (map.checkBorder(girllast.down().getLocation())
                && map.bear.checkForGirl(girllast.down().getLocation())) {
            goDown();
        }
    }

    /**
     * method to check next possible location for validation
     * first, checks if the move does not go out of borders of the map
     * second, checks if there's a bear there. stores the cell near the bear
     * third, checks if there's a wolf there
     * @param check
     * @return true if this move does not worsen girl's situation
     */
    public boolean checkPoint(Coordinate check) {
        if (map.checkBorder(check)) {
            if (map.wasThere(check))
                return false;
            if (map.bear.checkForGirl(check)) {
                if (!map.bear.checkForGirl(check.left())
                        && map.checkBorder(check.left()))
                    nearbear = new RedRidingHood(check.left(), map.girl);
                if (!map.bear.checkForGirl(check.right())
                        && map.checkBorder(check.right()))
                    nearbear = new RedRidingHood(check.right(), map.girl);
                if (!map.bear.checkForGirl(check.up())
                        && map.checkBorder(check.up()))
                    nearbear = new RedRidingHood(check.up(), map.girl);
                if (!map.bear.checkForGirl(check.down())
                        && map.checkBorder(check.down()))
                    nearbear = new RedRidingHood(check.down(), map.girl);
                map.addToMap(check, "S");
                return false;
            }
            if (map.wolf.checkForGirl(check)) {
                map.addToMap(check, "N");
                return false;
            }
            return true;
        }
        return false;
    }

    /**
     * situation of the risk
     * if the bear is on the right side of the girl, goes there
     * checks if she knows the cells from up and down
     * goes either up or down where she does not the environment
     */
    private void goRight() {
        girllast = move(girllast.right());
        if (check(girllast.up().getLocation())) {
            girllast = move(girllast.up());
        } else if (check(girllast.down().getLocation())) {
            girllast = move(girllast.down());
        } else {
            boolean up = false, down = false;
            for (int i = girllast.getLocation().x; i < girllast.getLocation().x + 3; i++) {
                if (check(new Coordinate(i, girllast.getLocation().y + 3)))
                    up = true;
            }
            for (int i = girllast.getLocation().x; i < girllast.getLocation().x + 3; i++) {
                if (check(new Coordinate(i, girllast.getLocation().y - 3)))
                    down = true;
            }
            if (up) {
                girllast = move(girllast.up());
                girllast = move(girllast.up());
            }else if (down) {
                girllast = move(girllast.down());
                girllast = move(girllast.down());
            } else {
                girllast = move(girllast.right());
                girllast = move(girllast.right());
                girllast = move(girllast.right());
            }
        }
    }

    /**
     * situation of the risk
     * if the bear is on the left side of the girl, goes there
     * checks if she knows the cells from up and down
     * goes either up or down where she does not the environment
     */
    private void goLeft() {
        girllast = move(girllast.left());
        if (check(girllast.up().getLocation())) {
            girllast = move(girllast.up());
        } else if (check(girllast.down().getLocation())) {
            girllast = move(girllast.down());
        } else {
            boolean up = false, down = false;
            for (int i = girllast.getLocation().x; i > girllast.getLocation().x - 3; i--) {
                if (check(new Coordinate(i, girllast.getLocation().y + 3)))
                    up = true;
            }
            for (int i = girllast.getLocation().x; i > girllast.getLocation().x - 3; i--) {
                if (check(new Coordinate(i, girllast.getLocation().y - 3)))
                    down = true;
            }
            if (up) {
                girllast = move(girllast.up());
                girllast = move(girllast.up());
            }else if (down) {
                girllast = move(girllast.down());
                girllast = move(girllast.down());
            } else {
                girllast = move(girllast.left());
                girllast = move(girllast.left());
                girllast = move(girllast.left());
            }
        }
    }

    /**
     * situation of the risk
     * if the bear is on the up side of the girl, goes there
     * checks if she knows the cells from left and right
     * goes either left or right where she does not the environment
     */
    private void goUp() {
        girllast = move(girllast.up());
        if (check(girllast.left().getLocation())) {
            girllast = move(girllast.left());
        } else if (check(girllast.right().getLocation())) {
            girllast = move(girllast.right());
        } else {
            boolean left = false, right = false;
            for (int i = girllast.getLocation().y; i < girllast.getLocation().y + 3; i++) {
                if (check(new Coordinate(girllast.getLocation().x + 3, i)))
                    right = true;
            }
            for (int i = girllast.getLocation().x; i < girllast.getLocation().y + 3; i++) {
                if (check(new Coordinate(girllast.getLocation().x - 3, i)))
                    left = true;
            }
            if (right) {
                girllast = move(girllast.right());
                girllast = move(girllast.right());
            }else if (left) {
                girllast = move(girllast.left());
                girllast = move(girllast.left());
            } else {
                girllast = move(girllast.up());
                girllast = move(girllast.up());
                girllast = move(girllast.up());
            }
        }
    }

    /**
     * situation of the risk
     * if the bear is on the down side of the girl, goes there
     * checks if she knows the cells from left and right
     * goes either left or right where she does not the environment
     */
    private void goDown() {
        girllast = move(girllast.down());
        if (check(girllast.left().getLocation())) {
            girllast = move(girllast.left());
        } else if (check(girllast.right().getLocation())) {
            girllast = move(girllast.right());
        } else {
            boolean left = false, right = false;
            for (int i = girllast.getLocation().y; i < girllast.getLocation().y - 3; i--) {
                if (check(new Coordinate(girllast.getLocation().x + 3, i)))
                    right = true;
            }
            for (int i = girllast.getLocation().x; i < girllast.getLocation().y - 3; i--) {
                if (check(new Coordinate(girllast.getLocation().x - 3, i)))
                    left = true;
            }
            if (right) {
                girllast = move(girllast.right());
                girllast = move(girllast.right());
            }else if (left) {
                girllast = move(girllast.left());
                girllast = move(girllast.left());
            } else {
                girllast = move(girllast.down());
                girllast = move(girllast.down());
                girllast = move(girllast.down());
            }
        }
    }

    /**
     * situation of the risk
     * checks whether the coordinate goes out of detection range and does not intersects with
     * the wolf detection range and was not inspected by the girl
     * @param check
     * @return
     */
    private boolean check(Coordinate check) {
        return map.checkBorder(check)
                && !map.wolf.checkForGirl(check)
                && map.getName(check) == null;
    }

    /**
     * moves to the given direction, adds the flag to the map
     * checks if bear ate some berries
     * @param g
     * @return
     */
    private RedRidingHood move(RedRidingHood g) {
        map.addToMap(g.getLocation(), g.name);
        g = map.bear.eatBerries(g);
        return g;
    }
}
