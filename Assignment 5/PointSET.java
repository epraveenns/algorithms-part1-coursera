/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class PointSET {

    private final Set<Point2D> set = new TreeSet<>();

    public PointSET()                               // construct an empty set of points
    {

    }

    public boolean isEmpty()                      // is the set empty?
    {
        return set.isEmpty();
    }

    public int size()                         // number of points in the set
    {
        return set.size();
    }

    public void insert(
            Point2D p)              // add the point to the set (if it is not already in the set)
    {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        set.add(p);
    }

    public boolean contains(Point2D p)            // does the set contain point p?
    {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        return set.contains(p);
    }

    public void draw()                         // draw all points to standard draw
    {
        set.forEach(s -> StdDraw.point(s.x(), s.y()));
    }

    public Iterable<Point2D> range(
            RectHV rect)             // all points that are inside the rectangle (or on the boundary)
    {
        if (rect == null) {
            throw new IllegalArgumentException();
        }
        return set.stream().filter(rect::contains).collect(Collectors.toList());
    }

    public Point2D nearest(
            Point2D p)             // a nearest neighbor in the set to point p; null if the set is empty
    {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        if (set.isEmpty())
        {
            return null;
        }
        Point2D champion = new Point2D(0, 0);
        double champDistance = Double.POSITIVE_INFINITY;

        for (Point2D point2D : set) {
            double v = p.distanceSquaredTo(point2D);
            if (v < champDistance) {
                champion = point2D;
                champDistance = v;
            }
        }

        return champion;
    }

}
