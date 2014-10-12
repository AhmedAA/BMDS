package questionabledatagramsocket;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;

public class UDPClientQuestionable {

	public static void main(String[] args) {
		UDPClientQuestionable udpClient = new UDPClientQuestionable();
		PacketReceiver pr = new PacketReceiver(1338);
		pr.start();
		for (Integer i = 0; i < 100; i++) {
			udpClient.sendPacket("localhost", 1337, i.toString());
		}
		try {
			pr.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void sendPacket(String hostString, int portSend,String message) {
		InetSocketAddress host = new InetSocketAddress(hostString, portSend);
		byte[] mBytes = null;

		DatagramSocket socket = null;
		try {
			socket = new DatagramSocket();
			mBytes = message.getBytes();
			DatagramPacket request = new DatagramPacket(mBytes, mBytes.length,host);
			//System.out.println("Sending: " + message);
			socket.send(request);
		} catch (SocketException e) {
			System.out.println("Socket error: " + e.getMessage());
		} catch (IOException e) {
			System.out.println("Error during read/write: " + e.getMessage());
		} finally {
			socket.close();
		}
	}

}

class PacketReceiver extends Thread {
	int portListen;
	
	public PacketReceiver(int portListen) {
		this.portListen = portListen;
	}
	
	public void run() {
		DatagramSocket socket = null;
		try {
			socket = new DatagramSocket(portListen);
			while(true) {
				byte[] buffer = new byte[100];
				DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
				socket.receive(reply);
				System.out.println("Received: " + new String(reply.getData(),0,reply.getLength()));
			}
		} catch (SocketException e) {
			System.out.println("Socket error: " + e.getMessage());
		} catch (IOException e) {
			System.out.println("Error during read/write: " + e.getMessage());
		} finally {
			if (socket != null)
				socket.close();
		}
	}
}