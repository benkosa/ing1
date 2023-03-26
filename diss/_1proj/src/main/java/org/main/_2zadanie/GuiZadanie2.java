package org.main._2zadanie;

import org.main.shared.EventSimulation.EventSimulationCore;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.PriorityQueue;

public class GuiZadanie2 extends JFrame implements ISimDelegate{

    private JPanel panel1;
    private JTable table5;
    private JTable table4;
    private JTable table3;
    private JTable table2;
    private JTable table1;
    private JScrollPane ScrollPane1;
    private JScrollPane ScrollPane2;
    private JScrollPane ScrollPane3;
    private JScrollPane ScrollPane4;
    private JScrollPane ScrollPane5;
    private JButton playButton;
    private JButton pauseButton;

    public GuiZadanie2() {
        pauseButton.addActionListener(e -> stk.setPause(true));
        playButton.addActionListener(e -> stk.setPause(false));
    }

    public JPanel getJPanel () {
        return this.panel1;
    }

    STK stk = new STK(1, 8*60*60, 25);

    public void start() {
        this.setContentPane(this.getJPanel());
        this.setTitle("Zadanie 2 udalostna simulacia");
        this.setSize(1780, 780);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        stk.registerDelegate(this);
        stk.simulationStart();
    }

    @Override
    public void refresh(EventSimulationCore core) {
        final STK stk = (STK)core;
        //refreshTable(ScrollPane1, new String[]{"i", "type"}, valuesToArray(stk.queueBeforeStk));
        refreshTable(ScrollPane2, new String[]{"i", "type"}, valuesToArray(stk.queueBeforeStk));
        refreshTable(ScrollPane3, new String[]{"i", "type"}, valuesToArray(stk.queueInStk));
        refreshTable(ScrollPane4, new String[]{"i", "type"}, valuesToArray(stk.queueAfterStk));
        //refreshTable(ScrollPane5, new String[]{"i", "type"}, valuesToArray(stk.queueBeforeStk));
    }

    private String[][] valuesToArray(PriorityQueue<Vehicle> queue) {
        String[][] tableValues = new String[queue.size()][2];
        int i = 0;
        for (Vehicle vehicle : queue) {
            tableValues[i] = new String[]{i+"", vehicle.getVehicleType()+""};
            i+=1;
        }

        return tableValues;
    }

    private void refreshTable(JScrollPane scrollPane, String[] tableHeader, String[][] tableValues) {
        JTable table = new JTable(tableValues, tableHeader);
        table.setEnabled(false);
        scrollPane.setViewportView(table);
    }
}
