import java.net.*;
import java.io.*;

class Client {
    private static Socket server;
	private static BufferedReader buffer;
	private static String data;
	public static void main(String args[]) {
		try {
			server = new Socket("localhost",2000);
			data = "";
			buffer = new BufferedReader(new InputStreamReader(server.getInputStream()));
			do {
			    System.out.println(data);
			} while(!(data = buffer.readLine()).equals("stop"));
			buffer.close();
			server.close();
		} catch(Exception e) {
			System.out.println("Could not create client" + e);
		}

	}

}
