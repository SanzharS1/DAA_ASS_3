import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.*;

public class Graph {
    @JsonProperty("id")
    private int id;
    
    @JsonProperty("nodes")
    private List<String> nodes;
    
    @JsonProperty("edges")
    private List<Edge> edges;
    
    private Map<String, List<Edge>> adjacencyList;

    public Graph() {
        this.nodes = new ArrayList<>();
        this.edges = new ArrayList<>();
        this.adjacencyList = new HashMap<>();
    }

    public Graph(int id, List<String> nodes, List<Edge> edges) {
        this.id = id;
        this.nodes = nodes;
        this.edges = edges;
        buildAdjacencyList();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<String> getNodes() {
        return nodes;
    }

    public void setNodes(List<String> nodes) {
        this.nodes = nodes;
        buildAdjacencyList();
    }

    public List<Edge> getEdges() {
        return edges;
    }

    public void setEdges(List<Edge> edges) {
        this.edges = edges;
        buildAdjacencyList();
    }

    @JsonIgnore
    public int getVertexCount() {
        return nodes.size();
    }

    @JsonIgnore
    public int getEdgeCount() {
        return edges.size();
    }

    @JsonIgnore
    public Map<String, List<Edge>> getAdjacencyList() {
        if (adjacencyList == null || adjacencyList.isEmpty()) {
            buildAdjacencyList();
        }
        return adjacencyList;
    }


    private void buildAdjacencyList() {
        adjacencyList = new HashMap<>();
        
        for (String node : nodes) {
            adjacencyList.put(node, new ArrayList<>());
        }
        
        for (Edge edge : edges) {
            adjacencyList.get(edge.getFrom()).add(edge);
            adjacencyList.get(edge.getTo()).add(new Edge(edge.getTo(), edge.getFrom(), edge.getWeight()));
        }
    }

    @JsonIgnore
    public boolean isConnected() {
        if (nodes.isEmpty()) {
            return true;
        }
        
        Set<String> visited = new HashSet<>();
        Queue<String> queue = new LinkedList<>();
        
        String startNode = nodes.get(0);
        queue.offer(startNode);
        visited.add(startNode);
        
        while (!queue.isEmpty()) {
            String current = queue.poll();
            
            for (Edge edge : getAdjacencyList().get(current)) {
                if (!visited.contains(edge.getTo())) {
                    visited.add(edge.getTo());
                    queue.offer(edge.getTo());
                }
            }
        }
        
        return visited.size() == nodes.size();
    }

    @Override
    public String toString() {
        return String.format("Graph{id=%d, vertices=%d, edges=%d}", id, getVertexCount(), getEdgeCount());
    }
}

