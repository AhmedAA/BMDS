package reliableudp;

import java.io.Closeable;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class ReliableUDPSocket implements Closeable  {

	protected static final int CHARSPERPACKET = 5;
	protected static final String HEADERSEPARATOR = ".-.";
	
	// The socket, address and port to use for sending messages.
	private DatagramSocket sendToSocket = null;

	// The socket and port to use for listening to messages. And other things.
	private PacketReceiver packRec = null; // Needs the listenToPort to be set before being initialised.
	protected DatagramSocket listenToSocket = null;
	protected int listenToPort = 0;
	protected Map<String, String[]> packetsReceived = Collections.synchronizedMap(new HashMap<String, String[]>());
	protected LinkedList<ReceivedUdpMessage> listOfReceivedMessages = new LinkedList<>();
	
	// Close everything!
	protected boolean close = false;
	
	public ReliableUDPSocket(int sendToListenPort, int listenToPort) {
		try {
			
			this.sendToSocket = new DatagramSocket(sendToListenPort);
			this.listenToPort = listenToPort;
			this.packRec = new PacketReceiver();
			this.packRec.start();
			
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}

	// Send a message to the receiver!
	public void send(String sendToAddress, int sendToPort, String message) {
		InetAddress sendToInetAddress = null;
		try {
			sendToInetAddress = InetAddress.getByName(sendToAddress);
		} catch (UnknownHostException e1) {
			e1.printStackTrace();
		}

		// How many packets will we need to send.
		int totalPackets = (int) Math.ceil((double) message.length()
				/ (double) CHARSPERPACKET);

		ArrayList<DatagramPacket> packetList = new ArrayList<>();
		for (int i = 0; i < totalPackets; i++) {
			int iStart = (i * CHARSPERPACKET);
			int iEnd = iStart + CHARSPERPACKET;
			if (iStart + CHARSPERPACKET >= message.length())
				iEnd = message.length();
			String messagePart = listenToPort + HEADERSEPARATOR + i + HEADERSEPARATOR + totalPackets + HEADERSEPARATOR
					+ message.substring(iStart, iEnd);
			DatagramPacket packet = new DatagramPacket(messagePart.getBytes(),
					(messagePart.getBytes()).length, sendToInetAddress, sendToPort);
			packetList.add(packet);
		}

		// While message isn't done sending, try sending the message.
		boolean isDone = false;
		while (!isDone && !close) {
			for (int i = 0; i < packetList.size(); i++) {
				boolean packetReceived = false;
				while (!packetReceived && !close) {
					try {
						System.out.println("Sending: " + new String(packetList.get(i).getData(), 0, packetList.get(i).getData().length) + "\t packet " + (i+1) + "/" + packetList.size());
						sendToSocket.send(packetList.get(i));
						byte[] buffer = new byte[100];
						DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
						sendToSocket.setSoTimeout(1000);
						sendToSocket.receive(reply);
						String replyMessage = new String( reply.getData(), 0, reply.getLength());
						System.out.println("Reply: " + new String(reply.getData(), 0, reply.getLength()));
						// If the current packet was received on the server, continue to the next packet, if any.
						if (getPckNb(replyMessage) == i) {
							packetReceived = true;
							System.out.println("Packet " + new String( reply.getData(), 0, reply.getLength()) + " succesfully send to reciever!");
						}
					} catch (SocketTimeoutException e) {
						
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				try {
					while(!close) {
						System.out.println("Listening for acknowledgement that the message was received...");
						byte[] buffer = new byte[100];
						DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
						sendToSocket.receive(reply);
						String replyMessage = new String(reply.getData(), 0, reply.getLength());
						// If the last packet was received on the server, quit sending.
						if (getPckNb(replyMessage) == -1) {
							System.out.println("Acknowledgement received!");
							isDone = true;
							return;
						}
					}
				} catch (IOException e) {
					System.err.println("Socket timed out, waiting for a reply...");
				}
			}
		}
	}
	
	/**
	 * The reply from the receiver will hold only the number of the packet received.
	 * Only use this method from within the send method.
	 * @param replyMessage
	 * @return The number of the packet received.
	 */
	private int getPckNb(String replyMessage) {
		return Integer.parseInt(replyMessage);
	}
	
	/**
	 * This class will listen for incoming UDP messages and invoke a packet handler to handle the actual packets.
	 * Needs the listenToPort to be initialised before instantiation.
	 */
	private class PacketReceiver extends Thread {
		public PacketReceiver() {
			try {
				listenToSocket = new DatagramSocket(listenToPort);
			} catch (SocketException e) {
				e.printStackTrace();
			}
		}
		
		public void run() {
			while(!close) {
				try {
					byte[] buffer = new byte[100];
					DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
					listenToSocket.receive(reply);
					ReceivedPacket packet = new ReceivedPacket(reply);
					System.out.print("Received packet: " + new String(reply.getData(), 0, reply.getData().length));
					System.out.println("\t packet " + (packet.getPacketNb()+1) + "/" + packet.getTotalNbOfPackets());
					ReceivedPacketHandler rph = new ReceivedPacketHandler(packet);
					rph.start(); // Handle the received packet in a separate thread.
					byte[] replyByteArray = ((Integer)packet.getPacketNb()).toString().getBytes();
					DatagramPacket replyToClientPacket = new DatagramPacket(replyByteArray, replyByteArray.length, packet.getSenderInetAddress(), packet.getSenderReplyPort());
					listenToSocket.send(replyToClientPacket);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * Represents a received packet in the reliable UDP system.
	 */
	private class ReceivedPacket {
		private int senderListenToPort, packetNb, totalNbOfPackets;
		private String messagePart;
		private DatagramPacket packet;
		
		public int getSenderListenToPort() { return senderListenToPort; }
		public int getPacketNb() { return packetNb; }
		public int getTotalNbOfPackets() { return totalNbOfPackets; }
		public String getMessagePart() { return messagePart; }
		public DatagramPacket getPacket() { return packet; }
		public String getSenderAddress() { return packet.getAddress().toString(); }
		public InetAddress getSenderInetAddress() { return packet.getAddress(); }
		public int getSenderReplyPort() { return packet.getPort(); }
		
		public ReceivedPacket(DatagramPacket packet) {
			String replyData = new String(packet.getData(), 0, packet.getLength());
			String[] dataInfo = replyData.split(HEADERSEPARATOR);
			senderListenToPort = Integer.parseInt(dataInfo[0]);
			packetNb = Integer.parseInt(dataInfo[1]);
			totalNbOfPackets = Integer.parseInt(dataInfo[2]);
			messagePart = dataInfo[3];
			this.packet = packet;
		}
	}
	
	/**
	 * This is a handler for handling an incoming ReceivedPacket.
	 * It puts it into the correct data structure and in the correct order.
	 */
	private class ReceivedPacketHandler extends Thread {
		
		private ReceivedPacket receivedPacket;
		
		public ReceivedPacketHandler(ReceivedPacket receivedPacket) {
			this.receivedPacket = receivedPacket;
		}
		
		public void run() {
			// If packets have already been received from this host, add packet message.
			if (packetsReceived.containsKey(receivedPacket.getSenderAddress())) {
				String[] packets = packetsReceived.get(receivedPacket.getSenderAddress());
				packets[receivedPacket.getPacketNb()] = receivedPacket.getMessagePart();
			} else {
				// If new host, create array for packet messages.
				String[] packets = new String[receivedPacket.getTotalNbOfPackets()];
				packets[receivedPacket.getPacketNb()] = receivedPacket.getMessagePart();
				packetsReceived.put(receivedPacket.getSenderAddress(), packets);
			}
			
			// If all packets are received, move the whole message to the list of received messages.
			String[] packets = packetsReceived.get(receivedPacket.getSenderAddress());
			if (isArrayFull(packets)) {
				ReceivedUdpMessage rm = new ReceivedUdpMessage(packets, receivedPacket.getSenderAddress(), receivedPacket.getSenderListenToPort());
				listOfReceivedMessages.add(rm);
				packetsReceived.remove(receivedPacket.getSenderAddress());
				try {
					byte[] replyByteArray = ("-1").getBytes();
					DatagramPacket messageReceivedPacket = new DatagramPacket(replyByteArray, replyByteArray.length, receivedPacket.getSenderInetAddress(), receivedPacket.getSenderReplyPort());
					listenToSocket.send(messageReceivedPacket);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	private boolean isArrayFull(String[] messageArray) {
		if (messageArray == null) {
			return false;
		}
		for (String string : messageArray) {
			if (string == null) {
				return false;
			}
		}
		return true;
	}
	
	public ReceivedUdpMessage receive() {
		while (listOfReceivedMessages.isEmpty()) {
			// Wait! Blocking call.
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// Do nothing if interrupted.
			}
		}
		return listOfReceivedMessages.pollFirst();
	}

	@Override
	public void close() throws IOException {
		close = true;
	}

}
