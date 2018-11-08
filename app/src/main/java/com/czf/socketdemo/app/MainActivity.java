package com.czf.socketdemo.app;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {

    private Handler uiHandler;
    private final String serverIP = "10.0.2.2";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        uiHandler = new Handler();

        TextView socketTv1 = findViewById(R.id.socket1);
        TextView socketTv2 = findViewById(R.id.socket2);
        TextView socketTv3 = findViewById(R.id.socket3);

        crateSocket(socketTv1, 55555);
        crateSocket(socketTv2, 55556);
        crateSocket(socketTv3, 55557);
    }

    /**
     * On the client-side, it is common practice for new outbound connections to use a random
     * client-side port, in which case it is possible to run out of available ports if you make
     * a lot of connections in a short amount of time.
     */
    private void crateSocket(final TextView socketTv, final int localPort) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Socket socket = new Socket(InetAddress.getByName(serverIP), 7777, // dest
                                               InetAddress.getByName(serverIP), localPort); // local
                    BufferedReader br =
                            new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    while (true) {
                        final String recvStr = br.readLine(); // readLine might block
                        uiHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                socketTv.setText(recvStr);
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

}
