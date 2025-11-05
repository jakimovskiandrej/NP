package Kolok1;

import java.io.*;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

class Racer {
    //Driver_name lap1 lap2 lap3
    String name;
    LocalTime time1;
    LocalTime time2;
    LocalTime time3;

    public Racer(String name, LocalTime time1, LocalTime time2, LocalTime time3) {
        this.name = name;
        this.time1 = time1;
        this.time2 = time2;
        this.time3 = time3;
    }

    public String getName() {
        return name;
    }

    public LocalTime getTime1() {
        return time1;
    }

    public LocalTime getTime2() {
        return time2;
    }

    public LocalTime getTime3() {
        return time3;
    }

    public LocalTime getBestTime() {
        return Collections.min(Arrays.asList(time1, time2, time3));
    }

    @Override
    public String toString() {
        return String.format("%-12s%2d:%02d:%03d", name, getBestTime().getMinute(), getBestTime().getSecond(), getBestTime().getNano()/1000000);
    }
}

class F1Race {

    List<Racer> racers;

    public F1Race() {
        racers = new ArrayList<>();
    }

    private LocalTime parseTime(String timeString) {
        String[] parts = timeString.split(":");
        int minutes = Integer.parseInt(parts[0]);
        int seconds = Integer.parseInt(parts[1]);
        int milliseconds = Integer.parseInt(parts[2]);
        return LocalTime.of(0 ,minutes, seconds).plusNanos(milliseconds*1000000L);
    }

    public void readResults(InputStream inputStream) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        while ((line = br.readLine()) != null && !line.isEmpty()) {
            String[] parts = line.split("\\s+");
            String name = parts[0];
            LocalTime time1 = parseTime(parts[1]);
            LocalTime time2 = parseTime(parts[2]);
            LocalTime time3 = parseTime(parts[3]);
            racers.add(new Racer(name, time1, time2, time3));
        }
    }

    public void printSorted(OutputStream outputStream) {
        PrintWriter pw = new PrintWriter(outputStream);
        racers.sort(Comparator.comparing(Racer::getBestTime));
        int index = 1;
        for (Racer racer : racers) {
            pw.printf(String.format("%d. %-12s%d:%02d:%03d\n", index++,racer.getName(),racer.getBestTime().getMinute(),racer.getBestTime().getSecond(),racer.getBestTime().getNano()/1000000));
        }
        pw.flush();
    }

}

public class F1Test {
    public static void main(String[] args) {
        F1Race f1Race = new F1Race();
        try {
            f1Race.readResults(System.in);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        f1Race.printSorted(System.out);
    }
}
