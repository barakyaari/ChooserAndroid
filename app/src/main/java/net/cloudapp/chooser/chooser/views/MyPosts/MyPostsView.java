package net.cloudapp.chooser.chooser.views.MyPosts;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import net.cloudapp.chooser.chooser.Common.PostRepository;
import net.cloudapp.chooser.chooser.Controller.MyPostsFetchController;
import net.cloudapp.chooser.chooser.R;


public class MyPostsView extends AppCompatActivity {
    private ListView postList;
    private MyPostsAdapter myPostsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_posts_view);
        postList = (ListView) findViewById(R.id.postList);
        getSupportActionBar().setTitle("My Posts");
        MyPostsFetchController myPostsFetchController = new MyPostsFetchController(this);
        myPostsFetchController.getMyPosts();
        myPostsAdapter = new MyPostsAdapter(this, PostRepository.myPosts);
        postList.setAdapter(myPostsAdapter);
    }


    public void refreshView() {
        myPostsAdapter.notifyDataSetChanged();
    }
}
