import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Observable;
import java.util.Observer;
import java.util.Scanner;

/**
 * Created by ahmed on 29/09/14.
 * Should simply print out responses from started Sink/Source threads
 */
public class Controller {

    public static void main(String args[]) throws Exception
    {
        ServerSocket listenSourceSocket = null;

        try {

            int sourcePort = 7896;

            listenSourceSocket = new ServerSocket(sourcePort);

            // source
            while (true) {

                Socket sourceSocket = listenSourceSocket.accept();
                System.out.println("Controller: Accepted SOURCE socket: " + sourceSocket.getLocalAddress());

                new SourceConnection(sourceSocket);
            }


        } finally {
            if (listenSourceSocket != null)
                listenSourceSocket.close();
        }

    }
}

class SourceConnection extends Thread {
    DataInputStream in;
    DataOutputStream out;
    Socket clientSocket;

    public SourceConnection(Socket aClientSocket) throws Exception {

        clientSocket = aClientSocket;
        in = new DataInputStream(clientSocket.getInputStream());
        out = new DataOutputStream(clientSocket.getOutputStream());

        this.start();

    }

    public void run() {

        try {

            while(true) {

                String data = in.readUTF();
                System.out.println("Data: " + data);

                }
            catch (Exception e) {
                System.out.println("Connection died:" + e.getMessage());
            }

        final Thread sourceThread = new Thread(new Runnable() {



            }
        });

        outputThread.start();

    }

}

class Connection extends Observable {
    DataInputStream in;
    DataOutputStream out;
    Socket clientSocket;

    public Connection(Socket aClientSocket) throws Exception {

        clientSocket = aClientSocket;
        in = new DataInputStream(clientSocket.getInputStream());
        out = new DataOutputStream(clientSocket.getOutputStream());
        final DataSink sink = new DataSink();

        final Thread outputThread = new Thread(new Runnable() {
            @Override
            public void run() {

                addObserver(sink);
                String input = null;

                while (true) {
                    try {
                        input = in.readUTF();
                        if (input != null) {
                            setChanged();
                            notifyObservers(input);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        outputThread.start();

    }

}