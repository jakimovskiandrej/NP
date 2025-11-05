package Aud2;

import java.util.Arrays;

public class MatrixSumAverage {

    public static double sum(double[][] a) {
        double suma = 0;
        for(int i = 0; i < a.length; i++) {
            for(int j = 0; j < a[i].length; j++) {
                suma += a[i][j];
            }
        }
        return suma;
    }

    public static double sumStream(double[][] a) {
        return Arrays.stream(a).mapToDouble(row -> Arrays.stream(row).sum()).sum();
    }

    public static double average(double[][] a) {
        double suma = 0;
        for(int i = 0; i < a.length; i++) {
            for(int j = 0; j < a[i].length; j++) {
                suma += a[i][j];
            }
        }
        if(a.length > 0) {
            return suma / a.length;
        }
        return 0;
    }

    public static double averageStream(double[][] a) {
        return Arrays.stream(a).mapToDouble(row -> Arrays.stream(row).sum()).average().orElse(0.0);
    }

    public static void main(String[] args) {
        double[][] matrix = {
                {1.5, 2.0, 3.0},
                {4.0, 5.5, 6.5},
                {7.0, 8.0, 9.5}
        };

        System.out.println("Sum (loop): " + sum(matrix));
        System.out.println("Sum (stream): " + sumStream(matrix));
        System.out.println("Average (loop): " + average(matrix));
        System.out.println("Average (stream): " + averageStream(matrix));
    }
}
