package Part1;

import java.util.List;

public record ClassificationResult(
        int predictedClass,
        List<Double> neighbourDistances,
        int trueClass) {}
