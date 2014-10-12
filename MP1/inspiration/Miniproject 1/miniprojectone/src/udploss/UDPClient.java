package udploss;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;

public class UDPClient {

	private DatagramSocket socket = null;
	private InetSocketAddress hostAddress;
	private int localPort;
	private int size, quantity, interval;

	public UDPClient(String host, int port1, int port2, int size, int quantity,	int interval) {
		this.size = size;
		this.quantity = quantity;
		this.interval = interval;
		hostAddress = new InetSocketAddress(host, port2);
		localPort = port1;
		
		sendStartMessage();
		sendPackets();
		sendEndMessage();
	}

	public static void main(String[] args) {
		int size = Integer.parseInt(args[0]);
		int quantity = Integer.parseInt(args[1]);
		int interval = Integer.parseInt(args[2]);

		new UDPClient("localhost", 80, 8080, size, quantity, interval);
	}
	
	
	/**
	 * Sends the message starting the transfer. Will wait for reply from server.
	 */
	private void sendStartMessage() {
		String message = "START " + size + "." + quantity + "." + interval;
		byte[] mBytes = message.getBytes();

		try {
			// Create new socket on client
			socket = new DatagramSocket(localPort);
			
			// Sending the start packet
			DatagramPacket request = new DatagramPacket(mBytes, mBytes.length, hostAddress);
			System.out.println("Sending start message: " + message);
			socket.send(request);

			// Receiving the packet from server
			byte[] buffer = new byte[mBytes.length]; // ??
			DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
			socket.receive(reply);
			System.out.println("Reply: " + new String(reply.getData()));
			
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} catch (SocketException e) {
			System.out.println("Socket error: " + e.getMessage());
		} catch (IOException e) {
			System.out.println("Error during read/write: " + e.getMessage());
		} finally {
			socket.close();
		}
	}
	
	/**
	 * Sends the packets
	 */
	private void sendPackets() {
		
		try {
			socket = new DatagramSocket(localPort);
			
			// Create a number of packets, based on input quantity, and send them to the server
			for (int i = 0; i < quantity; i++) {
				// Creating the packet
				String message = "DATA " + i;
				byte[] dest = new byte[size];
				byte[] bytes = message.getBytes();
				System.arraycopy(bytes, 0, dest, 0, bytes.length);
				DatagramPacket request = new DatagramPacket(dest, dest.length, hostAddress);
	
				// Sending the packet
				System.out.println("Sending: " + message);
				socket.send(request);
				//wait interval
				try {
					Thread.sleep(interval);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		} catch (SocketException e) {
			System.out.println("Socket error: " + e.getMessage());
		} catch (IOException e) {
			System.out.println("Error during read/write: " + e.getMessage());
		} finally {
			socket.close();
		}
	}
	
	/**
	 * Send the message ending the packet transfer
	 */
	private void sendEndMessage() {
		String message = "END";
		byte[] mBytes = message.getBytes();
		int lostNum, lostPercent, duplicatedNum, duplicatedPercent;

		try {
			// Create new socket on client
			socket = new DatagramSocket(localPort);
			
			// Sending the start packet
			DatagramPacket request = new DatagramPacket(mBytes, mBytes.length, hostAddress);
			System.out.println("Sending end message: " + message);
			socket.send(request);

			// Receiving the packet from server
			byte[] buffer = new byte[500];
			DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
			socket.receive(reply);
			//System.out.println("Reply: " + new String(reply.getData()));
			
			// Extract data from the reply
			String replyMsg = new String(reply.getData());
			String[] metaData = replyMsg.substring(7).split("\\."); //reply message starts with "REPLY "
			lostNum = Integer.parseInt(metaData[0]);
			lostPercent = Integer.parseInt(metaData[1]);
			duplicatedNum = Integer.parseInt(metaData[2]);
			duplicatedPercent = Integer.parseInt(metaData[3].replaceAll("[^0-9]", ""));
			
			// Print the results
			printResult(lostNum, lostPercent, duplicatedNum, duplicatedPercent);
		} catch (SocketException e) {
			System.out.println("Socket error: " + e.getMessage());
		} catch (IOException e) {
			System.out.println("Error during read/write: " + e.getMessage());
		} finally {
			socket.close();
		}
	}
	
	/**
	 * print the result to the console
	 */
	private void printResult(int lostNum, int lostPercent, int duplicatedNum, int duplicatedPercent) {
		System.out.println("Number of lost datagrams: " + lostNum);
		System.out.println("Percentage of lost datagrams: " + lostPercent);
		System.out.println("Number of duplicated datagrams: " + duplicatedNum);
		System.out.println("Percentage of duplicated datagrams: " + duplicatedPercent);
	}
}
