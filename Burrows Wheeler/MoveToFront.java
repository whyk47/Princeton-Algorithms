import java.util.LinkedList;
import java.util.ArrayList;

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class MoveToFront {
    private static final int R = 256;
    private static final int bits = 8;

    // apply move-to-front encoding, reading from standard input and writing to standard output
    public static void encode() {
        LinkedList<Character> chars = new LinkedList<>();
        for (char c = 0; c < R; c++) chars.add(c);
        String s = StdIn.readAll();
        ArrayList<Integer> output = new ArrayList<>();
        for (char c : s.toCharArray()) {
            for (int i = 0; i < chars.size(); i++) 
                if (c == chars.get(i)) {
                    StdOut.print(i + " ");
                    chars.remove(i);
                    chars.addFirst(c);
                    break;
                } 
        }
    }

    // apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode() {
        LinkedList<Character> chars = new LinkedList<>();
        for (char c = 0; c < R; c++) chars.add(c);
        String s = StdIn.readAll();
        s = s.replaceAll("\r\n|\r|\n", "");
        String[] input = s.split(" ");
        StringBuilder t = new StringBuilder();
        for (String c : input) {
            int i = Integer.parseInt(c);
            t.append(chars.get(i));
            char d = chars.remove(i);
            chars.addFirst(d);
        }
        StdOut.print(t.toString());
    }

    // if args[0] is "-", apply move-to-front encoding
    // if args[0] is "+", apply move-to-front decoding
    public static void main(String[] args) {
        if (args[0].equals("-")) encode();
        else if (args[0].equals("+")) decode();
    }
}