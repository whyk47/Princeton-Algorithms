import java.util.NoSuchElementException;

public class LLRB<Key extends Comparable<Key>, Value>
{
    /* Left leaning red-black tree. Self balancing tree supporting get, insert and del in logn time*/ 
    Node root;
    int N;
    private static final boolean RED = true;
    private static final boolean BLACK = false;

    public LLRB()
    {
        root = null;
        N = 0;
    }

    private boolean isRed(Node x)
    {
        if (x == null) return false;
        return x.colour == RED;
    }

    private class Node
    {
        Key key;
        Value val;
        Node left, right;
        boolean colour;

        public Node(Key key, Value val, boolean colour)
        {
            this.key = key;
            this.val = val;
            // colour of parent link
            this.colour = colour;
        }
    }

    private Node rotateLeft(Node h)
    {
        assert isRed(h.right);
        Node oldRight = h.right;
        h.right = oldRight.left;
        oldRight.left = h;
        oldRight.colour = h.colour;
        h.colour = RED;
        return oldRight;
    }

    private Node rotateRight(Node h)
    {
        assert isRed(h.left);
        Node oldLeft = h.left;
        h.left = oldLeft.right;
        oldLeft.right = h;
        oldLeft.colour = h.colour;
        h.colour = RED;
        return oldLeft;
    }

    private void flipColours(Node h)
    {
        h.colour = !h.colour;
        h.left.colour = !h.left.colour;
        h.right.colour = !h.right.colour;
    }

    private Node moveRedRight(Node h)
    {
        flipColours(h);
        if (isRed(h.left.left))
        {
            h = rotateRight(h);
            flipColours(h);
        }
        return h;
    }

    private Node moveRedLeft(Node h)
    {
        flipColours(h);
        if (isRed(h.right.left))
        {
            h.right = rotateRight(h.right);
            h = rotateLeft(h);
            flipColours(h);
        }
        return h;
    }

    private Node fixup(Node h)
    {
        // right leaning red link -> rotate left
        if (isRed(h.right) && !isRed(h.left)) h = rotateLeft(h);
        // 2 left leaning links in a row -> rotate top link right
        if (isRed(h.left) && isRed(h.left.left)) h = rotateRight(h);
        // both children red -> flip colours
        if (isRed(h.left) && isRed(h.right)) flipColours(h);
        return h;
    }

    private Node put(Node h, Key key, Value val)
    {
        // recursively navigates the tree to find the right spot
        // insert new node in vacant spot and colour it red
        if (h == null) return new Node(key, val, RED);
        int cmp = key.compareTo(h.key);
        if (cmp < 0) h.left = put(h.left, key, val);
        else if (cmp > 0) h.right = put(h.right, key, val);
        // update node if it already exists
        else h.val = val;
        // Recursively passes red links up the tree
        return fixup(h);
    }

    public void insert(Key key, Value val)
    {
        if (key == null || val == null) throw new IllegalArgumentException();
        if (get(key) == null) N++;
        root = put(root, key, val);
    }

    private Value get(Node x, Key key)
    {
        while (x != null)
        {
            int cmp = key.compareTo(x.key);
            if (cmp < 0) x = x.left;
            else if (cmp > 0) x = x.right;
            else return x.val;
        }
        return null;
    }

    public Value get(Key key)
    {
        return get(root, key);
    }

    public void delMin()
    {
        if (root == null) throw new NoSuchElementException();
        root = delMin(root);
        N--;
        if (root != null) root.colour = BLACK;
    }

    private Node delMin(Node h)
    {
        if (h.left == null) return null;
        if (!isRed(h.left) && !isRed(h.left.left)) h = moveRedLeft(h);
        h.left = delMin(h.left);
        return fixup(h);
    }

    public Value getMin()
    {
        return min(root).val;
    }

    private Node min(Node h)
    {
        while (h.left != null) h = h.left;
        return h;
    }

    private Node del(Node h, Key key)
    {
        /* perform rotations and colourflips on the way down to ensure search ends at a 3-node
         * so we can just delete the node at the bottom. Then fix the tree on the way up.
         * Results in an error if key does not exist. */ 
        if (key.compareTo(h.key) < 0)
        {
            if (!isRed(h.left) && !isRed(h.left.left)) h = moveRedLeft(h);
            h.left = del(h.left, key);
        }
        else
        {
            if (isRed(h.left)) h = rotateRight(h);
            if (key.compareTo(h.key) == 0 && h.right == null) return null;
            if (!isRed(h.right) && !isRed(h.right.left)) h = moveRedRight(h);
            if (key.compareTo(h.key) == 0) 
            {
                h.val = get(h.right, min(h.right).key);
                h.key = min(h.right).key;
                h.right = delMin(h.right);
            }
            else h.right = del(h.right, key);
        }
        return fixup(h);
    }

    public void del(Key key)
    {
        root = del(root, key);
        root.colour = BLACK;
        N--;
    }

    public int size()
    {
        return N;
    }

    private void inorder(Node x)
    {
        if (x == null) return;
        inorder(x.left);
        System.out.println(x.key);
        inorder(x.right);
    }

    public void inorder()
    {
        inorder(root);
    }

    public static void main(String[] args)
    {
        LLRB<Integer, Integer> r = new LLRB<>();
        for (int i = 0; i < 100; i++)
        {
            r.insert(i, i);
        }
        for (int i = 0; i < 50; i++)
        {
            r.del(i * 2);
        }
        r.inorder();
    }
}
