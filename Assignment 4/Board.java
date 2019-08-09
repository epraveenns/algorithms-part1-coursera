
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class Board {

    private final int[][] blocks;

    public Board(int[][] blocks) {
        this.blocks = deepCopy(blocks);
    }

    // (where blocks[i][j] = block in row i, column j)
    public int dimension() {
        return blocks.length;
    }

    public int hamming()                   // number of blocks out of place
    {
        int count = 1;
        int hamming = 0;
        for (int i = 0; i < dimension(); i++) {
            for (int j = 0; j < dimension(); j++) {
                if (blocks[i][j] != count) {
                    hamming++;
                }
                count++;
            }
        }
        --hamming;
        return hamming;
    }

    public int manhattan()                 // sum of Manhattan distances between blocks and goal
    {
        int m = 0;
        for (int i = 0; i < dimension(); i++) {
            for (int j = 0; j < dimension(); j++) {
                int correctNumber = (i * dimension()) + j + 1;
                int currentNumber = blocks[i][j];
                if (currentNumber == correctNumber) {
                    continue;
                }
                if (currentNumber == 0) {
                    continue;
                }

                int correctX = (currentNumber - 1) / dimension();
                int correctY = currentNumber - 1 - (dimension() * correctX);

                m = m + Math.abs(i - correctX) + Math.abs(j - correctY);
            }
        }
        return m;
    }

    public boolean isGoal()                // is this board the goal board?
    {
        int count = 1;
        for (int i = 0; i < dimension(); i++) {
            for (int j = 0; j < dimension(); j++) {
                int k = blocks[i][j];

                if (k == 0) {
                    if (i == dimension() - 1 && j == dimension() - 1) {
                        continue;
                    }
                    return false;
                }
                if (k != count) {
                    return false;
                }
                count++;
            }
        }
        return true;
    }

    public Board twin()                    // a board that is obtained by exchanging any pair of blocks
    {
        int[][] clone = deepCopy(blocks);

        int count = 0;

        List<Integer> list = new ArrayList<>(4);
        for (int i = 0; i < dimension() && count < 2; i++) {
            for (int j = 0; j < dimension() && count < 2; j++) {
                if (clone[i][j] != 0) {
                    count++;
                    list.add(i);
                    list.add(j);
                }
            }
        }
        int temp = clone[list.get(0)][list.get(1)];
        clone[list.get(0)][list.get(1)] = clone[list.get(2)][list.get(3)];
        clone[list.get(2)][list.get(3)] = temp;

        return new Board(clone);
    }

    public boolean equals(Object y)        // does this board equal y?
    {
        if (y == null) {
            return false;
        }
        if (this == y) {
            return true;
        }
        if (!(y.getClass().equals(this.getClass()))) {
            return false;
        }
        Board comp = (Board) y;
        if (comp.dimension() != this.dimension()) {
            return false;
        }
        return Arrays.deepEquals(this.blocks, comp.blocks);
    }

    public Iterable<Board> neighbors()     // all neighboring boards
    {
        Iterable<Board> iterable = () -> {
            List<Board> neighbours = new ArrayList<>(4);
            int spaceI = 0;
            int spaceJ = 0;

            iLoop:
            for (int i = 0; i < dimension(); i++) {
                for (int j = 0; j < dimension(); j++) {
                    if (blocks[i][j] == 0) {
                        spaceI = i;
                        spaceJ = j;
                        break iLoop;
                    }
                }
            }
            if (spaceI - 1 > -1) {
                int[][] clone = deepCopy(blocks);
                int n = blocks[spaceI - 1][spaceJ];
                clone[spaceI - 1][spaceJ] = 0;
                clone[spaceI][spaceJ] = n;
                neighbours.add(new Board(clone));
            }
            if (spaceI + 1 < dimension()) {
                int[][] clone = deepCopy(blocks);
                int n = blocks[spaceI + 1][spaceJ];
                clone[spaceI + 1][spaceJ] = 0;
                clone[spaceI][spaceJ] = n;
                neighbours.add(new Board(clone));
            }
            if (spaceJ - 1 > -1) {
                int[][] clone = deepCopy(blocks);
                int n = blocks[spaceI][spaceJ - 1];
                clone[spaceI][spaceJ - 1] = 0;
                clone[spaceI][spaceJ] = n;
                neighbours.add(new Board(clone));
            }
            if (spaceJ + 1 < dimension()) {
                int[][] clone = deepCopy(blocks);
                int n = blocks[spaceI][spaceJ + 1];
                clone[spaceI][spaceJ + 1] = 0;
                clone[spaceI][spaceJ] = n;
                neighbours.add(new Board(clone));
            }

            return neighbours.iterator();
        };
        return iterable;
    }

    public String toString()               // string representation of this board (in the output format specified below)
    {
        StringBuilder sb = new StringBuilder();
        sb.append(dimension() + "\n");
        for (int i = 0; i < dimension(); i++) {
            for (int j = 0; j < dimension(); j++) {
                sb.append(String.format("%2d ", blocks[i][j]));
            }
            sb.append("\n");
        }

        return sb.toString();
    }

    private int[][] deepCopy(int[][] array) {
        int[][] ret = new int[array.length][array[0].length];
        for (int i = 0; i < ret.length; i++) {
            ret[i] = Arrays.copyOf(array[i], array[0].length);
        }
        return ret;
    }
}