package Aud10;

import java.util.HashSet;
import java.util.Map;
import java.util.Random;

class BirthdayParadox {
    int maxPeople;
    static int TRIALS = 5000;

    public BirthdayParadox(int maxPeople) {
        this.maxPeople = maxPeople;
    }

    public void conductExperiment() {
        for(int i=2;i<=maxPeople;i++) {
            System.out.println(String.format("%d ---> %.2f", i, runSimulation(i)));
        }
    }

    public double runSimulation(int people) {
        int counter = 0;
        for(int i=0;i<TRIALS;i++) {
            if(runTrial(people)) {
                counter++;
            }
        }
        return counter;
    }

    public boolean runTrial(int people) {
        HashSet<Integer> birthdays = new HashSet<>();
        Random rand = new Random();
        for(int i=0;i<people;i++) {
            int birthday = rand.nextInt(365)+1;
            if(birthdays.contains(birthday)) {
                return true;
            } else {
                birthdays.add(birthday);
            }
        }
        return false;
    }
}

public class Birthdays {
    public static void main(String[] args) {
        BirthdayParadox bp = new BirthdayParadox(50);
        bp.conductExperiment();
    }
}
