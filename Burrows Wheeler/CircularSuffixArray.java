import java.util.Arrays;

public class CircularSuffixArray {
    private final String s;
    private final int[] index;

    private class CircularSuffix implements Comparable<CircularSuffix> {
        private final int start;

        public CircularSuffix(int start) {
            if (start < 0 || start >= length()) throw new IllegalArgumentException();
            this.start = start;
        }

        private int start() {
            return start;
        }

        private char getChar(int i) {
            if (i < 0 || i >= length()) throw new IllegalArgumentException();
            return s.charAt((i + start) % length());
        }

        public int compareTo(CircularSuffix that)
        {
            for (int i = 0; i < length(); i++) {
                int d = (this.getChar(i) - that.getChar(i));
                if (d != 0) return d;
            }
            return 0;
        }
    }

    // circular suffix array of s
    public CircularSuffixArray(String s) {
        if (s == null) throw new IllegalArgumentException();
        this.s = s;
        this.index = new int[length()];
        CircularSuffix[] suffixes = new CircularSuffix[length()];
        for (int i = 0; i < length(); i++) suffixes[i] = new CircularSuffix(i);
        Arrays.sort(suffixes);
        for (int i = 0; i < length(); i++) index[i] = suffixes[i].start();
    }

    // length of s
    public int length() {
        return s.length();
    }

    // returns index of ith sorted suffix
    public int index(int i) {
        if (i < 0 || i >= length()) throw new IllegalArgumentException();
        return index[i];
    }

    // unit testing (required)
    public static void main(String[] args) {
        CircularSuffixArray c = new CircularSuffixArray("ABRACADABRA!");
        System.out.println(c.length());
        System.out.println();
        for (int i = 0; i < c.length(); i++) System.out.println(c.index(i));
    }

}