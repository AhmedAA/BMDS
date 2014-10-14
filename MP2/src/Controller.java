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

                if (initmsgString.equals("sink")){
                    System.out.println("Controller: Sink accepted");
                    sinkNumber += 1;
                    //Socket sinkSocket = new Socket("localhost", sinkPort);
                    sinkPort += 1;
                    new Handler(sourceSocket, sinkNumber);
                }
            }
        } finally {
            if (listenSocket != null)
                listenSocket.close();
        }

    }
}

class SourceConnection extends Thread {
    DataInputStream in;
    DataOutputStream out;
    Socket clientSocket;
    int sourceNumber;

    public SourceConnection(Socket aClientSocket, int sn) throws Exception
    {
        clientSocket = aClientSocket;
        sourceNumber = sn;
        in = new DataInputStream(clientSocket.getInputStream());
        out = new DataOutputStream(clientSocket.getOutputStream());
        this.start();
    }

    public void run()
    {
        try {
            while(true) {
                List<Socket> sinks = Handler.getSinks();
                String data = in.readUTF();
                System.out.println(data);
                if(!sinks.isEmpty())
                {
                    for(Socket sinkSocket : sinks){
                        //out.writeUTF("A" + sourceNumber + " "  + data);
                        DataInputStream sinkIn = new DataInputStream(sinkSocket.getInputStream());
                        sinkIn.readUTF(in);
                    }
                } else {
                    continue;
                }
            }
        } catch (Exception e) {
            System.out.println("Connection died:" + e.getMessage());
        }
    }
}

class SinkConnection extends Thread {
    Socket clientSocket;
    DataOutputStream out;
    DataInputStream in;
    int sinkNumber;

    public SinkConnection(Socket aClientSocket, int sn) throws Exception {
        clientSocket = aClientSocket;
        out = new DataOutputStream(clientSocket.getOutputStream());
        in = new DataInputStream(clientSocket.getInputStream());
        sinkNumber = sn;
        this.start();
    }

    public void run()
    {

    }
}

class Handler extends Thread {
    private static ArrayList<Socket> sinks = new ArrayList<Socket>();
    Socket sinkSocket;
    int sinkNumber;
    DataOutputStream sinkOut;
    DataInputStream sinkIn;

    public Handler(Socket socket, int sinkNumber) throws IOException {
        sinkSocket = socket;
        this.sinkNumber = sinkNumber;
        sinkOut = new DataOutputStream(sinkSocket.getOutputStream());
        sinkIn = new DataInputStream(sinkSocket.getInputStream());
        //sinkOut.writeUTF("HEEEEEEEEEEEJ");
        //System.out.println("Burde have skrevet");
        this.run();
    }

    public static ArrayList<Socket> getSinks()
    {
        return sinks;
    }

    public void addSinks(Socket sinkSocket, int sinkNumber) throws IOException {
        System.out.println("Sink added");
        sinks.add(sinkSocket);
    }

    public void run()
    {
        try {
            addSinks(sinkSocket, sinkNumber);
            while(true)
            {
                String data = sinkIn.readUTF();
                sinkOut.writeUTF(data);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}