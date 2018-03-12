package com.github.ackintosh.walker;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.HashMap;

public class Main {
    public static void main(String[] args) {
        HashMap<String, String> values = new HashMap<String, String>();

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
                byte[] data = new byte[1024];
                while ((receivedByte = from.read(data)) != -1) {
                    String receivedString = new String(data, Charset.forName("UTF-8"));
                    if (receivedString.startsWith("set ")) {
                        String[] parts = receivedString.split("[\\s]+", 3);
                        String key = parts[1];
                        String value = parts[2].substring(0, parts[2].indexOf("\r\n"));
                        values.put(key, value);
                    } else if(receivedString.startsWith("get ")) {
                        String[] parts = receivedString.split("[\\s]+", 2);
                        String key = parts[1].substring(0, parts[1].indexOf("\r\n"));
                        to.write((values.get(key) + "\r\n").getBytes());
                    } else {
                        // echo
                        to.write(data);
                    }
                    // cleanup
                    data = new byte[1024];
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
