package tcpforwarder;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public class TCPClient {

	public static void main(String[] args) throws Exception {
		int hostPort = 8080;
		String host = "localhost";
		
		String message = "TestMessage";
		
		Socket serverSocket = null;
		
		try {
			serverSocket = new Socket(host, hostPort);
			DataInputStream in = new DataInputStream(serverSocket.getInputStream());
			DataOutputStream out = new DataOutputStream(serverSocket.getOutputStream());
			out.writeUTF(message);
			String reply = in.readUTF();
			System.out.println("Reply: " + reply);
		} finally {
			serverSocket.close();
		}
	}
	
}
