import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Ahmed & Rasmus on 13/11/14.
 */
public class Node {

    private String prev = null;
    private String next = null;
    private String nextnext = null;
    private int listenPort;

    public Node(int port, String... friendVars) {

        // try to establish node
        try {

            // get local IP address
            InetAddress localHost = InetAddress.getLocalHost();

            // create socket for Node
            listenPort = port;
            ServerSocket connectionSocket = new ServerSocket(listenPort, 0, localHost);

            // debug SOUT
            System.out.println("Node at " + localHost + ":" + listenPort + " is now running!");

        } catch (IOException e) {

            System.out.println("USER! You must use a free port ...");
            return;
            //e.printStackTrace();
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

        // the nodes own port
        int nodePort = Integer.parseInt(args[0]);

        if (args.length == 1) {

            new Node(nodePort);
        }

        else {

            // the optional friend input as "ip:port"
            String friendNode = args[1];
            new Node(nodePort, friendNode);
        }


    }
}
