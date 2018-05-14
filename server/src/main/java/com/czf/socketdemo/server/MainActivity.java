package com.czf.socketdemo.server;

import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.TextView;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {

    private TextView tv;
    private Handler bgHander;
    private ServerSocket server;
    private BufferedWriter bw;
    private float preX, preY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.czf.socketdemo.server.R.layout.activity_main);

        tv = (TextView) findViewById(com.czf.socketdemo.server.R.id.tv_server);

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
                        bw = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
                        startBgQueue();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void sendOut(final float x, final float y) {
        bgHander.post(new Runnable() {
            @Override
            public void run() {
                if (bw == null) {return;}
                try {
                    String str = x + "_" + y;
                    bw.write(str);
                    bw.newLine();
                    bw.flush();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void startBgQueue() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                bgHander = new Handler();
                Looper.loop();
            }
        }).start();
    }

    private boolean needUpdate(float newX, float newY) {
        if (preX == -1) {return true;}
        float deltaX = (newX-preX)*(newX-preX);
        float deltaY = (newY-preY)*(newY-preY);
        return (deltaX + deltaY) >= 64; // 8 pixels
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (bgHander == null) {
            return true;
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                sendOut(event.getX(), event.getY());
                break;
            case MotionEvent.ACTION_MOVE:
                float newX = event.getX();
                float newY = event.getY();
                if (needUpdate(newX ,newY)) {
                    preX = newX;
                    preY = newY;
                    sendOut(newX, newY);
                }
                break;
            case MotionEvent.ACTION_UP:
                preX = -1;
                break;
        }
        return true;
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
