package Kolok2;

import java.util.*;

class SeatNotAllowedException extends Exception {

}

class SeatTakenException extends Exception {

}

class Sector {
    String code;
    int seats;
    HashMap<Integer,Integer> taken;
    HashSet<Integer> types;

    public Sector(String code, int seats) {
        this.code = code;
        this.seats = seats;
        taken = new HashMap<>();
        types = new HashSet<>();
    }

    public String getCode() {
        return code;
    }

    public int getSeats() {
        return seats;
    }

    public HashMap<Integer, Integer> getTaken() {
        return taken;
    }

    public HashSet<Integer> getTypes() {
        return types;
    }

    public int freeSeats() {
        return seats - taken.size();
    }

    public void takeSeat(int seat, int type) throws SeatNotAllowedException {
        if(type == 1) {
            if(types.contains(2)) {
                throw new SeatNotAllowedException();
            }
        } else if(type == 2) {
            if(types.contains(1)) {
                throw new SeatNotAllowedException();
            }
        }
        types.add(type);
        taken.put(seat,type);
    }

    public boolean isTaken(int seat) {
        return taken.containsKey(seat);
    }

    @Override
    public String toString() {
        return String.format("%s\t%d/%d\t%.1f", code, freeSeats(), seats, (seats-freeSeats()) * 100.0/seats);
    }
}

class Stadium {
    String name;
    HashMap<String,Sector> sectors;

    public Stadium(String name) {
        this.name = name;
        sectors = new HashMap<>();
    }

    public void createSectors(String[] sectorNames, int[] sizes) {
        for(int i=0;i<sectorNames.length;i++) {
            addSector(sectorNames[i],sizes[i]);
        }
    }

    public void addSector(String name, int sizes) {
        Sector sector = new Sector(name,sizes);
        sectors.put(name,sector);
    }

    public void buyTicket(String sectorName, int seat, int type) throws SeatTakenException, SeatNotAllowedException {
        Sector sector = sectors.get(sectorName);
        if(sector.isTaken(seat)) {
            throw new SeatTakenException();
        }
        sector.takeSeat(seat,type);
    }

    public void showSectors() {
        sectors.values().stream()
                .sorted(Comparator.comparingInt(Sector::freeSeats).thenComparing(Sector::getCode).reversed()).forEach(System.out::println);
    }
}

public class StadiumTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        String[] sectorNames = new String[n];
        int[] sectorSizes = new int[n];
        String name = scanner.nextLine();
        for (int i = 0; i < n; ++i) {
            String line = scanner.nextLine();
            String[] parts = line.split(";");
            sectorNames[i] = parts[0];
            sectorSizes[i] = Integer.parseInt(parts[1]);
        }
        Stadium stadium = new Stadium(name);
        stadium.createSectors(sectorNames, sectorSizes);
        n = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < n; ++i) {
            String line = scanner.nextLine();
            String[] parts = line.split(";");
            try {
                stadium.buyTicket(parts[0], Integer.parseInt(parts[1]),
                        Integer.parseInt(parts[2]));
            } catch (SeatNotAllowedException e) {
                System.out.println("SeatNotAllowedException");
            } catch (SeatTakenException e) {
                System.out.println("SeatTakenException");
            }
        }
        stadium.showSectors();
    }
}
