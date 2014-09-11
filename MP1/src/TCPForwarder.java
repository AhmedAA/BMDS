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

        ServerSocket socket1 = null;
        ServerSocket socket2 = null;

        int port1 = Integer.parseInt(args[1]);
        int port2 = Integer.parseInt(args[2]);

        try {
            socket1 = new ServerSocket(port1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            socket2 = new ServerSocket(port2);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Socket socketa = socket1.accept();


        DataInputStream inSocket1 = new DataInputStream(socketa.getInputStream());

        DataOutputStream outSocket1 = new DataOutputStream(socketa.getOutputStream());
        System.out.println("Data has been received and sent from socket1\n");


        String socket1Data = inSocket1.readUTF();

        Socket socketb = socket2.accept();

        DataInputStream inSocket2 = new DataInputStream(socketb.getInputStream());

        inSocket2.readUTF();

        DataOutputStream outSocket2 = new DataOutputStream(socketb.getOutputStream());

        outSocket2.writeUTF(socket1Data);
        System.out.println("Data has been received at socket2\n");

        socket1.close();
        socket2.close();
    }
}