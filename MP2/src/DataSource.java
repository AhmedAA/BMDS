import java.io.*;
import java.net.*;
import java.util.Scanner;

/**
 * Created by ahmed on 29/09/14.
 * Connects to the controller and sends data.
 */
public class DataSource {

    public static void main(String[] args) {

        Thread inputThread = new Thread(new Runnable() {
            @Override
            public void run() {

                Socket s = null;
                int serverPort = 7896;

                try {
                    s = new Socket("localhost", serverPort);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                DataOutputStream out = null;
                try {
                    out = new DataOutputStream(s.getOutputStream());
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Scanner scan = new Scanner(System.in);
                String input = "";
                while (true) {
                    System.out.println("Type something: ");
                    input = scan.nextLine();
                    try {
                        out.writeUTF(input);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    System.out.println("Input: "+input);
                }
            }
        });

        inputThread.start();
    }

}

