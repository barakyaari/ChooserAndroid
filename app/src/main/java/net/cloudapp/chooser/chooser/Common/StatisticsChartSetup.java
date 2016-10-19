package net.cloudapp.chooser.chooser.Common;

import android.graphics.Color;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.charts.PieChart;

/**
 * Created by Ben on 19/10/2016.
 */
public abstract class StatisticsChartSetup {

    public enum ChartColors {
        FRAME(Color.rgb(30,30,30)),
        MAIN1(Color.BLACK),
        MAIN2(Color.LTGRAY),
        GENDER_DIST1(Color.rgb(224,119,203)),
        GENDER_DIST2(Color.rgb(119,153,224)),
        GENDER_FVOTES1(Color.rgb(74,0,59)),
        GENDER_FVOTES2(Color.rgb(227,191,220)),
        GENDER_MVOTES1(Color.rgb(0,23,74)),
        GENDER_MVOTES2(Color.rgb(173,190,224));

        public final int val;
        private ChartColors(int val) {
            this.val = val;
        }
    }

    public static void createAgePieChart(PieChart pieChart) {
        pieChart.setUsePercentValues(true);
        pieChart.setDescription("");
        pieChart.setDragDecelerationFrictionCoef(0.95f);
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(Color.rgb(50,64,93));
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
        horizontalBarChart.setDoubleTapToZoomEnabled(false);
        horizontalBarChart.setPinchZoom(false);
    }
}
