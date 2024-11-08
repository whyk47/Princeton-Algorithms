# WordNet

[Full Project Specification](https://coursera.cs.princeton.edu/algs4/assignments/wordnet/specification.php)

### Overview

WordNet is a lexical database that groups English words into sets of synonyms called synsets and describes semantic relationships between these synsets. This project builds a WordNet representation using directed acyclic graphs (DAGs) to model synsets and their hypernym (is-a) relationships. It includes functionality to find semantic relationships between words, compute shortest ancestral paths, and detect semantic outliers.

### Classes
- `WordNet`: models the WordNet digraph and provides methods to interact with it.
- `SAP`: computes the shortest ancestral path between vertices in a digraph.
- `Outcast`: identifies the noun that is least related to others in a given list.

### Usage
1. Running `SAP`.
    ```bash
    $ java -cp ".;..\algs4.jar" SAP <test_file>
    ```
2. Running `Outcast`.
    ```bash
    $ java -cp ".;..\algs4.jar" Outcast synsets.txt hypernyms.txt <test_file> [<test_file_2> ... <test_file_n>]
    ```

### Input File Formats
1. `synsets.txt`
    - This file contains all synsets, each represented by a unique ID and a set of synonymous nouns.
    - Format:
        ```
        synset_id,synset,gloss
        ```
        - `synset_id`: An integer ID of the synset.
        - `synset`: Space-separated list of nouns in the synset. Multi-word nouns are joined with underscores.
        - `gloss`: A dictionary definition (not used in this project).
2. `hypernyms.txt`
    - This file defines the hypernym relationships, where each line lists a synset ID followed by IDs of its hypernyms.
    - Format:
        ```
        synset_id,hypernym_id1,hypernym_id2,...
        ```
        - `synset_id`: An integer ID of the synset.
        - `hypernym_id`: Integer IDs of hypernyms.
3. Digraph text files
    - Define the vertices and directed edges of the graph. Used to test `SAP`.
    - First Line: An integer V, representing the total number of vertices in the digraph.
    - Second Line: An integer E, representing the total number of directed edges in the digraph.
    - Subsequent Lines: Each subsequent line consists of two integers v and w, representing a directed edge from vertex v to vertex w.
4. Outcast text files
    - Each line contains a noun from the WordNet dataset.