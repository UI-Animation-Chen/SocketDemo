package com.czf.socketdemo.server;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {

    private TextView tv;
    private Handler uiHander;
    private ServerSocket server;

    private int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.czf.socketdemo.server.R.layout.activity_main);

        tv = (TextView) findViewById(com.czf.socketdemo.server.R.id.tv_server);
        uiHander = new Handler();

        startServer();
    }

    private void startServer() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    server = new ServerSocket(7777);
                    for(;;) {
                        Socket s = server.accept();
                        uiHander.post(new Runnable() {
                            @Override
                            public void run() {

                            }
                        });
                        BufferedWriter bw =
                                new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
                        for (;;) {
                            String str = count++ + "";
                            bw.write(str);
                            bw.newLine();
                            bw.flush();
                            Thread.sleep(2000);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (server != null) {
            try {
                server.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
