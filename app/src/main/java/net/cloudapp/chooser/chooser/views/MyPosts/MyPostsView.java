package net.cloudapp.chooser.chooser.views.MyPosts;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;

import net.cloudapp.chooser.chooser.Common.PostRepository;
import net.cloudapp.chooser.chooser.Controller.MyPostsFetchController;
import net.cloudapp.chooser.chooser.R;


public class MyPostsView extends AppCompatActivity implements ListView.OnItemClickListener {
    private ListView postList;
    private LinearLayout noPostsLayout;
    private MyPostsAdapter myPostsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_posts_view);
        postList = (ListView) findViewById(R.id.postList);
        noPostsLayout = (LinearLayout) findViewById(R.id.noPostsLayout);
        getSupportActionBar().setTitle("My Posts");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        postList.setOnItemClickListener(this);
        myPostsAdapter = new MyPostsAdapter(this, PostRepository.myPosts);
        postList.setAdapter(myPostsAdapter);
    }


    public void updateView() {
        MyPostsFetchController myPostsFetchController = new MyPostsFetchController(this);
        myPostsFetchController.getMyPosts();
    }

    public void refreshView() {
        if (PostRepository.myPosts.isEmpty()) {
            postList.setVisibility(View.GONE);
            noPostsLayout.setVisibility(View.VISIBLE);
            return;
        }
        if (postList.getVisibility() == View.GONE) {
            postList.setVisibility(View.VISIBLE);
            noPostsLayout.setVisibility(View.GONE);
        }
        myPostsAdapter.notifyDataSetChanged();
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent i = new Intent("net.cloudapp.chooser.chooser.views.Statistics.StatisticsView");
        i.putExtra("PostIndex", position);
        startActivity(i);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateView();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return super.onOptionsItemSelected(item);
    }

}
