package Part2;

import java.io.File;
import java.io.FileWriter;
import java.util.*;

public class DecisionTree {
    public Node root;
    double threshold = 0.00001;


    public DecisionTree(List<Instance> instances) {
        root = new Node(instances);
    }

    public void growTree() {
        if (root.instances.isEmpty()) {
            throw new IllegalArgumentException("Empty instances list");
        }
        Set<String> attributes = new HashSet<>(root.instances.get(0).getAttributes().keySet());
        growTreeHelper(root, attributes);
    }

    /**
     * Print the tree
     *
     * @param node  The node to print
     * @param indent The indentation
     */
    public void printTree(Node node, String indent) {
        if (node.children.isEmpty()) {
            System.out.printf("%sLeaf: Classes (%d,%d) [entropy: %f]\n"
                    ,indent,node.getClassCounts()[0],node.getClassCounts()[1],calculateEntropy(node.getClassCounts()));
            return;
        }

        System.out.printf("%sSplit on %s (%d,%d) [entropy: %f, gain: %f]\n"
                ,indent,node.splitAttribute,node.getClassCounts()[0],node.getClassCounts()[1],calculateEntropy(node.getClassCounts()),informationGain(node,node.children));

        for (Node child : node.children) {
            printTree(child, indent + "|--");
        }
    }

    /**
     * Split the node into children based on the attribute
     *
     * @param node      The node to split
     * @param attributes The attributes to split on
     */
    private void growTreeHelper(Node node, Set<String> attributes) {
        String attributeToSplit = null;
        double maxGain = 0;

        // find the attribute with the highest information gain
        for (String attribute : attributes) {
            Node temp = new Node(node);
            temp.split(attribute);
            double gain = informationGain(temp, temp.children);
            if (gain > maxGain) {
                maxGain = gain;
                attributeToSplit = attribute;
            }
        }

        if (maxGain < threshold) {
            return;
        }

        node.split(attributeToSplit);
        Set<String> newAttributes = new HashSet<>(attributes);
        newAttributes.remove(attributeToSplit);

        for (Node child : node.children) {
            growTreeHelper(child, newAttributes);
        }
    }

    /**
     * Class representing a node in the decision tree
     *
     * @param args
     */
    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: DecisionTree <dataset> <output>");
            System.exit(1);
        }

        List<Instance> instances = DataSet.loadData(new File(args[0]));
        String output = args[1];

        DecisionTree dt = new DecisionTree(instances);
        dt.growTree();

        dt.printTree(dt.root, "");
        outputToTextFile(dt.root, "", output);
    }


    /**
     * Calculate the information gain of a particular split
     *
     * @param parentNode    The parent node
     * @param childrenNodes The children nodes
     * @return Information gain
     */
    public static double informationGain(Node parentNode, List<Node> childrenNodes) {
        double parentEntropy = calculateEntropy(parentNode.getClassCounts());

        // for weighting
        List<Double> childNum = childrenNodes.stream().map(c -> (double) c.instances.size()).toList();

        double parentNum = parentNode.instances.size();
        int numOfChildren = childrenNodes.size();

        double sum = 0;
        for (int i = 0; i < numOfChildren; i++) {
            double weight = childNum.get(i) / parentNum;
            double childEntropy = calculateEntropy(childrenNodes.get(i).getClassCounts());
            sum += weight * childEntropy;
        }

        return parentEntropy - sum;
    }

    /**
     * Calculate the entropy of a set of classes
     *
     *
     *
     * @param classCounts the counts of each class
     * @return the entropy of the set
     */
    public static double calculateEntropy(int[] classCounts) {
        double[] classProportions = classCountsToProportions(classCounts);

        double entropy = 0;
        for (double classProportion : classProportions) {
            if (classProportion == 0) continue;
            entropy += (Math.log(classProportion) / Math.log(2)) * classProportion;
        }

        return entropy == 0 ? 0 : -entropy;
    }

    /**
     * Convert a set of class counts to proportions
     *
     * @param classCounts the counts of each class
     * @return the proportions of each class (sum to 1)
     */
    public static double[] classCountsToProportions(int[] classCounts) {
        double total = Arrays.stream(classCounts).sum();
        return Arrays.stream(classCounts).mapToDouble(c -> c / total).toArray();
    }

    public static double calculateAccuracy(Node node, List<Instance> instances) {
        int correct = 0;
        for (Instance instance : instances) {
            int predictedClass = classify(node, instance);
            if (predictedClass == instance.getInstanceClass()) {
                correct++;
            }
        }

        return (double) correct / instances.size();
    }

    /**
     * Output the tree to a text file
     *
     * @param node
     * @param indent
     * @param filename
     */
    public static void outputToTextFile(Node node, String indent, String filename) {
        try {
            File file = new File(filename);
            FileWriter writer = new FileWriter(file);
            outputToTextFileHelper(node, indent, writer);
            double accuracy = calculateAccuracy(node, node.instances);
            writer.write(String.format("Accuracy: %.2f\n", accuracy * 100));
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Helper function to output the tree to a text file
     *
     * @param node
     * @param indent
     * @param writer
     */
    private static void outputToTextFileHelper(Node node, String indent, FileWriter writer) {
        try {
            if (node.children.isEmpty()) {
                writer.write(String.format("%sLeaf: Classes (%d,%d) [entropy: %f]\n"
                        , indent, node.getClassCounts()[0], node.getClassCounts()[1], calculateEntropy(node.getClassCounts())));
                return;
            }

            writer.write(String.format("%sSplit on %s (%d,%d) [entropy: %f, gain: %f]\n"
                    , indent, node.splitAttribute, node.getClassCounts()[0], node.getClassCounts()[1], calculateEntropy(node.getClassCounts()), informationGain(node, node.children)));

            for (Node child : node.children) {
                outputToTextFileHelper(child, indent + "|--", writer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static int classify(Node node, Instance instance) {
        if (node.children.isEmpty()) { // leaf node
            return node.getClassCounts()[0] > node.getClassCounts()[1] ? 0 : 1;
        }

        String splitAttribute = node.splitAttribute;
        double attributeValue = instance.getAttributes().get(splitAttribute);

        int attributeValueInt = attributeValue > 0 ? 1 : 0;

        Node child = node.children.get(attributeValueInt);
        return classify(child, instance);
    }
}
