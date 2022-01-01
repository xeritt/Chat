package org.chat.gui;

import java.awt.*;

public class Gui {
    private final TrayIcon trayIcon;

    public Gui() {
        System.out.println("Gui const");
        try {
            SystemTray tray = SystemTray.getSystemTray();
            Image image = Toolkit.getDefaultToolkit()
                    .createImage(getClass().getResource("/logo.png"));
            //Menu maniMenu = new Menu("Меню");

            trayIcon = new TrayIcon(image, "Free World");
            trayIcon.setImageAutoSize(true);
            trayIcon.setToolTip("Free World");
            tray.add(trayIcon);
            setMenu();
        } catch (AWTException e) {
            throw new RuntimeException(e);
        }
    }

    public void setMenu(){
        PopupMenu popup = new PopupMenu();

        MenuItem about = new MenuItem("About");
        about.addActionListener(e->{System.exit(0);});
        //popup.add(about);

        Menu menu = new Menu("Main menu");
        menu.add(about);
        popup.add(menu);

        MenuItem form = new MenuItem("Chat");
        form.addActionListener(e->{
            CommonChat dialog = new CommonChat();
            dialog.setVisible(true);
        });
        popup.add(form);

        MenuItem exit = new MenuItem("Exit");
        exit.addActionListener(e->{System.exit(0);});
        popup.add(exit);
        trayIcon.setPopupMenu(popup);
    }
}
