package Kolok1;

import java.util.*;
import java.io.*;
import java.util.function.Function;
import java.util.stream.Collectors;

enum TimeFormat {
    FORMAT_24, FORMAT_AMPM
}

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

class Time {
    int hour;
    int minute;

    public Time(String time) throws UnsupportedFormatException, InvalidTimeException {
        String[] parts = time.split("\\.");
        if(parts.length == 1) {
            parts = time.split(":");
        }
        if(parts.length == 1) {
            throw new InvalidTimeException(time);
        }
        hour = Integer.parseInt(parts[0]);
        minute = Integer.parseInt(parts[1]);
        if(hour > 23 || minute > 59 || hour < 0 || minute < 0) {
            throw new UnsupportedFormatException(time);
        }
    }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }

    public String convertAMToPM() {
        String part = "";
        if(hour == 0) {
            hour+=12;
            part = "AM";
        } else if(hour == 12) {
            part = "PM";
        } else if(hour > 12) {
            hour-=12;
            part = "PM";
        }
        return String.format("%2d:%02d %s", hour, minute,part);
    }

    @Override
    public String toString() {
        return String.format("%2d:%02d", hour, minute);
    }
}

class TimeTable {

    List<Time> times;

    public TimeTable() {
        times = new ArrayList<>();
    }

    void readTimes(InputStream inputStream) throws InvalidTimeException, UnsupportedFormatException {
        Scanner scanner = new Scanner(inputStream);
        while(scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] parts = line.split("\\s+");
            for (String part : parts) {
                Time time = new Time(part);
                times.add(time);
            }
        }
    }

    void writeTimes(OutputStream outputStream, TimeFormat format) {
        PrintWriter pw = new PrintWriter(outputStream);
        Function<Time,String> function = time -> {
          if(format.equals(TimeFormat.FORMAT_24)) {
              return format.toString();
          } else {
              return time.convertAMToPM();
          }
        };
        List<String> result = times.stream().sorted(Comparator.comparing(Time::getHour).thenComparing(Time::getMinute)).map(Time::toString).collect(Collectors.toList());
        result.forEach(System.out::println);
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
