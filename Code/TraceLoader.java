package org.fog.marina;

import java.io.*;
import java.util.*;

public class TraceLoader {
    public static List<VehicleState> loadVehicleTrace(String filePath) {
        List<VehicleState> vehicles = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line = br.readLine(); // skip header
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split(",");

                int time = Integer.parseInt(parts[0]);
                String vehicleId = parts[1];
                double x = Double.parseDouble(parts[2]);
                double y = Double.parseDouble(parts[3]);
                double speed = Double.parseDouble(parts[4]);
                double cpu = Double.parseDouble(parts[5]);

                vehicles.add(new VehicleState(time, vehicleId, x, y, speed, cpu, 100)); // storage dummy=100
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return vehicles;
    }

    public static List<VehicleState> getVehiclesAtTime(List<VehicleState> all, int time) {
        List<VehicleState> snapshot = new ArrayList<>();
        for (VehicleState v : all) {
            if (v.getTime() == time) {
                snapshot.add(v);
            }
        }
        return snapshot;
    }
}
