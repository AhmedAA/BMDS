import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

/**
 * Created by Ahmed & Rasmus on 13/11/14.
 */
public class Node {

    private String prev = null;
    private String next = null;
    private String nextnext = null;
    private int listenPort;
    private Socket tempSocket;
    private HashMap messages = new HashMap<>();
    private String[] friendData;
    private ServerSocket connectionSocket = null;

    public Node(int port, String... friendVars) {

        // try to establish node
        try {

            // get local IP address
            InetAddress localHost = InetAddress.getLocalHost();

            // create socket for Node
            listenPort = port;
            connectionSocket = new ServerSocket(listenPort, 0, localHost);

            // debug SOUTs
            System.out.println("Node at " + localHost + ":" + listenPort + " is now running!");

            // trigger event - "i got friends!"
            if (friendVars.length > 0) {

                // setting "prev" to the friend
                prev = friendVars[0];

                // my friend needs to know about our friendship!
                String[] friendData = friendVars[0].split(":");

                Socket friendConnector = new Socket(friendData[0], Integer.parseInt(friendData[1]));
                DataOutputStream toFriend = new DataOutputStream(friendConnector.getOutputStream());
                toFriend.writeUTF(connectionSocket.getInetAddress().getHostAddress() + ":" + connectionSocket.getLocalPort());

            }

        } catch (IOException e) {

            System.out.println("USER! You must use a free port ...");
            return;
        }

        Thread nodeThread = new Thread((Runnable) () -> {

            while (true) {

                try {

                    System.out.println("------------------------------");
                    System.out.println("Jeg er tråd: " + listenPort);
                    System.out.println("Previous Node: " + prev);
                    System.out.println("Next Node: " + next);
                    System.out.println("Next next Node: " + nextnext);
                    System.out.println("------------------------------");

                    Socket handShake = connectionSocket.accept();
                    DataInputStream in = new DataInputStream(handShake.getInputStream());
                    String data = in.readUTF();
                    next = data;

                    // node has been updated (in some way!)
                    System.out.println("Node updated!");

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        });

        nodeThread.start();
    }

    private void addMessage(int key, String message){
        if(!messages.containsKey(key)) {
            messages.put(key, message);
        }
        else {
            System.out.println("A message already exists with that key!");
        }
    }

    private void getMessages() {

    }

    public void updateNode(String ip, int port) throws IOException {

        Socket nodeSocket = new Socket(ip, port);

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
