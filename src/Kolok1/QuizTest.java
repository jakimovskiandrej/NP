package Kolok1;

import java.util.*;
import java.io.*;

class InvalidOperationException extends Exception {
    public InvalidOperationException(String message) {
        super(message);
    }
}

class Question {

    String type;
    String text;
    double points;

    public Question(String type, String text, double points) {
        this.type = type;
        this.text = text;
        this.points = points;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public double getPoints() {
        return points;
    }

    public void setPoints(double points) {
        this.points = points;
    }

    public double evaluateAnswer(String answer) {
        return 0;
    }
}

class MultipleChoiceQuestion extends Question {

    char answer;

    public MultipleChoiceQuestion(String type, String text, double points, char answer) {
        super(type, text, points);
        this.answer = answer;
    }

    public char isAnswer() {
        return answer;
    }

    public void setAnswer(char answer) {
        this.answer = answer;
    }

    @Override
    public double evaluateAnswer(String answer) {
        if(this.answer == answer.charAt(0)) {
            return getPoints();
        } else {
            return -getPoints() * 0.20;
        }
    }

    @Override
    public String toString() {
        return String.format("Multiple Choice Question: %s Points %d Answer: %c", text, (int)getPoints(), answer);
    }
}

class TrueFalseQuestion extends Question {

    boolean answer;

    public TrueFalseQuestion(String type, String text, double points, boolean answer) {
        super(type, text, points);
        this.answer = answer;
    }

    public boolean isAnswer() {
        return answer;
    }

    public void setAnswer(boolean answer) {
        this.answer = answer;
    }

    @Override
    public double evaluateAnswer(String answer) {
        if(Boolean.parseBoolean(answer) == this.answer) {
            return getPoints();
        } else {
            return 0;
        }
    }

    @Override
    public String toString() {
        return String.format("True/False Question: %s Points: %d Answer: %s", text, (int)getPoints(), answer);
    }
}

class Quiz {

    List<Question> questions;

    public Quiz() {
        questions = new ArrayList<>();
    }

    void addQuestion(String questionData) throws InvalidOperationException {
        questionData = questionData.trim();
        String[] parts = questionData.split(";");
        String type = parts[0];
        String text = parts[1];
        double points = Double.parseDouble(parts[2]);
        if(type.equals("MC")) {
            char answer = parts[3].charAt(0);
            if(!(answer == 'A' || answer == 'B' || answer == 'C' || answer == 'D' || answer == 'E')) {
                throw new InvalidOperationException(String.format("%c is not allowed option for this question", answer));
            }
            questions.add(new MultipleChoiceQuestion(type, text, points, answer));
        }
        if(type.equals("TF")) {
            boolean answer = Boolean.parseBoolean(parts[3]);
            questions.add(new TrueFalseQuestion(type, text, points, answer));
        }
    }

    void printQuiz(OutputStream os) {
        PrintWriter pw = new PrintWriter(os);
        questions.sort(Comparator.comparing(Question::getPoints).reversed());
        for (Question question : questions) {
            pw.println(question);
        }
        pw.flush();
    }

    void answerQuiz (List<String> answers, OutputStream os) throws InvalidOperationException {
        if(answers.size() != questions.size()) {
            throw new InvalidOperationException("Answers and questions must be of same length!");
        }
        PrintWriter pw = new PrintWriter(os);
        double totalPoints = 0;
        for(int i=0;i<questions.size();i++) {
            Question question = questions.get(i);
            String answer = answers.get(i);
            double points = question.evaluateAnswer(answer);
            totalPoints+=points;
            pw.println(String.format("%d. %.2f", i+1,points));
        }
        pw.println(String.format("Total points: %.2f", totalPoints));
        pw.flush();
    }

}

public class QuizTest {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        Quiz quiz = new Quiz();

        int questions = Integer.parseInt(sc.nextLine());

        for (int i=0;i<questions;i++) {
            try {
                quiz.addQuestion(sc.nextLine());
            } catch (InvalidOperationException e) {
                System.out.println(e.getMessage());
            }
        }

        List<String> answers = new ArrayList<>();

        int answersCount =  Integer.parseInt(sc.nextLine());

        for (int i=0;i<answersCount;i++) {
            answers.add(sc.nextLine());
        }

        int testCase = Integer.parseInt(sc.nextLine());

        if (testCase==1) {
            quiz.printQuiz(System.out);
        } else if (testCase==2) {
            try {
                quiz.answerQuiz(answers, System.out);
            } catch (InvalidOperationException e) {
                System.out.println(e.getMessage());
            }
        } else {
            System.out.println("Invalid test case");
        }
    }
}
