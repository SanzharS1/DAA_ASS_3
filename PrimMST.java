import java.util.*;

public class PrimMST {
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

        Set<String> visited = new HashSet<>();
        PriorityQueue<EdgeWithVertex> minHeap = new PriorityQueue<>();

        // Start from the first node
        String startNode = graph.getNodes().get(0);
        visited.add(startNode);
        operationsCount++; // Add to visited set

        // Add all edges from start node to priority queue
        for (Edge edge : graph.getAdjacencyList().get(startNode)) {
            minHeap.offer(new EdgeWithVertex(edge, edge.getTo()));
            operationsCount++; // Add to heap
        }

        // Continue until we have V-1 edges or heap is empty
        while (!minHeap.isEmpty() && mstEdges.size() < graph.getVertexCount() - 1) {
            EdgeWithVertex current = minHeap.poll();
            operationsCount++; // Poll from heap

            String vertex = current.vertex;
            Edge edge = current.edge;

            // If vertex already visited, skip
            operationsCount++; // Check if visited
            if (visited.contains(vertex)) {
                continue;
            }

            // Add edge to MST
            mstEdges.add(new Edge(edge.getFrom(), edge.getTo(), edge.getWeight()));
            totalCost += edge.getWeight();
            visited.add(vertex);
            operationsCount++; // Add to visited

            // Add all edges from newly added vertex
            for (Edge nextEdge : graph.getAdjacencyList().get(vertex)) {
                operationsCount++; // Check if visited
                if (!visited.contains(nextEdge.getTo())) {
                    minHeap.offer(new EdgeWithVertex(nextEdge, nextEdge.getTo()));
                    operationsCount++; // Add to heap
                }
            }
        }

        long endTime = System.nanoTime();
        double executionTimeMs = (endTime - startTime) / 1_000_000.0;

        return new MSTResult(mstEdges, totalCost, operationsCount, executionTimeMs);
    }

    private static class EdgeWithVertex implements Comparable<EdgeWithVertex> {
        Edge edge;
        String vertex;

        EdgeWithVertex(Edge edge, String vertex) {
            this.edge = edge;
            this.vertex = vertex;
        }

        @Override
        public int compareTo(EdgeWithVertex other) {
            return Integer.compare(this.edge.getWeight(), other.edge.getWeight());
        }
    }
}

