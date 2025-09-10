package org.fog.test;

import org.fog.marina.*;
import org.fog.placement.*;
import java.util.*;

public class TestMARINA {
    public static void main(String[] args) {
        String vehicleTraceFile = "dataset/sample_vehicle_trace.csv";
        List<VehicleState> allVehicles = TraceLoader.loadVehicleTrace(vehicleTraceFile);

        List<BaseStation> baseStations = new ArrayList<>();
        baseStations.add(new BaseStation("BS1", 50, 200));
        baseStations.add(new BaseStation("BS2", 70, 300));

        int maxTime = allVehicles.stream().mapToInt(VehicleState::getTime).max().orElse(0);
        for (int time = 0; time <= maxTime; time++) {
            System.out.println("=== Scheduling at time " + time + " ===");

            List<VehicleState> vehicles = TraceLoader.getVehiclesAtTime(allVehicles, time);

            List<Task> tasks = new ArrayList<>();
            for (VehicleState v : vehicles) {
                String taskId = "Task_" + v.getId() + "_t" + time;
                double size = 10 + (v.getSpeed() % 5); 
                double cpu = Math.min(v.getCpuCapacity() * 0.2, 100); 
                double deadline = 5 + (50.0 / (v.getSpeed() + 1)); 
                tasks.add(new Task(taskId, size, cpu, deadline));
            }
            MARINAScheduler scheduler = new MARINAScheduler(vehicles, baseStations);
            scheduler.schedule(tasks);
        }
    }
}
