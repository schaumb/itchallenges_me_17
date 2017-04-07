package solution;

import solution.ground.FiniteGround;
import solution.ground.Ground;
import solution.ground.InfiniteGround;
import solution.utils.Cell;

import java.util.List;

/**
 * Created by qqcs on 06/04/17.
 */
public class EntryPoint {
    public static void main(String[] args) {
        /*boolean finite = false;
        boolean circular = false;
        long seed = 3242245245L;
        */
        Ground ig = new InfiniteGround(2344322);

        for(int i = 0; i < 150; ++i) {
            List<List<Cell>> cells = ig.getCells(i, 0, 30, 0, 30);
            System.out.println(i + ". tick:");
            System.out.println("/------------------------------\\");
            for(int x = 0; x < 30; ++x) {
                System.out.print("|");
                for(int y = 0; y < 30; ++y) {
                    System.out.print(cells.get(x).get(y));
                }
                System.out.println("|");
            }
            System.out.println("\\------------------------------/");
        }

    }
}
