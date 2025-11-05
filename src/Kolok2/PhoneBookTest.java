package Kolok2;

import java.util.*;

class DuplicateNumberException extends Exception {
    public DuplicateNumberException(String message) {
        super(message);
    }
}

class Contact {
    String name;
    String number;

    public Contact(String name, String number) {
        this.name = name;
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    @Override
    public String toString() {
        return String.format("%s %s", name, number);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Contact contact = (Contact) o;
        return Objects.equals(name, contact.name) && Objects.equals(number, contact.number);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(name);
        result = 31 * result + Objects.hashCode(number);
        return result;
    }
}

class PhoneBook {

    Map<String, List<Contact>> nameMap;
    TreeMap<String, Contact> numberMap;

    public PhoneBook() {
        nameMap = new HashMap<>();
        numberMap = new TreeMap<>();
    }

    void addContact(String name, String number) throws DuplicateNumberException {
        Contact contact = new Contact(name, number);
        if (nameMap.containsKey(number)) {
            throw new DuplicateNumberException(String.format("Duplicate number %s", number));
        }
        nameMap.putIfAbsent(name, new ArrayList<>());
        nameMap.get(name).add(contact);
        numberMap.putIfAbsent(number, contact);
    }

    void contactsByNumber(String number) {
        List<Contact> result = new ArrayList<>();
        for (String s : numberMap.keySet()) {
            if(s.contains(number)) {
                result.add(numberMap.get(s));
            }
        }
        if(result.isEmpty()) {
            System.out.println("NOT FOUND");
        } else {
            result.stream().sorted(Comparator.comparing(Contact::getName).thenComparing(Contact::getNumber))
                    .forEach(s -> System.out.printf("%s %s%n", s.getName(), s.getNumber()));
        }
    }

    void contactsByName(String name) {
        List<Contact> result = nameMap.get(name);
        if(result == null || result.isEmpty()) {
            System.out.println("NOT FOUND");
        } else {
            result.stream().sorted(Comparator.comparing(Contact::getName).thenComparing(Contact::getNumber))
                    .forEach(System.out::println);
        }
    }

}

public class PhoneBookTest {
    public static void main(String[] args) {
        PhoneBook phoneBook = new PhoneBook();
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < n; ++i) {
            String line = scanner.nextLine();
            String[] parts = line.split(":");
            try {
                phoneBook.addContact(parts[0], parts[1]);
            } catch (DuplicateNumberException e) {
                System.out.println(e.getMessage());
            }
        }
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            System.out.println(line);
            String[] parts = line.split(":");
            if (parts[0].equals("NUM")) {
                phoneBook.contactsByNumber(parts[1]);
            } else {
                phoneBook.contactsByName(parts[1]);
            }
        }
    }

}
