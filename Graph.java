import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Graph implements GraphADT {

    private Map<GraphNode, List<GraphEdge>> adjacencyList;
    private Map<Integer, GraphNode> nodes; // Map node names to GraphNode objects

    public Graph(int n) {
        adjacencyList = new HashMap<>();
        nodes = new HashMap<>();
        // Create nodes and add to the adjacency list
        for (int i = 0; i < n; i++) {
            GraphNode node = new GraphNode(i);
            nodes.put(i, node);
            adjacencyList.put(node, new ArrayList<>());
        }
    }

    @Override
    public void insertEdge(GraphNode nodeu, GraphNode nodev, int type, String label) throws GraphException {
        if (!adjacencyList.containsKey(nodeu) || !adjacencyList.containsKey(nodev)) {
            throw new GraphException("One or both nodes do not exist in the graph.");
        }

        GraphEdge edge = new GraphEdge(nodeu, nodev, type, label);

        // Add the edge to both nodes' adjacency lists
        adjacencyList.get(nodeu).add(edge);
        adjacencyList.get(nodev).add(edge);
    }

    @Override
    public GraphNode getNode(int u) throws GraphException {
        if (!nodes.containsKey(u)) {
            throw new GraphException("Node with name " + u + " does not exist.");
        }
        return nodes.get(u);
    }

    @Override
    public Iterator<GraphEdge> incidentEdges(GraphNode u) throws GraphException {
        if (!adjacencyList.containsKey(u)) {
            throw new GraphException("Node does not exist in the graph.");
        }
        return adjacencyList.get(u).iterator();
    }

    @Override
    public GraphEdge getEdge(GraphNode u, GraphNode v) throws GraphException {
        if (!adjacencyList.containsKey(u) || !adjacencyList.containsKey(v)) {
            throw new GraphException("One or both nodes do not exist in the graph.");
        }

        // Search for the edge in the adjacency list of the node with fewer edges
        List<GraphEdge> edges = adjacencyList.get(u).size() <= adjacencyList.get(v).size()
                ? adjacencyList.get(u)
                : adjacencyList.get(v);

        for (GraphEdge edge : edges) {
            if ((edge.firstEndpoint().equals(u) && edge.secondEndpoint().equals(v)) ||
                (edge.firstEndpoint().equals(v) && edge.secondEndpoint().equals(u))) {
                return edge;
            }
        }

        return null; // No edge found
    }

    @Override
    public boolean areAdjacent(GraphNode u, GraphNode v) throws GraphException {
        // Reuse getEdge to check if an edge exists between u and v
        return getEdge(u, v) != null;
    }
}