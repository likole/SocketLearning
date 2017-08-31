import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by likole on 8/31/17.
 */
public class client {

    void clientTest() throws IOException, InterruptedException {
        Socket socket = new Socket("127.0.0.1", 11298);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        new Thread(new Runnable() {
            @Override
            public void run() {
                String msg = null;
                try {
                    while ((msg = bufferedReader.readLine()) != null) {
                        System.out.println(msg);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        PrintWriter printWriter = new PrintWriter(socket.getOutputStream());
        for (int i = 0; i < 5; i++) {
            printWriter.println(i);
            printWriter.flush();
            Thread.sleep(500);
        }

    }

}
