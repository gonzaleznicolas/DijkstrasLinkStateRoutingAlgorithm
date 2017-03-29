/**
 * @author      Nicolas Gonzalez
 * @version     1.0, 27 Mar 2017
 *
 */
public class Pair {
    private int node;
    private int cost;
    
    public Pair(int n, int c){
        node = n;
        cost = c;
    }
    
    public  int getCost(){
        return this.cost;
    }
    public int getNode(){
        return this.node;
    }
    public void setCost(int cost){
        this.cost = cost;
    }
    public void setNode(int node){
        this.node = node;
    }
    public void setPair(int node, int cost){
        this.node = node;
        this.cost = cost;
    }

    public boolean equals(Pair p1, Pair p2)
    {
        return (p1.node == p2.node && p1.cost == p2.cost);
    }

}