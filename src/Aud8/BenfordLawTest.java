package Aud8;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

interface NumbersReader {
    List<Integer> read(InputStream inputStream);
}

class LineNumbersReader implements NumbersReader {
    @Override
    public List<Integer> read(InputStream inputStream) {
        try(BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            return br.lines()
                    .filter(line -> !line.isEmpty())
                    .map(line -> Integer.parseInt(line.trim()))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

class CountVisualizer {
    final int n;

    public CountVisualizer(int n) {
        this.n = n;
    }

    public void visualize(OutputStream out, int[] counts) {
        PrintWriter pw = new PrintWriter(out);
        for (int count : counts) {
            while (count > 0) {
                pw.printf("*");
                count-=n;
            }
            pw.println();
        }
        pw.flush();
    }
}

public class BenfordLawTest {
    public static void main(String[] args) throws FileNotFoundException {
        final String INPUT_FILE = "examples/data/librarybooks.txt";
        NumbersReader numbersReader = new LineNumbersReader();
        List<Integer> numbers = numbersReader.read(new FileInputStream(INPUT_FILE));
        BenfordLawTest benfordLawTest = new BenfordLawTest();
        int[] count = benfordLawTest.counts(numbers);
        CountVisualizer visualizer = new CountVisualizer(100);
        visualizer.visualize(System.out, count);
    }
    public int[] counts(List<Integer> numbers) {
        int[] result = new int[10];
        for (Integer number : numbers) {
            int digit = firstDigit(number);
            result[digit]++;
        }
        return result;
    }

    public int[] countsFunc(List<Integer> numbers) {
        return numbers.stream()
                .map(BenfordLawTest::firstDigit)
                .map(x -> {
                    int[] res = new int[10];
                    res[x]++;
                    return res;
                })
                .reduce(new int[10], (left, right) -> {
                    Arrays.setAll(left, i -> left[i] + right[i]);
                    return left;
                });
    }

    static int firstDigit(int num) {
        while (num >= 10) {
            num /= 10;
        }
        return num;
    }
}
