import java.util.Arrays;
import java.util.HashMap;

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Queue;

public class BurrowsWheeler {

    // apply Burrows-Wheeler transform,
    // reading from standard input and writing to standard output 
    public static void transform() {
        String s = StdIn.readAll();
        s = s.replaceAll("\r\n|\r|\n", "");
        CircularSuffixArray c = new CircularSuffixArray(s);
        StringBuilder t = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            int index;
            if (c.index(i) == 0) {
                StdOut.println(i);
                index = c.length() - 1;
            }
            else index = c.index(i) - 1;
            t.append(s.charAt(index));
        }
        StdOut.print(t.toString());
    }

    // apply Burrows-Wheeler inverse transform,
    // reading from standard input and writing to standard output
    public static void inverseTransform() {
        int first = StdIn.readInt();
        String s = StdIn.readAll();
        s = s.replaceAll("\r\n|\r|\n", "");
        char[] t = s.toCharArray();
        char[] sorted = t.clone();
        Arrays.sort(sorted);
        HashMap<Character, Queue<Integer>> h = new HashMap<Character, Queue<Integer>>();
        for (int i = 0; i < t.length; i++) {
            if (!h.containsKey(t[i])) h.put(t[i], new Queue<Integer>());
            Queue<Integer> q = h.get(t[i]);
            q.enqueue(i);
        }
        int[] next = new int[t.length];
        for (int i = 0; i < sorted.length; i++) {
            Queue<Integer> q = h.get(sorted[i]);
            next[i] = q.dequeue();
            if (q.isEmpty()) h.remove(sorted[i]);
        }
        StdOut.print(sorted[first]);
        for (int i = next[first], count = 1; i != first || count < t.length; i = next[i], count++) StdOut.print(sorted[i]);
    }

    // if args[0] is "-", apply Burrows-Wheeler transform
    // if args[0] is "+", apply Burrows-Wheeler inverse transform
    public static void main(String[] args) {    
        if (args[0].equals("-")) transform();
        else if (args[0].equals("+")) inverseTransform();
    }

}