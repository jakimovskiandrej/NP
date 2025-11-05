package Aud2;

public class CombinationLock {

    private int combination;
    private boolean isOpen;

    public CombinationLock(int combination) {
        if (isValidCombination(combination)) {
            this.combination = combination;
        } else {
            throw new RuntimeException("Invalid combination");
        }
        this.isOpen = false;
    }

    public boolean isValidCombination(int combination) {
        return combination >= 100 && combination <= 999;
    }

    public boolean open(int combination) {
        this.isOpen = (this.combination == combination);
        return this.isOpen;
    }

    public boolean changeCombo(int oldCombination, int newCombination) {
        if(open(oldCombination) && isValidCombination(newCombination)) {
            this.combination = newCombination;
            return true;
        }
        return false;
    }

    public static void main(String[] args) {
        //me mrzi
    }
}
