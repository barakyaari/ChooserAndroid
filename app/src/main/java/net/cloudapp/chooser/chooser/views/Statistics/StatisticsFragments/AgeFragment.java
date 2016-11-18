package net.cloudapp.chooser.chooser.views.Statistics.StatisticsFragments;

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


class AgeFragment {
    private View kidStatItem, teenStatItem, youngStatItem, manStatItem, oldStatItem;
    private PostStatistics postStatistics;
    private final int kid_start = 0;
    private final int teen_start = 14;
    private final int young_start = 21;
    private final int man_start = 31;
    private final int old_start = 61;


     AgeFragment (View view) {
        kidStatItem = view.findViewById(R.id.kidStatisticsItem);
        teenStatItem = view.findViewById(R.id.teenStatisticsItem);
        youngStatItem = view.findViewById(R.id.youngManStatisticsItem);
        manStatItem = view.findViewById(R.id.manStatisticsItem);
        oldStatItem = view.findViewById(R.id.oldStatisticsItem);
        initializeAgeItem(kidStatItem, "Kids "+ kid_start + "-" + (teen_start-1) +" Votes", R.drawable.ic_age_teen);
        initializeAgeItem(teenStatItem, "Teens "+ teen_start + "-" + (young_start-1) +" Votes", R.drawable.ic_age_teen);
        initializeAgeItem(youngStatItem, "Young Men "+ young_start + "-" + (man_start-1) +" Votes", R.drawable.ic_age_young);
        initializeAgeItem(manStatItem, "Men "+ man_start + "-" + (old_start-1) +" Votes", R.drawable.ic_age_man);
        initializeAgeItem(oldStatItem, "Elderly "+ old_start +"+ Votes",R.drawable.ic_age_old);
    }

    private void initializeAgeItem(View view, String headline, int drawable) {
        StatisticsChartSetup.createBarChart((HorizontalBarChart) view.findViewById(R.id.barChart));
        ((TextView) view.findViewById(R.id.headline)).setText(headline);
        ImageView icon = (ImageView) view.findViewById(R.id.statIcon);
        icon.setImageResource(drawable);
    }



    void refreshAgeFragment(PostStatistics postStatistics) {
        this.postStatistics = postStatistics;
        updateAgeBarData(kid_start, teen_start-1, kidStatItem);
        updateAgeBarData(teen_start, young_start-1, teenStatItem);
        updateAgeBarData(young_start, man_start-1, youngStatItem);
        updateAgeBarData(man_start, old_start-1, manStatItem);
        updateAgeBarData(old_start,200, oldStatItem);
    }


    private void updateAgeBarData(int ageStart, int ageEnd, View view) {
        int data1 = getAgeRangeVotes(1, ageStart, ageEnd);
        int data2 = getAgeRangeVotes(2, ageStart, ageEnd);
        if (data1+data2 != 0)
            view.setVisibility(View.VISIBLE);
        StatisticsChartSetup.updateBarData(view, data1, data2);
    }


    private int getAgeRangeVotes(int voteNum, int fromAge, int toAge) {
        //fromAge inclusive, toAge inclusive
        int count = 0;
        ArrayList<Integer> arrayCount;
        if (voteNum == 1)
            arrayCount = postStatistics.ageVotes1;
        else if (voteNum == 2)
            arrayCount = postStatistics.ageVotes2;
        else return 0;

        int arraySize = arrayCount.size();
        if (fromAge > arraySize)
            return 0;

        for (int i = fromAge; i < Math.min(toAge+1,arraySize); i++)
            count += arrayCount.get(i);
        return count;
    }
}
