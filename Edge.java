import com.fasterxml.jackson.annotation.JsonProperty;

public class Edge implements Comparable<Edge> {
    @JsonProperty("from")
    private String from;
    
    @JsonProperty("to")
    private String to;
    
    @JsonProperty("weight")
    private int weight;

    public Edge() {
    }

    public Edge(String from, String to, int weight) {
        this.from = from;
        this.to = to;
        this.weight = weight;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    @Override
    public int compareTo(Edge other) {
        return Integer.compare(this.weight, other.weight);
    }

    @Override
    public String toString() {
        return String.format("{from: %s, to: %s, weight: %d}", from, to, weight);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Edge edge = (Edge) obj;
        return weight == edge.weight &&
                ((from.equals(edge.from) && to.equals(edge.to)) ||
                 (from.equals(edge.to) && to.equals(edge.from)));
    }

    @Override
    public int hashCode() {
        return weight;
    }
}

