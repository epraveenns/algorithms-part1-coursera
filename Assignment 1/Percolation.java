/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private WeightedQuickUnionUF weightedQuickUnionUF;
    private int openSitesCount;
    private int n;
    private boolean[][] openArray;

    public Percolation(int n)                // create n-by-n grid, with all sites blocked
    {
        int size = n * n;
        weightedQuickUnionUF = new WeightedQuickUnionUF(size + 2);
        for (int i = 1; i <= n; i++) {
            weightedQuickUnionUF.union(0, i);
            weightedQuickUnionUF.union(size + 1, size - n + i);
        }
        openArray = new boolean[n + 1][n + 1];
        openSitesCount = 0;
        this.n = n;
    }

    public void open(int row, int col)    // open site (row, col) if it is not open already
    {
        if (!isValidCell(row, col)) {
            throw new IllegalArgumentException();
        }
        if (isOpen(row, col)) {
            return;
        }
        if (isValidCell(row, col - 1) && isOpen(row, col - 1)) {
            weightedQuickUnionUF.union(getSiteIntValue(row, col), getSiteIntValue(row, col - 1));
        }
        if (isValidCell(row, col + 1) && isOpen(row, col + 1)) {
            weightedQuickUnionUF.union(getSiteIntValue(row, col), getSiteIntValue(row, col + 1));
        }
        if (isValidCell(row - 1, col) && isOpen(row - 1, col)) {
            weightedQuickUnionUF.union(getSiteIntValue(row, col), getSiteIntValue(row - 1, col));
        }
        if (isValidCell(row + 1, col) && isOpen(row + 1, col)) {
            weightedQuickUnionUF.union(getSiteIntValue(row, col), getSiteIntValue(row + 1, col));
        }

        openSitesCount++;
        openArray[row][col] = true;
    }

    public boolean isOpen(int row, int col)  // is site (row, col) open?
    {
        if (!isValidCell(row, col)) {
            throw new IllegalArgumentException();
        }
        return openArray[row][col];
    }

    public boolean isFull(int row, int col)  // is site (row, col) full?
    {
        if (!isValidCell(row, col)) {
            throw new IllegalArgumentException();
        }
        return isOpen(row, col) && weightedQuickUnionUF.connected(0, getSiteIntValue(row, col));
    }

    public int numberOfOpenSites()       // number of open sites
    {
        return openSitesCount;
    }

    public boolean percolates()              // does the system percolate?
    {
        return weightedQuickUnionUF.connected(0, n * n + 1);
    }

    private boolean isValidCell(int row, int col) {
        if (row <= 0 || col <= 0 || row > n || col > n) {
            return false;
        }
        return true;
    }

    private int getSiteIntValue(int row, int col) {
        return (row - 1) * n + col;
    }

    public static void main(String[] args)   // test client (optional)
    {
        Percolation percolation = new Percolation(5);
        System.out.println(percolation.isFull(1, 4));
    }
}
