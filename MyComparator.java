import java.util.Comparator;

public class MyComparator implements Comparator<Pair>
{
    public int compare( Pair p1, Pair p2)
    {
        return p1.getCost() - p2.getCost(); // compare pairs based on their cost attribute
    }
}