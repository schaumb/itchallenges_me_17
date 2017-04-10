package solution.ground;

import solution.random.TwoDimRng;
import solution.utils.Cell;

import java.util.HashMap;

/**
 * Created by qqcs on 06/04/17.
 */
public class InfiniteGround extends Ground {
    private  class Key {
        private final int time;
        private final int x;
        private final int y;

        private Key(int time, int x, int y) {
            this.time = time;
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Key key = (Key) o;

            if (time != key.time) return false;
            if (x != key.x) return false;
            return y == key.y;
        }

        @Override
        public int hashCode() {
            int result = time;
            result = 31 * result + x;
            result = 31 * result + y;
            return result;
        }
    }

    public InfiniteGround(long seed) {
        super(seed);
    }

    private final HashMap<Key, Cell> calculated_cells = new HashMap<>();

    @Override
    protected Cell getCell(int time, int x, int y) {
        return calculated_cells.computeIfAbsent(new Key(time, x, y), k -> createCell(time, x, y));
    }

    private Cell createCell(int time, int x, int y) {
        if(time == 0) {
            return Cell.getNthCell(Math.abs(getRandom().get(x, y)));
        } else {
            return createCellByHistory(time, x, y);
        }
    }
}
