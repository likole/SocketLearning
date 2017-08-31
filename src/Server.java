import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

public class Server extends Thread {

    public static Map<Socket, ServerSocketThread> serverSocketThreadMap = new HashMap<>();
    public static List<Socket> socketList = new CopyOnWriteArrayList<>();

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(11298);

        //clean client
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    for (Socket socket : socketList) {
                        try {
                            socket.sendUrgentData(0);
                        } catch (IOException e) {
                            socketList.remove(socket);
                            System.out.println("["+socketList.size()+"] "+socket.getInetAddress()+" disconnected!");
                        }
                    }
                    try {
                        Thread.sleep(30000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

        while (true) {
            Socket socket = serverSocket.accept();
            ServerSocketThread serverSocketThread = new ServerSocketThread(socket, new Server());
            socketList.add(socket);
            serverSocketThreadMap.put(socket, serverSocketThread);
            System.out.println("["+socketList.size()+"] "+socket.getInetAddress()+" connected!");
            serverSocketThread.start();
        }
    }

    public void sendMessage(String s, Socket so) {
        for (Socket socket : socketList) {
            try {
                if (so != socket)
                    serverSocketThreadMap.get(socket).sendMessage(s);
            } catch (Exception e) {

            }

        }
    }
}