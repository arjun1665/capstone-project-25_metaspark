package org.fog.marina;

public class BaseStation {
    private String id;
    private double cpuCapacity;
    private double storage;

    public BaseStation(String id, double cpuCapacity, double storage) {
        this.id = id;
        this.cpuCapacity = cpuCapacity;
        this.storage = storage;
    }

    public String getId() { return id; }
    public double getCpuCapacity() { return cpuCapacity; }
    public double getStorage() { return storage; }

    public boolean canProcess(Task task) {
        return task.getCpu() <= cpuCapacity && task.getSize() <= storage;
    }

    public void assignTask(Task task) {
        System.out.println("Task " + task.getId() + " assigned to Base Station " + id);
    }
}
