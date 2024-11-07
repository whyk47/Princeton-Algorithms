
import java.util.Comparator;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;

public class BruteCollinearPoints 
{
    private ArrayList<LineSegment> ls = new ArrayList<LineSegment>();

    public BruteCollinearPoints(Point[] points)    // finds all line segments containing 4 points
    {
        // check for null points or duplicated points
        if (points == null)
        {
            throw new IllegalArgumentException();
        }
        int len = points.length;
        for (Point i : points)
        {
            if (i == null) throw new IllegalArgumentException();
        }
        for (int i = 0; i < len - 1; i++)
        {
            for (int j = i + 1; j < len; j++)
            {
                if (i == j) continue;
                if (points[i].compareTo(points[j]) == 0) throw new IllegalArgumentException();
            }
        }

        // iterate through all combinations of 4 points, adding segments to list
        for (int a = 0; a < len - 3; a++)
        {
            for (int b = a + 1; b < len - 2; b++)
            {
                for (int c = b + 1; c < len - 1; c++)
                {
                    for (int d = c + 1; d < len; d++)
                    {
                        Comparator<Point> s = points[a].slopeOrder();
                        if (s.compare(points[b], points[c]) == 0 && s.compare(points[b], points[d]) == 0)
                        {
                            Point[] pts = {points[a], points[b], points[c], points[d]};
                            Point largest = max(pts, 0, pts.length - 1);
                            Point smallest = min(pts, 0, pts.length - 1);
                            ls.add(new LineSegment(largest, smallest));
                        }
                    }
                }
            }
        }
    }

    private Point max(Point[] points, int lower, int upper)
    {
        if (lower == upper)
        {
            return points[lower];
        }
        int mid = (upper + lower) / 2;
        Point max1 = max(points, lower, mid);
        Point max2 = max(points, mid + 1, upper);
        if (max1.compareTo(max2) > 0) return max1;
        else return max2;
    }

    private Point min(Point[] points, int lower, int upper)
    {
        if (lower == upper)
        {
            return points[lower];
        }
        int mid = (upper + lower) / 2;
        Point min1 = min(points, lower, mid);
        Point min2 = min(points, mid + 1, upper);
        if (min1.compareTo(min2) < 0) return min1;
        else return min2;
    }

    public int numberOfSegments()        // the number of line segments
    {
        return ls.size();
    }

    public LineSegment[] segments()              // the line segments
    {
        LineSegment[] seg = new LineSegment[ls.size()];
        return ls.toArray(seg);
    }

    public static void main(String[] args) 
    {
        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
