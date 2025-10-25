import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Main class to run MST algorithms and generate benchmark results.
 * Reads input from JSON, runs both Prim's and Kruskal's algorithms,
 * and saves results to output JSON file.
 */
public class BenchmarkRunner {
    private static final String DEFAULT_INPUT_FILE = "assign_3_input.json";
    private static final String DEFAULT_OUTPUT_FILE = "output.json";

    public static void main(String[] args) {
        String inputFile = DEFAULT_INPUT_FILE;
        String outputFile = DEFAULT_OUTPUT_FILE;

        // Allow command line arguments to override defaults
        if (args.length >= 1) {
            inputFile = args[0];
        }
        if (args.length >= 2) {
            outputFile = args[1];
        }

        try {
            runBenchmark(inputFile, outputFile);
            System.out.println("Benchmark completed successfully!");
            System.out.println("Results saved to: " + outputFile);
        } catch (IOException e) {
            System.err.println("Error running benchmark: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Runs the benchmark for all graphs in the input file.
     */
    public static void runBenchmark(String inputFile, String outputFile) throws IOException {
        GraphDataLoader loader = new GraphDataLoader();
        List<Graph> graphs;

        // Load from file path
        graphs = loader.loadGraphsFromFile(inputFile);
        System.out.println("Loaded graphs from file: " + inputFile);

        List<GraphResult> results = new ArrayList<>();

        System.out.println("\n" + "=".repeat(70));
        System.out.println("Processing " + graphs.size() + " graph(s)...");
        System.out.println("=".repeat(70));

        for (Graph graph : graphs) {
            System.out.println("\n--- Processing Graph ID: " + graph.getId() + " ---");
            System.out.println("Vertices: " + graph.getVertexCount() + ", Edges: " + graph.getEdgeCount());
            
            if (!graph.isConnected()) {
                System.out.println("WARNING: Graph is disconnected. MST cannot be computed.");
                continue;
            }

            // Run Prim's algorithm
            System.out.println("\nRunning Prim's algorithm...");
            PrimMST primMST = new PrimMST();
            MSTResult primResult = primMST.findMST(graph);
            System.out.printf("  Total Cost: %d, Operations: %d, Time: %.2f ms%n",
                    primResult.getTotalCost(),
                    primResult.getOperationsCount(),
                    primResult.getExecutionTimeMs());

            // Run Kruskal's algorithm
            System.out.println("Running Kruskal's algorithm...");
            KruskalMST kruskalMST = new KruskalMST();
            MSTResult kruskalResult = kruskalMST.findMST(graph);
            System.out.printf("  Total Cost: %d, Operations: %d, Time: %.2f ms%n",
                    kruskalResult.getTotalCost(),
                    kruskalResult.getOperationsCount(),
                    kruskalResult.getExecutionTimeMs());

            // Verify results match
            if (primResult.getTotalCost() == kruskalResult.getTotalCost()) {
                System.out.println("✓ Both algorithms produced the same total cost.");
            } else {
                System.out.println("✗ WARNING: Algorithms produced different total costs!");
            }

            // Create result object
            GraphResult graphResult = new GraphResult();
            graphResult.setGraphId(graph.getId());
            
            Map<String, Integer> inputStats = new HashMap<>();
            inputStats.put("vertices", graph.getVertexCount());
            inputStats.put("edges", graph.getEdgeCount());
            graphResult.setInputStats(inputStats);
            
            graphResult.setPrim(primResult);
            graphResult.setKruskal(kruskalResult);
            
            results.add(graphResult);
        }

        // Save results to output file
        saveResultsToJson(outputFile, results);
        
        // Save results to CSV file for analysis
        String csvFile = outputFile.replace(".json", ".csv");
        saveResultsToCSV(csvFile, results);
        
        System.out.println("\n" + "=".repeat(70));
        System.out.println("Benchmark Summary");
        System.out.println("=".repeat(70));
        printSummary(results);
    }

    /**
     * Saves results to JSON file.
     */
    private static void saveResultsToJson(String outputFile, List<GraphResult> results) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        
        Map<String, Object> output = new HashMap<>();
        output.put("results", results);
        
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(outputFile), output);
    }

    /**
     * Saves results to CSV file for easy analysis in spreadsheet applications.
     */
    private static void saveResultsToCSV(String csvFile, List<GraphResult> results) {
        try {
            java.io.PrintWriter writer = new java.io.PrintWriter(new java.io.File(csvFile));
            
            // Write CSV header
            writer.println("Graph_ID,Vertices,Edges,Prim_Cost,Prim_Operations,Prim_Time_ms,Kruskal_Cost,Kruskal_Operations,Kruskal_Time_ms,Cost_Match");
            
            // Write data rows
            for (GraphResult result : results) {
                writer.printf("%d,%d,%d,%d,%d,%.2f,%d,%d,%.2f,%s%n",
                        result.getGraphId(),
                        result.getInputStats().get("vertices"),
                        result.getInputStats().get("edges"),
                        result.getPrim().getTotalCost(),
                        result.getPrim().getOperationsCount(),
                        result.getPrim().getExecutionTimeMs(),
                        result.getKruskal().getTotalCost(),
                        result.getKruskal().getOperationsCount(),
                        result.getKruskal().getExecutionTimeMs(),
                        result.getPrim().getTotalCost() == result.getKruskal().getTotalCost() ? "YES" : "NO");
            }
            
            writer.close();
            System.out.println("CSV results saved to: " + csvFile);
        } catch (Exception e) {
            System.err.println("Error saving CSV file: " + e.getMessage());
        }
    }

    /**
     * Prints a summary of all results.
     */
    private static void printSummary(List<GraphResult> results) {
        System.out.printf("%-10s %-10s %-15s %-15s %-20s %-20s%n",
                "Graph ID", "Vertices", "Prim Cost", "Kruskal Cost", "Prim Time (ms)", "Kruskal Time (ms)");
        System.out.println("-".repeat(90));
        
        for (GraphResult result : results) {
            System.out.printf("%-10d %-10d %-15d %-15d %-20.2f %-20.2f%n",
                    result.getGraphId(),
                    result.getInputStats().get("vertices"),
                    result.getPrim().getTotalCost(),
                    result.getKruskal().getTotalCost(),
                    result.getPrim().getExecutionTimeMs(),
                    result.getKruskal().getExecutionTimeMs());
        }
    }

    /**
     * Helper class to represent a single graph's results in output JSON.
     */
    public static class GraphResult {
        @JsonProperty("graph_id")
        private int graphId;
        
        @JsonProperty("input_stats")
        private Map<String, Integer> inputStats;
        
        @JsonProperty("prim")
        private MSTResult prim;
        
        @JsonProperty("kruskal")
        private MSTResult kruskal;

        public int getGraphId() {
            return graphId;
        }

        public void setGraphId(int graphId) {
            this.graphId = graphId;
        }

        public Map<String, Integer> getInputStats() {
            return inputStats;
        }

        public void setInputStats(Map<String, Integer> inputStats) {
            this.inputStats = inputStats;
        }

        public MSTResult getPrim() {
            return prim;
        }

        public void setPrim(MSTResult prim) {
            this.prim = prim;
        }

        public MSTResult getKruskal() {
            return kruskal;
        }

        public void setKruskal(MSTResult kruskal) {
            this.kruskal = kruskal;
        }
    }
}

