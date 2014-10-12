package reliableudp;

import java.io.IOException;

public class ReliableUDPClient {

	public static void main(String[] args) throws IOException {
		
		String message = "Lorem Ipsum, dolor sit amet, something else and more to go! Hope this works as nicely as the other one.";
		ReliableUDPSocket udpSocket = new ReliableUDPSocket(1600,1400);
		System.out.println("Now sending message: " + message);
		udpSocket.send("192.168.1.9", 1400, message);
		udpSocket.close();
		System.out.println("Message now fully sent!");
	}
	
}
