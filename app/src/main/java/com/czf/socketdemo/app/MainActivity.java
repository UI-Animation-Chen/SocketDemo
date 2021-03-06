package com.czf.socketdemo.app;

import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private Handler uiHandler;
//    private final String serverIP = "localhost"; // emulator loopback. 127.0.0.1 or 10.0.2.15.
//    private final String serverIP = "android device ip"; // real android device
//    private final String serverIP = "10.0.2.2"; // development machine
    private final String serverIP = "192.168.8.141";
//    private String serverIP = "10.200.0.45";
    private int serverPort = 12345;

    private boolean exit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        exit = false;

        uiHandler = new Handler();

        TextView socketTv1 = findViewById(R.id.socket1);
        TextView socketTv2 = findViewById(R.id.socket2);
        TextView socketTv3 = findViewById(R.id.socket3);

//        crateSocket(socketTv1, 55555);
//        crateSocket(socketTv2, 55556);
//        crateSocket(socketTv3, 55557);
        sendDatagramPacket(socketTv1);
    }

    private void sendDatagramPacket(final TextView tv) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    DatagramSocket udpSocket = new DatagramSocket();
                    byte[] buf = new byte[1500];
                    buf[0] = 77;
                    for (;;) {
                        final DatagramPacket p =
                          new DatagramPacket(buf, 1, InetAddress.getByName(serverIP), serverPort);
                        udpSocket.send(p);

                        buf[0] = -1;
                        udpSocket.receive(p);
                        uiHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                tv.setText("" + p.getData()[0] + ", len: " + p.getLength());
                            }
                        });
                        SystemClock.sleep(1000);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
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
                    Socket socket = new Socket(InetAddress.getByName(serverIP), serverPort, // dest
                                               null, 0); // local
                    BufferedReader br =
                            new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    BufferedWriter bw =
                            new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                    while (true) {
                        if (exit) {
                            socket.close();
                        }
                        bw.write("" + new Random().nextInt());
                        bw.newLine();
                        bw.flush();
                        final String recvStr = br.readLine(); // readLine might block
                        uiHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                socketTv.setText(recvStr);
                            }
                        });
                        SystemClock.sleep(2000);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        exit = true;
    }
}
