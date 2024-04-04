package Part1;

import java.io.File;
import java.util.List;

public class Main {

    /**
     * Run the model
     * <p>
     * uses varargs
     *
     * @param args train_path testing_path output_path num_of_neighbours
     */
    public static void main(String[] args) {
        if (args.length != 4) {
            System.out.println("Expected 4 arguments but got " + args.length);
            return;
        }

        String trainingPath = args[0];
        String testingPath = args[1];
        String outputPath = args[2];
        int k = Integer.parseInt(args[3]);

        if (k < 1) {
            System.out.println("K must be greater than 0");
            return;
        }

        // Load the training data
        File trainingFile = new File(trainingPath);
        if (!trainingFile.exists()) {
            System.out.println("Training file does not exist");
            return;
        }

        // Load the testing data
        File testingFile = new File(testingPath);
        if (!testingFile.exists()) {
            System.out.println("Testing file does not exist");
            return;
        }

        // Check the output directory
        File outputFile = new File(outputPath);
        if (!outputFile.getParentFile().exists()) {
            System.out.println("Output file does not exist");
            return;
        }

        DataSet normalisedDataSet = new DataSet()
                .loadTrainingSet(trainingFile)
                .loadTestingSet(testingFile)
                .normaliseDataSet();


        KNN knn = new KNN(normalisedDataSet.getTrainingSet(), k);

        List<ClassificationResult> k1Predictions = knn.classifyInstances(normalisedDataSet.getTestingSet());

        normalisedDataSet.outputToFile(k1Predictions, k, outputFile);

        // Print the accuracy
        System.out.printf("Accuracy for K=%d: %.4f%%\n", k, knn.calculateAccuracy(k1Predictions) * 100);
        System.out.println("Predictions written to " + outputPath);
        System.out.println(normalisedDataSet.getTestingSet().size());
    }
}


