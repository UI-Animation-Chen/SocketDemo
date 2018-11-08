package com.czf.socketdemo.server;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.TextView;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.czf.socketdemo.server.R.layout.activity_main);

        startServer();
    }

    private void startServer() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ServerSocket serverSocket = new ServerSocket(7777);
                    for(;;) {
                        Log.d("---------", "server is ready");
                        Socket clientSocket = serverSocket.accept();
                        Log.d("---------", "a client is coming");
                        startTCPConn(clientSocket);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void startTCPConn(Socket clientSocket) {
        try {
            final String sokectName = clientSocket.toString();
            final BufferedWriter bw =
                    new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        SystemClock.sleep(1000);
                        try {
                            bw.write(sokectName + new Random().nextInt());
                            bw.newLine();
                            bw.flush();
                        } catch(IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
