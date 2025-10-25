import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

public class MSTTest {
    private Graph simpleGraph;
    private Graph mediumGraph;
    private Graph disconnectedGraph;

    @BeforeEach
    public void setUp() {
        // Create simple test graph (Graph 1 from example)
        simpleGraph = createSimpleGraph();
        
        // Create medium test graph (Graph 2 from example)
        mediumGraph = createMediumGraph();
        
        // Create disconnected graph for testing
        disconnectedGraph = createDisconnectedGraph();
    }

    private Graph createSimpleGraph() {
        List<String> nodes = Arrays.asList("A", "B", "C", "D", "E");
        List<Edge> edges = Arrays.asList(
            new Edge("A", "B", 4),
            new Edge("A", "C", 3),
            new Edge("B", "C", 2),
            new Edge("B", "D", 5),
            new Edge("C", "D", 7),
            new Edge("C", "E", 8),
            new Edge("D", "E", 6)
        );
        return new Graph(1, nodes, edges);
    }

    private Graph createMediumGraph() {
        List<String> nodes = Arrays.asList("A", "B", "C", "D");
        List<Edge> edges = Arrays.asList(
            new Edge("A", "B", 1),
            new Edge("A", "C", 4),
            new Edge("B", "C", 2),
            new Edge("C", "D", 3),
            new Edge("B", "D", 5)
        );
        return new Graph(2, nodes, edges);
    }

    private Graph createDisconnectedGraph() {
        List<String> nodes = Arrays.asList("A", "B", "C", "D");
        List<Edge> edges = Arrays.asList(
            new Edge("A", "B", 1),
            new Edge("C", "D", 2)
        );
        return new Graph(3, nodes, edges);
    }

    @Test
    @DisplayName("Test Prim's algorithm on simple graph")
    public void testPrimSimpleGraph() {
        PrimMST primMST = new PrimMST();
        MSTResult result = primMST.findMST(simpleGraph);
        
        assertNotNull(result);
        assertEquals(16, result.getTotalCost(), "Total cost should be 16");
        assertEquals(4, result.getMstEdges().size(), "MST should have V-1 = 4 edges");
        assertTrue(result.getOperationsCount() > 0, "Operations count should be positive");
        assertTrue(result.getExecutionTimeMs() >= 0, "Execution time should be non-negative");
    }

    @Test
    @DisplayName("Test Kruskal's algorithm on simple graph")
    public void testKruskalSimpleGraph() {
        KruskalMST kruskalMST = new KruskalMST();
        MSTResult result = kruskalMST.findMST(simpleGraph);
        
        assertNotNull(result);
        assertEquals(16, result.getTotalCost(), "Total cost should be 16");
        assertEquals(4, result.getMstEdges().size(), "MST should have V-1 = 4 edges");
        assertTrue(result.getOperationsCount() > 0, "Operations count should be positive");
        assertTrue(result.getExecutionTimeMs() >= 0, "Execution time should be non-negative");
    }

    @Test
    @DisplayName("Test Prim's algorithm on medium graph")
    public void testPrimMediumGraph() {
        PrimMST primMST = new PrimMST();
        MSTResult result = primMST.findMST(mediumGraph);
        
        assertNotNull(result);
        assertEquals(6, result.getTotalCost(), "Total cost should be 6");
        assertEquals(3, result.getMstEdges().size(), "MST should have V-1 = 3 edges");
    }

    @Test
    @DisplayName("Test Kruskal's algorithm on medium graph")
    public void testKruskalMediumGraph() {
        KruskalMST kruskalMST = new KruskalMST();
        MSTResult result = kruskalMST.findMST(mediumGraph);
        
        assertNotNull(result);
        assertEquals(6, result.getTotalCost(), "Total cost should be 6");
        assertEquals(3, result.getMstEdges().size(), "MST should have V-1 = 3 edges");
    }

    @Test
    @DisplayName("Test both algorithms produce same cost for simple graph")
    public void testAlgorithmsMatchSimpleGraph() {
        PrimMST primMST = new PrimMST();
        KruskalMST kruskalMST = new KruskalMST();
        
        MSTResult primResult = primMST.findMST(simpleGraph);
        MSTResult kruskalResult = kruskalMST.findMST(simpleGraph);
        
        assertEquals(primResult.getTotalCost(), kruskalResult.getTotalCost(),
                "Prim and Kruskal should produce same total cost");
        assertEquals(primResult.getMstEdges().size(), kruskalResult.getMstEdges().size(),
                "Prim and Kruskal should produce same number of edges");
    }

    @Test
    @DisplayName("Test both algorithms produce same cost for medium graph")
    public void testAlgorithmsMatchMediumGraph() {
        PrimMST primMST = new PrimMST();
        KruskalMST kruskalMST = new KruskalMST();
        
        MSTResult primResult = primMST.findMST(mediumGraph);
        MSTResult kruskalResult = kruskalMST.findMST(mediumGraph);
        
        assertEquals(primResult.getTotalCost(), kruskalResult.getTotalCost(),
                "Prim and Kruskal should produce same total cost");
        assertEquals(primResult.getMstEdges().size(), kruskalResult.getMstEdges().size(),
                "Prim and Kruskal should produce same number of edges");
    }

