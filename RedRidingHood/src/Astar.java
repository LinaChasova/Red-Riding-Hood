import java.io.FileNotFoundException;
import java.util.LinkedList;

import static java.lang.Math.abs;

/**
 * Created by AlinaCh on 13.09.2017.
 */
public class Astar {
    Map map;    //environment of the game
    LinkedList<RedRidingHood> closed;   //the cells, which red riding hood went through, while searching for the granny
    boolean nfound; //is granny found or not
    private RedRidingHood girllast; //last position of the girl
    private RedRidingHood nearbear; //last position near bear

    /**
     * the special constructor takes the generated map and solves the game
     * if the algorithm does not find the granny on the first try
     * it takes the last position of the red riding hood near the bear
     * and tries to go through him
     * if it is successful it goes to find woodcutter (tries work at the beginning, then house)
     * if she gains back her berries red riding hood goes to find granny
     * @param m the map to copy
     */
    Astar(Map m) throws FileNotFoundException {
        map = new Map(m);
        nfound = algorithm(map.girl, map.granny.getLocation());
        if (nfound) {
            reportPath(map.girl, map.girl.granny);
            System.out.println("YOU WIN!");
        } else {
            if (map.levelgame) {
                if (nearbear != null) {
                    girllast = new RedRidingHood(nearbear.getLocation(), girllast);
                    makeKnown();
                    goThroughBear();
                } else
                    map.levelgame = false;
            }
            if (!girllast.lostAllBerries() && map.levelgame) {
                map.girl = girllast;
                System.out.println(map.girl.getBerries());
                nfound = algorithm(map.girl, map.girl.house);
                if (nfound && girllast.getBerries() == 6) {
                    reportPath(map.girl, map.girl.house);
                    map.girl = girllast;
                    nfound = algorithm(map.girl, map.girl.granny);
                    if (nfound) {
                        reportPath(map.girl, map.girl.granny);
                        System.out.println("You win!");
                    }
                } else if (!nfound || girllast.getBerries() < 6) {
                    System.out.println(map.girl.getBerries());
                    map.girl = girllast;
                    nfound = algorithm(map.girl, map.girl.workplace);
                    if (nfound) {
                        reportPath(map.girl, map.girl.workplace);
                        map.girl = girllast;
                        nfound = algorithm(map.girl, map.girl.granny);
                        if (nfound) {
                            reportPath(map.girl, map.girl.granny);
                            System.out.println("You win!");
                        }
                    }
                } else
                    System.out.println("You lose!");
            } else
                System.out.println("You lose");
        }
        System.out.println("It took " + map.countSteps() + " steps");
    }

    /**
     * it searches the all possible steps and counts the cost to get there,
     * it takes the step with minimum cost and searches again, until the
     * fastest road is found
     * @param start the starter location of the red riding hood
     * @param find  the destination to be found
     * @return if the girl found the destination
     */
    boolean algorithm(RedRidingHood start, Coordinate find) {
        closed = new LinkedList<>();
        LinkedList<RedRidingHood> open = new LinkedList<>();
        open.add(start);

        while (!open.isEmpty()) {
            RedRidingHood current = minCost(open);  //the next step with minimum cost
            girllast = current;
            open.remove(current);   //removes from the open cells to go
            //if finds granny and has full basket, returns true
            if (map.granny.tookBerries(current)) {
                closed.add(current);
                return true;
            }
            current = map.bear.eatBerries(current);   //if goes through bear loses berries
            current = map.woodcutter.giveBerries(current);    //if goes through woodcutter gains her berries back
            //if girl is on the wolf detection range, wolf kills her
            if (map.wolf.killGirl(current)) {
                map.levelgame = false;
                return false;
            }
            //if girl lost all of her berries, she loses
            if (current.lostAllBerries()) {
                map.levelgame = false;
                return false;
            }
            //if finds the object returns true
            if (current.getLocation().equals(find)) {
                closed.add(current);
                return true;
            }
            //the list of all possible steps (up, down, left, right)
            LinkedList<RedRidingHood> neighbour = new LinkedList<>();
            neighbour.add(current.up());
            neighbour.add(current.right());
            neighbour.add(current.down());
            neighbour.add(current.left());
            //for all possible steps steps, checks if the step is possible
            //(does not go out of borders, does not intersects with wolf or bear)
            //puts new cost to the steps, checks if the step is already in the
            //set of possible steps, if not put it there, if yes, checks whether
            //new cost is less than the current, if yes, updates it
            for (RedRidingHood girl : neighbour) {
                if (!contains(closed, girl) && checkPoint(girl)) {
                    girl.g = current.g + 1;
                    girl.cost = girl.g + heuristic(girl,find);
                    girl.parent = current;
                    if (!contains(open, girl)) {
                        open.add(girl);
                    } else {
                        if (girl.cost < returnFrom(open, girl.getLocation()).cost) {
                            open.remove(returnFrom(open,girl.getLocation()));
                            open.add(girl);
                        }
                    }
                }
            }
            closed.add(current);
        }
        return false;
    }

