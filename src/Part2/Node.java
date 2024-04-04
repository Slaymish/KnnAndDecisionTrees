package Part2;

import java.util.ArrayList;
import java.util.List;

public class Node {
    public List<Instance> instances;
    public List<Node> children = new ArrayList<>();
    public String splitAttribute;

    public Node (Node parent){
        this.instances = parent.instances;
        this.children = parent.children;
    }

    public Node(List<Instance> children){
        this.instances = children;
    }


    /**
     * Split the node based on the attribute
     *
     * @param attribute The attribute to split on
     */
    public void split(String attribute){
        List<Instance> yes = instances.stream()
                .filter(i -> i.getAttributes().get(attribute) > 0)
                .toList();

        List<Instance> no = instances.stream()
                .filter(i -> i.getAttributes().get(attribute) == 0)
                .toList();

        children = List.of(new Node(no), new Node(yes));
        splitAttribute = attribute;
    }

    public int[] getClassCounts(){
        int[] counts = new int[2];
        counts[0] = (int) instances.stream().filter(i -> i.getInstanceClass() == 0).count();
        counts[1] = (int) instances.stream().filter(i -> i.getInstanceClass() == 1).count();
        return counts;
    }


}
