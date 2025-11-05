package Lab4;

import java.util.*;
import java.util.stream.Collectors;

class SuperString {

    private final LinkedList<String> list;
    private List<String> result;

    public SuperString() {
        list = new LinkedList<>();
        result = new ArrayList<>();
    }

    public void append(String s) {
        list.add(s);
        result.add(0,s);
    }

    public void insert(String s) {
        list.add(0,s);
        result.add(0,s);
    }

    public boolean contains(String s) {
        return toString().contains(s);
    }

    public void reverse() {
        Collections.reverse(list);
        for(int i=0;i<list.size();i++) {
            list.set(i,reverseString(list.get(i)));
            result.set(i,reverseString(result.get(i)));
        }
    }

    public void removeLast(int k) {
        if(list.isEmpty()) {
            return;
        }
        for(int i=0;i<k;i++) {
            list.remove(result.get(i));
        }
        result = result.subList(k,result.size());
    }

    @Override
    public String toString() {
        return list.stream().filter(s->!s.isEmpty()).collect(Collectors.joining());
    }

    public String reverseString(String s) {
        StringBuilder sb = new StringBuilder();
        for(int i=s.length()-1;i>=0;i--) {
            sb.append(s.charAt(i));
        }
        return sb.toString();
    }
}

public class SuperStringTest {

    public static void main(String[] args) {
        Scanner jin = new Scanner(System.in);
        int k = jin.nextInt();
        if (  k == 0 ) {
            SuperString s = new SuperString();
            while ( true ) {
                int command = jin.nextInt();
                if ( command == 0 ) {//append(String s)
                    s.append(jin.next());
                }
                if ( command == 1 ) {//insert(String s)
                    s.insert(jin.next());
                }
                if ( command == 2 ) {//contains(String s)
                    System.out.println(s.contains(jin.next()));
                }
                if ( command == 3 ) {//reverse()
                    s.reverse();
                }
                if ( command == 4 ) {//toString()
                    System.out.println(s);
                }
                if ( command == 5 ) {//removeLast(int k)
                    s.removeLast(jin.nextInt());
                }
                if ( command == 6 ) {//end
                    break;
                }
            }
        }
    }

}
