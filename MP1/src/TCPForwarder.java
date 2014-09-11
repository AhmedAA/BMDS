/**
 * Created by ahmed on 11/09/14.
 */

import java.net.*;
import java.io.*;

public class TCPForwarder
{
    public static void main(String args[])
    {
        if (args.length < 3) {
            System.out.println ("Usage: host port1 port2");
            System.exit(-1);
        }

        Socket socket1 = null;
        Socket socket2 = null;

        int port1 = (int)args[1];
        int port2 = (int)args[2];

        socket1 = new Socket(args[0], port1);
        socket2 = new Socket(args[0], port2);

        DataInputStream inSocket1 = new DataInputStream(socket1.getInputStream());
        DataOutputStream outSocket1 = new DataOutputStream(socket1.getOutputStream());
        System.out.println("Data has been received and sent from socket1\n");

        DataInputStream inSocket2 = new DataInputStream(outSocket1);
        DataOutputStream outSocket2 = new DataOutputStream(outSocket2);
        System.out.println("Data has been received at socket2\n");
        System.out.println("Socket2: " + (string)outSocket2);

        socket1.close();
        socket2.close();
    }
}