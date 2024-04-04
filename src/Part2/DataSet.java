package Part2;

import Part1.ClassificationResult;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class DataSet {

    public DataSet() {}

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
}