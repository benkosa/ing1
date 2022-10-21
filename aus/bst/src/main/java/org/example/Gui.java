package org.example;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Gui extends JFrame {
    private JButton setTimeButton;
    private JPanel panelMain;
    private JLabel label1;
    private JTextField inputTime;

    public JPanel getJPanel () {
        return this.panelMain;
    }

    public Gui() {
        setTimeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println(inputTime.getText());
            }
        });
    }
}
