package Kolok1;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

class Weather {

    private final double temperature;
    private final double humidity;
    private final double windSpeed;
    private final double visibility;
    private final Date time;

    public Weather(double temperature, double windSpeed, double humidity, double visibility, Date time) {
        this.temperature = temperature;
        this.windSpeed = windSpeed;
        this.humidity = humidity;
        this.visibility = visibility;
        this.time = time;
    }

    public double getTemperature() {
        return temperature;
    }

    public double getHumidity() {
        return humidity;
    }

    public double getWindSpeed() {
        return windSpeed;
    }

    public double getVisibility() {
        return visibility;
    }

    public Date getTime() {
        return time;
    }

    @Override
    public String toString() {
        SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss 'GMT' yyyy");
        return String.format("%.1f %.1f km/h %.1f%% %.1f km %s", temperature, windSpeed, humidity, visibility, formatter.format(time));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Weather weather = (Weather) o;
        return Double.compare(temperature, weather.temperature) == 0 && Double.compare(humidity, weather.humidity) == 0 && Double.compare(windSpeed, weather.windSpeed) == 0 && Double.compare(visibility, weather.visibility) == 0 && Objects.equals(time, weather.time);
    }

    @Override
    public int hashCode() {
        int result = Double.hashCode(temperature);
        result = 31 * result + Double.hashCode(humidity);
        result = 31 * result + Double.hashCode(windSpeed);
        result = 31 * result + Double.hashCode(visibility);
        result = 31 * result + Objects.hashCode(time);
        return result;
    }
}

class WeatherStation {

    private List<Weather> list;
    private final int days;

    public WeatherStation(int days) {
        list = new ArrayList<>();
        this.days = days;
    }

    public void addMeasurment(float temperature, float wind, float humidity, float visibility, Date date) {
        list.removeIf(w -> w.getTime().before(new Date(date.getTime() - (days * 24 * 60 * 60 * 1000))));

        for (Weather weather : list) {
            long diff = Math.abs(date.getTime() - weather.getTime().getTime());
            if(diff < 2.5 * 60 * 1000) {
                return;
            }
        }

        list.add(new Weather(temperature, wind, humidity, visibility, date));
    }

    public int total() {
        return list.size();
    }


    public void status(Date from,Date to) throws RuntimeException {
        List<Weather> result = list.stream()
                .filter(w -> !w.getTime().before(from) && !w.getTime().after(to))
                .distinct().sorted(Comparator.comparing(Weather::getTime))
                .collect(Collectors.toList());

        if(result.isEmpty()) {
            throw new RuntimeException();
        }

        for (Weather weather : result) {
            System.out.println(weather.toString());
        }

        double averageTemperature = result.stream()
                .mapToDouble(Weather::getTemperature)
                .average().orElse(0.0);

        System.out.printf("Average temperature: %.2f", averageTemperature);

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WeatherStation that = (WeatherStation) o;
        return days == that.days && Objects.equals(list, that.list);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(list);
        result = 31 * result + days;
        return result;
    }
}

public class WeatherStationTest {
    public static void main(String[] args) throws ParseException {
        Scanner scanner = new Scanner(System.in);
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
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
            Date date = df.parse(line);
            ws.addMeasurment(temp, wind, hum, vis, date);
        }
        String line = scanner.nextLine();
        Date from = df.parse(line);
        line = scanner.nextLine();
        Date to = df.parse(line);
        scanner.close();
        System.out.println(ws.total());
        try {
            ws.status(from, to);
        } catch (RuntimeException e) {
            System.out.println(e);
        }
    }
}