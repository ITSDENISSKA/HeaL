package com.example.heal;

import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private PieChartView pieChartView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PieChart pieChart = findViewById(R.id.pie_chart);

        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(75f, "Потрачено"));
        entries.add(new PieEntry(25f, "Осталось"));

        PieDataSet dataSet = new PieDataSet(entries, "Label");
        dataSet.setSliceSpace(0f);
        dataSet.setSelectionShift(0f);

        int[] color = {Color.rgb(170, 51, 78), Color.rgb(233, 157, 174)
        };

        dataSet.setColors(color);

        PieData data = new PieData(dataSet);

        pieChart.setData(data);
        pieChart.setUsePercentValues(true);
        pieChart.setHoleRadius(75f);
        pieChart.setTransparentCircleRadius(100f);
        pieChart.getDescription().setEnabled(false);
        pieChart.setDrawEntryLabels(false);
        pieChart.getLegend().setEnabled(false);
        pieChart.setEntryLabelTextSize(20f);
        pieChart.setDrawCenterText(true);
        pieChart.setCenterText("1020");
        pieChart.setCenterTextSize(50f);
        pieChart.animateY(1500, Easing.EaseInOutQuad);


    }
}