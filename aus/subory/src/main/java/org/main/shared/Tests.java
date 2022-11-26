package org.main.shared;

import org.main.app.Hospitalizacia;
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

        hosp.testSize();
        pacient.testSize();
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

            System.out.print("\b\b\b\b\b");
            System.out.print(Math.round(((float)seed/replication)*100) + " %");
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
}
