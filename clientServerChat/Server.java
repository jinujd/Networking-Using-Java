import java.io.*;//importing io classes
import java.net.*;//importing networking classes
class Server {
    private static ServerSocket senderSocket;// socket for sending data to client
	private static Socket client;//wait for client to connect,which receives data from server
	private static PrintWriter outStream;
	private static BufferedReader outBuffer;//buffer to read from keyboard
	private static String keyboardInput;
	private static BufferedReader inBuffer;//buffer to accept data from client
	private static String clientOutput;//string storing output from client 
    public static void main(String argc[]) {
	    try {
	        senderSocket = new ServerSocket(2000); 
			System.out.println("Waiting for a client...");
		    client = senderSocket.accept();
			System.out.println("Client connected....\nStarting chat...\n\n\n\n\n");
			outStream = new PrintWriter(client.getOutputStream());//get client's output stream to write
			outBuffer = new BufferedReader(new InputStreamReader(System.in));
			keyboardInput = "Ready to chat";
			Receiver clientReceiver = new Receiver(); 
			do {
				outStream.println(keyboardInput);
				outStream.flush();
				System.out.println("\nme: ");
			}
			while(!(keyboardInput =  outBuffer.readLine()).equals("stop"));
			stopServer();
			System.out.println("Server disconnected");
		} catch(Exception e) {
		    System.out.println("Could not start server!");
			System.exit(-1);
		}
	}
	private static void stopServer() {
	    try {
		    outBuffer.close();
		    outStream.close();
		    client.close();
		    senderSocket.close();
		} catch(Exception e) {
		    System.out.println("Could not stop server!");
			System.exit(-1);
		}
	}
	static class Receiver extends Thread {//Thread which receives  data from client
	    Receiver() {//constructor
		    super("Receiver Thread");//initialising thread name
			start();//start the thread
		}
		public void run() {//thread operations
		    try {
			    inBuffer = new BufferedReader(new InputStreamReader(client.getInputStream()));//initialise buffer with client's input stream
				while(!(clientOutput = inBuffer.readLine()).equals("stop")) {
				    System.out.println("\nClient: "+clientOutput+"\nme: ");
				}
			    stopServer();
				System.out.println("Client disconnected");
			} catch(Exception e) {
		        System.out.println("Could not initialise client!");
			    System.exit(-1);
			}
		}
    }
}