import java.util.ArrayList;
import java.util.List;

public class Edge {
    private List<Vertex> vertices = new ArrayList<Vertex>();// contains only the 2 vertices at the end of this edge

    public Edge(Vertex v1, Vertex v2) throws IllegalArgumentException //setting the 2 vertices at the end of this edge
    {
        if (v1 == null || v2 == null)
        {
            throw new IllegalArgumentException("Both vertices are required");
        }
        vertices.add(v1);
        vertices.add(v2);
    }
    public List<Vertex> getVertices(){return this.vertices;}

    public boolean contains(Vertex v1, Vertex v2)
    {
        return vertices.contains(v1) && vertices.contains(v2);
    }//checking if this edge contains the 2 vertices at its ends

    public Vertex getOppositeVertex(Vertex v)
    {
        if (!vertices.contains(v))
        {
            throw new IllegalArgumentException("Vertex " + v.getId());
        }
        return vertices.get(1 - vertices.indexOf(v));// vertices.get(1||0)
    }//getting the other end of this edge from the vertex v

    public void replaceVertex(Vertex oldVertex, Vertex newVertex)//replacing a vertex at the end of this edge with a new one
    {
        if (!vertices.contains(oldVertex))
        {
            throw new IllegalArgumentException("Vertex " + oldVertex.getId());
        }
        vertices.remove(oldVertex);
        vertices.add(newVertex);
    }
}
