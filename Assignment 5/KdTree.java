import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.LinkedList;
import java.util.List;

public class KdTree {
    private final BSTNode bstNode;
    private int size;
    private double minDistance = Double.POSITIVE_INFINITY;

    public KdTree()                               // construct an empty set of points
    {
        bstNode = new BSTNode();
    }

    public boolean isEmpty()                      // is the set empty?
    {
        return bstNode.point == null;
    }

    public int size()                         // number of points in the set
    {
        return size;
    }

    public void insert(
            Point2D p)              // add the point to the set (if it is not already in the set)
    {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        if (size == 0) {
            bstNode.point = p;
            bstNode.isVertical = true;
            double x = p.x();
            bstNode.leftRectangle = new RectHV(0, 0, x, 1);
            bstNode.rightRectangle = new RectHV(x, 0, 1, 1);
        }
        else {
            BSTNode node = bstNode;
            while (true) {
                if (node.isVertical ? node.point.x() >= p.x() : node.point.y() >= p.y()) {
                    if (node.point.compareTo(p) == 0) {
                        return;
                    }
                    if (node.left == null) {
                        node.left = new BSTNode();
                        node.left.point = p;
                        node.left.isVertical = !node.isVertical;
                        if (node.isVertical) {  //for horizontal lines
                            node.left.leftRectangle = new RectHV(node.leftRectangle.xmin(),
                                                                 node.leftRectangle.ymin(),
                                                                 node.point.x(), p.y());
                            node.left.rightRectangle = new RectHV(node.leftRectangle.xmin(), p.y(),
                                                                  node.leftRectangle.xmax(),
                                                                  node.leftRectangle.ymax());
                        }
                        else {
                            node.left.leftRectangle = new RectHV(node.leftRectangle.xmin(),
                                                                 node.leftRectangle.ymin(), p.x(),
                                                                 node.point.y());
                            node.left.rightRectangle = new RectHV(p.x(), node.leftRectangle.ymin(),
                                                                  node.leftRectangle.xmax(),
                                                                  node.leftRectangle.ymax());
                        }
                        break;
                    }
                    else {
                        node = node.left;
                    }
                }
                else {
                    if (node.right == null) {
                        node.right = new BSTNode();
                        node.right.point = p;
                        node.right.isVertical = !node.isVertical;
                        if (node.isVertical) {  //for horizontal lines
                            node.right.leftRectangle = new RectHV(node.rightRectangle.xmin(),
                                                                  node.rightRectangle.ymin(),
                                                                  node.rightRectangle.xmax(),
                                                                  p.y());
                            node.right.rightRectangle = new RectHV(node.rightRectangle.xmin(),
                                                                   p.y(),
                                                                   node.rightRectangle.xmax(),
                                                                   node.rightRectangle.ymax());
                        }
                        else {
                            node.right.leftRectangle = new RectHV(node.rightRectangle.xmin(),
                                                                  node.rightRectangle.ymin(), p.x(),
                                                                  node.rightRectangle.ymax());
                            node.right.rightRectangle = new RectHV(p.x(),
                                                                   node.rightRectangle.ymin(),
                                                                   node.rightRectangle.xmax(),
                                                                   node.rightRectangle.ymax());
                        }
                        break;
                    }
                    else {
                        node = node.right;
                    }
                }
            }
        }
        size++;
    }

    public boolean contains(Point2D p)            // does the set contain point p?
    {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        BSTNode node = bstNode;
        while (node != null && node.point != null) {
            int compare = node.point.compareTo(p);
            if (compare == 0) {
                return true;
            }
            if (node.isVertical ? node.point.x() >= p.x() : node.point.y() >= p.y()) {
                node = node.left;
            }
            else {
                node = node.right;
            }
        }
        return false;
    }

    public void draw()                         // draw all points to standard draw
    {
        BSTNode node = bstNode;

        drawLines(node);
    }

    private void drawLines(BSTNode node) {
        while (node != null && node.point != null) {
            if (node.isVertical) {
                StdDraw.line(node.point.x(), 1.0, node.point.x(), 0.0);
            }
            else {
                StdDraw.line(1.0, node.point.y(), 0.0, node.point.y());
            }
            drawLines(node.left);
            drawLines(node.right);
        }
    }

    public Iterable<Point2D> range(
            RectHV rect)             // all points that are inside the rectangle (or on the boundary)
    {
        if (rect == null) {
            throw new IllegalArgumentException();
        }
        List<Point2D> set = new LinkedList<>();

        BSTNode node = bstNode;
        search(rect, set, node);

        return set;
    }

    private void search(RectHV rect, List<Point2D> set, BSTNode node) {
        if (node != null && node.point != null) {
            if (rect.contains(node.point)) {
                set.add(node.point);
                search(rect, set, node.left);
                search(rect, set, node.right);
            }
            else {
                if (node.leftRectangle.intersects(rect)) {
                    search(rect, set, node.left);
                }
                if (node.rightRectangle.intersects(rect)) {
                    search(rect, set, node.right);
                }
            }
        }
    }

    public Point2D nearest(
            Point2D p)             // a nearest neighbor in the set to point p; null if the set is empty
    {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        minDistance = Double.POSITIVE_INFINITY;
        BSTNode champion = new BSTNode();
        champion.point = bstNode.point;

        champion = locateNearest(champion, bstNode, p);
        return champion.point;
    }

    private BSTNode locateNearest(BSTNode champion, BSTNode node, Point2D p) {
        if (node == null) {
            return champion;
        }
        double leftDistance = Double.POSITIVE_INFINITY;
        if (node.leftRectangle != null) {
            leftDistance = node.leftRectangle.distanceSquaredTo(p);
        }
        double rightDistance = Double.POSITIVE_INFINITY;
        if (node.rightRectangle != null) {
            rightDistance = node.rightRectangle.distanceSquaredTo(p);
        }
        if (leftDistance < rightDistance && leftDistance < minDistance) {
            //scan left node
            //come for right node if still distance < minDistance
            double currentDistance = node.point.distanceSquaredTo(p);
            if (currentDistance < minDistance) {
                minDistance = currentDistance;
                champion.point = node.point;
            }
            locateNearest(champion, node.left, p);
            if (rightDistance < minDistance) {
                locateNearest(champion, node.right, p);
            }
        }
        else if (rightDistance < leftDistance && rightDistance < minDistance) {
            //scan right node
            //come for left node if still distance < minDistance
            double currentDistance = node.point.distanceSquaredTo(p);
            if (currentDistance < minDistance) {
                minDistance = currentDistance;
                champion.point = node.point;
                locateNearest(champion, node.right, p);
                if (leftDistance < minDistance) {
                    locateNearest(champion, node.left, p);
                }
            }
        }

        return champion;
    }


    private class BSTNode {
        private Point2D point;
        private BSTNode left;
        private BSTNode right;
        private boolean isVertical;
        //if vertical, compare using x coordinate of point. Or else y coordinate
        private RectHV leftRectangle;
        private RectHV rightRectangle;
    }

}