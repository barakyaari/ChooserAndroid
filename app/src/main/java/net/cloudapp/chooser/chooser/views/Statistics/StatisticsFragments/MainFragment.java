package net.cloudapp.chooser.chooser.views.Statistics.StatisticsFragments;

import android.content.Intent;
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
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ViewPortHandler;

import net.cloudapp.chooser.chooser.Common.DateConverter;
import net.cloudapp.chooser.chooser.Common.PostRepository;
import net.cloudapp.chooser.chooser.Common.StatisticsChartSetup;
import net.cloudapp.chooser.chooser.Images.CloudinaryClient;
import net.cloudapp.chooser.chooser.R;
import net.cloudapp.chooser.chooser.model.Post;

import java.util.ArrayList;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by Ben on 18/10/2016.
 */
public class MainFragment implements View.OnClickListener {
    private View view;
    private Post post;
    private HorizontalBarChart generalBarChart;
    private TextView totalVotes,postDate, description1, description2;
    private ImageView image1, image2;

    public MainFragment (View view, int postIndex) {
        this.view = view;
        post = PostRepository.myPosts.get(postIndex);
        createGeneralFragment();
    }

    private void createGeneralFragment() {
        generalBarChart = (HorizontalBarChart) view.findViewById(R.id.chart);
        StatisticsChartSetup.createBarChart(generalBarChart);
        generalBarChart.setFitBars(true);
        image1 = (ImageView) view.findViewById(R.id.image1);
        image2 = (ImageView) view.findViewById(R.id.image2);
        image1.setOnClickListener(this);
        image2.setOnClickListener(this);
        totalVotes = (TextView) view.findViewById(R.id.totalVotes);
        postDate = (TextView) view.findViewById(R.id.postDate);
        description1 = (TextView) view.findViewById(R.id.description1);
        description2 = (TextView) view.findViewById(R.id.description2);
        loadImage (image1,post.image1);
        loadImage (image2,post.image2);
        description1.setText(post.description1);
        description2.setText(post.description2);
    }

    private void loadImage (ImageView view, String image) {
        String url = CloudinaryClient.bigImageUrl(image,true);
        Glide.with(getApplicationContext()).load(url).into(view);
    }

    private void showFullScreen(int selection) {
        Intent i;
        i = new Intent("net.cloudapp.chooser.chooser.ImageFullscreen");
        if (selection == 1)
            i.putExtra("image",post.image1);
        else
            i.putExtra("image",post.image2);
        view.getContext().startActivity(i);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.image1:
                showFullScreen(1);
                break;
            case R.id.image2:
                showFullScreen(2);
                break;
        }
    }

    void refreshGeneralBarData () {
        ArrayList<BarEntry> vals = new ArrayList<>();
        vals.add(new BarEntry(1f, post.votes2));
        vals.add(new BarEntry(2.2f, post.votes1));

        BarDataSet set = new BarDataSet(vals, "General Info");
        ArrayList<IBarDataSet> dataSet = new ArrayList<>();
        set.setColors(new int[]{ContextCompat.getColor(view.getContext(),R.color.bar2), ContextCompat.getColor(view.getContext(),R.color.bar1)});
        set.setValueTextColor(Color.LTGRAY);
        set.setBarBorderWidth(2);
        set.setBarBorderColor(Color.BLACK);
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
        postDate.setText("Posted: " + DateConverter.timeDiffFromNow(DateConverter.utcToCalendar(post.utcDate)) + " ago");

    }
}
