package com.prestigecode.mobilebank.User;

public class BankAccount {

    int type;
    double balance;

    BankAccount(int type, double balance) {
        this.type=type;
        this.balance=balance;
    }

    public int getType() {
        return type;
    }

    public double getBalance() {
        return balance;
    }

}
