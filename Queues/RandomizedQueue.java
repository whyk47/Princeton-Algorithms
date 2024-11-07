import java.util.Iterator;
import java.util.NoSuchElementException;
import edu.princeton.cs.algs4.StdRandom;

public class RandomizedQueue<Item> implements Iterable<Item> 
{
    private Item[] q;
    private int n, capacity;

    // construct an empty randomized queue
    public RandomizedQueue()
    {
        capacity = 1;
        n = 0;
        q = (Item[]) new Object[capacity];
    }

    // is the randomized queue empty?
    public boolean isEmpty()
    {
        return n == 0;
    }

    // return the number of items on the randomized queue
    public int size()
    {
        return n;
    }

    private boolean isFull()
    {
        return n == capacity;
    }

    private boolean mostlyEmpty()
    {
        return (float) n / capacity <= 0.25;
    }

    private void resize(int size)
    {
       Item[] copy;
       copy = (Item[]) new Object[size];
       for (int i = 0; i < n; i++)
       {
        copy[i] = q[i];
       }
       q = copy;
       capacity = size;
    }

    // add the item
    public void enqueue(Item item)
    {
        if (item == null)
        {
            throw new IllegalArgumentException();
        }
        if (isFull())
        {
            resize(capacity * 2);
        }
        q[n++] = item;
    }

    // remove and return a random item
    public Item dequeue()
    {
        if (isEmpty())
        {
            throw new NoSuchElementException();
        }
        int index = StdRandom.uniformInt(n);
        Item item = q[index];
        q[index] = q[--n];
        q[n] = null;
        if (mostlyEmpty() && capacity > 1)
        {
            resize(capacity / 2);
        }
        return item;
    }

    // return a random item (but do not remove it)
    public Item sample()
    {
        if (isEmpty())
        {
            throw new NoSuchElementException();
        }
        return q[StdRandom.uniformInt(n)];
    }

    private class RandomizedQueueIterator implements Iterator<Item>
    {
        private int i = n;
        private Item[] copy;

        public RandomizedQueueIterator()
        {
            copy = (Item[]) new Object[n];
            for (int i = 0; i < n; i++)
            {
                copy[i] = q[i];
            }
        }
        
        public boolean hasNext()
        {
            return i > 0;
        }

        public void remove()
        {
            throw new UnsupportedOperationException();
        }

        public Item next()
        {
            if (!hasNext())
            {
                throw new NoSuchElementException();
            }
            int index = StdRandom.uniformInt(i);
            Item item = copy[index];
            copy[index] = copy[--i];
            return item;
        }
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator()
    {
        return new RandomizedQueueIterator();
    }

    private void show()
    {
        System.out.println("Array");
        for (int i = 0; i < n; i++)
        {
            System.out.println(q[i]);
        }
    }

    // unit testing (required)
    public static void main(String[] args)
    {
        RandomizedQueue<Integer> rq = new RandomizedQueue<>();
        for (int i = 0; i < 10; i++)
        {
            rq.enqueue(i);
        }
        System.out.println(rq.isEmpty());
        System.out.println(rq.size());
        System.out.println("Sampling");
        for (int i = 0; i < 10; i++)
        {
            System.out.println(rq.sample());
        }
        System.out.println("Iterating");
        Iterator<Integer> rqi = rq.iterator();
        while (rqi.hasNext())
        {
            System.out.println(rqi.next());
        }
        System.out.println("Dequeueing");
        for (int i = 0; i < 10; i++)
        {
            System.out.println(rq.dequeue());
        }
        rqi.remove();
    }

}