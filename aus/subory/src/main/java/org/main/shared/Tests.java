package org.main.shared;

import org.main.app.Hospitalizacia;
import org.main.app.NewFeature;
import org.main.app.Pacient;
import org.main.dynamic_hashing.DynamicHashing;
import org.main.hashing.Hashing;

import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

public class Tests {
    public void testSize() {
        System.out.println("testSize: Compare length of toByteArray with getSize");
        Hospitalizacia hosp = new Hospitalizacia();
        Pacient pacient = new Pacient();
        NewFeature newFeature = new NewFeature();

        hosp.testSize();
        pacient.testSize();
        newFeature.testSize();

        System.out.println("testSize: done");
        System.out.println("----------------------------------------------------");
    }

    public void testRandomOperation(
            double pInsert,
            double pRemove,
            double pFind,
            int replication,
            int maxNumberOfElements,
            int maxValue,
            int blockSize,
            int blockNumber
    ) {
        if (replication == 0) return;

        System.out.println("testRandomOperation: Test insert find delete");
        System.out.println("pInsert: " + pInsert);
        System.out.println("pRemove: " + pRemove);
        System.out.println("pFind: " + pFind);
        System.out.println("replication: " + replication);
        System.out.println("maxNumberOfElements: " + maxNumberOfElements);
        System.out.println("maxValue: " + maxValue);
        System.out.println("blockSize: " + blockSize);
        System.out.println("blockNumber: " + blockNumber);

        if (pInsert + pRemove + pFind > 1) {
            System.out.println("error: p > 1");
            System.out.println("---------------------------------------------------");
            return;
        }

        for(int seed = 0; seed < replication; seed++) {
            Random rand = new Random(seed);
            Hashing<Pacient> hashing = new Hashing<>(
                    "test.dat",
                    blockSize,
                    blockNumber,
                    Pacient.class
            );

            ArrayList<Pacient> insertedPacients = new ArrayList<>();


            for (int i = 0; i < maxNumberOfElements; i++) {
                Integer value = rand.nextInt(maxValue);
                double operation = rand.nextFloat();
                Pacient pacient = new Pacient("meno", "priezvisko", value.toString(), value, new Date() );
                    // insert
                if (pInsert < operation) {
                    if (hashing.insert(pacient)) {
                        insertedPacients.add(pacient);
                    }

                    // remove
                } else if (pInsert + pRemove < operation) {

                    if (hashing.delete(pacient)) {
                        for (int i1 = 0; i1 < insertedPacients.size(); i1++) {
                            Pacient insertedPacient = insertedPacients.get(i);
                            if (insertedPacient.myEqual(pacient)) {
                                insertedPacients.remove(i1);
                            }
                        }
                    }
                    // find
                } else {
                    hashing.find(pacient);

                    if (insertedPacients.size() > 0) {
                        Pacient existingPacient = insertedPacients.get(rand.nextInt(insertedPacients.size()));
                        if (hashing.find(existingPacient) == null) {
                            System.out.println("error: existing pacient not found");
                        }
                    }
                }

            }


            // check if number of inserted elements is valid
            ArrayList<Pacient> fromHash = hashing.getWholeFile();

            if (fromHash.size() != insertedPacients.size()) {
                System.out.println("error: " + fromHash.size() + " != " + insertedPacients.size());
            }
            //hashing.readWholeFileNoValid();

            System.out.print("\b\b\b\b\b");
            System.out.print(Math.round(((float)seed/replication)*100) + " %");
        }

        System.out.println();
        System.out.println("testRandomOperation: done");
        System.out.println("----------------------------------------------------");
    }

