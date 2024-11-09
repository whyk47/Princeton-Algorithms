import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.HashSet;

public class BoggleSolver
{
    private static final int R = 26;
    private final TrieSet dict;
    private HashSet<String> validWords;
    private boolean[][] marked;

    private static class Node
    {
        private boolean isWord = false;
        private Node[] next = new Node[R];
    }

    private class TrieSet 
    {
        /* R way trie. Store characters in nodes (not keys).
        * Each node has R children, one for each possible character. 
        * Time: O(L), Space: O((R + 1)N)
        */
        private static final int OFFSET = 65;
        private Node root = new Node();

        private int getIndex(char c)
        { return (int) c - OFFSET; }

        public void put(String key)
        { root = put(root, key, 0); }

        private Node put(Node x, String key, int d)
        {
            if (x == null) x = new Node();
            if (d == key.length()) 
            { 
                x.isWord = true; 
                return x; 
            }
            char c = key.charAt(d);
            x.next[getIndex(c)] = put(x.next[getIndex(c)], key, d + 1);
            return x;
        }

        public boolean contains(String key)
        {
            Node x = get(root, key, 0);
            if (x == null) return false;
            return x.isWord;
        }

        public boolean contains(Node x)
        {
            if (x == null) return false;
            return x.isWord;
        }

        private Node get(Node x, String key, int d)
        {
            if (x == null) return null;
            if (d == key.length()) return x;
            char c = key.charAt(d);
            return get(x.next[getIndex(c)], key, d + 1);
        }

        public Node nextNode(Node x, char c)
        { return x.next[getIndex(c)]; }

        public Node root()
        { return root; }
    }

    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary)
    {
        dict = new TrieSet();
        for (String word : dictionary)
            dict.put(word);
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board)
    {
        validWords = new HashSet<>();
        marked = new boolean[board.rows()][board.cols()];
        // enumerate all simple paths in the Boggle graph
        for (int row = 0; row < board.rows(); row++)
            for (int col = 0; col < board.cols(); col++)
                dfs(row, col, new StringBuilder(), dict.root(), board);
        return validWords;
    }

    private void dfs(int row, int col, StringBuilder word, Node curr, BoggleBoard board)
    {
        if (row < 0 || row >= board.rows() || col < 0 || col >= board.cols()) return;
        if (marked[row][col]) return;
        char c = board.getLetter(row, col);
        // if prefix does not correspond to any word in dict, no need to expand the path further.
        Node next = dict.nextNode(curr, c);
        if (c == 'Q' && next != null) next = dict.nextNode(next, 'U');
        if (next == null) return;
        marked[row][col] = true;
        word.append(c);
        if (c == 'Q') word.append('U');
        if (dict.contains(next) && word.length() > 2) validWords.add(word.toString());
        for (int x = -1; x < 2; x++)
            for (int y = -1; y < 2; y++)
                dfs(row + x, col + y, word, next, board);
        marked[row][col] = false;
        word.deleteCharAt(word.length() - 1);
        if (c == 'Q') word.deleteCharAt(word.length() - 1);
    }

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word)
    {
        int len = word.length();
        if (len < 3 || !dict.contains(word)) return 0;
        if (len < 5) return 1;
        if (len < 6) return 2;
        if (len < 7) return 3;
        if (len < 8) return 5;
        return 11;
    }

    public static void main(String[] args) 
    {
        /* Test client takes the filename of a dictionary and the filename of a Boggle board 
         * and prints out all valid words for the given board using the given dictionary 
         */
        final long startTime = System.currentTimeMillis();
        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard(args[1]);
        System.out.println(board.toString());
        int score = 0;
        for (String word : solver.getAllValidWords(board)) 
        {
            StdOut.println(word);
            score += solver.scoreOf(word);
        }
        StdOut.println("Score = " + score);
        // Time taken to solve 10000 random 4x4 boggle boards
        int numBoards = 10000;
        for (int i = 0; i < numBoards; i++)
        {
            board = new BoggleBoard();
            solver.getAllValidWords(board);
        }
        final long endTime = System.currentTimeMillis();
        System.out.println("Total execution time for " + numBoards + " boards: " + (endTime - startTime) + "ms");
    }
}