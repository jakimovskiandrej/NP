package Kolok2;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

class DeadlineNotValidException extends Exception {
    public DeadlineNotValidException(String message) {
        super(message);
    }
}

class Task {
    String category;
    String exerciseName;
    String description;
    LocalDate deadline;
    Integer priority;

    public Task(String category, String exerciseName, String description, LocalDate deadline, Integer priority) {
        this.category = category;
        this.exerciseName = exerciseName;
        this.description = description;
        this.deadline = deadline;
        this.priority = priority;
    }

    public String getCategory() {
        return category;
    }

    public String getExerciseName() {
        return exerciseName;
    }

    public String getDescription() {
        return description;
    }

    public LocalDate getDeadline() {
        return deadline;
    }

    public Integer getPriority() {
        return priority;
    }

    @Override
    public String toString() {
        return "Task{" +
                "category='" + category + '\'' +
                ", exerciseName='" + exerciseName + '\'' +
                ", description='" + description + '\'' +
                ", deadline=" + deadline +
                ", priority=" + priority +
                '}';
    }
}

class TaskManager {

    List<Task> tasks;

    public TaskManager() {
        tasks = new ArrayList<>();
    }

    void readTasks (InputStream inputStream) {
        Scanner scanner = new Scanner(inputStream);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d.M.yyyy");
        LocalDate datum = LocalDate.parse("2.6.2020", formatter);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            try {
                String[] parts = line.split(",");
                String category = parts[0];
                String exerciseName = parts[1];
                String description = parts[2];
                LocalDate deadline = null;
                Integer priority = null;
                if(parts.length >= 4 && !parts[3].isBlank()) {
                    deadline = LocalDate.parse(parts[3].trim(), formatter);
                    if(deadline.isAfter(datum)) {
                        throw new DeadlineNotValidException("");
                    }
                }
                if(parts.length >= 5 && !parts[4].isBlank()) {
                    priority = Integer.valueOf(parts[4].trim());
                }
                Task task = new Task(category, exerciseName, description, deadline, priority);
                tasks.add(task);
            } catch (DeadlineNotValidException e) {
                throw new RuntimeException(e);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    void printTasks(OutputStream os, boolean includePriority, boolean includeCategory) {
        PrintWriter pw = new PrintWriter(os);
        LocalDate now = LocalDate.now();
        Comparator<Task> comparator;
        if(includePriority) {
            comparator = Comparator.comparingInt((Task t) -> t.priority).thenComparing(t -> {
                if(t.deadline != null) {
                    return Math.abs(t.deadline.compareTo(now));
                } else {
                    return 0;
                }
            });
        } else {
            comparator = Comparator.comparing(t -> {
                if(t.deadline != null) {
                    return Math.abs(t.deadline.compareTo(now));
                } else {
                    return 0;
                }
            });
        }
        List<Task> sorted = new ArrayList<>(tasks);
        sorted.sort(comparator);
        if (includeCategory) {
            Map<String, List<Task>> byCategory = new TreeMap<>();
            for (Task t : sorted) {
                byCategory.computeIfAbsent(t.category, k -> new ArrayList<>()).add(t);
            }
            byCategory.forEach((category, taskList) -> {
                pw.println(category);
                taskList.forEach(pw::println);
            });
        } else {
            sorted.forEach(pw::println);
        }
        pw.flush();
    }
}

public class TasksManagerTest {
    public static void main(String[] args) {

        TaskManager manager = new TaskManager();

        System.out.println("Tasks reading");
        manager.readTasks(System.in);
        System.out.println("By categories with priority");
        manager.printTasks(System.out, true, true);
        System.out.println("-------------------------");
        System.out.println("By categories without priority");
        manager.printTasks(System.out, false, true);
        System.out.println("-------------------------");
        System.out.println("All tasks without priority");
        manager.printTasks(System.out, false, false);
        System.out.println("-------------------------");
        System.out.println("All tasks with priority");
        manager.printTasks(System.out, true, false);
        System.out.println("-------------------------");

    }
}
