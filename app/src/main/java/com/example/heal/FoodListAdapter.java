package com.example.heal;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FoodListAdapter extends BaseAdapter {

    private List<FoodItem> foodList;
    private Set<Integer> selectedItems;

    public FoodListAdapter(Context context, List<FoodItem> foodList) {
        this.foodList = foodList;
        this.selectedItems = new HashSet<>();
    }

    @Override
    public int getCount() {
        return foodList.size();
    }

    @Override
    public Object getItem(int position) {
        return foodList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_food, parent, false);
        }

        TextView textViewFoodName = convertView.findViewById(R.id.textViewFoodName);
        TextView textViewCalories = convertView.findViewById(R.id.textViewCalories);

        final FoodItem foodItem = foodList.get(position);
        textViewFoodName.setText(foodItem.getName());
        String caloriesText = String.format("%s Ккал", foodItem.getCalories());
        textViewCalories.setText(caloriesText);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteFood(position);
            }
        });

        return convertView;
    }

    public void deleteFood(int position) {
        foodList.remove(position);
        selectedItems.remove(position);
        notifyDataSetChanged();
    }

    private void toggleSelection(int position) {
        if (selectedItems.contains(position)) {
            selectedItems.remove(position);
        } else {
            selectedItems.add(position);
        }
        notifyDataSetChanged();
    }

    public void clearSelection() {
        selectedItems.clear();
        notifyDataSetChanged();
    }

    public Set<Integer> getSelectedItems() {
        return selectedItems;
    }
}