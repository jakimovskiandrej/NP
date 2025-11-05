package Kolok2;

import java.util.*;

class Container<T extends Comparable<T>> {

    TreeSet<T> containers;

    public Container() {
        containers = new TreeSet<>();
    }

    public void add(T a) {
        containers.add(a);
    }

    public boolean remove(T a) {
        return containers.remove(a);
    }

    public boolean contains(T a) {
        return containers.contains(a);
    }

    public int size() {
        return containers.size();
    }

    public List<T> toList() {
        return new ArrayList<>(containers);
    }

    public boolean isEmpty() {
        return containers.isEmpty();
    }

    @Override
    public String toString() {
        return containers.toString();
    }
}

class BlockContainer<T extends Comparable<T>> {

    List<Container<T>> containers;
    int n;

    public BlockContainer(int n) {
        this.n = n;
        containers = new ArrayList<>();
    }

    public void add(T a) {
        if(containers.isEmpty() || containers.getLast().isEmpty()) {
            containers.add(new Container<T>());
        }
        containers.getLast().add(a);
    }

    public boolean remove(T a) {
        if(containers.isEmpty()) {
            return false;
        }
        Container<T> result = containers.getLast();
        boolean found = result.remove(a);
        if(result.isEmpty()) {
            containers.remove(containers.getLast());
        }
        return found;
    }

    public void sort() {
        List<T> elements = new ArrayList<>();
        for (Container<T> container : containers) {
            elements.addAll(container.toList());
        }
        Collections.sort(elements);
        containers.clear();
        for (T element : elements) {
            add(element);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Container<T> container : containers) {
            sb.append(container.toString()).append(",");
        }
        if(!sb.isEmpty()) {
            sb.setLength(sb.length() - 1);
        }
        return sb.toString();
    }
}

public class BlockContainerTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int size = scanner.nextInt();
        BlockContainer<Integer> integerBC = new BlockContainer<Integer>(size);
        scanner.nextLine();
        Integer lastInteger = null;
        for(int i = 0; i < n; ++i) {
            int element = scanner.nextInt();
            lastInteger = element;
            integerBC.add(element);
        }
        System.out.println("+++++ Integer Block Container +++++");
        System.out.println(integerBC);
        System.out.println("+++++ Removing element +++++");
        integerBC.remove(lastInteger);
        System.out.println("+++++ Sorting container +++++");
        integerBC.sort();
        System.out.println(integerBC);
        BlockContainer<String> stringBC = new BlockContainer<String>(size);
        String lastString = null;
        for(int i = 0; i < n; ++i) {
            String element = scanner.next();
            lastString = element;
            stringBC.add(element);
        }
        System.out.println("+++++ String Block Container +++++");
        System.out.println(stringBC);
        System.out.println("+++++ Removing element +++++");
        stringBC.remove(lastString);
        System.out.println("+++++ Sorting container +++++");
        stringBC.sort();
        System.out.println(stringBC);
    }
}
