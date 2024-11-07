# Collinear Points

### Overview
This project is a pattern recognition program to identify line segments among points in a 2D plane. Given a set of distinct points, it identifies every maximal line segment (connecting four or more points) using brute-force and efficient sorting-based approaches. This application is relevant in computer vision and data analysis.

### Sorting Based Approach
- Choose a point p as the origin.
- For each other point q, determine the slope it makes with p.
- Sort the points according to the slopes they makes with p.
- Check if any 3 (or more) adjacent points in the sorted order have equal slopes with respect to p. If so, these points, together with p, are collinear.

### Classes
- `Point`: Represents a point in the plane with methods for drawing, comparing, and calculating slopes with other points.
- `LineSegment`: Represents a line segment between two points.
- `BruteCollinearPoints`: Finds all line segments containing exactly four collinear points using a brute-force approach.
- `FastCollinearPoints`: Finds line segments containing 4 or more collinear points using a sorting-based approach for efficiency.

### Usage
1. Running `BruteCollinearPoints`.
    ```bash
    $ java -cp ".;..\algs4.jar" BruteCollinearPoints [path/to/test/file.txt]
    ```
2. Running `FastCollinearPoints`.
    ```bash
    $ java -cp ".;..\algs4.jar" FastCollinearPoints [path/to/test/file.txt]
    ```
##### Example:
    ```bash
    $ java -cp ".;..\algs4.jar" FastCollinearPoints tests/input2.txt 
    (0, 10000) -> (10000, 0)
    (20000, 21000) -> (3000, 4000)
    ```
![Standard Draw](image.png)

### Performance
- `BruteCollinearPoints`: O(n^4) time, O(n) space.
- `FastCollinearPoints`: O(n^2logn) time, O(n) space.