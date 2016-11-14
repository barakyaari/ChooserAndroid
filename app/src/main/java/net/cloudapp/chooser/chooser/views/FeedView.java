package net.cloudapp.chooser.chooser.views;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextSwitcher;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.facebook.login.LoginManager;

import net.cloudapp.chooser.chooser.Common.LoadingDialogs;
import net.cloudapp.chooser.chooser.Common.PostRepository;
import net.cloudapp.chooser.chooser.Common.SessionDetails;
import net.cloudapp.chooser.chooser.Controller.Callbacks.GetTokensCallback;
import net.cloudapp.chooser.chooser.Controller.GetTokensController;
import net.cloudapp.chooser.chooser.Controller.PostsFetchController;
import net.cloudapp.chooser.chooser.Controller.ReportController;
import net.cloudapp.chooser.chooser.Controller.PromotionController;
import net.cloudapp.chooser.chooser.Controller.VoteController;
import net.cloudapp.chooser.chooser.Images.CloudinaryClient;
import net.cloudapp.chooser.chooser.Animations.ImageSwitchFactory;
import net.cloudapp.chooser.chooser.Animations.TextSwitchFactory;
import net.cloudapp.chooser.chooser.R;
import net.cloudapp.chooser.chooser.model.Post;

public class FeedView extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener {
    Button refreshButton;
    TextView titleTextView, description1TextView, description2TextView, tokens;
    ImageSwitcher imageSwitcher1, imageSwitcher2;
    ImageView flagButton;
    TextSwitcher textSwitcher1, textSwitcher2;
    LinearLayout feedLayout, noPostsLayout;
    String mCurrentPostId;
    Boolean feedIsOn;
    public  boolean animating1, animating2;
    Post post;
    int votes1, votes2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("Chooser", "FeedView activity loading");
        LoadingDialogs.addLoadingDialog(this,"feed","Searching for posts\nPlease wait");
        setContentView(R.layout.posts_view);
        initializeViewElements();
        initializeOnClickListeners();
        initializeOnLongClickListeners();
        initializeControls();
        shutdownFeed();
        setTextAnimations();
    }

    private void initializeControls() {
        imageSwitcher1.setFactory(new ImageSwitchFactory(this));
        imageSwitcher2.setFactory(new ImageSwitchFactory(this));
        textSwitcher1.setFactory(new TextSwitchFactory(this));
        textSwitcher2.setFactory(new TextSwitchFactory(this));
    }

    public void updateTokens() {
        tokens.setText(String.valueOf(SessionDetails.getInstance().numOfTokens));
    }

    public void addToken() {
        SessionDetails.getInstance().numOfTokens++;
    }

    private void refreshFeedRepository() {
        LoadingDialogs.show("feed");
        PostsFetchController postsFetchController = new PostsFetchController(this);
        postsFetchController.getPosts();
    }

    private void initializeOnClickListeners() {
        //Set Click Listeners:
        imageSwitcher1.setOnClickListener(this);
        imageSwitcher2.setOnClickListener(this);
        refreshButton.setOnClickListener(this);
        flagButton.setOnClickListener(this);
    }

    private void initializeOnLongClickListeners() {
        imageSwitcher1.setOnLongClickListener(this);
        imageSwitcher2.setOnLongClickListener(this);
    }

    private void initializeViewElements() {
        noPostsLayout = (LinearLayout) findViewById(R.id.noPostsLayout);
        feedLayout = (LinearLayout) findViewById(R.id.feedLayout);

        flagButton = (ImageView) findViewById(R.id.flagButton);
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

        if (!feedIsOn)
            refreshFeedRepository();

        getTokens();
    }

    @Override
    public void onClick(View v) {
        if (animating1 || animating2)
            return;
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

            case R.id.flagButton:
                reportPost();
                break;
        }
    }

    @Override
    public boolean onLongClick(View v) {
        if (animating1 || animating2)
            return false;
        Log.i("ChooserApp", "FeedView OnLongClickListener: " + v.getId());
        switch (v.getId()) {
            case R.id.imageSwitcher1:
                showFullScreen(1);
                break;

            case R.id.imageSwitcher2:
                showFullScreen(2);
                break;
        }
        return false;
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

    private void getTokens() {
        GetTokensController tokensController = new GetTokensController();
        tokensController.getTokens(this);
    }

    private void vote(int selected) {
        Log.d("Chooser", "Vote selection: " + selected);
        VoteController voteController = new VoteController();
        voteController.vote(mCurrentPostId, selected);
        addToken();
        if (selected == 1)
            votes1++;
        else if (selected == 2)
            votes2++;
        loadNextPost();
    }

    private void reportPost() {
        Log.d("Chooser", "Report post requested");
        feedIsOn = false;
        ReportController reportController = new ReportController();
        reportController.report(mCurrentPostId);
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


    public void loadNextPost() {
        post = PostRepository.postsFeed.poll();

        if (feedIsOn) {
            textSwitcher1.setVisibility(View.VISIBLE);
            textSwitcher2.setVisibility(View.VISIBLE);
            textSwitcher1.setText(String.valueOf(votes1));
            textSwitcher2.setText(String.valueOf(votes2));
            return;
        }
        if (post == null) {
            //runs only when using 'refresh' and there are no posts or when logging in and there are no posts.
            Log.d("Chooser", "Posts empty...");
            return;
        }

        turnOnFeed();
        extractGeneralData();
        extractPostData1();
        extractPostData2();
    }


    private void setTextAnimations() {
        textSwitcher1.setInAnimation(TextSwitchFactory.getAnimation(1,this));
        textSwitcher2.setInAnimation(TextSwitchFactory.getAnimation(2,this));
    }

    public void onAnimationEnd1() {
        textSwitcher1.setVisibility(View.GONE);
        if (post == null) {
            //runs only when there are no more posts to show
            Log.d("Chooser", "Posts empty...");
            if (feedIsOn)
                shutdownFeed();
            return;
        }
        extractGeneralData();
        extractPostData1();
    }

    public void onAnimationEnd2() {
        textSwitcher2.setVisibility(View.GONE);
        if (post == null)
            return;

        extractPostData2();
    }


    private void extractGeneralData() {
        Log.i("ChooserApp", "Loading post " + post.title);
        mCurrentPostId = post._id;
        titleTextView.setText(post.title);
        updateTokens();
    }

    private void extractPostData1 () {
        String url1 = CloudinaryClient.bigImageUrl(post.image1,true);
        Glide
                .with(getApplicationContext())
                .load(url1)
                .animate(R.anim.slide_in_right)
                .into((ImageView) imageSwitcher1.getNextView());

        imageSwitcher1.setOutAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_out_left));
        description1TextView.setText(post.description1);
        votes1 = post.votes1;
        imageSwitcher1.showNext();
    }

    private void extractPostData2 () {
        String url2 = CloudinaryClient.bigImageUrl(post.image2,true);
        Glide
                .with(getApplicationContext())
                .load(url2)
                .animate(R.anim.slide_in_right)
                .into((ImageView) imageSwitcher2.getNextView());
        imageSwitcher2.setOutAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_out_left));
        description2TextView.setText(post.description2);
        votes2 = post.votes2;
        imageSwitcher2.showNext();
    }


    @Override
    public void onBackPressed() {
        // prevents 'back' button from showing the login screen.
        moveTaskToBack(true);
    }

}
