import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class BruteCollinearPoints {
    private final Point[] points;
    private int segments = -1;

    public BruteCollinearPoints(Point[] points)    // finds all line segments containing 4 points
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
        Arrays.sort(points);
        for (int i = 0; i < points.length; i++) {
            Point thisPoint = points[i];

            for (int j = i+1; j < points.length; j++) {
                Point thatPoint = points[j];
                double thatPointSlope = thisPoint.slopeTo(thatPoint);

                Point[] segment = new Point[4];
                segment[0] = thisPoint;
                segment[1] = thatPoint;
                int segmentCount = 2;

                for (int k = j+1; k < points.length; k++) {
                    Point nextPoint = points[k];
                    double nextPointSlope = thisPoint.slopeTo(nextPoint);
                    if (Double.compare(thatPointSlope, nextPointSlope) == 0) {
                        segment[segmentCount++] = nextPoint;
                    }
                }
                if (segmentCount == 4) {
                    LineSegment e = new LineSegment(segment[0], segment[3]);
                    lineSegments.add(e);
                }
            }
        }

        return lineSegments.toArray(new LineSegment[0]);
    }
}