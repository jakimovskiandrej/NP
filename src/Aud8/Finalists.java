package Aud8;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

class WrongNumberException extends Exception {
    public WrongNumberException(String message) {
        super(message);
    }
}

class RandomPicker {
    int finalist;
    static Random RANDOM = new Random();

    public RandomPicker(int finalist) {
        this.finalist = finalist;
    }

    public List<Integer> pick(int x) throws WrongNumberException {
        if(x > finalist) {
            throw new WrongNumberException("The number x must be in border with the finalist size");
        }
        if(x <= 0) {
            throw new WrongNumberException("The number x must positive");
        }
        List<Integer> pickedFinalists = new ArrayList<Integer>();
        while (pickedFinalists.size() != x) {
            int pick = RANDOM.nextInt(finalist)+1;
            if(!pickedFinalists.contains(pick)) {
                pickedFinalists.add(pick);
            }
        }
        return pickedFinalists;
    }
}

public class Finalists {
    public static void main(String[] args) throws WrongNumberException {
        RandomPicker picker = new RandomPicker(30);
        List<Integer> picked = picker.pick(3);
        System.out.println(picked);
    }
}