    public void testRandomOperationDynamic(
            double pInsert,
            double pRemove,
            double pFind,
            int replication,
            int maxNumberOfElements,
            int maxValue,
            int blockSize
    ) {
        if (replication == 0) return;

        System.out.println("testRandomOperation: Test insert find delete dynamic hashing");
        System.out.println("pInsert: " + pInsert);
        System.out.println("pRemove: " + pRemove);
        System.out.println("pFind: " + pFind);
        System.out.println("replication: " + replication);
        System.out.println("maxNumberOfElements: " + maxNumberOfElements);
        System.out.println("maxValue: " + maxValue);
        System.out.println("blockSize: " + blockSize);

        if (pInsert + pRemove + pFind > 1) {
            System.out.println("error: p > 1");
            System.out.println("---------------------------------------------------");
            return;
        }

        for(int seed = 0; seed < replication; seed++) {
            Random rand = new Random(seed);
            DynamicHashing<Pacient> hashing = new DynamicHashing<>(
                    "test_dynamic.dat",
                    blockSize,
                    Pacient.class
            );

            ArrayList<Pacient> insertedPacients = new ArrayList<>();


            for (int i = 0; i < maxNumberOfElements; i++) {
                Integer value = rand.nextInt(maxValue);
                double operation = rand.nextFloat();
                Pacient pacient = new Pacient("meno", "priezvisko", value.toString(), value, new Date() );
                // insert
                if (operation < pInsert) {
                    if (hashing.insert(pacient)) {
                        insertedPacients.add(pacient);
                    }

                    // remove
                } else if (operation < pInsert + pRemove) {

                    if (hashing.delete(pacient)) {
                        for (int i1 = 0; i1 < insertedPacients.size(); i1++) {
                            Pacient insertedPacient = insertedPacients.get(i);
                            if (insertedPacient.myEqual(pacient)) {
                                insertedPacients.remove(i1);
                            }
                        }
                    }
                    // find
                } else {
                    hashing.find(pacient);

                    if (insertedPacients.size() > 0) {
                        Pacient existingPacient = insertedPacients.get(rand.nextInt(insertedPacients.size()));
                        if (hashing.find(existingPacient) == null) {
                            System.out.println("error: existing pacient not found");
                        }
                    }
                }

            }


            // check if number of inserted elements is valid
            ArrayList<Pacient> fromHash = hashing.getWholeFile();

            if (fromHash.size() != insertedPacients.size()) {
                System.out.println("error: " + fromHash.size() + " != " + insertedPacients.size());
            }

            System.out.print("\b\b\b\b\b");
            System.out.print(Math.round(((float)seed/replication)*100) + " %");
            //hashing.readWholeFileNoValid();
        }

        System.out.println();
        System.out.println("testRandomOperation: done");
        System.out.println("----------------------------------------------------");
    }

    public void testInsertOperationDynamic(
            int replication,
            int maxNumberOfElements,
            int maxValue,
            int blockSize,
            int fixed
    ) {
        if (replication == 0) return;

        System.out.println("testInsertOperationDynamic: Test insert dynamic hashing");

        System.out.println("replication: " + replication);
        System.out.println("maxNumberOfElements: " + maxNumberOfElements);
        System.out.println("maxValue: " + maxValue);
        System.out.println("blockSize: " + blockSize);
        int seed = 0;
        if (fixed > 0) {
            replication = fixed+1;
            seed = fixed;
        }


        for(; seed < replication; seed++) {
            //System.out.println(seed);
            Random rand = new Random(seed);
            DynamicHashing<Pacient> hashing = new DynamicHashing<>(
                    "test_dynamic_insert.dat",
                    blockSize,
                    Pacient.class
            );

            ArrayList<Pacient> insertedPacients = new ArrayList<>();


            for (int i = 0; i < maxNumberOfElements; i++) {
                Integer value = rand.nextInt(maxValue);
                Pacient pacient = new Pacient("meno", "priezvisko", value.toString(), value, new Date() );
                // insert
                if (hashing.insert(pacient)) {
                    insertedPacients.add(pacient);
                }

            }


            // check if number of inserted elements is valid
            ArrayList<Pacient> fromHash = hashing.getWholeFile();

            if (fromHash.size() != insertedPacients.size()) {
                System.out.println("error: " + fromHash.size() + " != " + insertedPacients.size());
            }
            for (Pacient insertedPacient : insertedPacients) {
                if (hashing.find(insertedPacient) == null) {
                    System.out.println("Error: inserted pacient not found");
                }
            }

            for (Pacient insertedPacient : insertedPacients) {
                if (!hashing.delete(insertedPacient)) {
                    System.out.println("Error: inserted pacient not deleted");
                }
            }

            System.out.print("\b\b\b\b\b");
            System.out.print(Math.round(((float)seed/replication)*100) + " %");
        }

        System.out.println();
        System.out.println("testInsertOperationDynamic: done");
        System.out.println("----------------------------------------------------");
    }

