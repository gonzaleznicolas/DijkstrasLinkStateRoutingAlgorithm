/**
 * @author      Nicolas Gonzalez
 * @version     1.0, 27 Mar 2017
 *
 */


import java.util.TimerTask;
import java.net.*;
import java.util.*;
import java.io.*;


public class StateVectorSender extends TimerTask
{
    private Router router;

    public StateVectorSender(Router r)
    {
        router = r;
    }

    public void run()
    {
        router.processUpdateNeighbor();
    }
}