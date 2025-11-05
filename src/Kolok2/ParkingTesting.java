package Kolok2;

import java.time.*;
import java.util.*;
import java.io.*;
import java.util.stream.Collectors;

class DateUtil {
    public static long durationBetween(LocalDateTime start, LocalDateTime end) {
        return Duration.between(start, end).toMinutes();
    }
}

class Vehicle {
    String registration;
    String spot;
    LocalDateTime timestamp;
    boolean entry;

    public Vehicle(String registration, String spot, LocalDateTime timestamp) {
        this.registration = registration;
        this.spot = spot;
        this.timestamp = timestamp;
    }

    public String getRegistration() {
        return registration;
    }

    public String getSpot() {
        return spot;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setRegistration(String registration) {
        this.registration = registration;
    }

    public void setSpot(String spot) {
        this.spot = spot;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return String.format("Registration number: %s Spot: %s Start timestamp: %s", registration,spot,timestamp);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Vehicle vehicle = (Vehicle) o;
        return entry == vehicle.entry && Objects.equals(registration, vehicle.registration) && Objects.equals(spot, vehicle.spot) && Objects.equals(timestamp, vehicle.timestamp);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(registration);
        result = 31 * result + Objects.hashCode(spot);
        result = 31 * result + Objects.hashCode(timestamp);
        result = 31 * result + Boolean.hashCode(entry);
        return result;
    }
}

class ParkingHistory {
    String registration;
    String spot;
    LocalDateTime startTimestamp;
    LocalDateTime endTimestamp;
    long duration;

    public ParkingHistory(String registration, String spot, LocalDateTime startTimestamp, LocalDateTime endTimestamp, long duration) {
        this.registration = registration;
        this.spot = spot;
        this.startTimestamp = startTimestamp;
        this.endTimestamp = endTimestamp;
        this.duration = duration;
    }

    public String getRegistration() {
        return registration;
    }

    public void setRegistration(String registration) {
        this.registration = registration;
    }

    public String getSpot() {
        return spot;
    }

    public void setSpot(String spot) {
        this.spot = spot;
    }

    public LocalDateTime getStartTimestamp() {
        return startTimestamp;
    }

    public void setStartTimestamp(LocalDateTime startTimestamp) {
        this.startTimestamp = startTimestamp;
    }

    public LocalDateTime getEndTimestamp() {
        return endTimestamp;
    }

    public void setEndTimestamp(LocalDateTime endTimestamp) {
        this.endTimestamp = endTimestamp;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    @Override
    public String toString() {
        return String.format("Registration number: %s Spot: %s Start timestamp: %s End timestamp: %s Duration in minutes: %d", registration,spot,getStartTimestamp(),getEndTimestamp(),duration);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ParkingHistory that = (ParkingHistory) o;
        return duration == that.duration && Objects.equals(registration, that.registration) && Objects.equals(spot, that.spot) && Objects.equals(startTimestamp, that.startTimestamp) && Objects.equals(endTimestamp, that.endTimestamp);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(registration);
        result = 31 * result + Objects.hashCode(spot);
        result = 31 * result + Objects.hashCode(startTimestamp);
        result = 31 * result + Objects.hashCode(endTimestamp);
        result = 31 * result + Long.hashCode(duration);
        return result;
    }
}

class Parking {

    int capacity;
    Map<String,Vehicle> vehicles;
    Map<String,Integer> carParkCount;
    List<ParkingHistory> history;

    public Parking(int capacity) {
        this.capacity = capacity;
        vehicles = new HashMap<>();
        carParkCount = new TreeMap<>();
        history = new ArrayList<>();
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    void update(String registration, String spot, LocalDateTime timestamp, boolean entry) {
        Vehicle vehicle = vehicles.get(registration);
        if(entry) {
            if(vehicle == null) {
                vehicle = new Vehicle(registration, spot, timestamp);
                vehicles.put(registration,vehicle);
                carParkCount.put(registration, carParkCount.getOrDefault(registration,0)+1);
            } else {
                vehicle.setSpot(spot);
                vehicle.setTimestamp(timestamp);
            }
        } else {
            if(vehicle != null) {
                long duration = DateUtil.durationBetween(vehicle.getTimestamp(),timestamp);
                history.add(new ParkingHistory(vehicle.getRegistration(), vehicle.getSpot(), vehicle.getTimestamp(),timestamp,duration));
            }
            vehicles.remove(registration);
        }
    }

    void currentState() {
        System.out.println(String.format("Capacity filled: %.2f%%", (double)vehicles.size()/capacity * 100));
        vehicles.values().stream().sorted(Comparator.comparing(Vehicle::getTimestamp).reversed())
                .forEach(vehicle -> System.out.println(vehicle.toString()));
    }

    void history() {
        history.sort((v1,v2) -> Long.compare(v2.getDuration(),v1.getDuration()));
        history.forEach(v -> System.out.println(v.toString()));
    }

    Map<String, Integer> carStatistics() {
        Map<String,Integer> result = new TreeMap<>();
        for (ParkingHistory parkingHistory : history) {
            result.put(parkingHistory.getRegistration(),result.getOrDefault(parkingHistory.getRegistration(),0)+1);
        }
        return result;
    }

    Map<String,Double> spotOccupancy (LocalDateTime start, LocalDateTime end) {
        return null;
    }
}

public class ParkingTesting {
    public static <K, V extends Comparable<V>> void printMapSortedByValue(Map<K, V> map) {
        map.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .forEach(entry -> System.out.println(String.format("%s -> %s", entry.getKey().toString(), entry.getValue().toString())));

    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int capacity = Integer.parseInt(sc.nextLine());

        Parking parking = new Parking(capacity);

        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            String[] parts = line.split("\\s+");
            if (parts[0].equals("update")) {
                String registration = parts[1];
                String spot = parts[2];
                LocalDateTime timestamp = LocalDateTime.parse(parts[3]);
                boolean entrance = Boolean.parseBoolean(parts[4]);
                parking.update(registration, spot, timestamp, entrance);
            } else if (parts[0].equals("currentState")) {
                System.out.println("PARKING CURRENT STATE");
                parking.currentState();
            } else if (parts[0].equals("history")) {
                System.out.println("PARKING HISTORY");
                parking.history();
            } else if (parts[0].equals("carStatistics")) {
                System.out.println("CAR STATISTICS");
                printMapSortedByValue(parking.carStatistics());
            } else if (parts[0].equals("spotOccupancy")) {
                LocalDateTime start = LocalDateTime.parse(parts[1]);
                LocalDateTime end = LocalDateTime.parse(parts[2]);
                printMapSortedByValue(parking.spotOccupancy(start, end));
            }
        }
    }
}
