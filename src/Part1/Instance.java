package Part1;

import java.util.HashMap;
import java.util.Map;

public class Instance {
    Map<String, Double> attributes;
    int instanceClass;

    public Instance(){
        attributes = new HashMap<>();
    }

    public Instance addAttribute(String name, double value){
        attributes.put(name,value);
        return this;
    }

    public String toString(){
        //return attributes.toString();
        return attributes.size() + " Attributes, Class: " + instanceClass;
    }

    public int getInstanceClass(){
        return instanceClass;
    }

    public Map<String, Double> getAttributes(){
        return attributes;
    }

    public Instance setClass(int parseDouble) {
        instanceClass = parseDouble;
        return this;
    }

    public double distanceTo(Instance other){
        double sum = 0;
        for (String attribute: attributes.keySet()){
            double a = attributes.get(attribute);
            double b = other.attributes.get(attribute);

            sum += Math.pow(a-b,2);
        }

        return Math.sqrt(sum);
    }

}
