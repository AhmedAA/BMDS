package RFC862;

import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Created by ahmed on 23/11/14.
 */
public class RfcTCPTest {
    public static void main(String[] arg){
        int hostPort = 7007;
        String host = "localhost";

        String message = "What is recursion?";
        System.out.println("You sent: " + message);

        Socket serverSocket = null;

        try {
            serverSocket = new Socket(host, hostPort);
            DataInputStream in = new DataInputStream(serverSocket.getInputStream());
            DataOutput out = new DataOutputStream(serverSocket.getOutputStream());
            out.writeUTF(message);
            String reply = in.readUTF();
            System.out.println("Reply from server: " + reply);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
