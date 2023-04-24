package _3zadanie.support;

import _2zadanie.Vehicle;
import _2zadanie.VehicleType;
import shared.Distribution.DiscreteEmpiricalDistribution;
import shared.Distribution.DiscreteUniformDistribution;
import shared.Distribution.SeedGenerator;
import shared.Distribution.UniformDouble;

public class VehicleGenerator {
    private final DiscreteUniformDistribution inspectionTimePersonal;
    private final DiscreteEmpiricalDistribution inspectionTimeVan;
    private final DiscreteEmpiricalDistribution inspectionTimeCargo;
    private long vehicleId = 0;

    final UniformDouble vehicleTypeGen;

    public VehicleGenerator(SeedGenerator seedGenerator) {
        vehicleTypeGen = new UniformDouble(seedGenerator);
        inspectionTimePersonal = new DiscreteUniformDistribution(seedGenerator, 31*60, 45*60);
        inspectionTimeVan = new DiscreteEmpiricalDistribution(
                new int[]{35*60, 38*60, 41*60, 48*60},
                new int[]{37*60, 40*60, 47*60, 52*60},
                new double[]{.2, .35, .3, .15},
                seedGenerator
        );
        inspectionTimeCargo = new DiscreteEmpiricalDistribution(
                new int[]{37*60, 43*60, 46*60, 48*60 ,52*60, 56*60},
                new int[]{42*60, 45*60, 47*60, 51*60, 55*60, 65*60},
                new double[]{.05, .1, .15, .4, .25, .05},
                seedGenerator
        );
    }

    public Vehicle getVehicle() {
        double p = vehicleTypeGen.sample();
        double inspectionTime;
        VehicleType vehicleType;
        if (p < .65) {
            inspectionTime = inspectionTimePersonal.sample();
            vehicleType = VehicleType.PERSONAL;
        } else if (p < (.65+.21)) {
            inspectionTime = inspectionTimeVan.sample();
            vehicleType = VehicleType.VAN;
        } else {
            inspectionTime = inspectionTimeCargo.sample();
            vehicleType = VehicleType.CARGO;
        }

        return new Vehicle(vehicleType, inspectionTime, vehicleId += 1);
    }

    public void init() {
        vehicleId = 0;
    }
}
