package net.cloudapp.chooser.chooser.views.Statistics;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.mikephil.charting.charts.HorizontalBarChart;


import net.cloudapp.chooser.chooser.Common.PostRepository;
import net.cloudapp.chooser.chooser.Common.StatisticsChartSetup;
import net.cloudapp.chooser.chooser.Images.CloudinaryClient;
import net.cloudapp.chooser.chooser.R;
import net.cloudapp.chooser.chooser.model.Post;
import net.cloudapp.chooser.chooser.views.Statistics.StatisticsFragments.StatisticsFragmentManager;
import net.cloudapp.chooser.chooser.views.dialogs.DeletePostDialog;


public class StatisticsView extends AppCompatActivity implements View.OnClickListener {
    ViewPager viewPager;
    private int postIndex;
    private Post post;
    private Button deletePost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.post_statistics);
        postIndex = getIntent().getIntExtra("PostIndex",-1);
        if (postIndex == -1)
            finish();

        post = PostRepository.myPosts.get(postIndex);
        updatePostHeadline();
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        deletePost = (Button) findViewById(R.id.deletePost);
        deletePost.setOnClickListener(this);
        viewPager.setAdapter(new SampleFragmentPagerAdapter(getSupportFragmentManager(), this));
        tabLayout.setupWithViewPager(viewPager);
        Log.d("Chooser", "Statistics view loaded");

    }

    private void updatePostHeadline() {
        TextView votes1 = (TextView) findViewById(R.id.votes1);
        TextView votes2 = (TextView) findViewById(R.id.votes2);
        TextView headline = (TextView) findViewById(R.id.headline);
        ImageView image1 = (ImageView) findViewById(R.id.imageView1);
        ImageView image2 = (ImageView) findViewById(R.id.imageView2);

        votes1.setText(String.valueOf(post.votes1));
        votes2.setText(String.valueOf(post.votes2));
        headline.setText(post.title);
        if (post.title.length() > 20)
            headline.setTextSize(10);
        String url1 = CloudinaryClient.smallImageUrl(post.image1,true);
        String url2 = CloudinaryClient.smallImageUrl(post.image2,true);
        Glide.with(this).load(url1).into(image1);
        Glide.with(this).load(url2).into(image2);
        HorizontalBarChart horizontalBarChart = (HorizontalBarChart) this.findViewById(R.id.stat_bar);
        StatisticsChartSetup.updateSmallBarChart(horizontalBarChart, post.votes1, post.votes2, this);
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


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.deletePost:
                deletePost();
                break;
        }
    }


    private void deletePost() {
        DeletePostDialog dpDialog = new DeletePostDialog(post._id, this);
        dpDialog.show(getFragmentManager(),"DeletePostDialog");
    }

    public class SampleFragmentPagerAdapter extends FragmentPagerAdapter {
        final int TAB_COUNT = 3;
        private String tabTitles[] = new String[] { "Home", "Gender", "Age" };

        public SampleFragmentPagerAdapter(FragmentManager fm, Context context) {
            super(fm);
        }

        @Override
        public int getCount() {
            return TAB_COUNT;
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            return StatisticsFragmentManager.newInstance(++position, postIndex);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
        }

        @Override
        public int getItemPosition(Object object) {
            return super.getItemPosition(object);
        }
    }
}