import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdRandom;

public class Permutation {
    public static void main(String[] args)
    {
        RandomizedQueue<String> q = new RandomizedQueue<>();
        int k = Integer.parseInt(args[0]);
        int n = 0;
        while (!StdIn.isEmpty()) 
        {
            String value = StdIn.readString();
            n++;
            if (q.size() < k)
            {
                q.enqueue(value);
            }
            else if (q.size() >= k && StdRandom.uniformInt(n) < k)
            {
                q.dequeue();
                q.enqueue(value);
            }
        }
        for (int i = 0; i < k; i++)
        {
            System.out.println(q.dequeue());
        }
    }
}
