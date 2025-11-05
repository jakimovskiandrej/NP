package Lab2;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Objects;
import java.util.Scanner;

enum CONTACTTYPE {
    EMAIL,
    PHONE
}

enum Operator {
    VIP,
    ONE,
    TMOBILE
}

abstract class Contact {

    String date;
    private final int day;
    private final int month;
    private final int year;
    private CONTACTTYPE type;

    public Contact(String date) {
        String[] parts = date.split("-");
        year = Integer.parseInt(parts[0]);
        month = Integer.parseInt(parts[1]);
        day = Integer.parseInt(parts[2]);
    }

    public String getDate() {
        return date;
    }

    public long getDays() {
        return year * 365L + month * 30L + day;
    }

    public int getDay() {
        return day;
    }

    public int getMonth() {
        return month;
    }

    public int getYear() {
        return year;
    }

    public boolean isNewerThan(Contact c) {
        return getDays() > c.getDays();
    }

    public abstract String getType();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Contact contact = (Contact) o;
        return day == contact.day && month == contact.month && year == contact.year && Objects.equals(date, contact.date) && type == contact.type;
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(date);
        result = 31 * result + day;
        result = 31 * result + month;
        result = 31 * result + year;
        result = 31 * result + Objects.hashCode(type);
        return result;
    }
}

class EmailContact extends Contact {

    private String email;

    public EmailContact(String date, String email) {
        super(date);
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String getType() {
        return "Email";
    }

    @Override
    public String toString() {
        return String.format("\"" + email + "\"");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        EmailContact that = (EmailContact) o;
        return Objects.equals(email, that.email);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + Objects.hashCode(email);
        return result;
    }
}

class PhoneContact extends Contact {
    private String phone;
    private Operator operator;

    public PhoneContact(String date, String phone) {
        super(date);
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }

    public Operator getOperator() {
        String o = phone.substring(0,3);
        String result = "";
        if(o.equals("070") || o.equals("071") || o.equals("072")) {
            result = String.valueOf(Operator.TMOBILE);
        } else if(o.equals("075") || o.equals("076")) {
            result = String.valueOf(Operator.ONE);
        } else if(o.equals("077") || o.equals("078")) {
            result = String.valueOf(Operator.VIP);
        }
        return Operator.valueOf(result);
    }

    @Override
    public String getType() {
        return "Phone";
    }

    @Override
    public String toString() {
        return String.format("\"" + phone + "\"");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        PhoneContact that = (PhoneContact) o;
        return Objects.equals(phone, that.phone) && operator == that.operator;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + Objects.hashCode(phone);
        result = 31 * result + Objects.hashCode(operator);
        return result;
    }
}

class Student {
    //Student(String firstName, String lastName, String city, int age, long index)
    private Contact[] contacts;
    int n;
    private String firstName;
    private String lastName;
    private String city;
    private int age;
    private long index;

    public Student(String firstName, String lastName, String city, int age, long index) {
        n = 0;
        this.contacts = new Contact[n];
        this.firstName = firstName;
        this.lastName = lastName;
        this.city = city;
        this.age = age;
        this.index = index;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getCity() {
        return city;
    }

    public int getAge() {
        return age;
    }

    public long getIndex() {
        return index;
    }
    //addEmailContact(String date, String email):void
    public void addEmailContact(String date, String email) {
        contacts = Arrays.copyOf(contacts,n+1);
        contacts[n] = new EmailContact(date,email);
        n++;
    }
    //addPhoneContact(String date, String phone):void – метод што додава телефонски контакт во низата на контакти
    public void addPhoneContact(String date, String phone) {
        contacts = Arrays.copyOf(contacts,n+1);
        contacts[n] = new PhoneContact(date,phone);
        n++;
    }

    public String getFullName() {
        return String.format("%s %s", firstName, lastName);
    }
    //getLatestContact():Contact – го враќа најновиот контакт (според датум) од студентот
    public Contact getLatestContact() {
        Contact latest = contacts[0];
        for(Contact c : contacts) {
            if(c.isNewerThan(latest)) {
                latest = c;
            }
        }
        return latest;
    }

    public int getNumberContacts() {
        return n;
    }

    public Contact[] getPhoneContacts() {
        Contact[] array;
        int counter = 0;
        for (Contact contact : contacts) {
            if(contact.getType().equals("Phone")) {
                counter++;
            }
        }
        array = new Contact[counter];
        counter = 0;
        for (Contact contact : contacts) {
            if(contact.getType().equals("Phone")) {
                array[counter] = contact;
                counter++;
            }
        }
        return array;
    }

    public Contact[] getEmailContacts() {
        Contact[] array;
        int counter = 0;
        for (Contact contact : contacts) {
            if(contact.getType().equals("Email")) {
                counter++;
            }
        }
        array = new Contact[counter];
        counter = 0;
        for (Contact contact : contacts) {
            if(contact.getType().equals("Email")) {
                array[counter] = contact;
                counter++;
            }
        }
        return array;
    }

    @Override
    public String toString() {
        return "{\"ime\":\"" +
                firstName +
                "\", \"prezime\":\"" +
                lastName +
                "\", \"vozrast\":" +
                age +
                ", \"grad\":\"" +
                city +
                "\", \"indeks\":" +
                index +
                ", \"telefonskiKontakti\":" +
                Arrays.toString(getPhoneContacts()) +
                ", \"emailKontakti\":" +
                Arrays.toString(getEmailContacts()) +
                "}";
    }
}

class Faculty {
    private String name;
    private Student[] students;

