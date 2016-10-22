package net.cloudapp.chooser.chooser.views.MyPosts;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.mikephil.charting.charts.HorizontalBarChart;

import net.cloudapp.chooser.chooser.Common.DateConverter;
import net.cloudapp.chooser.chooser.Common.StatisticsChartSetup;
import net.cloudapp.chooser.chooser.Images.CloudinaryClient;
import net.cloudapp.chooser.chooser.R;
import net.cloudapp.chooser.chooser.model.Post;

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
        if (post.title.length() > 20)
            headline.setTextSize(10);
        if (post.votes1 > 99 || post.votes2 > 99) {
            vote1.setTextSize(10);
            vote2.setTextSize(10);
        }


        vote1.setText(String.valueOf(post.votes1));
        vote2.setText(String.valueOf(post.votes2));

        String dateString = (post.utcDate == null) ? "No Date" : DateConverter.utcToShortDate(post.utcDate);
        date.setText(dateString);

        HorizontalBarChart horizontalBarChart = (HorizontalBarChart) customView.findViewById(R.id.stat_bar);
        StatisticsChartSetup.updateSmallBarChart(horizontalBarChart, post.votes1, post.votes2, getContext());

        String url1 = CloudinaryClient.smallImageUrl(post.image1,true);
        String url2 = CloudinaryClient.smallImageUrl(post.image2,true);
        Glide.with(getContext()).load(url1).into(image1);
        Glide.with(getContext()).load(url2).into(image2);

        return customView;
    }



}
