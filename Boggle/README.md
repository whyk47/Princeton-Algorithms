# Boggle Solver

[Full Project Specification](https://coursera.cs.princeton.edu/algs4/assignments/boggle/specification.php)

### Overview
The Boggle Solver is a Java-based program designed to find all valid words and calculates the total score from a given Boggle board using a provided dictionary. Boggle is a word game where players form words by connecting adjacent letters on a grid of dice, aiming to score points based on word length.

`BoggleSolver` uses Depth-First Search (DFS) with backtracking to explore all possible word paths on the board. The dictionary words are stored in a trie allowing rapid detection of potential words during board traversal. The search is pruned early when no valid word can be formed from the current path, reducing unnecessary computations.

The solver is built for raw speed, and is able to process thousands of random 4x4 boards per second using the provided dictionaries.

### Classes
- `BoggleSolver`: main class responsible for finding all valid words on a Boggle board and scoring them.
- `BoggleGame`: GUI to play Boggle against computer on a random difficulty based on the dictionary used.
- `BoggleBoard`: represents the Boggle board and provides methods to interact with it.

### Usage
1. Running `BoggleSolver`.
    ```bash 
    $ java -cp ".;..\algs4.jar" BoggleSolver <dictionary> <board>
    ```
    Example:
    ```bash
    $ java -cp ".;..\algs4.jar" BoggleSolver tests/dictionary-algs4.txt tests/board-q.txt
    4 4
    S  N  R  T
    O  I  E  L
    E  Qu T  T
    R  S  A  T
    RES
    SER
    TIES
    ...
    TRIES
    Score = 84
    Total execution time for 10000 boards: 265ms
    ```
2. Running `BoggleGame`.
    ```bash
    $ cd BoggleGame
    $ java -cp ".;..\..\algs4.jar" BoggleGame
    ```

### Input Format
1. Dictionary
    - List of uppercase words (A-Z) separated by whitespace.
    ```
    <word1>
    <word2>
    ...
    ```
2. Board
    - Consists of two integers m (rows) and n (columns) followed by m × n uppercase characters.
    - Special case: The sequence "Qu" is represented as 'Q' or "Qu".
    - Example:
    ```
    4 4
    S N R T
    O I E L
    E Qu T T
    R S A T
    ```

### Performance
Given an m x n Boggle board and a dictionary with D words of average length L, the time and memory complexities are determined by the following:
1. Trie Construction:
    - Time: $O(DL)$
    - Memory: $O(DL)$
2. DFS:
    - Time: $O(mm \times 8^L)$
    - The traversal starts from each of the $mn$ cells on the board.
    - In the worst case, each DFS call has up to 8 possible moves (horizontal, vertical, diagonal), and the maximum depth of the DFS is the maximum word length L.
    - However, in practice, this complexity is reduced significantly due to:
        - Early termination when a prefix is not found in the trie.
        - Restriction on revisiting the same cell in a single word path.
        - Maximum L typically being small (e.g., 16 in a standard 4x4 Boggle board).
    - Memory: $O(mn + WL)$, where W is the number of valid words.
        - Visited cells are marked with a boolean array of size mn.
        - Maximum depth of the DFS stack is the maximum length of a word in the dictionary, which is L.
        - Results storage for W words of average length L. 