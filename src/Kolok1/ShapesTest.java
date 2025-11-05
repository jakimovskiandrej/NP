package Kolok1;

import java.util.*;
import java.util.stream.Collectors;

enum Color {
    RED, GREEN, BLUE
}

interface Scalable {
    void scale(float scaleFactor);
}

interface Stackable {
    float weight();
}

class Circle implements Scalable, Stackable {
    String id;
    Color color;
    float radius;

    public Circle(String id, Color color, float radius) {
        this.id = id;
        this.color = color;
        this.radius = radius;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    @Override
    public void scale(float scaleFactor) {
        radius *= scaleFactor;
    }

    @Override
    public float weight() {
        return (float) (Math.pow(radius,2) * Math.PI);
    }

    @Override
    public String toString() {
        return String.format("C: %-5s%-10s%-10.2f", id, color, weight());
    }
}

class Rectangle implements Scalable, Stackable {
    String id;
    Color color;
    float width;
    float height;

    public Rectangle(String id, Color color, float width, float height) {
        this.id = id;
        this.color = color;
        this.width = width;
        this.height = height;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    @Override
    public String toString() {
        return String.format("R: %-5s%-10s%-10.2f", id,color,weight());
    }

    @Override
    public void scale(float scaleFactor) {
        height*=scaleFactor;
        width*=scaleFactor;
    }

    @Override
    public float weight() {
        return width * height;
    }
}

class Canvas {

    List<Circle> circles;
    List<Rectangle> rectangles;

    public Canvas() {
        circles = new ArrayList<>();
        rectangles = new ArrayList<>();
    }

    void add(String id, Color color, float radius) {
        Circle c = new Circle(id,color,radius);
        circles.add(c);
        circles.sort(Comparator.comparing(Circle::weight).reversed());
    }

    void add(String id, Color color, float width, float height) {
        Rectangle r = new Rectangle(id,color,width,height);
        rectangles.add(r);
        rectangles.sort(Comparator.comparing(Rectangle::weight).reversed());
    }

    void scale(String id, float scaleFactor) {
        for(int i=0;i<circles.size();i++) {
            if(circles.get(i).getId().equals(id)) {
                Circle c = circles.remove(i);
                c.scale(scaleFactor);
                circles.add(c);
                circles.sort(Comparator.comparing(Circle::weight).reversed());
                return;
            }
        }
        for(int i=0;i<rectangles.size();i++) {
            if(rectangles.get(i).getId().equals(id)) {
                Rectangle r = rectangles.remove(i);
                r.scale(scaleFactor);
                rectangles.add(r);
                rectangles.sort(Comparator.comparing(Rectangle::weight).reversed());
                return;
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Circle circle : circles) {
            sb.append(circle).append("\n");
        }
        for (Rectangle rectangle : rectangles) {
            sb.append(rectangle).append("\n");
        }
        return sb.toString();
    }
}

public class ShapesTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Canvas canvas = new Canvas();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] parts = line.split(" ");
            int type = Integer.parseInt(parts[0]);
            String id = parts[1];
            if (type == 1) {
                Color color = Color.valueOf(parts[2]);
                float radius = Float.parseFloat(parts[3]);
                canvas.add(id, color, radius);
            } else if (type == 2) {
                Color color = Color.valueOf(parts[2]);
                float width = Float.parseFloat(parts[3]);
                float height = Float.parseFloat(parts[4]);
                canvas.add(id, color, width, height);
            } else if (type == 3) {
                float scaleFactor = Float.parseFloat(parts[2]);
                System.out.println("ORIGNAL:");
                System.out.print(canvas);
                canvas.scale(id, scaleFactor);
                System.out.printf("AFTER SCALING: %s %.2f\n", id, scaleFactor);
                System.out.print(canvas);
            }

        }
    }
}
