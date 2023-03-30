package org.main._2zadanie;

import org.main.shared.EventSimulation.EventSimulationCore;

import javax.swing.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;

public class GuiZadanie2 extends JFrame implements ISimDelegate{

    private JPanel panel1;
    private JScrollPane ScrollPane1;
    private JScrollPane ScrollPane2;
    private JScrollPane ScrollPane3;
    private JScrollPane ScrollPane4;
    private JScrollPane ScrollPane5;
    private JButton playButton;
    private JButton pauseButton;
    private JLabel group1;
    private JLabel group2;
    private JLabel simTime;
    private JLabel lastEvent;
    private JScrollPane ScrollPane6;

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
        refreshTable(ScrollPane1, new String[]{"i", "type"}, valuesToArray(stk.arrivedVehicles));
        refreshTable(ScrollPane2, new String[]{"id", "type"}, valuesToArray(stk.queueBeforeStk.getQueue()));
        refreshTable(ScrollPane3, new String[]{"id", "type"}, valuesToArray(stk.queueInStk.getQueue()));
        refreshTable(ScrollPane6, new String[]{"id", "type"}, hashMapToArray(stk.queueInStk.getLockedQueue()));
        refreshTable(ScrollPane4, new String[]{"id", "type"}, valuesToArray(stk.queueAfterStk.getQueue()));
        refreshTable(ScrollPane5, new String[]{"i", "type"}, valuesToArray(stk.leftVehicles));
        group1.setText("pocet pracovnikov: " + stk.group1.getNumberOfWorkers() + "; zaneprazdneny: " + stk.group1.getWorkersInUsage());
        group2.setText("pocet pracovnikov: " + stk.group2.getNumberOfWorkers() + "; zaneprazdneny: " + stk.group2.getWorkersInUsage());
        simTime.setText(stk.getCurrentTime()+"");
        getLastEventInfo();
    }

    private void getLastEventInfo() {
        final String[] className = (stk.lastEvent.getClass()+"").split("\\.");
        String vehicleInfo = "";
        if (stk.lastEvent instanceof final VehicleEvent event) {
            vehicleInfo+= event.vehicle.id;
        }
        if (className.length > 0)
            lastEvent.setText(className[className.length - 1] + " " + vehicleInfo);

    }

    private String[][] hashMapToArray(HashMap<Long, Vehicle> queue) {
        LinkedList<Vehicle> list  = new LinkedList<>();
        queue.forEach( (key, value) -> list.add(value));
        return valuesToArray(list);
    }

    private String[][] valuesToArray(Collection<Vehicle> queue) {
        String[][] tableValues = new String[queue.size()][2];
        int i = 0;
        for (Vehicle vehicle : queue) {
            tableValues[i] = new String[]{vehicle.id+"", vehicle.getVehicleType()+""};
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
