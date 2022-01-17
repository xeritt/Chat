package org.chat.gui;

import org.chat.security.RSAUtil;

import java.io.IOException;
import java.security.KeyPair;
import java.security.PublicKey;

public class Security {
    private boolean encrypt = false;
    private KeyPair writeKeys;
    private PublicKey readKey;
    public static final String PUBLIC_KEY_INSTALL = "Public key from server install succcess.";
    public static final String PUBLIC_KEY_NOINSTALL = "Error. Don't install encrypt key from.";

    public boolean isEncrypt() {return encrypt;}
    public void setEncrypt(boolean encrypt) {this.encrypt = encrypt;}
    public PublicKey getReadKey() {return readKey;}
    public void setReadKey(PublicKey readKey) {this.readKey = readKey;}
    public KeyPair getWriteKeys() {return writeKeys;}
    private CommonChat chat;

    public Security(CommonChat chat) {
        this.chat = chat;
    }

    public void setEncryptKey(String keyEncode) {
        PublicKey pubKey = RSAUtil.getPublicKey(keyEncode);
        String msg = PUBLIC_KEY_INSTALL;
        if (pubKey!=null) {
            setReadKey(pubKey);
        } else {
            msg = PUBLIC_KEY_NOINSTALL;
        }

        chat.toast("Encrypt keys", msg);
        chat.appendColorText(msg, chat.getUserColor());
    }

    public void sendKeys(){
        try {
            this.writeKeys = RSAUtil.generateKeyPair();
            chat.getDos().writeUTF("/key");
            String publicKey = RSAUtil.convertPublicKeyToString(writeKeys.getPublic());
            chat.getDos().writeUTF(publicKey);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getDecrypt(String msg) throws Exception {
        return RSAUtil.decrypt(msg, getWriteKeys().getPrivate());
    }

    public String getEncrypt(String msg) throws Exception {
        return RSAUtil.encrypt(msg, getReadKey());
    }

}
