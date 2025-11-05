//package Aud10;
//import java.io.FileInputStream;
//import java.io.FileNotFoundException;
//import java.io.IOException;
//import java.io.InputStream;
//import java.util.*;
//
//class Name {
//
//}
//
//public class NamesTest {
//
//    public static Map<String,Integer> createFromFile(String path) throws FileNotFoundException {
//        Map<String,Integer> map = new HashMap<>();
//        InputStream is = new FileInputStream(path);
//        Scanner scanner = new Scanner(is);
//        while (scanner.hasNextLine()) {
//            String line = scanner.nextLine();
//            String[] parts = line.split("\\s+");
//            map.put(parts[0],Integer.parseInt(parts[1]));
//        }
//        scanner.close();
//        return map;
//    }
//
//    public static void main(String[] args) {
//        NamesTest names = new NamesTest();
//        try {
//            List<Name> girls = names.readNames("examples/data/girlnames.txt");
//            List<Name> boys = names.readNames("examples/data/boynames.txt");
//            List<DuplicateName> duplicates = names.findDuplicates(girls, boys);
//            duplicates.forEach(System.out::println);
//        } catch (IOException e) {
//            System.err.println(e.getMessage());
//        }
//    }
//}
