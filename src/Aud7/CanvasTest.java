package Aud7;

import java.util.ArrayList;
import java.util.List;

enum Color {
    RED,GREEN,BLUE,YELLOW,WHITE,BLACK,PURPLE
}

interface Scalable {
    void scale(float scaleFactor);
}

interface Stackable {
    float weight();
}

class Circle extends Shape implements Stackable, Scalable{
    float radius;
    static float PI = (float) Math.PI;

    public Circle(String id, Color color, float radius) {
        super(id, color);
        this.radius = radius;
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    @Override
    public String toString() {
        return String.format("%-5s: %-10s %-10.2s", id, color, weight());
    }

    @Override
    public float weight() {
        return (float) (PI * Math.pow(radius,2));
    }

    @Override
    public void scale(float scaleFactor) {
        this.radius *= scaleFactor;
    }
}

class Rectangle extends Shape implements Stackable, Scalable{
    float width;
    float height;

    public Rectangle(String id, Color color, float width, float height) {
        super(id, color);
        this.width = width;
        this.height = height;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    @Override
    public String toString() {
        return String.format("%-5s: %-10s %-10.2s", id, color, weight());
    }

    @Override
    public void scale(float scaleFactor) {
        this.width *= scaleFactor;
        this.height *= scaleFactor;
    }

    @Override
    public float weight() {
        return width * height;
    }
}

abstract class Shape implements Scalable{
    String id;
    Color color;

    public Shape(String id, Color color) {
        this.id = id;
        this.color = color;
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

    @Override
    public void scale(float scaleFactor) {}
}

class Canvas {
    List<Shape> shapes;

    public Canvas() {
        shapes = new ArrayList<>();
    }

    public void add(String id, Color color, float radius) {
        shapes.add(new Circle(id, color, radius));
    }

    public void add(String id, Color color, float width, float height) {
        shapes.add(new Rectangle(id, color, width, height));
    }

    public void scale(String id, float scaleFactor) {
        Shape shape = null;
        int oldIndex = -1;
        for(int i=0; i<shapes.size(); i++) {
            if(shapes.get(i).getId().equals(id)) {
                shape = shapes.get(i);
                oldIndex = i;
                break;
            }
        }
        if(shape!=null) {
            shape.scale(scaleFactor);
            shapes.remove(oldIndex);
            float newWeight = ((Stackable) shape).weight();
            int newIndex = 0;
            while (newIndex < shapes.size() && ((Stackable) shapes.get(newIndex)).weight() < newWeight) {
                newIndex++;
            }
            shapes.add(newIndex, shape);
        }
    }

}

public class CanvasTest {
    public static void main(String[] args) {
        Canvas canvas = new Canvas();

        // Додавање на кругови
        canvas.add("c1", Color.RED, 2);    // weight ≈ 12.57
        canvas.add("c2", Color.BLUE, 3);   // weight ≈ 28.27
        canvas.add("c3", Color.GREEN, 1);  // weight ≈ 3.14

        // Додавање на правоаголници
        canvas.add("r1", Color.YELLOW, 2, 3);  // weight = 6
        canvas.add("r2", Color.BLACK, 1, 1);   // weight = 1
        canvas.add("r3", Color.WHITE, 4, 4);   // weight = 16

        System.out.println("Shapes before scaling:");
        for (Shape shape : canvas.shapes) {
            System.out.println(shape);
        }

        // Скалирање на c1 со фактор 2 -> radius 4 -> weight ≈ 50.27
        canvas.scale("c1", 2);

        System.out.println("\nShapes after scaling c1:");
        for (Shape shape : canvas.shapes) {
            System.out.println(shape);
        }
    }
}

