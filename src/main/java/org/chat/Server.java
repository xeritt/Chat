package org.chat;

import java.io.*;
import java.util.*;
import java.net.*;

// Server class
public class Server {
    // Vector to store active clients
    private Map<String, ClientHandler> clientHandlers = new HashMap<>();
    // counter for clients
    static int i = 0;
    private int port = 1234;

    public Map<String, ClientHandler> getClientHandlers() {
        return clientHandlers;
    }

    public void start(String[] args) throws IOException {
        // server is listening on port 1234
        if (args.length > 0) {
            port  = Integer.valueOf(args[0]);
        }
        ServerSocket ss = new ServerSocket(port);
        // running infinite loop for getting
        // client request
        while (true) {
            // Accept the incoming request
            Socket s = ss.accept();
            System.out.println("New client request received : " + s);
            System.out.println("Creating a new handler for this client...");
            // Create a new handler object for handling this request.
            //ClientHandler clientHandler = new ClientHandler(s, "client " + i, dis, dos);
            ClientHandler clientHandler = new ClientHandler(this, s, "client" + i);
            // Create a new Thread with this object.
            Thread client = new Thread(clientHandler);
            System.out.println("Adding " + clientHandler.getName() + " client to active client list");
            // add this client to active clients list
            clientHandlers.put(clientHandler.getName(), clientHandler);
            // start the thread.
            client.start();
            // increment i for new client.
            // i is used for naming only, and can be replaced
            // by any naming scheme
            i++;

        }

    }

    public static void main(String[] args) throws IOException {
        Server server = new Server();
        server.start(args);
    }
}

