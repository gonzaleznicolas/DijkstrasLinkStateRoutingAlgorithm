/**
 * @author      Nicolas Gonzalez
 * @version     1.0, 27 Mar 2017
 *
 */

import java.util.Comparator;

public class MyComparator implements Comparator<Pair>
{
    public int compare( Pair p1, Pair p2)
    {
        return p1.getCost() - p2.getCost(); // compare pairs based on their cost attribute
    }
}