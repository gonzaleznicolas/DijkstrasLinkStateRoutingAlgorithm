/**
 * Router Class
 * 
 * Router implements Dijkstra's algorithm for computing the minimum distance to all nodes in the network
 * @author      Nicolas Gonzalez
 * @version     1.0
 *
 */

import java.io.*;
import java.net.*;
import java.util.*;


public class Router {


    private String ipAddress;
    private int routerID;
    private int routerPort;
    private String configFile;
    private int neighborUpdateFreq;   // how frequently to send state vector to neighbors (in ms)
    private int dijkstraFreq;     // how frequently to recalculate and print best route

    private InetAddress routersIP;
    private int numberOfNodes;
    private int[] ports;        // info about the ports of my neighbors
    private int[][] graphTable; // an adjacency matrix representing the graph
    private boolean[] haveReceivedVector;  // a table which indicates for which nodes we have already received a vector

    public DatagramSocket udpSocket;
    public Timer timer;

    /*
        * Constructor to initialize the program 
        * 
        * @param peerip        IP address of other routers (we assume that all routers are running in the same machine)
        * @param routerid    Router ID
        * @param port        Router UDP port number
        * @param configfile    Configuration file name
        * @param neighborupdate    link state update interval - used to update router's link state vector to neighboring nodes
        * @param routeupdate     Route update interval - used to update route information using Dijkstra's algorithm
    */
    public Router(String peerip, int routerid, int port, String configfile, int neighborupdate, int routeupdate)
    {
        // set argument instance variables
        ipAddress = peerip;
        routerID = routerid;
        routerPort = port;
        configFile = configfile;
        neighborUpdateFreq = neighborupdate;
        dijkstraFreq = routeupdate;
    
    }
    

    /**
     *  Compute route information based on Dijkstra's algorithm and print the same
     * 
     */
    public void compute()
    {
        // initialize data structures to some preliminary values
        numberOfNodes = -1;
        ports = new int[1];
        graphTable = new int[1][1];


        // read the configuration file into the data structures numberOfNodes, ports, and graphTable
        FileInputStream fin = null;
        try
        {
            fin = new FileInputStream(configFile);
            Scanner fileReader = new Scanner(fin);

            // read first number: the number of nodes
            numberOfNodes = fileReader.nextInt();


            // this allows us to initiailze the ports array and the graphTable array
            ports = new int[numberOfNodes];
            graphTable = new int[numberOfNodes][numberOfNodes];
            haveReceivedVector = new boolean[numberOfNodes];

            for (int i = 0; i < ports.length; i++) {ports[i] = -1;}    // initialize the ports array to -1

            for (int i = 0; i < graphTable.length; i++)    // initialize the graphTable to all -1's
            {
                for (int j = 0; j < graphTable[0].length; j++)
                {
                    graphTable[i][j] = 999;
                }
            }

            for (int i = 0; i < haveReceivedVector.length; i++) {haveReceivedVector[i] = false;}  // initially we have not received any vectors

            // read the rest of the file into the appropriate data structures
            while (fileReader.hasNext())
            {
                fileReader.next(); // ignore the node label. i will work with the node ID
                int neighborID = fileReader.nextInt();  // the id of the neighbor
                int c = fileReader.nextInt();  // the cost of the edge
                // the cost from router #routerID to router #neighborID is c. update the graphTable
                graphTable[routerID][neighborID] = c;
                graphTable[neighborID][routerID] = c;

                ports[neighborID] = fileReader.nextInt();

            }

            // fill in the ports and graphTable with own information
            ports[routerID] = routerPort;
            graphTable[routerID][routerID] = 0; // the distance to myself is 0 
            haveReceivedVector[routerID] = true;

            /*
            System.out.println("number of nodes:");
            System.out.println(numberOfNodes);

            System.out.println("graph table:");
            System.out.println(Arrays.deepToString(graphTable));

            System.out.println("ports:");
            System.out.println(Arrays.toString(ports));
            */


        }
        catch (Exception e){
            System.out.println("Exception reading file: " + e.getMessage());
            System.exit(0); // exit
        }finally{
            try{if (fin != null){fin.close();}}
            catch (IOException ex){
                System.out.println("error closing file.");
                System.exit(0); // exit
            }
        }

        try
        {
            udpSocket = new DatagramSocket(routerPort); // initialize socket

            routersIP = InetAddress.getByName(ipAddress);


            timer = new Timer(true); // initialize timer. make thread daemon

            // set timer task to send link state vector to neighbouring nodes every neighborUpdateFreq ms
            StateVectorSender svs = new StateVectorSender(this);
            timer.scheduleAtFixedRate(svs, 0, neighborUpdateFreq);

            // TODOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO
            // set timer task to update node's route information every dijkstraFreq ms

            // allocate space for receiving a LinkState message
            byte[] messageSpace = new byte[LinkState.MAX_SIZE];
            DatagramPacket messagePacket = new DatagramPacket(messageSpace, messageSpace.length);

            while(true)
            {
                // recieve link state message from neighbor
                udpSocket.receive(messagePacket);
                LinkState receivedMessage = new LinkState(messagePacket);

                int sourceID = receivedMessage.sourceId;
                int receiverID = receivedMessage.destId;

                int[] receivedVector = receivedMessage.getCost();

                // update data structures
                graphTable[sourceID] = receivedVector;
                haveReceivedVector[sourceID] = true;

                // decrement the hopsLeft of receivedMessage
                // if hops left is now < 0, do nothing
                // else (hops left >=0, forward it to all of your neighbors)

                //System.out.println(Arrays.deepToString(graphTable));

            }



        }
        catch (Exception e)
        {
            System.out.println("An unexpected error occured. The program will terminate.");
            System.exit(0);
        }



        //You may use the follwing piece of code to print routing table info
        /*
        System.out.println("Routing Info");
        System.out.println("RouterID \t Distance \t Prev RouterID");
        for(int i = 0; i < numNodes; i++)
          {
              System.out.println(i + "\t\t   " + distancevector[i] +  "\t\t\t" +  prev[i]);
          }
        */

    }



