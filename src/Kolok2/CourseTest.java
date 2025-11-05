package Kolok2;

import java.util.*;
import java.util.stream.Collectors;

class Student {
    String index;
    String name;
    int firstMidtermPoints;
    int secondMidtermPoints;
    int labsPoints;

    public Student(String index, String name) {
        this.index = index;
        this.name = name;
        this.firstMidtermPoints = 0;
        this.secondMidtermPoints = 0;
        this.labsPoints = 0;
    }

    public String getIndex() {
        return index;
    }

    public String getName() {
        return name;
    }

    public int getFirstMidtermPoints() {
        return firstMidtermPoints;
    }

    public int getSecondMidtermPoints() {
        return secondMidtermPoints;
    }

    public int getLabsPoints() {
        return labsPoints;
    }
//midterm1 * 0.45 + midterm2 * 0.45 + labs.
    public double summaryPoints() {
        return firstMidtermPoints * 0.45 + secondMidtermPoints * 0.45 + labsPoints;
    }

    public int countGrade() {
        double result = firstMidtermPoints * 0.45 + secondMidtermPoints * 0.45 + labsPoints;
        if(result < 50) {
            return 5;
        } else if(result > 50 && result <= 60) {
            return 6;
        } else if(result > 60 && result <= 70) {
            return 7;
        } else if(result > 70 && result <= 80) {
            return 8;
        } else if(result > 80 && result <= 90) {
            return 9;
        } else if(result > 90 && result <= 100) {
            return 10;
        }
        return (int)result;
    }

    public void updatePoints(String activity, int points) {
        if(points < 0 || points > 100) {
            return;
        }
        switch(activity) {
            case "midterm1": firstMidtermPoints = points; break;
            case "midterm2": secondMidtermPoints = points; break;
            case "labs": labsPoints = points; break;
            default: break;
        }
    }

    //ID: 151020 Name: Stefan First midterm: 78 Second midterm 80 Labs: 8 Summary points: 79.10 Grade: 8
    @Override
    public String toString() {
        return String.format("ID: %s Name: %s First midterm: %d Second midterm %d Labs: %d Summary points: %.2f Grade: %d", index, name, firstMidtermPoints, secondMidtermPoints, labsPoints, summaryPoints(),countGrade());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Student student = (Student) o;
        return firstMidtermPoints == student.firstMidtermPoints && secondMidtermPoints == student.secondMidtermPoints && labsPoints == student.labsPoints && Objects.equals(index, student.index) && Objects.equals(name, student.name);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(index);
        result = 31 * result + Objects.hashCode(name);
        result = 31 * result + firstMidtermPoints;
        result = 31 * result + secondMidtermPoints;
        result = 31 * result + labsPoints;
        return result;
    }
}

class AdvancedProgrammingCourse {

    List<Student> students;

    public AdvancedProgrammingCourse() {
        students = new ArrayList<>();
    }

    public void addStudent (Student s) {
        students.add(s);
    }

    public void updateStudent (String idNumber, String activity, int points) {
        for (Student student : students) {
            if(student.getIndex().equals(idNumber)) {
                student.updatePoints(activity, points);
            }
        }
    }
//ги враќа првите N најдобри положени студенти на предметот сортирани во опаѓачки редослед според вкупниот број на сумарни поени.
// Сумарните поени се пресметуваат по формулата: midterm1 * 0.45 + midterm2 * 0.45 + labs.
    public List<Student> getFirstNStudents (int n) {
        return students.stream().limit(n).sorted(Comparator.comparing(Student::summaryPoints).reversed()).collect(Collectors.toList());
    }

    public Map<Integer,Integer> getGradeDistribution() {
        Map<Integer,Integer> result = new HashMap<>();
        for(int i=5;i<=10;i++) {
            result.put(i, 0);
        }
        for(Student student : students) {
            int grade = student.countGrade();
            result.put(grade,result.get(grade)+1);
        }
        return result;
    }
    //Count: 1 Min: 79.10 Average: 79.10 Max: 79.10
    public void printStatistics() {
        double min = Double.MAX_VALUE;
        double max = Double.MIN_VALUE;
        int counter = 0;
        double sum = 0;
        double average = 0;
        for(Student student : students) {
            if(student.summaryPoints() > 50) {
                counter++;
                double points = student.summaryPoints();
                min = Math.min(min, points);
                max = Math.max(max, points);
                sum += points;
                average = sum / counter;
            }
        }
        System.out.println(String.format("Count: %d Min: %.2f Average: %.2f Max: %.2f", counter, min, max, average));
    }
}

public class CourseTest {
    public static void printStudents(List<Student> students) {
        students.forEach(System.out::println);
    }

    public static void printMap(Map<Integer, Integer> map) {
        map.forEach((k, v) -> System.out.printf("%d -> %d%n", k, v));
    }

    public static void main(String[] args) {
        AdvancedProgrammingCourse advancedProgrammingCourse = new AdvancedProgrammingCourse();

        Scanner sc = new Scanner(System.in);

        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            String[] parts = line.split("\\s+");

            String command = parts[0];

            if (command.equals("addStudent")) {
                String id = parts[1];
                String name = parts[2];
                advancedProgrammingCourse.addStudent(new Student(id, name));
            } else if (command.equals("updateStudent")) {
                String idNumber = parts[1];
                String activity = parts[2];
                int points = Integer.parseInt(parts[3]);
                advancedProgrammingCourse.updateStudent(idNumber, activity, points);
            } else if (command.equals("getFirstNStudents")) {
                int n = Integer.parseInt(parts[1]);
                printStudents(advancedProgrammingCourse.getFirstNStudents(n));
            } else if (command.equals("getGradeDistribution")) {
                printMap(advancedProgrammingCourse.getGradeDistribution());
            } else {
                advancedProgrammingCourse.printStatistics();
            }
        }
    }
}
