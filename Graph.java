import java.util.*;

public class Graph
{
//    Scanner kb = new Scanner(System.in);

    // inner class representing graph edge
    private class Edge
    {
        public int vertex1,vertex2; // indices at the vertices list
        int dist; // distance (in miles to drive)
    }

    // inner class representing graph vertex
    private class Vertex
    {
        public String cityName;
        public ArrayList<String> places; // list of attractions

        public Vertex(String name)
        {
            cityName = name;
            places = new ArrayList<String>();
        }
    }

    // vertices and edges of the graph
    private ArrayList<Vertex         > vertices;
    private ArrayList<ArrayList<Edge>> edges;

    // list of vertices to visit between starting and ending cities
    private LinkedList<Integer       > places;

    // whether a vertex with the given name exists
    public boolean isVertex(String name)
    {
        return (vertexIndex(name) >= 0);
    }

    // and what's its index in vertices list
    public int vertexIndex(String name)
    {
        for(int vi = 0; vi < vertices.size(); vi++)
        {
            if(vertices.get(vi).cityName.equalsIgnoreCase(name))
                return vi;
        }

        return -1;
    }

    Graph() // default constructor
    {
        vertices = new ArrayList<Vertex>();
        edges    = new ArrayList<ArrayList<Edge>>();
        places   = new LinkedList<Integer>();
    }

    // adds edge given vertices names and distance between them
    public void addEdge(String vertexName1, String vertexName2, int dist)
    {
        // one directed edge
        if(!isVertex(vertexName1))
        {
            // if no such vertex, add it
            Vertex v1 = new Vertex(vertexName1);
            vertices.add(v1);
            // and initialize the list of its edges
            edges.add(new ArrayList<Edge>());
        }
        int vertex1 = vertexIndex(vertexName1);
        ArrayList<Edge> edges1 = edges.get(vertex1);

        // second directed edge (because graph is undirected)
        if(!isVertex(vertexName2))
        {
            Vertex v2 = new Vertex(vertexName2);
            vertices.add(v2);
            edges.add(new ArrayList<Edge>());
        }
        int vertex2 = vertexIndex(vertexName2);
        ArrayList<Edge> edges2 = edges.get(vertex2);

        // create two new edges
        Edge edge1 = new Edge();
        edge1.vertex1 = vertex1;
        edge1.vertex2 = vertex2;
        edge1.dist = dist;
        // and of the second vertex
        Edge edge2 = new Edge();
        edge2.vertex1 = vertex2;
        edge2.vertex2 = vertex1;
        edge2.dist = dist;

        // add the new edges to the lists of edges of each vertex
        edges1.add(edge1);
        edges2.add(edge2);
    }

    // addss the given attraction to the list of attractions of the given city 
    public boolean addPlaceToVertex(String cityName, String placeName)
    {
        if(!isVertex(cityName))
            return false;

        int vertexIndex = vertexIndex(cityName);
        vertices.get(vertexIndex).places.add(placeName);
//        System.out.println(cityName + ", " + placeName + " added");
//        kb.nextLine();
        return true;
    }

    // given the attraction names, adds the index of the corresponding city
    // to the list of cities to visit
    public boolean addPlaceToRoute(String placeName)
    {
        for(int I = 0; I < vertices.size(); I++)
        {
            Vertex vertex = vertices.get(I);
            if(vertex.places.contains(placeName))
            {
                places.add(I);

//                System.out.println(vertex.cityName + " = " + I + " added to route");

                return true;
            }
        }

        return false;
    }

    // Dijkstra algorithm fills array of distances from source to all other vertices
    // Also fills prev array with information about shortests path from source
    public void Dijkstra(int source, ArrayList<Integer> dist, ArrayList<Integer> prev)
    {
        boolean isPrint = false;

        ArrayList<Integer> Q = new ArrayList<Integer>();

        int ui,vi,alt,minDist,curDist;
        int u,v;
        for(v = 0; v < vertices.size(); v++)
        {
            dist.add(-1);
            prev.add(-1);
            Q.add(v);
        }
        dist.set(source,0);

        while(!Q.isEmpty())
        {
            ui = minDist = -1;
            for(vi = 0; vi < Q.size(); vi++)
            {
                v = Q.get(vi);
                curDist = dist.get(v);
                if(curDist >= 0 && (ui < 0 || curDist < minDist))
                {
                    ui = vi;
                    minDist = curDist;
                }
            }

            u = Q.get(ui);
            Q.remove(ui);

            if(isPrint)
            {
                System.out.println("u = " + u + " (" + vertices.get(u) + ")");
                System.out.print("edges[" + u + "] = ");
                for(Edge edge: edges.get(u))
                    System.out.print(edge.vertex1 + "->" + edge.vertex2 + " ");
                System.out.println();
            }

            for(vi = 0; vi < edges.get(u).size(); vi++)
            {
                v = edges.get(u).get(vi).vertex2;

                if(Q.contains(v))
                {
                    alt = dist.get(u) + edges.get(u).get(vi).dist;

                    if(isPrint)
                    {
                        System.out.println("  v = " + v + " (" + vertices.get(v) + ") is in Q");
                        System.out.println("  alt = " + alt + ", dist.get(v) = " + dist.get(v));
                    }

                    if(dist.get(v) < 0 || alt < dist.get(v))
                    {
                        dist.set(v,alt);
                        prev.set(v,u);
                    }
                }
            }
        }
    }

