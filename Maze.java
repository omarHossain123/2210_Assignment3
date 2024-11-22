import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Maze {

    private Graph graph; // Stores the graph representation of the maze
    private GraphNode startNode; // Starting node of the maze
    private GraphNode endNode; // Ending node of the maze
    private int requiredCoins; // Number of coins required to solve the maze
    private List<GraphNode> path; // Path to avoid cycles during traversal

    public Maze(String inputFile) throws MazeException {
        try (BufferedReader reader = new BufferedReader(new FileReader(new File(inputFile)))) {
            readInput(reader);
        } catch (IOException | GraphException e) {
            throw new MazeException("Error reading input file: " + e.getMessage());
        }
    }

    public Graph getGraph() {
        return graph;
    }

    public Iterator<GraphNode> solve() {
        path = new ArrayList<>();
        try {
            return DFS(requiredCoins, startNode);
        } catch (GraphException e) {
            return null; // Return null if an exception occurs during DFS
        }
    }

    private Iterator<GraphNode> DFS(int coinsLeft, GraphNode current) throws GraphException {
        // Base case: If the current node is the end node and all coins are collected
        if (current.equals(endNode) && coinsLeft == 0) {
            path.add(current);
            return path.iterator();
        }

        // Mark the current node and add it to the path
        current.mark(true);
        path.add(current);

        // Iterate over incident edges
        Iterator<GraphEdge> edges = graph.incidentEdges(current);
        while (edges.hasNext()) {
            GraphEdge edge = edges.next();
            GraphNode nextNode = edge.secondEndpoint().equals(current)
                    ? edge.firstEndpoint()
                    : edge.secondEndpoint();

            if (!nextNode.isMarked()) {
                // Decrement coinsLeft if the edge type is 1 (coin edge)
                int newCoinsLeft = edge.getType() == 1 ? coinsLeft - 1 : coinsLeft;

                Iterator<GraphNode> result = DFS(newCoinsLeft, nextNode);
                if (result != null) {
                    return result; // Return the successful path
                }
            }
        }

        // Backtrack: Unmark the current node and remove it from the path
        current.mark(false);
        path.remove(path.size() - 1);

        return null; // No valid path found from this branch
    }

    private void readInput(BufferedReader inputReader) throws IOException, GraphException {
        String[] firstLine = inputReader.readLine().split(" ");
        int numRooms = Integer.parseInt(firstLine[0]); // Number of rooms
        int numLinks = Integer.parseInt(firstLine[1]); // Number of links
        requiredCoins = Integer.parseInt(firstLine[2]); // Number of required coins
        int startRoom = Integer.parseInt(firstLine[3]); // Starting room ID
        int endRoom = Integer.parseInt(firstLine[4]); // Ending room ID

        // Initialize the graph
        graph = new Graph(numRooms);
        startNode = graph.getNode(startRoom);
        endNode = graph.getNode(endRoom);

        // Read and process edges
        for (int i = 0; i < numLinks; i++) {
            String[] edgeLine = inputReader.readLine().split(" ");
            int node1 = Integer.parseInt(edgeLine[0]);
            int node2 = Integer.parseInt(edgeLine[1]);
            int linkType = Integer.parseInt(edgeLine[2]);
            String label = edgeLine[3];
            insertEdge(node1, node2, linkType, label);
        }
    }

    private void insertEdge(int node1, int node2, int linkType, String label) throws GraphException {
        GraphNode firstNode = graph.getNode(node1);
        GraphNode secondNode = graph.getNode(node2);
        graph.insertEdge(firstNode, secondNode, linkType, label);
    }
}
