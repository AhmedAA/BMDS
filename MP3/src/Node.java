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

                // Details about our friend relationship
                String[] friendData = friendVars[0].split(":");

                // connect to friend - we have to talk!
                Socket friendConnector = new Socket(friendData[0], Integer.parseInt(friendData[1]));

                // Trying to start a dialogue (small talk) ...
                DataOutputStream toFriend = new DataOutputStream(friendConnector.getOutputStream());

                // in order to update our new friend, we need to tell about our friends!
                // order: handshake msg., myself (new next), next (new nextnext)
                String friendMsg;
                friendMsg = "FriendNext" + ";" + connectionSocket.getInetAddress().getHostAddress() + ":" + connectionSocket.getLocalPort();

                // sending friend data
                toFriend.writeUTF(friendMsg);

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
                    String[] friendData = data.split(";");

                    if (data.equals("Putting")){
                        //pull data, call addMessage
                    }

                    else if (friendData[0].equals("FriendNext")) {

                        next = friendData[1];

                        if (nextnext == null && prev != null) {

                            String[] nextNextStr = prev.split(":");
                            Socket nextNextSocket = new Socket(nextNextStr[0], Integer.parseInt(nextNextStr[1]));
                            DataOutputStream nextNextFriend = new DataOutputStream(nextNextSocket.getOutputStream());

                            String nextNextMsg;
                            nextNextMsg = "FriendNextNext" + ";" + next;

                            // sending friend data
                            nextNextFriend.writeUTF(nextNextMsg);

                        }

                    }

                    else if (friendData[0].equals("FriendNextNext")) {

                        nextnext = friendData[1];

                    }

                    else if (data.equals("Getting")) {
                        //fetch data, send data
                    }

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
