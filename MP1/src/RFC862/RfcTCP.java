package RFC862;

import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by ahmed on 23/11/14.
 */
public class RfcTCP {
    public static void main(String[] args) throws IOException {

        int listenPort = 7007;

        ServerSocket listenSocket = null;
        Socket clientSocket = null;

        listenSocket = new ServerSocket(listenPort);

        while(true)
        {
            clientSocket = listenSocket.accept();
            new Echo(clientSocket);
        }
    }
}

class Echo extends Thread {

    DataInputStream in;
    DataOutputStream out;
    Socket clientSocket;

    public Echo(Socket socket) throws IOException {
        clientSocket = socket;
        in = new DataInputStream(clientSocket.getInputStream());
        out = new DataOutputStream(clientSocket.getOutputStream());
        this.start();
    }

    public void run(){
        try {
            String data = in.readUTF();
            System.out.println("Data received! Transmit back Spock!");
            out.writeUTF(data);
            Thread.sleep(2000);
            System.out.println("Message should be sent back now Kirk!");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
