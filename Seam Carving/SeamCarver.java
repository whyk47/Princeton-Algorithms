/* Possible improvements:
 * 1. no need to create 2d array for energy. Just calculate energy as needed
 * 2. Store picture as 2d array of rgb ints in row major order. Use arraycopy to shift entries to remove seams
 */

import edu.princeton.cs.algs4.Picture;

public class SeamCarver 
{
    private final int BORDER = 1000;
    private final boolean VERTICAL = true;
    private final boolean HORIZONTAL = false;
    private Picture picture;
    private double[][] energy;
    private double[][] distTo;
    private int[][] edgeTo;
    private boolean orientation;

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture)
    {
        if (picture == null) throw new IllegalArgumentException("Argument cannot be null");
        this.picture = new Picture(picture);
        orientation = VERTICAL;
        getEnergies();
    }

    private void getEnergies()
    {
        if (energy == null)
        {
            energy = new double[w()][h()];
            for (int x = 0; x < w(); x++)
            {
                for (int y = 0; y < h(); y++)
                {
                    energy[x][y] = getEnergy(x, y);
                }
            }
        }
    }

    // current picture
    public Picture picture()
    {
        if (!isVertical()) transpose();
        return new Picture(picture);
    }

    // width of current picture
    public int width()
    {
        if (isVertical()) return w();
        return h();
    }

    // height of current picture
    public int height()
    {
        if (isVertical()) return h();
        return w();
    }

    // width and height of picture (depends on orientation)
    private int w()
    {
        return picture.width(); 
    }

    private int h()
    {
        return picture.height();
    }

    private boolean isVertical()
    {
        return orientation == VERTICAL;
    }

    private void transpose()
    {
        double[][] transpose = new double[h()][w()];
        Picture p = new Picture(h(), w());
        for (int x = 0; x < h(); x++)
        {
            for (int y = 0; y < w(); y++)
            {
                transpose[x][y] = energy[y][x];
                int rgb = picture.getRGB(y, x);
                p.setRGB(x, y, rgb);
            }
        }
        energy = transpose;
        picture = p;
        orientation = !orientation;
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y)
    {
        if (isVertical()) return getEnergy(x, y);
        return getEnergy(y, x);
    }
    
    // calculates energy of pixel at column x and row y of the image in the curret orientation 
    private double getEnergy(int x, int y)
    {
        if (x < 0 || y < 0 || x >= w() || y >= h()) throw new IllegalArgumentException("Pixel Coordinates out of range");
        if (x == 0 || y == 0 || x == w() - 1  || y == h() - 1) return BORDER; 
        int xGrad = gradient(getColour(x + 1, y), getColour(x - 1, y));
        int yGrad = gradient(getColour(x, y + 1), getColour(x, y - 1));
        return Math.sqrt(xGrad + yGrad);
    }

    private int[] getColour(int x, int y)
    {
        int rgb = picture.getRGB(x, y);
        int r = (rgb >> 16) & 0xFF;
        int g = (rgb >>  8) & 0xFF;
        int b = (rgb >>  0) & 0xFF;
        int[] colour = {r, g, b};
        return colour;
    }

    private int gradient(int[] i, int[] j)
    {
        assert i.length == 3 && j.length == 3;
        int sum = 0;
        for (int k = 0; k < 3; k++)
            sum += ((i[k] - j[k]) * (i[k] - j[k]));
        return sum;
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam()
    {
        if(isVertical()) transpose();
        return getSeam();
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam()
    {
        if (!isVertical()) transpose();
        return getSeam();
    }

    private int[] getSeam()
    {
        distTo = new double[w()][h()];
        edgeTo = new int[w()][h()];
        for (int x = 0; x < w(); x++)
        {
            distTo[x][0] = 1000;
            edgeTo[x][0] = -1;
            for (int y = 1; y < h(); y++)
            {
                distTo[x][y] = Double.POSITIVE_INFINITY;
            }
        } 
        
        for (int y = 0; y < h() - 1; y++)
        {
            for (int x = 0; x < w(); x++)
            {
                relax(x, y);
            }
        }

        int minx = 0;
        for (int x = 1; x < w(); x++)  
            if (distTo[minx][h() - 1] > distTo[x][h() - 1]) minx = x;   

        int[] path = new int[h()];
        for (int x = minx, y = h() - 1; x >= 0; x = edgeTo[x][y--])
        {
            path[y] = x;
        }
        return path;
    }

    private void relax(int x, int y)
    {
        int j = y + 1;
        for (int i = x - 1; i < x + 2; i++)
        {
            if (i >= 0 && i < w() && distTo[i][j] > distTo[x][y] + energy[i][j])
            {
                distTo[i][j] = distTo[x][y] + energy[i][j];
                edgeTo[i][j] = x;
            }
        }
    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam)
    {
        if (isVertical()) transpose();
        removeSeam(seam);
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam)
    {
        if (!isVertical()) transpose();
        removeSeam(seam);
    }

    private void removeSeam(int[] seam)
    {
        if (!validSeam(seam)) throw new IllegalArgumentException("Invalid seam");
        // create new picture and copy all pixels and energies except those in the seam
        Picture p = new Picture(w() - 1, h());
        double[][] e = new double[w() - 1][h()];
        for (int y = 0; y < h(); y++)
        {
            for (int x = 0; x < seam[y]; x++)
            {
                int rgb = picture.getRGB(x, y);
                p.setRGB(x, y, rgb);
                e[x][y] = energy[x][y];
            }
            for (int x = seam[y] + 1; x < w(); x++)
            {
                int rgb = picture.getRGB(x, y);
                p.setRGB(x - 1, y, rgb);
                e[x - 1][y] = energy[x][y];
            }
        }
        picture = p;
        energy = e;
        // update energies of pixels adjacent to seam
        for (int y = 0; y < h(); y++)
        {
            if (seam[y] > 0) energy[seam[y] - 1][y] = getEnergy(seam[y] - 1, y);
            if (seam[y] < w()) energy[seam[y]][y] = getEnergy(seam[y], y);
        }
    }

    private boolean validSeam(int[] seam)
    {
        if (seam == null || seam.length != h() || w() < 2) return false;
        for (int i = 0; i < h() - 1 ; i++)
        {
            if (seam[i] < 0 || seam[i] >= w() || Math.abs(seam[i] - seam[i + 1]) > 1) return false;
        } 
        if (seam[h() - 1] < 0 || seam[h() - 1] >= w()) return false;
        return true;
    }


    //  unit testing (optional)
    public static void main(String[] args)
    {
        Picture p = new Picture(args[0]);
        SeamCarver s = new SeamCarver(p);
        Integer outputWidth = Integer.parseInt(args[1]);
        Integer outputHeight = Integer.parseInt(args[2]);
        Integer verticalSeams = s.width() - outputWidth;
        Integer horizontalSeams = s.height() - outputHeight;
        if (verticalSeams < 0 || horizontalSeams < 0) throw new IllegalArgumentException("Invalid output dimensions");
        System.out.println(s.width() + "x" + s.height() + " -> " + outputWidth + "x" + outputHeight);
        System.out.println("Removing " + horizontalSeams + " horizontal seams and " + verticalSeams + " vertical seams");
        for (int i = 0; i < horizontalSeams; i++) {
            int[] seam = s.findHorizontalSeam();
            s.removeHorizontalSeam(seam);
        }
        for (int i = 0; i < verticalSeams; i++) {
            int[] seam = s.findVerticalSeam();
            s.removeVerticalSeam(seam);
        }
        s.picture().show();
        if (args.length == 4) {
            s.picture().save(args[3]);
        }
    }
}
