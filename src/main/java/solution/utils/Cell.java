package solution.utils;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Objects;

/**
 * Created by qqcs on 06/04/17.
 */
public class Cell {
    private final boolean live;
    private final Color color;

    private Cell(boolean live, Color color) {
        this.live = live;
        this.color = color;
    }

    private final static HashMap<Object, Cell> STATIC_INSTANCES;

    static {
        STATIC_INSTANCES = new HashMap<>();

        for (short b = 0; b <= 1; ++b) {
            boolean live = b == 1;
            for (Color color : Color.values()) {
                Object key = Arrays.asList(live, color);

                STATIC_INSTANCES.put(key, new Cell(live, color));
            }
        }
    }

    public static Cell getCell(boolean live, Color color) {
        Object key = Arrays.asList(live,
                Objects.requireNonNull(color));

        return STATIC_INSTANCES.get(key);
    }

    public static Cell getNthCell(long n) {

        Collection<Cell> cells = STATIC_INSTANCES.values();
        return cells.stream().skip(n % cells.size()).findFirst().get();
    }

    public boolean isLive() {
        return live;
    }

    public Color getColor() {
        return color;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Cell cell = (Cell) o;

        if (live != cell.live) return false;
        return color == cell.color;
    }

    @Override
    public int hashCode() {
        int result = (live ? 1 : 0);
        result = 31 * result + color.hashCode();
        return result;
    }

    @Override
    public String toString() {
        String c = getColor().name().charAt(0) + "";
        return isLive() ? c : " ";
    }
}
