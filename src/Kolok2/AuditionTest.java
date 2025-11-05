package Kolok2;

import java.util.*;
import java.util.stream.Collectors;

class Participant implements Comparable<Participant> {
    //String city, String code, String name, int age
    String city;
    String code;
    String name;
    int age;

    public Participant(String city, String code, String name, int age) {
        this.city = city;
        this.code = code;
        this.name = name;
        this.age = age;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return String.format("%s %s %d", code, name, age);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Participant that = (Participant) o;
        return age == that.age && Objects.equals(city, that.city) && Objects.equals(code, that.code) && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(city);
        result = 31 * result + Objects.hashCode(code);
        result = 31 * result + Objects.hashCode(name);
        result = 31 * result + age;
        return result;
    }

    @Override
    public int compareTo(Participant o) {
        return Integer.parseInt(this.code,Integer.parseInt(o.code));
    }
}

class Audition {

    Map<String,Map<String,Participant>> participants;

    public Audition() {
        participants = new HashMap<>();
    }

    void addParticpant(String city, String code, String name, int age) {
        participants.putIfAbsent(city, new HashMap<>());
        Map<String,Participant> map = participants.get(city);
        if(!map.containsKey(city)) {
            map.putIfAbsent(city, new Participant(city,code,name,age));
        }
    }

    void listByCity(String city) {
        if(participants.containsKey(city)) {
            Map<String,Participant> map = participants.get(city);
            map.values().stream().sorted(Comparator.comparing(Participant::getName).thenComparing(Participant::getAge)).forEach(System.out::println);
        }
    }
}

public class AuditionTest {
    public static void main(String[] args) {
        Audition audition = new Audition();
        List<String> cities = new ArrayList<String>();
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] parts = line.split(";");
            if (parts.length > 1) {
                audition.addParticpant(parts[0], parts[1], parts[2],
                        Integer.parseInt(parts[3]));
            } else {
                cities.add(line);
            }
        }
        for (String city : cities) {
            System.out.printf("+++++ %s +++++\n", city);
            audition.listByCity(city);
        }
        scanner.close();
    }
}
