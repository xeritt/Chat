package org.chat;

import org.chat.gui.Security;

import java.awt.*;
import java.io.DataOutputStream;
import java.io.IOException;

public interface IChat extends Log{
    void onSend();
    void onRead(String text);
    DataOutputStream getDos();
    void setDos(DataOutputStream dos);
    void setStatusConnected();
    void setStatusDisconnected() throws IOException;
    Security getSecurity();
    //getChatText
}
