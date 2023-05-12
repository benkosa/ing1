package _3zadanie;

import OSPABA.ISimDelegate;
import OSPABA.SimState;
import OSPABA.Simulation;
import _2zadanie.Graph.Graph1;
import _2zadanie.Graph.GraphAgent1;
import _2zadanie.Graph.GraphAgent2;
import _2zadanie.Vehicle;
import shared.Statistics.Statistics;
import shared.Workers.Worker;
import _3zadanie.simulation.MyMessage;
import _3zadanie.simulation.MySimulation;

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
    private JLabel realTime;
    private JTextArea textArea1;
    private JLabel g1pCount;
    private JLabel g2pCount;
    private JScrollPane ScrollPane11;
    private JScrollPane ScrollPane12;
    private JTextField a0TextField2;
    private JScrollPane ScrollPane13;
    private JScrollPane ScrollPane14;
    private JScrollPane ScrollPane15;
    private JLabel g3uCount;
    private JLabel g3nuCount;
    private JLabel g3pCount;
    private JCheckBox overenieCheckBox;
    private JTextField a5TextField1;
    private JButton mzdaButton;
    private JLabel mzdaOut;
    private JTextPane textPane1;

    public GuiZadanie3() {
        startRealTimeButton.addActionListener(e -> new Thread(this::startSimRealTime).start());
        startTurboButton.addActionListener(e -> new Thread(this::startSimTurbo).start());
        pauseButton.addActionListener(e -> stk.pauseSimulation());
        playButton.addActionListener(e -> stk.resumeSimulation());
        stopButton.addActionListener(e -> stk.stopSimulation());
        adjustSlowDownButton.addActionListener(e -> stk.setSimSpeed(
                Double.parseDouble(a60TextField.getText()),
                Double.parseDouble(a1000TextField.getText())
        ));
        a10Button.addActionListener(e -> stk.setSimSpeed(
                Integer.parseInt(a60TextField.getText()),
                0.001
        ));
        a100Button.addActionListener(e -> stk.setSimSpeed(
                Integer.parseInt(a60TextField.getText()),
                0.01
        ));
        a1000Button.addActionListener(e -> stk.setSimSpeed(
                Integer.parseInt(a60TextField.getText()),
                .1
        ));
        a2000Button.addActionListener(e -> stk.setSimSpeed(
                Integer.parseInt(a60TextField.getText()),
                .5
        ));
        graph1Button.addActionListener(e -> {
            new Thread(() -> new GraphAgent1(
                    1,
                    16,
                    Integer.parseInt(a100000TextField.getText()),
                    Integer.parseInt(a0TextField.getText()),
                    Integer.parseInt(a20TextField.getText()),
                    Integer.parseInt(a5TextField1.getText()),
                    getInputFlow()
            )).start();
        });
        graph2Button.addActionListener(e -> {
            new Thread(() -> new GraphAgent2(
                    10,
                    26,
                    Integer.parseInt(a100000TextField.getText()),
                    Integer.parseInt(a0TextField.getText()),
                    Integer.parseInt(a5TextField.getText()),
                    Integer.parseInt(a20TextField.getText()),
                    Integer.parseInt(a5TextField1.getText()),
                    getInputFlow()
            )).start();
        });
        mzdaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mzdaOut.setText(countWage());
            }
        });
    }


    private static final int RECEPTION_WAGE = 1100;
    private static final int CHEAP_MECHANIC_WAGE = 1500;
    private static final int EXPENSIVE_MECHANIC_WAGE = 2000;

    private static final int SIM_TIME = 8*60*60;

    private String countWage() {
        return (Integer.parseInt(a5TextField.getText()) * RECEPTION_WAGE +
                        Integer.parseInt(a20TextField.getText()) * EXPENSIVE_MECHANIC_WAGE +
                        Integer.parseInt(a5TextField1.getText()) * CHEAP_MECHANIC_WAGE) + " €";
    }
    private String countWage(MySimulation stk) {
        return (stk.agentGroup1().group1.getNumberOfWorkers() * RECEPTION_WAGE +
                stk.agentInspection().groupExpensive.getNumberOfWorkers() * EXPENSIVE_MECHANIC_WAGE +
                stk.agentInspection().groupCheap.getNumberOfWorkers() * CHEAP_MECHANIC_WAGE) + " €";
    }

    public void start() {
        this.setContentPane(this.panel1);
        this.setTitle("Zadanie 3 agentova simulacia");
        this.setSize(1450, 830);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    @Override
    public void simStateChanged(Simulation simulation, SimState simState) { }

    @Override
    public void refresh(Simulation simulation) {
        MySimulation stk = (MySimulation) simulation;

        refreshTable(ScrollPane1, new String[]{"i", "type", "start waiting"}, valuesToArrayVehicleTotalArrived(stk.agentModel().arrivedVehicles));
        refreshTable(ScrollPane2, new String[]{"id", "type", "start waiting"}, valuesToArrayVehicle(stk.agentGroup1().queueBeforeStk.getQueue()));
        refreshTable(ScrollPane3, new String[]{"id", "type", "start waiting"}, valuesToArrayVehicle(stk.agentGroup1().queueInStk.getQueue()));
        refreshTable(ScrollPane6, new String[]{"id", "type", "start waiting"}, hashMapToArrayVehicles(stk.agentGroup1().queueInStk.getLockedQueue()));
        refreshTable(ScrollPane4, new String[]{"id", "type", "start waiting"}, valuesToArrayVehicle(stk.agentGroup1().queueAfterStk.getQueue()));
        refreshTable(ScrollPane5, new String[]{"i", "type", "start waiting"}, valuesToArrayVehicleTotalLeft(stk.agentModel().leftVehicles));

        refreshTable(ScrollPane7, new String[]{"id w", "p"}, valuesToArrayWorkers(stk.agentGroup1().group1.getWorkers()));
        refreshTable(ScrollPane8, new String[]{"id w", "id c", "s", "p"}, hashMapToArrayWorkers(stk.agentGroup1().group1.getHiredWorkers()))
                .getColumnModel().getColumn(2).setMinWidth(65);
        refreshTable(ScrollPane11, new String[]{"id w", "p"}, hashMapToArrayBreak(stk.agentGroup1().group1.getLunchBreakWorkers()));

        refreshTable(ScrollPane9, new String[]{"id w", "p"}, valuesToArrayWorkers(stk.agentInspection().groupExpensive.getWorkers()));
        refreshTable(ScrollPane10, new String[]{"id w", "id c", "s", "p"}, hashMapToArrayWorkers(stk.agentInspection().groupExpensive.getHiredWorkers()))
                .getColumnModel().getColumn(2).setMinWidth(65);
        refreshTable(ScrollPane12, new String[]{"id w", "p"}, hashMapToArrayBreak(stk.agentInspection().groupExpensive.getLunchBreakWorkers()));

        refreshTable(ScrollPane13, new String[]{"id w", "p"}, valuesToArrayWorkers(stk.agentInspection().groupCheap.getWorkers()));
        refreshTable(ScrollPane14, new String[]{"id w", "id c", "s", "p"}, hashMapToArrayWorkers(stk.agentInspection().groupCheap.getHiredWorkers()))
                .getColumnModel().getColumn(2).setMinWidth(65);
        refreshTable(ScrollPane15, new String[]{"id w", "p"}, hashMapToArrayBreak(stk.agentInspection().groupCheap.getLunchBreakWorkers()));

        count1.setText(stk.agentModel().arrivedVehicles.size() + "");
        count2.setText(stk.agentGroup1().queueBeforeStk.getQueue().size() + "");
        count3.setText(stk.agentGroup1().queueInStk.getLockedQueue().size() + "");
        count4.setText(stk.agentGroup1().queueAfterStk.getQueue().size() + "");
        count5.setText(stk.agentModel().leftVehicles.size() + "");
        count6.setText(stk.agentGroup1().queueInStk.getQueue().size() + "");

        g1uCount.setText(stk.agentGroup1().group1.getWorkers().size() + "");
        g1nuCount.setText(stk.agentGroup1().group1.getHiredWorkers().size() + "");
        g1pCount.setText(stk.agentGroup1().group1.getLunchBreakWorkers().size() + "");

        g2uCount.setText(stk.agentInspection().groupExpensive.getWorkers().size() + "");
        g2nuCount.setText(stk.agentInspection().groupExpensive.getHiredWorkers().size() + "");
        g2pCount.setText(stk.agentInspection().groupExpensive.getLunchBreakWorkers().size() + "");

        g3uCount.setText(stk.agentInspection().groupCheap.getWorkers().size() + "");
        g3nuCount.setText(stk.agentInspection().groupCheap.getHiredWorkers().size() + "");
        g3pCount.setText(stk.agentInspection().groupCheap.getLunchBreakWorkers().size() + "");


        simTime.setText(stk.getCurrentTime() + "");
        realTime.setText(msToHMS(stk.getCurrentTime()));

    }

    private void printResult(MySimulation stk) {
        String result = "";
        result += String.format("replikacie:\t\t\t%d\n", stk.replicationCount());
        result += String.format("pracovnikov 1:\t\t\t%d\n", stk.agentGroup1().group1.getNumberOfWorkers());
        if(!stk.isVerificationMode()) {
            result += String.format("pracovnikov 2 drahy:\t\t%d\n", stk.agentInspection().groupExpensive.getNumberOfWorkers());
            result += String.format("pracovnikov 2 lacny:\t\t%d\n", stk.agentInspection().groupCheap.getNumberOfWorkers());
            result += String.format("mzda:\t\t\t%s\n", countWage(stk));
            result += String.format("Vstupny tok:\t\t\t%.4f\n", stk.agentModel().testInputFlow.totalResult());
        } else {
            result += String.format("pracovnikov 2:\t\t\t%d\n", stk.agentInspection().groupExpensive.getNumberOfWorkers());
        }
        result += String.format("vozidla v stk po ukonceni:\t\t%s\n",
                stdResult(stk.agentModel().averageVehiclesInSTK, 1)
        );
        result += String.format(
                "priemerny cas vozidla v stk:\t\t%s\n",
                stdResult(stk.averageVehicleTimeInSystem, 60)
        );
        result += String.format("priemerny pocet volnych recepncnych\t%s\n",
                stdResult(stk.agentGroup1().averageFreeWorker1, 1)
        );
        result += String.format("priemerny pocet volnych pracovnikov drahy\t%s\n",
                stdResult(stk.agentInspection().averageFreeWorker2, 1)
        );
        if(!stk.isVerificationMode()) {
            result += String.format("priemerny pocet volnych pracovnikov lacny\t%s\n",
                    stdResult(stk.agentInspection().averageFreeWorkerCheap, 1)
            );
        }
        result += String.format("priemerna dlzka cakania v rade pred stk\t%s\n",
                stdResult(stk.agentGroup1().averageWaitingBeforeSTK, 60)
        );
        result += String.format("priemerny pocet cakajucich pred stk\t%s\n",
                stdResult(stk.agentGroup1().averageQueueBeforeSTK, 1)
        );
        result += String.format(
                "priemerny pocet zakaznikov v systeme\t%s\n",
                stdResult(stk.agentModel().averageQueueInSystem ,1)
        );
        textArea1.setText(result);
        //printCsv();
        //System.out.print(printCsvOneLine(stk));
    }

    private void printCsv() {
        String result = "";
        result += String.format("%d\n", stk.replicationCount());
        result += String.format("%d\n", stk.agentGroup1().group1.getNumberOfWorkers());
        if(!stk.isVerificationMode()) {
            result += String.format("%d\n", stk.agentInspection().groupExpensive.getNumberOfWorkers());
            result += String.format("%d\n", stk.agentInspection().groupCheap.getNumberOfWorkers());
            result += String.format("%s\n", countWage(stk));
            result += String.format("%.4f\n", stk.agentModel().testInputFlow.totalResult());
        } else {
            result += String.format("%d\n", stk.agentInspection().groupExpensive.getNumberOfWorkers());
        }
        result += String.format("%s\n",
                stdResultCsv(stk.agentModel().averageVehiclesInSTK, 1)
        );
        result += String.format(
                "%s\n",
                stdResultCsv(stk.averageVehicleTimeInSystem, 60)
        );
        result += String.format("%s\n",
                stdResultCsv(stk.agentGroup1().averageFreeWorker1, 1)
        );
        result += String.format("%s\n",
                stdResultCsv(stk.agentInspection().averageFreeWorker2, 1)
        );
        if(!stk.isVerificationMode()) {
            result += String.format("%s\n",
                    stdResultCsv(stk.agentInspection().averageFreeWorkerCheap, 1)
            );
        }
        result += String.format("%s\n",
                stdResultCsv(stk.agentGroup1().averageWaitingBeforeSTK, 60)
        );
        result += String.format("%s\n",
                stdResultCsv(stk.agentGroup1().averageQueueBeforeSTK, 1)
        );
        result += String.format(
                "%s\n",
                stdResultCsv(stk.agentModel().averageQueueInSystem ,1)
        );
        result = result.replace(".", ",");
        System.out.println(result);
    }

    private String stdResultCsv(Statistics stat, double divide) {
        return String.format(
                "%.4f\t%.4f\t%.4f\t%.4f\t%.4f",
                stat.totalResult()/divide,
                stat.sampleStandardDeviation.getConfidenceInterval(1.645)[0]/divide,
                stat.sampleStandardDeviation.getConfidenceInterval(1.645)[1]/divide,
                stat.sampleStandardDeviation.getConfidenceInterval(1.812)[0]/divide,
                stat.sampleStandardDeviation.getConfidenceInterval(1.812)[1]/divide
        );
    }

    private String stdResult(Statistics stat, double divide) {
        return String.format(
                "%.4f <%.4f,%.4f>",
                stat.totalResult()/divide,
                stat.sampleStandardDeviation.getConfidenceInterval(1.812)[0]/divide,
                stat.sampleStandardDeviation.getConfidenceInterval(1.812)[1]/divide
        );
    }

    private String stdResultCsvOneLine(Statistics stat, double divide) {
        return String.format(
                "%.4f",
                stat.totalResult()/divide
        );
    }
    private String printCsvOneLine(MySimulation stk) {
        String result = "";
        result += String.format("%d\t", stk.replicationCount());
        result += String.format("%d\t", stk.agentGroup1().group1.getNumberOfWorkers());
        if(!stk.isVerificationMode()) {
            result += String.format("%d\t", stk.agentInspection().groupExpensive.getNumberOfWorkers());
            result += String.format("%d\t", stk.agentInspection().groupCheap.getNumberOfWorkers());
            result += String.format("%s\t", countWage(stk));
            result += String.format("%.4f\t", stk.agentModel().testInputFlow.totalResult());
        } else {
            result += String.format("%d\t", stk.agentInspection().groupExpensive.getNumberOfWorkers());
        }
        result += String.format("%s\t",
                stdResultCsv(stk.agentModel().averageVehiclesInSTK, 1)
        );
        result += String.format(
                "%s\t",
                stdResultCsv(stk.averageVehicleTimeInSystem, 60)
        );
        result += String.format("%s\t",
                stdResultCsv(stk.agentGroup1().averageFreeWorker1, 1)
        );
        result += String.format("%s\t",
                stdResultCsv(stk.agentInspection().averageFreeWorker2, 1)
        );
        if(!stk.isVerificationMode()) {
            result += String.format("%s\t",
                    stdResultCsv(stk.agentInspection().averageFreeWorkerCheap, 1)
            );
        }
        result += String.format("%s\t",
                stdResultCsv(stk.agentGroup1().averageWaitingBeforeSTK, 60)
        );
        result += String.format("%s\t",
                stdResultCsv(stk.agentGroup1().averageQueueBeforeSTK, 1)
        );
        result += String.format(
                "%s\t",
                stdResultCsv(stk.agentModel().averageQueueInSystem ,1)
        );
        result = result.replace(".", ",");
        return result+"\n";
    }

    private MySimulation stk;

    private void startSimRealTime() {
        stk = new MySimulation(
                Integer.parseInt(a0TextField.getText()),
                Integer.parseInt(a5TextField.getText()),
                Integer.parseInt(a20TextField.getText()),
                Integer.parseInt(a5TextField1.getText()),
                overenieCheckBox.isSelected(),
                getInputFlow()
        );
        stk.registerDelegate(this);

        stk.setSimSpeed(
                Integer.parseInt(a60TextField.getText()),
                Integer.parseInt(a1000TextField.getText())
        );

        stk.onSimulationDidFinish((sim) -> {
            printResult(stk);
        });

        stk.simulate(1 , SIM_TIME);
    }

    private double getInputFlow() {
        double inputFlow = Double.parseDouble(a0TextField2.getText());
        if (inputFlow == 0) {
            return 1;
        }
        return 1+inputFlow/100;
    }

    private void startSimTurbo() {
        stk = new MySimulation(
                Integer.parseInt(a0TextField.getText()),
                Integer.parseInt(a5TextField.getText()),
                Integer.parseInt(a20TextField.getText()),
                Integer.parseInt(a5TextField1.getText()),
                overenieCheckBox.isSelected(),
                getInputFlow()
        );
        stk.onSimulationDidFinish((sim) -> {
            printResult(stk);
        });

        stk.simulate(Integer.parseInt(a100000TextField.getText()), SIM_TIME);
    }

    private JTable refreshTable(JScrollPane scrollPane, String[] tableHeader, String[][] tableValues) {
        JTable table = new JTable(tableValues, tableHeader);
        table.setEnabled(false);
        scrollPane.setViewportView(table);
        return table;
    }

    private String[][] valuesToArrayWorkers(Collection<Worker> queue) {
        String[][] tableValues = new String[queue.size()][2];
        int i = 0;
        for (Worker worker : queue) {
            tableValues[i] = new String[]{worker.getId()+"", worker.isHadLunchBreak() ? "1" : "0"};
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

    private String[][] valuesToArrayVehicleTotalArrived(Collection<MyMessage> queue) {
        String[][] tableValues = new String[queue.size()][3];
        int i = 0;
        for (MyMessage message : queue) {
            Vehicle vehicle = message.getVehicle();
            tableValues[i] = new String[]{vehicle.id+"", vehicle.getVehicleType()+"", msToHMS(message.vehicleArrived)};
            i+=1;
        }

        return tableValues;
    }

    private String[][] valuesToArrayVehicleTotalLeft(Collection<MyMessage> queue) {
        String[][] tableValues = new String[queue.size()][3];
        int i = 0;
        for (MyMessage message : queue) {
            Vehicle vehicle = message.getVehicle();
            tableValues[i] = new String[]{vehicle.id+"", vehicle.getVehicleType()+"", msToHMS(message.vehicleLeft)};
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
        String[][] tableValues = new String[queue.size()][4];
        final int[] i = {0};
        queue.forEach( (key, value) -> {
            tableValues[i[0]] = new String[] {value.getId()+"", key+"", msToHMS(value.getStartedWork()) ,value.isHadLunchBreak() ? "1" : "0"};
            i[0] +=1;
        });
        return tableValues;
    }

    private String[][] hashMapToArrayBreak(HashMap<Integer, Worker> queue) {
        String[][] tableValues = new String[queue.size()][3];
        final int[] i = {0};
        queue.forEach( (key, value) -> {
            tableValues[i[0]] = new String[] {value.getId()+"", value.isHadLunchBreak() ? "1" : "0"};
            i[0] +=1;
        });
        return tableValues;
    }
}
