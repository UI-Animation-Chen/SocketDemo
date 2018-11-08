package com.czf.socketdemo.app;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {

    private TextView socketTv1;
    private TextView socketTv2;
    private TextView socketTv3;
    private Handler uiHander;
    private final String serverIP = "10.200.0.60";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        uiHander = new Handler();

        socketTv1 = findViewById(R.id.socket1);
        socketTv2 = findViewById(R.id.socket2);
        socketTv3 = findViewById(R.id.socket3);

        crateSocket(socketTv1);
        crateSocket(socketTv2);
        crateSocket(socketTv3);
    }

    private void crateSocket(final TextView socketTv) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Socket socket = new Socket(InetAddress.getByName(serverIP), 7777);
                    BufferedReader br =
                            new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    while (true) {
                        final String recvStr = br.readLine(); // readLine might block
                        uiHander.post(new Runnable() {
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
