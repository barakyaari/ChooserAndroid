package net.cloudapp.chooser.chooser.Common;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

import net.cloudapp.chooser.chooser.Images.CloudinaryClient;
import net.cloudapp.chooser.chooser.R;
import net.cloudapp.chooser.chooser.model.Post;

import java.util.ArrayList;


/**
 * Created by Ben on 19/10/2016.
 */
public abstract class StatisticsChartSetup {


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

    private static void createSmallBarChart (HorizontalBarChart horizontalBarChart) {
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

    private static void updateSmallBarChart (HorizontalBarChart horizontalBarChart, int vote1, int vote2, Context context) {
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

    public static void updateBarData (View view, int data1, int data2) {
        TextView vote1 = (TextView) view.findViewById(R.id.vote1);
        TextView vote2 = (TextView) view.findViewById(R.id.vote2);
        vote1.setText(String.valueOf(data1));
        vote2.setText(String.valueOf(data2));
        HorizontalBarChart horizontalBarChart = (HorizontalBarChart) view.findViewById(R.id.barChart);
        int color1 = ContextCompat.getColor(view.getContext(),R.color.bar1);
        int color2 = ContextCompat.getColor(view.getContext(),R.color.bar2);
        vote1.setTextColor(color1);
        vote2.setTextColor(color2);

        if (data1 + data2 == 0) {
            color1 = ContextCompat.getColor(view.getContext(),R.color.appBackground);
            data1 = 1;
        }

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
    }

    public static void fillPostItem (View view, Post post, Context context, boolean showDate) {
        TextView date = (TextView) view.findViewById(R.id.date);
        TextView vote1 = (TextView) view.findViewById(R.id.vote1);
        TextView vote2 = (TextView) view.findViewById(R.id.vote2);
        TextView headline = (TextView) view.findViewById(R.id.headline);
        ImageView image1 = (ImageView) view.findViewById(R.id.imageView1);
        ImageView image2 = (ImageView) view.findViewById(R.id.imageView2);

        headline.setText(post.title);

        if (post.title.length() > 32)
            headline.setTextSize(10);

        vote1.setText(String.valueOf(getPercentage(post.votes1, post.votes2, 1) + "%"));
        vote2.setText(String.valueOf(getPercentage(post.votes1, post.votes2, 2) + "%"));

        if (showDate) {
            String dateString = (post.utcDate == null) ? "No Date" : DateConverter.utcToShortDate(post.utcDate);
            date.setText(dateString);
        } else
            date.setVisibility(View.GONE);

        HorizontalBarChart horizontalBarChart = (HorizontalBarChart) view.findViewById(R.id.stat_bar);
        updateSmallBarChart(horizontalBarChart, post.votes1, post.votes2, context);

        String url1 = CloudinaryClient.smallImageUrl(post.image1,true);
        String url2 = CloudinaryClient.smallImageUrl(post.image2,true);
        Glide.with(context).load(url1).into(image1);
        Glide.with(context).load(url2).into(image2);
    }



    private static int getPercentage(int votes1, int votes2, int choice) {
        double sum = votes1 + votes2;
        int per_votes1 = (int)Math.round(votes1*100/sum);
        if (sum == 0)
            return 0;
        switch (choice) {
            case 1:
                return per_votes1;
            case 2:
                return 100-per_votes1;
        }
        return -1;
    }
}
