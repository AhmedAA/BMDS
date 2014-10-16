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

    List<Socket> sinks = new ArrayList<Socket>();

    public static void main(String[] args) throws Exception {
        new Controller().start();
    }


    public void start() throws Exception {
        ServerSocket listenSocket = null;
        int sourceNumber = 0;
        int sinkNumber = 0;

        try {

            int listenPort = 7896;
            int sinkPort = 9001;

            listenSocket = new ServerSocket(listenPort);

            // source
            while (true) {
                Socket sourceSocket = listenSocket.accept();
                DataInputStream initialMsg = new DataInputStream(sourceSocket.getInputStream());
                String initmsgString = initialMsg.readUTF();
                System.out.println(initmsgString);

                if (initmsgString.equals("source")) {
                    sourceNumber += 1;
                    System.out.println("Source A" + sourceNumber + " connected");
                    new SourceConnection(sourceSocket, sourceNumber);
                }

                if (initmsgString.equals("sink")) {
                    System.out.println("Controller: Sink accepted");
                    sinkNumber += 1;
                    //Socket sinkSocket = new Socket("localhost", sinkPort);
                    sinkPort += 1;
                    setSinks(sourceSocket, sinkNumber);
                }
            }
        } finally {
            if (listenSocket != null)
                listenSocket.close();
        }

    }

    public void setSinks(Socket sinkSocket, int sinkNumber) {
        sinks.add(sinkSocket);
    }

    private class SourceConnection extends Thread {
        DataInputStream in;
        DataOutputStream out;
        Socket clientSocket;
        int sourceNumber;

        public SourceConnection(Socket aClientSocket, int sn) throws Exception {
            clientSocket = aClientSocket;
            sourceNumber = sn;
            in = new DataInputStream(clientSocket.getInputStream());
            out = new DataOutputStream(clientSocket.getOutputStream());
            this.start();
        }

        public void run() {
            try {
                while (true) {
                    String data = in.readUTF();
                    System.out.println(data);

                }
            } catch (Exception e) {
                System.out.println("Connection died:" + e.getMessage());
            }
        }
    }

    public void publish(String message) throws IOException {
        for(Socket sinkSocket : sinks)
        {
            DataOutputStream sinkOut = new DataOutputStream(sinkSocket.getOutputStream());
            sinkOut.writeUTF(message);
        }
    }
}

//class SinkConnection extends Thread {
//    Socket clientSocket;
//    DataOutputStream out;
//    DataInputStream in;
//    int sinkNumber;
//
//    public SinkConnection(Socket aClientSocket, int sn) throws Exception {
//        clientSocket = aClientSocket;
//        out = new DataOutputStream(clientSocket.getOutputStream());
//        in = new DataInputStream(clientSocket.getInputStream());
//        sinkNumber = sn;
//        this.start();
//    }
//
//    public void run()
//    {
//
//    }
//}
//
//class Handler {
//    //List<Socket> sinks = new ArrayList<Socket>();
//    Socket sinkSocket;
//    int sinkNumber;
//
//    public Handler(Socket socket, int sinkNumber) throws IOException {
//        sinkSocket = socket;
//        this.sinkNumber = sinkNumber;
//        DataOutputStream sinkOut = new DataOutputStream(sinkSocket.getOutputStream());
//        //sinkOut.writeUTF("HEEEEEEEEEEEJ");
//        //System.out.println("Burde have skrevet");
//
//        addSinks(sinkSocket, sinkNumber);
//    }
//
//    public List<Socket> getSinks()
//    {
//        return sinks;
//    }
//
//    public void addSinks(Socket sinkSocket, int sinkNumber) throws IOException {
//        System.out.println("Sink added");
//        sinks.add(sinkSocket);
//    }
//}