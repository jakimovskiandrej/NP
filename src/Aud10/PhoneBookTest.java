package Aud10;

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

    public String getNumber() {
        return number;
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

    Map<String, Set<Contact>> contacts;

    public PhoneBook() {
        contacts = new HashMap<>();
    }

    public void addContact(String name, String number) throws DuplicateNumberException {
        if(contacts.containsKey(number)) {
            throw new DuplicateNumberException("Contact already exists");
        }
        Contact contact = new Contact(name, number);
        contacts.putIfAbsent(contact.getNumber(), new HashSet<>());
    }

    public void contactsByNumber(String number) {
        if(contacts.containsKey(number)) {
            contacts.get(number).forEach(System.out::println);
        }
    }

    public void contactsByName(String name) {
        if(contacts.containsKey(name)) {
            contacts.get(name).forEach(System.out::println);
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
            String[] parts = line.split(":");
            if (parts[0].equals("N")) {
                phoneBook.contactsByNumber(parts[1]);
            } else {
                phoneBook.contactsByName(parts[1]);
            }
        }
    }
}
