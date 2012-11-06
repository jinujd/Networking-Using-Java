/*Server.java*/
import java.io.*; //importing io classes
import java.net.*; //importing networking classes
class Server {
    private static Client clients[]; //array storing clients
    private static int noOfClients = 0; //no of  clients connected
    private static ServerSocket server; //server socket
    private static Socket client; //client socket
    private static boolean stopped = false; //variable to indicate server stopped or not
    private static Sender sender;
    private static PrintWriter outStream;
    private static BufferedReader buffer;
    private static String keyboardInput;
    public static void main(String argc[]) {
        try {
            server = new ServerSocket(2000);
            sender = new Sender();
            while (!stopped) {
                client = server.accept();
                addClient(client);
                if (!stopped) { //output this for all clients except that used to wakeup server
                    System.out.println("New client connected...\nClients connected - " + noOfClients);
                } else {
                    client.close(); //close the client socket created to wake up the server
                    server.close();
                    System.exit(0);
                }
            }
        } catch (Exception e) {
            System.out.println("Could not create server..." + e);
            System.exit(-1);
        }
    }
    static class Sender extends Thread { //Thread which sends data to clients
        Sender() {
            super("Sender Thread");
            try {
                buffer = new BufferedReader(new InputStreamReader(System. in ));
                System.out.println("Starting broadcast...");
                start();
            } catch (Exception e) {
                System.out.println("Could not start sender thread");
                System.exit(-1);
            }
        }
        public void run() {
            if (!stopped) {
                try {
                    int i = 0;
                    do {
                        keyboardInput = buffer.readLine();
                        while (i < noOfClients) { //sending data to all clients
                            System.out.println("Sending to client " + (i + 1));
                            clients[i++].print(keyboardInput);
                        }
                        i = 0;
                        System.out.println("Packets containing \"" + keyboardInput + "\" sent.Waiting for next broadcast...");
                    } while (!keyboardInput.equals("stop"));
                    stopped = true;
                    i = 0;
                    while (i < noOfClients) {
                        clients[i++].close();
                    }
                    noOfClients = 0;
                    clients = new Client[0];
                    System.out.println("All clients disconnected..");
                    client = new Socket("localhost", 2000); //just a way to wake up other thread from waiting
                } catch (Exception e) {
                    System.out.println("Could not send");
                    System.exit(-1);
                }
            }
        }
    }




    static void addClient(Socket client) { //adds new client
        try {
            Client clientsTmp[] = new Client[++noOfClients];
            int i = 0;
            while (i < noOfClients - 1) {
                clientsTmp[i] = clients[i];
                i++;
            }
            clientsTmp[noOfClients - 1] = new Client(client);
            i = 0;
            clients = new Client[noOfClients];
            if (noOfClients > 1) {
                while (i < noOfClients) {
                    clients[i] = new Client(clientsTmp[i].client);
                    i++;
                }
            } else {
                clients = clientsTmp;
            }
        } catch (Exception e) {
            System.out.println("Some problem occured " + e);
        }
    }
    static class Client { //Class storing details of client
        Socket client; //client socket
        PrintWriter outputStream; //client's output stream
        Client(Socket clientSocket) { //constructor
            client = clientSocket;
            try {
                outputStream = new PrintWriter(client.getOutputStream()); //initialising output stream
            } catch (Exception e) {
                System.out.println("Could not initialise client output stream");
            }
        }
        void print(String str) { //printing to clients output stream
            try {
                outputStream.println(str);
                outputStream.flush();
                System.out.println("Sent");
            } catch (Exception e) {
                System.out.println("Could not send to client" + e);
            }

        }
        void close() { //closing client
            try {
                client.close();
                outputStream.close();
            } catch (Exception e) {
                System.out.println("Could not close client");
            }
        }
    }
}