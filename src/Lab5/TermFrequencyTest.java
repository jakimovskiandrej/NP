package Lab5;

import java.util.*;
import java.io.*;
import java.util.stream.Collectors;

class TermFrequency {

    Map<String,Integer> wordCount;
    int totalWordCount;

    public TermFrequency(InputStream inputStream, String[] stopWords) throws IOException {
        wordCount = new TreeMap<>();
        totalWordCount = 0;

        Set<String> result = Arrays.stream(stopWords).map(String::toLowerCase)
                .collect(Collectors.toSet());

        try(Scanner sc = new Scanner(inputStream)) {
            while (sc.hasNextLine()) {
                String word = sc.next().toLowerCase().replaceAll("[.,]", "");
                if (!result.contains(word) && !wordCount.isEmpty()) {
                    wordCount.put(word, wordCount.getOrDefault(word, 0) + 1);
                    totalWordCount++;
                }
            }
        }
    }

    public int countTotal() {
        return totalWordCount;
    }

    public int countDistinct() {
        return wordCount.size();
    }

    public List<String> mostOften(int k){
        return wordCount.entrySet().stream().sorted(Comparator.<Map.Entry<String,Integer>>comparingInt(Map.Entry::getValue).reversed().thenComparing(Map.Entry::getKey)).limit(k).map(Map.Entry::getKey).collect(Collectors.toList());
    }
}

public class TermFrequencyTest {
    public static void main(String[] args) throws FileNotFoundException {
        String[] stop = new String[] { "во", "и", "се", "за", "ќе", "да", "од",
                "ги", "е", "со", "не", "тоа", "кои", "до", "го", "или", "дека",
                "што", "на", "а", "но", "кој", "ја" };
        TermFrequency tf = null;
        try {
            tf = new TermFrequency(System.in,
                    stop);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println(tf.countTotal());
        System.out.println(tf.countDistinct());
        System.out.println(tf.mostOften(10));
    }
}
