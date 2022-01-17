package org.chat.gui;

import javax.swing.*;
import java.awt.*;
import dorkbox.systemTray.SystemTray;
import org.drjekyll.fontchooser.FontDialog;

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
        this.tray.setStatus(status);
    }
    final private FontDialog fontDialog;

    public Gui() {
        System.out.println("Gui const");
        tray = SystemTray.get();
        tray.installShutdownHook();
        setLogo(LOGO_OFFLINE);
        tray.setStatus(DISCONECTED);
        setMenu();
        fontDialog = new FontDialog((Frame) null, "Select Font", true);
        setOnCenter(fontDialog, 4);
    }

    public void setLogo(String logo) {
        Image image = Toolkit.getDefaultToolkit()
               .createImage(getClass().getResource(logo));
        tray.setImage(image);
    }

    public void setMenu(){
        JMenu menu = new JMenu("Main menu");
        addPropMenu(menu);

        JMenuItem about = new JMenuItem("About");
        about.addActionListener(e->{
            //showNotification("This is free chat. ");
            Toast.showToast("Information","This is free chat.", 5000);
        });
        menu.add(about);

        JMenuItem form = new JMenuItem("Chat");
        form.addActionListener(e->{
                commonChat.setVisible(true);
        });
        menu.add(form);

        JMenuItem exit = new JMenuItem("Exit");
        exit.addActionListener(e->{System.exit(0);});
        menu.add(exit);

        tray.setMenu(menu);
    }

    static public void setOnCenter(Component component, int kof) {
        final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        final int width = screenSize.width / kof;
        final int height = screenSize.height / kof;
        component.setSize(width, height);
        component.setLocation(width - component.getWidth() / kof, height - component.getHeight() / kof);
    }

    public void showFontDialog(Component component) {
        //FontDialog dialog = new FontDialog((Frame) null, "Select Font", true);
        fontDialog.setDefaultCloseOperation(2);
        fontDialog.setSelectedFont(component.getFont());
        fontDialog.setVisible(true);
    }

    public void setFont(Component component){
        if (!fontDialog.isCancelSelected()) {
            component.setFont(fontDialog.getSelectedFont());
        }
    }

    private void addPropMenu(JMenu menu) {
        JMenu propMenu = new JMenu("Properties");
        menu.add(propMenu);

        JMenuItem prop = new JMenuItem("Set Font");
        prop.addActionListener(e->{
            showFontDialog(commonChat.getChatText());
            setFont(commonChat.getChatText());
            //FontDialog.showDialog(commonChat.getChatText());
        });
        propMenu.add(prop);

        JMenuItem color = new JMenuItem("Set User Color");
        color.addActionListener(e->{
            Color newColor = JColorChooser.showDialog(
                    commonChat,
                    "Choose User Color",
                    commonChat.getUserColor()
                    );
            if (newColor != null) {
                commonChat.setUserColor(newColor);
            }
        });
        propMenu.add(color);

        JMenuItem textColor = new JMenuItem("Set Text Color");
        textColor.addActionListener(e->{
            Color newColor = JColorChooser.showDialog(
                    commonChat,
                    "Choose Color",
                    commonChat.getTextColor()
            );
            if (newColor != null) {
                commonChat.setTextColor(newColor);
            }
        });
        propMenu.add(textColor);

        JMenuItem genKey = new JMenuItem("Set Encrypt");
        genKey.addActionListener(e->{
            commonChat.getSecurity().setEncrypt(true);
            commonChat.getSecurity().sendKeys();
        });
        propMenu.add(genKey);
    }
}
