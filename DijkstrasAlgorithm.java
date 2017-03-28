/**
 * Dijkstra's shortest path algorithm for a graph in adjacency matrix representation.
 * Does no error checking. It assumes adjacencyMatrix represents an appropriate graph (no negative edges, etc.)
 * adjacencyMatrix must be a perfect square (both dimensions are equal).
 * 999 in the adjacencyMatrix means there is no edge.
 * There are adjacencyMatrix.length - 1 nodes in the graph
 * 0 <= startingNode <= adjacencyMatrix.length - 1
 * 
 * @author      Nicolas Gonzalez
 * @version     1.0
 *
 */

public class DijkstrasAlgorithm
{
    private int[][] adjacencyMatrix;
    private int startingNode;

    public final static int WHITE = 1;
    public final static int RED = 2;
    public final static int BLUE = 3;

    private int[] color;  // WHITE (1) means not yet visited, RED (2) means visited but shortest path not yet confirmed, BLUE (3) means shortest path confirmed
    private int[] d;  // distance array
    private int[] pi;  // predecessor array

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
    }



    public DijkstrasAlgorithm(int[][] am, int sn)
    {
        this.adjacencyMatrix = am;
        this.startingNode = sn;
    }

    public void computeShortestPath()
    {
        
    }

}