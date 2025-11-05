package Aud9;

import java.util.Arrays;
import java.util.List;

public class EqualsTest {

    public boolean equals(List<Integer> left, List<Integer> right) {
        if (left.size() != right.size()) {
            return false;
        }
        for (int i = 0; i < left.size(); i++) {
            if (!left.get(i).equals(right.get(i))) {
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        EqualsTest tester = new EqualsTest();

        List<Integer> list1 = Arrays.asList(1, 2, 3, 4, 5);
        List<Integer> list2 = Arrays.asList(1, 2, 3, 4, 5);
        List<Integer> list3 = Arrays.asList(5, 4, 3, 2, 1);
        List<Integer> list4 = Arrays.asList(1, 2, 3);

        System.out.println("list1 == list2: " + tester.equals(list1, list2)); // true
        System.out.println("list1 == list3: " + tester.equals(list1, list3)); // false
        System.out.println("list1 == list4: " + tester.equals(list1, list4)); // false
    }
}
