package Kolok2;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

class File {
    String name;
    int size;
    LocalDateTime time;
    char folder;

    public File(char folder,String name, int size, LocalDateTime time) {
        this.folder = folder;
        this.name = name;
        this.size = size;
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public int getSize() {
        return size;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public char getFolder() {
        return folder;
    }

    public String getYearAndMonth() {
        return String.format("%s-%s", time.getMonth(),time.getDayOfMonth());
    }

    @Override
    public String toString() {
        return String.format("%-10s %5sB %s", name, size, time);
    }
}

class FileSystem {
    List<File> files;

    public FileSystem() {
        files = new ArrayList<>();
    }

    //името на фолдерот е еден знак, може да биде . или голема буква
    public void addFile(char folder, String name, int size, LocalDateTime createdAt) {
        files.add(new File(folder,name, size, createdAt));
    }
    //враќа листа на сите скриени датотеки (тоа се датотеки чие што име започнува со знакот за точка .) со големина помала од size.
    public List<File> findAllHiddenFilesWithSizeLessThen(int size) {
        return files.stream().filter(s->s.getSize() < size)
                .sorted(Comparator.comparing(File::getTime).thenComparing(File::getSize).thenComparing(File::getName).thenComparing(File::getTime))
                .collect(Collectors.toList());
    }
    //враќа вкупна големина на сите датотеки кои се наоѓаат во фолдерите кои се зададени во листата folders
    public int totalSizeOfFilesFromFolders(List<Character> folders) {
        return files.stream()
                .filter(file -> folders.contains(file.folder))
                .mapToInt(File::getSize).sum();
    }
    //враќа мапа Map во која за датотеките се групирани според годината на креирање.
    public Map<Integer, Set<File>> byYear() {
        return files.stream().collect(Collectors.groupingBy(f->f.time.getYear(),Collectors.toCollection(TreeSet::new)));
    }

    public Map<String, Long> sizeByMonthAndDay() {
        return files.stream().collect(Collectors.groupingBy(File::getYearAndMonth,Collectors.summingLong(File::getSize)));
    }
}

public class FileSystemTest {
    public static void main(String[] args) {
        FileSystem fileSystem = new FileSystem();
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < n; i++) {
            String line = scanner.nextLine();
            String[] parts = line.split(":");
            fileSystem.addFile(parts[0].charAt(0), parts[1],
                    Integer.parseInt(parts[2]),
                    LocalDateTime.of(2016, 12, 29, 0, 0, 0).minusDays(Integer.parseInt(parts[3]))
            );
        }
        int action = scanner.nextInt();
        if (action == 0) {
            scanner.nextLine();
            int size = scanner.nextInt();
            System.out.println("== Find all hidden files with size less then " + size);
            List<File> files = fileSystem.findAllHiddenFilesWithSizeLessThen(size);
            files.forEach(System.out::println);
        } else if (action == 1) {
            scanner.nextLine();
            String[] parts = scanner.nextLine().split(":");
            System.out.println("== Total size of files from folders: " + Arrays.toString(parts));
            int totalSize = fileSystem.totalSizeOfFilesFromFolders(Arrays.stream(parts)
                    .map(s -> s.charAt(0))
                    .collect(Collectors.toList()));
            System.out.println(totalSize);
        } else if (action == 2) {
            System.out.println("== Files by year");
            Map<Integer, Set<File>> byYear = fileSystem.byYear();
            byYear.keySet().stream().sorted()
                    .forEach(key -> {
                        System.out.printf("Year: %d\n", key);
                        Set<File> files = byYear.get(key);
                        files.stream()
                                .sorted()
                                .forEach(System.out::println);
                    });
        } else if (action == 3) {
            System.out.println("== Size by month and day");
            Map<String, Long> byMonthAndDay = fileSystem.sizeByMonthAndDay();
            byMonthAndDay.keySet().stream().sorted()
                    .forEach(key -> System.out.printf("%s -> %d\n", key, byMonthAndDay.get(key)));
        }
        scanner.close();
    }
}
