import java.util.*;
import java.io.*;

public class RoadTrip
{
    private static List<String> route
    (
        String starting_city, String ending_city, List<String> attractions
    ) throws IOException // because it opens and reads files
    {
        Graph graph = new Graph();

        // roads are edges of the graph
        String fileRoads = "roads.csv",line;
        BufferedReader br = new BufferedReader(new FileReader(fileRoads));
        while ((line = br.readLine()) != null)
        {
            String[] data = line.split(",");
            graph.addEdge(data[0],data[1],Integer.parseInt(data[2]));
        }
        br.close();

        // attractions are inside vertices of the graph
        String filePlaces = "attractions.csv";
        br = new BufferedReader(new FileReader(filePlaces));
        while ((line = br.readLine()) != null)
        {
            String[] data = line.split(",");
            graph.addPlaceToVertex(data[1],data[0]);
        }
        br.close();

        // store intermediate cities to visit in the graph
        for(String place: attractions)
            graph.addPlaceToRoute(place);

        // find and return the route
        LinkedList<String> list = graph.route(starting_city,ending_city);

        return list;
    }

    public static void main(String[] args) throws IOException
    {
        Scanner kb = new Scanner(System.in);

        System.out.print("Please enter the starting city name: ");
        String starting_city = kb.nextLine();
        System.out.print("Please enter the ending city name  : ");
        String ending_city = kb.nextLine();

        LinkedList<String> attractions = new LinkedList<String>();
        while(true)
        {
            System.out.println("\nPlease enter an attraction name (-1 to finish): ");
            String place = kb.nextLine();
            if(!place.equals("-1"))
                attractions.add(place);
            else
                break;
        }

        // find and output the route
        List<String> trip = route(starting_city,ending_city,attractions);
        System.out.println("route = " + trip);
    }
}
