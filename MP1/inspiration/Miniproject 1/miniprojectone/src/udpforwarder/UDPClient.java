package udpforwarder;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;

public class UDPClient {

	// This client is for testing the UDP forwarding server.
	public static void main(String[] args) {
		InetSocketAddress host = new InetSocketAddress(args[0], Integer.parseInt(args[2]));
		int portListen = Integer.parseInt(args[1]);
		String message = "TestMessageTestMessageTestMessageTestMessageTestMessageTestMessageTestMessage";
		byte[] mBytes = message.getBytes();

		DatagramSocket socket = null;
		try {
			socket = new DatagramSocket(portListen);
			DatagramPacket request = new DatagramPacket(mBytes, mBytes.length, host);
			System.out.println("Sending: " + message);
			socket.send(request);
			byte[] buffer = new byte[1000];
			DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
			socket.receive(reply);
			System.out.println("Reply: " + new String(reply.getData()));
		} catch (SocketException e) {
			System.out.println("Socket error: " + e.getMessage());
		} catch (IOException e) {
			System.out.println("Error during read/write: " + e.getMessage());
		} finally {
			socket.close();
		}
	}

}
