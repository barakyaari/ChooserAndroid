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
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

import net.cloudapp.chooser.chooser.Common.PostRepository;
import net.cloudapp.chooser.chooser.Common.StatisticsChartSetup;
import net.cloudapp.chooser.chooser.R;
import net.cloudapp.chooser.chooser.model.Post;
import net.cloudapp.chooser.chooser.views.dialogs.AgeFragmentIntervalDialog;

import java.util.ArrayList;

/**
 * Created by Ben on 18/10/2016.
 */
public class AgeFragment {
    private View view;
    private HorizontalBarChart ageBarChart;
    private PieChart agePieChart;
    private TextView ageData1, ageData2;
    private Button ageIntervalButton, fromAgeButton, toAgeButton;


    public AgeFragment (View view) {
        this.view = view;
//        createAgeFragment();
    }
/*
    private void createAgeFragment() {
        agePieChart = (PieChart) view.findViewById(R.id.pieChart);
        ageBarChart = (HorizontalBarChart) view.findViewById(R.id.barChart);
        StatisticsChartSetup.createPieChart(agePieChart);
        StatisticsChartSetup.createBarChart(ageBarChart);
        ageBarChart.setFitBars(true);

        ageData1 = (TextView) view.findViewById(R.id.ageData1);
        ageData2 = (TextView) view.findViewById(R.id.ageData2);
        ageIntervalButton = (Button) view.findViewById(R.id.changeIntervalButton);
        fromAgeButton = (Button) view.findViewById(R.id.fromAgeButton);
        toAgeButton = (Button) view.findViewById(R.id.toAgeButton);

        ageIntervalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AgeFragmentIntervalDialog seekBarDialog = new AgeFragmentIntervalDialog("Choose Age Intervals",3,25, postStatistics.ageIntervals) {
                    @Override
                    public void onEndOfSeekBarTracking(int progress) {
                        postStatistics.ageIntervals = progress;
                        updateAgePieData();
                    }
                };
                seekBarDialog.show(getActivity().getSupportFragmentManager(), "Seek Bar Dialog");
            }

       });

        fromAgeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AgeFragmentIntervalDialog seekBarDialog = new AgeFragmentIntervalDialog("From Age",0,postStatistics.toAgeGroup-1, postStatistics.fromAgeGroup) {
                    @Override
                    public void onEndOfSeekBarTracking(int progress) {
                        postStatistics.fromAgeGroup = progress;
                        updateAgeBarData();
                        fromAgeButton.setText(String.valueOf(progress));
                    }
                };
                seekBarDialog.show(getActivity().getSupportFragmentManager(), "Seek Bar Dialog");
            }
        });
        toAgeButton.setText(String.valueOf(postStatistics.toAgeGroup));
        fromAgeButton.setText(String.valueOf(postStatistics.fromAgeGroup));

        toAgeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AgeFragmentIntervalDialog seekBarDialog = new AgeFragmentIntervalDialog("To Age",postStatistics.fromAgeGroup+1,Math.max(postStatistics.ageVotes1.size(),postStatistics.ageVotes2.size())-1, postStatistics.toAgeGroup) {
                    @Override
                    public void onEndOfSeekBarTracking(int progress) {
                        postStatistics.toAgeGroup = progress;
                        updateAgeBarData();
                        toAgeButton.setText(String.valueOf(progress));
                    }
                };
                seekBarDialog.show(getActivity().getSupportFragmentManager(), "Seek Bar Dialog");
            }
        });

    }




    private void updateAgePieData () {
        ArrayList<PieEntry> entries = new ArrayList<>();
        ArrayList<Integer> values = postStatistics.getTotalAgeRangeVotes();
        for (int i = 0; i < values.size(); i++) {
            int val = values.get(i);
            if (val != 0)
                entries.add(new PieEntry(val, (i * postStatistics.ageIntervals) + "-" + ((i + 1) * postStatistics.ageIntervals - 1)));
        }

        PieDataSet dataSet = new PieDataSet(entries, "test chart");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);
        dataSet.setColor(Color.LTGRAY);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.BLACK);
        agePieChart.setData(data);


        agePieChart.notifyDataSetChanged();
        agePieChart.invalidate();
    }

    public void updateAgeBarData() {
        int data1 = postStatistics.getAgeRangeVotes(1);
        int data2 = postStatistics.getAgeRangeVotes(2);
        ArrayList<BarEntry> vals = new ArrayList<>();
        vals.add(new BarEntry(0, new float[]{data1, data2}));

        BarDataSet set = new BarDataSet(vals, "");
        ArrayList<IBarDataSet> dataSet = new ArrayList<>();
        set.setColors(new int[]{ChartColors.MAIN1.val, ChartColors.MAIN2.val});
        set.setBarBorderWidth(2);
        set.setBarBorderColor(ChartColors.FRAME.val);
        dataSet.add(set);

        BarData data = new BarData(dataSet);
        data.setDrawValues(false);

        ageBarChart.setData(data);
        ageBarChart.notifyDataSetChanged();
        ageBarChart.invalidate();

        ageData1.setText(String.valueOf(data1));
        ageData2.setText(String.valueOf(data2));
    }
    */

}
