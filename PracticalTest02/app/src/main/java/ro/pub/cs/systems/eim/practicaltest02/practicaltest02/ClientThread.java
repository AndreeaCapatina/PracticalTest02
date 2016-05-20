package ro.pub.cs.systems.eim.practicaltest02.practicaltest02;

import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import ro.pub.cs.systems.eim.practicaltest02.practicaltest02.Constants;
import ro.pub.cs.systems.eim.practicaltest02.practicaltest02.Utilities;

public class ClientThread extends Thread {

    private String address;
    private int port;
    private String url;
    private TextView urlTextView;

    private Socket socket;

    public ClientThread(
            String address,
            int port,
            String url,
            TextView urlTextView
            ) {
        this.address = address;
        this.port = port;
        this.url = url;
        this.urlTextView = urlTextView;
    }

    @Override
    public void run() {
        try {
            socket = new Socket(address, port);
            if (socket == null) {
                Log.e(Constants.TAG, "[CLIENT THREAD] Could not create socket!");
            }

            BufferedReader bufferedReader = Utilities.getReader(socket);
            PrintWriter printWriter = Utilities.getWriter(socket);
            if (bufferedReader != null && printWriter != null) {
                printWriter.println(url);
                printWriter.flush();

                String contentUrl;
                while ((contentUrl = bufferedReader.readLine()) != null) {
                    final String content = contentUrl;
                    urlTextView.post(new Runnable() {
                        @Override
                        public void run() {
                            urlTextView.append(content + "\n");
                        }
                    });
                }
            } else {
                Log.e(Constants.TAG, "[CLIENT THREAD] BufferedReader / PrintWriter are null!");
            }
            socket.close();
        } catch (IOException ioException) {
            Log.e(Constants.TAG, "[CLIENT THREAD] An exception has occurred: " + ioException.getMessage());
            if (Constants.DEBUG) {
                ioException.printStackTrace();
            }
        }
    }

}
