package Aud5;

import java.io.*;
import java.util.Comparator;
import java.util.stream.IntStream;

public class LongestPalindromeTest {
    public static final String FILENAME = "examples/data/words.txt";

    static String findLongestFunc(InputStream inputStream) throws IOException {
        try(BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            return br.lines()
                    .filter(LongestPalindromeTest::isPalindromeFunc)
                    .max(Comparator.comparing(String::length)).orElse(null);
        }
    }

    static boolean isPalindrome(String word) {
        int len = word.length();
        for (int i = 0; i < len / 2; i++) {
            if (word.charAt(i) != word.charAt(len - 1 - i)) {
                return false;
            }
        }
        return true;
    }

    static boolean isPalindromeFunc(String word) {
        return IntStream.range(0, word.length() / 2)
                .allMatch(i -> word.charAt(i) == word.charAt(word.length() - 1 - i));
    }

    public static void main(String[] args) {
        try {
            //System.out.println(findLongest(new FileInputStream(FILENAME)));
            System.out.println(findLongestFunc(new FileInputStream(FILENAME)));
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}
