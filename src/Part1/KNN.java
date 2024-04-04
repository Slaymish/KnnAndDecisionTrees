package Part1;


import java.util.*;

public class KNN {
    List<Instance> trainingInstances;
    int k;

    public KNN(List<Instance> trainingInstances, int k){
        this.trainingInstances = trainingInstances;
        this.k = k;
    }

    /**
     * Classify the testing instances
     * @param testInstances  List of testing instances
     * @return List of classification results
     */
    public List<ClassificationResult> classifyInstances(List<Instance> testInstances) {
        List<ClassificationResult> classifications = new ArrayList<>();
        // Classify the testing instances
        for (Instance instance : testInstances) {
            List<Instance> neighbours = findKNearestNeighbors(instance);
            int predictedClass = determineClassLabel(neighbours);
            classifications.add(new ClassificationResult(predictedClass,
                    neighbours.stream()
                            .map(n -> n.distanceTo(instance)).toList(),
                    instance.getInstanceClass()));

        }

        return classifications;
    }

    /**
     * Find the k nearest neighbours to the instance
     *
     * @param instance Utility.Instance to find them around
     * @return List of nearest neighbours
     */
    private List<Instance> findKNearestNeighbors(Instance instance) {
        List<Instance> sortedList = trainingInstances.stream()
                .sorted(Comparator.comparing(i -> i.distanceTo(instance)))
                .toList();

        return sortedList.subList(0, k);
    }

    /**
     * Determine the class label of the instance based on the neighbours
     *
     * @param neighbours List of neighbours
     * @return The class label
     */
    private int determineClassLabel(List<Instance> neighbours) {
        Map<Integer, Integer> classesCount = new HashMap<>();
        for (Instance neighbour : neighbours) {
            int instanceClass = neighbour.getInstanceClass();
            classesCount.put(instanceClass, classesCount.getOrDefault(instanceClass, 0) + 1);
        }

        // Return the class with the most occurrences
        return Collections.max(classesCount.entrySet(), Map.Entry.comparingByValue()).getKey();
    }

    /**
     * Calculate the accuracy of the model
     *
     * @param predictions  List of predicted classes
     * @return The accuracy
     */
    public double calculateAccuracy(List<ClassificationResult> predictions) {
        int correct = 0;
        for (ClassificationResult prediction : predictions) {
            if (prediction.predictedClass() == prediction.trueClass()) {
                correct++; // Correct classification
            }
        }

        // TP + TN / Total
        System.out.println("Correct: " + correct);
        return (double) correct / predictions.size();
    }
}
