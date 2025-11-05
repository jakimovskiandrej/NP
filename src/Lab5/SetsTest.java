package Lab5;

import java.util.*;
import java.util.stream.Collectors;

class StudentAlreadyExistException extends Exception {
    public StudentAlreadyExistException(String message) {
        super(message);
    }
}

class Student {
    String id;
    List<Integer> grades;
    int maxGrade;

    public Student(String id, List<Integer> grades) {
        this.id = id;
        this.grades = grades;
    }

    public String getId() {
        return id;
    }

    public List<Integer> getGrades() {
        return grades;
    }

    public double averageGrade() {
        int sum = 0;
        for (Integer grade : grades) {
            sum += grade;
        }
        return (double)sum / grades.size();
    }

    public int numOfPassedExams() {
        int counter = 0;
        for (Integer grade : grades) {
            if(grade >= 6) {
                counter++;
            }
        }
        return counter;
    }

    public int getMaxGrade() {
        maxGrade = grades.getLast();
        return maxGrade;
    }

    @Override
    public String toString() {
        return "Student{" +
                "id='" + id + '\'' +
                ", grades=" + grades +
                ", maxGrade=" + maxGrade +
                '}';
    }
}

class Faculty {
    Map<String,Student> students;

    public Faculty() {
        students = new HashMap<>();
    }

    void addStudent(String id, List<Integer> grades) throws StudentAlreadyExistException {
        if(students.containsKey(id)) {
            throw new StudentAlreadyExistException(String.format("Student with ID %s already exists",id));
        }
        students.putIfAbsent(id,new Student(id,grades));
    }

    void addGrade(String id, int grade) {
        Student student = students.get(id);
        if(student != null) {
            student.grades.add(grade);
        }
    }

    Set<Student> getStudentsSortedByAverageGrade() {
        return students.values().stream()
                .sorted(Comparator.comparingDouble(Student::averageGrade).thenComparing(Student::numOfPassedExams).thenComparing(Student::getId).reversed())
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    Set<Student> getStudentsSortedByCoursesPassed() {
        return students.values().stream()
                .sorted(Comparator.comparingInt(Student::numOfPassedExams).thenComparing(Student::averageGrade).thenComparing(Student::getId).reversed())
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    Set<Student> getStudentsSortedByMaxGrade() {
        return students.values().stream()
                .sorted(Comparator.comparingInt(Student::getMaxGrade).thenComparing(Student::averageGrade).thenComparing(Student::getId).reversed())
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }
}

public class SetsTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Faculty faculty = new Faculty();

        while (true) {
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("exit")) {
                break;
            }

            String[] tokens = input.split("\\s+");
            String command = tokens[0];

            switch (command) {
                case "addStudent":
                    String id = tokens[1];
                    List<Integer> grades = new ArrayList<>();
                    for (int i = 2; i < tokens.length; i++) {
                        grades.add(Integer.parseInt(tokens[i]));
                    }
                    try {
                        faculty.addStudent(id, grades);
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    break;

                case "addGrade":
                    String studentId = tokens[1];
                    int grade = Integer.parseInt(tokens[2]);
                    faculty.addGrade(studentId, grade);
                    break;

                case "getStudentsSortedByAverageGrade":
                    System.out.println("Sorting students by average grade");
                    Set<Student> sortedByAverage = faculty.getStudentsSortedByAverageGrade();
                    for (Student student : sortedByAverage) {
                        System.out.println(student);
                    }
                    break;

                case "getStudentsSortedByCoursesPassed":
                    System.out.println("Sorting students by courses passed");
                    Set<Student> sortedByCourses = faculty.getStudentsSortedByCoursesPassed();
                    for (Student student : sortedByCourses) {
                        System.out.println(student);
                    }
                    break;

                default:
                    break;
            }
        }

        scanner.close();
    }
}
