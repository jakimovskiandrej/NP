package Aud4;

import java.util.ArrayList;
import java.util.List;

class CannotWithdrawAmountException extends Exception {
    public CannotWithdrawAmountException(double currentAmount, double amount) {
        super(String.format("Cannot withdraw %.2f from %.2f", currentAmount, amount));
    }
}

interface InterestBearingAccount {
    void addInterest();
}

class Bank {
    private List<Account> accounts;

    public Bank() {
        accounts = new ArrayList<>();
    }

    public void addAccount(Account account) {
        accounts.add(account);
    }

    public void addInterest() {
        for(Account account : accounts) {
            if(account instanceof InterestBearingAccount) {
                ((InterestBearingAccount) account).addInterest();
            }
        }
    }

    public double totalAssets() {
        double sum = 0;
        for (Account account : accounts) {
            sum+=account.getCurrentAmount();
        }
        return sum;
    }
}

abstract class Account {
    private String accountOwner;
    private int accountId;
    private double currentAmount;
    private static int idSeed = 1000;

    public Account(String accountOwner, double currentAmount) {
        this.accountOwner = accountOwner;
        idSeed++;
        this.accountId = idSeed;
        this.currentAmount = currentAmount;
    }

    public double getCurrentAmount() {
        return currentAmount;
    }

    public String getAccountOwner() {
        return accountOwner;
    }

    public int getAccountId() {
        return accountId;
    }

    public void addAmount(double amount) {
        currentAmount += amount;
    }

    public void withdrawAmount(double amount) throws CannotWithdrawAmountException {
        if(currentAmount < amount) {
            throw new CannotWithdrawAmountException(currentAmount,amount);
        }
        currentAmount -= amount;
    }

    @Override
    public String toString() {
        return String.format("ID: %d, Current amount: %.2f", accountId, currentAmount);
    }
}

class InterestCheckingAccount extends Account implements InterestBearingAccount{

    private static final double INTEREST = 0.3;

    public InterestCheckingAccount(String accountOwner, double currentAmount) {
        super(accountOwner, currentAmount);
    }

    @Override
    public void addInterest() {
        addAmount(getCurrentAmount() * INTEREST);
    }
}

class PlatinumCheckingAccount extends InterestCheckingAccount implements InterestBearingAccount {

    private static final double INTEREST = 0.3;

    public PlatinumCheckingAccount(String accountOwner, double currentAmount) {
        super(accountOwner, currentAmount);
    }

    @Override
    public void addInterest() {
        addAmount((getCurrentAmount() * INTEREST) * 2);
    }
}

class NonInterestCheckingAccount extends Account {

    public NonInterestCheckingAccount(String accountOwner, double currentAmount) {
        super(accountOwner, currentAmount);
    }
}

public class BankSystem {
    public static void main(String[] args) {
        Bank bank = new Bank();

        Account account1 = new InterestCheckingAccount("Alice", 1000.0);
        Account account2 = new PlatinumCheckingAccount("Bob", 2000.0);
        Account account3 = new NonInterestCheckingAccount("Charlie", 500.0);

        bank.addAccount(account1);
        bank.addAccount(account2);
        bank.addAccount(account3);

        System.out.println("Total assets: " + bank.totalAssets());

        System.out.println("Account details before interest:");
        System.out.println(account1);
        System.out.println(account2);
        System.out.println(account3);

        bank.addInterest();

        System.out.println("Total assets after adding interest: " + bank.totalAssets());

        System.out.println("Account details after interest:");
        System.out.println(account1);
        System.out.println(account2);
        System.out.println(account3);

        try {
            account1.withdrawAmount(500.0);
            System.out.println("Alice's balance after withdrawal: " + account1.getCurrentAmount());
        } catch (CannotWithdrawAmountException e) {
            System.out.println(e.getMessage());
        }

        try {
            account2.withdrawAmount(2500.0);
        } catch (CannotWithdrawAmountException e) {
            System.out.println(e.getMessage());
        }

        System.out.println("Final total assets: " + bank.totalAssets());

        System.out.println("Account details after withdrawal:");
        System.out.println(account1);
        System.out.println(account2);
        System.out.println(account3);
    }
}

