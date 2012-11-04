import java.io.*;//importing io classes
import java.net.*;//importing networking classes
class Client { 
    private static Socket server; //server socket
	private static BufferedReader inBuffer;
	private static String serverOutput;//string storing output from server
	private static Sender sender;
    private static PrintWriter outStream;//output stream to server
	private static String keyboardInput;//string storing input from client 
	private static BufferedReader outBuffer;//buffer to store characters from keyboard
    public static void main(String argc[]) {
	    try {
			System.out.println("Connecting to server...");
            server = new Socket("localhost",2000);//connecting to server
			System.out.println("Server connected....\nStarting chat...\n\n\n\n\n");
			inBuffer = new BufferedReader(new InputStreamReader(server.getInputStream()));
			sender =  new Sender();
			while( !( serverOutput = inBuffer.readLine() ).equals("stop") ) {
			    System.out.println("\nServer: " + serverOutput+"\nme: ");
			}
			disconnectClient();
			System.out.println("Server disconnected");//never happens due to exception :P
		} catch(Exception e) {
		    System.out.println("Could not create client...");
			System.exit(-1);
		}
	}
	private static void disconnectClient() {
	    try {
		    outBuffer.close();
		    outStream.close();
		    server.close();
			inBuffer.close();
		} catch(Exception e) {
		    System.out.println("Could not stop server!");
			System.exit(-1);
		}
	}
	static class Sender extends Thread  {//Thread which sends  data to server
	    Sender(){
		    super("Sender Thread");//initialising thread name
			try {
			    outStream = new PrintWriter(server.getOutputStream());
				outBuffer = new BufferedReader(new InputStreamReader(System.in));
			} catch(Exception e) {
		        System.out.println("Could initialise  output stream...");
			    System.exit(-1);
			}
			keyboardInput = "Ready to chat";
			start();
		}
		public void run() {
		    try {
		        do {
				    outStream.println(keyboardInput);
				    outStream.flush();
				    System.out.println("\nme: ");
			    } while( !(keyboardInput = outBuffer.readLine()).equals("stop") );
			    System.out.println("Disconnected from the server");
			    disconnectClient();
			} catch(Exception e) {
		        System.out.println("Could not read from input buffer..");
			    System.exit(-1);
			}
		}
	}
}