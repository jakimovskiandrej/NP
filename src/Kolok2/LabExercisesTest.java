package Kolok2;

import java.util.Scanner;
import java.util.*;
import java.io.*;
import java.util.stream.*;

class Student {
    String index;
    List<Integer> points;
    static int COUNT_LAB_EXERCISES = 10;

    public Student(String index, List<Integer> points) {
        this.index = index;
        this.points = points;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public List<Integer> getPoints() {
        return points;
    }

    public void setPoints(List<Integer> points) {
        this.points = points;
    }

    public double summaryPoints() {
        return points.stream().mapToDouble(i -> i).sum() / (double) COUNT_LAB_EXERCISES;
    }

    public boolean hasSignature() {
        return points.size() >= 8;
    }

    public int getYearOfStudies() {
        return 20 - Integer.parseInt(index.substring(0,2));
    }

    @Override
    public String toString() {
        return String.format("%s %s %.2f", index, hasSignature() ? "YES" : "NO", summaryPoints());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Student student = (Student) o;
        return Objects.equals(index, student.index) && Objects.equals(points, student.points);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(index);
        result = 31 * result + Objects.hashCode(points);
        return result;
    }
}

class LabExercises {

    Collection<Student> students;

    public LabExercises() {
        students = new ArrayList<>();
    }

    public void addStudent (Student student) {
        students.add(student);
    }

    public void printByAveragePoints (boolean ascending, int n) {
        Comparator<Student> comparator = Comparator.comparing(Student::summaryPoints).thenComparing(Student::getIndex);
        if(!ascending) {
            comparator.reversed();
        }
        students.stream().sorted(comparator).limit(n).forEach(student -> System.out.println(student.toString()));
    }

    public List<Student> failedStudents () {
        return students.stream().filter(s->!s.hasSignature()).sorted(Comparator.comparing(Student::summaryPoints).thenComparing(Student::getIndex)).collect(Collectors.toList());
    }

    public Map<Integer,Double> getStatisticsByYear() {
        return students.stream().filter(Student::hasSignature).collect(Collectors.groupingBy(Student::getYearOfStudies,Collectors.averagingDouble(Student::summaryPoints)));
    }
}

public class LabExercisesTest {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        LabExercises labExercises = new LabExercises();
        while (sc.hasNextLine()) {
            String input = sc.nextLine();
            String[] parts = input.split("\\s+");
            String index = parts[0];
            List<Integer> points = Arrays.stream(parts).skip(1)
                    .mapToInt(Integer::parseInt)
                    .boxed()
                    .collect(Collectors.toList());

            labExercises.addStudent(new Student(index, points));
        }

        System.out.println("===printByAveragePoints (ascending)===");
        labExercises.printByAveragePoints(true, 100);
        System.out.println("===printByAveragePoints (descending)===");
        labExercises.printByAveragePoints(false, 100);
        System.out.println("===failed students===");
        labExercises.failedStudents().forEach(System.out::println);
        System.out.println("===statistics by year");
        labExercises.getStatisticsByYear().entrySet().stream()
                .map(entry -> String.format("%d : %.2f", entry.getKey(), entry.getValue()))
                .forEach(System.out::println);

    }
}
