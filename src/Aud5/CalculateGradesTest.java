package Aud5;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

class Student implements Comparable<Student>{

    private String firstName;
    private String lastName;
    private int exam1;
    private int exam2;
    private int exam3;
    private char grade;
    private double total;

    public Student(String firstName, String lastName, int exam1, int exam2, int exam3) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.exam1 = exam1;
        this.exam2 = exam2;
        this.exam3 = exam3;
        this.total = (exam1 * 0.25) + (exam2 * 0.3) + (exam3 * 0.45);
        if(total <= 60) {
            grade = 'F';
        } else if (total > 60 && total <= 70) {
            grade = 'D';
        } else if (total > 70 && total <= 80) {
            grade = 'C';
        } else if (total > 80 && total <= 90) {
            grade = 'B';
        } else if (total > 90 && total <= 100) {
            grade = 'A';
        }
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public int getExam1() {
        return exam1;
    }

    public int getExam2() {
        return exam2;
    }

    public int getExam3() {
        return exam3;
    }

    public char getGrade() {
        return grade;
    }

    public double getTotal() {
        return total;
    }

    @Override
    public String toString() {
        return String.format("%s %s %d %d %d %c %.2f\n", firstName, lastName, exam1, exam2, exam3, grade, total);
    }

    @Override
    public int compareTo(Student o) {
        return Double.compare(this.total, o.total);
    }
}

public class CalculateGradesTest {
    public static void main(String[] args) throws FileNotFoundException { //LastName FirstName LetterGrade
        Scanner sc = new Scanner(new File("C:\\Users\\Hp\\IdeaProjects\\NP\\src\\Aud5\\datotekaStudenti"));
        List<Student> students = new ArrayList<>();

        while(sc.hasNextLine()) {
            String line = sc.nextLine();
            String[] parts = line.split("\\s+");
            String name = parts[0];
            String lastName = parts[1];
            int exam1 = Integer.parseInt(parts[2]);
            int exam2 = Integer.parseInt(parts[3]);
            int exam3 = Integer.parseInt(parts[4]);
            Student student = new Student(name, lastName, exam1, exam2, exam3);
            students.add(student);
        }

        students.sort(Comparator.comparing(Student::getTotal).reversed());

        students.stream().sorted()
                .forEach(student -> String.format("%s %s %c\n", student.getFirstName(), student.getLastName(), student.getGrade()));

        System.out.println(students);

    }


}
