package org.chat;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientReadData implements Runnable, Log{

    private Socket s;
    public boolean fRun = true;

    public ClientReadData(Socket s) {
        this.s = s;
    }

    public void stop() {
        fRun = false;
    }

    @Override
    public void run() {
        try (DataInputStream dis = new DataInputStream(s.getInputStream())) {
            // read the message sent to this client
            while (fRun) {
                String msg = dis.readUTF();
                log(msg);
            }
        } catch (IOException e) {
            e.printStackTrace();
            log("Error read data");
            fRun = false;
            //System.exit(1);
        }
        log("Stop read data");
    }

}
