package tcpforwarder;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServer {

	public static void main(String[] args) {
		int listenPort = 8080;

		ServerSocket listenSocket = null;
		Socket clientSocket = null;

		try {
			listenSocket = new ServerSocket(listenPort);
			while (true) {
				System.out.println("Now listening for new clients!");
				clientSocket = listenSocket.accept();
				System.out.println("Accepted connection");
				new Connection(clientSocket);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				listenSocket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}

class Connection extends Thread {
	private static String host = "itu.dk";
	private static int hostPort = 80;

	private DataInputStream in;
	private DataOutputStream out;
	private Socket clientSocket;
	private DataInputStream forwardIn;
	private DataOutputStream forwardOut;
	private Socket forward;

	public Connection(Socket clientSocket) throws Exception {
		this.clientSocket = clientSocket;
		in = new DataInputStream(clientSocket.getInputStream());
		out = new DataOutputStream(clientSocket.getOutputStream());
		forward = new Socket(host, hostPort);
		forwardIn = new DataInputStream(forward.getInputStream());
		forwardOut = new DataOutputStream(forward.getOutputStream());
		this.start();
	}

	@Override
	public void run() {
		try {
			byte[] data = new byte[15000];
			in.read(data);
			System.out.println("From client: " + new String(data));
			forwardOut.write(data);
			boolean eof = false;
			while (!eof) {
				try {
					// out.writeByte(forwardIn.readByte());
					out.writeByte(forwardIn.readByte());
				} catch (EOFException e) {
					eof = true;
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				clientSocket.close();
				forward.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}