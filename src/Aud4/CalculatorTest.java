package Aud4;

import java.util.Scanner;

class UnknownOperatorException extends Exception {
    public UnknownOperatorException(String message, char operator) {
        super(message);
    }
}

interface Strategy {
    double calculate(double num1,double num2);
}

class Addition implements Strategy {

    @Override
    public double calculate(double num1, double num2) {
        return num1 + num2;
    }
}

class Multiplication implements Strategy {

    @Override
    public double calculate(double num1, double num2) {
        return num1 * num2;
    }
}

class Subtraction implements Strategy{

    @Override
    public double calculate(double num1, double num2) {
        return num1 - num2;
    }
}

class Division implements Strategy {

    @Override
    public double calculate(double num1, double num2) {
        if(num2 > 0) {
            return num2 / num1;
        }
        else {
            return 0;
        }
    }
}

class Calculator {

    private double result;
    private Strategy strategy;

    public Calculator() {
        result = 0.0;
    }

    public String init() {
        return String.format("result = %.2f", result);
    }

    public double getResult() {
        return result;
    }

    public String execute(char operator, double value) throws UnknownOperatorException {
        if(operator == '+') {
            strategy = new Addition();
        }
        else if(operator == '-') {
            strategy = new Subtraction();
        }
        else if(operator == '*') {
            strategy = new Multiplication();
        }
        else if(operator == '/') {
            strategy = new Division();
        }
        else {
            throw new UnknownOperatorException("%s is an Unknown operator", operator);
        }
        result = strategy.calculate(value, result);
        return String.format("result = %.2f", result);
    }
}

public class CalculatorTest {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        while (true) {
            Calculator calc = new Calculator();
            System.out.println(calc.init());
            while (true) {
                String line = sc.nextLine();
                char choice = line.charAt(0);
                if(choice == 'r') {
                    System.out.printf("Final result: %.2f%n",calc.getResult());
                    break;
                }
                String[] parts = line.split("\\s+");
                char operator = parts[0].charAt(0);
                double value = Double.parseDouble(parts[1]);
                try {
                    String result = calc.execute(operator,value);
                    System.out.println(result);
                    System.out.println(calc);
                } catch (UnknownOperatorException e) {
                    throw new RuntimeException(e);
                }
            }
            System.out.println("Again (Y/N)");
            String line = sc.nextLine();
            if(line.equals("Y")) {
                calc = new Calculator();
            }
            char choice = line.charAt(0);
            if (choice == 'N') {
                break;
            }
        }
    }
}
