package Aud9;
import java.util.*;

public class ReverseListTest {

    public static <T> void printReverse(Collection<? extends T> collection) {
        ArrayList<T> list = new ArrayList<>(collection);
        Collections.reverse(list);
        System.out.println(list);
    }

    public static void main(String[] args) {
        List<String> words = Arrays.asList("FINKI", "is", "the", "best");
        System.out.println("Original list: " + words);
        System.out.print("Reversed list: ");
        printReverse(words);
    }
}
