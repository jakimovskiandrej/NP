package Kolok2;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

class Temperature {
    int day;
    List<Double> celsius;

    public Temperature(int day) {
        this.day = day;
        celsius = new ArrayList<Double>();
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public void addTemperature(double value,char scale) {
        if(scale == 'C') {
            celsius.add(value);
        } else if(scale == 'F') {
            celsius.add(fahrenheitToCelsius(value));
        }
    }

    public double getTemperaturesCount() {
        return celsius.size();
    }

    public double getMinTemperature() {
        return celsius.stream().mapToDouble(Double::doubleValue).min().orElse(0.0);
    }

    public double getMaxTemperature() {
        return celsius.stream().mapToDouble(Double::doubleValue).max().orElse(0.0);
    }

    public double getAverageTemperature() {
        return celsius.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
    }

    public double celsiusToFahrenheit(double celsius) {
        return (celsius * 9 / 5) + 32;
    }

    public double fahrenheitToCelsius(double fahrenheit) {
        return (fahrenheit - 32) * 5 / 9;
    }
}

class DailyTemperatures {

    List<Temperature> temperatures;

    public DailyTemperatures() {
        temperatures = new ArrayList<>();
    }

    void readTemperatures(InputStream inputStream) {
        Scanner sc = new Scanner(inputStream);
        while (sc.hasNextLine()) {
            String line = sc.nextLine().trim();
            if(!line.isEmpty()) {
                String[] parts = line.split("\\s+");
                int day = Integer.parseInt(parts[0]);
                Temperature temp = new Temperature(day);
                for(int i=1;i<parts.length;i++) {
                    String str = parts[i];
                    char scale = str.charAt(str.length()-1);
                    double value = Double.parseDouble(str.substring(0, str.length()-1));
                    temp.addTemperature(value, scale);
                }
                temperatures.add(temp);
            }
        }
    }
//[ден]: Count: [вк. мерења - 3 места] Min: [мин. температура] Max: [макс. температура] Avg: [просек ],
    //Минималната, максималната и просечната температура се печатат со 6 места, од кои 2 децимални, а по бројот се запишува во која скала е температурата (C/F).
    void writeDailyStats(OutputStream outputStream, char scale) {
        PrintWriter pw = new PrintWriter(outputStream);
        temperatures.sort(Comparator.comparingInt(Temperature::getDay));
        for (Temperature temperature : temperatures) {
            pw.printf("%3d ", temperature.getDay());
            if(scale == 'C') {
                pw.printf("Count: %3d Min: %6.2fC Max: %6.2fC Avg: %6.2fC\n", (int)temperature.getTemperaturesCount(),
                        temperature.getMinTemperature(),
                        temperature.getMaxTemperature(),
                        temperature.getAverageTemperature());
            } else if(scale == 'F') {
                pw.printf("Count: %3d Min: %6.2fF Max: %6.2fF Avg: %6.2fF\n", (int)temperature.getTemperaturesCount(),
                        temperature.celsiusToFahrenheit(temperature.getMinTemperature()),
                        temperature.celsiusToFahrenheit(temperature.getMaxTemperature()),
                        temperature.celsiusToFahrenheit(temperature.getAverageTemperature()));
            }
        }
        pw.flush();
    }
}

public class DailyTemperatureTest {
    public static void main(String[] args) {
        DailyTemperatures dailyTemperatures = new DailyTemperatures();
        dailyTemperatures.readTemperatures(System.in);
        System.out.println("=== Daily temperatures in Celsius (C) ===");
        dailyTemperatures.writeDailyStats(System.out, 'C');
        System.out.println("=== Daily temperatures in Fahrenheit (F) ===");
        dailyTemperatures.writeDailyStats(System.out, 'F');
    }
}
