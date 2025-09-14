package org.fog.marina;

import java.util.ArrayList;
import java.util.List;

public class VehicularCloud {
    private int id;
    private List<VehicleState> members;

    public VehicularCloud(int id) {
        this.id = id;
        this.members = new ArrayList<>();
    }

    public void addMember(VehicleState v) {
        members.add(v);
    }

    public List<VehicleState> getMembers() {
        return members;
    }

    public double getTotalCpu() {
        return members.stream().mapToDouble(VehicleState::getCpuCapacity).sum();
    }

    public int getSize() {
        return members.size();
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("VC-").append(id).append(" { ");
        for (VehicleState v : members) {
            sb.append("Veh").append(v.getId()).append(" ");
        }
        sb.append("} | Total CPU: ").append(getTotalCpu());
        return sb.toString();
    }
}
