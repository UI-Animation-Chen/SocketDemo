package com.czf.socketdemo.server;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {

    private final int serverPort = 12345;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.czf.socketdemo.server.R.layout.activity_main);

//        startTcpServer();
        startUdpServer();
    }

    private void startUdpServer() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    DatagramSocket udpServer = new DatagramSocket(serverPort);
                    System.out.println("----- udp server is ready");
                    byte[] buf = new byte[1500];
                    for (;;) {
                        DatagramPacket p = new DatagramPacket(buf, buf.length);
                        udpServer.receive(p);
                        System.out.println("reveived a packet");

                        buf[0]++;
                        p = new DatagramPacket(buf, p.getLength(), p.getAddress(), p.getPort());
                        udpServer.send(p);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * A server socket listens on a single port. All established client connections on that server
     * are associated with that same listening port on the server side of the connection.
     * An established connection is uniquely identified by the combination of client-side and
     * server-side IP/Port pairs. Multiple connections on the same server can share the same
     * server-side IP/Port pair as long as they are associated with different client-side
     * IP/Port pairs, and the server would be able to handle as many clients as available system
     * resources allow it to.
     */
    private void startTcpServer() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ServerSocket serverSocket = new ServerSocket(serverPort);
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
            final BufferedReader br =
                    new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            final BufferedWriter bw =
                    new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        try {
                            String recv = br.readLine();
                            if (recv == null) { // closed.
                                return;
                            }
                            bw.write(sokectName + recv);
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
