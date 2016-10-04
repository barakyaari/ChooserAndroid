package net.cloudapp.chooser.chooser;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

import net.cloudapp.chooser.chooser.Model.Post;
import net.cloudapp.chooser.chooser.R;

import java.util.ArrayList;
import java.util.List;

public class NavDrawerAdapter extends ArrayAdapter<Post> {

    public NavDrawerAdapter(Context context, List<Post> myPosts) {
        super(context, R.layout.custom_row, myPosts);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View customView = inflater.inflate(R.layout.custom_row,parent,false);
        TextView date = (TextView) customView.findViewById(R.id.date);
        TextView percentage1 = (TextView) customView.findViewById(R.id.percentage1);
        TextView percentage2 = (TextView) customView.findViewById(R.id.percentage2);
        TextView headline = (TextView) customView.findViewById(R.id.headline);
        ImageView image1 = (ImageView) customView.findViewById(R.id.imageView1);
        ImageView image2 = (ImageView) customView.findViewById(R.id.imageView2);

        Post post = getItem(position);
//        percentage1.setText(post.getPercentage(1) + "%");
//        percentage2.setText(post.getPercentage(2) + "%");
//        headline.setText(post.title);
//        date.setText(post.getShortDate());
//        image1.setImageBitmap(post.image1);
//        image2.setImageBitmap(post.image2);
//
//        updatePostPercentage(customView, post.getPercentage(1), post.getPercentage(2));

        return customView;
    }


    private void updatePostPercentage(View view, int percentage1, int percentage2) {
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
        vals.add(new BarEntry(0, new float[]{percentage1, percentage2}));

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
