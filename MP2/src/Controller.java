import javax.xml.transform.Source;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

/**
 * Created by ahmed on 29/09/14.
 * Should simply print out responses from started Sink/Source threads
 */
public class Controller {

    public static void main(String args[]) throws Exception
    {
        ServerSocket listenSocket = null;

        try {

            int listenPort = 7896;

            listenSocket = new ServerSocket(listenPort);

            // source
            while (true) {

                Socket sourceSocket = listenSocket.accept();
                DataInputStream initialMsg = new DataInputStream(sourceSocket.getInputStream());
                String initmsgString = initialMsg.readUTF();
                System.out.println(initmsgString);

                if (initmsgString.equals("source")) {
                    System.out.println("Controller: Accepted SOURCE socket: " + sourceSocket.getLocalAddress());
                    new SourceConnection(sourceSocket);
                }

                if (initmsgString.equals("sink")){
                    System.out.println("Controller: Sink accepted");
                    new SinkConnection(sourceSocket);
                }

            }


        } finally {
            if (listenSocket != null)
                listenSocket.close();
        }

    }
}

class SourceConnection extends Thread {
    String messages;
    DataInputStream in;
    DataOutputStream out;
    Socket clientSocket;

    public SourceConnection(Socket aClientSocket) throws Exception
    {
        messages = "";
        clientSocket = aClientSocket;
        in = new DataInputStream(clientSocket.getInputStream());
        out = new DataOutputStream(clientSocket.getOutputStream());
        this.start();
    }

    public String GetMessage() {
        return messages;
    }

    public void SetMessage(String newMessage) {
        messages = newMessage;
    }

    public void run()
    {
        try {
            while(true) {
                String data = in.readUTF();
                SetMessage(data);
                System.out.println(data);
                out.writeUTF("Source: " + data);
            }
        } catch (Exception e) {
            System.out.println("Connection died:" + e.getMessage());
        }
    }
}

class SinkConnection extends Observable implements Runnable{
    DataSink sink = new DataSink();
    Socket clientSocket;

    public SinkConnection(Socket aClientSocket) throws Exception {
        clientSocket = aClientSocket;
        this.addObserver(sink);
        this.run();
    }

    @Override
    public void run() {
        boolean changed = false;;
        while (!changed)
        {

        }
    }
}