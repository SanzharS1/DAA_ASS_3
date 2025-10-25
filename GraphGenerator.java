import java.util.*;

public class GraphGenerator {
    private Random random;

    public GraphGenerator(long seed) {
        this.random = new Random(seed);
    }

    public Graph generateConnectedGraph(int id, int numVertices) {
        List<String> nodes = new ArrayList<>();
        
        for (int i = 0; i < numVertices; i++) {
            nodes.add("V" + i);
        }

        List<Edge> edges = new ArrayList<>();
        Set<Integer> inTree = new HashSet<>();
        
        inTree.add(0);

        while (inTree.size() < numVertices) {
            int from = new ArrayList<>(inTree).get(random.nextInt(inTree.size()));
            int to = random.nextInt(numVertices);
            
            if (!inTree.contains(to)) {
                int weight = random.nextInt(100) + 1; // Weight 1-100
                edges.add(new Edge("V" + from, "V" + to, weight));
                inTree.add(to);
            }
        }

        int additionalEdges = numVertices / 4;
        int attempts = 0;
        Set<String> existingEdges = new HashSet<>();
        
        for (Edge edge : edges) {
            existingEdges.add(getEdgeKey(edge.getFrom(), edge.getTo()));
        }

        while (edges.size() < numVertices - 1 + additionalEdges && attempts < additionalEdges * 10) {
            int from = random.nextInt(numVertices);
            int to = random.nextInt(numVertices);
            
            if (from != to) {
                String edgeKey = getEdgeKey("V" + from, "V" + to);
                if (!existingEdges.contains(edgeKey)) {
                    int weight = random.nextInt(100) + 1;
                    edges.add(new Edge("V" + from, "V" + to, weight));
                    existingEdges.add(edgeKey);
                }
            }
            attempts++;
        }

        return new Graph(id, nodes, edges);
    }

    private String getEdgeKey(String from, String to) {
        if (from.compareTo(to) < 0) {
            return from + "-" + to;
        } else {
            return to + "-" + from;
        }
    }


    public static void main(String[] args) {
        GraphGenerator generator = new GraphGenerator(42); // Fixed seed for reproducibility
        
        System.out.println("Generating graphs as per assignment requirements...\n");

        GraphDataLoader.GraphData allData = new GraphDataLoader.GraphData();
        List<Graph> allGraphs = new ArrayList<>();
        
        int graphId = 1;

        // 1. Generate 5 small graphs (30 nodes)
        System.out.println("Generating 5 small graphs (30 nodes)...");
        for (int i = 0; i < 5; i++) {
            Graph g = generator.generateConnectedGraph(graphId++, 30);
            allGraphs.add(g);
            System.out.printf("  Graph %d: %d vertices, %d edges%n", 
                g.getId(), g.getVertexCount(), g.getEdgeCount());
        }

        // 2. Generate 10 medium graphs (300 nodes)
        System.out.println("\nGenerating 10 medium graphs (300 nodes)...");
        for (int i = 0; i < 10; i++) {
            Graph g = generator.generateConnectedGraph(graphId++, 300);
            allGraphs.add(g);
            System.out.printf("  Graph %d: %d vertices, %d edges%n", 
                g.getId(), g.getVertexCount(), g.getEdgeCount());
        }

        // 3. Generate 10 large graphs (1000 nodes)
        System.out.println("\nGenerating 10 large graphs (1000 nodes)...");
        for (int i = 0; i < 10; i++) {
            Graph g = generator.generateConnectedGraph(graphId++, 1000);
            allGraphs.add(g);
            System.out.printf("  Graph %d: %d vertices, %d edges%n", 
                g.getId(), g.getVertexCount(), g.getEdgeCount());
        }

        // 4. Generate 3 extra large graphs (1400, 1600, 1800 nodes)
        System.out.println("\nGenerating 3 extra large graphs...");
        int[] sizes = {1400, 1600, 1800};
        for (int size : sizes) {
            Graph g = generator.generateConnectedGraph(graphId++, size);
            allGraphs.add(g);
            System.out.printf("  Graph %d: %d vertices, %d edges%n", 
                g.getId(), g.getVertexCount(), g.getEdgeCount());
        }

        // Save all graphs to single JSON file
        allData.setGraphs(allGraphs);
        
        try {
            GraphDataLoader loader = new GraphDataLoader();
            loader.saveGraphsToFile("assign_3_input.json", allGraphs);
            System.out.println("\n" + "=".repeat(70));
            System.out.println("✓ All 28 graphs saved to: assign_3_input.json");
            System.out.println("=".repeat(70));
            System.out.println("\nTotal graphs generated: " + allGraphs.size());
            System.out.println("  - 5 small (30 nodes)");
            System.out.println("  - 10 medium (300 nodes)");
            System.out.println("  - 10 large (1000 nodes)");
            System.out.println("  - 3 extra large (1400, 1600, 1800 nodes)");
            System.out.println("\nNow run BenchmarkRunner to process all graphs!");
        } catch (Exception e) {
            System.err.println("Error saving graphs: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

