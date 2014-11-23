package UDPDatagramEstimator;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

/**
 * Created by RLNC on 23-11-2014.
 */
public class Tracker {

    private DatagramSocket socket;
    private int size, quantity;
    private int[] data;

    public Tracker(int port1) {
        try {
            socket = new DatagramSocket(port1);
            waitStartMessage();
            receiveData();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            socket.close();
        }
    }

    public static void main(String[] args) {
        new Tracker(8080);
    }

    private void waitStartMessage() throws IOException {
        // buffer
        byte[] buffer = new byte[500];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

        while (true) {
            // receive
            socket.receive(packet);
            String message = new String(packet.getData());
            System.out.println("Server received START message: " + message);

            // check
            if (message.startsWith("START")) {
                // data
                String[] metaData = message.substring(6).split("\\.");
                size = Integer.parseInt(metaData[0]);
                quantity = Integer.parseInt(metaData[1]);
                // create data array
                data = new int[quantity];
                break;
            }
        }

        // reply
        DatagramPacket echo = new DatagramPacket(packet.getData(),
                packet.getData().length, packet.getSocketAddress());
        socket.send(echo);
    }

    private void receiveData() throws IOException {

        System.out.println("Started receiving DATA...");

        while (true) {
            byte[] buffer = new byte[size];
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            // receive
            socket.receive(packet);
            String message = new String(packet.getData());
            System.out.println("Server received DATA: " + message);

            // check
            if (message.startsWith("DATA")) {
                String justTheNumber = message.replaceAll("[^0-9]", "");
                int number = Integer.parseInt(justTheNumber);

                data[number]++;
                continue;
            }

            // check
            if (message.startsWith("END")) {
                System.out.println("Received END message");
                int[] r = calculateResult();
                String rMessage = "RESULT " + r[0] + "." + r[1]
                        + "." + r[2] + "." + r[3];
                System.out.println(rMessage);
                byte[] bytes = rMessage.getBytes();

                // reply with result
                DatagramPacket reply = new DatagramPacket(bytes,
                        bytes.length, packet.getSocketAddress());
                socket.send(reply);
                return;
            }
        }
    }

    private int[] calculateResult() {
        // total packets received
        int apl = 0;
        for (int p : data) {
            if (p == 0)
                apl++;
        }
        // relative packets lost (in percent)
        int rpl = (apl * 100) / quantity;

        // absolute number of duplicates
        int dups = 0;
        for (int p : data) {
            if (p > 1)
                dups += p - 1;
        }
        // relative number of duplicates (in percent)
        int rdups = (dups * 100) / quantity;

        return new int[] { apl, rpl, dups, rdups };
    }
}
