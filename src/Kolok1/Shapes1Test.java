package Kolok1;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

class Canvas {

    String id;
    List<Integer> sizes;

    public Canvas(String id, List<Integer> sizes) {
        this.id = id;
        this.sizes = sizes;
    }

    public String getId() {
        return id;
    }

    public List<Integer> getSizes() {
        return sizes;
    }
}

class ShapesApplication {

    List<Canvas> canvases;

    public ShapesApplication() {
        canvases = new ArrayList<Canvas>();
    }

    //canvas_id size_1 size_2 size_3 …. size_n,
    public int readCanvases (InputStream inputStream) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        int totalSquares = 0;
        while ((line = br.readLine()) != null && !line.isEmpty()) {
            String[] parts = line.split("\\s+");
            String id = parts[0];
            List<Integer> sizes = new ArrayList<>();
            for(int i = 1; i < parts.length; i++) {
                sizes.add(Integer.parseInt(parts[i]));
            }
            canvases.add(new Canvas(id,sizes));
            totalSquares += sizes.size();
        }
        return totalSquares;
    }

    public void printLargestCanvasTo (OutputStream outputStream) {
        // canvas_id squares_count total_squares_perimeter.
        PrintWriter pw = new PrintWriter(outputStream);
        Canvas maxCanvas = null;
        int maxPerimeter = 0;
        for (Canvas canvases : canvases) {
            int perimeter = canvases.getSizes().stream().mapToInt(size -> 4 * size).sum();
            if (perimeter > maxPerimeter || maxCanvas == null) {
                maxPerimeter = perimeter;
                maxCanvas = canvases;
            }
        }
        if(maxCanvas != null) {
            pw.printf("%s %d %d", maxCanvas.getId(), maxPerimeter, maxCanvas.getSizes().size());
        }
        pw.flush();
    }
}

public class Shapes1Test {
    public static void main(String[] args) {
        ShapesApplication shapesApplication = new ShapesApplication();

        System.out.println("===READING SQUARES FROM INPUT STREAM===");
        try {
            System.out.println(shapesApplication.readCanvases(System.in));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("===PRINTING LARGEST CANVAS TO OUTPUT STREAM===");
        shapesApplication.printLargestCanvasTo(System.out);

    }
}
