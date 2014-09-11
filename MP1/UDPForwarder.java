import java.net.*;
import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPForwarder{

    public void UDPForwarder(String host, Integer p1, Integer p2) {

        DatagramSocket p1Socket = null;
        DatagramSocket p2Socket = null;

        try {
            // listen at p1
            p1Socket = new DatagramSocket(p1);
            System.out.println("Listening at " + p1Socket.getLocalAddress() + ":" +
                p1Socket.getLocalPort());

            byte[] buffer = new byte[1000];
            while(true){

                DatagramPacket request = new DatagramPacket(buffer, buffer.length);
                System.out.println ("Listening...");

                // recieving data on p1
                p1Socket.recieve(request);
                System.out.println("Received " +
                    request.getLength() + " bytes" +
                    " from " +
                    request.getAddress().toString() +
                    ":" + request.getPort());

                // creating and forwarding to p2
                p2Socket = new DatagramSocket(p2);
                DatagramPacket reply = new DatagramPacket(request.getData(), request.getLength(),
                            request.getAddress(), request.getPort());

                p2Socket.send(reply);

            }
        }catch (SocketException e){System.out.println("Socket: " + e.getMessage());
        }catch (IOException e) {System.out.println("IO: " + e.getMessage());
        }finally {if(p2Socket != null) p2Socket.close();}
    }

    public static void main(String args[]){

        InetAddress localHost = new InetAddress.getLocalHost();
        DatagramSocket p1 = new DatagramSocket().getLocalPort();
        DatagramSocket p2 = new DatagramSocket(8888);
        UDPForwarder(localHost, p1, p2);
    }
}