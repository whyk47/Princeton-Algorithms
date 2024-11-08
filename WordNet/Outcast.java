import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Outcast 
{
    private final WordNet wordnet;

    public Outcast(WordNet wordnet)         // constructor takes a WordNet object
    {
        if (wordnet == null) throw new IllegalArgumentException("Argument cannot be null");
        this.wordnet = wordnet;
    }

    public String outcast(String[] nouns)   // given an array of WordNet nouns, return an outcast
    {
        if (nouns == null) throw new IllegalArgumentException("Argument cannot be null");
        if (nouns.length < 2) throw new IllegalArgumentException("Array must contain at least 2 nouns");
        String out = nouns[0];
        int outDist = Integer.MIN_VALUE;
        // for each noun, compute total dist from all other nouns. Outcast is noun with largest total dist
        for (String nounA : nouns)
        {
            int totalDist = 0;
            for (String nounB : nouns)
            {
                totalDist += wordnet.distance(nounA, nounB);
            }
            if (totalDist > outDist)
            {
                out = nounA;
                outDist = totalDist;
            }
        }
        return out;
    }
    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
    }
}
