package ro.pub.cs.systems.eim.practicaltest02.practicaltest02;

import android.util.Log;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;


/**
 * Created by student on 5/20/16.
 */
public class CommunicationThread extends Thread {

    private ServerThread serverThread;
    private Socket socket;

    public CommunicationThread(ServerThread serverThread, Socket socket) {
        this.serverThread = serverThread;
        this.socket = socket;
    }
    @Override
    public void run() {
        if (socket != null) {
            try {
                BufferedReader bufferedReader = Utilities.getReader(socket);
                PrintWriter printWriter = Utilities.getWriter(socket);
                if (bufferedReader != null && printWriter != null) {
                    Log.i(Constants.TAG, "[COMMUNICATION THREAD] Waiting for parameters from client (city / information type)!");
                    String url = bufferedReader.readLine();
                    if (url != null && !url.isEmpty()) {
                        Log.d("==========", url);
                        if (!url.contains("bad")) {
                            HttpClient httpClient = new DefaultHttpClient();
                            HttpGet httpGet = new HttpGet(url);
                            ResponseHandler<String> responseHandlerGet = new BasicResponseHandler();
                            try {
                                String result = httpClient.execute(httpGet, responseHandlerGet);
                                printWriter.println(result);

                            } catch (ClientProtocolException clientProtocolException) {
                                Log.e(Constants.TAG, clientProtocolException.getMessage());
                                if (Constants.DEBUG) {
                                    clientProtocolException.printStackTrace();
                                }
                            } catch (IOException ioException) {
                                Log.e(Constants.TAG, ioException.getMessage());
                                if (Constants.DEBUG) {
                                    ioException.printStackTrace();
                                }
                            }
                        } else {
                                String result = "";
                                printWriter.println(result);


                        }
                    }
                }
            } catch (IOException e) {

            }
        }
    }
}
