package Part1;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class DataSet {
    List<Instance> trainingInstances;
    List<Instance> testingInstances;

    boolean dataNormalised = false;

    Map<String,Double> trainingMin;
    Map<String,Double> trainingMax;

    public DataSet() {
        trainingMin = new HashMap<>();
        trainingMax = new HashMap<>();
    }

    public DataSet normaliseDataSet() {
        if (trainingInstances == null || testingInstances == null) {
            throw new IllegalStateException("Load training and testing data first");
        }

        setRange(trainingInstances);
        normaliseValues(this.testingInstances);
        normaliseValues(this.trainingInstances);
        dataNormalised = true;
        return this;
    }

    private void setRange(List<Instance> instances) {
        for (String attribute : instances.get(0).attributes.keySet()) {
            double max = Double.MIN_VALUE;
            double min = Double.MAX_VALUE;

            // Get min and max for each attribute
            for (Instance instance : instances) {
                double value = instance.attributes.get(attribute);
                if (value > max) {
                    max = value;
                }
                if (value < min) {
                    min = value;
                }
            }

            trainingMax.put(attribute,max);
            trainingMin.put(attribute,min);
        }
    }

    private void normaliseValues(List<Instance> instances) {
        if (trainingMin.isEmpty() || trainingMax.isEmpty())
            throw new IllegalStateException("Set range first");

        for (String attribute : instances.get(0).attributes.keySet()) {
            // Set each value based on that
            for (Instance instance : instances) {
                double value = instance.attributes.get(attribute);

                // Uses the range for the training data for both training and testing sets
                instance.attributes.put(attribute, (value - getMin(attribute)) / (getMax(attribute) - getMin(attribute)));
            }
        }
    }

    public DataSet loadTrainingSet(File trainingFile) {
        this.trainingInstances = loadData(trainingFile);
        return this;
    }

    public DataSet loadTestingSet(File testingFile) {
        this.testingInstances = loadData(testingFile);
        return this;
    }

    public List<Instance> getTrainingSet() {
        return trainingInstances;
    }

    public List<Instance> getTestingSet() {
        return testingInstances;
    }

    /**
     * Load the data from the file into the instances list
     * Last column is the class
     *
     * @param trainingFile The file to load the data from
     */
    public static List<Instance> loadData(File trainingFile) {
        List<Instance> instances = new ArrayList<>();
        try {
            Scanner sc = new Scanner(trainingFile);

            // Get attribute names
            String[] attributes = sc.nextLine().split(",");

            while (sc.hasNextLine()) {
                Instance instance = new Instance();
                String[] values = sc.nextLine().split(",");

                for (int i = 0; i < attributes.length - 1; i++) { // Skip the last class column
                    instance.addAttribute(attributes[i], Double.parseDouble(values[i]));
                }

                // Set the class
                instance.setClass(Integer.parseInt(values[values.length - 1]));

                instances.add(instance);
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return instances;
    }

    private void writeClassificationResult(FileWriter fileWriter, ClassificationResult c) {
        try {
            fileWriter.write(c.trueClass() + "," + c.predictedClass());
            for (Double d : c.neighbourDistances()) {
                fileWriter.write("," + d);
            }
            fileWriter.write("\n");
        } catch (IOException e) {
            // Handle or log the IOException
            System.err.println("Error writing classification result: " + e.getMessage());
        }
    }


    public void outputToFile(List<ClassificationResult> k1Predictions, int k, File outputFile) {
        // true_class, predicted y, distance1, distance2, distance3
        try (FileWriter fileWriter = new FileWriter(outputFile)) {
            StringBuilder header = new StringBuilder("true_class,predicted_class");
            for (int i = 0; i < k; i++) {
                header.append(",distance").append(i + 1);
            }
            fileWriter.write(header + "\n");

            // print each instance
            k1Predictions.forEach(c -> writeClassificationResult(fileWriter, c));
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }


    private double getMin(String attribute) {
        return trainingMin.get(attribute);
    }

    private double getMax(String attribute) {
        return trainingMax.get(attribute);
    }

}