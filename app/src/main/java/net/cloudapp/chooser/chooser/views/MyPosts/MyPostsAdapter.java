package net.cloudapp.chooser.chooser.views.MyPosts;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

import net.cloudapp.chooser.chooser.Common.DateConverter;
import net.cloudapp.chooser.chooser.Images.CloudinaryClient;
import net.cloudapp.chooser.chooser.R;
import net.cloudapp.chooser.chooser.model.Post;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ben on 26/06/2016.
 */
public class MyPostsAdapter extends ArrayAdapter<Post> {

    public MyPostsAdapter(Context context, List<Post> myPosts) {
        super(context, R.layout.my_posts_row, myPosts);
    }



    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View customView = inflater.inflate(R.layout.my_posts_row,parent,false);
        TextView date = (TextView) customView.findViewById(R.id.date);
        TextView vote1 = (TextView) customView.findViewById(R.id.vote1);
        TextView vote2 = (TextView) customView.findViewById(R.id.vote2);
        TextView headline = (TextView) customView.findViewById(R.id.headline);
        ImageView image1 = (ImageView) customView.findViewById(R.id.imageView1);
        ImageView image2 = (ImageView) customView.findViewById(R.id.imageView2);

        Post post = getItem(position);
        headline.setText(post.title);

        vote1.setText(String.valueOf(post.votes1));
        vote2.setText(String.valueOf(post.votes2));

        String dateString = (post.utcDate == null) ? "No Date" : DateConverter.utcToShortDate(post.utcDate);
        date.setText(dateString);

        updatePostBar(customView, post.votes1, post.votes2);

        String url1 = CloudinaryClient.smallImageUrl(post.image1);
        String url2 = CloudinaryClient.smallImageUrl(post.image2);
        Glide.with(getContext()).load(url1).into(image1);
        Glide.with(getContext()).load(url2).into(image2);

        return customView;
    }


    private void updatePostBar(View view, int vote1, int vote2) {
        HorizontalBarChart horizontalBarChart = (HorizontalBarChart) view.findViewById(R.id.stat_bar);

        horizontalBarChart.getXAxis().setEnabled(false);
        horizontalBarChart.getAxisLeft().setEnabled(false);
        horizontalBarChart.getAxisLeft().setAxisMinValue(0f);
        horizontalBarChart.getAxisRight().setEnabled(false);
        horizontalBarChart.setDescription("");
        horizontalBarChart.setClickable(false);
        horizontalBarChart.setDoubleTapToZoomEnabled(false);
        horizontalBarChart.setPinchZoom(false);


        ArrayList<BarEntry> vals = new ArrayList<>();
        vals.add(new BarEntry(0, new float[]{vote1, vote2}));

        BarDataSet set = new BarDataSet(vals, "");
        set.setBarBorderWidth(2);
        set.setBarBorderColor(Color.BLACK);
        ArrayList<IBarDataSet> dataSet = new ArrayList<>();
        set.setColors(new int[]{Color.CYAN, Color.LTGRAY});
        dataSet.add(set);

        BarData data = new BarData(dataSet);
        data.setDrawValues(false);

        horizontalBarChart.setData(data);
        horizontalBarChart.invalidate();
    }

}
