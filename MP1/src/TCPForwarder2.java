import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.*;

/**
 * Created by Rasmus on 14-09-2014.
 */
public class TCPForwarder2 {

    public static void TCPForwarder(String host, int p1, int p2) throws IOException {

        // creates the listener at localhost:p2
        ServerSocket serverSocket = new ServerSocket();
        serverSocket.bind(new InetSocketAddress("localhost", p2));

        InputStream data = null;
        OutputStream out = null;

        // listen loop
        while(true) {

            Socket source = new Socket();
            Socket target = null;

            source.connect(new InetSocketAddress(host, p1));
            target = serverSocket.accept();

            // getting the data
            data = source.getInputStream();
            out = target.getOutputStream();

//            byte[] buff = new byte[1000];
//
//            while (true) {
//
//                try {
//
//                    int bytesRead = data.read(buff);
//
//                    if (bytesRead == -1) {
//                        break;
//                    }
//                    out.write(buff, 0, bytesRead);
//                    out.flush();
//
//                } catch (IOException e) {data.close();}
//            }

            System.out.println("Connected to: " + source.getInetAddress() + ":" + source.getPort());
        }
    }

    public static void main(String[] args) throws IOException {

        TCPForwarder(args[0], Integer.parseInt(args[1]), Integer.parseInt(args[2]));

    }

}
