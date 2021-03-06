import java.net.*;
import java.io.*;
import java.util.Scanner;

public class TCPClient {
    public static void main (String args[]) throws Exception
    {
        if (args.length < 2) {
            System.out.println ("Usage: ... host msg");
            System.exit(-1);
        }

        Scanner scanner = new Scanner(System.in);
        Socket s = null;
        int serverPort = 7896;
        s = new Socket(args[0], serverPort);
        DataInputStream in = new DataInputStream(s.getInputStream());
        DataOutputStream out = new DataOutputStream(s.getOutputStream());

        while(true) {
            String text = scanner.nextLine();
            out.writeUTF(text);            // UTF is a string encoding see Sec. 4.4
            String data = in.readUTF();        // read a line of data from the stream
            System.out.println("Received: " + data);

        }
    }
}
