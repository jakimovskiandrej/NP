package Kolok2;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

interface Element<T> {
    public long id();
    public double distance(T other);
}

class Cluster<T extends Element<T>> {

    List<T> elements;

    public Cluster() {
        this.elements = new ArrayList<T>();
    }

    void addItem(T element) {
        elements.add(element);
    }

    void near(long id, int top) {
        T reference = elements.stream().filter(e -> e.id() == id).findFirst().orElse(null);
        if (reference == null) {
            return;
        }
        List<T> sorted = elements.stream().filter(e -> e.id() != id)
                .sorted((e1, e2) -> {
                    double distance1 = e1.distance(reference);
                    double distance2 = e2.distance(reference);
                    return Double.compare(distance1, distance2);
                }).limit(top)
                .collect(Collectors.toList());
        int rank = 1;
        for (T e : sorted) {
            double distance = reference.distance(e);
            System.out.printf("%d. %d -> %.3f\n", rank++, e.id(), distance);
        }
    }
}


class Point2D implements Element<Point2D> {

    long id;
    float x;
    float y;

    public Point2D(long id, float x, float y) {
        this.id = id;
        this.x = x;
        this.y = y;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    @Override
    public long id() {
        return id;
    }

    @Override
    public double distance(Point2D other) {
        return Math.sqrt(Math.pow(x - other.x,2) + Math.pow(y - other.y,2));
    }
}

public class ClusterTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Cluster<Point2D> cluster = new Cluster<>();
        int n = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < n; ++i) {
            String line = scanner.nextLine();
            String[] parts = line.split(" ");
            long id = Long.parseLong(parts[0]);
            float x = Float.parseFloat(parts[1]);
            float y = Float.parseFloat(parts[2]);
            cluster.addItem(new Point2D(id, x, y));
        }
        int id = scanner.nextInt();
        int top = scanner.nextInt();
        cluster.near(id, top);
        scanner.close();
    }
}
