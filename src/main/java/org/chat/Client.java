package org.chat;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client implements Log{
    private int ServerPort = 1234;
    private Thread sendMessage;
    private Thread readMessage;
    private InetAddress ip = null;


    public static void main(String args[]) throws IOException {
        Client client = new Client();
        client.start(args);
    }

    private void start(String[] args) {
        // = null;
        try {
            if (args.length > 0) {
                ServerPort = Integer.valueOf(args[0]);
            }
            if (args.length > 1) {
                ip = InetAddress.getByName(args[1]);
            } else
                ip = InetAddress.getByName("localhost");
                Socket clientSocket = new Socket(ip, ServerPort);
                ClientReadData readData = new ClientReadData(clientSocket);
                ClientWriteData writeData = new ClientWriteData(clientSocket);
                sendMessage = new Thread(writeData);
                readMessage = new Thread(readData);
                sendMessage.start();
                readMessage.start();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
