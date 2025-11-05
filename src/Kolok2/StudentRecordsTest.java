package Kolok2;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

class Student {
    String code;
    String direction;
    List<Integer> grades;

    public Student(String code, String direction,List<Integer> grades) {
        this.code = code;
        this.direction = direction;
        this.grades = grades;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public List<Integer> getGrades() {
        return grades;
    }

    public void setGrades(List<Integer> grades) {
        this.grades = grades;
    }

    public double averageGrade() {
        return grades.stream().mapToDouble(x -> x).average().getAsDouble();
    }

    @Override
    public String toString() {
        return String.format("%s %.2f", code,averageGrade());
    }
}

class StudentRecords {
    Map<String,Student> students;

    public StudentRecords() {
        students = new HashMap<>();
    }

    int readRecords(InputStream inputStream) throws IOException {
        String line;
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        while ((br.readLine()) != null){
            line = br.readLine();
            String[] parts = br.readLine().split("\\s+");
            String code = parts[0];
            String direction = parts[1];
            List<Integer> grades = Arrays.stream(parts,2,parts.length)
                    .map(Integer::parseInt)
                    .collect(Collectors.toList());
            Student student = new Student(code,direction,grades);
            students.put(code,student);
        }
        return students.size();
    }

    void writeTable(OutputStream outputStream) {
        PrintWriter pw = new PrintWriter(outputStream);
        Map<String,List<Student>> groupedByDirection = students.values().stream().collect(Collectors.groupingBy(Student::getDirection));
        List<String> sorted = new ArrayList<>(groupedByDirection.keySet());
        for (String s : sorted) {
            pw.write(s);
            pw.write("\n");
            List<Student> sortedStudents = groupedByDirection.get(s).stream().sorted(Comparator.comparing(Student::averageGrade).thenComparing(Student::getCode).reversed())
                    .collect(Collectors.toList());
            for (Student sortedStudent : sortedStudents) {
                pw.write(sortedStudent.toString());
                pw.write("\n");
            }
        }
        pw.flush();
    }

    void writeDistribution(OutputStream outputStream) {
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(outputStream));

    }
}

public class StudentRecordsTest {
    public static void main(String[] args) {
        System.out.println("=== READING RECORDS ===");
        StudentRecords studentRecords = new StudentRecords();
        int total = 0;
        try {
            total = studentRecords.readRecords(System.in);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.printf("Total records: %d\n", total);
        System.out.println("=== WRITING TABLE ===");
        studentRecords.writeTable(System.out);
        System.out.println("=== WRITING DISTRIBUTION ===");
        studentRecords.writeDistribution(System.out);
    }
}
