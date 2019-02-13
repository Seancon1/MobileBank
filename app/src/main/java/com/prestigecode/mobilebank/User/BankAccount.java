package com.prestigecode.mobilebank.User;

import android.os.Parcel;
import android.os.Parcelable;

public class BankAccount implements Parcelable {

    int type;
    double balance;
    String cardNum;
    String routingNum;
    String wireNum;
    int CVV;


    public BankAccount(int type, double balance, String cardNumber, String routingNum, String wireNum, int CVV) {
        this.type = type;
        this.balance = balance;
        this.cardNum = cardNumber;
        this.routingNum = routingNum;
        this.wireNum = wireNum;
        this.CVV = CVV;
    }

    protected BankAccount(Parcel in) {
        type = in.readInt();
        balance = in.readDouble();
        cardNum = in.readString();
        routingNum = in.readString();
        wireNum = in.readString();
        CVV = in.readInt();
    }

    public static final Creator<BankAccount> CREATOR = new Creator<BankAccount>() {
        @Override
        public BankAccount createFromParcel(Parcel in) {
            return new BankAccount(in);
        }

        @Override
        public BankAccount[] newArray(int size) {
            return new BankAccount[size];
        }
    };

    public int getType() {
        return type;
    }
    public double getBalance() {
        return balance;
    }
    public String getCardNum() {
        return cardNum;
    }
    public String getRoutingNum() {
        return routingNum;
    }
    public String getWireNum() {
        return wireNum;
    }
    public int getCVV() {
        return CVV;
    }

    public String getLastFourCardNum() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(getCardNum());
        if(stringBuilder.length() < 16) {
           return "0";
        } else {
            return stringBuilder.substring(12, stringBuilder.length()); //cardNum will be max 16 chars, we want last 4, starting at index of 12
        }
    }

    public String showAccountType(int number) {
        if(number > -1) {
            switch (number) {
                case 0:
                    return "Checking";
                case 1:
                    return "Savings";
                case 2:
                    return "Credit";
            }
        }
        return "";
    }

    @Override
    public String toString() {
        return showAccountType(getType()) + "(" + getLastFourCardNum() + ") : " + this.getBalance();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(type);
        dest.writeDouble(balance);
        dest.writeString(cardNum);
        dest.writeString(routingNum);
        dest.writeString(wireNum);
        dest.writeInt(CVV);
    }
}
