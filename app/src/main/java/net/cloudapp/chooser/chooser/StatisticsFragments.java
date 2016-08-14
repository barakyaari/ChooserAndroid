package net.cloudapp.chooser.chooser;


import android.graphics.Color;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ViewPortHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by Ben on 08/07/2016.
 */
public class StatisticsFragments extends Fragment {
    public static final String ARG_PAGE = "ARG_PAGE";
    private SessionDetails sessionDetails;
    private Post post;
    private PostStatistics postStatistics;
    private HorizontalBarChart generalBarChart, genderBarChart, femaleBarChart, maleBarChart, ageBarChart;
    private PieChart agePieChart;
    private TextView totalVotes,postDate,promotionStatus, genDist1, genDist2, genF1, genF2, genM1, genM2, ageData1, ageData2;
    private Button ageIntervalButton, fromAgeButton, toAgeButton;
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

        private final int val;
        private ChartColors(int val) {
            this.val = val;
        }
    }

    public static StatisticsFragments newInstance(int page, SessionDetails sessionDetails) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        args.putSerializable("SessionDetails", sessionDetails);
        StatisticsFragments fragment = new StatisticsFragments();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        sessionDetails = (SessionDetails) getArguments().getSerializable("SessionDetails");
        post = sessionDetails.post.getPost();
        postStatistics = post.postStatistics;
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {
        View view = null;
        switch (getArguments().getInt(ARG_PAGE)) {
            case 1:
                view = inflater.inflate(R.layout.frag_stat_general, container, false);
                createGeneralFragment(view);
                break;
            case 2:
                view = inflater.inflate(R.layout.frag_stat_gender, container, false);
                createGenderFragment(view);
                break;
            case 3:
                view = inflater.inflate(R.layout.frag_stat_age, container, false);
                createAgeFragment(view);
                break;
            case 4:
                view = inflater.inflate(R.layout.frag_stat_map, container, false);
                createMapFragment(view);
                break;
            case 5:
                view = inflater.inflate(R.layout.frag_stat_advanced, container, false);
                createAdvancedFragment(view);
                break;
        }
        refreshStatisticsTabs();
        return view;
    }

    public void refreshStatisticsTabs() {
        Runnable doAtFinish = new Runnable() {
            @Override
            public void run() {
                String responseText = sessionDetails.responseString;
                sessionDetails.responseString = "";
                try {
                    JSONArray jArray = new JSONArray(responseText);
                    parseJson(jArray);
                    updateStatistics();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        ConnectionManager connectionManager = new ConnectionManager(sessionDetails);
        connectionManager.getStatistics(post.id,doAtFinish);
    }

    private void updateStatistics() {
        switch (getArguments().getInt(ARG_PAGE)) {
            case 1:
                updateGeneralBarData();
                break;
            case 2:
                updateGenderBarData(genderBarChart, ChartColors.GENDER_DIST1.val, ChartColors.GENDER_DIST2.val, postStatistics.getTotalFemaleVotes(),postStatistics.getTotalMaleVotes(),genDist1,genDist2);
                updateGenderBarData(femaleBarChart, ChartColors.GENDER_FVOTES1.val,ChartColors.GENDER_FVOTES2.val, postStatistics.femaleVotes1,postStatistics.femaleVotes2,genF1,genF2);
                updateGenderBarData(maleBarChart, ChartColors.GENDER_MVOTES1.val,ChartColors.GENDER_MVOTES2.val, postStatistics.maleVotes1,postStatistics.maleVotes2,genM1,genM2);
                break;
            case 3:
                updateAgePieData();
                updateAgeBarData();
                break;
            case 4:

                break;
            case 5:

                break;
        }

    }



    private void createGeneralFragment(View view) {
        generalBarChart = (HorizontalBarChart) view.findViewById(R.id.chart);
        createBarChart(generalBarChart);
        generalBarChart.setFitBars(true);
        totalVotes = (TextView) view.findViewById(R.id.totalVotes);
        postDate = (TextView) view.findViewById(R.id.postDate);
        promotionStatus = (TextView) view.findViewById(R.id.promotion);
        promotionStatus.setVisibility(View.INVISIBLE);
    }


    private void createGenderFragment(View view) {
        genderBarChart = (HorizontalBarChart) view.findViewById(R.id.percentBar);
        femaleBarChart = (HorizontalBarChart) view.findViewById(R.id.femaleBar);
        maleBarChart = (HorizontalBarChart) view.findViewById(R.id.maleBar);
        createBarChart(genderBarChart);
        createBarChart(femaleBarChart);
        createBarChart(maleBarChart);
        genDist1 = (TextView) view.findViewById(R.id.dist_data1);
        genDist2 = (TextView) view.findViewById(R.id.dist_data2);
        genF1 = (TextView) view.findViewById(R.id.female_data1);
        genF2 = (TextView) view.findViewById(R.id.female_data2);
        genM1 = (TextView) view.findViewById(R.id.male_data1);
        genM2 = (TextView) view.findViewById(R.id.male_data2);
    }


    private void createAgeFragment(View view) {
        agePieChart = (PieChart) view.findViewById(R.id.pieChart);
        ageBarChart = (HorizontalBarChart) view.findViewById(R.id.barChart);
        createPieChart(agePieChart);
        createBarChart(ageBarChart);
        ageBarChart.setFitBars(true);

        ageData1 = (TextView) view.findViewById(R.id.ageData1);
        ageData2 = (TextView) view.findViewById(R.id.ageData2);
        ageIntervalButton = (Button) view.findViewById(R.id.changeIntervalButton);
        fromAgeButton = (Button) view.findViewById(R.id.fromAgeButton);
        toAgeButton = (Button) view.findViewById(R.id.toAgeButton);
        ageIntervalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SeekBarDialog seekBarDialog = new SeekBarDialog("Choose Age Intervals",3,25, postStatistics.ageIntervals) {
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
                SeekBarDialog seekBarDialog = new SeekBarDialog("From Age",0,postStatistics.toAgeGroup-1, postStatistics.fromAgeGroup) {
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
                SeekBarDialog seekBarDialog = new SeekBarDialog("To Age",postStatistics.fromAgeGroup+1,Math.max(postStatistics.ageVotes1.size(),postStatistics.ageVotes2.size())-1, postStatistics.toAgeGroup) {
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




    private void createMapFragment(View view){

    }

    private void createAdvancedFragment(View view) {

    }

    public abstract class SeekBarDialog extends DialogFragment {
        private TextView seekBarValue, headline;
        private SeekBar seekBar;
        private int minVal;
        private int maxVal;
        private int progress;
        private String headlineText;
        public SeekBarDialog(String headlineText, int minVal, int maxVal, int initialValue) {
            this.headlineText = headlineText;
            this.minVal = minVal;
            this.maxVal = maxVal;
            this.progress = initialValue;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.seek_bar_dialog, container);
            seekBar = (SeekBar) view.findViewById(R.id.intervalSeekBar);
            headline = (TextView) view.findViewById(R.id.seekBarHeadline);
            seekBarValue = (TextView) view.findViewById(R.id.seekBarValue);
            headline.setText(headlineText);
            seekBar.setMax(maxVal-minVal);
            seekBarValue.setText(progress + "/" + (seekBar.getMax()+minVal));
            seekBar.setProgress(progress-minVal);
            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                boolean progressChanged = false;
                @Override
                public void onProgressChanged(SeekBar seekBar, int progressValue, boolean fromUser) {
                    progress = progressValue;
                    progressChanged = true;
                    seekBarValue.setText((progress+minVal) + "/" + (seekBar.getMax()+minVal));
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    seekBarValue.setText((progress+minVal) + "/" + (seekBar.getMax()+minVal));
                    if (progressChanged)
                        onEndOfSeekBarTracking(progress+minVal);
                    else
                        onEndOfSeekBarTracking(progress);


                    dismiss();
                }
            });
            return view;
        }

        public abstract void onEndOfSeekBarTracking(int progress);
    }


    private void createPieChart(PieChart pieChart) {
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

    private void createBarChart(HorizontalBarChart horizontalBarChart) {
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

    private void updateGeneralBarData () {
        ArrayList<BarEntry> vals = new ArrayList<>();
        vals.add(new BarEntry(1f, post.votes1));
        vals.add(new BarEntry(2.2f, post.votes2));


        BarDataSet set = new BarDataSet(vals, "General Info");
        ArrayList<IBarDataSet> dataSet = new ArrayList<>();
        set.setColors(new int[]{ChartColors.MAIN1.val, ChartColors.MAIN2.val});
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

    private void updateGenderBarData (HorizontalBarChart horizontalBarChart, int color1, int color2, float data1, float data2, TextView dataText1, TextView dataText2) {
        ArrayList<BarEntry> vals = new ArrayList<>();
        vals.add(new BarEntry(0, new float[]{data1, data2}));

        BarDataSet set = new BarDataSet(vals, "");
        ArrayList<IBarDataSet> dataSet = new ArrayList<>();
        set.setColors(new int[]{color1, color2});
        set.setBarBorderWidth(2);
        set.setBarBorderColor(ChartColors.FRAME.val);
        dataSet.add(set);

        BarData data = new BarData(dataSet);
        data.setDrawValues(false);

        horizontalBarChart.setData(data);
        horizontalBarChart.notifyDataSetChanged();
        horizontalBarChart.invalidate();

        dataText1.setText(String.valueOf(data1));
        dataText2.setText(String.valueOf(data2));
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

    private void updateAgeBarData() {
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

    private void parseJson(JSONArray jArray) throws JSONException {
        JSONObject jObject = jArray.getJSONObject(0);
        postStatistics.femaleVotes1 = Integer.parseInt(jObject.getString("femaleVotes1"));
        postStatistics.femaleVotes2 = Integer.parseInt(jObject.getString("femaleVotes2"));
        postStatistics.maleVotes1 = Integer.parseInt(jObject.getString("maleVotes1"));
        postStatistics.maleVotes2 = Integer.parseInt(jObject.getString("maleVotes2"));
        post.postStatistics = postStatistics;

        JSONArray jAgeArray = jArray.getJSONArray(1);
        JSONObject jAgeObject;
        int vote1Filler = 0;
        int vote2Filler = 0;
        int age,vote,sumVotes;
        ArrayList<Integer> ageVotes1 = new ArrayList<>();
        ArrayList<Integer> ageVotes2 = new ArrayList<>();

        for (int i = 0; i < jAgeArray.length(); i++) {
            jAgeObject = jAgeArray.getJSONObject(i);
            vote = Integer.parseInt(jAgeObject.getString("vote"));
            age = Integer.parseInt(jAgeObject.getString("age"));
            sumVotes = Integer.parseInt(jAgeObject.getString("SumVotes"));
            if (vote == 1) {
                for (int j = vote1Filler; j < age; j++)
                    ageVotes1.add(0);
                vote1Filler = age + 1;
                ageVotes1.add(sumVotes);
            }
            if (vote == 2) {
                for (int j = vote2Filler; j < age; j++)
                    ageVotes2.add(0);
                vote2Filler = age + 1;
                ageVotes2.add(sumVotes);
            }

        }
        postStatistics.ageVotes1 = ageVotes1;
        postStatistics.ageVotes2 = ageVotes2;


        jObject = jArray.getJSONObject(2);
        sessionDetails.currentServerTime = Post.string2Calendar(jObject.getString("currentTime"));

        jObject = jArray.getJSONObject(3);
        if (!jObject.getString("promotion_expiration").equals("null"))
            post.setPromotionExpiration(jObject.getString("promotion_expiration"));
    }

    private String postedTime(GregorianCalendar earlyDate, GregorianCalendar laterDate) {

        int laterTime = laterDate.get(Calendar.DAY_OF_YEAR) + laterDate.get(Calendar.YEAR)*365;
        int earlyTime = earlyDate.get(Calendar.DAY_OF_YEAR) + earlyDate.get(Calendar.YEAR)*365;
        int amount = laterTime-earlyTime;
        if (amount > 730)
            return amount/365 + " years";
        if (amount >= 365)
            return "a year";
        if (amount > 60)
            return amount/30 + " months";
        if (amount >= 30)
            return "a month";

        if (amount > 2)
            return amount + " days";
        if (amount >= 1)
            return "a day";

        laterTime = laterDate.get(Calendar.MINUTE) + laterDate.get(Calendar.HOUR)*60;
        earlyTime = earlyDate.get(Calendar.MINUTE) + earlyDate.get(Calendar.HOUR)*60;
        amount = laterTime-earlyTime;
        if (amount > 120)
            return amount/60 + " hours";
        if (amount >= 60)
            return "an hour";
        if (amount > 1)
            return amount + " minutes";
        if (amount == 1)
            return "a minute";
        return "less than a minute";
    }


}
