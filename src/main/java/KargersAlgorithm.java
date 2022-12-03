
import java.util.*;
import java.util.List;
import java.util.logging.Logger;

import static java.lang.System.currentTimeMillis;

public class KargersAlgorithm{
    public static void main(String[] args) throws InterruptedException {
        Map<Integer, List<Integer>> data = new HashMap<>();
        RandomGraphGenerator randomGraph = new RandomGraphGenerator();


        for(int i =0; i<randomGraph.adjacencyList.size();i++){
            List<Integer> list = randomGraph.adjacencyList.get(i);
            if (list.isEmpty())
            {
                Logger.getGlobal().info(" No adjacent vertices,PLEASE RERUN THE PROGRAM TO REGENEREATE NEW GRAPH ");
                return;
            }
            else data.put(i,list);
        }

        Map<Integer, Integer> statistics = new LinkedHashMap<>();
        int min = data.size();//number of vertices
        System.out.println("number of vertices : "+min);
        int iter = min*min;//iterates a squared number of the vertices times
        System.out.println("Iteration to be made: "+iter);
        Graph g ;
        g = makeGraph(data);
        displayGraph(g);
        long time1 = currentTimeMillis();
        for (int i = 0; i < iter; i++)
        {
            Graph gr = makeGraph(data);
            int currMin = MultithreadedGraph.kargersMinCut(gr);
            min = Math.min(min, currMin);
            Integer counter;
            if ((counter = statistics.get(currMin)) == null)
            {
                counter = 0;
            }
            statistics.put(currMin, counter + 1);
        }
        long time2 = currentTimeMillis();
        System.out.println("SEQUENTIAL Min: " + min + " stat: "
                + (statistics.get(min) * 100 / iter) + "%");
        System.out.println("EXCECUTION TIME : " + (time2 - time1) + " milliseconds");
        time2=0;
        time1=0;

        min = data.size();
        MultithreadedGraph thread [] = new MultithreadedGraph[2];//creating 2 threads
        for(int i = 0; i< thread.length;i++){//initializing the threads
            thread[i] = new MultithreadedGraph(data,iter/thread.length);
        }
        time1= currentTimeMillis();
        for(int i=0;i< thread.length;i++){
            thread[i].start();
        }
        //for multithreaded part
        int currOccur =0;
        for(int i=0;i< thread.length;i++){
            thread[i].join();
            if(!thread[i].isAlive())
            {
                Map<Integer,Integer> statistic2= new HashMap<>();
                for(Integer stat : thread[i].getStatistics()){//gets the list
                        min = Math.min(min, stat);
                        Integer counter =statistic2.get(stat);
                        if (counter == null)//doesn't exist yet
                        {
                            counter = 0;
                        }
                        statistic2.put(stat, counter + 1);
                }
                currOccur =statistic2.get(min);
            }

        }
        time2 =currentTimeMillis();
            System.out.println("MULTITHREADED Min: " + min + " stat: "
                    + (currOccur * 100 / iter) + "%");
            System.out.println("EXECUTION TIME :"+ (time2-time1) +" milliseconds");


    }

    public static void displayGraph(Graph gr)
    {
        System.out.println("Printing graph");
        for (Vertex v : gr.vertices.values())
        {
            System.out.print(v.getId() + ":");
            for (Edge edge : v.getEdges())
            {
                System.out.print(" " + edge.getOppositeVertex(v).getId());
            }
            System.out.println();
        }
    }

    public static Graph makeGraph(Map<Integer, List<Integer>> array)
    {
        Graph gr = new Graph();
        for (int i = 0; i < array.size(); i++)
        {
            Vertex v = gr.getVertex(i);//creating the vertex
            for (int edgeTo : array.get(i))
            {
                Vertex v2 = gr.getVertex(edgeTo);//creating the vertex
                Edge e;
                if ((e = v2.getEdgeTo(v)) == null)
                {
                    e = new Edge(v, v2);
                    gr.edges.add(e);
                    v.addEdge(e);
                    v2.addEdge(e);
                }
            }
        }
        return gr;
    }
}

