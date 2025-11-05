package Lab3;

import java.util.*;

class ArrayIndexOutOfBoundsException extends Exception {
    public ArrayIndexOutOfBoundsException(String s) {
        super();
    }
}

class ResizableArray<T> {
    private List<T> elements;

    public ResizableArray() {
        elements = new ArrayList<T>();
    }

    //addElement(T element) - додава нов елемент во полето (доколку нема доволно место го зголемува капацитетот на полето).
    public void addElement(T element) {
        elements.add(element);
    }
    //removeElement(T element):boolean - aко постои таков елемент истиот го брише и враќа true, во спротивно враќа false, доколку има повеќе инстанци од дадениот елемент се брише само една од нив (ако има многу празно место во полето го намалува неговиот капацитет)
    public boolean removeElement(T element) {
        if(elements.contains(element)) {
            elements.remove(element);
            return true;
        }
        return false;
    }
    //contains(T element):boolean - враќа true доколку во полето постои дадениот елемент
    public boolean contains(T element) {
        return elements.contains(element);
    }
    //toArray():Object[] - ги враќа сите елементи во полето како обична низа
    public Object[] toArray() {
        return elements.toArray();
    }
    //isEmpty() - враќа true доколку во полето нема ниеден елемент
    public boolean isEmpty() {
        return elements.isEmpty();
    }
    //count():int - го браќа бројот на елементи во полето
    public int count() {
        return elements.size();
    }
    //elementAt(int idx):T - го враќа елементот на соодветната позиција, доколку нема таков фрла исклучок ArrayIndexOutOfBoundsException (елементите во полето се наоѓаат на позиции [0, count()])
    public T elementAt(int index) throws ArrayIndexOutOfBoundsException {
        if(!elements.contains(elements.get(index))) {
            throw new ArrayIndexOutOfBoundsException("Enter valid index for start & end");
        }
        return elements.get(index);
    }

