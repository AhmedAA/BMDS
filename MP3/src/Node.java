import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by ahmed & rasmus on 13/11/14.
 */
public class Node {

    private String prev = null;
    private String next = null;
    private String nextnext = null;
    private int localPort;
    private int optionalPort;
    private int listenPort;

    public Node(int... nodeVars) {

        // create socket to establish connection
        try {

            // get local IP address
            InetAddress localHost = InetAddress.getLocalHost();

            // create socket for Node
            listenPort = 8000; //localPort;
            ServerSocket connectionSocket = new ServerSocket(listenPort, 0, localHost);
            listenPort++;

            // debug SOUT
            System.out.println("Node at " + localHost + ":" + listenPort + " is now running!");

        } catch (IOException e) {

            e.printStackTrace();
        }

        Thread nodeThread = new Thread((Runnable) () -> {

            while (true) {

                System.out.println("Jeg en tråd: " + listenPort);

                try {

                    Thread.sleep(3000);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        });

        nodeThread.start();
    }

    public static void main(String[] args) throws IOException {

        if (args.length == 0) {

            new Node();
        }

        else {

            int nodeIP = Integer.parseInt(args[0]);
            int nodePort = Integer.parseInt(args[1]);
            new Node(nodeIP, nodePort);
        }


    }
}