    // utility function that pulls the shortest path source->target from prev array
    private List<Integer> fillPath(int source, int target, ArrayList<Integer> prev)
    {
        LinkedList<Integer> path = new LinkedList<Integer>();

        int u = target;
        if(u == source || prev.get(u) >= 0)
        {
            while(u >= 0)
            {
                path.addFirst(u);
                u = prev.get(u);
            }
        }

        return path;
    }

    // static function that returns list of all permutations of the given list am
    private static LinkedList<LinkedList<Integer>> perm(LinkedList<Integer> am)
    {
        LinkedList<LinkedList<Integer>> list = new LinkedList<LinkedList<Integer>>();

        int m = am.size();

        // the terminal recursion case, a list with one element has only one permutation
        if(m == 1)
        {
            // duplicate the array am to not affect it further
            LinkedList<Integer> am1 = new LinkedList<Integer>();
            am1.addLast(am.get(0));

            list.addLast(am1);
        }
        else
        {
            for(int I = 0; I < m; I++)
            {
                LinkedList<Integer> am1 = new LinkedList<Integer>();

                // add all elements except the I-th
                for(int J = 0; J < m; J++)
                {
                    if(J != I)
                        am1.add(am.get(J));
                }

                // call the same function recursively with shorter array
                LinkedList<LinkedList<Integer>> list1 = perm(am1);

                // and add I-th element to the end of each permutation
                for(int J = 0; J < list1.size(); J++)
                {
                    LinkedList<Integer> arr = list1.get(J);
                    arr.add(0,am.get(I));
                    list.add(arr);
                }
            }
        }

        return list;
    }

    // the function that actually finds the shortest route
    // between starting_city and ending_city visiting all vertices from places list
    public LinkedList<String> route(String starting_city, String ending_city)
    {
        int source = vertexIndex(starting_city),target = vertexIndex(ending_city);
        //System.out.println("source = " + source + ",  target = " + target);

        // if there are no places in between, add source to them to unify
        boolean isPlaces = !places.isEmpty();
        if(!isPlaces)
            places.add(source);
        List<LinkedList<Integer>> listp = perm(places);
        int numPlaces = places.size();

        // add both ends to the end of the places list
        if(isPlaces)
            places.add(source);
        places.add(target);

        int numSource = numPlaces,numTarget = numPlaces + 1;
        if(!isPlaces)
        {
            numSource = 0;
            numTarget = 1;
        }

        // find all shortest paths between source, target and all places
        // store all requlting arrays at the lists dists and prevs
        ArrayList<ArrayList<Integer>> dists = new ArrayList<ArrayList<Integer>>();
        ArrayList<ArrayList<Integer>> prevs = new ArrayList<ArrayList<Integer>>();
        for(int placei = 0; placei < places.size(); placei++)
        {
            int place = places.get(placei);

            ArrayList<Integer> dist = new ArrayList<Integer>();
            ArrayList<Integer> prev = new ArrayList<Integer>();
            Dijkstra(place,dist,prev);
            dists.add(dist);
            prevs.add(prev);
        }

        // for each permutation of places, use the known shortest paths
        // between all points at the given order.
        // And remember which distance was the best.
        int dist = -1,npMin = -1;
        for(int np = 0; np < listp.size(); np++)
        {
            LinkedList<Integer> perm = listp.get(np);
            //System.out.println("numperm = " + np + ", perm = " + perm);

            int curDist = 0;

            // the first path, from source to the first vertex in the permutation
            int end2i = perm.get(0);
            curDist += dists.get(numSource).get(end2i);
            //System.out.println("curDistS = " + curDist);
            List<Integer> pathS = fillPath(source,end2i,prevs.get(numSource));

            //System.out.println(pathS);
            //for(int num: pathS)
            //    System.out.println(vertices.get(num).cityName);

            // the next paths, between the vertices in the permutation
            for(int I = 0; I < perm.size() - 1; I++)
            {
                int place1 = perm.get(I),place2 = perm.get(I+1);
                int place1i = places.indexOf(place1),place2i = places.indexOf(place2);

                curDist += dists.get(place1i).get(place2);
                List<Integer> path = fillPath(place1,place2,prevs.get(place1i)); //I

                //System.out.println("I = " + I + " " + path);
            }

            // the lastst path, from the last vertex in the permutation to target
            end2i = perm.get(perm.size()-1);
            curDist += dists.get(numTarget).get(end2i);
            //System.out.println("curDistT = " + curDist);
            List<Integer> pathT = fillPath(target,end2i,prevs.get(numTarget));
            Collections.reverse(pathT);

            //System.out.println(pathT);
            //for(int num: pathT)
            //    System.out.println(vertices.get(num).cityName);

            // if it is the best distance for now, remember it and the best permutation [index]
            if(np == 0 || dist > curDist)
            {
                dist = curDist;
                npMin = np;
            }

            //System.out.println("curDist = " + curDist + ", dist = " + dist);
            //kb.nextLine();
        }

        // finally, compose all sub-paths into one connected path
        LinkedList<String> shortPath = new LinkedList<String>();
        LinkedList<Integer> permMin = listp.get(npMin);
        permMin.addFirst(source);
        permMin.addLast (target);

        for(int I = 0; I < permMin.size()-1; I++)
        {
            int place1 = permMin.get(I),place2 = permMin.get(I+1);
            int place1i = places.indexOf(place1),place2i = places.indexOf(place2);
            List<Integer> path = fillPath(place1,place2,prevs.get(place1i));

            for(int J = 0; J < path.size()-1; J++)
                shortPath.addLast(vertices.get(path.get(J)).cityName);
        }
        shortPath.addLast(vertices.get(target).cityName);

        // and return it
        return shortPath;
    }
}