class RandomGraphGenerator {
    public int vertices;//number of vertices in the graph |V|
    public int edges;//number of edges in the graph  |E|<= |V|*|V-1| TO have a connected graph
    final int MAX_LIMIT = 100;// the maximum vertices that the graph can have
    public List<List<Integer>> adjacencyList;// An adjacency list to represent a graph
    Random random = new Random();


    public RandomGraphGenerator() {
        // Set a maximum limit for the
        // number of vertices say 20
        this.vertices = random.nextInt(MAX_LIMIT-3) + 3;//minimum number of vertices is 3

        this.edges = random.nextInt(computeMaxEdges(vertices)-3) + 3;

        // Creating an adjacency list
        // representation for the random graph
        adjacencyList = new ArrayList<>(vertices);
        for (int i = 0; i < vertices; i++)
            adjacencyList.add(new ArrayList<>());

        // A for loop to randomly generate edges
        for (int i = 0; i < edges; i++) {
            // Randomly select two vertices to
            // create an edge between them
            int v = random.nextInt(vertices);
            int w = random.nextInt(vertices);

            // Check if there is already an edge between v
            // and w
            if ((v == w) || adjacencyList.get(v).contains(w)) {
                // Reduce the value of i
                // so that again v and w can be chosen
                // for the same edge count
                i = i - 1;
                continue;
            }

            // Add an edge between them if
            // not previously created
            addEdge(v, w);
        }
    }
    int computeMaxEdges(int numOfVertices) {
        return numOfVertices * (numOfVertices - 1);
    }

    // Method to add edges between given vertices
    void addEdge(int vertex, int edge) {
        adjacencyList.get(vertex).add(edge);
    }
}

class MultithreadedGraph extends Thread{
    private Map<Integer, List<Integer>> data;
    private int iter;
    private static List<Integer> statistics;
    public MultithreadedGraph(Map<Integer, List<Integer>> data, int iter){
        this.data = data;
        this.iter = iter;
        statistics = new ArrayList<>();
    }
    public List<Integer> getStatistics(){return MultithreadedGraph.statistics;}
    @Override
    public void run(){
        for (int i = 0; i < iter; i++)//run this part in one thread
        {
            Graph temp = KargersAlgorithm.makeGraph(data);
            int currMin = kargersMinCut(temp);
            statistics.add(currMin);
        }
    }
    public static int kargersMinCut(Graph gr)
    {
        Random rnd = new Random();
        while (gr.vertices.size() > 2)
        {
            Edge edge = gr.edges.remove(rnd.nextInt(gr.edges.size()));
            Vertex v1 = deleteVertex(gr, edge.getVertices().get(0), edge);
            Vertex v2 = deleteVertex(gr, edge.getVertices().get(1), edge);
            // contract
            Vertex mergedVertex = new Vertex(v1.getId());
            redirectEdges(gr, v1, mergedVertex);
            redirectEdges(gr, v2, mergedVertex);
            gr.addVertex(mergedVertex);
        }
        return gr.edges.size();//return the number of edges left between the 2 vertices.
    }
    public static Vertex deleteVertex(Graph gr, Vertex v, Edge e)
    {
        gr.vertices.remove(v.getId());
        v.getEdges().remove(e);
        return v;
    }
    public static void redirectEdges(Graph gr, Vertex fromV, Vertex toV)
    {
        for (Iterator<Edge> it = fromV.getEdges().iterator(); it.hasNext();)
        {
            Edge edge = it.next();
            it.remove();
            if (edge.getOppositeVertex(fromV) == toV)
            {
                // remove self-loop
                toV.getEdges().remove(edge);
                gr.edges.remove(edge);
            }
            else
            {
                edge.replaceVertex(fromV, toV);
                toV.addEdge(edge);
            }
        }
    }
}