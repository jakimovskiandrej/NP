 package Aud5;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class WordTest {

    private static String processFile(String filename) throws IOException {
        int lines = 0;
        int words = 0;
        int chars = 0;
        try(BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line = br.readLine();
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\s+");
                lines++;
                words += parts.length;
                chars += line.length() + 1;
            }
        }
        return String.format("Lines: %d, Words: %d, Chars: %d", lines, words, chars);
    }


    public static void main(String[] args) {

    }
}
