import java.io.DataInputStream;
import java.io.DataOutputStream;
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
    private Socket tempSocket;

    public Node(int port, String... friendVars) {

        // try to establish node
        try {

            // get local IP address
            InetAddress localHost = InetAddress.getLocalHost();

            // create socket for Node
            listenPort = port;
            ServerSocket connectionSocket = new ServerSocket(listenPort, 0, localHost);

            // debug SOUTs
            System.out.println("Node at " + localHost + ":" + listenPort + " is now running!");

            // trigger event - "i got friends!"
            if (friendVars.length > 0) {

                // setting "prev" to the new friend
                prev = friendVars[0];

                // my friend needs to know about our friendship!
                String[] friendData = friendVars[0].split(":");
                Socket friendSocket = new Socket(friendData[0], Integer.parseInt(friendData[1]));

                tempSocket = friendSocket;
                DataOutputStream out = new DataOutputStream(tempSocket.getOutputStream());
                out.writeUTF("HEJ MED DIG NODE?"); // test streng!

            }

        } catch (IOException e) {

            System.out.println("USER! You must use a free port ...");
            return;
            //e.printStackTrace();
        }

        Thread nodeThread = new Thread((Runnable) () -> {

            while (true) {

                System.out.println("------------------------------");
                System.out.println("Jeg er tråd: " + listenPort);
                System.out.println("Previous Node: " + prev);
                System.out.println("Next Node: " + next);
                System.out.println("Next next Node: " + nextnext);
                System.out.println("------------------------------");

                if (tempSocket != null) {
                    try {
                        DataInputStream in = new DataInputStream(tempSocket.getInputStream());

                        while (true) {

                            next = in.readUTF();

                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }


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
