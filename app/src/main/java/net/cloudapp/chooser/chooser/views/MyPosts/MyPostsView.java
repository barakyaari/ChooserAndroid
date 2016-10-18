package net.cloudapp.chooser.chooser.views.MyPosts;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import net.cloudapp.chooser.chooser.Common.PostRepository;
import net.cloudapp.chooser.chooser.Controller.DeletePostController;
import net.cloudapp.chooser.chooser.Controller.MyPostsFetchController;
import net.cloudapp.chooser.chooser.R;
import net.cloudapp.chooser.chooser.views.dialogs.DeletePostDialog;


public class MyPostsView extends AppCompatActivity implements ListView.OnItemLongClickListener, ListView.OnItemClickListener {
    private ListView postList;
    private MyPostsAdapter myPostsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_posts_view);
        postList = (ListView) findViewById(R.id.postList);
        getSupportActionBar().setTitle("My Posts");
        postList.setOnItemLongClickListener(this);
        postList.setOnItemClickListener(this);
        MyPostsFetchController myPostsFetchController = new MyPostsFetchController(this);
        myPostsFetchController.getMyPosts();
        myPostsAdapter = new MyPostsAdapter(this, PostRepository.myPosts);
        postList.setAdapter(myPostsAdapter);
    }


    public void refreshView() {
        myPostsAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onItemLongClick(AdapterView parent, View view, int position, long id) {
        DeletePostDialog dpDialog = new DeletePostDialog(PostRepository.myPosts.get(position)._id,this);
        dpDialog.show(getFragmentManager(),"DeletePostDialog");
        return false;
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent i = new Intent("net.cloudapp.chooser.chooser.views.Statistics.StatisticsView");
        i.putExtra("PostIndex", position);
        startActivity(i);
    }
}
