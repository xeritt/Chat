package org.chat;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class ClientWriteData implements Runnable, Log{
    private Socket s;

    public ClientWriteData(Socket s) {
        this.s = s;
    }

    @Override
    public void run() {

        try (DataOutputStream dos = new DataOutputStream(s.getOutputStream())) {
            Scanner scn = new Scanner(System.in);
            // read the message to deliver.
            while (true) {
                String msg = scn.nextLine();
                try {
                    // write on the output stream
                    dos.writeUTF(msg);
                } catch (IOException e) {
                    e.printStackTrace();
                    log("Error read data");
                    System.exit(1);
                    //break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            log("Error with data stream");
            System.exit(1);
        }

    }

}
