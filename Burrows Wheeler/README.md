# Burrows Wheeler Data Compression Algorithm

[Full Project Specification](https://coursera.cs.princeton.edu/algs4/assignments/burrows/specification.php)

### Overview
The Burrows–Wheeler data compression algorithm improves compression efficiency through three main steps:
1. Burrows–Wheeler Transform (BWT): Rearranges input characters to group similar ones together.
2. Move-to-Front Encoding (MTF): Transforms the BWT output into a format where frequently occurring characters have smaller indices.
3. Huffman Compression: Compresses the MTF output using variable-length codes.
Steps 1 & 2 are implemented in this project.

### Classes
- `BurrowsWheeler`: Uses `CircularSuffixArray` to convert input text into a format suitable for compression by clustering similar characters.
- `MoveToFront`: Efficiently encode sequences of similar characters by moving recently accessed characters to the front of a list.
- `CircularSuffixArray`: Create a sorted array of all circular suffixes of a given string.

### Usage
1. Encoding:
    ```bash
    $ cat <test_file> | java -cp ".;..\algs4.jar" <class_file> - > <output_file>
    ```
    Example:
    ```bash
    # Encode using Burrows-Wheeler
    $ cat tests/abra.txt | java -cp ".;..\algs4.jar" BurrowsWheeler - > tests/bwt.txt 

    # Encode using Move-to-Front  
    $ cat tests/abra.txt | java -cp ".;..\algs4.jar" MoveToFront - > tests/mtf.txt 
    ```
2. Decoding:
    ```bash
    $ cat <encoded_file> | java -cp ".;..\algs4.jar" <class_file> + 
    ```
    Example:
    ```bash
    # Decode using Burrows-Wheeler
    $ cat tests/bwt.txt | java -cp ".;..\algs4.jar" BurrowsWheeler +
    ABRACADABRA!

    # Decode using Move-to-Front  
    $ cat tests/mtf.txt | java -cp ".;..\algs4.jar" MoveToFront +
    ABRACADABRA!
    ```
3. Applying encoding/decoding operations in succession:
    ```bash
    # Encode using Burrows-Wheeler and Move-to-Front    
    $ cat tests/abra.txt | java -cp ".;..\algs4.jar" BurrowsWheeler - | java -cp ".;..\algs4.jar" MoveToFront - > tests/output.txt 

    # Decode the compressed message
    $ cat tests/output.txt | java -cp ".;..\algs4.jar" MoveToFront + | java -cp ".;..\algs4.jar" BurrowsWheeler +
    ABRACADABRA!

    # Encode using Burrows-Wheeler and immediately decode the output
    $ cat tests/nums.txt | java -cp ".;..\algs4.jar" BurrowsWheeler - | java -cp ".;..\algs4.jar" BurrowsWheeler +
    123456789
    ```

