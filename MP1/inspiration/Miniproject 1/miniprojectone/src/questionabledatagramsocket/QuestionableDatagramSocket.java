package questionabledatagramsocket;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.Random;

public class QuestionableDatagramSocket extends DatagramSocket {
	
	private int percentageFails;
	private Random random = new Random();
	private DatagramPacket tempPacket = null;
	
	public QuestionableDatagramSocket(int port, int percentageFails) throws SocketException {
		super(port);
		this.percentageFails = percentageFails;
	}

	@Override
	public void send(DatagramPacket dgp) throws IOException {
		if (random.nextInt(100) < percentageFails-1) {
			int typeOfFault = random.nextInt(3); // 0 - Discard, 1 - duplicate, 2 - reorder.
			if (typeOfFault == 0) {
				// Discard the packet (Don't send it).
				System.err.println("QDGS: Packet discarded! Message: " + new String(dgp.getData(),0,dgp.getLength()));
				return;
			} else if (typeOfFault == 1) {
				// Duplicate the packet.
				System.err.println("QDGS: Packet duplicated! Message: " + new String(dgp.getData(),0,dgp.getLength()));
				if (tempPacket != null) {
					System.err.println("QDGS: Packet reordered! 1st mes: " + new String(dgp.getData(),0,dgp.getLength()) + " 2nd mes: " + new String(tempPacket.getData(),0,tempPacket.getLength()));
					super.send(dgp);
					super.send(tempPacket);
					tempPacket = null;
				} else {
					super.send(dgp);
				}
				super.send(dgp);
			} else {
				// Reorder packets. Save current one and send it, after the next one.
				// This also makes it possible to discard the packet this way, if it was the last packet.
				if (tempPacket == null) {
					System.err.println("QDGS: Packet reordered! Message saved: " + new String(dgp.getData(),0,dgp.getLength()));
					tempPacket = dgp;
				} else {
					System.err.println("QDGS: Packet reordered! 1st mes: " + new String(dgp.getData(),0,dgp.getLength()) + " 2nd mes: " + new String(tempPacket.getData(),0,tempPacket.getLength()));
					super.send(dgp);
					super.send(tempPacket);
					tempPacket = null;
				}
			}
		} else {
			if (tempPacket != null) {
				System.err.println("QDGS: Packet reordered! 1st mes: " + new String(dgp.getData(),0,dgp.getLength()) + " 2nd mes: " + new String(tempPacket.getData(),0,tempPacket.getLength()));
				super.send(dgp);
				super.send(tempPacket);
				tempPacket = null;
			} else {
				super.send(dgp);
			}
			
		}
	}
	
}
