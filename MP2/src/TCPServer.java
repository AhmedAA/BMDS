import java.net.*;
import java.io.*;

public class TCPServer
{
    public static void main(String args[]) throws Exception
    {
        ServerSocket listenSocket = null;
        try {
            int serverPort = 7896;
            listenSocket = new ServerSocket(serverPort);
            while (true) {
                Socket clientSocket = listenSocket.accept();
                System.out.println("Accepted connection.");
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
            while(true) {
                String data = in.readUTF();
                System.out.println(data);
                out.writeUTF("Right back at 'ya: " + data);
            }
        } catch (Exception e) {
            System.out.println("Connection died:" + e.getMessage());
        }
    }
}