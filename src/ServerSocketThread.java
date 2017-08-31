import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by likole on 8/31/17.
 */
public class ServerSocketThread extends Thread {

    private Socket socket;
    private PrintWriter printWriter = null;
    private Server server;

    public ServerSocketThread(Socket socket, Server server) {
        this.socket = socket;
        this.server = server;
    }

    @Override
    public void run() {
        try {
            printWriter = new PrintWriter(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String msg;
            while ((msg = bufferedReader.readLine()) != null) {
                System.out.println("received from " + socket.getInetAddress() + " : " + msg);
                server.sendMessage(msg, socket);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        if (printWriter != null) printWriter.close();
        if (socket != null)
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    public void sendMessage(String s) {
        System.out.println("send to " + socket.getInetAddress() + " : " + s);
        printWriter.println(s);
        printWriter.flush();
    }
}
