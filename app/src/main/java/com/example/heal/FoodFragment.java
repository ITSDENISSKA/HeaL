package com.example.heal;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FoodFragment extends Fragment {
    private ArrayList<FoodItem> foodList;
    private FoodListAdapter foodListAdapter;
    private SharedPreferences sharedPreferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = requireContext().getSharedPreferences("FoodPreferences", Context.MODE_PRIVATE);
        foodList = loadFoodListFromSharedPreferences();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_food, container, false);
        ListView listViewFood = view.findViewById(R.id.listViewFood);
        foodListAdapter = new FoodListAdapter(requireContext(), foodList);
        listViewFood.setAdapter(foodListAdapter);


        listViewFood.setOnItemClickListener((adapterView, view1, position, id) -> {
            foodList.remove(position);
            foodListAdapter.notifyDataSetChanged();
            saveFoodListToSharedPreferences();
        });

        EditText editTextFoodName = view.findViewById(R.id.editTextFoodName);
        EditText editTextCalories = view.findViewById(R.id.editTextCalories);
        Button addButton = view.findViewById(R.id.buttonAdd);

        addButton.setOnClickListener(v -> {
            String foodName = editTextFoodName.getText().toString().trim();
            String calories = editTextCalories.getText().toString().trim();
            if (TextUtils.isEmpty(foodName) || TextUtils.isEmpty(calories)) {
                Toast.makeText(requireContext(), "Введите продукт и калорийность", Toast.LENGTH_SHORT).show();
                return;
            }

            FoodItem foodItem = new FoodItem(foodName, calories);
            foodList.add(foodItem);
            editTextFoodName.setText("");
            editTextCalories.setText("");
            foodListAdapter.notifyDataSetChanged();
            saveFoodListToSharedPreferences();
        });

        return view;
    }

    private void saveFoodListToSharedPreferences() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        JSONArray jsonArray = new JSONArray();
        for (FoodItem foodItem : foodList) {
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("name", foodItem.getName());
                jsonObject.put("calories", foodItem.getCalories());
                jsonArray.put(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        editor.putString("foodList", jsonArray.toString());
        editor.apply();
    }

    private ArrayList<FoodItem> loadFoodListFromSharedPreferences() {
        String json = sharedPreferences.getString("foodList", "");
        ArrayList<FoodItem> foodList = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String name = jsonObject.getString("name");
                String calories = jsonObject.getString("calories");
                foodList.add(new FoodItem(name, calories));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return foodList;
    }
}