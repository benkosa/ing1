package org.main._2zadanie;

import org.main._2zadanie.Workers.Worker;
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
    private JLabel simTime;
    private JLabel lastEvent;
    private JScrollPane ScrollPane6;
    private JScrollPane ScrollPane7;
    private JScrollPane ScrollPane8;
    private JScrollPane ScrollPane9;
    private JScrollPane ScrollPane10;
    private JLabel count1;
    private JLabel count2;
    private JLabel count3;
    private JLabel count6;
    private JLabel count4;
    private JLabel count5;
    private JLabel g1uCount;
    private JLabel g1nuCount;
    private JLabel g2uCount;
    private JLabel g2nuCount;
    private JButton startRealTimeButton;
    private JButton startTurboButton;
    private JTextField a100000TextField;
    private JTextField a0TextField;
    private JTextField a60TextField;
    private JTextField a1000TextField;
    private JLabel realTime;

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
        refreshTable(ScrollPane1, new String[]{"i", "type"}, valuesToArrayVehicle(stk.arrivedVehicles));
        refreshTable(ScrollPane2, new String[]{"id", "type"}, valuesToArrayVehicle(stk.queueBeforeStk.getQueue()));
        refreshTable(ScrollPane3, new String[]{"id", "type"}, valuesToArrayVehicle(stk.queueInStk.getQueue()));
        refreshTable(ScrollPane6, new String[]{"id", "type"}, hashMapToArrayVehicles(stk.queueInStk.getLockedQueue()));
        refreshTable(ScrollPane4, new String[]{"id", "type"}, valuesToArrayVehicle(stk.queueAfterStk.getQueue()));
        refreshTable(ScrollPane5, new String[]{"i", "type"}, valuesToArrayVehicle(stk.leftVehicles));

        refreshTable(ScrollPane7, new String[]{"id w"}, valuesToArrayWorkers(stk.group1.getWorkers()));
        refreshTable(ScrollPane8, new String[]{"id w", "id c"}, hashMapToArrayWorkers(stk.group1.getHiredWorkers()));
        refreshTable(ScrollPane9, new String[]{"id w"}, valuesToArrayWorkers(stk.group2.getWorkers()));
        refreshTable(ScrollPane10, new String[]{"id w", "id c"}, hashMapToArrayWorkers(stk.group2.getHiredWorkers()));

        count1.setText(stk.arrivedVehicles.size()+"");
        count2.setText(stk.queueBeforeStk.getQueue().size()+"");
        count3.setText(stk.queueInStk.getLockedQueue().size()+"");
        count4.setText(stk.queueAfterStk.getQueue().size()+"");
        count5.setText(stk.leftVehicles.size()+"");
        count6.setText(stk.queueInStk.getQueue().size()+"");

        g1uCount.setText(stk.group1.getWorkers().size()+"");
        g1nuCount.setText(stk.group1.getHiredWorkers().size()+"");
        g2uCount.setText(stk.group2.getWorkers().size()+"");
        g2nuCount.setText(stk.group2.getHiredWorkers().size()+"");

        simTime.setText(stk.getCurrentTime()+"");

        getLastEventInfo();
        actualiseRealTime(stk);
    }

    private void actualiseRealTime(STK stk) {
        this.realTime.setText(stk.getCurrentTime()+"");
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

    private String[][] hashMapToArrayVehicles(HashMap<Long, Vehicle> queue) {
        LinkedList<Vehicle> list  = new LinkedList<>();
        queue.forEach( (key, value) -> list.add(value));
        return valuesToArrayVehicle(list);
    }

    private String[][] valuesToArrayVehicle(Collection<Vehicle> queue) {
        String[][] tableValues = new String[queue.size()][2];
        int i = 0;
        for (Vehicle vehicle : queue) {
            tableValues[i] = new String[]{vehicle.id+"", vehicle.getVehicleType()+""};
            i+=1;
        }

        return tableValues;
    }
    private String[][] valuesToArrayWorkers(Collection<Worker> queue) {
        String[][] tableValues = new String[queue.size()][1];
        int i = 0;
        for (Worker worker : queue) {
            tableValues[i] = new String[]{worker.getId()+""};
            i+=1;
        }
        return tableValues;
    }
    private String[][] hashMapToArrayWorkers(HashMap<Long, Worker> queue) {
        String[][] tableValues = new String[queue.size()][2];
        final int[] i = {0};
        queue.forEach( (key, value) -> {
            tableValues[i[0]] = new String[] {value.getId()+"", key+""};
            i[0] +=1;
        });
        return tableValues;
    }

    private void refreshTable(JScrollPane scrollPane, String[] tableHeader, String[][] tableValues) {
        JTable table = new JTable(tableValues, tableHeader);
        table.setEnabled(false);
        scrollPane.setViewportView(table);
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
