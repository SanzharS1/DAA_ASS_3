import java.util.*;

public class KruskalMST {
    private long operationsCount;

    public MSTResult findMST(Graph graph) {
        operationsCount = 0;
        long startTime = System.nanoTime();

        List<Edge> mstEdges = new ArrayList<>();
        int totalCost = 0;

        if (graph.getNodes().isEmpty()) {
            long endTime = System.nanoTime();
            double executionTimeMs = (endTime - startTime) / 1_000_000.0;
            return new MSTResult(mstEdges, totalCost, operationsCount, executionTimeMs);
        }

        // Check if graph is connected
        operationsCount++;
        if (!graph.isConnected()) {
            long endTime = System.nanoTime();
            double executionTimeMs = (endTime - startTime) / 1_000_000.0;
            return new MSTResult(mstEdges, -1, operationsCount, executionTimeMs);
        }

        // Initialize Union-Find structure
        UnionFind uf = new UnionFind();
        for (String node : graph.getNodes()) {
            uf.makeSet(node);
        }

        // Sort edges by weight
        List<Edge> sortedEdges = new ArrayList<>(graph.getEdges());
        Collections.sort(sortedEdges);
        operationsCount += sortedEdges.size() * Math.log(sortedEdges.size()); // Sorting complexity

        // Process edges in order of increasing weight
        for (Edge edge : sortedEdges) {
            operationsCount++; // Processing edge
            
            String from = edge.getFrom();
            String to = edge.getTo();

            // Check if adding this edge creates a cycle
            if (uf.union(from, to)) {
                // No cycle, add edge to MST
                mstEdges.add(new Edge(from, to, edge.getWeight()));
                totalCost += edge.getWeight();
                operationsCount++; // Edge added to MST

                // If we have V-1 edges, MST is complete
                if (mstEdges.size() == graph.getVertexCount() - 1) {
                    break;
                }
            }
        }

        // Add Union-Find operations to total count
        operationsCount += uf.getOperationsCount();

        long endTime = System.nanoTime();
        double executionTimeMs = (endTime - startTime) / 1_000_000.0;

        return new MSTResult(mstEdges, totalCost, operationsCount, executionTimeMs);
    }
}

