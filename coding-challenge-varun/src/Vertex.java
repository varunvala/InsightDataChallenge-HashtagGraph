

import java.util.ArrayList;

public class Vertex {

	private String label;
    private ArrayList<Edge> neighbourList;
    
    public Vertex(String label){
        this.label = label;
        this.neighbourList = new ArrayList<Edge>();
    }
    
    public boolean containsNeighbor(Edge other){
        return this.neighbourList.contains(other);
    }
    
    public void addNeighbor(Edge edge){
        if(this.neighbourList.contains(edge)){
            return;
        }
        this.neighbourList.add(edge);
    }
    
    public int getNeighborCount(){
        return this.neighbourList.size();
    }
    
    public String getLabel(){
        return this.label;
    }
    
    public String toString(){
        return "Vertex:: " + label;
    }

    public boolean equals(Object other){
        if(!(other instanceof Vertex)){
            return false;
        }
        Vertex v = (Vertex)other;
        return this.label.equals(v.label);
    }
    
    public int hashCode(){
        return this.label.hashCode();
    }
    
}