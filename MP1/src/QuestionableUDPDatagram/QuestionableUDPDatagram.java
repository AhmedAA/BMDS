
package QuestionableUDPDatagram;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.Random;

/**
 * Created by RLNC on 23-11-2014.
 */

public class QuestionableUDPDatagram extends DatagramSocket {

    private Random random = new Random();
    private int percentage;
    private DatagramPacket dupPackage;


    public QuestionableUDPDatagram(int port, int percentage) throws SocketException {

        super(port);
        this.percentage = percentage;
    }

    @Override
    public void send (DatagramPacket data) throws IOException {

        // We have to choose randomly between either discarding, duplicating or reordering packages
        // Furthermore its only gonna be a percentage of the packages which have to be treated this way.
        // The rest of the packages are being sent 'normally'.

        // Doing the dividing of packages based on the percentage value
        // if true - hell is about to break loose on the package!
        // if false - the package is going to be sent normally.
        if (random.nextInt(100) < percentage) {

            // randOP - discarding, duplicating or reordering?
            // 0: discarding; 1: duplicating; 2: reorder
            int randOP = random.nextInt(3);

            switch (randOP) {

                case 0: // discard
                    System.err.println("Questionable DatagramSocket: Packet discarded! Message: " + new String(data.getData(),0,data.getLength()));
                    break;

                case 1: // duplicating
                    System.err.println("Questionable DatagramSocket: Packet duplicated! Original message: " + new String(data.getData(),0,data.getLength()));
                    // create the duplicate package
                    dupPackage = data;
                    System.err.println("Questionable DatagramSocket: Packet duplicated! 1st mes: " + new String(data.getData(),0,data.getLength()) + " 2nd mes: " + new String(dupPackage.getData(),0,dupPackage.getLength()));
                    // sending the duplicated packages
                    super.send(data);
                    super.send(dupPackage);
                    dupPackage = null;
                    break;

                case 2: // reorder
                    if (dupPackage == null) {
                        System.err.println("Questionable DatagramSocket: Packet reordered! Message saved: " + new String(data.getData(),0,data.getLength()));
                        dupPackage = data;
                    }
                    else {
                        System.err.println("Questionable DatagramSocket: Packet reordered! 1st mes: " + new String(data.getData(),0,data.getLength()) + " 2nd mes: " + new String(dupPackage.getData(),0,dupPackage.getLength()));
                        super.send(data);
                        super.send(dupPackage);
                        dupPackage = null;
                    }
                    break;
            }
        }

        else {

            // package is going to be sent normally - all is well.
            super.send(data);
        }

    }
}
