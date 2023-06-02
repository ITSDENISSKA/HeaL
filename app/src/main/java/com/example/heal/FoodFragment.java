package com.example.heal;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.ArrayList;

public class FoodFragment extends Fragment {
    private ArrayList<FoodItem> foodList;
    private FoodListAdapter foodListAdapter;
    private SharedPreferences sharedPreferences;
    FirebaseAuth auth;
    FirebaseUser user;
    String data;
    String[] oldData;
    ListView listViewFood;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = requireContext().getSharedPreferences("FoodPreferences", Context.MODE_PRIVATE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_food, container, false);
        listViewFood = view.findViewById(R.id.listViewFood);
        foodListAdapter = new FoodListAdapter(requireContext(), foodList);
        listViewFood.setAdapter(foodListAdapter);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        listViewFood.setOnItemClickListener((adapterView, view1, position, id) -> {
            Log.e(TAG, String.valueOf(foodList));
            if (user == null) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
            } else {
                oldData = user.getDisplayName().split(";");
                data = oldData[0] + ";" + oldData[1] + ";" + oldData[2] + ";" +
                        (Float.parseFloat(oldData[3]) - Float.parseFloat(String
                                .valueOf(foodList.get(position).getCalories())));

                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                        .setDisplayName(data)
                        .build();

                user.updateProfile(profileUpdates)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Log.d(TAG, "User profile updated.");
                                }
                            }
                        });
                foodList.remove(position);
                foodListAdapter.notifyDataSetChanged();
            }
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


            if (user == null) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
            } else {
                oldData = user.getDisplayName().split(";");
                data = oldData[0] + ";" + oldData[1] + ";" + oldData[2] + ";" +
                        (Float.parseFloat(oldData[3]) + Float.parseFloat(String
                                .valueOf(editTextCalories.getText())));

                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                        .setDisplayName(data)
                        .build();

                user.updateProfile(profileUpdates)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Log.d(TAG, "User profile updated.");
                                }
                            }
                        });

            }
            editTextFoodName.setText("");
            editTextCalories.setText("");
            foodListAdapter.notifyDataSetChanged();
        });

        return view;
    }
}