import java.util.ArrayList;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

public class KdTree 
{
    // Stores 2d points within the unit square in a 2d binary search tree 
    // Supports nearest neighbour and 2d range operations
    private final boolean HORIZONTAL = true;
    private final boolean VERTICAL = false;
    private Node root;

    private class Node
    {
        Point2D key;
        Node left, right;
        int size;
        boolean axis;

        public Node(Point2D key, boolean axis)
        {
            this.axis = axis;
            this.key = key;
            this.size = 1;
        }
    }

    private int size(Node x)
    {
        if (x == null) return 0;
        return x.size;
    }

    private int compare(Point2D a, Point2D b, boolean axis)
        {
            // compares x if horizontal, y if vertical
            if (a.x() == b.x() && a.y() == b.y()) return 0;
            if (axis == HORIZONTAL)
            {
                if (a.x() > b.x()) return 1;
                return -1;
            }
            else
            {
                if (a.y() > b.y()) return 1;
                return -1;
            }
        }

    private boolean axis(int depth)
    {
        // returns horizontal (true) if depth is even, and vertical (false) if depth is odd
        return depth % 2 == 0;
    }

    public KdTree() // construct an empty set of points 
    {
        root = null;
    }

    public boolean isEmpty() // is the set empty? 
    {
        return size(root) == 0;
    }

    public int size() // number of points in the set 
    {
        return size(root);
    }

    public void insert(Point2D p) // add the point to the set (if it is not already in the set)
    {
        if (p == null) throw new IllegalArgumentException();
        root = insert(root, p, 0);
    }

    private Node insert(Node x, Point2D p, int depth)
    {
        if (x == null) return new Node(p, axis(depth));
        int cmp = compare(p, x.key, x.axis);
        if (cmp < 0) x.left = insert(x.left, p, ++depth);
        else if (cmp > 0) x.right = insert(x.right, p, ++depth);
        x.size = 1 + size(x.left) + size(x.right);
        return x;        
    }

    public boolean contains(Point2D p) // does the set contain point p? 
    {
        if (p == null) throw new IllegalArgumentException();
        return contains(root, p);
    }

    private boolean contains(Node x, Point2D p)
    {
        if (x == null) return false;
        int cmp = compare(p, x.key, x.axis);
        if (cmp < 0) return contains(x.left, p);
        else if (cmp > 0) return contains(x.right, p);
        else return true;
    }

    public void draw() // draw all points to standard draw 
    {
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(-0.1, 1.1);
        StdDraw.setYscale(-0.1, 1.1);
        draw(root, new RectHV(0, 0, 1, 1));
        StdDraw.show();
    }

    private void draw(Node x, RectHV bound)
    {
        if (x == null) return;
        StdDraw.setPenRadius(0.002);
        if (x.axis == HORIZONTAL) 
        {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.line(x.key.x(), bound.ymin(), x.key.x(), bound.ymax());
        }
        else
        {
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.line(bound.xmin(), x.key.y(), bound.xmax(), x.key.y());
        }
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.005);
        x.key.draw();
        // boundary rects on the 2 sides of the splitting line
        RectHV lower = (x.axis == HORIZONTAL) ? 
            new RectHV(bound.xmin(), bound.ymin(), x.key.x(), bound.ymax()) : 
            new RectHV(bound.xmin(), bound.ymin(), bound.xmax(), x.key.y());
        RectHV upper = (x.axis == HORIZONTAL) ? 
            new RectHV(x.key.x(), bound.ymin(), bound.xmax(), bound.ymax()) :
            new RectHV(bound.xmin(), x.key.y(), bound.xmax(), bound.ymax());
        draw(x.left, lower);
        draw(x.right, upper);
    }

    public Iterable<Point2D> range(RectHV rect) // all points that are inside the rectangle (or on the boundary) 
    {
        if (rect == null) throw new IllegalArgumentException();
        ArrayList<Point2D> inside = new ArrayList<Point2D>();
        range(root, rect, inside);
        return inside;
    }

    private void range(Node x, RectHV rect, ArrayList<Point2D> inside)
    {
        if (x == null) return;
        if (rect.contains(x.key)) inside.add(x.key);
        // Search subtree whose rect intersects the query rect
        if (x.axis == HORIZONTAL)
        {
            if (x.key.x() >= rect.xmin()) range(x.left, rect, inside);
            if (x.key.x() <= rect.xmax()) range(x.right, rect, inside);
        }
        else
        {
            if (x.key.y() >= rect.ymin()) range(x.left, rect, inside);
            if (x.key.y() <= rect.ymax()) range(x.right, rect, inside);
        }
    }

    public Point2D nearest(Point2D p) // a nearest neighbor in the set to point p; null if the set is empty 
    {
        if (p == null) throw new IllegalArgumentException();
        if (isEmpty()) return null;
        return nearest(root, p, root.key, new RectHV(0, 0, 1, 1));
    }

    private Point2D nearest(Node x, Point2D p, Point2D closest, RectHV bound)
    {
        if (x == null) return closest;
        if (x.key.distanceSquaredTo(p) < closest.distanceSquaredTo(p)) closest = x.key;

        // boundary rects on the 2 sides of the splitting line
        RectHV lower = (x.axis == HORIZONTAL) ? 
            new RectHV(bound.xmin(), bound.ymin(), x.key.x(), bound.ymax()) : 
            new RectHV(bound.xmin(), bound.ymin(), bound.xmax(), x.key.y());
        RectHV upper = (x.axis == HORIZONTAL) ? 
            new RectHV(x.key.x(), bound.ymin(), bound.xmax(), bound.ymax()) :
            new RectHV(bound.xmin(), x.key.y(), bound.xmax(), bound.ymax());
        
        // shortest possible dist of each subtree
        double lowerDist = lower.distanceSquaredTo(p);
        double upperDist = upper.distanceSquaredTo(p);

        // check subtree on the same side as query point 1st and update closest
        closest = (lowerDist < upperDist) ? 
            nearest(x.left, p, closest, lower) :
            nearest(x.right, p, closest, upper);

        // only search other subtree if it could contain a closer point
        if (closest.distanceSquaredTo(p) > Math.max(lowerDist, upperDist)) 
        {
            closest = (lowerDist >= upperDist) ? 
            nearest(x.left, p, closest, lower) :
            nearest(x.right, p, closest, upper);
        }
        return closest;
    }

    public static void main(String[] args)
    {
        KdTree k = new KdTree();
        for (int i = 0; i < 100; i++)
        {
            k.insert(new Point2D(Math.random(), Math.random()));
        }
        k.draw();
        RectHV r = new RectHV(0.25, 0.25, 1, 1);
        StdDraw.setPenColor(StdDraw.GRAY);
        StdDraw.setPenRadius(0.002);
        r.draw();
        StdDraw.show();
        for (Point2D p : k.range(r)) System.out.println(p);
        Point2D p = new Point2D(Math.random(), Math.random());
        StdDraw.setPenColor(StdDraw.GREEN);
        StdDraw.setPenRadius(0.01);
        p.draw();
        StdDraw.setPenColor(StdDraw.MAGENTA);
        StdDraw.setPenRadius(0.01);
        Point2D n = k.nearest(p);
        n.draw();
        System.out.println();
        System.out.println(n);
        StdDraw.show();
    }
}
