package org.chat.gui;

import javax.swing.*;
import java.awt.*;
import dorkbox.systemTray.SystemTray;

/**
 * dorkbox.systemTray.SystemTray
 * https://github.undefined.moe/dorkbox/SystemTray/tree/master/src9
 */

public class Gui {
    public  final String LOGO_PNG = "/logo2.png";
    private final SystemTray tray;
    private CommonChat commonChat = new CommonChat();

    public CommonChat getCommonChat() {
        return commonChat;
    }

    public Gui() {
        System.out.println("Gui const");
        tray = SystemTray.get();
        tray.installShutdownHook();
        Image image = Toolkit.getDefaultToolkit()
               .createImage(getClass().getResource(LOGO_PNG));
        tray.setImage(image);
        setMenu();
    }

    public void setMenu(){
        JMenu menu = new JMenu("Main menu");

        JMenuItem about = new JMenuItem("About");
        about.addActionListener(e->{System.exit(0);});
        menu.add(about);

        JMenuItem form = new JMenuItem("Chat");
        form.addActionListener(e->{
            //CommonChat dialog = new CommonChat();
            commonChat.setVisible(true);
        });
        menu.add(form);

        JMenuItem exit = new JMenuItem("Exit");
        exit.addActionListener(e->{System.exit(0);});
        menu.add(exit);

        tray.setMenu(menu);
    }
}
