import java.io.*;
import java.net.*;

/**
 * Created by ahmed on 29/09/14.
 * Connects to a sink, and dumps messages there. Also sends data to controller when new thread has been started.
 */
public class DataSource {

    int serverPort = 10000;
    int sinkNumber = 1;

    public void Client() throws IOException {
        ServerSocket listenSocket = null;

        listenSocket = new ServerSocket(serverPort);

        while (true) {
            Socket clientSocket = listenSocket.accept();
            System.out.println("Sink " + sinkNumber + " opened");
            sinkNumber++;
        }
    }
}
    class Connection extends Thread {

        DataInputStream in;

    {

    }
}
