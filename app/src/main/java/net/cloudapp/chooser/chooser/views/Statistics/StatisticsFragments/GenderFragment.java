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
    private View view, femaleStat, maleStat;

    public GenderFragment (View view) {
        this.view = view;
        createGenderFragment();
    }

    private void createGenderFragment() {
        femaleStat = view.findViewById(R.id.femaleStatisticsItem);
        maleStat = view.findViewById(R.id.maleStatisticsItem);
        initializeGenderItem(femaleStat,"Female Votes", R.color.genderBarFemale,R.drawable.ic_female);
        initializeGenderItem(maleStat,"Male Votes", R.color.genderBarMale,R.drawable.ic_male);
    }

    private void initializeGenderItem(View view, String headline, int headlineColor, int drawable) {
        StatisticsChartSetup.createBarChart((HorizontalBarChart) view.findViewById(R.id.barChart));
        TextView viewHeadline = (TextView) view.findViewById(R.id.headline);
        viewHeadline.setText(headline);
        viewHeadline.setTextColor(ContextCompat.getColor(view.getContext(),headlineColor));
        ImageView icon = (ImageView) view.findViewById(R.id.statIcon);
        icon.setImageResource(drawable);
    }


    void refreshGenderFragment(PostStatistics postStatistics) {
        StatisticsChartSetup.updateBarData(femaleStat, postStatistics.femaleVotes1, postStatistics.femaleVotes2);
        StatisticsChartSetup.updateBarData(maleStat, postStatistics.maleVotes1, postStatistics.maleVotes2);
    }

}
