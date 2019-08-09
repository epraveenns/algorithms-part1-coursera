/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.MinPQ;

import java.util.ArrayList;
import java.util.List;

public class Solver {
    private int moves = -1;
    private final List<Board> solution = new ArrayList<Board>();

    public Solver(
            Board initial)           // find a solution to the initial board (using the A* algorithm)
    {
        if(initial == null)
        {
            throw new IllegalArgumentException("Board cannot be null");
        }
        MinPQ<SearchNode> minPQ = new MinPQ<>();
        MinPQ<SearchNode> twinPQ = new MinPQ<>();

        SearchNode s = new SearchNode(null, initial, 0);
        SearchNode twinS = new SearchNode(null, initial.twin(), 0);

        minPQ.insert(s);
        twinPQ.insert(twinS);

        while (true) {
            s = minPQ.delMin();
            solution.add(s.node);

            twinS = twinPQ.delMin();

            if (s.node.isGoal()) {
                moves = s.move;
                break;
            }
            else if (twinS.node.isGoal()) {
                break;
            }

            for (Board board : s.node.neighbors()) {
                if (!board.equals(s.previousNode)) {
                    minPQ.insert(new SearchNode(s.node, board, s.move + 1));
                }
            }
            for (Board board : twinS.node.neighbors()) {
                if (!board.equals(twinS.previousNode)) {
                    twinPQ.insert(new SearchNode(twinS.node, board, twinS.move + 1));
                }
            }
        }
    }

    public boolean isSolvable()            // is the initial board solvable?
    {
        return moves != -1;
    }

    public int moves()                     // min number of moves to solve initial board; -1 if unsolvable
    {
        return moves;
    }

    public Iterable<Board> solution()      // sequence of boards in a shortest solution; null if unsolvable
    {
        if(!isSolvable())
        {
            return null;
        }
        return solution;
    }


    private class SearchNode implements Comparable<SearchNode> {
        private final Board previousNode;
        private final Board node;
        private final int priority;
        private final int move;

        public SearchNode(Board previousNode, Board node, int move) {
            this.previousNode = previousNode;
            this.node = node;
            this.priority = node.manhattan() + move;
            this.move = move;
        }

        @Override
        public int compareTo(SearchNode searchNode) {
            return Integer.compare(this.priority, searchNode.priority);
        }
    }
}
