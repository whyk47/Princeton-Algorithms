# Queues

### Overview
This project involves implementing two generic data structures, `Deque` and `RandomizedQueue`, using arrays and linked lists. These structures are designed to introduce concepts in generics, iterators, and memory-efficient operations.

A client program, `Permutation`, uses `RandomizedQueue` to read a list of strings from input and prints a random subset of k strings, ensuring each is printed only once.


### Usage
1. Run `Permutation` with the following command.
    ```bash
    $ Get-Content [path/to/testfile.txt] | java -cp ".;..\algs4.jar" Permutation [length_of_permutation]
    ```
    Example:
    ```bash
    $ Get-Content tests/duplicates.txt | java -cp ".;..\algs4.jar" Permutation 8
    BB
    BB
    BB
    CC
    BB
    BB
    AA
    CC
    ```
2. Run unit tests for `Deque` with the following command.
    ```bash
    $ java Deque
    ```

### Performance
- `Deque`: Linear memory complexity. Each operation runs in constant time.
- `RandomizedQueue`: Linear memory complexity. Each operation, excluding the iterator, runs in constant amortized time.
- `Permutation`: Constant extra memory plus one `RandomizedQueue`. Runs in linear time relative to input size.