import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FlowEdge;

public class BaseballElimination 
{
    /* In a division consisting of n teams, team i has w[i] wins, l[i] losses, r[i] remaining games, 
     * and g[i][j] games left to play against team j.A team is mathematically eliminated if it cannot 
     * finish the season in (or tied for) first place. 
     * The goal is to determine exactly which teams are mathematically eliminated. 
    */
    // Immutable
    private final int[] w, l, r; // wins, losses, remaining games of each team 
    private final int[][] g; // games left between teams i and j
    private final ArrayList<String> teams;
    private final int maxwins;
    private final String leadingTeam;
    private final int remainingGames;
    private final int n; // no. of teams
    private final int v; // no. of vertices in the flow network
    private int t; // last team checked for elimination
    private FordFulkerson maxflow; // last maxflow created

    // create a baseball division from given filename in format specified below
    public BaseballElimination(String filename) 
    {
        if (filename == null) throw new IllegalArgumentException("Invalid argument");
        // read input file
        In in = new In(filename);
        n = in.readInt();
        w = new int[n];
        l = new int[n];
        r = new int[n];
        g = new int[n][n];
        teams = new ArrayList<>();
        int max = Integer.MIN_VALUE, remaining = 0;
        String lead = null;
        for (int index = 0; index < n; index++)
        {
            teams.add(in.readString());
            w[index] = in.readInt();
            if (w[index] > max) 
            {
                max = w[index];
                lead = teams.get(index);
            }
            l[index] = in.readInt();
            r[index] = in.readInt();
            for (int i = 0; i < n; i++)
            {
                g[index][i] = in.readInt();
                remaining += g[index][i];
            }
        }
        maxwins = max;
        remainingGames = remaining / 2;
        leadingTeam = lead;
        // 2 source vertices, n - 1 team vertices and (n - 1)C2 game vertices
        v = 2 + (n - 1) + ncr(n - 1, 2);
        t = -1;
    }

    private int ncr(int n, int r)
    {
        int sum = 1;
        for (int i = 1; i <= r; i++) 
            sum = sum * (n - r + i) / i;
        return sum;
    }

    public int numberOfTeams() // number of teams
    {
        return n;
    }

    public Iterable<String> teams() // all teams
    {
        return teams;
    }

    public int wins(String team) // number of wins for given team
    {
        int index = teams.indexOf(team);
        if (index < 0) throw new IllegalArgumentException("Invalid team");
        return w[index];
    }

    public int losses(String team) // number of losses for given team
    {
        int index = teams.indexOf(team);
        if (index < 0) throw new IllegalArgumentException("Invalid team");
        return l[index];
    }

    public int remaining(String team) // number of remaining games for given team
    {
        int index = teams.indexOf(team);
        if (index < 0) throw new IllegalArgumentException("Invalid team");
        return r[index];
    }

    public int against(String team1, String team2) // number of remaining games between team1 and team2
    {
        int i = teams.indexOf(team1), j = teams.indexOf(team2);
        if (i < 0 || j < 0) throw new IllegalArgumentException("Invalid team");
        return g[i][j];
    }

    private FordFulkerson mincut(int t)
    {
        FlowNetwork f = new FlowNetwork(v);
        // Vertices 0 to n - 1 are team vertices. vertex t is the sink vertex.
        for (int i = 0; i < n; i++)
        {
            if (i == t) continue;
            // capacity of i -> t is the max games that the team can win without eliminating team t
            int capacity = w[t] + r[t] - w[i];
            FlowEdge e = new FlowEdge(i, t, capacity);
            f.addEdge(e);
        }
        // vertex n is the source vertex s, rest are game vertices
        int s = n;
        int currVertex = n + 1;
        for (int i = 0; i < n; i++)
        {
            if (i == t) continue;
            for (int j = i + 1; j < n; j++)
            {
                if (j == t) continue;
                // edge from s to game vertex
                FlowEdge fromS = new FlowEdge(s, currVertex, g[i][j]);
                f.addEdge(fromS);
                // edge from game vertex to team vertices
                FlowEdge toTeam1 = new FlowEdge(currVertex, i, Double.MAX_VALUE);
                f.addEdge(toTeam1);
                FlowEdge toTeam2 = new FlowEdge(currVertex++, j, Double.MAX_VALUE);
                f.addEdge(toTeam2);
            }
        }
        FordFulkerson ff = new FordFulkerson(f, s, t);
        return ff;
    }

    public boolean isEliminated(String team) // is given team eliminated?
    {
        // trivial elimination
        if (wins(team) + remaining(team) < maxwins) return true;
        // non-trivial elimination
        int t = teams.indexOf(team);
        if (this.t != t) maxflow = mincut(t);
        this.t = t;
        /* team is eliminated iff any edge from s is not full in maxflow
         * i.e. maxflow < remaining games played between teams other than t
         */
        return (maxflow.value() < remainingGames - Arrays.stream(g[t]).sum());
    }
    // subset R of teams that eliminates given team; null if not eliminated
    public Iterable<String> certificateOfElimination(String team) 
    {
        if (!isEliminated(team)) return null;
        HashSet<String> elimCert = new HashSet<>();
        // trivial elimination
        if (wins(team) + remaining(team) < maxwins)
        {
            elimCert.add(leadingTeam);
            return elimCert;
        }
        // non-trivial elimination
        int t = teams.indexOf(team);
        if (this.t != t) maxflow = mincut(t);
        this.t = t;
        // teams is in elimCert iff it is in the s side of the mincut
        for (int i = 0; i < n; i++)
            if (maxflow.inCut(i)) elimCert.add(teams.get(i));
        return elimCert;
    }

    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination(args[0]);
        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) {
                    StdOut.print(t + " ");
                }
                StdOut.println("}");
            }
            else {
                StdOut.println(team + " is not eliminated");
            }
        }
    }
}
