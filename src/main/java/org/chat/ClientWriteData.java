package org.chat;
import org.chat.gui.Gui;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class ClientWriteData implements Runnable, Log{
    private Socket s;
    public boolean fRun = true;
    private Scanner scanner;

    public ClientWriteData(Socket s) {
        this.s = s;
    }

    public void stop(){
        fRun = false;
        scanner.close();
    }

    @Override
    public void run() {

        try (DataOutputStream dos = new DataOutputStream(s.getOutputStream())) {
            scanner = new Scanner(System.in);
            while (fRun) {
                // read the message to deliver.
                String msg = scanner.nextLine();
                try {
                    // write on the output stream
                    dos.writeUTF(msg);
                } catch (IOException e) {
                    e.printStackTrace();
                    log("Error write data");
                    fRun = false;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            log("Error with data stream");
            //System.exit(1);
            fRun = false;
        }
        log("Stop write data");
    }

}
