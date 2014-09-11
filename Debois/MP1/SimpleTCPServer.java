import java.net.*;
import java.io.*;

public class SimpleTCPServer {
    public static void main (String args[]) throws Exception
    {
    		int port = 7896;	
        ServerSocket serverSocket = new ServerSocket(port);
        
    		try 
    		{	    		
    			while (true) 
	        {
	        		// Accept connection 
	        	
	            Socket socket = serverSocket.accept();   // blocking
	            DataInputStream dis = new DataInputStream(socket.getInputStream());
	            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
	            
	            // Read and print message
	            
	            String message = dis.readUTF();         // blocking
	            System.out.println(message);
	            
	            // Echo message back. 
	            
	            dos.writeUTF(message);
	            dis.close();
	            dos.close();
	            socket.close();
	            
	            Thread.sleep(10000);
	        }
    		} finally {
    			serverSocket.close();
    		}
    }
}


