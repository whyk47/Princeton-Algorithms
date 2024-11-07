// implementation of weighted Quick Union with path compression
public class UF 
{
    // instance varible
    private int[] id;
    private int[] size;
    private int[] largest;

    // constructor: create an id array, where id of each object is initially set to itself
    public UF(int N) 
    {
        id = new int[N];
        size = new int[N];
        largest = new int[N];
        for (int i = 0; i < N; i++) 
        {
            id[i] = i;
            size[i] = 1;
            largest[i] = i;
        }
    } 

    // helper function to find the root of an object
    private int root(int i) 
    {
        while (i != id[i]) 
        {
            id[i] = id[id[i]];
            i = id[i];
        }
        return i;
    }

    // returns true if p and q are connected
    public boolean connected(int p, int q) 
    {
        return root(p) == root(q);
    }

    // change root of p to root of q
    public void union(int p, int q) 
    {
        int root_of_p = root(p);
        int root_of_q = root(q);
        if (root_of_p == root_of_q) return;
        if (size[root_of_p] < size[root_of_q]) 
        {
            id[root_of_p] = root_of_q;
            size[root_of_q] += size[root_of_p];
            largest[root_of_q] = Math.max(largest[root_of_p], largest[root_of_q]);
        } 
        else 
        {
            id[root_of_q] = root_of_p;
            size[root_of_p] += size[root_of_q];
            largest[root_of_p] = Math.max(largest[root_of_p], largest[root_of_q]);
        }
        
    }

    public int find(int p)
    {
        return largest[root(p)];
    }

    public static void main(String[] args) {
        UF union_find = new UF(10);
        union_find.union(0, 1);
        union_find.union(0, 2);
        union_find.union(2, 3);
        String connection = String.valueOf(union_find.connected(0, 3));
        System.out.println(connection);
        connection = String.valueOf(union_find.connected(1, 9));
        System.out.println(connection);
        System.out.println(union_find.find(0));
    }
}
