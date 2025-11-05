package Lab6;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

public class Anagrams {

    public static void main(String[] args) {
        try {
            findAll(System.in);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void findAll(InputStream inputStream) throws IOException {
        Map<String, List<String>> result = new TreeMap<>();
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] words = line.split("\\s+");
                for (String word : words) {
                    char[] chars = word.toCharArray();
                    Arrays.sort(chars);
                    String sorted = new String(chars);
                    result.computeIfAbsent(sorted, k -> new ArrayList<>()).add(word);
                }
            }
        }

        List<List<String>> groups = new ArrayList<>();
        for (Map.Entry<String, List<String>> entry : result.entrySet()) {
            List<String> group = entry.getValue();
            if(group.size() >= 5) {
                groups.add(group);
            }
        }

        groups.sort(Comparator.comparing(group -> group.get(0)));
        for (List<String> group : groups) {
            Collections.sort(group);
            System.out.println(String.join(" ", group));
        }
    }
}
