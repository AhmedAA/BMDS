/**
 * Created by Rasmus on 11-09-2014.
 */

import java.net.*;
import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPForwarder {

    private InetAddress host = null;

    public static void UDPForwarder(String host, int p1, int p2) {

        DatagramSocket p1Socket = null;
        DatagramSocket p2Socket = null;

        try {

            // creates data packet
            byte[] buffer = new byte[1000];
            DatagramPacket data = new DatagramPacket(buffer, buffer.length);

            while(true){

                // creates p1 ("from" socket)
                p1Socket = new DatagramSocket(null);

                // binds p1Socket ("from" socket) to "localhost:p1"
                p1Socket.bind(new InetSocketAddress("localhost", p1));

                // awesome print
                System.out.println("Listening at " + p1Socket.getLocalAddress() + ":" +
                        p1Socket.getLocalPort());

                // listening ...
                System.out.println ("Listening..." + "\n");

                // receiving data on p1 ("from" socket)
                p1Socket.receive(data);

                // creating p2 ("to" socket)
                p2Socket = new DatagramSocket(null);

                // binds p2Socket ("to" socket) to "host:p2"
                p2Socket.bind(new InetSocketAddress(host, p2));

                // sending data to p2
                p2Socket.send(data);

                System.out.println("Data received from: " + p1Socket.getLocalAddress() + ":" +
                        p1Socket.getLocalPort() + "\n");

                System.out.println("Data sent to: " + p2Socket.getLocalAddress() + ":" +
                        p2Socket.getLocalPort() + "\n");

                System.out.println("Data sent: " + new String(data.getData()) + "\n");

                p1Socket.close();
                p2Socket.close();
            }

        }catch (SocketException e){System.out.println("Socket: " + e.getMessage());
        }catch (IOException e) {System.out.println("IO: " + e.getMessage());
        }finally {if(p2Socket != null || p1Socket != null) p2Socket.close(); p1Socket.close();}
    }

    public static void main(String args[]) throws SocketException {

        if (args.length < 3) {
            System.out.println ("Usage: host port1 port2");
            System.exit(-1);
        }

        UDPForwarder(args[0], Integer.parseInt(args[1]), Integer.parseInt(args[2]));
    }
}