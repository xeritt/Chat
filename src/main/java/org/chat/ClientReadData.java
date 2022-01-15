package org.chat;
import org.chat.gui.Gui;
import org.chat.gui.Toast;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientReadData implements Runnable, Log{

    private Socket s;
    public boolean fRun = true;
    private Gui gui;

    public ClientReadData(Socket s, Gui gui) {
        this.s = s;
        this.gui = gui;
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
                //gui.getCommonChat().appendChatText(msg);
                gui.getCommonChat().appendColorText(msg, gui.getCommonChat().getTextColor());
                //gui.getCommonChat().appendTimeText(msg);
                if (gui.getCommonChat().isVisible()==false) {
                    Toast.showToast("Message", msg, 5000);
                }
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
