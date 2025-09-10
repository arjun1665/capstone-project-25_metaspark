package org.fog.placement;

import org.fog.marina.*;
import java.util.*;

public class MARINAScheduler {

    private List<VehicleState> vehicles;
    private List<BaseStation> baseStations;

    public MARINAScheduler(List<VehicleState> vehicles, List<BaseStation> baseStations) {
        this.vehicles = vehicles;
        this.baseStations = baseStations;
    }

    private List<Task> paretoFilter(List<Task> tasks) {
        List<Task> paretoSet = new ArrayList<>();
        for (Task t1 : tasks) {
            boolean dominated = false;
            for (Task t2 : tasks) {
                if (t2.getDeadline() <= t1.getDeadline() && t2.getCpu() <= t1.getCpu()
                        && (t2.getDeadline() < t1.getDeadline() || t2.getCpu() < t1.getCpu())) {
                    dominated = true;
                    break;
                }
            }
            if (!dominated) paretoSet.add(t1);
        }
        return paretoSet;
    }


    private List<Task> binCovering(List<Task> tasks, double capacity) {
        tasks.sort((a, b) -> Double.compare(b.getCpu(), a.getCpu())); 
        List<Task> selected = new ArrayList<>();
        double used = 0;
        for (Task task : tasks) {
            if (used + task.getCpu() <= capacity) {
                selected.add(task);
                used += task.getCpu();
            }
        }
        return selected;
    }


    public void schedule(List<Task> tasks) {
        List<Double> cpuHistory = new ArrayList<>();
        cpuHistory.add(50.0); 
        cpuHistory.add(60.0);
        double predictedCPU = ResourcePredictor.predictAvailableCPU(cpuHistory);
        System.out.println("[Prediction] Predicted available CPU: " + predictedCPU);

        
        List<Task> paretoSet = paretoFilter(tasks);
        int dropped = tasks.size() - paretoSet.size();
        System.out.println("[Pareto] Selected tasks: " + paretoSet.size());
        if (dropped > 0) {
            System.out.print("[MARINA] Dropped " + dropped + " tasks due to Pareto filtering: ");
            for (Task t : tasks) {
                if (!paretoSet.contains(t)) {
                    System.out.print(t.getId() + " ");
                }
            }
            System.out.println();
        }

        
        for (VehicleState v : vehicles) {
            if (!v.isAvailable()) continue;
            List<Task> chosen = binCovering(paretoSet, v.getCpuCapacity());
            for (Task t : chosen) {
                if (t.getDeadline() >= (t.getCpu() / v.getCpuCapacity())) {
                    v.assignTask(t);
                    paretoSet.remove(t);
                }
            }
        }

        
        for (BaseStation bs : baseStations) {
            List<Task> chosen = binCovering(paretoSet, bs.getCpuCapacity());
            for (Task t : chosen) {
                if (t.getDeadline() >= (t.getCpu() / bs.getCpuCapacity())) {
                    bs.assignTask(t);
                    paretoSet.remove(t);
                }
            }
        }

       
        for (Task t : paretoSet) {
            System.out.println("Task " + t.getId() + " could not be scheduled.");
        }
    }
}
