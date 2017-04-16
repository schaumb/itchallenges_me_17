package solution.ground;

import java.util.HashMap;

/**
 * Created by ecosim on 4/10/17.
 */
public class GroundFactory {
    private final HashMap<Key, GroundCounter> grounds = new HashMap<>();

    private GroundFactory() {
    }

    public static GroundFactory getInstance() {
        return SingletonHelper.INSTANCE;
    }

    public Ground getGround(boolean finite, boolean cyclic, int sizeX, int sizeY, long seed) {
        return grounds.computeIfAbsent(new Key(finite, cyclic, sizeX, sizeY, seed),
                k -> new GroundCounter(finite ? new FiniteGround(seed, cyclic, sizeX, sizeY) :
                        new InfiniteGround(seed)))
                .addUser().getGround();
    }

    public void removeGround(Ground g) {
        grounds.entrySet().removeIf(e ->
                e.getValue().getGround() == g && e.getValue().release().removable()
        );
    }

    private static class SingletonHelper {
        private static final GroundFactory INSTANCE = new GroundFactory();
    }

    private class Key {
        private final boolean finite;
        private final boolean cyclic;
        private final int sizeX;
        private final int sizeY;
        private final long seed;

        private Key(boolean finite, boolean cyclic, int sizeX, int sizeY, long seed) {
            this.finite = finite;
            this.cyclic = cyclic;
            this.sizeX = sizeX;
            this.sizeY = sizeY;
            this.seed = seed;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Key key = (Key) o;

            if (finite != key.finite) return false;
            if (finite) {
                if (cyclic != key.cyclic) return false;
                if (sizeX != key.sizeX) return false;
                if (sizeY != key.sizeY) return false;
            }
            return seed == key.seed;
        }

        @Override
        public int hashCode() {
            int result = (finite ? 1 : 0);
            if (finite) {
                result = 31 * result + (cyclic ? 1 : 0);
                result = 31 * result + sizeX;
                result = 31 * result + sizeY;
            }
            result = 31 * result + (int) (seed ^ (seed >>> 32));
            return result;
        }
    }

    private class GroundCounter {
        private final Ground ground;
        private int usersCount;

        public GroundCounter(Ground ground) {
            this.ground = ground;
            this.usersCount = 0;
        }

        public Ground getGround() {
            return ground;
        }

        public synchronized GroundCounter addUser() {
            ++usersCount;
            return this;
        }

        public synchronized GroundCounter release() {
            --usersCount;
            return this;
        }

        public synchronized boolean removable() {
            return usersCount <= 0;
        }
    }
}
