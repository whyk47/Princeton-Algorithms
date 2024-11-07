import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> 
{
    private Node first, last;
    private int size;

    private class Node 
    {
        Item item;
        Node next, previous;
        private Node(Item item, Node next, Node previous)
        {
            this.item = item;
            this.next = next;
            this.previous = previous;
        }
    }

    // construct an empty deque
    public Deque()
    {
        first = last = null;
        size = 0;
    }

    // is the deque empty?
    public boolean isEmpty()
    {
        return first == null;
    }

    // return the number of items on the deque
    public int size()
    {
        return size;
    }

    // add the item to the front
    public void addFirst(Item item)
    {
        if (item == null)
        {
            throw new IllegalArgumentException();
        }
        Node oldfirst = first;
        first = new Node(item, oldfirst, null);
        if (oldfirst == null)
        {
            last = first;
        }
        else
        {
            oldfirst.previous = first;
        }
        size++;
    }

    // add the item to the back
    public void addLast(Item item)
    {
        if (item == null)
        {
            throw new IllegalArgumentException();
        }
        Node oldlast = last;
        last = new Node(item, null, oldlast);
        if (oldlast == null)
        {
            first = last;
        }
        else
        {
            oldlast.next = last;
        }
        size++;
    }

    // remove and return the item from the front
    public Item removeFirst()
    {
        if (isEmpty())
        {
            throw new NoSuchElementException();
        }
        Node oldfirst = first;
        first = first.next;
        if (first == null)
        {
            last = first;
        }
        else
        {
            first.previous = null;
        }
        oldfirst.next = null;
        size--;
        return oldfirst.item;
    }

    // remove and return the item from the back
    public Item removeLast()
    {
        if (isEmpty())
        {
            throw new NoSuchElementException();
        }
        Node oldlast = last;
        last = last.previous;
        if (last == null)
        {
            first = last;
        }
        else
        {
            last.next = null;
        }
        oldlast.previous = null;
        size--;
        return oldlast.item;
    }

    private class DequeIterator implements Iterator<Item>
    {
        private Node current = first;

        public boolean hasNext()
        {
            return current != null;
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
            Item item = current.item;
            current = current.next;
            return item;
        }
    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator()
    {
        return new DequeIterator();
    }

    // unit testing (required)
    public static void main(String[] args)
    {
        Deque<Integer> d = new Deque<Integer>();
        d.addFirst(1);
        System.out.println(d.isEmpty());
        d.addLast(2);
        System.out.println(d.removeLast());
        System.out.println(d.removeFirst());
        System.out.println(d.isEmpty());
        for (int i = 0; i < 5; i++)
        {
            d.addLast(i);
            d.addFirst(i);
        }
        Iterator<Integer> di = d.iterator();
        while (di.hasNext())
        {
            System.out.println(di.next());
        }
    }

}