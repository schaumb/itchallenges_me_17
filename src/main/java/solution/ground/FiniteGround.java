package solution.ground;

import solution.utils.Cell;
import solution.utils.Color;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by qqcs on 07/04/17.
 */
public class FiniteGround extends Ground {
    private final static Cell OUTSIDE = Cell.getCell(false, Color.BLUE);
    private final boolean cyclic;
    private final int sizeX;
    private final int sizeY;
    private final ArrayList<List<List<Cell>>> timedFields = new ArrayList<>();

    public FiniteGround(long seed, boolean cyclic, int sizeX, int sizeY) {
        super(seed);
        this.cyclic = cyclic;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
    }


    private synchronized void generateToTime(int time) {
        if (timedFields.isEmpty()) {
            List<List<Cell>> firstField = IntStream.range(0, sizeY).parallel()
                    .mapToObj(y ->
                            IntStream.range(0, sizeX).parallel()
                                    .mapToObj(x ->
                                            Cell.getNthCell(Math.abs(getRandom().get(x, y))))
                                    .collect(Collectors.toList())
                    )
                    .collect(Collectors.toList());
            timedFields.add(firstField);
        }
        for (int t = timedFields.size(); t <= time; ++t) {
            List<List<Cell>> newField = new ArrayList<>(sizeY);
            for (int y = 0; y < sizeY; ++y) {
                List<Cell> line = new ArrayList<>(sizeX);
                newField.add(line);
                for (int x = 0; x < sizeX; ++x) {
                    line.add(createCellByHistory(t, x, y));
                }
            }
            timedFields.add(newField);
        }
    }

    @Override
    public List<List<Cell>> getCells(int time, int fromX, int toX, int fromY, int toY) {
        if (time >= timedFields.size()) {
            generateToTime(time);
        }

        if (cyclic) {
            // normalize the coordinates
            int diffY = Math.floorMod(fromY, sizeY) - fromY;
            fromY += diffY;
            toY += diffY;
            int diffX = Math.floorMod(fromX, sizeX) - fromX;
            fromX += diffX;
            toX += diffX;
        }

        // shortcut
        if (fromX == 0 && toX == sizeX) {
            if (0 <= fromY && toY <= sizeY) {
                return timedFields.get(time).subList(fromY, toY);
            }
        }

        return super.getCells(time, fromX, toX, fromY, toY);
    }

    @Override
    protected List<Cell> getCells(int time, int fromX, int toX, int y) {
        if (cyclic) {
            // normalize the coordinates
            y = Math.floorMod(y, sizeY);
            int diffX = Math.floorMod(fromX, sizeX) - fromX;
            fromX += diffX;
            toX += diffX;
        } else if (0 > y || y >= sizeY) {
            return Collections.nCopies(toX - fromX, OUTSIDE);
        }

        if (0 <= fromX && toX <= sizeX && 0 <= y && y < sizeY) {
            return timedFields.get(time).get(y).subList(fromX, toX);
        }

        return super.getCells(time, fromX, toX, y);
    }

    @Override
    protected Cell getCell(int time, int x, int y) {
        if (cyclic) {
            // normalize the coordinates
            y = Math.floorMod(y, sizeY);
            x = Math.floorMod(x, sizeX);
        } else if (0 > x || x >= sizeX || 0 > y || y >= sizeY) {
            return OUTSIDE;
        }

        return timedFields.get(time).get(y).get(x);
    }

    @Override
    public End tryToFindEnd() {
        HashMap<List<List<Cell>>, Integer> elements = new HashMap<>();
        int i = 0;
        while (true) {
            if (timedFields.size() <= i) {
                generateToTime(i);
            }
            Integer lastSame = elements.get(timedFields.get(i));
            if (lastSame != null) {
                return new End(lastSame, i - 1);
            }
            elements.put(timedFields.get(i), i);
            ++i;
        }
    }
}
