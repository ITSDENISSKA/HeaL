package com.example.heal;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;


public class MainFragment extends Fragment {

    private PieChart pieChart;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);


        PieChart mobilityPieChart = view.findViewById(R.id.mobility_pie_chart);
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
        mobilityPieChart.setCenterTextSize(50f);
        mobilityPieChart.animateY(1500, Easing.EaseInOutQuad);

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