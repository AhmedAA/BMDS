import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by rasmus on 13/11/14.
 */
public class Get {

    public Get(int key, String ip, int port) throws IOException {

        Socket nodeSocket = new Socket(ip, port);
        ObjectOutputStream out = new ObjectOutputStream(nodeSocket.getOutputStream());

        // my own conn. details
        String myIP = InetAddress.getLocalHost().getHostAddress();
        int myPort = 9001;

        ServerSocket serverSocket = new ServerSocket(myPort, 0 , InetAddress.getLocalHost());

        out.writeObject("Getting" + ";" + myIP + ":" + myPort + ":" + key);

        // response handling ...
        Socket acceptSocket = serverSocket.accept();
        DataInputStream in = new DataInputStream(acceptSocket.getInputStream());

        while (true) {

            System.out.println("Response incoming ...");
            // await response from Node
            String response = in.readUTF();
            System.out.println(response);
            break;

        }

    }

    public static void main(String[] args) throws IOException {

        int key = Integer.parseInt(args[0]);
        int port = Integer.parseInt(args[2]);

        // invoke new get-client
        new Get(key, args[1], port);

    }
}
