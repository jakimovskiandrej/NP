package Kolok1;

import java.io.*;
import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

class SubtitlesElement {
    private int index;
    private LocalTime start;
    private LocalTime end;
    private String text;

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss,SSS");

    SubtitlesElement(int index, LocalTime start, LocalTime end, String text) {
        this.index = index;
        this.start = start;
        this.end = end;
        this.text = text;
    }

    public int getIndex() {
        return index;
    }

    public LocalTime getStart() {
        return start;
    }

    public LocalTime getEnd() {
        return end;
    }

    public String getText() {
        return text;
    }

    public void shiftBy(int ms) {
        Duration duration = Duration.ofMillis(ms);
        start = start.plus(duration);
        end = end.plus(duration);
    }

    @Override
    public String toString() {
        return String.format("%d\n%s --> %s\n%s", index, start.format(TIME_FORMATTER), end.format(TIME_FORMATTER), text);
    }
}

class Subtitles {

    private List<SubtitlesElement> list;

    public Subtitles() {
        list = new ArrayList<>();
    }

    public int loadSubtitles(InputStream inputStream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        int counter = 0;

        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss,SSS");
        while ((line = reader.readLine())!=null) {
            if(line.isEmpty()) {
                continue;
            }
            String[] parts = line.split(" ");
            int index = Integer.parseInt(parts[0]);
            line = reader.readLine();
            if(line!=null && line.contains(" --> ")) {
                String[] timeLine = line.split(" --> ");
                String startStr = timeLine[0];
                String endStr = timeLine[1];
                LocalTime start = LocalTime.parse(startStr, timeFormatter);
                LocalTime end = LocalTime.parse(endStr, timeFormatter);
                StringBuilder sb = new StringBuilder();
                while ((line = reader.readLine())!=null && !line.isEmpty()) {
                    sb.append(line).append("\n");
                }

                list.add(new SubtitlesElement(index,start,end,sb.toString().trim()));
                counter++;
            }
        }
        return counter;
    }

    public void print() {
        for (SubtitlesElement subtitlesElement : list) {
            System.out.println(subtitlesElement);
            System.out.println();
        }
    }

    public void shift(int ms) {
        for (SubtitlesElement subtitlesElement : list) {
            subtitlesElement.shiftBy(ms);
        }
    }

}

public class SubtitlesTest {
    public static void main(String[] args) {
        Subtitles subtitles = new Subtitles();
        int n = 0;
        try {
            n = subtitles.loadSubtitles(System.in);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("+++++ ORIGINIAL SUBTITLES +++++");
        subtitles.print();
        int shift = n * 37;
        shift = (shift % 2 == 1) ? -shift : shift;
        System.out.println(String.format("SHIFT FOR %d ms", shift));
        subtitles.shift(shift);
        System.out.println("+++++ SHIFTED SUBTITLES +++++");
        subtitles.print();
    }
}
