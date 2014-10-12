package rfc862;

import java.net.*;
import java.io.*;

public class RFC862_TCP {
	public static void main(String args[]) throws Exception
    {
        ServerSocket listenSocket = null;
        try {
            int serverPort = 7007;
            listenSocket = new ServerSocket(serverPort);
            while (true) {
                Socket clientSocket = listenSocket.accept();
                System.out.println("Accepted connection...");
                new Connection(clientSocket);
            }
        } finally {
            if (listenSocket != null)
                listenSocket.close();
        }
    }
}

class Connection extends Thread
{
    DataInputStream in;
    DataOutputStream out;
    Socket clientSocket;
 
    public Connection(Socket aClientSocket) throws Exception
    {
        clientSocket = aClientSocket;
        in = new DataInputStream(clientSocket.getInputStream());
        out = new DataOutputStream(clientSocket.getOutputStream());
        this.start();
    }
 
    public void run()
    {
        try {
            String data = in.readUTF();
            System.out.println("Received message: " + data);
            out.writeUTF(data); //write the data to output stream
            Thread.sleep(2000);
            System.out.println("Message sent back.");
        } catch (Exception e) {
            System.out.println("Connection died:" + e.getMessage());
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                System.out.println("Close failed: " + e.getMessage());
            }
        }
    }
}