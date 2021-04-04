import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private static final String SERVER_ADDRESS = "localhost";
    private static Socket socket;
    private static final int SERVER_PORT = 8189;

    public static void main(String[] args) {

            try{socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
                System.out.println("Client connect to server." + socket.getRemoteSocketAddress());
                DataInputStream inputMsg = new DataInputStream(socket.getInputStream());
                DataOutputStream outMsg = new DataOutputStream(socket.getOutputStream());
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
                        System.out.println("Client " + socket.getRemoteSocketAddress() + " disconnected.");
                        outMsg.writeUTF("/exit");
                        break;
                    }else{
                        System.out.println("Server: " + str);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
