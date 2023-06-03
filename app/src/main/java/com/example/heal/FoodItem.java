package com.example.heal;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class FoodItem implements Parcelable {
    private String name;
    private String calories;

    public FoodItem(String name, String calories) {
        this.name = name;
        this.calories = calories;
    }

    protected FoodItem(Parcel in) {
        name = in.readString();
        calories = in.readString();
    }

    public static final Creator<FoodItem> CREATOR = new Creator<FoodItem>() {
        @Override
        public FoodItem createFromParcel(Parcel in) {
            return new FoodItem(in);
        }

        @Override
        public FoodItem[] newArray(int size) {
            return new FoodItem[size];
        }
    };

    public String getName() {
        return name;
    }

    public String getCalories() {
        return calories;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(calories);
    }
}