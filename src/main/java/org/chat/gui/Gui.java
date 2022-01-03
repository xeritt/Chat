package org.chat.gui;

import javax.swing.*;
import java.awt.*;
import dorkbox.systemTray.SystemTray;

/**
 * dorkbox.systemTray.SystemTray
 * https://github.undefined.moe/dorkbox/SystemTray/tree/master/src9
 * https://github.com/dorkbox/SystemTray
 */

public class Gui {
    public final String DISCONECTED = "Disconected";
    public final String CONNECTED = "Connected";
    public final String LOGO_OFFLINE = "/logo_offline.png";
    public final String LOGO_ONLINE = "/logo_online.png";
    private final SystemTray tray;

    private CommonChat commonChat = new CommonChat();

    public CommonChat getCommonChat() {
        return commonChat;
    }

    public void setStatus(String status) {
      //  this.status = status;
        this.tray.setStatus(status);
    }

    public Gui() {
        System.out.println("Gui const");
        tray = SystemTray.get();
        tray.installShutdownHook();
        setLogo(LOGO_OFFLINE);
        //tray.setTooltip(FREE_CHAT);
        tray.setStatus(DISCONECTED);
        setMenu();
    }

    public void setLogo(String logo) {
        Image image = Toolkit.getDefaultToolkit()
               .createImage(getClass().getResource(logo));
        tray.setImage(image);
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
