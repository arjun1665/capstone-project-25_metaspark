package org.fog.marina;

public class VehicleState {
    private String id;
    private int time;
    private double x;
    private double y;
    private double speed;
    private double cpuCapacity;
    private double storage;
    private boolean available;

    public VehicleState(int time, String id, double x, double y, double speed, double cpuCapacity, double storage) {
        this.time = time;
        this.id = id;
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.cpuCapacity = cpuCapacity;
        this.storage = storage;
        this.available = true;
    }

    public int getTime() { return time; }
    public String getId() { return id; }
    public double getX() { return x; }
    public double getY() { return y; }
    public double getSpeed() { return speed; }
    public double getCpuCapacity() { return cpuCapacity; }
    public double getStorage() { return storage; }
    public boolean isAvailable() { return available; }

    public void setAvailable(boolean available) { this.available = available; }

    public boolean canProcess(Task task) {
        return available && task.getCpu() <= cpuCapacity && task.getSize() <= storage;
    }

    public void assignTask(Task task) {
        this.available = false;
        System.out.println("Task " + task.getId() + " assigned to Vehicle " + id + " at time " + time);
    }
}
