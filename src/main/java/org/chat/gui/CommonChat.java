package org.chat.gui;

import org.chat.Log;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Date;
import java.text.SimpleDateFormat;

public class CommonChat extends JDialog implements Log {
    public final String FREE_CHAT = "Free Chat";
    public static final int TOAST_DELAY = 5000;
    public static final String DATE_FORMAT = "[MM/dd HH:mm] ";

    final private JTextField inputText;
    //private JTextArea chatText = new JTextArea();
    final private JTextPane chatText;
    private Color textColor = Color.BLACK;
    private Color userColor = Color.RED;
    private DataOutputStream dos;
    final private Security security;

    public Security getSecurity() {return security;}
    public DataOutputStream getDos() {return dos;}
    public void setTextColor(Color textColor) {this.textColor = textColor;}
    public void setUserColor(Color userColor) {this.userColor = userColor;}
    public JTextPane getChatText() {return chatText;}
    public Color getTextColor() {return textColor;}
    public Color getUserColor() {return userColor;}
    public void setDos(DataOutputStream dos) {this.dos = dos;}

    public CommonChat() {
        security = new Security(this);

        JPanel contentPane = new JPanel();
        contentPane.setLayout(new BorderLayout());
        chatText = new JTextPane();
        chatText.setAutoscrolls(true);
        chatText.setEditable(false);

        JScrollPane scroll = new JScrollPane(chatText);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        contentPane.add(scroll, BorderLayout.CENTER);

        JPanel bottom = new JPanel();
        bottom.setLayout(new BorderLayout());
        inputText = new JTextField();
        bottom.add(inputText, BorderLayout.CENTER);
        JButton buttonSend = new JButton("Send");
        bottom.add(buttonSend, BorderLayout.EAST);
        contentPane.add(bottom, BorderLayout.SOUTH);

        setContentPane(contentPane);
        setModal(false);
        getRootPane().setDefaultButton(buttonSend);

        buttonSend.addActionListener(e -> onSend());
/*
        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });
*/
        // call onCancel() when cross is clicked
        setDefaultCloseOperation(HIDE_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        ActionListener action = e -> onCancel();

        contentPane.registerKeyboardAction(
                action,
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT
        );
        setTitle(FREE_CHAT);
        pack();
        setOnCenter();
       // System.out.println("Chat constructor");
    }

    private SimpleAttributeSet getAlignStyle(int alignWay, Color color, int s) {
        SimpleAttributeSet align = new SimpleAttributeSet();
        StyleConstants.setAlignment(align, alignWay);
        StyleConstants.setForeground(align, color);
        StyleConstants.setFontSize(align, s);
        return align;
    }

    private String getFormatDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        return dateFormat.format(new Date());
    }

    public void appendColorText(String text, Color color) {
        String date = getFormatDate();
        SimpleAttributeSet style = getAlignStyle(StyleConstants.ALIGN_LEFT, color, 13);
        addColoredText(this.chatText, date + text + "\n", style);
    }
    /*
    public void appendStyleText(String text, SimpleAttributeSet style) {
        String date = getFormatDate();
        addColoredText(this.chatText, date + text + "!!\n", style);
    }
*/
    private void addColoredText(JTextPane pane, String text, SimpleAttributeSet style) {
        StyledDocument doc = pane.getStyledDocument();
        try {
            doc.insertString(doc.getLength(), text, style);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    private void setOnCenter() {
        final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        final int width = screenSize.width / 2;
        final int height = screenSize.height / 2;
        setSize(width, height);
        setLocation(width - getWidth() / 2, height - getHeight() / 2);
    }

    private void onSend() {
        try {
            String cipherText = inputText.getText();
            if (getSecurity().isEncrypt()){
                cipherText = getSecurity().getEncrypt(cipherText);
            }
            dos.writeUTF(cipherText);
            appendColorText(inputText.getText(), getUserColor());
            inputText.setText("");
        } catch (IOException e) {
            e.printStackTrace();
            log("Error write");
        } catch (Exception e) {
            e.printStackTrace();
            log("Error encrypt");
        }
    }


    private void onCancel() {
        setVisible(false);
    }

    public void toast(String title, String msg){
        Toast.showToast(title, msg, TOAST_DELAY);
    }

}
