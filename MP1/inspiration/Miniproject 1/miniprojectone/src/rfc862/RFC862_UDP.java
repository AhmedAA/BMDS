package rfc862;

import java.net.*;
import java.io.*;

public class RFC862_UDP {
	public static void main(String args[]){ 
        DatagramSocket aSocket = null;
        try{
            aSocket = new DatagramSocket(7007);
            byte[] buffer = new byte[1000];
            while(true){
                DatagramPacket request = new DatagramPacket(buffer, buffer.length);
                System.out.println ("Listening...");
                aSocket.receive(request);     
                System.out.println("Received " +
                        request.getLength() + " bytes" +
                        " from " + 
                        request.getAddress().toString() +
                        ":"  + request.getPort() +
                        "\nMessage: " + new String(request.getData())        
                		);
                DatagramPacket reply = new DatagramPacket(request.getData(), request.getLength(), 
                        request.getAddress(), request.getPort());
                System.out.println("Message sent back.");
                aSocket.send(reply);
            }
        }catch (SocketException e){System.out.println("Socket: " + e.getMessage());
        }catch (IOException e) {System.out.println("IO: " + e.getMessage());
        }finally {if(aSocket != null) aSocket.close();}
    }
}