    public synchronized void processUpDateDS(DatagramPacket receivepacket)
    {
        // Update data structure(s).
        // Forward link state message received to neighboring nodes
        // based on broadcast algorithm used.
    }


    public synchronized void processUpdateNeighbor()
    {
        // Send node’s link state vector to neighboring nodes as link
        // state message over UDP
        
        for (int i = 0; i < ports.length; i++)  // for each node i in the network
        {
            if ((ports[i] != -1) && (ports[i] != routerPort)) // if that node is a neighbor and not this router
            {
                // if here, ports[i] holds the port of node i (which is a neighbor with ID i)

                // make the link state message
                LinkState message = new LinkState(routerID, i, numberOfNodes,graphTable[routerID]); // source ID, destination ID, max hops, state vector
                // since the graph is connected, max number of hops = numberOfNodes ensures the message will reach every node
                
                // send it over UDP
                DatagramPacket packet = new DatagramPacket(message.getBytes(), message.getBytes().length, routersIP, ports[i]);
                try{ udpSocket.send(packet);}
                catch (IOException e){ System.out.println("There was an excepion sending the packet."); System.exit(0);}
            }
        }


    }


    public synchronized void processUpdateRoute()
    {
        // If link state vectors of all nodes received,
        // Yes => Compute route info based on Dijkstra’s algorithm
        // and print as per the output format.
        // No => ignore the event.
        // Schedule task if Method-1 followed to implement recurring
        // timer task.
    }



    /* A simple test driver */
    public static void main(String[] args)
    {
        
        String peerip = "127.0.0.1"; // all router programs running in the same machine for simplicity. 127.0.0.1 is the same as localhost
        String configfile = "";
        int routerid = 999;
                int neighborupdate = 1000; // milli-seconds, update neighbor with link state vector every second
        int forwardtable = 10000; // milli-seconds, print route information every 10 seconds
        int port = -1; // router port number
    
        // check for command line arguments
        if (args.length == 3) {
            // either provide 3 parameters
            routerid = Integer.parseInt(args[0]);
            port = Integer.parseInt(args[1]);    
            configfile = args[2];
        }
        else {
            System.out.println("wrong number of arguments, try again.");
            System.out.println("usage: java Router routerid routerport configfile");
            System.exit(0);
        }

        
        Router router = new Router(peerip, routerid, port, configfile, neighborupdate, forwardtable);
        
        System.out.println("Router initialized..running");
        router.compute();
    }

}
