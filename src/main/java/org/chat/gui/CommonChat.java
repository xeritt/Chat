package org.chat.gui;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.DataOutputStream;
import java.io.IOException;

public class CommonChat extends JDialog {
    private JPanel contentPane;
    private JButton buttonSend;
    private JTextField inputText;
    private JTextArea chatText;
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

        setTitle(FREE_CHAT);
        pack();
        setOnCenter();
    }

    private void setOnCenter(){
        final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        final int width = screenSize.width / 2;
        final int height = screenSize.height / 2;
        setSize(width, height);
        setLocation(width - getWidth() / 2, height - getHeight() / 2);
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
