package org.chat;

import org.chat.gui.Gui;
import java.io.*;
import java.net.*;

public class Client implements Log, Runnable {
    public static final int SLEEP = 5000;
    private Socket clientSocket = null;
    //public DataOutputStream dos = null;

    private int ServerPort = 1234;
    private Thread sendMessage;
    private Thread readMessage;
    private InetAddress ip = null;
    private boolean fConnected = false;
    private String[] args = null;
    private Gui gui = new Gui();

    public Client(String[] args) {
        this.args = args;
    }

    public static void main(String args[]) throws IOException {
        Thread client = new Thread(new Client(args));
        client.start();
    }

    public Socket connect(String[] args) throws IOException {
        if (args.length > 0) {
            ServerPort = Integer.valueOf(args[0]);
        }
        if (args.length > 1) {
            ip = InetAddress.getByName(args[1]);
        } else {
            ip = InetAddress.getByName("localhost");
        }
        Socket clientSocket = new Socket(ip, ServerPort);
        fConnected = true;
        log("Connected to ip="+ ip + " port="+ ServerPort);
        return clientSocket;
    }

    @Override
    public void run() {
        ClientReadData readData = null;
        ClientWriteData writeData = null;

        while (true) {
            try {

                if (fConnected == false) {
                    log("Try to connect...");
                    clientSocket = connect(args);
                    DataOutputStream dos = new DataOutputStream(clientSocket.getOutputStream());
                    gui.getCommonChat().setDos(dos);
                    readData = new ClientReadData(clientSocket, gui);
                    //writeData = new ClientWriteData(clientSocket, gui);
                    //sendMessage = new Thread(writeData);
                    readMessage = new Thread(readData);
                    //sendMessage.start();
                    readMessage.start();
                }

                Thread.sleep(SLEEP);

                if (readData.fRun == false/* || writeData.fRun == false*/) {
                    readData.fRun = false;
                    //writeData.fRun = false;
                    readData.stop();
                    //writeData.stop();
                    fConnected = false;
                    gui.getCommonChat().getDos().close();
                    log("Disconnected.");
                }

            } catch (IOException e) {
                e.printStackTrace();
                try {
                    Thread.sleep(SLEEP);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