    public void powerTests() {
        int numberOfPacients = 1000;



        ArrayList<Pacient> pacients = new ArrayList<>(numberOfPacients);
        ArrayList<Float> operations = new ArrayList<>(numberOfPacients);

        Random rand = new Random();

        for (int i = 0; i < numberOfPacients; i++) {
            String rc = "";
            for (int j = 0; j < 10; j++) {
                rc += rand.nextInt(10);
            }
            pacients.add(new Pacient("", "", rc, 0, new Date()));
            operations.add(rand.nextFloat());
        }

        int blockFactors[] = {10};

        for (int blockFactor : blockFactors) {

            ArrayList<Pacient> insertedPacients = new ArrayList<>(numberOfPacients);

            // meranie casu potrebneho na alokovanie
            long start = System.nanoTime();
            Hashing<Pacient> staticHashing = new Hashing<>("powerTestStatic.dat", blockFactor,(numberOfPacients/blockFactor)*3, Pacient.class);
            long finish = System.nanoTime();
            long staticAlocation = finish - start;

            start = System.nanoTime();
            Hashing<Pacient> dynamicHashing = new DynamicHashing<>("powerTestDynamic.dat", blockFactor, Pacient.class);
            finish = System.nanoTime();
            long dynamicAlocation = finish - start;

            long staticInsert = 0;
            long staticFind = 0;
            long staticRemove = 0;

            long dynamicInsert = 0;
            long dynamicFind = 0;
            long dynamicRemove = 0;

            int i = 0;
            for (Float operation : operations) {
                // insert
                if (operation < 0.6) {
                    start = System.nanoTime();
                    staticHashing.insert(pacients.get(i));
                    finish = System.nanoTime();
                    staticInsert += finish-start;

                    start = System.nanoTime();
                    dynamicHashing.insert(pacients.get(i));
                    finish = System.nanoTime();
                    dynamicInsert += finish-start;

                    insertedPacients.add(pacients.get(i));
                    // remove
                } else if (operation < 0.8) {
                    Pacient pacient;
                    if (insertedPacients.size() > 0) {
                        pacient = insertedPacients.get(rand.nextInt(insertedPacients.size()));
                    } else {
                        pacient = pacients.get(i);
                    }
                    start = System.nanoTime();
                    dynamicHashing.delete(pacient);
                    finish = System.nanoTime();
                    dynamicRemove += finish-start;

                    start = System.nanoTime();
                    staticHashing.delete(pacient);
                    finish = System.nanoTime();
                    staticRemove += finish-start;

                    insertedPacients.remove(pacient);
                    // find
                } else {
                    Pacient pacient;
                    if (insertedPacients.size() > 0) {
                        pacient = insertedPacients.get(rand.nextInt(insertedPacients.size()));
                    } else {
                        pacient = pacients.get(i);
                    }
                    start = System.nanoTime();
                    dynamicHashing.find(pacient);
                    finish = System.nanoTime();
                    dynamicFind += finish-start;

                    start = System.nanoTime();
                    staticHashing.find(pacient);
                    finish = System.nanoTime();
                    staticFind += finish-start;
                }
                i++;
            }


            System.out.println(blockFactor+";"+staticAlocation + ";" + staticInsert + ";" + staticFind + ";" + staticRemove + ";" + staticHashing.fileSize());
            System.out.println(blockFactor+";"+dynamicAlocation + ";" + dynamicInsert + ";" + dynamicFind + ";" + dynamicRemove + ";" + dynamicHashing.fileSize());

        }

    }

