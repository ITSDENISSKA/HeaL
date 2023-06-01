package com.example.heal;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

public class FoodFragment extends Fragment {

    private List<String> foodList;
    private ArrayAdapter<String> adapter;

    private EditText editTextFoodName;
    private EditText editTextCalories;
    private Button addButton;
    private ListView listViewFood;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_food, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        editTextFoodName = view.findViewById(R.id.editTextFoodName);
        editTextCalories = view.findViewById(R.id.editTextCalories);
        addButton = view.findViewById(R.id.buttonAdd);
        listViewFood = view.findViewById(R.id.listViewFood);

        foodList = new ArrayList<>();
        adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, foodList);
        listViewFood.setAdapter(adapter);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String foodName = editTextFoodName.getText().toString();
                String calories = editTextCalories.getText().toString();
                String foodEntry = foodName + " - " + calories + " calories";
                foodList.add(foodEntry);
                adapter.notifyDataSetChanged();

                editTextFoodName.setText("");
                editTextCalories.setText("");
            }
        });

        listViewFood.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String foodEntry = foodList.get(position);
                foodList.remove(position);
                adapter.notifyDataSetChanged();
            }
        });
    }
}