import java.net.*;
import java.io.*;

class Client {
    private static Socket server;
    private static BufferedReader buffer;
    private static String data;
    public static void main(String args[]) {
        try {
            server = new Socket("localhost", 2000);
            System.out.println("Connected to server...");
            buffer = new BufferedReader(new InputStreamReader(server.getInputStream()));
            while (!(data = buffer.readLine()).equals("stop")) {
                System.out.println(data);
            }
            buffer.close();
            server.close();
            System.out.println("Disconnected from server...");
        } catch (Exception e) {
            System.out.println("Could not create client" + e);
        }

    }

}