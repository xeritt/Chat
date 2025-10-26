package org.chat;

import javax.swing.*;
import java.awt.*;

public interface IGuiChat extends IChat{
    public JTextPane getChatText();
    public Color getTextColor();
    public Color getUserColor();
}
