package org.chat;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientReadData implements Runnable, Log{

    private Socket s;

    public ClientReadData(Socket s) {
        this.s = s;
    }

    @Override
    public void run() {
        try (DataInputStream dis = new DataInputStream(s.getInputStream())) {
            // read the message sent to this client
            while (true) {
                String msg = dis.readUTF();
                log(msg);
            }
        } catch (IOException e) {
            e.printStackTrace();
            log("Error write data");
            System.exit(1);
        }

    }

}
