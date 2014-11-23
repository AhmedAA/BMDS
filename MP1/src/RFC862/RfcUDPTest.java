package RFC862;

import java.io.IOException;
import java.net.*;

/**
 * Created by ahmed on 23/11/14.
 */
public class RfcUDPTest {
    public static void main(String[] args){
        DatagramSocket aSocket = null;
        try {
            aSocket = new DatagramSocket();
            String message = "Hello out there!";
            System.out.println("Sent: " + message);
            byte[] m = message.getBytes();
            InetAddress aHost = InetAddress.getByName("localhost");
            int serverPort = 7007;
            DatagramPacket request = new DatagramPacket(m, message.length(), aHost, serverPort);
            aSocket.send(request);
            byte[] buffer = new byte[1000];
            DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
            aSocket.receive(reply);
            System.out.println("Received the following: " + new String(reply.getData()));
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
