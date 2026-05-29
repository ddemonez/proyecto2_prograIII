// GraphEdge
package graph;

public class GraphEdge<T> {
    private GraphNode<T> source;
    private GraphNode<T> destination;
    private String relationType; // Ej: "FACTURA", "DETALLE", "PRODUCTO", "MARCA"
    
    public GraphEdge(GraphNode<T> source, GraphNode<T> destination, String relationType) {
        this.source = source;
        this.destination = destination;
        this.relationType = relationType;
    }
    
    public GraphNode<T> getSource() { return source; }
    public GraphNode<T> getDestination() { return destination; }
    public String getRelationType() { return relationType; }
    
    @Override
    public String toString() {
        return source.getData() + " --[" + relationType + "]--> " + destination.getData();
    }
}