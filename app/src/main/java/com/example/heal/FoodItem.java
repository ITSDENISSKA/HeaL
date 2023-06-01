package com.example.heal;

public class FoodItem {
    private String name;
    private String calories;

    public FoodItem(String name, String calories) {
        this.name = name;
        this.calories = calories;
    }

    public String getName() {
        return name;
    }

    public String getCalories() {
        return calories;
    }
}