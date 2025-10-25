import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.*;
import java.util.List;

public class GraphDataLoader {
    private ObjectMapper objectMapper;

    public GraphDataLoader() {
        this.objectMapper = new ObjectMapper();
    }

    public List<Graph> loadGraphsFromResource(String fileName) throws IOException {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(fileName);
        
        if (inputStream == null) {
            throw new FileNotFoundException("Resource file not found: " + fileName);
        }
        
        GraphData data = objectMapper.readValue(inputStream, GraphData.class);
        return data.getGraphs();
    }

    public List<Graph> loadGraphsFromFile(String filePath) throws IOException {
        File file = new File(filePath);
        GraphData data = objectMapper.readValue(file, GraphData.class);
        return data.getGraphs();
    }

    public void saveGraphsToFile(String filePath, List<Graph> graphs) throws IOException {
        GraphData data = new GraphData();
        data.setGraphs(graphs);
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(filePath), data);
    }

    public static class GraphData {
        @JsonProperty("graphs")
        private List<Graph> graphs;

        public List<Graph> getGraphs() {
            return graphs;
        }

        public void setGraphs(List<Graph> graphs) {
            this.graphs = graphs;
        }
    }
}

