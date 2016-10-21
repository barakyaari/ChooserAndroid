package net.cloudapp.chooser.chooser.Common;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

import net.cloudapp.chooser.chooser.R;

import java.util.ArrayList;


/**
 * Created by Ben on 19/10/2016.
 */
public abstract class StatisticsChartSetup {

    public static void createAgePieChart(PieChart pieChart, Context context) {
        pieChart.setUsePercentValues(true);
        pieChart.setDescription("");
        pieChart.setDragDecelerationFrictionCoef(0.95f);
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(ContextCompat.getColor(context,R.color.appBackground));
        pieChart.setTransparentCircleColor(Color.rgb(50,64,93));
        pieChart.setCenterTextColor(Color.LTGRAY);
        pieChart.setTransparentCircleAlpha(110);
        pieChart.setHoleRadius(45f);
        pieChart.setTransparentCircleRadius(50f);
        pieChart.setDrawCenterText(true);
        pieChart.setCenterText("Age\nDistribution");
        pieChart.getLegend().setEnabled(false);
        pieChart.setRotationAngle(0);
        pieChart.setRotationEnabled(true);
        pieChart.setHighlightPerTapEnabled(true);
        pieChart.setClickable(false);
        pieChart.animateY(650, Easing.EasingOption.EaseInOutQuad);
        pieChart.setEntryLabelColor(Color.BLACK);
        pieChart.setEntryLabelTextSize(12f);
    }

    public static void createBarChart(HorizontalBarChart horizontalBarChart) {
        horizontalBarChart.getXAxis().setEnabled(false);
        horizontalBarChart.getAxisLeft().setEnabled(false);
        horizontalBarChart.getAxisLeft().setAxisMinValue(0f);
        horizontalBarChart.getAxisRight().setEnabled(false);
        horizontalBarChart.setDescription("");
        horizontalBarChart.setClickable(false);
        horizontalBarChart.setTouchEnabled(false);
        horizontalBarChart.setDoubleTapToZoomEnabled(false);
        horizontalBarChart.setPinchZoom(false);
        horizontalBarChart.animateY(650);
        horizontalBarChart.getLegend().setEnabled(false);
    }

    public static void createSmallBarChart (HorizontalBarChart horizontalBarChart) {
        horizontalBarChart.getXAxis().setEnabled(false);
        horizontalBarChart.getAxisLeft().setEnabled(false);
        horizontalBarChart.getAxisLeft().setAxisMinValue(0f);
        horizontalBarChart.getAxisRight().setEnabled(false);
        horizontalBarChart.setDescription("");
        horizontalBarChart.setClickable(false);
        horizontalBarChart.setTouchEnabled(false);
        horizontalBarChart.setDoubleTapToZoomEnabled(false);
        horizontalBarChart.setPinchZoom(false);
    }

    public static void updateSmallBarChart (HorizontalBarChart horizontalBarChart, int vote1, int vote2, Context context) {
        StatisticsChartSetup.createSmallBarChart(horizontalBarChart);

        ArrayList<BarEntry> vals = new ArrayList<>();
        vals.add(new BarEntry(0, new float[]{vote1, vote2}));

        BarDataSet set = new BarDataSet(vals, "");
        set.setBarBorderWidth(2);
        set.setBarBorderColor(Color.BLACK);
        ArrayList<IBarDataSet> dataSet = new ArrayList<>();

        set.setColors(new int[]{ContextCompat.getColor(context,R.color.bar1), ContextCompat.getColor(context,R.color.bar2)});
        dataSet.add(set);

        BarData data = new BarData(dataSet);
        data.setDrawValues(false);

        horizontalBarChart.setData(data);
        horizontalBarChart.invalidate();
    }
}