    public void powerTests2() {
        int numberOfPacients = 100000;

        ArrayList<Pacient> pacients = new ArrayList<>(numberOfPacients);

        Random rand = new Random();

        for (int i = 0; i < numberOfPacients; i++) {
            String rc = "";
            for (int j = 0; j < 10; j++) {
                rc += rand.nextInt(10);
            }
            pacients.add(new Pacient("", "", rc, 0, new Date()));
        }

        int blockFactors[] = {10, 20, 50, 100, 1000};

        for (int blockFactor : blockFactors) {

            // meranie casu potrebneho na alokovanie
            long start = System.nanoTime();
            Hashing<Pacient> staticHashing = new Hashing<>("powerTestStatic.dat", blockFactor,(numberOfPacients/blockFactor)*3, Pacient.class);
            long finish = System.nanoTime();
            long staticAlocation = finish - start;

            start = System.nanoTime();
            Hashing<Pacient> dynamicHashing = new DynamicHashing<>("powerTestDynamic.dat", blockFactor, Pacient.class);
            finish = System.nanoTime();
            long dynamicAlocation = finish - start;

            long staticInsert = 0;
            long staticFind = 0;
            long staticRemove = 0;

            long dynamicInsert = 0;
            long dynamicFind = 0;
            long dynamicRemove = 0;

            // insert
            for (Pacient pacient : pacients) {
                start = System.nanoTime();
                staticHashing.insert(pacient);
                finish = System.nanoTime();
                staticInsert += finish-start;

                start = System.nanoTime();
                dynamicHashing.insert(pacient);
                finish = System.nanoTime();
                dynamicInsert += finish-start;
            }
            long dynamicFileSize = dynamicHashing.fileSize();

            // find
            for (Pacient pacient : pacients) {
                start = System.nanoTime();
                dynamicHashing.find(pacient);
                finish = System.nanoTime();
                dynamicFind += finish-start;

                start = System.nanoTime();
                staticHashing.find(pacient);
                finish = System.nanoTime();
                staticFind += finish-start;
            }

            // remove
            for (Pacient pacient : pacients) {
                // insert
                start = System.nanoTime();
                dynamicHashing.delete(pacient);
                finish = System.nanoTime();
                dynamicRemove += finish-start;

                start = System.nanoTime();
                staticHashing.delete(pacient);
                finish = System.nanoTime();
                staticRemove += finish-start;
            }

            System.out.println(blockFactor+";"+staticAlocation + ";" + staticInsert + ";" + staticFind + ";" + staticRemove + ";" + staticHashing.fileSize());
            System.out.println(blockFactor+";"+dynamicAlocation + ";" + dynamicInsert + ";" + dynamicFind + ";" + dynamicRemove + ";" + dynamicFileSize);

        }

    }


    public void testInsertOperationDynamicNewFeature(
            int replication,
            int maxNumberOfElements,
            int maxValue,
            int blockSize,
            int fixed
    ) {
        if (replication == 0) return;

        System.out.println("testInsertOperationDynamic: Test insert dynamic hashing");

        System.out.println("replication: " + replication);
        System.out.println("maxNumberOfElements: " + maxNumberOfElements);
        System.out.println("maxValue: " + maxValue);
        System.out.println("blockSize: " + blockSize);
        int seed = 0;
        if (fixed > 0) {
            replication = fixed+1;
            seed = fixed;
        }


        for(; seed < replication; seed++) {
            //System.out.println(seed);
            Random rand = new Random(seed);
            DynamicHashing<NewFeature> hashing = new DynamicHashing<>(
                    "test_dynamic_insert.dat",
                    blockSize,
                    NewFeature.class
            );

            ArrayList<NewFeature> insertedPacients = new ArrayList<>();


            for (int i = 0; i < maxNumberOfElements; i++) {
                Integer value = rand.nextInt(maxValue);
                NewFeature pacient = new NewFeature(rand.nextInt(), value.toString(), "popis", rand.nextInt());
                // insert
                if (hashing.insert(pacient)) {
                    insertedPacients.add(pacient);
                }

            }


            // check if number of inserted elements is valid
            ArrayList<NewFeature> fromHash = hashing.getWholeFile();

            if (fromHash.size() != insertedPacients.size()) {
                System.out.println("error: " + fromHash.size() + " != " + insertedPacients.size());
            }
            for (NewFeature insertedPacient : insertedPacients) {
                if (hashing.find(insertedPacient) == null) {
                    System.out.println("Error: inserted pacient not found");
                }
            }

            for (NewFeature insertedPacient : insertedPacients) {
                if (!hashing.delete(insertedPacient)) {
                    System.out.println("Error: inserted pacient not deleted");
                }
            }

            System.out.print("\b\b\b\b\b");
            System.out.print(Math.round(((float)seed/replication)*100) + " %");
        }

        System.out.println();
        System.out.println("testInsertOperationDynamic: done");
        System.out.println("----------------------------------------------------");
    }

}
