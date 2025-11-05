package Lab3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

class IndexOutOfBoundsException extends Exception {
    public IndexOutOfBoundsException(String message) {
        super(message);
    }
}

class IntegerList {
    private ArrayList<Integer> list;

    public IntegerList() {
        list = new ArrayList<>();
    }
    //IntegerList(Integer… numbers) - конструктор коj креира листа што ги содржи елементите numbers во истиот редослед во кој тие се појавуваат во низата.
    public IntegerList(Integer... numbers) {
        list = new ArrayList<>(Arrays.asList(numbers));
    }
    //add(int el, int idx) - го додава елементот на соодветниот индекс. Доколку има други елементи после таа позиција истите се поместуваат на десно за едно место (нивните индекси им се зголемуваат за 1). Доколку idx е поголемо од сегашната големина на листата ја зголемуваме листата и сите нови елементи ги иницијалираме на нула (освен тој на позиција idx кој го поставуваме на el).
    public void add(int el, int idx) {
        list.add(idx, el);
    }
    //remove(int idx):int - го отстранува елементот на дадена позиција од листата и истиот го враќа. Доколку после таа позиција има други елементи истите се поместуваат во лево (нивните индекси се намалуваат за 1).
    public int remove(int idx) throws IndexOutOfBoundsException {
        if(idx < 0 || idx > list.size()) {
            throw new IndexOutOfBoundsException("Wrong index");
        }
        return list.remove(idx);
    }
    //set(int el, int idx) - го поставува елементот на соодветната позиција.
    public void set(int el, int idx) throws IndexOutOfBoundsException {
        if(idx < 0 || idx > list.size()) {
            throw new IndexOutOfBoundsException("Wrong index");
        }
        list.set(idx, el);
    }
    //get(int idx):int - го враќа елементот на соодветната позиција.
    public int get(int idx) throws IndexOutOfBoundsException {
        if(idx < 0 || idx > list.size()) {
            throw new IndexOutOfBoundsException("Wrong index");
        }
        return list.get(idx);
    }
    //size():int - го враќа бројот на елементи во листата.
    public int size() {
        return list.size();
    }
    //count(int el):int - го враќа бројот на појавувања на соодветниот елемент во листата.
    public int count(int el) {
        int counter = 0;
        for(int i=0;i<list.size();i++) {
            if(list.get(i).equals(el)) {
                counter++;
            }
        }
        return counter;
    }
    //removeDuplicates() - врши отстранување на дупликат елементите од листата. Доколку некој елемент се сретнува повеќе пати во листата ја оставаме само последната копија од него. Пр: 1,2,4,3,4,5. -> removeDuplicates() -> 1,2,3,4,5
    public void removeDuplicates() {
        for(int i=0;i<list.size();i++) {
            for(int j=0;j<list.size()-1;j++) {
                if(list.get(i).equals(list.get(j))) {
                    list.remove(i);
                    break;
                }
            }
        }
    }
    //sumFirst(int k):int - ја дава сумата на првите k елементи.
    public int sumFirst(int k) {
        int sum = 0;
        for(int i=0;i<k;i++) {
            sum+=list.get(i);
        }
        return sum;
    }
    //sumLast(int k):int - ја дава сумата на последните k елементи.
    public int sumLast(int k) {
        int sum = 0;
        for(int i=list.size()-1;i>=0;i--) {
            sum+=list.get(i);
        }
        return sum;
    }
    //shiftRight(int idx, int k) - го поместува елементот на позиција idx за k места во десно. При поместувањето листата ја третираме како да е кружна. Пр: list = [1,2,3,4]; list.shiftLeft(1,2); list = [1,3,4,2] - (листата е нула индексирана така да индексот 1 всушност се однесува на елементот 2 кој го поместуваме две места во десно) list = [1,2,3,4]; list.shiftLeft(2, 3); list = [1,3,2,4] - елементот 3 го поместуваме 3 места во десно. По две поместувања стигнуваме до крајот на листата и потоа продолжуваме да итерираме од почетокот на листата уште едно место и овде го сместуваме.
    public void shiftRight(int idx, int k) throws IndexOutOfBoundsException {
        if(idx < 0 || idx > list.size()) {
            throw new IndexOutOfBoundsException("Wrong index");
        }
        k = k % list.size();
        int newIndex = (idx+k) % list.size();
        Integer element = list.get(idx);
        list.remove(idx);
        list.add(newIndex,element);
    }
    //shiftLeft(int idx , int k) - аналогно на shiftRight.
    public void shiftLeft(int idx, int k) throws IndexOutOfBoundsException {
        if(idx < 0 || idx > list.size()) {
            throw new IndexOutOfBoundsException("Wrong index");
        }
        k = k % list.size();
        int newIndex = (idx-k+list.size()) % list.size();
        Integer element = list.get(idx);
        list.remove(idx);
        list.add(newIndex,element);
    }
    //addValue(int value):IntegerList - враќа нова листа каде елементите се добиваат од оригиналната листа со додавање на value на секој елемент. Пр: list = [1,4,3]; addValue(5) -> [6,9,8]
    public IntegerList addValue(int value) {
        IntegerList result = new IntegerList();
        for(int i=0;i<list.size();i++) {
            result.add(list.get(i)+value,i);
        }
        return result;
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
                        } catch (IndexOutOfBoundsException e) {
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
                    print(list.addValue(jin.nextInt()));
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
                    }catch (IndexOutOfBoundsException e) {
                        throw new RuntimeException(e);
                    }
                }
                if ( num == 1 ) {
                    try {
                        list.shiftRight(jin.nextInt(), jin.nextInt());
                    } catch (IndexOutOfBoundsException e) {
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
            } catch (IndexOutOfBoundsException e) {
                throw new RuntimeException(e);
            }
        }
        System.out.println();
    }

}
