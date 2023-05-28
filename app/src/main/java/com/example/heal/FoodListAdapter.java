package com.example.heal;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

public class FoodListAdapter extends ArrayAdapter<FoodItem> {
    private List<FoodItem> foodList;

    public FoodListAdapter(Context context, List<FoodItem> foodList) {
        super(context, 0, foodList);
        this.foodList = foodList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_food, parent, false);
        }

        FoodItem foodItem = foodList.get(position);

        TextView textViewFoodName = convertView.findViewById(R.id.textViewFoodName);
        TextView textViewCalories = convertView.findViewById(R.id.textViewCalories);
        ImageView imageViewDelete = convertView.findViewById(R.id.imageViewDelete);

        textViewFoodName.setText(foodItem.getName());
        textViewCalories.setText(String.valueOf(foodItem.getCalories()));

        imageViewDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Удаление элемента из списка добавленной еды
                foodList.remove(position);
                notifyDataSetChanged();
            }
        });

        return convertView;
    }
}