    public static <T> void copyAll(ResizableArray<? super T> dest, ResizableArray<? extends T> src) {
        for(int i = 0; i < src.count(); i++) {
            try {
                dest.addElement(src.elementAt(i));
            } catch (ArrayIndexOutOfBoundsException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public ResizableArray<T> subArray(int start, int end) throws ArrayIndexOutOfBoundsException {
        if(start < 0 || start > end) {
            throw new ArrayIndexOutOfBoundsException("Enter valid index for start & end");
        }

        ResizableArray<T> result = new ResizableArray<>();
        for(int i = start; i < end; i++) {
            result.addElement(elementAt(i));
        }

        return result;
    }
}

class IntegerArray extends ResizableArray<Integer> {
    //sum():double - ја враќа сумата на сите елементи во полето
    public double sum() {
        double suma = 0;
        for(int i=0;i<count();i++) {
            try {
                suma+=elementAt(i);
            } catch (ArrayIndexOutOfBoundsException e) {
                throw new RuntimeException(e);
            }
        }
        return suma;
    }
    //mean():double - го дава просекот на сите елементи во полето
    public double mean() {
        return sum()/count();
    }
    //countNonZero():int - го дава бројот на елементи во полето кои имаат вредност различна од нула
    public int countNonZero() {
        int counter = 0;
        for(int i=0;i<count();i++) {
            try {
                if(elementAt(i) != 0) {
                    counter++;
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                throw new RuntimeException(e);
            }
        }
        return counter;
    }
    //distinct():IntegerArray - враќа нов објект кој во себе ги содржи истите елементи кои ги содржи this, но нема дупликат елементи
    public IntegerArray distinct() {
        IntegerArray distinct = new IntegerArray();
        for(int i=0;i<count();i++) {
            Integer current = null;
            try {
                current = elementAt(i);
                if (!distinct.contains(current)) {
                    distinct.addElement(current);
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                throw new RuntimeException(e);
            }
        }
        return distinct;
    }
    //increment(int offset):IntegerArray - враќа нов објект кој во себе ги содржи сите елемeнти кои ги содржи this, но на нив додавајќи offset
    public IntegerArray increment(int offset) {
        IntegerArray result = new IntegerArray();
        for(int i=0;i<count();i++) {
            try {
                result.addElement(elementAt(i) + offset);
            } catch (ArrayIndexOutOfBoundsException e) {
                throw new RuntimeException(e);
            }
        }
        return result;
    }

}

public class ResizableArrayTest {

    public static void main(String[] args) {
        Scanner jin = new Scanner(System.in);
        int test = jin.nextInt();
        if ( test == 0 ) { //test ResizableArray on ints
            ResizableArray<Integer> a = new ResizableArray<Integer>();
            int start = jin.nextInt();
            int end = jin.nextInt();
            System.out.println(a.count());
            int first = jin.nextInt();
            a.addElement(first);
            System.out.println(a.count());
            int last = first;
            while ( jin.hasNextInt() ) {
                last = jin.nextInt();
                a.addElement(last);
            }
            System.out.println(a.count());
            System.out.println(a.contains(first));
            System.out.println(a.contains(last));
            System.out.println(a.removeElement(first));
            System.out.println(a.contains(first));
            System.out.println(a.count());
            try {
                System.out.println(a.subArray(start,end));
            } catch (ArrayIndexOutOfBoundsException e) {
                System.out.println(e.getMessage());
            }
        }
        if ( test == 1 ) { //test ResizableArray on strings
            ResizableArray<String> a = new ResizableArray<String>();
            System.out.println(a.count());
            String first = jin.next();
            a.addElement(first);
            System.out.println(a.count());
            String last = first;
            for ( int i = 0 ; i < 4 ; ++i ) {
                last = jin.next();
                a.addElement(last);
            }
            System.out.println(a.count());
            System.out.println(a.contains(first));
            System.out.println(a.contains(last));
            System.out.println(a.removeElement(first));
            System.out.println(a.contains(first));
            System.out.println(a.count());
            ResizableArray<String> b = new ResizableArray<String>();
            ResizableArray.copyAll(b, a);
            System.out.println(b.count());
            System.out.println(a.count());
            System.out.println(a.contains(first));
            System.out.println(a.contains(last));
            System.out.println(b.contains(first));
            System.out.println(b.contains(last));
            ResizableArray.copyAll(b, a);
            System.out.println(b.count());
            System.out.println(a.count());
            System.out.println(a.contains(first));
            System.out.println(a.contains(last));
            System.out.println(b.contains(first));
            System.out.println(b.contains(last));
            System.out.println(b.removeElement(first));
            System.out.println(b.contains(first));
            System.out.println(b.removeElement(first));
            System.out.println(b.contains(first));

            System.out.println(a.removeElement(first));
            ResizableArray.copyAll(b, a);
            System.out.println(b.count());
            System.out.println(a.count());
            System.out.println(a.contains(first));
            System.out.println(a.contains(last));
            System.out.println(b.contains(first));
            System.out.println(b.contains(last));
        }
        if ( test == 2 ) { //test IntegerArray
            IntegerArray a = new IntegerArray();
            System.out.println(a.isEmpty());
            while ( jin.hasNextInt() ) {
                a.addElement(jin.nextInt());
            }
            jin.next();
            System.out.println(a.sum());
            System.out.println(a.mean());
            System.out.println(a.countNonZero());
            System.out.println(a.count());
            IntegerArray b = a.distinct();
            System.out.println(b.sum());
            IntegerArray c = a.increment(5);
            System.out.println(c.sum());
            if ( a.sum() > 100 )
                ResizableArray.copyAll(a, a);
            else
                ResizableArray.copyAll(a, b);
            System.out.println(a.sum());
            System.out.println(a.removeElement(jin.nextInt()));
            System.out.println(a.sum());
            System.out.println(a.removeElement(jin.nextInt()));
            System.out.println(a.sum());
            System.out.println(a.removeElement(jin.nextInt()));
            System.out.println(a.sum());
            System.out.println(a.contains(jin.nextInt()));
            System.out.println(a.contains(jin.nextInt()));
        }
        if ( test == 3 ) { //test insanely large arrays
            LinkedList<ResizableArray<Integer>> resizable_arrays = new LinkedList<ResizableArray<Integer>>();
            for ( int w = 0 ; w < 500 ; ++w ) {
                ResizableArray<Integer> a = new ResizableArray<Integer>();
                int k =  2000;
                int t =  1000;
                for ( int i = 0 ; i < k ; ++i ) {
                    a.addElement(i);
                }

                a.removeElement(0);
                for ( int i = 0 ; i < t ; ++i ) {
                    a.removeElement(k-i-1);
                }
                resizable_arrays.add(a);
            }
            System.out.println("You implementation finished in less then 3 seconds, well done!");
        }
    }

}
