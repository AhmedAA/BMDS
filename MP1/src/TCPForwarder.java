import java.io.*;
import java.net.*;

/**
 * Created by Rasmus on 14-09-2014.
 */
public class TCPForwarder {

    public static void main(String[] args) throws IOException {

        int listenPort = 8080;

        ServerSocket listenSocket = null;
        Socket clientSocket = null;

        while(true)
        {
            listenSocket = new ServerSocket(listenPort);
            clientSocket = listenSocket.accept();
            new Forward(clientSocket);
        }
    }
}

class Forward extends Thread {
    String hostname = "itu.dk";
    int hostport = 80;

    DataInputStream in;
    DataOutputStream out;
    Socket clientSocket;

    DataInputStream forwardIn;
    DataOutputStream forwardOut;
    Socket aforwardSocket;

    public Forward(Socket clientSocket) throws IOException {
        this.clientSocket = clientSocket;
        in = new DataInputStream(clientSocket.getInputStream());
        out = new DataOutputStream(clientSocket.getOutputStream());

        aforwardSocket = new Socket(hostname, hostport);
        forwardIn = new DataInputStream(aforwardSocket.getInputStream());
        forwardOut = new DataOutputStream(aforwardSocket.getOutputStream());

        this.start();
    }

    public void run()
    {
        try {
            byte[] data = new byte[15000];
            boolean keepGoing = false;
            in.read(data);
            forwardOut.write(data);
            System.out.println("From client: " + new String(data));
            while (!keepGoing) {
                try {
                    out.writeByte(forwardIn.readByte());
                } catch (EOFException e) {
                    keepGoing = true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
