package _3zadanie;

import OSPABA.IAnimDelegate;
import OSPABA.ISimDelegate;
import OSPABA.SimState;
import OSPABA.Simulation;
import _2zadanie.STK;
import _2zadanie.Vehicle;
import _2zadanie.Workers.Worker;
import _3zadanie.simulation.MyMessage;
import _3zadanie.simulation.MySimulation;
import shared.EventSimulation.EventSimulationCore;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;

public class GuiZadanie3 extends JFrame implements ISimDelegate {
    private JPanel panel1;
    private JScrollPane ScrollPane1;
    private JScrollPane ScrollPane2;
    private JScrollPane ScrollPane4;
    private JScrollPane ScrollPane5;
    private JLabel count1;
    private JLabel count2;
    private JLabel count3;
    private JLabel count4;
    private JLabel count5;
    private JScrollPane ScrollPane6;
    private JScrollPane ScrollPane3;
    private JLabel count6;
    private JButton startRealTimeButton;
    private JButton startTurboButton;
    private JTextField a100000TextField;
    private JTextField a0TextField;
    private JTextField a60TextField;
    private JTextField a1000TextField;
    private JTextField a5TextField;
    private JTextField a20TextField;
    private JButton adjustSlowDownButton;
    private JButton graph2Button;
    private JButton pauseButton;
    private JButton playButton;
    private JButton graph1Button;
    private JButton a2000Button;
    private JButton a1000Button;
    private JButton a10Button;
    private JButton a100Button;
    private JButton stopButton;
    private JScrollPane ScrollPane7;
    private JScrollPane ScrollPane8;
    private JLabel g1nuCount;
    private JLabel g1uCount;
    private JScrollPane ScrollPane9;
    private JScrollPane ScrollPane10;
    private JLabel g2uCount;
    private JLabel g2nuCount;
    private JLabel simTime;
    private JLabel lastEvent;
    private JLabel realTime;
    private JTextArea textArea1;

    public GuiZadanie3() {
        startRealTimeButton.addActionListener(e -> new Thread(() -> startSimRealTime()).start());
    }

