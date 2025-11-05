package Kolok2;

import java.util.*;

class Names {
    Map<String,Integer> namesCount;
    Set<String> unique;

    public Names() {
        namesCount = new HashMap<>();
        unique = new HashSet<>();
    }

    public void addName(String name) {
        namesCount.put(name,namesCount.getOrDefault(name,0)+1);
        unique.add(name);
    }

    public void printN(int n) {
        for (String s : unique) {
            int count = namesCount.getOrDefault(s,0);
            if(count >= n) {
                int uniqueLetters = (int) s.chars().distinct().count();
                System.out.printf("%s (%d) %d%n", s, count, uniqueLetters);
            }
        }
    }

    public String findName(int len, int x) {
        List<String> result = new ArrayList<>();
        for (String s : unique) {
            if(s.length() < len) {
                result.add(s);
            }
        }
        if(result.isEmpty()) {
            return null;
        }
        int index = x % result.size();
        return result.get(index);
    }

}

public class NamesTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        Names names = new Names();
        for (int i = 0; i < n; ++i) {
            String name = scanner.nextLine();
            names.addName(name);
        }
        n = scanner.nextInt();
        System.out.printf("===== PRINT NAMES APPEARING AT LEAST %d TIMES =====\n", n);
        names.printN(n);
        System.out.println("===== FIND NAME =====");
        int len = scanner.nextInt();
        int index = scanner.nextInt();
        System.out.println(names.findName(len, index));
        scanner.close();

    }
}
