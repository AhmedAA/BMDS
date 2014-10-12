package rfc862;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class UDPClient {

	 public static void main(String args[]){ 
	        DatagramSocket aSocket = null;
	        try {
	            aSocket = new DatagramSocket();
	            String message = "TEST";
	            System.out.println("Original message: " + message);
	            byte [] m = message.getBytes();
	            InetAddress aHost = InetAddress.getByName("localhost");
	            int serverPort = 7007;                                                       
	            DatagramPacket request =
	                new DatagramPacket(m,  message.length(), aHost, serverPort);
	            aSocket.send(request);                                  
	            byte[] buffer = new byte[1000];
	            DatagramPacket reply = new DatagramPacket(buffer, buffer.length);   
	            aSocket.receive(reply);
	            System.out.println("Reply from the server: " + new String(reply.getData()));    
	        }catch (SocketException e){System.out.println("Socket: " + e.getMessage());
	        }catch (IOException e){System.out.println("IO: " + e.getMessage());
	        }finally {if(aSocket != null) aSocket.close();}
	    }    

}