    public void start() {
        this.setContentPane(this.panel1);
        this.setTitle("Zadanie 3 agentova simulacia");
        this.setSize(1000, 830);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    @Override
    public void simStateChanged(Simulation simulation, SimState simState) { }

    @Override
    public void refresh(Simulation simulation) {
        MySimulation stk = (MySimulation) simulation;

        refreshTable(ScrollPane1, new String[]{"i", "type", "start waiting"}, valuesToArrayVehicle(stk.agentModel().arrivedVehicles));
        refreshTable(ScrollPane2, new String[]{"id", "type", "start waiting"}, valuesToArrayVehicle(stk.agentGroup1().queueBeforeStk.getQueue()));
        refreshTable(ScrollPane3, new String[]{"id", "type", "start waiting"}, valuesToArrayVehicle(stk.agentGroup1().queueInStk.getQueue()));
        refreshTable(ScrollPane6, new String[]{"id", "type", "start waiting"}, hashMapToArrayVehicles(stk.agentGroup1().queueInStk.getLockedQueue()));
        refreshTable(ScrollPane4, new String[]{"id", "type", "start waiting"}, valuesToArrayVehicle(stk.agentGroup1().queueAfterStk.getQueue()));
        refreshTable(ScrollPane5, new String[]{"i", "type", "start waiting"}, valuesToArrayVehicle(stk.agentModel().leftVehicles));

        refreshTable(ScrollPane7, new String[]{"id w"}, valuesToArrayWorkers(stk.agentGroup1().group1.getWorkers()));
        refreshTable(ScrollPane8, new String[]{"id w", "id c"}, hashMapToArrayWorkers(stk.agentGroup1().group1.getHiredWorkers()));
        refreshTable(ScrollPane9, new String[]{"id w"}, valuesToArrayWorkers(stk.agentInspection().group2.getWorkers()));
        refreshTable(ScrollPane10, new String[]{"id w", "id c"}, hashMapToArrayWorkers(stk.agentInspection().group2.getHiredWorkers()));

        count1.setText(stk.agentModel().arrivedVehicles.size() + "");
        count2.setText(stk.agentGroup1().queueBeforeStk.getQueue().size() + "");
        count3.setText(stk.agentGroup1().queueInStk.getLockedQueue().size() + "");
        count4.setText(stk.agentGroup1().queueAfterStk.getQueue().size() + "");
        count5.setText(stk.agentModel().leftVehicles.size() + "");
        count6.setText(stk.agentGroup1().queueInStk.getQueue().size() + "");

        g1uCount.setText(stk.agentGroup1().group1.getWorkers().size() + "");
        g1nuCount.setText(stk.agentGroup1().group1.getHiredWorkers().size() + "");
        g2uCount.setText(stk.agentInspection().group2.getWorkers().size() + "");
        g2nuCount.setText(stk.agentInspection().group2.getHiredWorkers().size() + "");


        simTime.setText(stk.getCurrentTime() + "");

    }

    private void printResult(MySimulation stk) {
        String result = "";
        result += String.format("replikacie:\t\t\t%d\n", stk.replicationCount());
        result += String.format("pracovnikov 1:\t\t\t%d\n", stk.agentGroup1().group1.getNumberOfWorkers());
        result += String.format("pracovnikov 2:\t\t\t%d\n", stk.agentInspection().group2.getNumberOfWorkers());
        //result += String.format("vozidla v stk po ukonceni:\t\t%.4f\n", stk.averageVehiclesInSTK.totalResult());
        result += String.format(
                "priemerny cas vozidla v stk:\t\t%.4f <%.4f,%.4f>\n",
                stk.averageVehicleTimeInSystem.totalResult() / 60.,
                stk.averageVehicleTimeInSystem.sampleStandardDeviation.getConfidenceInterval(1.645)[0],
                stk.averageVehicleTimeInSystem.sampleStandardDeviation.getConfidenceInterval(1.645)[1]
        );
//        result += String.format("priemerny pocet volnych pracovnikov 1\t%.4f\n", stk.averageFreeWorker1.totalResult());
//        result += String.format("priemerny pocet volnych pracovnikov 2\t%.4f\n", stk.averageFreeWorker2.totalResult());
//        result += String.format("priemerna dlzka cakania v rade pred stk\t%.4f\n", stk.averageWaitingBeforeSTK.totalResult() / 60.);
//        result += String.format("priemerny pocet cakajucich pred stk\t%.4f\n", stk.averageQueueBeforeSTK.totalResult());
//        result += String.format(
//                "priemerny pocet zakaznikov v systeme\t%.4f <%.4f,%.4f>\n",
//                stk.averageQueueInSystem.totalResult(),
//                stk.averageQueueInSystem.sampleStandardDeviation.getConfidenceInterval(1.96)[0],
//                stk.averageQueueInSystem.sampleStandardDeviation.getConfidenceInterval(1.96)[1]
//        );

        this.textArea1.setText(result);
    }

    STK stk;
    private void startSimRealTime() {
        MySimulation stk = new MySimulation(25, 20, 20);
        stk.registerDelegate(this);

        stk.setSimSpeed(1000, 0.1);

//        stk.onRefreshUI((sim)->
//        {
//            System.out.println("onRefreshUI");
//        });
//
//        stk.onSimulationWillStart(s ->{
//            System.out.println("Simulating...");
//        });

        stk.onSimulationDidFinish((sim) -> {
            printResult(stk);
        });


        stk.simulate(1, 8*60*60);
    }

    private void refreshTable(JScrollPane scrollPane, String[] tableHeader, String[][] tableValues) {
        JTable table = new JTable(tableValues, tableHeader);
        table.setEnabled(false);
        scrollPane.setViewportView(table);
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

    private String[][] valuesToArrayVehicle(Collection<MyMessage> queue) {
        String[][] tableValues = new String[queue.size()][3];
        int i = 0;
        for (MyMessage message : queue) {
            Vehicle vehicle = message.getVehicle();
            tableValues[i] = new String[]{vehicle.id+"", vehicle.getVehicleType()+"", msToHMS(vehicle.getStartWaitingInQue())};
            i+=1;
        }

        return tableValues;
    }

    private String msToHMS(double seconds) {
        seconds += 9*60*60;
        double minutes = seconds / 60;
        double hours = minutes / 60;
        minutes %= 60;
        seconds %= 60;
        return String.format("%02d:%02d:%02d", (int) hours, (int) minutes, (int) seconds);
    }

    private String[][] hashMapToArrayVehicles(HashMap<Long, MyMessage> queue) {
        LinkedList<MyMessage> list  = new LinkedList<>();
        queue.forEach( (key, value) -> list.add(value));
        return valuesToArrayVehicle(list);
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
}
