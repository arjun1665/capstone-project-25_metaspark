package org.fog.marina;

import java.io.*;
import java.util.*;

public class ResourcePredictor {

    public static Map<String, Map<Integer,double[]>> loadPredictions(String filePath) {
        Map<String, Map<Integer,double[]>> predictions = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line = br.readLine(); 
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split(",");
                if (parts.length < 6) continue;
                String vehicleId = parts[0].trim();
                int offset = Integer.parseInt(parts[1].trim());
                double predX = Double.parseDouble(parts[2].trim());
                double predY = Double.parseDouble(parts[3].trim());
                double predSpeed = Double.parseDouble(parts[4].trim());
                double predCpu = Double.parseDouble(parts[5].trim());
                predictions.putIfAbsent(vehicleId, new HashMap<>());
                predictions.get(vehicleId).put(offset, new double[]{predX,predY,predSpeed,predCpu});
            }
        } catch (IOException e) {
            System.err.println("Warning: could not read predictions: " + e.getMessage());
        }
        return predictions;
    }
}
