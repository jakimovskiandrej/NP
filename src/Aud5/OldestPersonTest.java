package Aud5;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

class Person {
    String name;
    int age;

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
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
        return String.format("Name: %s, Age: %d", name, age);
    }
}

public class OldestPersonTest {
    public static void main(String[] args) throws FileNotFoundException {
        try(Scanner sc = new Scanner(new File("C:\\Users\\Hp\\IdeaProjects\\NP\\src\\Aud5\\datoteka"))) {
            int max = Integer.MIN_VALUE;
            String oldestName = null;
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                String[] parts = line.split("\\s+");
                String name = parts[0];
                int age = Integer.parseInt(parts[1]);
                if(age > max) {
                    max = age;
                    oldestName = name;
                }
            }
            System.out.printf("%s %d%n", oldestName, max);
        }
    }
}
