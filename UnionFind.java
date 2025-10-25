import java.util.HashMap;
import java.util.Map;

public class UnionFind {
    private Map<String, String> parent;
    private Map<String, Integer> rank;
    private long operationsCount;

    public UnionFind() {
        parent = new HashMap<>();
        rank = new HashMap<>();
        operationsCount = 0;
    }

    public void makeSet(String node) {
        parent.put(node, node);
        rank.put(node, 0);
        operationsCount++;
    }

    public String find(String node) {
        operationsCount++;
        if (!parent.get(node).equals(node)) {
            parent.put(node, find(parent.get(node))); // Path compression
        }
        return parent.get(node);
    }

    public boolean union(String node1, String node2) {
        String root1 = find(node1);
        String root2 = find(node2);
        
        operationsCount++;
        if (root1.equals(root2)) {
            return false;
        }

        // Union by rank
        int rank1 = rank.get(root1);
        int rank2 = rank.get(root2);
        
        operationsCount++;
        if (rank1 < rank2) {
            parent.put(root1, root2);
        } else if (rank1 > rank2) {
            parent.put(root2, root1);
        } else {
            parent.put(root2, root1);
            rank.put(root1, rank1 + 1);
        }
        
        return true;
    }


    public long getOperationsCount() {
        return operationsCount;
    }


    public void resetOperationsCount() {
        operationsCount = 0;
    }
}

