import java.util.ArrayList;
import java.util.HashSet;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.Topological;
import edu.princeton.cs.algs4.Queue;

public class WordNet 
{
    private HashSet<String> nouns;
    private ArrayList<HashSet<String>> synsets;
    private Digraph hypernyms, reverse;
    private SAP sap;
    private boolean[] marked;
    private int root;
    private Queue<Path> cache;
    private final int CAPACITY = 100;

    private class Path
    {
        private String nounA, nounB;
        private int ancestor, length;

        public Path(String nounA, String nounB)
        {
            this.nounA = nounA;
            this.nounB = nounB;
        }

        public boolean equals(Object y)
        {
            if (y == null || y.getClass() != this.getClass()) return false;
            Path that = (Path) y;
            return (this.nounA.equals(that.nounA) && this.nounB.equals(that.nounB)) || 
            (this.nounA.equals(that.nounB) && this.nounB.equals(that.nounA));
        }
    }

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms)
    {
        if (synsets == null || hypernyms == null) throw new IllegalArgumentException("Argument cannot be null");
        // Stores synsets in an arraylist of string hashsets and updates noun set (O(nlogn)).
        In syn = new In(synsets);
        this.synsets = new ArrayList<>();
        nouns = new HashSet<>();
        while (syn.hasNextLine())
        {
            String[] entries = syn.readLine().split(",");
            String[] synset = entries[1].split(" ");
            HashSet<String> s = new HashSet<String>();
            for (String noun : synset)
            {
                nouns.add(noun);
                s.add(noun);
            } 
            this.synsets.add(s);
        }    
        
        // stores hypernym relationships in a digraph (O(E))
        In hyper = new In(hypernyms);
        this.hypernyms = new Digraph(this.synsets.size());
        while (hyper.hasNextLine())
        {
            String[] vertices = hyper.readLine().split(",");
            int source = Integer.parseInt(vertices[0]);
            for (int i = 1; i < vertices.length; i++)
            {
                this.hypernyms.addEdge(source, Integer.parseInt(vertices[i]));
            }
        }   
        // topological sort to check if graph is a DAG (O(E + V))
        Topological topo = new Topological(this.hypernyms);
        if (!topo.hasOrder()) throw new IllegalArgumentException("Hypernyms must correspond to a rooted DAG");
        // check if graph is rooted, i.e. there is 1 vertex reachable from all other vertices (O(V))
        Iterable<Integer> revPost = topo.order();
        if (!isRooted(revPost)) throw new IllegalArgumentException("Hypernyms must correspond to a rooted DAG");
        
        sap = new SAP(this.hypernyms);
        reverse = this.hypernyms.reverse();
        marked = new boolean[this.synsets.size()];
        cache = new Queue<>();
    }

    private boolean isRooted(Iterable<Integer> revPost)
    {
        // DAG is rooted iff there is exactly 1 vertex with outdegree 0
        int count = 0;
        for (int i : revPost)
        {
            if (hypernyms.outdegree(i) == 0)
            {
                root = i;
                count++;
            } 
        }
        return count == 1;
    }
 
    // returns all WordNet nouns
    public Iterable<String> nouns()
    {
        return nouns;
    }
 
    // is the word a WordNet noun?
    public boolean isNoun(String word)
    {
        if (word == null) throw new IllegalArgumentException("Argument cannot be null");
        return nouns.contains(word);
    }

    private Path getPath(String nounA, String nounB)
    {
        if (nounA == null || nounB == null) throw new IllegalArgumentException("Argument cannot be null");
        if (!isNoun(nounB) || !isNoun(nounA)) throw new IllegalArgumentException("Argument not a WordNet noun");
        Path p = new Path(nounA, nounB);
        for (Path q : cache)
            if (p.equals(q)) return q;
        // Perform DFS on the reverse graph to find all synsets with nounA and nounB respectively (O(E + V))
        ArrayList<Integer> synsA = new ArrayList<>();
        ArrayList<Integer> synsB = new ArrayList<>();
        dfs(root, nounA, nounB, synsA, synsB);
        int ancestor = sap.ancestor(synsA, synsB);
        int len = sap.length(synsA, synsB);
        if (ancestor == -1 || len == -1) return null;
        p.ancestor = ancestor;
        p.length = len;
        cache.enqueue(p);
        while (cache.size() > CAPACITY) cache.dequeue();
        return p;
    }

    private void dfs(int v, String nounA, String nounB, ArrayList<Integer> synsA, ArrayList<Integer> synsB)
    {
        if (marked[v]) return;
        HashSet<String> synset = synsets.get(v);
        if (synset.contains(nounB)) synsB.add(v);
        if (synset.contains(nounA)) synsA.add(v);
        for (int w : reverse.adj(v))
        {
            if (!marked[w]) dfs(w, nounA, nounB, synsA, synsB);
        }
    }
 
    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB)
    {
        return getPath(nounA, nounB).length;
    }
 
    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB)
    {
        int ancestorID = getPath(nounA, nounB).ancestor;
        StringBuilder synset = new StringBuilder();
        for (String noun : synsets.get(ancestorID)) synset.append(noun + " ");
        synset.deleteCharAt(synset.length() - 1);
        return synset.toString();
    }

    // do unit testing of this class
    public static void main(String[] args)
    {
        // empty
    }
 }