    @Test
    @DisplayName("Test MST has V-1 edges")
    public void testMSTEdgeCount() {
        PrimMST primMST = new PrimMST();
        KruskalMST kruskalMST = new KruskalMST();
        
        MSTResult primResult = primMST.findMST(simpleGraph);
        MSTResult kruskalResult = kruskalMST.findMST(simpleGraph);
        
        int expectedEdges = simpleGraph.getVertexCount() - 1;
        assertEquals(expectedEdges, primResult.getMstEdges().size(),
                "Prim's MST should have V-1 edges");
        assertEquals(expectedEdges, kruskalResult.getMstEdges().size(),
                "Kruskal's MST should have V-1 edges");
    }

    @Test
    @DisplayName("Test MST is acyclic (no cycles)")
    public void testMSTAcyclic() {
        PrimMST primMST = new PrimMST();
        MSTResult result = primMST.findMST(simpleGraph);
        
        assertTrue(isAcyclic(result.getMstEdges(), simpleGraph.getNodes()),
                "MST should not contain cycles");
    }

    @Test
    @DisplayName("Test MST connects all vertices")
    public void testMSTConnectsAllVertices() {
        PrimMST primMST = new PrimMST();
        MSTResult result = primMST.findMST(simpleGraph);
        
        assertTrue(isConnected(result.getMstEdges(), simpleGraph.getNodes()),
                "MST should connect all vertices");
    }

    @Test
    @DisplayName("Test disconnected graph handling")
    public void testDisconnectedGraph() {
        PrimMST primMST = new PrimMST();
        KruskalMST kruskalMST = new KruskalMST();
        
        MSTResult primResult = primMST.findMST(disconnectedGraph);
        MSTResult kruskalResult = kruskalMST.findMST(disconnectedGraph);
        
        assertEquals(-1, primResult.getTotalCost(),
                "Prim's algorithm should return -1 for disconnected graph");
        assertEquals(-1, kruskalResult.getTotalCost(),
                "Kruskal's algorithm should return -1 for disconnected graph");
    }

    @Test
    @DisplayName("Test empty graph")
    public void testEmptyGraph() {
        Graph emptyGraph = new Graph(99, new ArrayList<>(), new ArrayList<>());
        
        PrimMST primMST = new PrimMST();
        KruskalMST kruskalMST = new KruskalMST();
        
        MSTResult primResult = primMST.findMST(emptyGraph);
        MSTResult kruskalResult = kruskalMST.findMST(emptyGraph);
        
        assertEquals(0, primResult.getTotalCost());
        assertEquals(0, kruskalResult.getTotalCost());
        assertEquals(0, primResult.getMstEdges().size());
        assertEquals(0, kruskalResult.getMstEdges().size());
    }

    @Test
    @DisplayName("Test execution time is measured")
    public void testExecutionTimeMeasured() {
        PrimMST primMST = new PrimMST();
        KruskalMST kruskalMST = new KruskalMST();
        
        MSTResult primResult = primMST.findMST(simpleGraph);
        MSTResult kruskalResult = kruskalMST.findMST(simpleGraph);
        
        assertTrue(primResult.getExecutionTimeMs() >= 0,
                "Execution time should be non-negative");
        assertTrue(kruskalResult.getExecutionTimeMs() >= 0,
                "Execution time should be non-negative");
    }

    @Test
    @DisplayName("Test operation counts are positive")
    public void testOperationCounts() {
        PrimMST primMST = new PrimMST();
        KruskalMST kruskalMST = new KruskalMST();
        
        MSTResult primResult = primMST.findMST(simpleGraph);
        MSTResult kruskalResult = kruskalMST.findMST(simpleGraph);
        
        assertTrue(primResult.getOperationsCount() > 0,
                "Prim's operations count should be positive");
        assertTrue(kruskalResult.getOperationsCount() > 0,
                "Kruskal's operations count should be positive");
    }

    /**
     * Helper method to check if MST edges form an acyclic graph.
     */
    private boolean isAcyclic(List<Edge> edges, List<String> nodes) {
        UnionFind uf = new UnionFind();
        for (String node : nodes) {
            uf.makeSet(node);
        }
        
        for (Edge edge : edges) {
            String root1 = uf.find(edge.getFrom());
            String root2 = uf.find(edge.getTo());
            
            if (root1.equals(root2)) {
                return false; // Cycle detected
            }
            uf.union(edge.getFrom(), edge.getTo());
        }
        
        return true;
    }

    /**
     * Helper method to check if MST edges connect all vertices.
     */
    private boolean isConnected(List<Edge> edges, List<String> nodes) {
        if (nodes.isEmpty()) {
            return true;
        }
        
        Map<String, List<String>> adjacencyList = new HashMap<>();
        for (String node : nodes) {
            adjacencyList.put(node, new ArrayList<>());
        }
        
        for (Edge edge : edges) {
            adjacencyList.get(edge.getFrom()).add(edge.getTo());
            adjacencyList.get(edge.getTo()).add(edge.getFrom());
        }
        
        Set<String> visited = new HashSet<>();
        Queue<String> queue = new LinkedList<>();
        queue.offer(nodes.get(0));
        visited.add(nodes.get(0));
        
        while (!queue.isEmpty()) {
            String current = queue.poll();
            for (String neighbor : adjacencyList.get(current)) {
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    queue.offer(neighbor);
                }
            }
        }
        
        return visited.size() == nodes.size();
    }
}

