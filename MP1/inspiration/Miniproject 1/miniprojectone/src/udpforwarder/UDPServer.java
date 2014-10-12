package udpforwarder;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;

public class UDPServer {
	
	public static void main(String[] args) {
		InetSocketAddress host = new InetSocketAddress(args[0], Integer.parseInt(args[2]));
		int portListen = Integer.parseInt(args[1]);
		
		DatagramSocket socket = null;
		try {
			socket = new DatagramSocket(portListen);
			System.out.println("Listening at port: " + socket.getLocalPort());
			byte[] buffer = new byte[1000];
			while(true) {
				DatagramPacket request = new DatagramPacket(buffer, buffer.length);
				socket.receive(request);
				System.out.println("Received " + request.getLength() + " bytes from " + request.getAddress().toString() + ":" + request.getPort());
				DatagramPacket forward = new DatagramPacket(request.getData(), request.getLength(), host);
				socket.send(forward);
			}
		} catch (SocketException e) {
			System.out.println("Socket error: " + e.getMessage());
		} catch (IOException e) {
			System.out.println("Error during read/write: " + e.getMessage());
		} finally {
			socket.close();
		}
	}
	
}
