package Kolok1;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

class IrregularCanvasException extends Exception {
    public IrregularCanvasException(String message) {
        super(message);
    }
}

enum TYPE {
    S,C
}

class Canvas {
    String id;
    int totalCircles;
    int totalSquares;
    List<Double> shapes;

    public Canvas(String id) {
        this.id = id;
        totalCircles = 0;
        totalSquares = 0;
        shapes = new ArrayList<>();
    }

    public int getTotalCircles() {
        return totalCircles;
    }

    public int getTotalSquares() {
        return totalSquares;
    }

    public String getId() {
        return id;
    }

    public double getTotalArea() {
        return shapes.stream().mapToDouble(Double::doubleValue).sum();
    }

    public double getMinArea() {
        return shapes.stream().mapToDouble(Double::doubleValue).min().orElse(0);
    }
    public double getMaxArea() {
        return shapes.stream().mapToDouble(Double::doubleValue).max().orElse(0);
    }
    public double getAverageArea() {
        return shapes.stream().mapToDouble(Double::doubleValue).average().orElse(0);
    }

}

class ShapesApplication {

    double maxArea;
    List<Canvas> canvases;
    static double AREA = 10000.00;


    public ShapesApplication(double maxArea) {
        this.maxArea = maxArea;
    }

    public double calculateArea(String type, double size) {
        if(type.equals("C")) {
            return Math.PI * Math.pow(size,2);
        } else if(type.equals("S")) {
            return Math.pow(size,2);
        }
        return 0;
    }

    public void readCanvases (InputStream inputStream) throws IrregularCanvasException, IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        canvases = new ArrayList<>();
        while ((line=br.readLine()) != null && !line.isEmpty()) {
            String[] parts = line.split("\\s+");
            String id = parts[0];
            Canvas canvas = new Canvas(id);
            for(int i=1; i<parts.length; i+=2) {
                String type = parts[i];
                double size = Double.parseDouble(parts[i+1]);
                double area = calculateArea(type,size);
                if(area > AREA) {
                    throw new IrregularCanvasException(String.format("Canvas %s has a shape with area larger than %.2f", id, AREA));
                }
                canvas.shapes.add(area);
                if(type.equals("S")) {
                    canvas.totalSquares++;
                } else if(type.equals("C")) {
                    canvas.totalCircles++;
                }
            }
            canvases.add(canvas);
        }
    }

    public void printCanvases (OutputStream os) {
        PrintWriter pw = new PrintWriter(os);
        canvases.stream()
                        .sorted(Comparator.comparingDouble(Canvas::getTotalArea).reversed())
                                .forEach(c -> pw.printf("%s %d %d %d %.2f %.2f %.2f",
                                        c.getId(), c.shapes.size(), c.getTotalCircles(), c.getTotalSquares(), c.getMinArea(), c.getMaxArea(), c.getAverageArea()));
        pw.flush();
        pw.close();
    }
}

public class Shapes2Test {
    public static void main(String[] args) {

        ShapesApplication shapesApplication = new ShapesApplication(10000);

        System.out.println("===READING CANVASES AND SHAPES FROM INPUT STREAM===");
        try {
            shapesApplication.readCanvases(System.in);
        } catch (IrregularCanvasException | IOException e) {
            System.out.println(e.getMessage());
            return;
        }

        System.out.println("===PRINTING SORTED CANVASES TO OUTPUT STREAM===");
        shapesApplication.printCanvases(System.out);


    }
}
