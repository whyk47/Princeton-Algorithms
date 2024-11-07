import java.util.ArrayList;

public class Board 
{
    private final int[][] tiles;
    private int row0 = -1;
    private int col0 = -1;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles)
    {
        this.tiles = new int[tiles.length][tiles.length];
        // find index of 0
        for (int row = 0; row < dimension(); row++)
        {
            for (int col = 0; col < dimension(); col++)
            {
                this.tiles[row][col] = tiles[row][col];
                if (tiles[row][col] == 0)
                {
                    row0 = row; 
                    col0 = col;
                } 
            }
        }
    }
                                           
    // string representation of this board
    public String toString()
    {
        StringBuilder str = new StringBuilder(dimension() + "\n");
        for (int row = 0; row < dimension(); row++)
        {
            for (int col = 0; col < dimension(); col++)
            {
                str.append(tiles[row][col] + " ");
            }
            str.append("\n");
        }
        return str.toString();
    }

    // board dimension n
    public int dimension()
    {
        return tiles.length;
    }

    // number of tiles out of place
    public int hamming()
    {
        int outOfPlace = 0;
        for (int row = 0; row < dimension(); row++)
        {
            for (int col = 0; col < dimension(); col++)
            {
                if (tiles[row][col] == 0) continue;
                if (tiles[row][col] != correctTile(row, col)) outOfPlace++;
            }
        }
        return outOfPlace;
    }

    private int correctTile(int row, int col)
    {
        if (row == dimension() - 1 && col == dimension() - 1) return 0;
        return col + 1 + row * dimension();
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan()
    {
        int sum = 0;
        for (int row = 0; row < dimension(); row++)
        {
            for (int col = 0; col < dimension(); col++)
            {
                if (tiles[row][col] == 0) continue;
                int[] spot = correctSpot(tiles[row][col]);
                sum += (Math.abs(row - spot[0]) + Math.abs(col - spot[1]));
            }
        }
        return sum;
    }

    private int[] correctSpot(int tile)
    {
        if (tile == 0)
        {
            int[] spot = {dimension() - 1, dimension() - 1};
            return spot;
        }
        /* row * n + col + 1 = tile -> row = (tile - 1 - col) / n
         * Upper and lower bound for row obtained by subbing col = 0 & col = n
         * upper = (tile - 1) / n, lower = (tile - 1 - n) / n = upper - 1
         * upper and lower are ints iff col = 0. In this case row = (tile - 1) / n
         * Otherwise row is the integer between upper and upper - 1
         * Hence in both cases, row = floor(upper) = floor((tile - 1) / n)
         */
        int row = (tile - 1) / dimension();
        int col = tile - 1 - row * dimension();
        int[] spot = {row, col};
        return spot;
       
    }

    // is this board the goal board?
    public boolean isGoal()
    {
        return hamming() == 0;
    }

    // does this board equal y?
    public boolean equals(Object y)
    {
        if (y == null) return false;
        return toString().equals(y.toString());
    }

    // all neighboring boards
    public Iterable<Board> neighbors()
    {
        ArrayList<Board> boards = new ArrayList<Board>();
        int[][] displacements = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
        for (int[] d : displacements)
        {
            Board b = getNeighbour(row0, col0, row0 + d[0], col0 + d[1]);
            if (b != null) boards.add(b);
        }
        return boards;
    }

    private Board getNeighbour(int row1, int col1, int row2, int col2)
    {
        if (row2 < 0 || row2 >= dimension() || col2 < 0 || col2 >= dimension()) return null;
        int[][] copy = new int[dimension()][dimension()];
        for (int row = 0; row < dimension(); row++)
        {
            for (int col = 0; col < dimension(); col++)
            {
                copy[row][col] = tiles[row][col];
            }
        }
        int tmp = copy[row1][col1];
        copy[row1][col1] = tiles[row2][col2];
        copy[row2][col2] = tmp;
        return new Board(copy);
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin()
    {
        int[][] coords = new int[2][2];
        int numCoords = 0;
        for (int row = 0; row < dimension(); row++)
        {
            if (numCoords >= 2) break;
            for (int col = 0; col < dimension(); col++)
            {
                if (numCoords >= 2) break;
                int[] coord =  {row, col};
                if (tiles[row][col] != 0) coords[numCoords++] = coord;
            }
        }
        return getNeighbour(coords[0][0], coords[0][1], coords[1][0], coords[1][1]);
    }

    // unit testing (not graded)
    public static void main(String[] args)
    {
        int[][] tiles = {
            {1, 2, 3}, 
            {4, 5, 6},
            {7, 0, 8}
        };
        Board b = new Board(tiles);
        System.out.println(b);
        System.out.println(b.hamming());
        System.out.println(b.manhattan());
        System.out.println(b.isGoal());
        Board b1 = new Board(tiles);
        System.out.println(b.equals(b1));
        System.out.println("Neighbours");
        for (Board i : b.neighbors())
        {
            System.out.println(i);
        }
        System.out.println(b.twin());
    }

}
