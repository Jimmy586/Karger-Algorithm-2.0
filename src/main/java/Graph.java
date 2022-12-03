import java.util.*;

public class Graph {
        Map<Integer, Vertex> vertices = new TreeMap<Integer, Vertex>();
        List<Edge> edges = new ArrayList<Edge>();
        public Vertex getVertex(int id)
        {
            Vertex v;
            if ((v = vertices.get(id)) == null)
            {
                v = new Vertex(id);//when creating graphs and to create new vertices if it has not been done yet
                this.addVertex(v);
            }
            return v;
        }
        void addVertex(Vertex newVertex) {
        this.vertices.put(newVertex.getId(), newVertex);
    }//add new vertex to the graph

}