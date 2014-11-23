package RFC862;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

/**
 * Created by ahmed on 23/11/14.
 */
public class RfcUDP {
    public static void main(String[] args){
        DatagramSocket aSocket = null;

        try {
            aSocket = new DatagramSocket(7007);
            byte[] buffer = new byte[1000];

            while(true){
                DatagramPacket request = new DatagramPacket(buffer, buffer.length);
                System.out.println("Waiting for connections");
                aSocket.receive(request);
                System.out.println("Received: " + new String(request.getData()));
                DatagramPacket reply = new DatagramPacket(request.getData(), request.getLength(),
                        request.getAddress(), request.getPort());
                System.out.println("Message sent back");
                aSocket.send(reply);
            }
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
