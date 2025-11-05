package Aud9;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class CountOccurrencesTest {

    public static int count(List<List<String>> c, String str) {
        int count = 0;
        for (Collection<String> strings : c) {
            if(strings.contains(str)) {
                count++;
            }
        }
        return count;
    }

    public static long countFunc(List<List<String>> collection, String str) {
        return collection.stream().mapToLong(coll -> coll.stream().filter(x -> x.contains(str)).count()).sum();
    }

    public static void main(String[] args) {
        List<List<String>> list = new ArrayList<>();

        list.add(Arrays.asList("apple", "banana", "cherry"));
        list.add(Arrays.asList("banana", "grape"));
        list.add(Arrays.asList("apple", "banana", "kiwi"));
        list.add(Arrays.asList("mango", "peach"));

        String wordToSearch = "banana";

        int countResult = count(list, wordToSearch);
        long countFuncResult = countFunc(list, wordToSearch);

        System.out.println("Result from count: " + countResult);
        System.out.println("Result from countFunc: " + countFuncResult);
    }
}
