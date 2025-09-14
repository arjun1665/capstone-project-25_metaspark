package org.fog.test;

import org.fog.marina.*;
import org.fog.placement.*;
import java.util.*;

public class TestMARINA {

    // Helper: calculate distance between two vehicles
    private static double distance(VehicleState v1, VehicleState v2) {
        double dx = v1.getX() - v2.getX();
        double dy = v1.getY() - v2.getY();
        return Math.sqrt(dx * dx + dy * dy);
    }

    // Form Vehicular Clouds based on coverage radius
    public static List<VehicularCloud> formVehicularClouds(List<VehicleState> vehicles, double radius) {
        List<VehicularCloud> clouds = new ArrayList<>();
        boolean[] visited = new boolean[vehicles.size()];
        int cloudId = 1;

        for (int i = 0; i < vehicles.size(); i++) {
            if (!visited[i]) {
                VehicularCloud vc = new VehicularCloud(cloudId++);
                VehicleState seed = vehicles.get(i);
                vc.addMember(seed);
                visited[i] = true;

                for (int j = i + 1; j < vehicles.size(); j++) {
                    if (!visited[j] && distance(seed, vehicles.get(j)) <= radius) {
                        vc.addMember(vehicles.get(j));
                        visited[j] = true;
                    }
                }
                clouds.add(vc);
            }
        }
        return clouds;
    }

    public static void main(String[] args) {
        String vehicleTraceFile = "dataset/sample_vehicle_trace.csv";

        // Load full vehicle time series
        List<VehicleState> allVehicles = TraceLoader.loadVehicleTrace(vehicleTraceFile);

        // Create some base stations
        List<BaseStation> baseStations = new ArrayList<>();
        baseStations.add(new BaseStation("BS1", 50, 200));
        baseStations.add(new BaseStation("BS2", 70, 300));

        // Simulate scheduling across all times in trace
        int maxTime = allVehicles.stream().mapToInt(VehicleState::getTime).max().orElse(0);

        for (int time = 0; time <= maxTime; time++) {
            System.out.println("=== Scheduling at time " + time + " ===");

            // Vehicles available at this time
            List<VehicleState> vehicles = TraceLoader.getVehiclesAtTime(allVehicles, time);

            // Generate tasks from vehicles
            List<Task> tasks = new ArrayList<>();
            for (VehicleState v : vehicles) {
                // Example: create 1 task per vehicle at each tick
                String taskId = "Task_" + v.getId() + "_t" + time;

                double size = 10 + (v.getSpeed() % 5);           // MB, e.g. based on speed
                double cpu = Math.min(v.getCpuCapacity() * 0.2, 100); // require 20% of CPU, capped at 100 MIPS
                double deadline = 5 + (50.0 / (v.getSpeed() + 1));   // tighter deadline if slower

                tasks.add(new Task(taskId, size, cpu, deadline));
            }

            // Form Vehicular Clouds with 200m coverage radius
            List<VehicularCloud> clouds = formVehicularClouds(vehicles, 200.0);

            System.out.println("Time Slot " + time + ":");
            for (VehicularCloud vc : clouds) {
                System.out.println("  " + vc.toString());
            }

            // Run MARINA scheduler
            MARINAScheduler scheduler = new MARINAScheduler(vehicles, baseStations);
            scheduler.schedule(tasks);
        }
    }
}
