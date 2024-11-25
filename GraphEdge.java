public class GraphEdge {

    // Fields for the origin, destination, type, and label
    private GraphNode origin; // Starting node of the edge
    private GraphNode destination; // Ending node of the edge
    private int type; // Additional metadata (e.g., weight or classification)
    private String label; // Descriptive label for the edge

    // Constructor to initialize all fields
    public GraphEdge(GraphNode origin, GraphNode destination, int type, String label) {
        this.origin = origin;
        this.destination = destination;
        this.type = type;
        this.label = label;
    }

    // Getter for the first endpoint (origin)
    public GraphNode firstEndpoint() {
        return origin;
    }

    // Getter for the second endpoint (destination)
    public GraphNode secondEndpoint() {
        return destination;
    }

    // Getter for the type field
    public int getType() {
        return type;
    }

    // Setter for the type field
    public void setType(int type) {
        this.type = type;
    }

    // Getter for the label field
    public String getLabel() {
        return label;
    }

    // Setter for the label field
    public void setLabel(String label) {
        this.label = label;
    }
}