// GraphNode
package graph;

import java.util.ArrayList;
import java.util.List;

public class GraphNode<T> {
    private T data;
    private int id;
    private List<GraphEdge<T>> edges;
    
    public GraphNode(int id, T data) {
        this.id = id;
        this.data = data;
        this.edges = new ArrayList<>();
    }
    
    public int getId() { return id; }
    public T getData() { return data; }
    public void setData(T data) { this.data = data; }
    public List<GraphEdge<T>> getEdges() { return edges; }
    
    public void addEdge(GraphNode<T> destination, String relationType) {
        edges.add(new GraphEdge<>(this, destination, relationType));
    }
    
    @Override
    public String toString() {
        return data.toString();
    }
}