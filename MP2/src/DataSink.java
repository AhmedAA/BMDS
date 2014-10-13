import com.sun.org.apache.xpath.internal.SourceTree;
import org.omg.CORBA.portable.ResponseHandler;

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
 * Should be a threaded socket server, that handles client (source) connections, handle the data sent, and sends this
 * to the controller.
 */
public class DataSink {

    public static void main(String args[]) throws Exception
    {
        Thread sinkThread = new Thread(new Runnable() {
            @Override
            public void run() {

                Socket s = null;
                int serverPort = 7896;

                DataOutputStream out = null;
                DataInputStream in = null;

                try {
                    s = new Socket("localhost", serverPort);
                    out = new DataOutputStream(s.getOutputStream());
                    in = new DataInputStream(s.getInputStream());
                    out.writeUTF("sink");

                    boolean eof = false;
                    String data = "";
                    while(!eof) {
                        try {
                            //System.out.println("herp");
                            // Challenge: React when data is received, dammit!
                            data = in.readUTF();
                            System.out.println(data);
                        } catch (IOException e) {
                            e.printStackTrace();
                            eof = true;
                        }
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        sinkThread.start();
    }
}

