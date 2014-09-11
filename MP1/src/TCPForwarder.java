/**
 * Created by ahmed on 11/09/14.
 */

import java.net.*;
import java.io.*;

public class TCPForwarder
{
    public static void main(String args[]) throws IOException {
        if (args.length < 3) {
            System.out.println ("Usage: host port1 port2");
            System.exit(-1);
        }

        Socket socket1 = null;
        Socket socket2 = null;

        int port1 = Integer.parseInt(args[1]);
        int port2 = Integer.parseInt(args[2]);

        try {
            socket1 = new Socket(args[0], port1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            socket2 = new Socket(args[0], port2);
        } catch (IOException e) {
            e.printStackTrace();
        }

        DataInputStream inSocket1 = new DataInputStream(socket1.getInputStream());
        DataOutputStream outSocket1 = new DataOutputStream(socket1.getOutputStream());
        System.out.println("Data has been received and sent from socket1\n");

        DataOutputStream outSocket2 = new DataOutputStream(outSocket1);
        System.out.println("Data has been received at socket2\n");
        System.out.println("Socket2: " + outSocket2);

        socket1.close();
        socket2.close();
    }
}