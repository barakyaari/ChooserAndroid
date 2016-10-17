package net.cloudapp.chooser.chooser.views;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextSwitcher;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.facebook.login.LoginManager;

import net.cloudapp.chooser.chooser.Common.PostRepository;
import net.cloudapp.chooser.chooser.Controller.PostsFetchController;
import net.cloudapp.chooser.chooser.Controller.VoteController;
import net.cloudapp.chooser.chooser.Images.CloudinaryClient;
import net.cloudapp.chooser.chooser.Animations.ImageSwitchFactory;
import net.cloudapp.chooser.chooser.Animations.TextSwitchFactory;
import net.cloudapp.chooser.chooser.R;
import net.cloudapp.chooser.chooser.model.Post;

public class FeedView extends AppCompatActivity implements View.OnClickListener {
    ImageButton flagButton;
    TextView titleTextView, description1TextView, description2TextView, tokens;
    ImageSwitcher imageSwitcher1, imageSwitcher2;
    TextSwitcher textSwitcher1, textSwitcher2;
    String mCurrentPostId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("Chooser", "FeedView activity loading");
        setContentView(R.layout.posts_view);
        initializeViewElements();
        initializeOnClickListeners();
        initializeControls();

        PostsFetchController postsFetchController = new PostsFetchController(this);
        postsFetchController.getPosts();
    }

    private void initializeControls() {
        imageSwitcher1.setFactory(new ImageSwitchFactory(this));
        imageSwitcher2.setFactory(new ImageSwitchFactory(this));
        textSwitcher1.setFactory(new TextSwitchFactory(this));
        textSwitcher2.setFactory(new TextSwitchFactory(this));
        tokens.setText(String.valueOf(-1));
    }

    public void refreshView(){
        Post post = PostRepository.postsFeed.poll();
        if(post == null){
            Log.d("Chooser", "Posts empty...");
            return;
        }
        Log.i("ChooserApp", "Loading post " + post.title);
        String url1 = CloudinaryClient.bigImageUrl(post.image1);
        String url2 = CloudinaryClient.bigImageUrl(post.image2);

        Glide.with(this).load(url1).into((ImageView) imageSwitcher1.getCurrentView());
        Glide.with(this).load(url2).into((ImageView) imageSwitcher2.getCurrentView());
        titleTextView.setText(post.title);
        description1TextView.setText(post.description1);
        description2TextView.setText(post.description2);
        mCurrentPostId = post._id;
    }

    private void initializeOnClickListeners() {
        //Set Click Listeners:
        imageSwitcher1.setOnClickListener(this);
        imageSwitcher2.setOnClickListener(this);
    }

    private void initializeViewElements() {
        flagButton = (ImageButton) findViewById(R.id.flagButton);

        titleTextView = (TextView) findViewById(R.id.titleTextView);
        description1TextView = (TextView) findViewById(R.id.description1TextView);
        description2TextView = (TextView) findViewById(R.id.description2TextView);
        tokens = (TextView) findViewById(R.id.tokens);
        imageSwitcher1 = (ImageSwitcher) findViewById(R.id.imageSwitcher1);
        imageSwitcher2 = (ImageSwitcher) findViewById(R.id.imageSwitcher2);
        textSwitcher1 = (TextSwitcher) findViewById(R.id.percentage1);
        textSwitcher2 = (TextSwitcher) findViewById(R.id.percentage2);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        Log.i("ChooserApp", "FeedView OnclickListener: " + v.getId());
        switch (v.getId()) {

            case R.id.imageSwitcher1:
                vote(1);
                break;

            case R.id.imageSwitcher2:
                vote(2);
                break;
        }
    }

    private void vote(int selected) {
        Log.d("Chooser", "Vote selection: " + selected);
        String postId = mCurrentPostId;
        VoteController voteController = new VoteController();
        voteController.vote(postId, selected);
        loadNextPost();
    }

    private void loadNextPost() {
        refreshView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent i;
        switch (item.getItemId()) {
            case R.id.add_poll:
            i = new Intent("net.cloudapp.chooser.chooser.AddPostView");
                startActivity(i);
                return true;
            case R.id.settings:
                i = new Intent("net.cloudapp.chooser.chooser.Settings");
                startActivity(i);
                return true;
            case R.id.logout:
                LoginManager.getInstance().logOut();
                finish();
                return true;
            case R.id.my_posts:
                i = new Intent("net.cloudapp.chooser.chooser.MyPostsView");
                startActivity(i);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        // prevents 'back' button from showing the login screen.
        moveTaskToBack(true);
    }
}
