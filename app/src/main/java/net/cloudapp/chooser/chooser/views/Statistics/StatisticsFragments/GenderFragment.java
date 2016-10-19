package net.cloudapp.chooser.chooser.views.Statistics.StatisticsFragments;

import android.view.View;
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

/**
 * Created by Ben on 18/10/2016.
 */
public class GenderFragment {
    private View view;
    private PostStatistics postStatistics;
    private HorizontalBarChart genderBarChart, femaleBarChart, maleBarChart;
    private TextView  genDist1, genDist2, genF1, genF2, genM1, genM2;

    public GenderFragment (View view) {
        this.view = view;
        createGenderFragment();
    }

    private void createGenderFragment() {
        genderBarChart = (HorizontalBarChart) view.findViewById(R.id.percentBar);
        femaleBarChart = (HorizontalBarChart) view.findViewById(R.id.femaleBar);
        maleBarChart = (HorizontalBarChart) view.findViewById(R.id.maleBar);
        StatisticsChartSetup.createBarChart(genderBarChart);
        StatisticsChartSetup.createBarChart(femaleBarChart);
        StatisticsChartSetup.createBarChart(maleBarChart);
        genDist1 = (TextView) view.findViewById(R.id.dist_data1);
        genDist2 = (TextView) view.findViewById(R.id.dist_data2);
        genF1 = (TextView) view.findViewById(R.id.female_data1);
        genF2 = (TextView) view.findViewById(R.id.female_data2);
        genM1 = (TextView) view.findViewById(R.id.male_data1);
        genM2 = (TextView) view.findViewById(R.id.male_data2);
    }


    public void refreshGenderFragment(PostStatistics postStatistics) {
        this.postStatistics = postStatistics;
        updateGenderBarData(genderBarChart, StatisticsChartSetup.ChartColors.GENDER_DIST1.val, StatisticsChartSetup.ChartColors.GENDER_DIST2.val, getTotalFemaleVotes(),getTotalMaleVotes(),genDist1,genDist2);
        updateGenderBarData(femaleBarChart, StatisticsChartSetup.ChartColors.GENDER_FVOTES1.val, StatisticsChartSetup.ChartColors.GENDER_FVOTES2.val, postStatistics.femaleVotes1,postStatistics.femaleVotes2,genF1,genF2);
        updateGenderBarData(maleBarChart, StatisticsChartSetup.ChartColors.GENDER_MVOTES1.val, StatisticsChartSetup.ChartColors.GENDER_MVOTES2.val, postStatistics.maleVotes1,postStatistics.maleVotes2,genM1,genM2);
    }


    private void updateGenderBarData (HorizontalBarChart horizontalBarChart, int color1, int color2, float data1, float data2, TextView dataText1, TextView dataText2) {
        ArrayList<BarEntry> vals = new ArrayList<>();
        vals.add(new BarEntry(0, new float[]{data1, data2}));


        BarDataSet set = new BarDataSet(vals, "");
        ArrayList<IBarDataSet> dataSet = new ArrayList<>();
        set.setColors(new int[]{color1, color2});
        set.setBarBorderWidth(2);
        set.setBarBorderColor(StatisticsChartSetup.ChartColors.FRAME.val);
        dataSet.add(set);

        BarData data = new BarData(dataSet);
        data.setDrawValues(false);

        horizontalBarChart.setData(data);
        horizontalBarChart.notifyDataSetChanged();
        horizontalBarChart.invalidate();

        dataText1.setText(String.valueOf(((int)data1)));
        dataText2.setText(String.valueOf((int)data2));
    }

    private int getTotalFemaleVotes() {
        return postStatistics.femaleVotes1+postStatistics.femaleVotes2;
    }

    private int getTotalMaleVotes() {
        return postStatistics.maleVotes1+postStatistics.maleVotes2;
    }
}
