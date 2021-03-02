/**
 * Original code:
 * Copyright © 2000–2017, Robert Sedgewick and Kevin Wayne.
 * <p>
 * Modifications:
 * Copyright (c) 2017. Phasmid Software
 */
package edu.neu.coe.info6205.union_find;


import edu.neu.coe.info6205.util.Benchmark;
import edu.neu.coe.info6205.util.Benchmark_Timer;

import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;


/**
 * Height-weighted Quick Union with Path Compression
 */
public class WQUPC_Alternate1 implements UF {


    public static void main(String args[]){

        Benchmark<Integer> WQU1PC_Size_RUN = new Benchmark_Timer<>("Weighted Quick Union Find by storing Size of elements", run1 -> {
            WQUPC_Alternate2.generatePairs(10000); });

        System.out.printf("Weighted Quick Union Find Using Single Pass Halving Mechanism by storing Size of elements to union 10000 sites takes the following %.2f milliseconds\n",WQU1PC_Size_RUN.run(0,300));


        Benchmark<Integer> WQU1PC_Depth_RUN = new Benchmark_Timer<>("Weighted Quick Union Find by storing Depth of elements", run2-> {
            WQUPC_Alternate1.generatePairs(10000); });

        System.out.printf("Weighted Quick Union Find Using Single Pass Halving Mechanism by storing Depth of elements to union 10000 sites takes the following %.2f milliseconds\n",WQU1PC_Depth_RUN.run(0,300));

    }

    //to generate the number of pairs required to make a component of size 1
    public static int generatePairs(int n){
        WQUPC_Alternate1 obj = new WQUPC_Alternate1(n);

        int pairCount = 0;

        while (obj.components() != 1) {
            int pair_val1 = ThreadLocalRandom.current().nextInt(0, n);
            int pair_val2 = ThreadLocalRandom.current().nextInt(0, n);
            obj.connect(pair_val1, pair_val2);
            pairCount++;
        }
        return pairCount;
    }

    /**
     * Ensure that site p is connected to site q,
     *
     * @param p the integer representing one site
     * @param q the integer representing the other site
     */
    public void connect(int p, int q) {
        if (!isConnected(p, q)) union(p, q);
    }

    /**
     * Initializes an empty union–find data structure with {@code n} sites
     * {@code 0} through {@code n-1}. Each site is initially in its own
     * component.
     *
     * @param n the number of sites
     * @throws IllegalArgumentException if {@code n < 0}
     */
    public WQUPC_Alternate1(int n) {
        count = n;
        parent = new int[n];
        depth = new int[n];
        for (int i = 0; i < n; i++) {
            parent[i] = i;
            depth[i] = 1;
        }
    }

    /**
     * Returns the number of components.
     *
     * @return the number of components (between {@code 1} and {@code n})
     */
    public int components() {
        return count;
    }

    /**
     * Returns the component identifier for the component containing site {@code p}.
     *
     * @param p the integer representing one site
     * @return the component identifier for the component containing site {@code p}
     * @throws IllegalArgumentException unless {@code 0 <= p < n}
     */
    public int find(int p) {
        validate(p);
        int root = p;
        // TO BE IMPLEMENTED
        while (root != parent[root]) {
            root = parent[root];
        }
            return root;
    }


    /**
     * Merges the component containing site {@code p} with the
     * the component containing site {@code q}.
     *
     * @param p the integer representing one site
     * @param q the integer representing the other site
     * @throws IllegalArgumentException unless
     *                                  both {@code 0 <= p < n} and {@code 0 <= q < n}
     */
    public void union(int p, int q) {
        // CONSIDER can we avoid doing find again?
        mergeComponents(find(p), find(q));
        count--;
    }

    @Override
    public int size() {
        return 0;
    }


    @Override
    public String toString() {
        return "UF_HWQUPC:" + "\n  count: " + count +
                "\n  parents: " + Arrays.toString(parent) +
                "\n  heights: " + Arrays.toString(depth);
    }

    // validate that p is a valid index
    private void validate(int p) {
        int n = parent.length;
        if (p < 0 || p >= n) {
            throw new IllegalArgumentException("index " + p + " is not between 0 and " + (n - 1));
        }
    }

    private final int[]  parent;   // parent[i] = parent of i
    private final int[] depth;   // height[i] = height of subtree rooted at i
    private int count;  // number of components

    private void mergeComponents(int i, int j) {
        // TO BE IMPLEMENTED make shorter root point to taller one
        int rootP = find(i);
        int rootQ = find(j);
        if (rootP == rootQ) return;

        // make smaller root point to larger one
        if (depth[rootP] < depth[rootQ]) {
            parent[rootP] = rootQ;
        }
        //If Depth is equal then point root of one component to another
        else if(depth[rootP] == depth[rootQ]){
            parent[rootQ] = rootP;
            depth[rootP]++;
        }
        else {
            parent[rootQ] = rootP;
        }
    }

}
