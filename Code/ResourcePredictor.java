package org.fog.marina;

import java.util.*;

public class ResourcePredictor {
    public static double predictAvailableCPU(List<Double> history) {
        if (history.isEmpty()) return 0;
        double sum = 0;
        for (double v : history) sum += v;
        return sum / history.size();
    }
}
