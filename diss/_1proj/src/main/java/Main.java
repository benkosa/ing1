import _2zadanie.GuiZadanie2;

public class Main {
    public static void main(String[] args) {
//        new GuiZadanie1().start();

//        new Thread(Pi::new).start();

//        Random genSeed = new Random(0);
//        ExponentialDistribution customerArrived = new ExponentialDistribution(genSeed, 5);
//        ExponentialDistribution customerServing = new ExponentialDistribution(genSeed, 4);
//
//        for (int i = 0; i < 12; i++) {
//            System.out.println("customerArrived "+customerArrived.sample());
//            System.out.println("customerServing "+customerServing.sample());
//        }
//

//        MonteCarlo test = new Shop(1, 1000000000, 25);
//        test.simulationStart();

//        MonteCarlo test = new STK(1, 8*60*60, 25);
//        test.simulationStart();

//            new Thread(GuiZadanie2::new).start();
//            new GuiZadanie2().start();

        new Thread(() -> new GuiZadanie2().start()).start();


        //DistTests distTests = new DistTests();
            //distTests.testTriangular();
            //distTests.testExponential(100000, 5, 25);


    }
}