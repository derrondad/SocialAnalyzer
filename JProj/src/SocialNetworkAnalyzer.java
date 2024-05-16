import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class SocialNetworkAnalyzer {

    // Function to calculate graph density
    public static double graphDensity(Map<String, List<String>> graph) {
        int numVertices = graph.size();
        int numEdges = 0;
        for (List<String> followers : graph.values()) {
            numEdges += followers.size();
        }
        return (double) numEdges / (numVertices * (numVertices - 1));
    }

    // Function to find the person with the highest number of followers
    public static String personWithMostFollowers(Map<String, List<String>> graph) {
        int maxFollowers = 0;
        String personWithMaxFollowers = null;
        for (Map.Entry<String, List<String>> entry : graph.entrySet()) {
            int numFollowers = entry.getValue().size();
            if (numFollowers > maxFollowers || (numFollowers == maxFollowers && (personWithMaxFollowers == null || entry.getKey().compareTo(personWithMaxFollowers) < 0))) {
                maxFollowers = numFollowers;
                personWithMaxFollowers = entry.getKey();
            }
        }
        return personWithMaxFollowers;
    }

    // Function to find the person who follows the highest number of people
    public static String personWithMostFollowing(Map<String, List<String>> graph) {
        int maxFollowing = 0;
        String personWithMaxFollowing = null;
        Map<String, Integer> followingCount = new HashMap<>();
        for (List<String> followers : graph.values()) {
            for (String follower : followers) {
                followingCount.put(follower, followingCount.getOrDefault(follower, 0) + 1);
            }
        }
        for (Map.Entry<String, Integer> entry : followingCount.entrySet()) {
            if (entry.getValue() > maxFollowing || (entry.getValue() == maxFollowing && (personWithMaxFollowing == null || entry.getKey().compareTo(personWithMaxFollowing) < 0))) {
                maxFollowing = entry.getValue();
                personWithMaxFollowing = entry.getKey();
            }
        }
        return personWithMaxFollowing;
    }

    // Function to calculate degree of separation between two people
    public static int degreeOfSeparation(Map<String, List<String>> graph, String source, String target) {
        Set<String> visited = new HashSet<>();
        Queue<String> queue = new LinkedList<>();
        Map<String, Integer> distances = new HashMap<>();
        queue.add(source);
        distances.put(source, 0);
        while (!queue.isEmpty()) {
            String person = queue.poll();
            int distance = distances.get(person);
            if (person.equals(target)) {
                return distance;
            }
            visited.add(person);
            for (String follower : graph.getOrDefault(person, Collections.emptyList())) {
                if (!visited.contains(follower)) {
                    queue.add(follower);
                    distances.put(follower, distance + 1);
                }
            }
        }
        return -1; // If target is unreachable
    }

    // Function to parse the input file and create the graph
    public static Map<String, List<String>> parseInput(String filePath) throws IOException {
        Map<String, List<String>> graph = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.trim().split("\\s+");
                String person = data[0];
                List<String> follows = Arrays.asList(Arrays.copyOfRange(data, 1, data.length));
                graph.put(person, follows);
            }
        }
        return graph;
    }

    // Main method
    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Usage: java SocialNetworkAnalyzer <inputFile>");
            System.exit(1);
        }

        String inputFilePath = args[0];

        try {
            Map<String, List<String>> graph = parseInput(inputFilePath);

            // Task 1: Graph Density
            double density = graphDensity(graph);
            System.out.println("Graph Density: " + density);

            // Task 2: Person with the highest number of followers
            String personWithMostFollowers = personWithMostFollowers(graph);
            System.out.println("Person with the highest number of followers: " + personWithMostFollowers);

            // Task 3: Person who follows the highest number of people
            String personWithMostFollowing = personWithMostFollowing(graph);
            System.out.println("Person who follows the highest number of people: " + personWithMostFollowing);

            // Task 4: Degree of separation between two people
            String source = "Raman";
            String target = "Anwar";
            int degree = degreeOfSeparation(graph, source, target);
            System.out.println("Degree of separation between " + source + " and " + target + ": " + degree);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
