package Part2;

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
}
