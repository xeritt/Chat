package org.chat;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.StringTokenizer;

// ClientHandler class
class ClientHandler implements Runnable, Log {
    private Scanner scn = new Scanner(System.in);
    private String name;
    private DataInputStream dis;
    private DataOutputStream dos;
    private Socket socket;
    private boolean isloggedin;
    private Server server;

    public String getName() {
        return name;
    }

    public ClientHandler(Socket socket, String name) throws IOException {
        // obtain input and output streams
        this.name = name;
        this.socket = socket;
        this.isloggedin = true;
    }

    public ClientHandler(Server server, Socket socket, String name) throws IOException {
        // obtain input and output streams
        this.name = name;
        this.socket = socket;
        this.server = server;
        this.isloggedin = true;
    }

    @Override
    public void run() {
        try (DataInputStream dis = new DataInputStream(socket.getInputStream());
             DataOutputStream dos = new DataOutputStream(socket.getOutputStream())) {
            this.dos = dos;
            this.dis = dis;
            String received;
            while (true) {
                try {
                    // receive the string
                    received = dis.readUTF();
                    log(received);
                    if (logoutCommand(received)) break;
                    // break the string into message and recipient part
                    StringTokenizer st = new StringTokenizer(received, "#");
                    String MsgToSend = st.nextToken();
                    String recipient = st.nextToken();

                    if (setNameCommand(MsgToSend, recipient)) continue;

                    // search for the recipient in the connected devices list.
                    // ar is the vector storing client of active users
                    ClientHandler clientHandler = server.getClientHandlers().get(recipient);
                    if (clientHandler == null) continue;
                    if (clientHandler.isloggedin == false) continue;

                    clientHandler.dos.writeUTF(this.name + " : " + MsgToSend);

               } catch (NoSuchElementException e){
                    log("Error input!");
                } catch (IOException e) {
                    e.printStackTrace();
                    isloggedin = false;
                    socket.close();
                    server.getClientHandlers().remove(this);
                    log("Client: " + getName() + " disconected.");
                    break;
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private boolean setNameCommand(String MsgToSend, String recipient) {
        if (MsgToSend.equals("/setname")) {
            String oldName = name;
            name = recipient;
            Map<String, ClientHandler> clients = server.getClientHandlers();
            synchronized(clients){
                clients.remove(this);
                clients.put(name, this);
            }
            log("Client " + oldName + " change name to " + getName());
            return true;
        }
        return false;
    }

    private boolean logoutCommand(String received) throws IOException {
        if (received.equals("logout")) {
            isloggedin = false;
            socket.close();
            return true;
        }
        return false;
    }
}
