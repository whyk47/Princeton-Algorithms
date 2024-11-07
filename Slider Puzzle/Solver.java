import java.util.ArrayList;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;

public class Solver 
{
    private Node goalNode;

    private class Node implements Comparable<Node>
    {
        public Node prev;
        public final Board b;
        public final int numMoves, manFunc;

        public Node(Node prev, Board b, int numMoves)
        {
            this.prev = prev;
            this.b = b;
            this.numMoves = numMoves;
            this.manFunc = b.manhattan() + numMoves;
        }

        public int compareTo(Node that)
        {
            if (this.manFunc > that.manFunc) return 1;
            if (this.manFunc < that.manFunc) return -1;
            return 0;
        }

        public boolean same(Node that)
        {
            if (that == null) return false;
            return this.b.equals(that.b);
        }
    }
    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial)
    {
        if (initial == null) throw new IllegalArgumentException();
        goalNode = null;
        MinPQ<Node> pq = new MinPQ<Node>(), twinpq = new MinPQ<Node>();
        pq.insert(new Node(null, initial, 0));
        twinpq.insert(new Node(null, initial.twin(), 0));
        while (!pq.isEmpty() && !twinpq.isEmpty())
        {
            goalNode = expandNode(pq);
            if (goalNode != null || expandNode(twinpq) != null) break;
        }  
    }

    private Node expandNode(MinPQ<Node> pq)
    {
        Node n = pq.delMin();
        if (n.b.isGoal()) return n;
        for (Board k : n.b.neighbors()) 
        {
            Node neighbour = new Node(n, k, n.numMoves + 1);
            if (!neighbour.same(n.prev)) pq.insert(neighbour);
        }
        return null;
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable()
    {
        return goalNode != null;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves()
    {
        if (!isSolvable()) return -1;
        return goalNode.numMoves;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution()
    {
        if (!isSolvable()) return null;
        ArrayList<Board> sol = new ArrayList<Board>();
        Node curr = goalNode;
        while (curr != null)
        {
            sol.add(0, curr.b);
            curr = curr.prev;
        } 
        return sol;
    }

    // test client (see below) 
    public static void main(String[] args) 
    {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else 
        {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }

}
