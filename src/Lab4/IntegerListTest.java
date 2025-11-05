package Lab4;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.Scanner;

class ArrayIndexOutOfBoundsException extends Exception {
    public ArrayIndexOutOfBoundsException(String message) {
        super(message);
    }
}

class IntegerList {

    private ArrayList<Integer> list;

    public IntegerList() {
        list = new ArrayList<>();
    }

    public IntegerList(Integer... numbers) {
        list = new ArrayList<>(Arrays.asList(numbers));
    }

    public void add(int el, int idx) {
        while(idx > list.size()) {
            list.add(0);
        }
        list.add(idx,el);
    }

    public int remove(int idx) throws ArrayIndexOutOfBoundsException {
        if(idx < 0 || idx >= list.size()) {
            throw new ArrayIndexOutOfBoundsException("");
        }
        return list.remove(idx);
    }

    public void set(int el, int idx) throws ArrayIndexOutOfBoundsException {
        if(idx < 0 || idx > list.size()) {
            throw new ArrayIndexOutOfBoundsException("");
        }
        list.set(idx, el);
    }

    public int get(int idx) throws ArrayIndexOutOfBoundsException {
        if(idx < 0 || idx > list.size()) {
            throw new ArrayIndexOutOfBoundsException("");
        }
        return list.get(idx);
    }

    public int size() {
        return list.size();
    }

    public int count(int el) {
        int counter = 0;
        for (Integer i : list) {
            if(i.equals(el)) {
                counter++;
            }
        }
        return counter;
    }

    public void removeDuplicates() {
        for(int i=list.size()-1;i>=0;i--) {
            for(int j=i-1;j>=0;j--) {
                if(list.get(j).equals(list.get(i))) {
                    list.remove(j);
                    break;
                }
            }
        }
    }

    public int sumFirst(int k) {
        int n = Math.min(k,list.size());
        int sum = 0;
        for(int i=0; i<n; i++) {
            sum += list.get(i);
        }
        return sum;
    }

    public int sumLast(int k) {
        int n = Math.min(k,list.size());
        int sum = 0;
        for(int i=list.size()-n; i<list.size(); i++) {
            sum += list.get(i);
        }
        return sum;
    }

    public void shiftRight(int idx, int k) throws ArrayIndexOutOfBoundsException {
        if(idx >= list.size() || idx < 0) {
            throw new ArrayIndexOutOfBoundsException("");
        }
        int n = list.size();
        k = k%n;

        int newIndex = (idx+k) % n;
        Integer element = list.get(idx);

        list.remove(idx);
        list.add(newIndex, element);

    }

    public void shiftLeft(int idx, int k) throws ArrayIndexOutOfBoundsException {
        if(idx >= list.size() || idx < 0) {
            throw new ArrayIndexOutOfBoundsException("");
        }
        Integer element = list.get(idx);
        int n = list.size();
        k = k%n;
        list.remove(idx);
        int newIndex = (idx-k+n) % n;
        list.add(newIndex, element);
    }

    public IntegerList addValue(int value) throws ArrayIndexOutOfBoundsException {
        IntegerList rez = new IntegerList();
        for(int i=0; i<list.size(); i++) {
            rez.add(list.get(i)+value,i);
        }
        return rez;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        IntegerList that = (IntegerList) o;
        return Objects.equals(list, that.list);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(list);
    }
}

public class IntegerListTest {

    public static void main(String[] args) {
        Scanner jin = new Scanner(System.in);
        int k = jin.nextInt();
        if ( k == 0 ) { //test standard methods
            int subtest = jin.nextInt();
            if ( subtest == 0 ) {
                IntegerList list = new IntegerList();
                while ( true ) {
                    int num = jin.nextInt();
                    if ( num == 0 ) {
                        list.add(jin.nextInt(), jin.nextInt());
                    }
                    if ( num == 1 ) {
                        try {
                            list.remove(jin.nextInt());
                        } catch (ArrayIndexOutOfBoundsException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    if ( num == 2 ) {
                        print(list);
                    }
                    if ( num == 3 ) {
                        break;
                    }
                }
            }
            if ( subtest == 1 ) {
                int n = jin.nextInt();
                Integer a[] = new Integer[n];
                for ( int i = 0 ; i < n ; ++i ) {
                    a[i] = jin.nextInt();
                }
                IntegerList list = new IntegerList(a);
                print(list);
            }
        }
        if ( k == 1 ) { //test count,remove duplicates, addValue
            int n = jin.nextInt();
            Integer a[] = new Integer[n];
            for ( int i = 0 ; i < n ; ++i ) {
                a[i] = jin.nextInt();
            }
            IntegerList list = new IntegerList(a);
            while ( true ) {
                int num = jin.nextInt();
                if ( num == 0 ) { //count
                    System.out.println(list.count(jin.nextInt()));
                }
                if ( num == 1 ) {
                    list.removeDuplicates();
                }
                if ( num == 2 ) {
                    try {
                        print(list.addValue(jin.nextInt()));
                    } catch (ArrayIndexOutOfBoundsException e) {
                        throw new RuntimeException(e);
                    }
                }
                if ( num == 3 ) {
                    list.add(jin.nextInt(), jin.nextInt());
                }
                if ( num == 4 ) {
                    print(list);
                }
                if ( num == 5 ) {
                    break;
                }
            }
        }
        if ( k == 2 ) { //test shiftRight, shiftLeft, sumFirst , sumLast
            int n = jin.nextInt();
            Integer a[] = new Integer[n];
            for ( int i = 0 ; i < n ; ++i ) {
                a[i] = jin.nextInt();
            }
            IntegerList list = new IntegerList(a);
            while ( true ) {
                int num = jin.nextInt();
                if ( num == 0 ) { //count
                    try {
                        list.shiftLeft(jin.nextInt(), jin.nextInt());
                    } catch (ArrayIndexOutOfBoundsException e) {
                        throw new RuntimeException(e);
                    }
                }
                if ( num == 1 ) {
                    try {
                        list.shiftRight(jin.nextInt(), jin.nextInt());
                    } catch (ArrayIndexOutOfBoundsException e) {
                        throw new RuntimeException(e);
                    }
                }
                if ( num == 2 ) {
                    System.out.println(list.sumFirst(jin.nextInt()));
                }
                if ( num == 3 ) {
                    System.out.println(list.sumLast(jin.nextInt()));
                }
                if ( num == 4 ) {
                    print(list);
                }
                if ( num == 5 ) {
                    break;
                }
            }
        }
    }

    public static void print(IntegerList il) {
        if ( il.size() == 0 ) System.out.print("EMPTY");
        for ( int i = 0 ; i < il.size() ; ++i ) {
            if ( i > 0 ) System.out.print(" ");
            try {
                System.out.print(il.get(i));
            } catch (ArrayIndexOutOfBoundsException e) {
                throw new RuntimeException(e);
            }
        }
        System.out.println();
    }

}