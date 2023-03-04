package org.main._1zadanie;

import javax.swing.*;

public class UpdateGui {

    UpdateGui (JLabel replication, JLabel result) {
        this.replication = replication;
        this.result = result;
    }

    private JLabel replication;
    private JLabel result;

    public void updateResults(double value, long replication) {
        if (this.replication != null) {
            this.replication.setText(String.valueOf(value));
            this.result.setText(String.valueOf(replication));
        }
    }
}
