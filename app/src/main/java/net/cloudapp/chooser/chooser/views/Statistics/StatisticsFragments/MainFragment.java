package net.cloudapp.chooser.chooser.views.Statistics.StatisticsFragments;

import android.graphics.Color;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ViewPortHandler;

import net.cloudapp.chooser.chooser.Common.PostRepository;
import net.cloudapp.chooser.chooser.Common.StatisticsChartSetup;
import net.cloudapp.chooser.chooser.R;
import net.cloudapp.chooser.chooser.model.Post;

import java.util.ArrayList;

/**
 * Created by Ben on 18/10/2016.
 */
public class MainFragment {
    private View view;
    private Post post;
    private HorizontalBarChart generalBarChart;
    private TextView totalVotes,postDate,promotionStatus;

    public MainFragment (View view, int postIndex) {
        this.view = view;
        post = PostRepository.myPosts.get(postIndex);
        createGeneralFragment();
    }

    private void createGeneralFragment() {
        generalBarChart = (HorizontalBarChart) view.findViewById(R.id.chart);
        StatisticsChartSetup.createBarChart(generalBarChart);
        generalBarChart.setFitBars(true);
        totalVotes = (TextView) view.findViewById(R.id.totalVotes);
        postDate = (TextView) view.findViewById(R.id.postDate);
        promotionStatus = (TextView) view.findViewById(R.id.promotion);
        promotionStatus.setVisibility(View.INVISIBLE);
    }

/*
    public void updateGeneralBarData () {
        ArrayList<BarEntry> vals = new ArrayList<>();
        vals.add(new BarEntry(1f, post.votes1));
        vals.add(new BarEntry(2.2f, post.votes2));


        BarDataSet set = new BarDataSet(vals, "General Info");
        ArrayList<IBarDataSet> dataSet = new ArrayList<>();
        set.setColors(new int[]{StatisticsChartSetup.ChartColors.MAIN1.val, StatisticsChartSetup.ChartColors.MAIN2.val});
        set.setValueTextColor(Color.LTGRAY);
        dataSet.add(set);

        BarData data = new BarData(dataSet);
        data.setValueTextSize(15f);
        data.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                return String.valueOf(Math.round(value));
            }
        });
        generalBarChart.setData(data);
        generalBarChart.notifyDataSetChanged();
        generalBarChart.invalidate();

        totalVotes.setText("Total Votes: " + (post.votes1 + post.votes2));
        postDate.setText("Posted: " + postedTime(post.date, sessionDetails.currentServerTime) + " ago");

        if (post.promotionExpiration.compareTo(sessionDetails.currentServerTime) > 0)
            promotionStatus.setText("Promotion Time Left: " + postedTime(sessionDetails.currentServerTime, post.promotionExpiration));
        else
            promotionStatus.setText("Promotion Expired!");

        if (post.date != post.promotionExpiration)
            promotionStatus.setVisibility(View.VISIBLE);
    }

    */
}
