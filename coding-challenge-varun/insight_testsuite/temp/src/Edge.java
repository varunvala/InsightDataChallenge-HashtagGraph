

public class Edge {

    private Vertex one, two;
   
    public Edge(Vertex one, Vertex two){
    	this.one = one;
        this.two = two;
    }
    
    public String toString(){
        return "(" + one + " ," + two + ")";
    }

    public boolean equals(Object other){
        if(!(other instanceof Edge)){
            return false;
        }
        Edge e = (Edge)other;
        return e.one.equals(this.one) && e.two.equals(this.two);
    }
    
    public int hashCode(){
        return (one.getLabel() + two.getLabel()).hashCode(); 
    }
}


