package org.chat;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;
import java.util.StringTokenizer;

// ClientHandler class
class ClientHandler implements Runnable {
    private Scanner scn = new Scanner(System.in);
    private String name;
    private DataInputStream dis;
    private DataOutputStream dos;
    private Socket s;
    private boolean isloggedin;
    private Server server;

    public String getName() {
        return name;
    }

    public ClientHandler(Socket s, String name) throws IOException {
        // obtain input and output streams
        this.name = name;
        this.s = s;
        this.isloggedin = true;
    }

    public ClientHandler(Server server, Socket s, String name) throws IOException {
        // obtain input and output streams
        this.name = name;
        this.s = s;
        this.server = server;
        this.isloggedin = true;
    }

    @Override
    public void run() {
        try (DataInputStream dis = new DataInputStream(s.getInputStream());
             DataOutputStream dos = new DataOutputStream(s.getOutputStream())) {

            String received;
            while (true) {
                try {
                    // receive the string
                    received = dis.readUTF();

                    System.out.println(received);

                    if (received.equals("logout")) {
                        this.isloggedin = false;
                        this.s.close();
                        break;
                    }

                    // break the string into message and recipient part
                    StringTokenizer st = new StringTokenizer(received, "#");
                    String MsgToSend = st.nextToken();
                    String recipient = st.nextToken();

                    // search for the recipient in the connected devices list.
                    // ar is the vector storing client of active users
                    for (ClientHandler mc : server.getClientHandlers()) {
                        // if the recipient is found, write on its
                        // output stream
                        if (mc.name.equals(recipient) && mc.isloggedin == true) {
                            mc.dos.writeUTF(this.name + " : " + MsgToSend);
                            break;
                        }
                    }
                } catch (IOException e) {

                    e.printStackTrace();
                    isloggedin = false;
                    s.close();
                    System.out.println("Client: " + getName() + " disconected.");
                    break;
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
