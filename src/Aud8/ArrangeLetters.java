package Aud8;

import java.util.Arrays;

public class ArrangeLetters {

    public String arrange(String input) {
        String[] parts = input.split("\\s+");
        for(int i = 0; i < parts.length; i++) {
            char[] w = parts[i].toCharArray();
            Arrays.sort(w);
            parts[i] = String.valueOf(w);
        }
        return String.join(" ", parts);
    }

    public static void main(String[] args) {
        ArrangeLetters al = new ArrangeLetters();
        String input = "Hello World from FINKI";
        String arranged = al.arrange(input);
        System.out.println("Original: " + input);
        System.out.println("Arranged: " + arranged);
    }
}
