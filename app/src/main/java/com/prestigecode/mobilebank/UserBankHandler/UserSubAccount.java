package com.prestigecode.mobilebank.UserBankHandler;

public class UserSubAccount {

    public int id;
    public String currency;
    private float amount;

    UserSubAccount() {

    }

    UserSubAccount(int id, String currency, float amount) {
        this.id =id;
        this.currency = currency;
        this.amount = amount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public void adjustAmount(float amount) {
        this.amount += amount;
    }
}