    public Faculty(String name, Student[] students) {
        this.name = name;
        this.students = Arrays.copyOf(students,students.length);
    }
    //countStudentsFromCity(String cityName):int – враќа колку студенти има од даден град
    public int countStudentsFromCity(String cityName) {
        int counter = 0;
        for (Student student : students) {
            if(student.getCity().equals(cityName)) {
                counter++;
            }
        }
        return counter;
    }
    //getStudent(long index):Student
    public Student getStudent(long index) {
        for (Student student : students) {
            if(student.getIndex() == index) {
                return student;
            }
        }
        return null;
    }
    //getAverageNumberOfContacts():double – враќа просечен број на контакти по студент
    public double getAverageNumberOfContacts() {
        double sum = 0;
        for (Student student : students) {
            sum+=student.getNumberContacts();
        }
        return sum/students.length;
    }
    //getStudentWithMostContacts():Student
    public Student getStudentWithMostContacts() {
        Student max = students[0];
        for (Student student : students) {
            if(student.getNumberContacts() > max.getNumberContacts()) {
                max = student;
            } else if(student.getNumberContacts() == max.getNumberContacts() && student.getIndex() > max.getIndex()) {
                max = student;
            }
        }
        return max;
    }

    @Override
    public String toString() {
        return "{\"fakultet\":\"" +
                name +
                "\", \"studenti\":" +
                Arrays.toString(students) +
                "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Faculty faculty = (Faculty) o;
        return Objects.equals(name, faculty.name) && Arrays.equals(students, faculty.students);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(name);
        result = 31 * result + Arrays.hashCode(students);
        return result;
    }
}

public class ContactsTester {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int tests = scanner.nextInt();
        Faculty faculty = null;

        int rvalue = 0;
        long rindex = -1;

        DecimalFormat df = new DecimalFormat("0.00");

        for (int t = 0; t < tests; t++) {

            rvalue++;
            String operation = scanner.next();

            switch (operation) {
                case "CREATE_FACULTY": {
                    String name = scanner.nextLine().trim();
                    int N = scanner.nextInt();

                    Student[] students = new Student[N];

                    for (int i = 0; i < N; i++) {
                        rvalue++;

                        String firstName = scanner.next();
                        String lastName = scanner.next();
                        String city = scanner.next();
                        int age = scanner.nextInt();
                        long index = scanner.nextLong();

                        if ((rindex == -1) || (rvalue % 13 == 0))
                            rindex = index;

                        Student student = new Student(firstName, lastName, city,
                                age, index);
                        students[i] = student;
                    }

                    faculty = new Faculty(name, students);
                    break;
                }

                case "ADD_EMAIL_CONTACT": {
                    long index = scanner.nextInt();
                    String date = scanner.next();
                    String email = scanner.next();

                    rvalue++;

                    if ((rindex == -1) || (rvalue % 3 == 0))
                        rindex = index;

                    faculty.getStudent(index).addEmailContact(date, email);
                    break;
                }

                case "ADD_PHONE_CONTACT": {
                    long index = scanner.nextInt();
                    String date = scanner.next();
                    String phone = scanner.next();

                    rvalue++;

                    if ((rindex == -1) || (rvalue % 3 == 0))
                        rindex = index;

                    faculty.getStudent(index).addPhoneContact(date, phone);
                    break;
                }

                case "CHECK_SIMPLE": {
                    System.out.println("Average number of contacts: "
                            + df.format(faculty.getAverageNumberOfContacts()));

                    rvalue++;

                    String city = faculty.getStudent(rindex).getCity();
                    System.out.println("Number of students from " + city + ": "
                            + faculty.countStudentsFromCity(city));

                    break;
                }

                case "CHECK_DATES": {

                    rvalue++;

                    System.out.print("Latest contact: ");
                    Contact latestContact = faculty.getStudent(rindex)
                            .getLatestContact();
                    if (latestContact.getType().equals("Email"))
                        System.out.println(((EmailContact) latestContact)
                                .getEmail());
                    if (latestContact.getType().equals("Phone"))
                        System.out.println(((PhoneContact) latestContact)
                                .getPhone()
                                + " ("
                                + ((PhoneContact) latestContact).getOperator()
                                .toString() + ")");

                    if (faculty.getStudent(rindex).getEmailContacts().length > 0
                            && faculty.getStudent(rindex).getPhoneContacts().length > 0) {
                        System.out.print("Number of email and phone contacts: ");
                        System.out
                                .println(faculty.getStudent(rindex)
                                        .getEmailContacts().length
                                        + " "
                                        + faculty.getStudent(rindex)
                                        .getPhoneContacts().length);

                        System.out.print("Comparing dates: ");
                        int posEmail = rvalue
                                % faculty.getStudent(rindex).getEmailContacts().length;
                        int posPhone = rvalue
                                % faculty.getStudent(rindex).getPhoneContacts().length;

                        System.out.println(faculty.getStudent(rindex)
                                .getEmailContacts()[posEmail].isNewerThan(faculty
                                .getStudent(rindex).getPhoneContacts()[posPhone]));
                    }

                    break;
                }

                case "PRINT_FACULTY_METHODS": {
                    System.out.println("Faculty: " + faculty.toString());
                    System.out.println("Student with most contacts: "
                            + faculty.getStudentWithMostContacts().toString());
                    break;
                }

            }

        }

        scanner.close();
    }
}
