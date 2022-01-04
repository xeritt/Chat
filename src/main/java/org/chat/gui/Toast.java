package org.chat.gui;

import java.awt.*;
import javax.swing.*;

public class Toast extends JWindow implements Runnable{
    private static int count = 0;
    private int delay = 2000;
    private int widthText = 300;
    private int heightText = 50;
    private String text;
    private int x;
    private int y;

    public Toast(String text, int x, int y) {
        super();
        this.text = text;
        this.x = x;
        this.y = y;
    }

    public void setWidthText(int widthText) {
        this.widthText = widthText;
    }

    public void setHeightText(int heightText) {
        this.heightText = heightText;
    }

    static public void showToast(String text){
        final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        final int x = screenSize.width;
        final int y = /*screenSize.height - */50;
        Toast toast = new Toast(text, x, y);
        toast.setDelay(5000);
        Thread th = new Thread(toast);
        th.start();
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    // function to pop up the toast
    void showtoast() {
        // make the background transparent

        //setBackground(new Color(220, 220, 0, 240));
        //setBackground(Color.black);
        setBackground(new Color(0, 0, 0, 0));
        // create a panel

        JPanel p = new JPanel() {
            public void paintComponent(Graphics g) {
                int wid = g.getFontMetrics().stringWidth(text);
                int hei = g.getFontMetrics().getHeight();

                // draw the boundary of the toast and fill it

                g.setColor(new Color(47, 48, 50, 255));
                g.fillRect(10, 10, wid + 30, hei + 10);

                Color color = new Color(76, 76, 76, 255);
                g.setColor(color);
                g.drawRect(10, 10, wid + 30, hei + 10);

                // set the color of text
                g.setColor(new Color(255, 255, 255, 240));
                g.drawString(text, 25, 27);
                int t = 255;

                // draw the shadow of the toast

                for (int i = 0; i < 4; i++) {
                    t -= 60;

                    g.setColor(new Color(76, 76, 76, t));
                    g.drawRect(10 - i, 10 - i, wid + 30 + i * 2,
                            hei + 10 + i * 2);
                }
            }
        };

        add(p);
       // widthText = this.getGraphics().getFontMetrics().stringWidth(text);
       // heightText = this.getGraphics().getFontMetrics().getHeight();
        setLocation(x, y + count*heightText);
        final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        setSize(widthText + 20, heightText);
        System.out.println(widthText);
        count++;
        if ((y + count*heightText) > screenSize.height){
            count = 0;
        }
        /////////////////////////////////////////////////////////
        try {
            setOpacity(1);
            setVisible(true);

            // wait for some time
            Thread.sleep(delay);

            // make the message disappear  slowly
            for (double d = 1.0; d > 0.2; d -= 0.1) {
                Thread.sleep(100);
                setOpacity((float) d);
            }

            // set the visibility to false
           // setVisible(false);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            setVisible(false);
            count--;
        }
    }

    @Override
    public void run() {
        showtoast();
    }
}