package org.chat.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Date;
import java.text.SimpleDateFormat;

public class CommonChat extends JDialog {
    private JPanel contentPane  = new JPanel();
    private JButton buttonSend = new JButton("Send");
    private JTextField inputText = new JTextField();
    private JTextArea chatText = new JTextArea();
    public final String FREE_CHAT = "Free Chat";

    private DataOutputStream dos;

    public DataOutputStream getDos() {
        return dos;
    }

    public void setDos(DataOutputStream dos) {
        this.dos = dos;
    }

    public void appendChatText(String text) {
        this.chatText.append(text + '\n');
    }

    public void appendTimeText(String text) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("[MM/dd HH:mm] ");
        String date = dateFormat.format(new Date());
        this.chatText.append(date + text + '\n');
    }

    public CommonChat() {
        contentPane.setLayout(new BorderLayout());
        chatText.setAutoscrolls(true);
        chatText.setEditable(false);

        JScrollPane scroll = new JScrollPane (chatText);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        contentPane.add(scroll, BorderLayout.CENTER);

        JPanel bottom = new JPanel();
        bottom.setLayout(new BorderLayout());


        bottom.add(inputText, BorderLayout.CENTER);
        bottom.add(buttonSend, BorderLayout.EAST);
        contentPane.add(bottom, BorderLayout.SOUTH);

        setContentPane(contentPane);
        setModal(false);
        getRootPane().setDefaultButton(buttonSend);

        buttonSend.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onSend();
            }
        });
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
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        setTitle(FREE_CHAT);
        pack();
        setOnCenter();
        System.out.println("Chat constructor");
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
            dos.writeUTF(inputText.getText());
            appendTimeText(inputText.getText());
            inputText.setText("");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void onCancel() {
        setVisible(false);
    }

}
