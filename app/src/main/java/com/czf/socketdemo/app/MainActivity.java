package com.czf.socketdemo.app;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {

    private TextView tv;
    private String line;
    private Handler uiHander;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv = (TextView) findViewById(com.czf.socketdemo.app.R.id.tv_app);
        uiHander = new Handler();

        crateSocket();
    }

    private void crateSocket() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Socket socket = new Socket(InetAddress.getLocalHost(), 7777);
                    BufferedReader br =
                            new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    while (!(line = br.readLine()).equals("bye")) { // readLine might block
                        uiHander.post(new Runnable() {
                            @Override
                            public void run() {
                                tv.setText(line);
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