    /**
     * checks whether the point does not go out of borders,
     * intersects with wold or bear detection ranges
     */
    public boolean checkPoint(RedRidingHood check) {
        if (map.checkBorder(check.getLocation())) {
            if (map.bear.checkForGirl(check.getLocation())) {
                if (!map.bear.checkForGirl(check.getLocation().left())
                        && map.checkBorder(check.left().getLocation()))
                    nearbear = new RedRidingHood(check.getLocation().left(), check);
                if (!map.bear.checkForGirl(check.getLocation().right())
                        && map.checkBorder(check.right().getLocation()))
                    nearbear = new RedRidingHood(check.getLocation().right(), check);
                if (!map.bear.checkForGirl(check.getLocation().up())
                        && map.checkBorder(check.up().getLocation()))
                    nearbear = new RedRidingHood(check.getLocation().up(), check);
                if (!map.bear.checkForGirl(check.getLocation().down())
                        && map.checkBorder(check.down().getLocation()))
                    nearbear = new RedRidingHood(check.getLocation().down(), check);
                map.addToMap(check.getLocation(), "S");
                return false;
            }
            if (map.wolf.checkForGirl(check.getLocation())) {
                map.addToMap(check.getLocation(), "N");
                return false;
            }
            return true;
        }
        return false;
    }

    /**
     * prints the shortest path generated by A* algorithm
     * @param start the starter location
     * @param find  the destination
     */
    private void reportPath(RedRidingHood start, Coordinate find){
        RedRidingHood finish = returnFrom(closed, find);
        while (!finish.getLocation().equals(start.getLocation())) {
            map.addToMap(finish.getLocation(), "R");
            finish = finish.parent;
        }
        map.printMap();
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
     * counts the heuristic function for the taking the step
     * @param g the location for which to count the heuristic function
     * @param find the destination location
     * @return the value
     */
    private int heuristic(RedRidingHood g, Coordinate find) {
        int h = abs(g.getLocation().x - find.x) + abs(g.getLocation().y - find.y);
        return h;
    }

    /**
     * @param c closed list
     * @param g location to check
     * @return whether the location is already in the closed list
     */
    private boolean contains(LinkedList<RedRidingHood> c, RedRidingHood g) {
        for (RedRidingHood cur : c) {
            if (cur.getLocation().equals(g.getLocation()))
                return true;
        }
        return false;
    }

    /**
     * @param c closed list
     * @param find the location
     * @return information for the location in the closed list
     */
    private RedRidingHood returnFrom(LinkedList<RedRidingHood> c, Coordinate find) {
        for (RedRidingHood cur : c) {
            if (cur.getLocation().equals(find))
                return cur;
        }
        return null;
    }

    /**
     * @param open list
     * @return the coordinate with the minimum cost
     */
    private RedRidingHood minCost(LinkedList<RedRidingHood> open) {
        RedRidingHood min = open.peek();
        for (RedRidingHood r : open) {
            if (r.cost < min.cost)
                min = r;
        }
        return min;
    }

    /**
     * makes the map known after the first algorithm, when it didn't find granny
     */
    private void makeKnown() {
        for (RedRidingHood g : closed) {
            map.addToMap(g.getLocation(), ".");
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
     * return
     */
    private RedRidingHood move(RedRidingHood g) {
        map.addToMap(g.getLocation(), g.name);
        g = map.bear.eatBerries(g);
        return g;
    }
}
