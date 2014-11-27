import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

/**
 * Created by ahmed on 26/11/14.
 */
public class NodeTest {

    HashMap messages = new HashMap();

    public static void main (String[] args){

        Thread nodeThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ServerSocket testNode = new ServerSocket(8000);
                    Socket acceptSocket = testNode.accept();
                    ObjectInputStream msgIn = new ObjectInputStream(acceptSocket.getInputStream());
                    Message m = null;
                    while (true) {
                        if (msgIn.readUTF().equals("Putting")){
                            m = (Message) msgIn.readObject();
                            msgIn.close();
                            acceptSocket.close();

                            System.out.println(m.key + " " + m.message);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
            });

        nodeThread.start();
    }
}