package net.cloudapp.chooser.chooser;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;


import java.util.ArrayList;

public class Statistics extends AppCompatActivity implements View.OnClickListener {
    private SessionDetails sessionDetails;
    ViewPager viewPager;
    private Post post;
    private Button promotePost,deletePost;
    private PromotionDialog promotionDialog;
    private StatisticsFragments currentFrag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.post_statistics);
        sessionDetails = (SessionDetails) getIntent().getSerializableExtra("SessionDetails");
        post = sessionDetails.post.getPost();

        updatePostHeadline();
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        promotePost = (Button) findViewById(R.id.promotePost);
        deletePost = (Button) findViewById(R.id.deletePost);
        deletePost.setOnClickListener(this);
        promotePost.setOnClickListener(this);

        viewPager.setAdapter(new SampleFragmentPagerAdapter(getSupportFragmentManager(), this));
        tabLayout.setupWithViewPager(viewPager);
    }

    private void updatePostHeadline() {
        TextView percentage1 = (TextView) findViewById(R.id.percentage1);
        TextView percentage2 = (TextView) findViewById(R.id.percentage2);
        TextView headline = (TextView) findViewById(R.id.headline);
        ImageView image1 = (ImageView) findViewById(R.id.imageView1);
        ImageView image2 = (ImageView) findViewById(R.id.imageView2);

        percentage1.setText(post.getPercentage(1) + "%");
        percentage2.setText(post.getPercentage(2) + "%");
        headline.setText(post.title);
        image1.setImageBitmap(post.image1);
        image2.setImageBitmap(post.image2);
        updatePostPercentage();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updatePostPercentage() {
        HorizontalBarChart horizontalBarChart = (HorizontalBarChart) this.findViewById(R.id.stat_bar);

        horizontalBarChart.getXAxis().setEnabled(false);
        horizontalBarChart.getAxisLeft().setEnabled(false);
        horizontalBarChart.getAxisLeft().setAxisMinValue(0f);
        horizontalBarChart.getAxisRight().setEnabled(false);
        horizontalBarChart.setDescription("");
        horizontalBarChart.setClickable(false);
        horizontalBarChart.setDoubleTapToZoomEnabled(false);
        horizontalBarChart.setPinchZoom(false);

        ArrayList<BarEntry> vals = new ArrayList<>();
        vals.add(new BarEntry(0, new float[]{post.getPercentage(1), post.getPercentage(2)}));

        BarDataSet set = new BarDataSet(vals, "Percentage");
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


    private void deletePost () {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Deleting Post")
                .setMessage("Are you sure you want to delete this post?\nNote: All your promotion time will be lost!")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Runnable doAtFinish = new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), "Post Deleted!", Toast.LENGTH_LONG).show();
                                finish();
                            }
                        };
                        ConnectionManager connectionManager = new ConnectionManager(sessionDetails);
                        connectionManager.deletePost(post.id, doAtFinish);
                    }

                })
                .setNegativeButton("No", null)
                .show();
    }

    private void promotePost () {
        promotionDialog = new PromotionDialog(sessionDetails, "Promote Post For:") {
            @Override
            public void onPromoteDialogFinish() {
                sessionDetails.userTokenCount -= getPrice();

                Runnable doAtFinish = new Runnable() {
                    @Override
                    public void run() {
                        if (sessionDetails.responseString.equals("1")) {
                            viewPager.getAdapter().notifyDataSetChanged();

                            Toast.makeText(getApplicationContext(), "Promotion Added!", Toast.LENGTH_LONG).show();
                        }

                        Toast.makeText(getApplicationContext(), "Promotion Failed!", Toast.LENGTH_LONG).show();
                    }
                };
                ConnectionManager connectionManager = new ConnectionManager(sessionDetails);
                connectionManager.addPromotion(sessionDetails.userId, post.id,getDuration(),promotionTime.name(), doAtFinish);
            }
        };
        promotionDialog.show(getFragmentManager(), "PromotionDialog");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.deletePost:
                deletePost();
                break;

            case R.id.promotePost:
                promotePost();
                break;
        }
    }

    public class SampleFragmentPagerAdapter extends FragmentPagerAdapter {
        final int TAB_COUNT = 5;
        private String tabTitles[] = new String[] { "Home", "Gender", "Age", "Map", "More" };

        public SampleFragmentPagerAdapter(FragmentManager fm, Context context) {
            super(fm);
        }

        @Override
        public int getCount() {
            return TAB_COUNT;
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            currentFrag = StatisticsFragments.newInstance(++position, sessionDetails);
            return currentFrag;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
        }

        @Override
        public int getItemPosition(Object object) {
            StatisticsFragments fragment = (StatisticsFragments) object;
            if (fragment != null)
                fragment.refreshStatisticsTabs();
            return super.getItemPosition(object);
        }
    }
}
