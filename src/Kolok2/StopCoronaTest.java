package Kolok2;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

interface ILocation{
    double getLongitude();

    double getLatitude();

    LocalDateTime getTimestamp();
}

class UserIdAlreadyExistsException extends Exception {
    public UserIdAlreadyExistsException(String message) {
        super(message);
    }
}

class User {
    //String name, String id
    String name;
    String id;

    public User(String name, String id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;
        return Objects.equals(name, user.name) && Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(name);
        result = 31 * result + Objects.hashCode(id);
        return result;
    }
}

class Location {
    String id;
    List<ILocation> locations;

    public Location(String id, List<ILocation> locations) {
        this.id = id;
        this.locations = locations;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<ILocation> getLocations() {
        return locations;
    }

    public void setLocations(List<ILocation> locations) {
        this.locations = locations;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Location location = (Location) o;
        return Objects.equals(id, location.id) && Objects.equals(locations, location.locations);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(id);
        result = 31 * result + Objects.hashCode(locations);
        return result;
    }
}

class StopCoronaApp {
    List<User> users;
    Map<String,List<ILocation>> locations;
    Set<String> infectedUsers;

    public StopCoronaApp() {
        users = new ArrayList<>();
        locations = new HashMap<>();
    }

    void addUser(String name, String id) throws UserIdAlreadyExistsException {
        for (User user : users) {
            if(user.getId().equals(id)) {
                throw new UserIdAlreadyExistsException("User with id " + id + " already exists");
            }
        }
        users.add(new User(name, id));
    }

    void addLocations (String id, List<ILocation> iLocations) {
        locations.putIfAbsent(id, new ArrayList<>());
        locations.get(id).addAll(iLocations);
    }

    void detectNewCase (String id, LocalDateTime timestamp) {
        if(infectedUsers == null) {
            infectedUsers = new HashSet<>();
        }
        infectedUsers.add(id);
        if(infectedUsers == null) {
            return;
        }
        List<ILocation> infectedLocations = locations.get(id);
        for (Map.Entry<String, List<ILocation>> string : locations.entrySet()) {
            String otherId = string.getKey();
            List<ILocation> otherLocations = string.getValue();
            for (ILocation infectedLocation : infectedLocations) {
                for (ILocation otherLocation : otherLocations) {
                    double distance = calculateDistance(infectedLocation.getLatitude() , infectedLocation.getLongitude(), otherLocation.getLatitude(), otherLocation.getLongitude());
                    double timeDiff = Duration.between(infectedLocation.getTimestamp(), otherLocation.getTimestamp()).abs().getSeconds();
                    if(distance <= 2.0 && timeDiff <= 300) {
                        infectedUsers.add(otherId);
                        break;
                    }
                }
            }
        }
    }

    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        double R = 6371e3;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double d = R * c;
        return d;
    }

    void createReport() {
        int totalDirectContacts = 0;
        int totalIndirectContacts = 0;
        int infectedCount = infectedUsers.size();

        for (String infectedId : infectedUsers) {
            User infectedUser = null;
            for (User user : users) {
                if (user.getId().equals(infectedId)) {
                    infectedUser = user;
                    break;
                }
            }
            if (infectedUser == null) continue;
            List<ILocation> userLocations = locations.get(infectedId);
            LocalDateTime detectedTime = null;
            if (userLocations != null) {
                for (ILocation loc : userLocations) {
                    if (infectedUsers.contains(infectedId)) {
                        detectedTime = loc.getTimestamp();
                        break;
                    }
                }
            }

            System.out.printf("%s %s %s\n", infectedUser.getName(), infectedUser.getId(), detectedTime);

            Map<User, Integer> directContacts = getDirectContacts(infectedUser);
            totalDirectContacts += directContacts.size();

            System.out.println("Direct contacts:");
            for (Map.Entry<User, Integer> entry : directContacts.entrySet()) {
                User contact = entry.getKey();
                int times = entry.getValue();
                System.out.printf("%s %s %d\n", contact.getName(), contact.getId().substring(0, 5), times);
            }
            System.out.printf("Count of direct contacts: %d\n", directContacts.size());

            Collection<User> indirectContacts = getIndirectContacts(infectedUser);
            totalIndirectContacts += indirectContacts.size();

            System.out.println("Indirect contacts:");
            for (User contact : indirectContacts) {
                System.out.printf("%s %s\n", contact.getName(), contact.getId().substring(0, 5));
            }
            System.out.printf("Count of indirect contacts: %d\n", indirectContacts.size());
        }

        double avgDirect = infectedCount == 0 ? 0 : (double) totalDirectContacts / infectedCount;
        double avgIndirect = infectedCount == 0 ? 0 : (double) totalIndirectContacts / infectedCount;

        System.out.printf("Average direct contacts: %.2f\n", avgDirect);
        System.out.printf("Average indirect contacts: %.2f\n", avgIndirect);
    }


