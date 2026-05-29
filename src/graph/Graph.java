// Graph
package graph;

import java.util.*;

public class Graph<T> {
    private Map<Integer, GraphNode<T>> nodes;
    private int nextId;
    
    public Graph() {
        this.nodes = new HashMap<>();
        this.nextId = 0;
    }
    
    // Agregar nodo
    public GraphNode<T> addNode(T data) {
        GraphNode<T> node = new GraphNode<>(nextId++, data);
        nodes.put(node.getId(), node);
        return node;
    }

    
    // Agregar nodo con ID específico
    public GraphNode<T> addNode(int id, T data) {
        GraphNode<T> node = new GraphNode<>(id, data);
        nodes.put(id, node);
        return node;
    }

    
    // Obtener nodo por ID
    public GraphNode<T> getNode(int id) {
        return nodes.get(id);
    }
    
    // Agregar arista
    public void addEdge(int sourceId, int destinationId, String relationType) {
        GraphNode<T> source = nodes.get(sourceId);
        GraphNode<T> destination = nodes.get(destinationId);
        if (source != null && destination != null) {
            source.addEdge(destination, relationType);
        }
    }
    
    // Agregar arista entre nodos existentes
    public void addEdge(GraphNode<T> source, GraphNode<T> destination, String relationType) {
        source.addEdge(destination, relationType);
    }
    
    // BFS (Búsqueda en amplitud) para recorrer el grafo
    public List<GraphNode<T>> bfs(int startId) {
        List<GraphNode<T>> result = new ArrayList<>();
        Set<Integer> visited = new HashSet<>();
        Queue<GraphNode<T>> queue = new LinkedList<>();
        
        GraphNode<T> startNode = nodes.get(startId);
        if (startNode == null) return result;
        
        queue.add(startNode);
        visited.add(startNode.getId());
        
        while (!queue.isEmpty()) {
            GraphNode<T> current = queue.poll();
            result.add(current);
            
            for (GraphEdge<T> edge : current.getEdges()) {
                GraphNode<T> neighbor = edge.getDestination();
                if (!visited.contains(neighbor.getId())) {
                    visited.add(neighbor.getId());
                    queue.add(neighbor);
                }
            }
        }
        return result;
    }
    
    // DFS (Búsqueda en profundidad)
    public List<GraphNode<T>> dfs(int startId) {
        List<GraphNode<T>> result = new ArrayList<>();
        Set<Integer> visited = new HashSet<>();
        dfsRecursive(nodes.get(startId), visited, result);
        return result;
    }
    
    private void dfsRecursive(GraphNode<T> node, Set<Integer> visited, List<GraphNode<T>> result) {
        if (node == null || visited.contains(node.getId())) return;
        
        visited.add(node.getId());
        result.add(node);
        
        for (GraphEdge<T> edge : node.getEdges()) {
            dfsRecursive(edge.getDestination(), visited, result);
        }
    }
    
    // Encontrar camino entre dos nodos
    public List<GraphNode<T>> findPath(int startId, int endId) {
        Map<Integer, Integer> parent = new HashMap<>();
        Set<Integer> visited = new HashSet<>();
        Queue<Integer> queue = new LinkedList<>();
        
        queue.add(startId);
        visited.add(startId);
        parent.put(startId, null);
        
        while (!queue.isEmpty()) {
            int currentId = queue.poll();
            
            if (currentId == endId) {
                return reconstructPath(parent, startId, endId);
            }
            
            GraphNode<T> currentNode = nodes.get(currentId);
            for (GraphEdge<T> edge : currentNode.getEdges()) {
                int neighborId = edge.getDestination().getId();
                if (!visited.contains(neighborId)) {
                    visited.add(neighborId);
                    parent.put(neighborId, currentId);
                    queue.add(neighborId);
                }
            }
        }
        return null; // No hay camino
    }
    
    private List<GraphNode<T>> reconstructPath(Map<Integer, Integer> parent, int startId, int endId) {
        List<GraphNode<T>> path = new ArrayList<>();
        Integer current = endId;
        while (current != null) {
            path.add(0, nodes.get(current));
            current = parent.get(current);
        }
        return path;
    }
    
    // Mostrar estructura del grafo
    public void display() {
        System.out.println("\n ESTRUCTURA DEL GRAFO:");
        for (GraphNode<T> node : nodes.values()) {
            System.out.print("   " + node.getData());
            if (!node.getEdges().isEmpty()) {
                System.out.print(" → ");
                for (int i = 0; i < node.getEdges().size(); i++) {
                    GraphEdge<T> edge = node.getEdges().get(i);
                    System.out.print("[" + edge.getRelationType() + "] " + edge.getDestination().getData());
                    if (i < node.getEdges().size() - 1) System.out.print(", ");
                }
            }
            System.out.println();
        }
    }
    
    public Collection<GraphNode<T>> getAllNodes() {
        return nodes.values();
    }
    
    public int getSize() { return nodes.size(); }
}