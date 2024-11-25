import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Maze {

    private Graph graph; // Graph representation of the maze
    private int entranceNode; // ID of the entrance node
    private int exitNode; // ID of the exit node
    private int totalCoins; // Total number of coins available
    private List<GraphNode> path; // Stores the current path during DFS

    // Constructor
    public Maze(String inputFile) throws MazeException {
        try (BufferedReader inputReader = new BufferedReader(new FileReader(inputFile))) {
            readInput(inputReader);
        } catch (IOException | GraphException e) {
            throw new MazeException("Error reading the input file: " + e.getMessage());
        }
    }

    // Returns the graph representation of the maze
    public Graph getGraph() throws MazeException {
        if (graph == null) {
            throw new MazeException("Graph is not initialized.");
        }
        return graph;
    }

    // Solves the maze using DFS and returns an iterator for the path
    public Iterator<GraphNode> solve() {
        path = new ArrayList<>();
        try {
            if (DFS(totalCoins, graph.getNode(entranceNode))) {
                return path.iterator();
            }
        } catch (GraphException e) {
            e.printStackTrace();
        }
        return null; // No solution found
    }

    // Reads the input file and constructs the graph
    private void readInput(BufferedReader inputReader) throws IOException, GraphException {
        inputReader.readLine(); // Skip scale factor (S)
        int width = Integer.parseInt(inputReader.readLine()); // Read width (A)
        int length = Integer.parseInt(inputReader.readLine()); // Read length (L)
        totalCoins = Integer.parseInt(inputReader.readLine()); // Read total coins (k)

        int totalNodes = width * length;
        graph = new Graph(totalNodes);

        String line;
        int row = 0;

        while ((line = inputReader.readLine()) != null) {
            if (row % 2 == 0) { // Node rows
                parseNodes(line, row / 2, width);
            } else { // Edge rows
                parseEdges(line, row / 2, width, row % 2 != 0);
            }
            row++;
        }
    }

    // Parses nodes from the input line
    private void parseNodes(String line, int row, int width) throws GraphException {
        for (int col = 0; col < line.length(); col++) {
            char cell = line.charAt(col);
            int nodeIndex = row * width + col;

            if (cell == 's') {
                entranceNode = nodeIndex;
            } else if (cell == 'x') {
                exitNode = nodeIndex;
            }
        }
    }

    // Parses edges from the input line
    private void parseEdges(String line, int row, int width, boolean horizontal) throws GraphException {
        for (int col = 0; col < line.length(); col++) {
            char edgeType = line.charAt(col);
            int node1, node2;

            if (horizontal) {
                node1 = row * width + col;
                node2 = node1 + 1;
            } else {
                node1 = col * width + row;
                node2 = node1 + width;
            }

            if (edgeType == 'c') {
                insertEdge(node1, node2, 0, "corridor");
            } else if (Character.isDigit(edgeType)) {
                insertEdge(node1, node2, Character.getNumericValue(edgeType), "door");
            }
        }
    }

    // Inserts an edge between two nodes in the graph
    private void insertEdge(int node1, int node2, int linkType, String label) throws GraphException {
        graph.insertEdge(graph.getNode(node1), graph.getNode(node2), linkType, label);
    }

    // Depth-First Search for solving the maze
    private boolean DFS(int coinsLeft, GraphNode currentNode) throws GraphException {
        currentNode.mark(true); // Mark the node as visited
        path.add(currentNode);

        if (currentNode.getName() == exitNode) {
            return true; // Exit found
        }

        Iterator<GraphEdge> edges = graph.incidentEdges(currentNode);
        while (edges.hasNext()) {
            GraphEdge edge = edges.next();
            GraphNode neighbor = edge.firstEndpoint().equals(currentNode) 
                ? edge.secondEndpoint() 
                : edge.firstEndpoint();

            if (!neighbor.isMarked()) {
                int edgeCost = edge.getLabel().equals("door") ? edge.getType() : 0;
                if (coinsLeft >= edgeCost) {
                    if (DFS(coinsLeft - edgeCost, neighbor)) {
                        return true;
                    }
                }
            }
        }

        // Backtrack
        currentNode.mark(false);
        path.remove(path.size() - 1);
        return false;
    }
}
