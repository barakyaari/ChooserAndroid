package net.cloudapp.chooser.chooser.views.Statistics.StatisticsFragments;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

import net.cloudapp.chooser.chooser.Common.StatisticsChartSetup;
import net.cloudapp.chooser.chooser.R;
import net.cloudapp.chooser.chooser.model.PostStatistics;

import java.util.ArrayList;

public class GenderFragment {
    private View view;
    private HorizontalBarChart femaleBarChart, maleBarChart;
    private TextView genF1, genF2, genM1, genM2;

    public GenderFragment (View view) {
        this.view = view;
        createGenderFragment();
    }

    private void createGenderFragment() {
        View femaleStat = view.findViewById(R.id.femaleStatisticsItem);
        View maleStat = view.findViewById(R.id.maleStatisticsItem);
        femaleBarChart = (HorizontalBarChart) femaleStat.findViewById(R.id.barChart);
        maleBarChart = (HorizontalBarChart) maleStat.findViewById(R.id.barChart);
        StatisticsChartSetup.createBarChart(femaleBarChart);
        StatisticsChartSetup.createBarChart(maleBarChart);
        genF1 = (TextView) femaleStat.findViewById(R.id.vote1);
        genF2 = (TextView) femaleStat.findViewById(R.id.vote2);
        genM1 = (TextView) maleStat.findViewById(R.id.vote1);
        genM2 = (TextView) maleStat.findViewById(R.id.vote2);
        ((ImageView) femaleStat.findViewById(R.id.statIcon)).setImageResource(R.drawable.ic_female);
        ((ImageView) maleStat.findViewById(R.id.statIcon)).setImageResource(R.drawable.ic_male);
        TextView femaleHeadline = (TextView) femaleStat.findViewById(R.id.headline);
        TextView maleHeadline = (TextView) maleStat.findViewById(R.id.headline);

        femaleHeadline.setTextColor(ContextCompat.getColor(view.getContext(),R.color.genderBarFemale));
        femaleHeadline.setText("Female Votes");
        maleHeadline.setTextColor(ContextCompat.getColor(view.getContext(),R.color.genderBarMale));
        maleHeadline.setText("Male Votes");

    }



    public void refreshGenderFragment(PostStatistics postStatistics) {
        updateGenderBarData(femaleBarChart, postStatistics.femaleVotes1, postStatistics.femaleVotes2,genF1,genF2);
        updateGenderBarData(maleBarChart, postStatistics.maleVotes1, postStatistics.maleVotes2,genM1,genM2);
    }


    private void updateGenderBarData (HorizontalBarChart horizontalBarChart, float data1, float data2, TextView dataText1, TextView dataText2) {

        int color1 = ContextCompat.getColor(view.getContext(),R.color.bar1);
        int color2 = ContextCompat.getColor(view.getContext(),R.color.bar2);
        dataText1.setTextColor(color1);
        dataText2.setTextColor(color2);

        ArrayList<BarEntry> vals = new ArrayList<>();
        vals.add(new BarEntry(0, new float[]{data1, data2}));

        BarDataSet set = new BarDataSet(vals, "");
        ArrayList<IBarDataSet> dataSet = new ArrayList<>();
        set.setColors(new int[]{color1, color2});
        set.setBarBorderWidth(2);
        set.setBarBorderColor(Color.BLACK);
        dataSet.add(set);

        BarData data = new BarData(dataSet);
        data.setDrawValues(false);

        horizontalBarChart.setData(data);
        horizontalBarChart.notifyDataSetChanged();
        horizontalBarChart.invalidate();

        dataText1.setText(String.valueOf((int)data1));
        dataText2.setText(String.valueOf((int)data2));
    }

}
