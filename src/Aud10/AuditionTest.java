package Aud10;

import java.util.*;

class Participant {
    String city;
    String code;
    String name;
    int age;

    public Participant(String code, String name, int age) {
        this.code = code;
        this.name = name;
        this.age = age;
    }

    public String getCity() {
        return city;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
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
    public String toString() {
        return String.format("%s %s %s %d", city, code, name, age);
    }
}

class Audition {

    HashMap<String,HashSet<Participant>> participants;

    public Audition() {
        participants = new HashMap<>();
    }

    public void addParticipant(String city, String code, String name, int age) {
        Set<Participant> cityParticipants = participants.get(city);
        if (cityParticipants == null) {
            cityParticipants = new HashSet<>();
        } else {
            cityParticipants.add(new Participant(code, name, age));
        }
    }

    public void listByCity(String city) {
        participants.get(city).stream().sorted(Comparator.comparing(Participant::getName).thenComparing(Participant::getAge))
                        .forEach(participant -> System.out.println(participant.toString()));
    }

}

public class AuditionTest {
    public static void main(String[] args) {
        Audition audition = new Audition();
        List<String> cities = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] parts = line.split(";");
            if (parts.length > 1) {
                audition.addParticipant(parts[0], parts[1], parts[2],
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
