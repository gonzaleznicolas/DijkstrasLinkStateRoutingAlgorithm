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

}