package org.main._1zadanie;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GuiZadanie1 extends JFrame {
    private JPanel panel1;
    private JTextField a1000000000TextField;
    private JTextField a1000000TextField;
    private JTextField a500TextField;
    private JButton startButton;
    private JTextField a0TextField;
    private JLabel replication1;
    private JLabel result1;
    private JButton stopButton;
    private JButton stopButton1;
    private JButton startButton1;
    private JTextField a1000000000TextField1;
    private JTextField a1000000TextField1;
    private JTextField a500TextField1;
    private JTextField a0TextField1;
    private JLabel replication2;
    private JLabel result2;

    Problem1 problem1;
    Problem2 problem2;

    /**
     * start zadanie 1
     */
    public GuiZadanie1() {
        startButton.addActionListener(e -> {
            if (problem1 == null) {
                int seed = Integer.parseInt(a0TextField.getText());
                long replications = Long.parseLong(a1000000000TextField.getText());
                int offset = Integer.parseInt(a1000000TextField.getText());
                int maxChart = Integer.parseInt(a500TextField.getText());

                Thread t = new Thread(() -> problem1 = new Problem1(
                        seed,
                        replications,
                        offset,
                        maxChart,
                        "Problem 1",
                        replication1,
                        result1
                ));
                t.start();
            }
        });

        stopButton.addActionListener(e -> {
            problem1.stopChart();
            problem1 = null;
        });
        startButton1.addActionListener(e -> {
            if (problem2 == null) {
                int seed = Integer.parseInt(a0TextField1.getText());
                long replications = Long.parseLong(a1000000000TextField1.getText());
                int offset = Integer.parseInt(a1000000TextField1.getText());
                int maxChart = Integer.parseInt(a500TextField1.getText());

                Thread t = new Thread(() -> problem2 = new Problem2(
                        seed,
                        replications,
                        offset,
                        maxChart,
                        "Problem 2",
                        replication2,
                        result2
                ));
                t.start();
            }
        });
        stopButton1.addActionListener(e -> {
            problem2.stopChart();
            problem2 = null;
        });
    }


    public JPanel getJPanel () {
        return this.panel1;
    }

    public void start() {
        this.setContentPane(this.getJPanel());
        this.setTitle("hello");
        this.setSize(1000, 1500);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
