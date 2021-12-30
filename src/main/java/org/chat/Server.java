package org.chat;

import java.io.*;
import java.util.*;
import java.net.*;

// Server class
public class Server implements Log{
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
            Socket clientSocket = ss.accept();
            log("New client request received : " + clientSocket);
            log("Creating a new handler for this client...");
            // Create a new handler object for handling this request.
            //ClientHandler clientHandler = new ClientHandler(clientSocket, "client " + i, dis, dos);
            ClientHandler clientHandler = new ClientHandler(this, clientSocket, "user" + i);
            // Create a new Thread with this object.
            Thread client = new Thread(clientHandler);
            log("Adding " + clientHandler.getName() + " client to active client list");
            // add this client to active clients list
            clientHandlers.put(clientHandler.getName(), clientHandler);
            // start the thread.
            client.start();
            i++;

        }

    }

    public static void main(String[] args) throws IOException {
        Server server = new Server();
        server.start(args);
    }

    @Override
    public void log(String str) {
        System.out.println(str);
    }
}

