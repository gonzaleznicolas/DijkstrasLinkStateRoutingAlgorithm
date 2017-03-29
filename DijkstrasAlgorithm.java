/**
 * Dijkstra's shortest path algorithm for a graph in adjacency matrix representation.
 * Does no error checking. It assumes adjacencyMatrix represents an appropriate graph (all edge weights > 0, etc.)
 * adjacencyMatrix must be a perfect square (both dimensions are equal).
 * 999 in the adjacencyMatrix means there is no edge.
 * There are adjacencyMatrix.length - 1 nodes in the graph
 * 0 <= startingNode <= adjacencyMatrix.length - 1
 * 
 * @author      Nicolas Gonzalez
 * @version     1.0
 *
 */

import java.util.*;

public class DijkstrasAlgorithm
{
    private int[][] adjacencyMatrix;
    private int startingNode;

    public final static int INFINITY = 999;
    public final static int NIL = -1;

    public final static int WHITE = 1;
    public final static int RED = 2;
    public final static int BLUE = 3;

    private int[] color;  // WHITE (1) means not yet visited, RED (2) means visited but shortest path not yet confirmed, BLUE (3) means shortest path confirmed
    private int[] d;  // distance array
    private int[] pi;  // predecessor array

    private PriorityQueue<Pair> q;

    public static void main(String[] args)
    {

        int[] a0 = {0, 1, 999, 999, 999};
        int[] a1 = {1, 0, 2, 2, 1};
        int[] a2 = {999, 2, 0, 999, 4};
        int[] a3 = {999, 2, 999, 0, 5};
        int[] a4 = {999, 1, 4, 5, 0};

        int[][] test = new int[5][5];
        test[0] = a0;
        test[1] = a1;
        test[2] = a2;
        test[3] = a3;
        test[4] = a4;


        int start = 2;

        DijkstrasAlgorithm da = new DijkstrasAlgorithm(test, start);
        da.computeShortestPath();

        /*
        System.out.println("\nStarting node: " + da.startingNode);
        System.out.println("\nDistance array:");
        System.out.println(Arrays.toString(da.d));
        System.out.println("\nPredecessor array:");
        System.out.println(Arrays.toString(da.pi));
        */
    }



    public DijkstrasAlgorithm(int[][] am, int sn)
    {
        this.adjacencyMatrix = am;
        this.startingNode = sn;

        this.d = new int[adjacencyMatrix.length];
        this.pi = new int[adjacencyMatrix.length];
        this.color = new int[adjacencyMatrix.length];

        this.q = new PriorityQueue<Pair>(adjacencyMatrix.length, new MyComparator());  // initialize a priority queue with max capacity every node

    }

    public int[] getDistanceArray() {return d;}

    public int[] getPredecessorArray() {return pi;}

    public void computeShortestPath()
    {
        
        for (int v = 0; v < adjacencyMatrix.length; v++)
        {
            color[v] = WHITE;
            d[v] = INFINITY;
            pi[v] = NIL;
        }

        //q = new PriorityQueue<Pair>(adjacencyMatrix.length, new MyComparator());  // initialize a priority queue with max capacity every node

        color[startingNode] = RED;
        d[startingNode] = 0;
        q.add(new Pair(startingNode, 0)); // add staring node to q with priority 0

        while (q.size() != 0) // while queue not empty
        {
            Pair vc = q.poll();  // extract min
            int v = vc.getNode();
            int c = vc.getCost();

            for (int u = 0; u < adjacencyMatrix.length; u++)
            {
                if ((adjacencyMatrix[v][u] != INFINITY) && (adjacencyMatrix[v][u] != 0)) // if u is not a non neighbor and not v, then it is a neighbor of v
                {
                    // if here, u is a neighbor of v
                    if (color[u] == WHITE)
                    {
                        d[u] = c + adjacencyMatrix[v][u];
                        color[u] = RED;
                        pi[u] = v;
                        q.add(new Pair(u, d[u]));
                    }
                    else if (color[u] == RED)
                    {
                        if (c + adjacencyMatrix[v][u] < d[u])
                        {
                            int old = d[u];
                            d[u] = c + adjacencyMatrix[v][u];
                            pi[u] = v;
                            // replace Pair(u,old) with Pair(u,d[u]) in q
                            q.remove(new Pair(u, old));
                            q.add(new Pair(u, d[u]));
                        }
                    }
                }
            }
            color[v] = BLUE;
        }
        
    }

}