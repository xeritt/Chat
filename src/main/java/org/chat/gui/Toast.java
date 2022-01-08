package org.chat.gui;

import java.awt.*;
import javax.swing.*;

public class Toast extends JWindow implements Runnable{
    private int delay = 5000;
    private String winTitle;
    private String text;

    public Toast(String winTitle, String text, int delay) {
        super();
        this.delay = delay;
        this.winTitle = winTitle;
        this.text = text;
    }

    static public void showToast(String winTitle, String text, int delay) {
        Toast toast = new Toast(winTitle, text, delay);
        Thread threadToast = new Thread(toast);
        threadToast.start();
    }

    public void showMsg(String title, String text, int delay) {
        Toolkit.getDefaultToolkit().beep();
        JOptionPane optionPane = new JOptionPane(text, JOptionPane.INFORMATION_MESSAGE);
        JDialog dialog = optionPane.createDialog(title);
        dialog.setAlwaysOnTop(true);
        dialog.setModal(false);

        final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int x = screenSize.width - dialog.getWidth();
        int y = screenSize.height - dialog.getHeight();
        dialog.setLocation(x, y);
        dialog.setVisible(true);
        try {
            Thread.sleep(delay);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            dialog.setVisible(false);
        }
    }


    @Override
    public void run() {
        showMsg(winTitle, text, delay);
    }
}