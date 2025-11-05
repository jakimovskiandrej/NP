package Kolok2;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

class Airport {
    //String name, String country, String code, int passengers
    String name;
    String country;
    String code;
    int passengers;

    public Airport(String name, String country, String code, int passengers) {
        this.name = name;
        this.country = country;
        this.code = code;
        this.passengers = passengers;
    }

    public String getName() {
        return name;
    }

    public String getCountry() {
        return country;
    }

    public String getCode() {
        return code;
    }

    public int getPassengers() {
        return passengers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Airport airport = (Airport) o;
        return passengers == airport.passengers && Objects.equals(name, airport.name) && Objects.equals(country, airport.country) && Objects.equals(code, airport.code);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(name);
        result = 31 * result + Objects.hashCode(country);
        result = 31 * result + Objects.hashCode(code);
        result = 31 * result + passengers;
        return result;
    }
}

class Flight {
    
    String from;
    String to;
    int time;
    int duration;

    public Flight(String from, String to, int time, int duration) {
        this.from = from;
        this.to = to;
        this.time = time;
        this.duration = duration;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String formatTime(int minutes) {
        int hours = (minutes/60) % 24;
        int minute = minutes % 60;
        return String.format("%02d:%02d", hours,minute);
    }

    public String formatDuration() {
        int hours = duration / 60;
        int minutes = duration % 60;
        return String.format("%dh%02dm", hours,minutes);
    }

    private String calculateArrivalTime() {
        int result = time + duration;
        return formatTime(result);
    }

    @Override
    public String toString() {
        String departure = formatTime(time);
        String arrival = calculateArrivalTime();
        String flight = formatDuration();
        String res = "";
        if(time + duration > 1440) {
            res = " +1d";
        }
        return String.format("%s-%s %s-%s%s %s", from,to,departure,arrival,res,flight);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Flight flight = (Flight) o;
        return time == flight.time && duration == flight.duration && Objects.equals(from, flight.from) && Objects.equals(to, flight.to);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(from);
        result = 31 * result + Objects.hashCode(to);
        result = 31 * result + time;
        result = 31 * result + duration;
        return result;
    }
}

class Airports {

    List<Airport> airports;
    Map<String,List<Flight>> flights;

    public Airports() {
        airports = new ArrayList<>();
        flights = new HashMap<>();
    }

    public void addAirport(String name, String country, String code, int passengers) {
        airports.add(new Airport(name,country,code,passengers));
    }

    public void addFlights(String from, String to, int time, int duration) {
        Flight flight = new Flight(from,to,time,duration);
        flights.computeIfAbsent(from,k->new ArrayList<>()).add(flight);
    }

    public void showFlightsFromAirport(String code) {
        List<Flight> result = flights.getOrDefault(code,new ArrayList<>());
        for (Airport airport : airports) {
            if(airport.code.equals(code)) {
                System.out.println(String.format("%s (%s)\n %s\n %s", airport.getName(),airport.getCode(),airport.getCountry(),airport.getPassengers()));
                break;
            }
        }
        result.sort(Comparator.comparingInt(Flight::getTime).thenComparingInt(Flight::getDuration));
        for(int i=0;i<result.size();i++) {
            Flight flight = result.get(i);
            System.out.printf("%d. %s\n", i+1,flight);
        }
    }

    public void showDirectFlightsFromTo(String from, String to) {
        List<Flight> result = flights.getOrDefault(from,new ArrayList<>());
        boolean found = false;
        for(Flight flight : result) {
            if(flight.getTo().equals(to)) {
                System.out.println(flight);
                found = true;
            }
        }
        if(!found) {
            System.out.printf("No flights from %s to %s\n",from,to);
        }
    }

    public void showDirectFlightsTo(String to) {
        List<Flight> result = new ArrayList<>();
        boolean found = false;

        for (List<Flight> value : flights.values()) {
            for (Flight flight : value) {
                if(flight.getTo().equals(to)) {
                    result.add(flight);
                }
            }
        }
        for(int i=0;i<result.size()-1;i++) {
            for(int j=i+1;j<result.size();j++) {
                if(result.get(i).getTime() > result.get(j).getTime()) {
                    Flight tmp = result.get(i);
                    result.set(i,result.get(j));
                    result.set(j,tmp);
                }
            }
        }
        for (Flight flight : result) {
            System.out.println(flight);
            found = true;
        }
        if(!found) {
            System.out.printf("No flights to %s",to);
        }

    }

}

public class AirportsTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Airports airports = new Airports();
        int n = scanner.nextInt();
        scanner.nextLine();
        String[] codes = new String[n];
        for (int i = 0; i < n; ++i) {
            String al = scanner.nextLine();
            String[] parts = al.split(";");
            airports.addAirport(parts[0], parts[1], parts[2], Integer.parseInt(parts[3]));
            codes[i] = parts[2];
        }
        int nn = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < nn; ++i) {
            String fl = scanner.nextLine();
            String[] parts = fl.split(";");
            airports.addFlights(parts[0], parts[1], Integer.parseInt(parts[2]), Integer.parseInt(parts[3]));
        }
        int f = scanner.nextInt();
        int t = scanner.nextInt();
        String from = codes[f];
        String to = codes[t];
        System.out.printf("===== FLIGHTS FROM %S =====\n", from);
        airports.showFlightsFromAirport(from);
        System.out.printf("===== DIRECT FLIGHTS FROM %S TO %S =====\n", from, to);
        airports.showDirectFlightsFromTo(from, to);
        t += 5;
        t = t % n;
        to = codes[t];
        System.out.printf("===== DIRECT FLIGHTS TO %S =====\n", to);
        airports.showDirectFlightsTo(to);
    }
}
