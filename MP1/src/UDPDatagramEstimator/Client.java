package UDPDatagramEstimator;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;

/**
 * Created by RLNC on 23-11-2014.
 */
public class Client {

    private DatagramSocket socket = null;
    private InetSocketAddress hostAddress;
    private int localPort;
    private int size, quantity, interval;

    public Client(String host, int port1, int port2, int size, int quantity, int interval) {

        this.size = size;
        this.quantity = quantity;
        this.interval = interval;
        hostAddress = new InetSocketAddress(host, port2);
        localPort = port1;

        sendStartMes();
        sendMes();
        sendEndMes();
    }

    public static void main(String[] args) {

        int size = 8000; //Integer.parseInt(args[0]);
        int quantity = 1000; //Integer.parseInt(args[1]);
        int interval = 2; //Integer.parseInt(args[2]);

        new Client("localhost", 80, 8080, size, quantity, interval);
    }

    public void sendStartMes() {

        String message = "START " + size + "." + quantity + "." + interval;
        byte[] bytesMes = message.getBytes();

        try {

            socket = new DatagramSocket(localPort);
            DatagramPacket request = new DatagramPacket(bytesMes, bytesMes.length, hostAddress);
            System.out.println("Sending START message: " + message);

            // sending the start message
            socket.send(request);

            byte[] replyBuffer = new byte[bytesMes.length];
            DatagramPacket reply = new DatagramPacket(replyBuffer, replyBuffer.length);

            // receiving the reply
            socket.receive(reply);
            System.out.println("START message Reply: " + new String(reply.getData()));


        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            socket.close();
        }

    }

    public void sendMes() {

        try {
            socket = new DatagramSocket(localPort);

            // create a number of packages
            for (int i = 0; i < quantity; i++) {

                String message = "DATA " + i;
                byte[] dest = new byte[size];
                byte[] bytes = message.getBytes();
                System.arraycopy(bytes, 0, dest, 0, bytes.length);
                DatagramPacket request = new DatagramPacket(dest, dest.length, hostAddress);

                // Sending...
                System.out.println("Sending Message: " + message);
                socket.send(request);

                //waiting the defined interval
                try {
                    Thread.sleep(interval);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            socket.close();
        }

    }

    public void sendEndMes() {

        String message = "END";
        byte[] bytesMes = message.getBytes();
        int lostNum, lostPercent, duplicatedNum, duplicatedPercent;

        try {
            socket = new DatagramSocket(localPort);

            // Sending the end message
            DatagramPacket request = new DatagramPacket(bytesMes, bytesMes.length, hostAddress);
            System.out.println("Sending END message: " + message);
            socket.send(request);

            // Receiving end message
            byte[] buffer = new byte[500];
            DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
            socket.receive(reply);

            // looking at the reply data
            String replyMsg = new String(reply.getData());
            String[] metaData = replyMsg.substring(7).split("\\."); //reply message starts with "REPLY "
            lostNum = Integer.parseInt(metaData[0]);
            lostPercent = Integer.parseInt(metaData[1]);
            duplicatedNum = Integer.parseInt(metaData[2]);
            duplicatedPercent = Integer.parseInt(metaData[3].replaceAll("[^0-9]", ""));

            // One (wait - there is more!?) print to rule them all ....
            System.out.println("Lost datagrams: " + lostNum);
            System.out.println("Percentage - Lost datagrams: " + lostPercent);
            System.out.println("Duplicated datagrams: " + duplicatedNum);
            System.out.println("Percentage - Duplicated datagrams: " + duplicatedPercent);

        } catch (SocketException e) {
            System.out.println("Socket error: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Error during read/write: " + e.getMessage());
        } finally {
            socket.close();
        }

    }


}
