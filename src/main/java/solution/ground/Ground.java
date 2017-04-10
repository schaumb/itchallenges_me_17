package solution.ground;

import solution.random.TwoDimRng;
import solution.utils.Cell;
import solution.utils.Color;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by qqcs on 06/04/17.
 */
public abstract class Ground {
    private final TwoDimRng random;

    protected Ground(long seed) {
        this.random = new TwoDimRng(seed);
    }

    protected TwoDimRng getRandom() {
        return random;
    }

    public List<List<Cell>> getCells(int time, int fromX, int toX, int fromY, int toY) {
        List<List<Cell>> lines = new ArrayList<>(toY - fromY);
        for(int y = fromY; y < toY; ++y) {
            lines.add(getCells(time, fromX, toX, y));
        }
        return lines;
    }

    protected List<Cell> getCells(int time, int fromX, int toX, int y) {
        List<Cell> line = new ArrayList<>(toX - fromX);
        for(int x = fromX; x < toX; ++x) {
            line.add(getCell(time, x, y));
        }
        return line;
    }

    protected abstract Cell getCell(int time, int x, int y);

    // the rule
    protected Cell createCellByHistory(int time, int x, int y) {
        List<List<Cell>> getPrevCells = getCells(time - 1, x - 1, x + 2, y - 1, y + 2);

        Cell previous = getPrevCells.get(1).get(1);

        List<Cell> liveCells = getPrevCells.stream()
                .flatMap(Collection::stream)
                .filter(Cell::isLive)
                .collect(Collectors.toList());

        int liveCellsCount = liveCells.size();
        if(previous.isLive()) {
            switch (liveCellsCount) {
                // live
                case 3: // +1 because of previous calculated
                case 4:
                    return previous;
                // dead
                default:
                    return Cell.getCell(false, previous.getColor());
            }
        } else if(liveCellsCount == 3) {
            // born
            Color newColor = liveCells.stream()
                    .collect(Collectors.groupingBy(Cell::getColor, Collectors.counting()))
                    .entrySet().stream().max(Comparator.comparingLong(Map.Entry::getValue))
                    .get().getKey();

            return Cell.getCell(true, newColor);
        } else {
            return previous;
        }
    }
}
