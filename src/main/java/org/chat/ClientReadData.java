package org.chat;
import org.chat.gui.CommonChat;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientReadData implements Runnable{
    final private Socket s;
    public boolean fRun = true;
    final private CommonChat chat;

    public ClientReadData(Socket s, CommonChat chat) {
        this.s = s;
        this.chat = chat;
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
                if (chat.getSecurity().isEncrypt()){
                    if (msg.equals("/key")){
                        String keyEncode = dis.readUTF();
                        chat.getSecurity().setEncryptKey(keyEncode);
                        continue;
                    } else {
                        msg = chat.getSecurity().getDecrypt(msg);
                    }
                }
                chat.appendColorText(msg, chat.getTextColor());

                if (!chat.isVisible()) {
                    chat.toast("Message", msg);
                }
                chat.log(msg);
            }
        } catch (IOException e) {
            e.printStackTrace();
            chat.log("Error read data");
            fRun = false;
            //System.exit(1);
        } catch (Exception e) {
            e.printStackTrace();
            chat.log("Decrypt error");
        }
        chat.log("Stop read data");
    }
}
