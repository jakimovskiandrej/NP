package Kolok2;

import java.io.*;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.Duration;
import java.time.LocalTime;
import java.util.*;
import java.util.List;

class Attender {

    String id;
    long time;

    public Attender(String id, long time) {
        this.id = id;
        this.time = time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }
}

class TeamRace {

    static void findBestTeam(InputStream is, OutputStream os) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        PrintWriter pw = new PrintWriter(os);
        String line;
        List<Attender> attenders = new ArrayList<>();
        while ((line = br.readLine()) != null && !line.isEmpty()) {
            String[] parts = line.split(" ");
            String id = parts[0];
            LocalTime startTime = LocalTime.parse(parts[1]);
            LocalTime endTime = LocalTime.parse(parts[2]);
            long time = Duration.between(startTime, endTime).getSeconds();
            attenders.add(new Attender(id,time));
        }
        attenders.sort(Comparator.comparing(Attender::getTime));
        List<Attender> bestTeam = attenders.subList(0,4);
        long total = 0;
        for (Attender attender : bestTeam) {
            long seconds = attender.getTime();
            total += seconds;
            pw.println(String.format("%s %02d:%02d:%02d", attender.getId(), seconds/3600, seconds%3600/60, seconds%3600%60));
        }
        pw.println(String.format("%02d:%02d:%02d", total/3600, total%3600/60, total%3600%60));
        pw.flush();
    }
}

public class RaceTest {
    public static void main(String[] args) {
        try {
            TeamRace.findBestTeam(System.in, System.out);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
