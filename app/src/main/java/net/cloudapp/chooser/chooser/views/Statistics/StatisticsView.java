package net.cloudapp.chooser.chooser.views.Statistics;

import android.content.Context;
import android.content.Intent;
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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.mikephil.charting.charts.HorizontalBarChart;


import net.cloudapp.chooser.chooser.Common.PostRepository;
import net.cloudapp.chooser.chooser.Common.SessionDetails;
import net.cloudapp.chooser.chooser.Common.StatisticsChartSetup;
import net.cloudapp.chooser.chooser.Controller.PromotionController;
import net.cloudapp.chooser.chooser.Images.CloudinaryClient;
import net.cloudapp.chooser.chooser.R;
import net.cloudapp.chooser.chooser.model.Post;
import net.cloudapp.chooser.chooser.views.Statistics.StatisticsFragments.StatisticsFragmentManager;
import net.cloudapp.chooser.chooser.views.dialogs.DeletePostDialog;
import net.cloudapp.chooser.chooser.views.dialogs.PromotePostDialog;


public class StatisticsView extends AppCompatActivity implements View.OnClickListener {
    ViewPager viewPager;
    private int postIndex;
    private Post post;
    private Button deletePost;
    private Button promotePost;
    private View postItem;
    private ImageView image1,image2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.post_statistics);
        postIndex = getIntent().getIntExtra("PostIndex",-1);
        if (postIndex == -1)
            finish();

        post = PostRepository.myPosts.get(postIndex);
        initializePostItem();
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        viewPager.setAdapter(new SampleFragmentPagerAdapter(getSupportFragmentManager(), this));
        tabLayout.setupWithViewPager(viewPager);
        Log.d("Chooser", "Statistics view loaded");

    }

    private void initializePostItem() {
        postItem = findViewById(R.id.postRowData);
        image1 = (ImageView) postItem.findViewById(R.id.imageView1);
        image2 = (ImageView) postItem.findViewById(R.id.imageView2);
        deletePost = (Button) findViewById(R.id.deletePost);
        promotePost = (Button) findViewById(R.id.promotePostButton);

        image1.setOnClickListener(this);
        image2.setOnClickListener(this);
        deletePost.setOnClickListener(this);
        promotePost.setOnClickListener(this);
        StatisticsChartSetup.fillPostItem(postItem, post, this, false);

        refreshPromoteButton();
    }

    public void refreshPromoteButton() {
        int numOfTokens = SessionDetails.getInstance().numOfTokens;
        int cost = PromotionController.PROMOTION_COST;

        promotePost.setEnabled(numOfTokens >= cost);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.promotePostButton:
                promotePost();
                break;
            case R.id.deletePost:
                deletePost();
                break;
            case R.id.imageView1:
                showFullScreen(1);
                break;
            case R.id.imageView2:
                showFullScreen(2);
                break;
        }
    }

    private void showFullScreen(int selection) {
        Intent i;
        i = new Intent("net.cloudapp.chooser.chooser.ImageFullscreen");
        if (selection == 1)
            i.putExtra("image",post.image1);
        else
            i.putExtra("image",post.image2);
        startActivity(i);
    }

    private void promotePost() {
        PromotePostDialog ppDialog = new PromotePostDialog(post._id, this);
        ppDialog.show(getFragmentManager(), "PromotePostDialog");
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