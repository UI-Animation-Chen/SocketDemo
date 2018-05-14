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

    private SketchView sketchView;
    private String line;
    private Handler uiHander;
    private final String serverIP = "192.168.89.108";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sketchView = (SketchView) findViewById(R.id.sketch_view);
        uiHander = new Handler();

        crateSocket();
    }

    private void crateSocket() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Socket socket = new Socket(InetAddress.getByName(serverIP), 7777);
                    BufferedReader br =
                            new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    while (!(line = br.readLine()).equals("bye")) { // readLine might block
                        uiHander.post(new Runnable() {
                            @Override
                            public void run() {
                                String[] strs = line.split("_");
                                sketchView.moveToPoint(Float.parseFloat(strs[0]),
                                                       Float.parseFloat(strs[1]));
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
