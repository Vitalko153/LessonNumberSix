import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server {

    private static Socket clientSocket;
    private static final int PORT = 8189;

    public static void main(String[] args) {
        try{
            ServerSocket server = new ServerSocket(PORT);
            System.out.println("Server started.");
            clientSocket = server.accept();
            System.out.println("Client connected: " + clientSocket.getRemoteSocketAddress());
            DataInputStream inputMsg = new DataInputStream(clientSocket.getInputStream());
            DataOutputStream outMsg = new DataOutputStream(clientSocket.getOutputStream());
            Scanner sc = new Scanner(System.in);

            Thread threadRead = new Thread(() -> {
                try {
                while (true) {
                    outMsg.writeUTF(sc.nextLine());
                }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
            });

            threadRead.setDaemon(true);
            threadRead.start();

            while(true){
                String str = inputMsg.readUTF();
                if(str.equals("/exit")) {
                    System.out.println("Client " + clientSocket.getRemoteSocketAddress() + " disconnected.");
                    outMsg.writeUTF("/exit");
                    break;
                }else{
                    System.out.println("Client: " + str);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
