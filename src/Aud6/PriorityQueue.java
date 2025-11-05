package Aud6;
import java.util.ArrayList;
import java.util.List;

public class PriorityQueue<T> {

    private final ArrayList<T> queue;
    private final ArrayList<Integer> priorities;

    public PriorityQueue() {
        queue = new ArrayList<>();
        priorities = new ArrayList<>();
    }

    public void add(T item, int priority) {
        int i;
        for(i = 0; i < priorities.size(); i++) {
            if(priorities.get(i) < priority) {
                break;
            }
        }
        queue.add(i,item);
        priorities.add(priority);
    }

    public T remove() {
        if(queue.isEmpty()) {
            return null;
        }
        T item = queue.getFirst();
        queue.removeFirst();
        priorities.removeFirst();
        return item;
    }

    public static void main(String[] args) {
        PriorityQueue<String> pq = new PriorityQueue<String>();
        pq.add("X", 0);
        pq.add("Y", 1);
        pq.add("Z", 3);
        System.out.println(pq.remove()); // Returns X
        System.out.println(pq.remove()); // Returns Z
        System.out.println(pq.remove()); // Returns Y
    }
}