    public Map<User, Integer> getDirectContacts (User u) {
        Map<User, Integer> contacts = new HashMap<>();
        List<ILocation> userLocations = locations.get(u.getId());
        for (User user : users) {
            List<ILocation> otherLocations = locations.get(user.getId());
            for (ILocation loc1 : userLocations) {
                for (ILocation loc2 : otherLocations) {
                    double distance = calculateDistance(loc1.getLatitude(), loc2.getLatitude(), loc1.getLongitude(), loc2.getLongitude());
                    double timeDiff = Duration.between(loc1.getTimestamp(), loc2.getTimestamp()).abs().getSeconds();
                    if(distance <= 2.0 && timeDiff <= 300) {
                        contacts.put(user, contacts.getOrDefault(user, 0) + 1);
                    }
                }
            }
        }
        return contacts;
    }

    public Collection<User> getIndirectContacts(User u) {
        Set<User> indirectContacts = new HashSet<>();
        Map<User, Integer> direct = getDirectContacts(u);
        Set<User> directContacts = direct.keySet();

        for (User directContact : directContacts) {
            Map<User, Integer> directOfDirect = getDirectContacts(directContact);
            for (User possibleIndirect : directOfDirect.keySet()) {
                if (!possibleIndirect.equals(u) &&
                        !directContacts.contains(possibleIndirect)) {
                    indirectContacts.add(possibleIndirect);
                }
            }
        }
        return indirectContacts;
    }


}

public class StopCoronaTest {

    public static double timeBetweenInSeconds(ILocation location1, ILocation location2) {
        return Math.abs(Duration.between(location1.getTimestamp(), location2.getTimestamp()).getSeconds());
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        StopCoronaApp stopCoronaApp = new StopCoronaApp();

        while (sc.hasNext()) {
            String line = sc.nextLine();
            String[] parts = line.split("\\s+");

            switch (parts[0]) {
                case "REG": //register
                    String name = parts[1];
                    String id = parts[2];
                    try {
                        stopCoronaApp.addUser(name, id);
                    } catch (UserIdAlreadyExistsException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case "LOC": //add locations
                    id = parts[1];
                    List<ILocation> locations = new ArrayList<>();
                    for (int i = 2; i < parts.length; i += 3) {
                        locations.add(createLocationObject(parts[i], parts[i + 1], parts[i + 2]));
                    }
                    stopCoronaApp.addLocations(id, locations);

                    break;
                case "DET": //detect new cases
                    id = parts[1];
                    LocalDateTime timestamp = LocalDateTime.parse(parts[2]);
                    stopCoronaApp.detectNewCase(id, timestamp);

                    break;
                case "REP": //print report
                    stopCoronaApp.createReport();
                    break;
                default:
                    break;
            }
        }
    }

    private static ILocation createLocationObject(String lon, String lat, String timestamp) {
        return new ILocation() {
            @Override
            public double getLongitude() {
                return Double.parseDouble(lon);
            }

            @Override
            public double getLatitude() {
                return Double.parseDouble(lat);
            }

            @Override
            public LocalDateTime getTimestamp() {
                return LocalDateTime.parse(timestamp);
            }
        };
    }
}
