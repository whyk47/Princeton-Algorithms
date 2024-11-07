# Slider Puzzle Solver

[Full Project Specification](https://coursera.cs.princeton.edu/algs4/assignments/8puzzle/specification.php)

### Overview
A Java program to solve the 8-puzzle problem using the A* search algorithm.

The 8-puzzle is a 3x3 grid with numbered tiles from 1 to 8 and one blank tile. The goal is to rearrange the tiles into row-major order with as few moves as possible. 

### Classes
- `Board`: Models an n x n board with tiles and operations for calculating distances and neighbors.
- `Solver`: Implements A* search using priority functions based on Hamming or Manhattan distances to find the shortest solution.

### Usage
- Running the solver:
    ```bash
    $ java -cp ".;..\algs4.jar" Solver [path/to/test/file.txt]
    ```
- Example:
    ```bash
    $ java -cp ".;..\algs4.jar" Solver tests/unsolvable.txt
    No solution possible
    $ java -cp ".;..\algs4.jar" Solver tests/puzzle.txt
    Minimum number of moves = 4
    3
    0 1 3
    4 2 5
    7 8 6

    3
    1 0 3
    4 2 5
    7 8 6

    3
    1 2 3
    4 0 5
    7 8 6

    3
    1 2 3
    4 5 0
    7 8 6

    3
    1 2 3
    4 5 6
    7 8 0
    ```

### Input file format
- First line: board size n
- Next n lines: tiles in row-major order (0 represents the blank).