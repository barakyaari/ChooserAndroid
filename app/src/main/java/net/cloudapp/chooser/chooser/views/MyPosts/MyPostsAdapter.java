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
        StatisticsChartSetup.fillPostItem(customView,getItem(position),getContext(),true);
        return customView;
    }



}
