package org.chat;

import org.chat.gui.Gui;
import java.io.*;
import java.net.*;

public class Client implements Log, Runnable {
    public static final int SLEEP = 5000;
    private Socket clientSocket = null;
    private int ServerPort = 1234;
    private Thread readMessage;
    private InetAddress ip = null;
    private boolean fConnected = false;
    private String[] args = null;
    private Gui gui = new Gui();

    public Client(String[] args) {
        this.args = args;
    }

    public static void main(String args[]) throws IOException {
        Thread clientThread = new Thread(new Client(args));
        clientThread.start();
    }

    public Socket connect(String[] args) throws IOException {
        if (args.length > 0) {
            ServerPort = Integer.valueOf(args[0]);
        }
        if (args.length > 1) {
            ip = InetAddress.getByName(args[1]);
        } else {
            ip = InetAddress.getByName("82.202.172.104");
        }
        Socket clientSocket = new Socket(ip, ServerPort);
        fConnected = true;
        log("Connected to ip="+ ip + " port="+ ServerPort);
        return clientSocket;
    }

    @Override
    public void run() {
        ClientReadData readData = null;
        while (true) {
            try {
                if (fConnected == false) {
                    log("Try to connect...");
                    clientSocket = connect(args);
                    DataOutputStream dos = new DataOutputStream(clientSocket.getOutputStream());
                    gui.getCommonChat().setDos(dos);
                    readData = new ClientReadData(clientSocket, gui);
                    readMessage = new Thread(readData);
                    readMessage.start();
                    gui.setStatus(gui.CONNECTED);
                    gui.setLogo(gui.LOGO_ONLINE);
                }

                Thread.sleep(SLEEP);

                if (readData.fRun == false) {
                    fConnected = false;
                    gui.getCommonChat().getDos().close();
                    gui.setStatus(gui.DISCONECTED);
                    gui.setLogo(gui.LOGO_OFFLINE);
                    log("Disconnected.");
                }

            } catch (IOException e) {

                e.printStackTrace();
                log("Error with connection.");
                try {
                    Thread.sleep(SLEEP);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                    log("Sleep interrapted");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                log("Sleep interrapted");
            }
        }
    }
}
