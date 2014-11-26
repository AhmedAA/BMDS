import java.io.*;
import java.net.Socket;

/**
 * Created by ahmed on 13/11/14.
 */
public class Put implements Serializable{
    public static void main(String[] args) throws IOException {
        Message m = new Message();
        Socket nodeSocket = null;

        String nodeIP = args[0];
        int nodePort = Integer.parseInt(args[1]);

        nodeSocket = new Socket(nodeIP, nodePort);

        m.key = Integer.parseInt(args[2]);
        m.message = args[3];

        ObjectOutputStream nodeOut = new ObjectOutputStream(nodeSocket.getOutputStream());
        nodeOut.writeObject(m);
        nodeOut.close();

        // Old code
        //        FileOutputStream fs = new FileOutputStream("messagetmp");
        //        ObjectOutputStream out = new ObjectOutputStream(fs);
        //
        //        out.writeObject(m);
        //        out.close();
    }
}
