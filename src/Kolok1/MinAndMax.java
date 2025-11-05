package Kolok1;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

class MinMax<E extends Comparable<E>> {

    private E min;
    private E max;
    private List<E> list;

    public MinMax() {
        list = new ArrayList<E>();
    }

    public E getMin() {
        return min;
    }

    public E getMax() {
        return max;
    }

    public void update(E element) {
        if(list.isEmpty()) {
            min = element;
            max = element;
        }
        for(int i = 0; i < list.size(); i++) {
            if(element.compareTo(max) > 0) {
                max = element;
            } else if(element.compareTo(min) < 0) {
                min = element;
            }
        }
        list.add(element);
    }

    public long different() {
        return list.stream().filter(a->!a.equals(min)&&!a.equals(max)).count();
    }

    @Override
    public String toString() {
        return min + " " + max + " " + different() + "\n";
    }
}

public class MinAndMax {
    public static void main(String[] args) throws ClassNotFoundException {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        MinMax<String> strings = new MinMax<String>();
        for(int i = 0; i < n; ++i) {
            String s = scanner.next();
            strings.update(s);
        }
        System.out.println(strings);
        MinMax<Integer> ints = new MinMax<Integer>();
        for(int i = 0; i < n; ++i) {
            int x = scanner.nextInt();
            ints.update(x);
        }
        System.out.println(ints);
    }
}