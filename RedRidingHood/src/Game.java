import java.io.FileNotFoundException;
import java.io.PrintWriter;

/**
 * Created by AlinaCh on 12.09.2017.
 */
public class Game {

    public Map map; //generate map
    public Backtrack backtrack; //backtrack algorithm
    public Astar astar; //A* algorithm

    /**
     * generates map of the game and chooses algorithm to solve it
     */
    Game() throws FileNotFoundException {
        PrintWriter print = new PrintWriter("output.txt");
        int a = 0;
        int b = 0;
        int c = 0;
        for (int i = 0; i < 1000; i++) {
            map = new Map();
            map.printMap();
            print.println("Test");
            System.out.println("SOLVING WITH BACKTRACK");
            long startb = System.nanoTime();
            backtrack = new Backtrack(map);
            long endb = System.nanoTime();
            print.println(endb - startb);
            System.out.println("\n\n\n\nSOLVING WITH ASTAR");
            long starta = System.nanoTime();
            astar = new Astar(map);
            long enda = System.nanoTime();
            print.println(enda - starta);
            if ((enda - starta) / 1000000 > (endb-startb) / 1000000)
                b++;
            else if ((enda - starta) / 1000000 < (endb-startb) / 1000000)
                a++;
            else
                c++;
        }
        print.println();
        print.println("A* works quicker " + a + " times");
        print.println();
        print.println("Backtrack works quicker " + b + " times");
        print.println("Algorithm works the same time  " + c + " times");
        print.close();
    }

}
