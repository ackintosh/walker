package com.github.ackintosh.walker;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    public static void main(String[] args) {
        try {
            ServerSocket listener = new ServerSocket();
            listener.setReuseAddress(true);
            listener.bind(new InetSocketAddress(11211));
            System.out.println("Walker is listening on port 11211...");

            while (true) {
                Socket socket = listener.accept();
                InputStream from = socket.getInputStream();
                OutputStream to = socket.getOutputStream();
                int receivedByte;
                while ((receivedByte = from.read()) != -1) {
                    to.write(receivedByte);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
