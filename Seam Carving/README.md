# Seam Carving

[Full Project Specification](https://coursera.cs.princeton.edu/algs4/assignments/seam/specification.php)

### Overview
Seam carving is a content-aware image resizing technique that reduces the width or height of an image by removing low-importance seams. Unlike traditional resizing methods (cropping or scaling), seam carving preserves important features and the image's overall structure.

### Seam Identification
- Finding a seam of minimum energy is similar to finding the shortest path in an edge weighted digraph.
- For a vertical seam, we find the shortest path from any of the W pixels in the top row to any of the W pixels in the bottom row using a dynamic programming approach.
- The energy of each pixel is calculated using the dual-gradient energy function to identify areas of high importance.

### Usage
- Running `SeamCarver`.
    ```bash
    $ java -cp ".;..\algs4.jar" SeamCarver <input_image> <output_width> <output_height> <output_file>
    ```
- Example:
    ```bash
    $ java -cp ".;..\algs4.jar" SeamCarver tests/image.png 400 200 tests/edited.png
    ```

### Input Format
- Image: JPEG, PNG, GIF, BMP, and TIFF files supported.

### Performance
- Each seam finding and removal operation has a time complexity of $O(width \times height)$