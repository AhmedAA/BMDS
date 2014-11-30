import java.io.IOException;
import java.net.Socket;

/**
 * Created by ahmed on 13/11/14.
 */
public class Get {
    public static void main(String[] args) throws IOException {
        Message m = new Message();
        Socket nodeSocket = null;

        String nodeIP = args[0];
        int nodePort = Integer.parseInt(args[1]);

        nodeSocket = new Socket(nodeIP, nodePort);
    }
}
