package Kolok2;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.function.Predicate;

class Measurement {
    float temperature;
    float wind;
    float humidity;
    float visibility;
    LocalDateTime date;

    public Measurement(float temperature, float wind, float humidity, float visibility, LocalDateTime date) {
        this.temperature = temperature;
        this.wind = wind;
        this.humidity = humidity;
        this.visibility = visibility;
        this.date = date;
    }

    public float getTemperature() {
        return temperature;
    }

    public float getWind() {
        return wind;
    }

    public float getHumidity() {
        return humidity;
    }

    public float getVisibility() {
        return visibility;
    }

    public LocalDateTime getDate() {
        return date;
    }

    @Override
    public String toString() {
        return String.format("%.1f %.1f km/h %.1f%% %.1f km %s", temperature, wind, humidity, visibility, date);
    }
}

class WeatherStation {

    List<Measurement> measurements;
    int n;

    public WeatherStation(int n) {
        measurements = new ArrayList<>();
        this.n = n;
    }

    public void addMeasurement(float temperature, float wind, float humidity, float visibility, LocalDateTime date) {
        measurements.add(new Measurement(temperature, wind, humidity, visibility, date));
    }

    public int total() {
        return measurements.size();
    }

    public void status(LocalDateTime from, LocalDateTime to) {
        Predicate<Measurement> isInRange = measurement -> !(measurement.getDate().isBefore(from) || measurement.getDate().isAfter(to));
        double avgTemperature = measurements.stream().mapToDouble(Measurement::getTemperature).average().orElse(0);
        measurements.stream().filter(isInRange).forEach(System.out::println);
        System.out.printf("Average temperature: %.2f\n", avgTemperature);
    }
}

public class WeatherStationTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
        int n = scanner.nextInt();
        scanner.nextLine();
        WeatherStation ws = new WeatherStation(n);
        while (true) {
            String line = scanner.nextLine();
            if (line.equals("=====")) {
                break;
            }
            String[] parts = line.split(" ");
            float temp = Float.parseFloat(parts[0]);
            float wind = Float.parseFloat(parts[1]);
            float hum = Float.parseFloat(parts[2]);
            float vis = Float.parseFloat(parts[3]);
            line = scanner.nextLine();
            LocalDateTime date = LocalDateTime.parse(line, timeFormatter);
            ws.addMeasurement(temp, wind, hum, vis, date);
        }
        String line = scanner.nextLine();
        LocalDateTime from = LocalDateTime.parse(line, timeFormatter);
        line = scanner.nextLine();
        LocalDateTime to = LocalDateTime.parse(line, timeFormatter);
        scanner.close();
        System.out.println(ws.total());
        try {
            ws.status(from, to);
        } catch (RuntimeException e) {
            System.out.println(e);
        }
    }
}
