import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {

    private double[] results;
    private int t;
    private double confidence95 = 1.96;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials)
    {
        if (n < 1 || trials < 1)
        {
            throw new IllegalArgumentException();
        }
        t = trials;
        results = new double[trials];
        for (int i = 0; i < trials; i++)
        {
            Percolation a = new Percolation(n);
            while (!a.percolates())
            {
                a.open(StdRandom.uniformInt(1, n + 1), StdRandom.uniformInt(1, n + 1));
            }
            results[i] = Double.valueOf(a.numberOfOpenSites()) / (n * n);
        }
    }

    // sample mean of percolation threshold
    public double mean()
    {
        return StdStats.mean(results);
    }

    // sample standard deviation of percolation threshold
    public double stddev()
    {
        return StdStats.stddev(results);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo()
    {
        return mean() - confidence95 * stddev() / Math.sqrt(t);
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi()
    {
        return mean() + confidence95 * stddev() / Math.sqrt(t);
    }

   // test client (see below)
   public static void main(String[] args)
   {
    PercolationStats stats = new PercolationStats(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
    System.out.println("mean = " + stats.mean());
    System.out.println("stddev = " + stats.stddev());
    System.out.println("95% confidence interval = [" + stats.confidenceLo() + ", " + stats.confidenceHi() + "]");
   }

}