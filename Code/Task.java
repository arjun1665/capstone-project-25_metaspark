package org.fog.marina;

public class Task {
    private String id;
    private double size; 
    private double cpu;  
    private double deadline; 

    public Task(String id, double size, double cpu, double deadline) {
        this.id = id;
        this.size = size;
        this.cpu = cpu;
        this.deadline = deadline;
    }

    public String getId() { return id; }
    public double getSize() { return size; }
    public double getCpu() { return cpu; }
    public double getDeadline() { return deadline; }
}
