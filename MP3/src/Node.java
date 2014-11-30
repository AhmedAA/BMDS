import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Ahmed & Rasmus on 13/11/14.
 */
public class Node {

    private String prev = null;
    private String next = null;
    private String nextnext = null;
    private int listenPort;
    private Socket tempSocket;
    private String[] friendData;
    private ServerSocket connectionSocket = null;
    private HashMap messages = null;

    public Node(int port, String... friendVars) {

        // try to establish node
        try {

            // get local IP address
            InetAddress localHost = InetAddress.getLocalHost();

            // hashmap
            HashMap messages = new HashMap<>();

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
                ObjectOutputStream toFriend = new ObjectOutputStream(friendConnector.getOutputStream());

                // in order to update our new friend, we need to tell about our friends!
                // order: handshake msg., myself (new next), next (new nextnext)
                String friendMsg;
                friendMsg = "FriendNext" + ";" + connectionSocket.getInetAddress().getHostAddress() + ":" + connectionSocket.getLocalPort();

                // sending friend data
                toFriend.writeObject(friendMsg);

            }

        } catch (IOException e) {

            System.out.println("USER! You must use a free port ...");
            return;
        }

        Thread nodeThread = new Thread((Runnable) () -> {

            // Message data structure
            HashMap messages = new HashMap<>();

            while (true) {

                try {

                    System.out.println("------------------------------");
                    System.out.println("Jeg er tråd: " + listenPort);
                    System.out.println("Previous Node: " + prev);
                    System.out.println("Next Node: " + next);
                    System.out.println("Next next Node: " + nextnext);
                    System.out.println("DATA(key=1) er: " + messages.get(1));
                    System.out.println("DATA(key=2) er: " + messages.get(2));
                    System.out.println("------------------------------");

                    Socket handShake = connectionSocket.accept();

                    ObjectInputStream in = new ObjectInputStream(handShake.getInputStream());

                    Object data = in.readObject();

                    if (data.getClass().equals(String.class)) {

                        String input = (String) data;
                        friendData = input.split(";");

                    }

                    else {

                        // TODO
                        messages = (HashMap) data;
                        friendData = "l33t;haxx".split(";");

                    }

                    if (friendData[0].equals("Putting")){

                        Message m = (Message) in.readObject();

                        if ((!messages.isEmpty()) || (String) messages.get(m.key) == m.message) {

                            // already updated !
                        }

                        else {

                            addMessage(messages, m.key, m.message);

                            String key = Integer.toString(m.key);

                            if (next == null) {

                                //nothing to update, moving on ...
                            }
                            else {

                                String[] nextPut = next.split(":");
                                String[] moreData = new String[4];

                                moreData[0] = nextPut[0];
                                moreData[1] = nextPut[1];
                                moreData[2] = key;
                                moreData[3] = m.message;

                                Put.main(moreData);

                                if (prev != null) {

                                    String[] prevPut = prev.split(":");
                                    String[] evenMoreData = new String[4];

                                    evenMoreData[0] = prevPut[0];
                                    evenMoreData[1] = prevPut[1];
                                    evenMoreData[2] = key;
                                    evenMoreData[3] = m.message;

                                    Put.main(evenMoreData);
                                }
                            }
                        }
                    }

                    else if (friendData[0].equals("FriendNext")) {

                        next = friendData[1];

                        String[] dataStr = friendData[1].split(":");
                        Socket dataBack = new Socket(dataStr[0], Integer.parseInt(dataStr[1]));
                        ObjectOutputStream dataOut = new ObjectOutputStream(dataBack.getOutputStream());
                        dataOut.writeObject(messages);

                        if (nextnext == null && prev != null) {

                            String[] nextNextStr = prev.split(":");
                            Socket nextNextSocket = new Socket(nextNextStr[0], Integer.parseInt(nextNextStr[1]));
                            ObjectOutputStream nextNextFriend = new ObjectOutputStream(nextNextSocket.getOutputStream());

                            String nextNextMsg;
                            nextNextMsg = "FriendNextNext" + ";" + next;

                            // sending friend data
                            nextNextFriend.writeObject(nextNextMsg);

                        }

                    }

                    else if (friendData[0].equals("FriendNextNext")) {

                        nextnext = friendData[1];

                    }

                    else if (friendData[0].equals("Getting")) {

                        String[] sendBack = friendData[1].split(":");

                        Socket dataBack = new Socket(sendBack[0], Integer.parseInt(sendBack[1]));
                        DataOutputStream dataOut = new DataOutputStream(dataBack.getOutputStream());

                        if (messages.containsKey(Integer.parseInt(sendBack[2]))) {

                            dataOut.writeUTF("SUCCES! - Key: (" + sendBack[2] + ", " + messages.get(Integer.parseInt(sendBack[2])) + ")");

                        }

                        else {

                            dataOut.writeUTF("FAIL! - No key matches in Node network");

                        }

                    }

                    // node has been updated (in some way!)
                    System.out.println("Node updated!");

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }

        });

        nodeThread.start();
    }

    private void addMessage(HashMap messages, int key, String message){
        if(!messages.containsKey(key)) {
            messages.put(key, message);
            System.out.println("Added message with key " + key + " value: " + message + "\n");
        }
        else {
            System.out.println("A message already exists with that key!");
        }
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
