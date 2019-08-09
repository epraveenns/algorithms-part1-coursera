import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class FastCollinearPoints {
    private final Point[] points;
    private int segments = -1;

    public FastCollinearPoints(
            Point[] points)     // finds all line segments containing 4 or more points
    {
        if (points == null || Arrays.stream(points).anyMatch(Objects::isNull)) {
            throw new IllegalArgumentException();
        }
        int length = points.length;
        for (int i = 0; i < length; i++) {
            Point thisPoint = points[i];
            for (int j = i + 1; j < length; j++) {
                Point thatPoint = points[j];
                if (thisPoint.compareTo(thatPoint) == 0) {
                    throw new IllegalArgumentException();
                }
            }
        }
        this.points = new Point[length];
        for (int i = 0; i < length; i++) {
            this.points[i] = points[i];
        }
    }

    public int numberOfSegments()        // the number of line segments
    {
        if (segments == -1) {
            segments = segments().length;
        }
        return segments;
    }

    public LineSegment[] segments()                // the line segments
    {
        List<LineSegment> lineSegments = new ArrayList<>();
        for (int i = 0; i < points.length - 3; i++) {
            Arrays.sort(points);

            Arrays.sort(points, points[i].slopeOrder());

            for (int p = 0, first = 1, last = 2; last < points.length; last++) {
                // find last collinear to p point
                while (last < points.length
                        && Double.compare(points[p].slopeTo(points[first]),
                                          points[p].slopeTo(points[last])) == 0) {
                    last++;
                }
                // if found at least 3 elements, make segment if it's unique
                if (last - first >= 3 && points[p].compareTo(points[first]) < 0) {
                    lineSegments.add(new LineSegment(points[p], points[last - 1]));
                }
                // Try to find next
                first = last;
            }
        }
        return lineSegments.toArray(new LineSegment[0]);
    }
}