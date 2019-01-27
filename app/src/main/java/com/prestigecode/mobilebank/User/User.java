package com.prestigecode.mobilebank.User;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * User class, this class will be instantiated and hold the data
 * for the user during app runtime.
 */
public class User implements Parcelable {


    /**
     * Values of User
     */
    int ID = 0;
    String name = "";
    String authString;


    User() {

    }

    //Create user
    public User(int id, String name, String authString) {
        this.ID = id;
        this.name = name;
        this.authString = authString;
    }

    protected User(Parcel in) {
        ID = in.readInt();
        name = in.readString();
        authString = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public int getID() { return this.ID; }
    public String getName() { return this.name; }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(ID);
        dest.writeString(name);
        dest.writeString(authString);
    }
}
