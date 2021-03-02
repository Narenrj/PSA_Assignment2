package edu.neu.coe.info6205.union_find;

import edu.neu.coe.info6205.util.Benchmark;
import edu.neu.coe.info6205.util.Benchmark_Timer;

import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

public class WQUPC_Alternate2 {


    public static void main(String args[]){

        Benchmark<Integer> WQU1PC_RUN = new Benchmark_Timer<>("Weighted Quick Union Find Using Single Pass Halving Mechanism by storing Size of elements", run1 -> {
            WQUPC_Alternate2.generatePairs(100000); });

        System.out.printf("Weighted Quick Union Find Using Single Pass Halving Mechanism by storing Size of elements to union 100000 sites takes the following %.2f milliseconds\n",WQU1PC_RUN.run(0,200));


        Benchmark<Integer> WQU2PC_RUN = new Benchmark_Timer<>("Weighted Quick Union Find Using Double Pass Mechanism by storing Size of elements", run2-> {
            WQUPC.generatePairs(100000); });

        System.out.printf("Weighted Quick Union Find Using Double Pass Mechanism by storing Size of elements to union 100000 sites takes the following %.2f milliseconds\n",WQU2PC_RUN.run(0,200));
    }

    private final int[] parent;   // parent[i] = parent of i
    private final int[] size;   // size[i] = size of subtree rooted at i
    private int count;  // number of components

    /**
     * Initializes an empty union–find data structure with {@code n} sites
     * {@code 0} through {@code n-1}. Each site is initially in its own
     * component.
     *
     * @param n the number of sites
     * @throws IllegalArgumentException if {@code n < 0}
     */
    public WQUPC_Alternate2(int n) {
        count = n;
        parent = new int[n];
        size = new int[n];
        for (int i = 0; i < n; i++) {
            parent[i] = i;
            size[i] = 1;
        }
    }

    //to generate the number of pairs required to make a component of size 1
    public static int generatePairs(int n){
        WQUPC_Alternate2 obj = new WQUPC_Alternate2(n);

        int pairCount = 0;

        while (obj.count() != 1) {
            int pair_val1 = ThreadLocalRandom.current().nextInt(0, n);
            int pair_val2 = ThreadLocalRandom.current().nextInt(0, n);
            obj.union(pair_val1, pair_val2);
            pairCount++;
        }
        return pairCount;
    }

    public void show() {
        for (int i = 0; i < parent.length; i++) {
            System.out.printf("%d: %d, %d\n", i, parent[i], size[i]);
        }
    }

    /**
     * Returns the number of components.
     *
     * @return the number of components (between {@code 1} and {@code n})
     */
    public int count() {
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
        while (root != parent[root]) {
            root = parent[root];
        }

        return root;
    }

    // validate that p is a valid index
    private void validate(int p) {
        int n = parent.length;
        if (p < 0 || p >= n) {
            throw new IllegalArgumentException("index " + p + " is not between 0 and " + (n - 1));
        }
    }

    /**
     * Returns true if the the two sites are in the same component.
     *
     * @param p the integer representing one site
     * @param q the integer representing the other site
     * @return {@code true} if the two sites {@code p} and {@code q} are in the same component;
     * {@code false} otherwise
     * @throws IllegalArgumentException unless
     *                                  both {@code 0 <= p < n} and {@code 0 <= q < n}
     */
    public boolean connected(int p, int q) {
        return find(p) == find(q);
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
        int rootP = find(p);
        int rootQ = find(q);
        if (rootP == rootQ) return;
        // make smaller root point to larger one
        if (size[rootP] < size[rootQ]) {
            parent[rootP] = rootQ;
            size[rootQ] += size[rootP];
        } else {
            parent[rootQ] = rootP;
            size[rootP] += size[rootQ];
        }
        count--;
    }
}
