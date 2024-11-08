import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Queue;
import java.util.Arrays;
import java.util.HashSet;
import java.util.HashMap;

public class SAP 
{
    private final Digraph dg;
    private HashSet<Integer> markedv, markedw;
    private HashMap<Integer, Integer> edgeTov, edgeTow;
    private Path lastPath;

    private class Path
    {
        private int length, ancestor;
        private Iterable<Integer> v, w;

        public Path(Iterable<Integer> v, Iterable<Integer> w)
        {
            this.v = v;
            this.w = w;
        }

        private void pathLen(int ancestor)
        {
            this.ancestor = ancestor;
            // Find path length by traversing edges
            int dist = 0;
            for (int x = ancestor; edgeTov.containsKey(x); x = edgeTov.get(x))
            {
                dist++;
            } 
            for (int y = ancestor; edgeTow.containsKey(y); y = edgeTow.get(y))
            {
                dist++;
            } 
            length = dist;
        }

        private boolean equals(Path that)
        {
            return (this.v.equals(that.v) && this.w.equals(that.w)) || (this.w.equals(that.v) && this.v.equals(that.w));
        }
    }

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G)
    {
        dg = new Digraph(G);
        markedv = new HashSet<>();
        markedw = new HashSet<>();
        edgeTov = new HashMap<>();
        edgeTow = new HashMap<>();
        lastPath = new Path(null, null);
    }
    
    private Path shortestPath(int v, int w)
    {
        Integer[] x = {v};
        Integer[] y = {w};
        return shortestPath(Arrays.asList(x), Arrays.asList(y));
    }

    private Path shortestPath(Iterable<Integer> v, Iterable<Integer> w)
    {
        // check args
        if (v == null || w == null) throw new IllegalArgumentException("Argument cannot be null");
        for (Integer x : v)
        {
            if (x == null || x >= dg.V() || x < 0) throw new IllegalArgumentException("Iterable contains invalid item");
        }
        for (Integer x : w)
        {
            if (x == null || x >= dg.V() || x < 0) throw new IllegalArgumentException("Iterable contains invalid item");
        }        
        Path p = new Path(v, w);
        if (p.equals(lastPath)) return lastPath;
        // Find common ancestor with bfs
        return bfs(v, w);
    }

    private Path bfs(Iterable<Integer> x, Iterable<Integer> y)
    {
        markedv.clear();
        markedw.clear();
        edgeTov.clear();
        edgeTow.clear();
        // performs bfs in lockstep to find common ancestor (O(E + V))
        Queue<Integer> qv = new Queue<Integer>();
        Queue<Integer> qw = new Queue<Integer>();
        Queue<Integer> nextv = new Queue<Integer>();
        Queue<Integer> nextw = new Queue<Integer>();
        HashSet<Integer> setv = new HashSet<>();
        for (int v : x)
        {
            qv.enqueue(v);
            markedv.add(v);
            setv.add(v);
        }
        for (int w : y)
        {
            if (setv.contains(w))
            {
                Path p = new Path(x, y);
                p.pathLen(w);
                return p;
            }
            qw.enqueue(w);
            markedw.add(w);
        }
        int v, w;
        int dist = 1;
        Path best = new Path(null, null);
        best.length = Integer.MAX_VALUE;
        while (!qv.isEmpty() || !qw.isEmpty())
        {
            while (!qv.isEmpty())
            {
                v = qv.dequeue();
                for (int adjv : dg.adj(v))
                {
                    if (!markedv.contains(adjv))
                    {
                        nextv.enqueue(adjv);
                        markedv.add(adjv);
                        edgeTov.put(adjv, v);
                    }
                    if (markedw.contains(adjv))
                    {
                        Path p = new Path(x, y);
                        p.pathLen(adjv);
                        if (p.length < best.length) best = p;
                    }
                }
            }
            qv = nextv;
            nextv = new Queue<>();

            while (!qw.isEmpty())
            {
                w = qw.dequeue();
                for (int adjw : dg.adj(w))
                {
                    if (!markedw.contains(adjw))
                    {
                        nextw.enqueue(adjw);
                        markedw.add(adjw);
                        edgeTow.put(adjw, w);
                    }
                    if (markedv.contains(adjw))
                    {
                        Path p = new Path(x, y);
                        p.pathLen(adjw);
                        if (p.length < best.length) best = p;
                    }
                }
            }
            qw = nextw;
            nextw = new Queue<>();
            dist++;
            if (dist >= best.length) return best;
        }
        if (best.length < Integer.MAX_VALUE) return best;
        return null;
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w)
    {
        Path p = shortestPath(v, w);
        if (p == null) return -1;
        return p.length;
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w)
    {
        Path p = shortestPath(v, w);
        if (p == null) return -1;
        return p.ancestor;
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w)
    {
        Path p = shortestPath(v, w);
        if (p == null) return -1;
        return p.length;
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w)
    {
        Path p = shortestPath(v, w);
        if (p == null) return -1;
        return p.ancestor;
    }

    // do unit testing of this class
    public static void main(String[] args) 
    {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length   = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}