package org.fog.marina;

public class Task {
    private String id;
    private double size;
    private double cpu;       
    private double deadline;   
    private double arrivalTime; 


    private double finishTime;
    private double cost;

    public Task(String id, double size, double cpu, double deadline) {
        this(id, size, cpu, deadline, 0); 
    }

    public Task(String id, double size, double cpu, double deadline, double arrivalTime) {
        this.id = id;
        this.size = size;
        this.cpu = cpu;
        this.deadline = deadline;
        this.arrivalTime = arrivalTime;
        this.finishTime = -1;
        this.cost = 0;
    }


    public String getId() { return id; }
    public double getSize() { return size; }
    public double getCpu() { return cpu; }
    public double getDeadline() { return deadline; }
    public double getArrivalTime() { return arrivalTime; }
    public double getFinishTime() { return finishTime; }
    public double getCost() { return cost; }

  
    public void setFinishTime(double finishTime) { this.finishTime = finishTime; }
    public void setCost(double cost) { this.cost = cost; }

 
    public boolean meetsDeadline() {
        return (finishTime > 0) && (finishTime <= arrivalTime + deadline);
    }

  
    public double computeProcessingTime(double availableCpu) {
        if (availableCpu <= 0) return Double.MAX_VALUE;
        return cpu / availableCpu;
    }
}
