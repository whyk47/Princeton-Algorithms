import java.util.ArrayList;
import java.util.Comparator;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

public class FastCollinearPoints 
{
    private ArrayList<LineSegment> ls = new ArrayList<LineSegment>(); 

    public FastCollinearPoints(Point[] points)     // finds all line segments containing 4 or more points
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

        Point[] sorted = new Point[points.length];
        for (int i = 0; i < points.length; i++)
        {
            sorted[i] = points[i];
        }

        for (Point p : points)
        {
            Comparator<Point> s =  p.slopeOrder();
            Arrays.sort(sorted, s);
            int head = 0, tail = 1;
            while (tail < sorted.length)
            {
                if (s.compare(sorted[head], sorted[tail]) == 0)
                {
                    tail++;
                    continue;
                }
                else if (tail - head >= 3)
                {
                    addLine(sorted, p, head, tail);
                }
                head = tail;
                tail++;
            }
            if (tail - head >= 3)
                {
                    addLine(sorted, p, head, tail);
                }
        }
    }

    private void addLine(Point[] sorted, Point p, int head, int tail)
    {
        Point[] pts = new Point[tail - head + 1];
        pts[0] = p;
        for (int i = 0; i < tail - head; i++)
        {
            pts[i + 1] = sorted[head + i];
        }
        Point largest = max(pts, 0, pts.length - 1);
        if (p != largest) return;
        Point smallest = min(pts, 0, pts.length - 1);
        ls.add(new LineSegment(largest, smallest));
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