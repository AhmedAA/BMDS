package reliableudp;

import java.io.IOException;

public class ReliableUDPServer {

	public static void main(String[] args) throws IOException {
		ReliableUDPSocket udpSocket = new ReliableUDPSocket(16000,1400);
		System.out.println("Now listening for messages!");
		ReceivedUdpMessage rum = udpSocket.receive();
		udpSocket.close();
		System.out.println("Full message received: " + rum.getMessage());
	}
	
}
