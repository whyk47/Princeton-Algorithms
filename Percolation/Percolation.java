import edu.princeton.cs.algs4.WeightedQuickUnionUF;

// TODO: Address backwash

public class Percolation 
{

    private WeightedQuickUnionUF grid;
    private int n;
    private boolean[] opened;
    private int top;
    private int bottom;
    private int numOpen;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n)
    {
        if (n < 1) 
        {
            throw new IllegalArgumentException();
        }
        grid = new WeightedQuickUnionUF(n * n + 2);
        this.n = n;
        top = n * n;
        bottom = n * n + 1;
        numOpen = 0;
        opened = new boolean[n * n];
        for (int i = 0; i < n * n; i++) 
        {
            opened[i] = false;
        }
    }

    private int index(int row, int col)
    {
        if (row < 1 || row > n || col < 1 || col > n)
        {
            return -1;
        }
        return (col - 1) + (row - 1) * n;
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col)
    {
        if (row < 1 || col < 1 || row > n || col > n) 
        {
            throw new IllegalArgumentException();
        }
        if (!opened[index(row, col)]) 
        {
            opened[index(row, col)] = true;
            numOpen++;
            int[] neighbours = {index(row - 1, col), index(row + 1, col), index(row, col - 1), index(row, col + 1)};
            if (row == 1)
                {
                    grid.union(index(row, col), top);
                }
            if (row == n)
                {
                    grid.union(index(row, col), bottom);
                }
            for (int i = 0; i < neighbours.length; i++)
            {
                if (neighbours[i] != -1 && opened[neighbours[i]])
                {
                    grid.union(index(row, col), neighbours[i]);
                }            
            }
        }        
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col)
    {
        if (row < 1 || col < 1 || row > n || col > n) 
        {
            throw new IllegalArgumentException();
        }
        return opened[index(row, col)];
    }
    // is the site (row, col) full?
    public boolean isFull(int row, int col)
    {
        if (row < 1 || col < 1 || row > n || col > n) 
        {
            throw new IllegalArgumentException();
        }
        return grid.find(top) == grid.find(index(row, col)) && isOpen(row, col);
    }

    // returns the number of open sites
    public int numberOfOpenSites()
    {
        return numOpen;
    }

    // does the system percolate?
    public boolean percolates()
    {
        return grid.find(top) == grid.find(bottom);
    }

    // test client (optional)
    public static void main(String[] args)
    {
        Percolation a = new Percolation(3);
        a.open(1, 1);
        System.out.println(a.isOpen(1, 1));
        System.out.println(a.isOpen(2, 2));
        System.out.println(a.percolates());
        a.open(2, 1);
        System.out.println(a.isFull(2, 2));
        a.open(2, 2);
        System.out.println(a.isFull(2, 2));
        a.open(3, 2);
        System.out.println(a.percolates());
        System.out.println(a.numberOfOpenSites());
        Percolation b = new Percolation(1);
        b.open(1, 1);
        System.out.println(b.percolates());
    }
}