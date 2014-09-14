/**
 * Created by ahmed on 11/09/14.
 */

import com.sun.corba.se.spi.activation.Server;
import javax.xml.crypto.Data;
import java.net.*;
import java.io.*;

public class TCPForwarder
{
    public static void main(String args[]) throws IOException {

        ServerSocket serverSocket = new ServerSocket();
        serverSocket.setReuseAddress(true);

        String hostAddress = args[0];
        int hostPort = Integer.parseInt(args[1]);
        int toPort = Integer.parseInt(args[2]);

        InetSocketAddress fromAddress = new InetSocketAddress(hostAddress, hostPort);
        InetSocketAddress toAddress   = new InetSocketAddress(toPort);
        serverSocket.bind(fromAddress);
        String hostName = fromAddress.getHostName();
        System.out.println("Ready to accept connections on: " + hostName);
        run(toAddress, fromAddress, serverSocket);
    }

    public static void run(InetSocketAddress to, InetSocketAddress from, ServerSocket serverSocket) throws IOException {
        Socket source = null;
        while (true)
        {
            source = serverSocket.accept();
            Socket socket = new Socket(to.getAddress(), to.getPort());
            copy(source, socket);
        }
    }

    public static void copy(Socket source, Socket target) throws IOException {
        InputStream in = source.getInputStream();
        OutputStream out = target.getOutputStream();
        int count;
        byte[] buf = new byte[512];

        while ((count = in.read(buf)) != -1)
        {
            out.write(buf, 0, count);
        }
    }
}

/*        if (args.length < 3) {
            System.out.println ("Usage: host port1 port2");
            System.exit(-1);
        }

        ServerSocket listenSocket = new ServerSocket(Integer.parseInt(args[1]));
        while (true)
        {
            Socket clientSocket = listenSocket.accept();

        }
        Socket dServerSocket = new Socket("localhost", Integer.parseInt(args[2]));
        dServerSocket.setKeepAlive(true);
        clientSocket.setKeepAlive(true);

        InputStream serverIn   = clientSocket.getInputStream();
        OutputStream serverOut = clientSocket.getOutputStream();
        InputStream clientIn   = dServerSocket.getInputStream();
        OutputStream clientOut = dServerSocket.getOutputStream();*/



//        ServerSocket socket1 = null;
//        ServerSocket socket2 = null;
//
//        int port1 = Integer.parseInt(args[1]);
//        int port2 = Integer.parseInt(args[2]);
//
//        socket1 = new ServerSocket(port1);
//        socket2 = new ServerSocket(port2);
//
//        Socket socketa = new Socket(args[0], port1);
//
//        DataInputStream inSocket1 = new DataInputStream(socketa.getInputStream());
//
//        DataOutputStream outSocket1 = new DataOutputStream(socketa.getOutputStream());
//        System.out.println("Data has been received and sent from socket1\n");
//
//
//        String socket1Data = inSocket1.readUTF();
//
//        Socket socketb = socket2.accept();
//
//        DataInputStream inSocket2 = new DataInputStream(socketb.getInputStream());
//
//        inSocket2.readUTF();
//
//        DataOutputStream outSocket2 = new DataOutputStream(socketb.getOutputStream());
//
//        outSocket2.writeUTF(socket1Data);
//        System.out.println("Data has been received at socket2\n");
//
//        socket1.close();
//        socket2.close();