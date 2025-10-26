package org.chat.gui;

import org.chat.IChat;
import org.chat.Log;
import org.chat.security.RSAUtil;

import java.io.IOException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;

public class Security implements Log {
    private boolean encrypt = false;
    private KeyPair writeKeys;
    private PublicKey readKey;
    public static final String PUBLIC_KEY_INSTALL = "Public key from server install succcess.";
    public static final String PUBLIC_KEY_NOINSTALL = "Error. Don't install encrypt key from.";
    public static final String SECURITY_CHAT = "---==== Security Chat ====---";

    public boolean isEncrypt() {return encrypt;}
    public void setEncrypt(boolean encrypt) {this.encrypt = encrypt;}
    public PublicKey getReadKey() {return readKey;}
    public void setReadKey(PublicKey readKey) {this.readKey = readKey;}
    public KeyPair getWriteKeys() {return writeKeys;}

    public String setEncryptKey(String keyEncode) {
        PublicKey pubKey = RSAUtil.getPublicKey(keyEncode);
        String msg = PUBLIC_KEY_INSTALL;
        if (pubKey!=null) {
            setReadKey(pubKey);
            msg = SECURITY_CHAT;
        } else {
            msg = PUBLIC_KEY_NOINSTALL;
        }
        return msg;
    }

    public String generateKeys(){
        try {
            this.writeKeys = RSAUtil.generateKeyPair();
            String publicKey = RSAUtil.convertPublicKeyToString(writeKeys.getPublic());
            return publicKey;
        }
        catch (NoSuchAlgorithmException e) {
            log("Error RSA NoSuchAlgorithmException");
            setEncrypt(false);
            throw new RuntimeException(e);
        } catch (Exception e) {
            log("Error RSA keys");
            setEncrypt(false);
            throw new RuntimeException(e);
        }
    }

    public String getDecrypt(String msg) throws Exception {
        return RSAUtil.decrypt(msg, getWriteKeys().getPrivate());
    }

    public String getEncrypt(String msg) throws Exception {
        return RSAUtil.encrypt(msg, getReadKey());
    }
}
