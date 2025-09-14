package org.fog.marina;

import java.util.List;

public class VehicularCloud {
    private String id;
    private BaseStation baseStation; // can be null for ad-hoc VC (vehicle-only)
    private List<VehicleState> vehicles;

    public VehicularCloud(String id, BaseStation baseStation, List<VehicleState> vehicles) {
        this.id = id;
        this.baseStation = baseStation;
        this.vehicles = vehicles;
    }

    public String getId() { return id; }
    public BaseStation getBaseStation() { return baseStation; }
    public List<VehicleState> getVehicles() { return vehicles; }

    public double totalCpu() {
        double sum = 0.0;
        if (baseStation != null) sum += baseStation.getCpuCapacity();
        for (VehicleState v : vehicles) sum += v.getCpuCapacity();
        return sum;
    }

    public double totalStorage() {
        double sum = 0.0;
        if (baseStation != null) sum += baseStation.getStorage();
        for (VehicleState v : vehicles) sum += v.getStorage();
        return sum;
    }
}

what does this code do?
