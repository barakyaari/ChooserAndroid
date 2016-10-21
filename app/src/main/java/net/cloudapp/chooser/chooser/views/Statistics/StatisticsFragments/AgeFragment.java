package net.cloudapp.chooser.chooser.views.Statistics.StatisticsFragments;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
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

import net.cloudapp.chooser.chooser.Common.StatisticsChartSetup;
import net.cloudapp.chooser.chooser.R;
import net.cloudapp.chooser.chooser.model.PostStatistics;
import net.cloudapp.chooser.chooser.views.dialogs.AgeFragmentDialog;

import java.util.ArrayList;
import java.util.Collections;


public class AgeFragment implements View.OnClickListener{
    private View view;
    private android.support.v4.app.Fragment parentFragment;
    private PostStatistics postStatistics;

    private HorizontalBarChart ageBarChart;
    private PieChart agePieChart;
    private TextView ageData1, ageData2;
    private Button ageIntervalButton, fromAgeButton, toAgeButton;
    public int ageIntervals, fromAgeGroup, toAgeGroup;


    public AgeFragment (View view, android.support.v4.app.Fragment parentFragment) {
        this.view = view;
        this.parentFragment = parentFragment;
        createAgeFragment();
    }

    private void addDefaultValues () {
        ageIntervals = 5;
        fromAgeGroup = 0;
        toAgeGroup = 21;
    }

    private void createAgeFragment() {
        addDefaultValues();
        agePieChart = (PieChart) view.findViewById(R.id.pieChart);
        ageBarChart = (HorizontalBarChart) view.findViewById(R.id.barChart);
        StatisticsChartSetup.createAgePieChart(agePieChart,view.getContext());
        StatisticsChartSetup.createBarChart(ageBarChart);
        ageBarChart.setFitBars(true);

        ageData1 = (TextView) view.findViewById(R.id.ageData1);
        ageData2 = (TextView) view.findViewById(R.id.ageData2);
        ageIntervalButton = (Button) view.findViewById(R.id.changeIntervalButton);
        fromAgeButton = (Button) view.findViewById(R.id.fromAgeButton);
        toAgeButton = (Button) view.findViewById(R.id.toAgeButton);

        ageIntervalButton.setOnClickListener(this);

        fromAgeButton.setOnClickListener(this);
        toAgeButton.setText(String.valueOf(toAgeGroup));
        fromAgeButton.setText(String.valueOf(fromAgeGroup));

        toAgeButton.setOnClickListener(this);
    }

    public void refreshAgeFragment(PostStatistics postStatistics) {
        this.postStatistics = postStatistics;
        updateAgePieData();
        updateAgeBarData();
    }


    private void updateAgePieData () {
        ArrayList<PieEntry> entries = new ArrayList<>();
        ArrayList<Integer> values = getTotalAgeRangeVotes();
        for (int i = 0; i < values.size(); i++) {
            int val = values.get(i);
            if (val != 0)
                entries.add(new PieEntry(val, (i * ageIntervals) + "-" + ((i + 1) * ageIntervals - 1)));
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

    private void updateAgeBarData() {
        int data1 = getAgeRangeVotes(1);
        int data2 = getAgeRangeVotes(2);
        ArrayList<BarEntry> vals = new ArrayList<>();
        vals.add(new BarEntry(0, new float[]{data1, data2}));

        BarDataSet set = new BarDataSet(vals, "");
        ArrayList<IBarDataSet> dataSet = new ArrayList<>();
        set.setColors(new int[]{ContextCompat.getColor(view.getContext(),R.color.bar1), ContextCompat.getColor(view.getContext(),R.color.bar2)});
        set.setBarBorderWidth(2);
        set.setBarBorderColor(Color.BLACK);
        dataSet.add(set);
        BarData data = new BarData(dataSet);
        data.setDrawValues(false);

        ageBarChart.setData(data);
        ageBarChart.notifyDataSetChanged();
        ageBarChart.invalidate();

        ageData1.setText(String.valueOf(data1));
        ageData2.setText(String.valueOf(data2));
    }


    private ArrayList<Integer> getTotalAgeRangeVotes() {
        int ageCap = Math.max(postStatistics.ageVotes1.size(),postStatistics.ageVotes2.size());

        ArrayList<Integer> voteArray = new ArrayList<>(Collections.nCopies(1 + ageCap/ageIntervals, 0));

        for (int i = 0; i < ageCap; i++) {
            int currInterval = i/ageIntervals;
            if (i < postStatistics.ageVotes1.size())
                voteArray.set(currInterval, voteArray.get(currInterval) + postStatistics.ageVotes1.get(i));
            if (i < postStatistics.ageVotes2.size())
                voteArray.set(currInterval, voteArray.get(currInterval) + postStatistics.ageVotes2.get(i));
        }
        return voteArray;
    }

    private int getAgeRangeVotes(int voteNum) {
        //start inclusive, end inclusive
        int count = 0;
        ArrayList<Integer> arrayCount;
        if (voteNum == 1)
            arrayCount = postStatistics.ageVotes1;
        else if (voteNum == 2)
            arrayCount = postStatistics.ageVotes2;
        else return 0;

        int arraySize = arrayCount.size();
        if (fromAgeGroup > arraySize)
            return 0;

        for (int i = fromAgeGroup; i < Math.min(toAgeGroup+1,arraySize); i++)
            count += arrayCount.get(i);


        return count;
    }

    @Override
    public void onClick(View v) {
        AgeFragmentDialog seekBarDialog;
        switch (v.getId()) {
            case R.id.changeIntervalButton:
                seekBarDialog = new AgeFragmentDialog("Choose Age Intervals",3,25, ageIntervals) {
                    @Override
                    public void onEndOfSeekBarTracking(int progress) {
                        ageIntervals = progress;
                        updateAgePieData();
                    }
                };
                seekBarDialog.show(parentFragment.getActivity().getFragmentManager(), "Seek Bar Dialog");
                break;

            case R.id.fromAgeButton:
                 seekBarDialog = new AgeFragmentDialog("From Age",0,toAgeGroup-1, fromAgeGroup) {
                    @Override
                    public void onEndOfSeekBarTracking(int progress) {
                        fromAgeGroup = progress;
                        updateAgeBarData();
                        fromAgeButton.setText(String.valueOf(progress));
                    }
                };
                seekBarDialog.show(parentFragment.getActivity().getFragmentManager(), "Seek Bar Dialog");
                break;

            case R.id.toAgeButton:
                seekBarDialog = new AgeFragmentDialog("To Age",fromAgeGroup+1,Math.max(postStatistics.ageVotes1.size(),postStatistics.ageVotes2.size())-1, toAgeGroup) {
                    @Override
                    public void onEndOfSeekBarTracking(int progress) {
                        toAgeGroup = progress;
                        updateAgeBarData();
                        toAgeButton.setText(String.valueOf(progress));
                    }
                };
                seekBarDialog.show(parentFragment.getActivity().getFragmentManager(), "Seek Bar Dialog");
                break;
        }
    }
}
