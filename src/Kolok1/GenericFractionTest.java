package Kolok1;

import java.util.Objects;
import java.util.Scanner;

class ZeroDenominatorException extends Exception {
    public ZeroDenominatorException(String message) {
        super(message);
    }
}

class GenericFraction<T extends Number, U extends Number> {

    private T numerator;
    private U denominator;

    public GenericFraction(T numerator, U denominator) throws ZeroDenominatorException {
        if(denominator.equals(0)) {
            throw new ZeroDenominatorException("Denominator cannot be zero");
        }
        this.numerator = numerator;
        this.denominator = denominator;
    }

    public T getNumerator() {
        return numerator;
    }

    public U getDenominator() {
        return denominator;
    }

    public int findDenominator(int num, int dem) {
        int newDem = Math.max(num,dem);
        for(int i=newDem;i<=num*dem;i++) {
            if(i%num==0&&i%dem==0) {
                newDem = i;
                return newDem;
            }
        }
        return newDem;
    }

    public GenericFraction<Double, Double> add(GenericFraction<? extends Number, ? extends Number> gf) throws ZeroDenominatorException {
        int pomnozen1, pomnozen2;
        int zaednickiImenitel = findDenominator(denominator.intValue(), gf.denominator.intValue());
        pomnozen1 = zaednickiImenitel / denominator.intValue();
        pomnozen2 = zaednickiImenitel / gf.denominator.intValue();

        Double novNum = numerator.doubleValue() * pomnozen1 + gf.numerator.doubleValue() * pomnozen2;
        Double novDem = Double.valueOf(zaednickiImenitel);
        for (int i = (int) Math.max(novDem, novNum); i >= 2; i--) {
            if (novDem % i == 0 && novNum % i == 0) {
                novNum = novNum / i;
                novDem = novDem / i;
            }
        }
        return new GenericFraction<Double,Double>(novNum,novDem);
    }

    public double toDouble() {
        return numerator.doubleValue() / denominator.doubleValue();
    }

    @Override
    public String toString() {
        return String.format("%.2f / %.2f", numerator.doubleValue(), denominator.doubleValue());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GenericFraction<?, ?> that = (GenericFraction<?, ?>) o;
        return Objects.equals(numerator, that.numerator) && Objects.equals(denominator, that.denominator);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(numerator);
        result = 31 * result + Objects.hashCode(denominator);
        return result;
    }
}

public class GenericFractionTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        double n1 = scanner.nextDouble();
        double d1 = scanner.nextDouble();
        float n2 = scanner.nextFloat();
        float d2 = scanner.nextFloat();
        int n3 = scanner.nextInt();
        int d3 = scanner.nextInt();
        try {
            GenericFraction<Double, Double> gfDouble = new GenericFraction<Double, Double>(n1, d1);
            GenericFraction<Float, Float> gfFloat = new GenericFraction<Float, Float>(n2, d2);
            GenericFraction<Integer, Integer> gfInt = new GenericFraction<Integer, Integer>(n3, d3);
            System.out.printf("%.2f\n", gfDouble.toDouble());
            System.out.println(gfDouble.add(gfFloat));
            System.out.println(gfInt.add(gfFloat));
            System.out.println(gfDouble.add(gfInt));
            gfInt = new GenericFraction<Integer, Integer>(n3, 0);
        } catch(ZeroDenominatorException e) {
            System.out.println(e.getMessage());
        }

        scanner.close();
    }

}

// вашиот код овде
