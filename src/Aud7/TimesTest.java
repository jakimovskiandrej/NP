package Aud7;

import java.io.*;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

class UnsupportedFormatException extends Exception {
    public UnsupportedFormatException(String message) {
        super(message);
    }
}

class InvalidTimeException extends Exception {
    public InvalidTimeException(String message) {
        super(message);
    }
}

enum TimeFormat {
    FORMAT_24, FORMAT_AMPM
}

class Time {
    int hour;
    int minute;

    public Time(String line) throws UnsupportedFormatException, InvalidTimeException {
        String[] parts = line.split("\\.");
        if(parts.length == 1) {
            parts = line.split(":");
        }
        if(parts.length == 1) {
            throw new UnsupportedFormatException(line);
        }
        this.hour = Integer.parseInt(parts[0]);
        this.minute = Integer.parseInt(parts[1]);
        if(this.hour > 23 || this.hour < 0 || this.minute > 59 || this.minute < 0) {
            throw new InvalidTimeException(line);
        }
    }

    public String convertAMtoPM() {
        String part = "AM";
        int h = hour;
        if(hour == 0) {
            h+=12;
        } else if(hour == 12) {
            h+=12;
            part = "PM";
        }
        else if(hour > 12) {
            h-=12;
            part = "PM";
        }
        return String.format("%02d:%02d %s", h, minute, part);
    }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }
}

class TimeTable {

    List<Time> times;

    public TimeTable() {
        times = new ArrayList<Time>();
    }

    public void readTimes(InputStream inputStream) throws InvalidTimeException, UnsupportedFormatException {
        Scanner scanner = new Scanner(inputStream);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] parts = line.split("\\s+");
            for (String part : parts) {
                Time time = new Time(part);
                times.add(time);
            }
        }
    }

    public void writeTimes(OutputStream outputStream, TimeFormat format) {
        PrintWriter pw = new PrintWriter(outputStream);

        Function<Time,String> result = time -> {
            if(format.equals(TimeFormat.FORMAT_24)) {
                return time.toString();
            }
            else return time.convertAMtoPM();
        };

        List<String> timesToPrint = times.stream().sorted(Comparator.comparing(Time::getHour).thenComparing(Time::getMinute)).map(result).collect(Collectors.toList());
        timesToPrint.forEach(pw::println);
        pw.flush();
    }
}

public class TimesTest {
    public static void main(String[] args) {
        TimeTable timeTable = new TimeTable();
        try {
            timeTable.readTimes(System.in);
        } catch (UnsupportedFormatException e) {
            System.out.println("UnsupportedFormatException: " + e.getMessage());
        } catch (InvalidTimeException e) {
            System.out.println("InvalidTimeException: " + e.getMessage());
        }
        System.out.println("24 HOUR FORMAT");
        timeTable.writeTimes(System.out, TimeFormat.FORMAT_24);
        System.out.println("AM/PM FORMAT");
        timeTable.writeTimes(System.out, TimeFormat.FORMAT_AMPM);
    }
}
