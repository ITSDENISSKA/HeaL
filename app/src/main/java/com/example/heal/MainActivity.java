package com.example.heal;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.data.Bucket;
import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.fitness.request.DataReadRequest;
import com.google.android.gms.fitness.result.DataReadResponse;
import com.google.android.gms.fitness.result.DataReadResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private PieChartView pieChartView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PieChart mobilityPieChart = findViewById(R.id.mobility_pie_chart);
        mobilityPieChart.setDragDecelerationFrictionCoef(1f);

        ArrayList<PieEntry> mobilityEntries = new ArrayList<>();
        mobilityEntries.add(new PieEntry(75f, "Потрачено"));
        mobilityEntries.add(new PieEntry(25f, "Осталось"));

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
//        mobilityPieChart.setCenterText("1020");
        mobilityPieChart.setCenterTextSize(50f);
        mobilityPieChart.animateY(1500, Easing.EaseInOutQuad);

        PieChart receivePieChart = findViewById(R.id.receive_pie_chart);
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
//        receivePieChart.setCenterText("1020");
        receivePieChart.setCenterTextSize(50f);
        receivePieChart.animateY(1500, Easing.EaseInOutQuad);

        MainActivity main_fragment = new MainActivity();
        FoodActivity food_fragment = new FoodActivity();
        AccountActivity account_fragment = new AccountActivity();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav_view);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.nav_food:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            food_fragment).commit();
                    return true;
                case R.id.nav_home:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            main_fragment).commit();
                    return true;
                case R.id.nav_account:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            account_fragment).commit();
                    return true;
                default:
                    return false;
            }
        });
        // Устанавливаем интервал времени для запроса данных за день
        Calendar cal = Calendar.getInstance();
        Date now = new Date();
        cal.setTime(now);
        long endTime = cal.getTimeInMillis();
        cal.add(Calendar.DAY_OF_YEAR, -1);
        long startTime = cal.getTimeInMillis();

        // Создаем запрос на чтение данных
        DataReadRequest readRequest = new DataReadRequest.Builder().aggregate(DataType.TYPE_ACTIVITY_SEGMENT, DataType.AGGREGATE_ACTIVITY_SUMMARY).bucketByTime(1, TimeUnit.DAYS).setTimeRange(startTime, endTime, TimeUnit.MILLISECONDS).build();
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (account != null) {
            Fitness.getHistoryClient(this, account).readData(readRequest).addOnCompleteListener(new OnCompleteListener<DataReadResponse>() {
                @Override
                public void onComplete(@NonNull Task<DataReadResponse> task) {
                    if (task.isSuccessful()) {
                        DataReadResponse result = task.getResult();
                        if (result != null && result.getBuckets().size() > 0) {
                            // Обработка полученных данных
                            for (Bucket bucket : result.getBuckets()) {
                                List<DataSet> dataSets = bucket.getDataSets();
                                for (DataSet dataSet : dataSets) {
                                    for (DataPoint dp : dataSet.getDataPoints()) {
                                        // Получение данных о подвижности (название, время начала, время окончания)
                                        String activityName = dp.getValue(Field.FIELD_ACTIVITY).asActivity();
                                        long startTime = dp.getStartTime(TimeUnit.MILLISECONDS);
                                        long endTime = dp.getEndTime(TimeUnit.MILLISECONDS);
                                        Log.d(TAG, "Activity: " + activityName + ", Start: " + startTime + ", End: " + endTime);
                                    }
                                }
                            }
                        }
                    } else {
                        Log.e(TAG, "Failed to read data.");
                    }
                }
            });
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, main_fragment).commit();
    }
}
