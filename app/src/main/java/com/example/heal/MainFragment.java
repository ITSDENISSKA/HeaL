package com.example.heal;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.FitnessOptions;
import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.fitness.request.DataReadRequest;
import com.google.android.gms.fitness.result.DataReadResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;


public class MainFragment extends Fragment {
    int mobilityDesire = 1000;
    FirebaseAuth auth;

    FirebaseUser user;

    int mobilityCalories = 0;
    int mobilitySpent;
    int mobilityStill;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        auth = FirebaseAuth.getInstance();

        user = auth.getCurrentUser();
        if (user == null) {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(getContext(), LoginActivity.class);
            startActivity(intent);
        } else {
            if (!Objects.requireNonNull(user.getDisplayName()).contains(";")) {
                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                        .setDisplayName("Ваше имя;1000;1000;0")
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
        }

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.HOUR, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        long startTime = cal.getTimeInMillis();

        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.HOUR, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);
        long endTime = cal.getTimeInMillis();


        DataReadRequest request = new DataReadRequest.Builder()
                .read(DataType.TYPE_CALORIES_EXPENDED)
                .setTimeRange(startTime, endTime, TimeUnit.MILLISECONDS)
                .build();

        FitnessOptions fitnessOptions = FitnessOptions.builder()
                .addDataType(DataType.TYPE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
                .addDataType(DataType.AGGREGATE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
                .addDataType(DataType.TYPE_DISTANCE_DELTA, FitnessOptions.ACCESS_READ)
                .build();


        Task<DataReadResponse> responseTask = Fitness.getHistoryClient(requireContext(),
                GoogleSignIn.getAccountForExtension(getActivity(), fitnessOptions))
                .readData(request);

        responseTask.addOnSuccessListener(new OnSuccessListener<DataReadResponse>() {
            @Override
            public void onSuccess(DataReadResponse dataReadResponse) {
                if (dataReadResponse.getDataSets().size() > 0) {
                    for (DataSet dataSet : dataReadResponse.getDataSets()) {
                        List<DataPoint> dataPoints = dataSet.getDataPoints();
                        for (DataPoint dataPoint : dataPoints) {
                            if (String.valueOf(dataPoint).contains("from_user_input_activities_local")) {
                                mobilityCalories += (int) dataPoint.getValue(Field.FIELD_CALORIES).asFloat();
                            }
                        }
                    }
                    ArrayList<PieEntry> mobilityEntries = new ArrayList<>();

                    PieChart mobilityPieChart = view.findViewById(R.id.mobility_pie_chart);
                    mobilityPieChart.setDragDecelerationFrictionCoef(1f);

                    mobilitySpent = mobilityCalories;
                    mobilityStill = (mobilityDesire - mobilityCalories);

                    mobilityEntries.add(new PieEntry((float) mobilitySpent, "Потрачено"));
                    mobilityEntries.add(new PieEntry((float) mobilityStill, "Осталось"));

                    PieDataSet mobilityDataSet = new PieDataSet(mobilityEntries, "Label");
                    mobilityDataSet.setSliceSpace(5f);
                    mobilityDataSet.setSelectionShift(0f);
                    mobilityDataSet.setValueTextSize(12f);
                    mobilityDataSet.setValueTextColor(Color.TRANSPARENT);

                    int[] mobilityColor = {Color.rgb(170, 51, 78), Color.rgb(233, 157, 174)};

                    mobilityDataSet.setColors(mobilityColor);

                    PieData mobilityData = new PieData(mobilityDataSet);

                    mobilityPieChart.setData(mobilityData);
                    mobilityPieChart.setUsePercentValues(false);
                    mobilityPieChart.setHoleRadius(75f);
                    mobilityPieChart.setTransparentCircleRadius(100f);
                    mobilityPieChart.getDescription().setEnabled(false);
                    mobilityPieChart.setDrawEntryLabels(false);
                    mobilityPieChart.getLegend().setEnabled(false);
                    mobilityPieChart.setEntryLabelTextSize(20f);
                    mobilityPieChart.setDrawCenterText(true);
                    mobilityPieChart.setCenterTextSize(50f);
                    mobilityPieChart.animateY(1500, Easing.EaseInOutQuad);
                }
            }
        });


        PieChart receivePieChart = view.findViewById(R.id.receive_pie_chart);
        receivePieChart.setDragDecelerationFrictionCoef(1f);

        ArrayList<PieEntry> receiveEntries = new ArrayList<>();
        receiveEntries.add(new PieEntry(60f, "Приобрёл"));
        receiveEntries.add(new PieEntry(40f, "Осталось"));

        PieDataSet receiveDataSet = new PieDataSet(receiveEntries, "Label");
        receiveDataSet.setSliceSpace(5f);
        receiveDataSet.setSelectionShift(0f);

        receiveDataSet.setValueTextSize(12f);
        receiveDataSet.setValueTextColor(Color.TRANSPARENT);
        int[] receiveColor = {Color.rgb(0, 150, 0), Color.rgb(0, 255, 0)};

        receiveDataSet.setColors(receiveColor);


        PieData receiveData = new PieData(receiveDataSet);

        receivePieChart.setData(receiveData);
        receivePieChart.setUsePercentValues(false);
        receivePieChart.setHoleRadius(65f);
        receivePieChart.setTransparentCircleRadius(100f);
        receivePieChart.getDescription().setEnabled(false);
        receivePieChart.setDrawEntryLabels(false);
        receivePieChart.getLegend().setEnabled(false);
        receivePieChart.setEntryLabelTextSize(20f);
        receivePieChart.setDrawCenterText(true);
        receivePieChart.setCenterTextSize(50f);
        receivePieChart.animateY(1500, Easing.EaseInOutQuad);


        return view;
    }
}