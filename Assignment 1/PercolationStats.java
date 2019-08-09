/* *****************************************************************************
 *  Name: Erramilli Naga Surya Praveen
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {

    private double[] percThreshold;
    private int t;
    private double mean;
    private double stddev;
    private double confLo;
    private double confHigh;

    public PercolationStats(int n,
                            int trials)    // perform trials independent experiments on an n-by-n grid
    {
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException();
        }
        percThreshold = new double[trials];
        this.t = trials;
        int index = 0;
        while (trials-- > 0) {
            int count = 0;
            Percolation percolation = new Percolation(n);
            while (true) {
                count++;
                int row, col;
                do {
                    int uniform = StdRandom.uniform(n * n);
                    uniform++;
                    row = (int) Math.ceil(uniform / (double) n);
                    col = uniform - (n * (row - 1));
                }
                while (percolation.isOpen(row, col));
                percolation.open(row, col);
                if (percolation.percolates()) {
                    percThreshold[index++] = count / (double) (n * n);
                    break;
                }
            }
        }
        this.mean = StdStats.mean(percThreshold);
        this.stddev = StdStats.stddev(percThreshold);
        this.confLo = mean - ((1.96 * stddev) / Math.sqrt(t));
        this.confHigh = mean + ((1.96 * stddev) / Math.sqrt(t));
    }

    public double mean()                          // sample mean of percolation threshold
    {
        return mean;
    }

    public double stddev()                        // sample standard deviation of percolation threshold
    {
        return stddev;
    }

    public double confidenceLo()                  // low  endpoint of 95% confidence interval
    {
        return confLo;
    }

    public double confidenceHi()                  // high endpoint of 95% confidence interval
    {
        return confHigh;
    }

    public static void main(String[] args)        // test client (described below)
    {
        int n = Integer.parseInt(args[0]);
        int t = Integer.parseInt(args[1]);

        PercolationStats percolationStats = new PercolationStats(n, t);
        System.out.println("mean=" + percolationStats.mean());
        System.out.println("stddev=" + percolationStats.stddev());
        System.out.println("95% confidence interval=[" + percolationStats.confidenceLo() + ","
                                   + percolationStats.confidenceHi() + "]");
    }
}
