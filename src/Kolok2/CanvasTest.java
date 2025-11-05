package Kolok2;

import java.util.*;
import java.io.*;
import java.util.stream.Collectors;

class InvalidIDException extends Exception {
    public InvalidIDException(String message) {
        super(message);
    }
}

class InvalidDimensionException extends Exception {
    public InvalidDimensionException(String message) {
        super(message);
    }
}

abstract class Shape implements Comparable<Shape> {

    String id;

    public Shape(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    abstract double area();
    abstract double perimeter();
    abstract void scale(double coef);

    @Override
    public int compareTo(Shape o) {
        return Double.compare(this.area(),o.area());
    }
}

class Circle extends Shape {

    double radius;
    public Circle(String id, double radius) {
        super(id);
        this.radius = radius;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    @Override
    double area() {
        return Math.pow(radius,2) * Math.PI;
    }

    @Override
    double perimeter() {
        //2 * radius * Math.PI
        return 2 * radius * Math.PI;
    }

    @Override
    void scale(double coef) {
        radius *= coef;
    }

    @Override
    public String toString() {
        return String.format("Circle -> Radius: %.2f Area: %.2f Perimeter: %.2f", radius, area(), perimeter());
    }
}

class Square extends Shape {

    double side;

    public Square(String id, double side) {
        super(id);
        this.side = side;
    }

    public double getSide() {
        return side;
    }

    public void setSide(double side) {
        this.side = side;
    }

    @Override
    public String toString() {
        return String.format("Square: -> Side: %.2f Area: %.2f Perimeter: %.2f", side, area(), perimeter());
    }

    @Override
    double area() {
        return Math.pow(side,2);
    }

    @Override
    double perimeter() {
        return 4 * side;
    }

    @Override
    void scale(double coef) {
        side *= coef;
    }
}

class Rectangle extends Shape {

    double width;
    double height;

    public Rectangle(String id, double width, double height) {
        super(id);
        this.width = width;
        this.height = height;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    @Override
    public String toString() {
        return String.format("Rectangle: -> Sides: %.2f, %.2f Area: %.2f Perimeter: %.2f", width, height, area(), perimeter());
    }

    @Override
    double area() {
        return width * height;
    }

    @Override
    double perimeter() {
        return 2 * (width+height);
    }

    @Override
    void scale(double coef) {
        width *= coef;
        height *= coef;
    }
}

class Canvas {

    List<Shape> shapes;

    public Canvas() {
        shapes = new ArrayList<>();
    }

    void readShapes (InputStream is) throws InvalidIDException, InvalidDimensionException {
        Scanner sc = new Scanner(is);
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            String[] parts = line.split(" ");
            String number = parts[0];
            String id = parts[1];
            try {
                if(!id.matches("[a-zA-Z0-9]+") || id.length() > 6) {
                    throw new InvalidIDException(String.format("ID %s is not valid", id));
                }
                if(number.equals("1")) {
                    double radius = Double.parseDouble(parts[2]);
                    if(radius <= 0) {
                        throw new InvalidDimensionException("Dimension 0 is not allowed!");
                    }
                    shapes.add(new Circle(id, radius));
                } else if(number.equals("2")) {
                    double side = Double.parseDouble(parts[2]);
                    if(side <= 0) {
                        throw new InvalidDimensionException("Dimension 0 is not allowed!");
                    }
                    shapes.add(new Square(id, side));
                } else if(number.equals("3")) {
                    double width = Double.parseDouble(parts[2]);
                    double height = Double.parseDouble(parts[3]);
                    if(width <= 0 || height <= 0) {
                        throw new InvalidDimensionException("Dimension 0 is not allowed!");
                    }
                    shapes.add(new Rectangle(id, width, height));
                }
            } catch (InvalidIDException e) {
                System.out.println(e.getMessage());
            } catch (InvalidDimensionException e) {
                System.out.println(e.getMessage());
                break;
            }
        }
    }

    void scaleShapes (String userID, double coef) {
        for (Shape shape : shapes) {
            if(shape.getId().equals(userID)) {
                shape.scale(coef);
            }
        }
    }

    void printAllShapes (OutputStream os) {
        PrintWriter pw = new PrintWriter(os);
        for(int i=0;i<shapes.size()-1;i++) {
            int minIndex = i;
            for(int j=i+1;j<shapes.size();j++) {
                if(shapes.get(i).area() < shapes.get(j).area()) {
                    minIndex = j;
                }
            }
            if(minIndex != i) {
                Shape tmp = shapes.get(i);
                shapes.set(i,shapes.get(minIndex));
                shapes.set(minIndex,tmp);
            }
        }
        Collections.sort(shapes);
        for (Shape shape : shapes) {
            System.out.println(shape);
        }
        pw.flush();
    }

    void printByUserId (OutputStream os) {
        PrintWriter pw = new PrintWriter(os);
        Map<String,List<Shape>> userShape = new HashMap<>();
        for (Shape shape : shapes) {
            userShape.computeIfAbsent(shape.getId(),k-> new ArrayList<>()).add(shape);
        }

        List<Map.Entry<String,List<Shape>>> entries = new ArrayList<>(userShape.entrySet());

        entries.sort(Comparator.comparing((Map.Entry<String,List<Shape>> e)-> e.getValue().size())
                .thenComparing(e->e.getValue().stream().mapToDouble(Shape::area).sum()));

        for (Map.Entry<String, List<Shape>> entry : entries) {
            String userId = entry.getKey();
            List<Shape> userList = entry.getValue();
            pw.println(String.format("Shapes of user: %s", userId));
            userList.stream().sorted(Comparator.comparingDouble(Shape::area).reversed())
                    .forEach(System.out::println);
        }
    }

    void statistics (OutputStream os) {
        PrintWriter pw = new PrintWriter(os);
        double min = shapes.stream().mapToDouble(Shape::area).min().orElse(0.0);
        double max = shapes.stream().mapToDouble(Shape::area).max().orElse(0.0);
        double sum = shapes.stream().mapToDouble(Shape::area).sum();
        double average = shapes.stream().mapToDouble(Shape::area).average().orElse(0.0);
        int count = shapes.size();
        pw.println(String.format("count: %d\nsum: %.2f\nmin: %.2f\naverage: %.2f\nmax: %.2f\n", count,sum,min,average,max));
        pw.flush();
    }
}

public class CanvasTest {
    public static void main(String[] args) {
        Canvas canvas = new Canvas();

        System.out.println("READ SHAPES AND EXCEPTIONS TESTING");
        try {
            canvas.readShapes(System.in);
        } catch (InvalidIDException | InvalidDimensionException e) {
            throw new RuntimeException(e);
        }

        System.out.println("BEFORE SCALING");
        canvas.printAllShapes(System.out);
        canvas.scaleShapes("123456", 1.5);
        System.out.println("AFTER SCALING");
        canvas.printAllShapes(System.out);

        System.out.println("PRINT BY USER ID TESTING");
        canvas.printByUserId(System.out);

        System.out.println("PRINT STATISTICS");
        canvas.statistics(System.out);
    }
}
