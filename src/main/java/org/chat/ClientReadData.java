package org.chat;
import org.chat.gui.Security;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientReadData implements Runnable{
    final private Socket socket;
    public boolean fRun = true;
    final private IChat chat;
    public ClientReadData(Socket socket, IChat chat) throws IOException {
        this.socket = socket;
        this.chat = chat;
    }

    public void stop() {
        fRun = false;
    }

    public void sendKeys(String publicKey){
        try {
            chat.getDos().writeUTF("/key");
            chat.getDos().writeUTF(publicKey);
        } catch (IOException e) {
            chat.getSecurity().setEncrypt(false);
            chat.log("Error write keys to client");
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        try (DataInputStream dis = new DataInputStream(socket.getInputStream())) {
            // read the message sent to this client
            Security security = chat.getSecurity();
            String publicKey = security.generateKeys();
            if (publicKey.equals("")) throw new IOException("Error public key");
            sendKeys(publicKey);
            while (fRun) {
                String msg = dis.readUTF();
                System.out.println(msg);
                if (msg.equals("/key")){
                    String keyEncode = dis.readUTF();
                    String result = security.setEncryptKey(keyEncode);
                    chat.onRead(result);
                    security.setEncrypt(true);
                    continue;
                }
                if (security.isEncrypt()){
                        msg = security.getDecrypt(msg);
                }
                chat.onRead(msg);
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
