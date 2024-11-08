import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeSet;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

public class PointSET 
{
    private TreeSet<Point2D> points;

    public PointSET() // construct an empty set of points 
    {
        points = new TreeSet<Point2D>();
    }

    public boolean isEmpty() // is the set empty? 
    {
        return points.isEmpty();
    }

    public int size() // number of points in the set 
    {
        return points.size();
    }

    public void insert(Point2D p) // add the point to the set (if it is not already in the set)
    {
        if (p == null) throw new IllegalArgumentException();
        points.add(p);
    }

    public boolean contains(Point2D p) // does the set contain point p? 
    {
        if (p == null) throw new IllegalArgumentException();
        return points.contains(p);
    }

    public void draw() // draw all points to standard draw 
    {
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(-0.1, 1.1);
        StdDraw.setYscale(-0.1, 1.1);
        Iterator<Point2D> pts = points.iterator();
        while (pts.hasNext())
        {
            Point2D pt = pts.next();
            pt.draw();
        }
        StdDraw.show();
    }

    public Iterable<Point2D> range(RectHV rect) // all points that are inside the rectangle (or on the boundary) 
    {
        if (rect == null) throw new IllegalArgumentException();
        ArrayList<Point2D> inside = new ArrayList<Point2D>();
        Iterator<Point2D> pts = points.iterator();
        while (pts.hasNext())
        {
            Point2D pt = pts.next();
            if (rect.contains(pt)) inside.add(pt);
        }
        return inside;
    }

    public Point2D nearest(Point2D p) // a nearest neighbor in the set to point p; null if the set is empty 
    {
        if (p == null) throw new IllegalArgumentException();
        if (isEmpty()) return null;
        Iterator<Point2D> pts = points.iterator();
        Point2D nearest = pts.next();
        while (pts.hasNext())
        {
            Point2D pt = pts.next();
            if (nearest.distanceSquaredTo(p) > pt.distanceSquaredTo(p)) nearest = pt;
        }
        return nearest;
    }

    public static void main(String[] args) // unit testing of the methods (optional) 
    {
        PointSET p = new PointSET();
        for (double x = 0; x < 1; x += 0.1)
        {
            for (double y = 0; y < 1; y += 0.1)
            {
                p.insert(new Point2D(x, y));
            }
        }
        System.out.println(p.size());
        p.draw();
        Iterable<Point2D> pts = p.range(new RectHV(0.4, 0.4, 0.6, 0.6));
        for (Point2D pt : pts) System.out.println(pt);
        System.out.println(p.nearest(new Point2D(-15, -15)));
    }
}