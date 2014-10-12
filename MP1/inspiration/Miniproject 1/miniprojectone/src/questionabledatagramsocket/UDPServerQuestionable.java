package questionabledatagramsocket;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;

public class UDPServerQuestionable {
	
	public static void main(String[] args) {
		start("localhost", 1337, 1338);
	}
	
	public static void start(String hostString, int portListen, int portSend) {
		InetSocketAddress host = new InetSocketAddress(hostString, portSend);
		
		DatagramSocket socket = null;
		try {
			// Here we use the Questionable Datagram Socket with 33% failures.
			socket = new QuestionableDatagramSocket(portListen, 33);
			System.out.println("Listening at port: " + socket.getLocalPort());
			byte[] buffer = new byte[100];
			while(true) {
				DatagramPacket request = new DatagramPacket(buffer, buffer.length);
				socket.receive(request);
				System.out.println("Received " + request.getLength() + " bytes from " + request.getAddress().toString() + ":" + request.getPort() + " Message: " + new String(request.getData(),0,request.getLength()));
				DatagramPacket forward = new DatagramPacket(request.getData(), request.getLength(), request.getAddress(), portSend);
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
