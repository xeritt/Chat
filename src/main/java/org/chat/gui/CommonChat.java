package org.chat.gui;

import org.chat.Client;
import org.chat.ClientWriteData;

import javax.swing.*;
import java.awt.event.*;
import java.io.DataOutputStream;
import java.io.IOException;

public class CommonChat extends JDialog {
    private JPanel contentPane;
    private JButton buttonSend;
    private JTextField inputText;
    private JTextArea chatText;

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


  //  private JButton buttonCancel;

    public CommonChat() {
        setContentPane(contentPane);
        setModal(true);
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

        pack();
    }

    private void onSend() {
        //ClientWriteData.write(Client.clientSocket, inputText.getText());
        try {
            dos.writeUTF(inputText.getText());
            chatText.append(inputText.getText() + '\n');
            inputText.setText("");

        } catch (IOException e) {
            e.printStackTrace();
        }

        // add your code here
        //dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        //dispose();
        setVisible(false);
    }

    public static void main(String[] args) {
        CommonChat dialog = new CommonChat();
        dialog.setVisible(true);
    }
}
