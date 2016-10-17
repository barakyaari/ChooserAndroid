package net.cloudapp.chooser.chooser.views;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextSwitcher;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
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
    Button refreshButton;
    TextView titleTextView, description1TextView, description2TextView, tokens;
    ImageSwitcher imageSwitcher1, imageSwitcher2;
    TextSwitcher textSwitcher1, textSwitcher2;
    LinearLayout feedLayout, noPostsLayout;
    String mCurrentPostId;
    Boolean feedIsOn, animating;
    Post post;
    int votes1, votes2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("Chooser", "FeedView activity loading");
        setContentView(R.layout.posts_view);
        initializeViewElements();
        initializeOnClickListeners();
        initializeControls();
        feedIsOn = true;
        refreshFeedRepository();
    }

    private void initializeControls() {
        imageSwitcher1.setFactory(new ImageSwitchFactory(this));
        imageSwitcher2.setFactory(new ImageSwitchFactory(this));
        textSwitcher1.setFactory(new TextSwitchFactory(this));
        textSwitcher2.setFactory(new TextSwitchFactory(this));
        tokens.setText(String.valueOf(-1));
    }

//refresh view

    private void refreshFeedRepository() {
        PostsFetchController postsFetchController = new PostsFetchController(this);
        postsFetchController.getPosts();
    }

    private void initializeOnClickListeners() {
        //Set Click Listeners:
        imageSwitcher1.setOnClickListener(this);
        imageSwitcher2.setOnClickListener(this);
        refreshButton.setOnClickListener(this);
    }

    private void initializeViewElements() {
        noPostsLayout = (LinearLayout) findViewById(R.id.noPostsLayout);
        feedLayout = (LinearLayout) findViewById(R.id.feedLayout);

        flagButton = (ImageButton) findViewById(R.id.flagButton);
        refreshButton = (Button) findViewById(R.id.refreshButton);

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

            case R.id.refreshButton:
                refreshFeedRepository();
                break;
        }
    }

    private void vote(int selected) {
        Log.d("Chooser", "Vote selection: " + selected);
        String postId = mCurrentPostId;
        VoteController voteController = new VoteController();
        voteController.vote(postId, selected);

        if (selected == 1)
            votes1++;
        else
            votes2++;

        loadNextPost();
    }


    private void shutdownFeed() {
        feedLayout.setVisibility(View.INVISIBLE);
        flagButton.setVisibility(View.INVISIBLE);
        noPostsLayout.setVisibility(View.VISIBLE);
        feedIsOn = false;
    }

    private void turnOnFeed() {
        noPostsLayout.setVisibility(View.INVISIBLE);
        feedLayout.setVisibility(View.VISIBLE);
        flagButton.setVisibility(View.VISIBLE);
        feedIsOn = true;
    }

    private void loadNextPost() {

        //MAGIC

        loadPosts();
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


    public void refreshView(){
        Post post = PostRepository.postsFeed.poll();
        if (post == null) {
            Log.d("Chooser", "Posts empty...");
            if (feedIsOn)
                shutdownFeed();
            return;
        }

        if (!feedIsOn)
            turnOnFeed();

        Log.i("ChooserApp", "Loading post " + post.title);

        titleTextView.setText(post.title);
        String url1 = CloudinaryClient.bigImageUrl(post.image1);
        Glide.with(this).load(url1).into((ImageView) imageSwitcher1.getCurrentView());
        description1TextView.setText(post.description1);
        votes1 = post.votes1;


        String url2 = CloudinaryClient.bigImageUrl(post.image2);
        Glide.with(this).load(url2).into((ImageView) imageSwitcher2.getCurrentView());
        description2TextView.setText(post.description2);

        imageSwitcher2.setImageDrawable(new BitmapDrawable(getResources(), post.image2));

        votes2 = post.votes2;

        mCurrentPostId = post._id;
    }

    public void loadPosts() {
        post = PostRepository.postsFeed.poll();

        setTextAnimations(); //loads post after percentage appears on screen
        textSwitcher1.setText(String.valueOf(votes1));
        textSwitcher2.setText(String.valueOf(votes2));
    }


    private void setTextAnimations() {
        textSwitcher1.setVisibility(View.VISIBLE);
        textSwitcher2.setVisibility(View.VISIBLE);
        //not yet visible

        //set fade in animation
        Animation inAnimation1 = AnimationUtils.loadAnimation(this, android.R.anim.fade_in);
        Animation inAnimation2 = AnimationUtils.loadAnimation(this, android.R.anim.fade_in);

        //set fade in duration to 1 sec
        inAnimation1.setDuration(1000);
        inAnimation2.setDuration(1000);

        inAnimation1.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}

            @Override
            public void onAnimationRepeat(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) {
                //this runs when votes are on the screen
                if (post == null) {
                    Log.d("Chooser", "Posts empty...");
                    if (feedIsOn)
                        shutdownFeed();
                    return;
                } else {
                    titleTextView.setText(post.title);
                    description1TextView.setText(post.description1);
                    textSwitcher1.setVisibility(View.GONE);

                    String url1 = CloudinaryClient.bigImageUrl(post.image1);
                    Glide
                            .with(getApplicationContext())
                            .load(url1)
                            .animate(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_in_right))
                            .into((ImageView) imageSwitcher1.getNextView());

                    imageSwitcher1.setOutAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_out_left));

                    imageSwitcher1.showNext();
                    //shows new picture and texts on feed
                }
            }
        });

        inAnimation2.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                //to disable clicking while animating
                animating = true;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) {
                description2TextView.setText(post.description2);
                textSwitcher2.setVisibility(View.GONE);
                String url2 = CloudinaryClient.bigImageUrl(post.image2);
                Glide
                        .with(getApplicationContext())
                        .load(url2)
                        .animate(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_in_right))
                        .into((ImageView) imageSwitcher2.getNextView());
                imageSwitcher2.setOutAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_out_left));

                imageSwitcher2.showNext();
                animating = false;
            }
        });

        Animation hideText = new AlphaAnimation(0.0f, 0.0f);
        hideText.setDuration(1000);

        textSwitcher1.setInAnimation(inAnimation1);
        textSwitcher2.setInAnimation(inAnimation2);

        textSwitcher1.setOutAnimation(hideText);
        textSwitcher2.setOutAnimation(hideText);
    }

















    @Override
    public void onBackPressed() {
        // prevents 'back' button from showing the login screen.
        moveTaskToBack(true);
    }
}
