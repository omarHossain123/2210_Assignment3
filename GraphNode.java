public class GraphNode {

    // Fields for the node's name and its marked status
    private int name; // The unique identifier for this node
    private boolean mark; // Whether the node is marked (visited or not)

    // Constructor to initialize the node with a name
    public GraphNode(int name) {
        this.name = name;
        this.mark = false; // Default mark value is false (unmarked)
    }

    // Setter for the mark field
    public void mark(boolean mark) {
        this.mark = mark;
    }

    // Getter for the mark field
    public boolean isMarked() {
        return mark;
    }

    // Getter for the name field
    public int getName() {
        return name;
    }
}