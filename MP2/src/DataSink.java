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

                try {
                    s = new Socket("localhost", serverPort);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                DataOutputStream out = null;
                DataInputStream in = null;

                try {
                    out = new DataOutputStream(s.getOutputStream());
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    out.writeUTF("sink");
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    in = new DataInputStream(s.getInputStream());
                } catch (IOException e) {
                    e.printStackTrace();
                }

                String output = "";

                while (true) {
                    try {
                        output = in.readUTF();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    try {
                        out.writeUTF(output);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        sinkThread.start();
    }
}

