import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class Vertex {
    private final int id;// the label of the vertex
    private final List<Edge> edges = new LinkedList<>();//the set of edges linked to this vertex
    public Vertex(int id)
    {
        this.id = id;
    }//constructor
    public int getId(){return this.id;}
    public List<Edge> getEdges(){return this.edges;}
    public void addEdge(Edge edge)
    {
        edges.add(edge);
    }//adding an edge to this vertex

    public Edge getEdgeTo(Vertex v2)//getting the edge between this and the vertex v2
    {
        for (Edge edge : edges)
        {
            if (edge.contains(this, v2))
                return edge;
        }
        return null;
    }
}