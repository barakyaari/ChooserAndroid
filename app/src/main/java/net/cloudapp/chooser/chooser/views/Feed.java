package net.cloudapp.chooser.chooser.views;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextSwitcher;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.facebook.login.LoginManager;

import net.cloudapp.chooser.chooser.Common.PostRepository;
import net.cloudapp.chooser.chooser.Controller.PostsController;
import net.cloudapp.chooser.chooser.Images.ImagePicker;
import net.cloudapp.chooser.chooser.Network.RestFramework.RestClient;
import net.cloudapp.chooser.chooser.Images.CloudinaryClient;
import net.cloudapp.chooser.chooser.Common.SessionDetails;
import net.cloudapp.chooser.chooser.Animations.ImageSwitchFactory;
import net.cloudapp.chooser.chooser.Animations.TextSwitchFactory;
import net.cloudapp.chooser.chooser.R;
import net.cloudapp.chooser.chooser.model.Post;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class Feed extends AppCompatActivity implements View.OnClickListener {
    Button buttonRight, refreshPostsButton, buttonLeft;
    ImageButton flagButton;
    TextView titleTextView, description1TextView, description2TextView, tokens;
    ImageSwitcher imageSwitcher1, imageSwitcher2;
    TextSwitcher textSwitcher1, textSwitcher2;
    Bitmap image1Bitmap, image2Bitmap;
    boolean animating;
    private SessionDetails sessionDetails;
    private List<Post> posts, myPosts;
    private SharedPreferences sharedPrefs;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private NavDrawerAdapter drawerAdapter;
    private ListView drawerList;
    private int currentPost = 0;
    private static final int PICK_IMAGE_ID = 234; // the number doesn't matter
    RestClient restClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("Chooser", "Feed activity loading");
        setContentView(R.layout.posts_view);
        initializeViewElements();
        initializeOnClickListeners();
        initializeControls();

        posts = new ArrayList();
        myPosts = new ArrayList();

        drawerAdapter = new NavDrawerAdapter(this, myPosts);
        drawerList.setAdapter(drawerAdapter);
        PostsController postsController = new PostsController(this);
        postsController.getPosts();
    }

    private void initializeControls() {
        imageSwitcher1.setFactory(new ImageSwitchFactory(this));
        imageSwitcher2.setFactory(new ImageSwitchFactory(this));
        textSwitcher1.setFactory(new TextSwitchFactory(this));
        textSwitcher2.setFactory(new TextSwitchFactory(this));

        tokens.setText(String.valueOf(-1));
        //init myPosts
        drawerLayout.addDrawerListener(drawerToggle);

        animating = false;
    }


    public void refreshView(){
        Post post = PostRepository.postsFeed.poll();
        Log.i("ChooserApp", "Loading post " + post.title);
        String url1 = CloudinaryClient.bigImageUrl(post.image1);
        String url2 = CloudinaryClient.bigImageUrl(post.image2);

        Glide.with(this).load(url1).into((ImageView) imageSwitcher1.getCurrentView());
        Glide.with(this).load(url2).into((ImageView) imageSwitcher2.getCurrentView());
        titleTextView.setText(post.title);
        description1TextView.setText(post.description1);
        description2TextView.setText(post.description2);
        setTextAnimations(); //loads post after percentage appears on screen
//            textSwitcher1.setText(String.valueOf(posts.get(prevPost).votes1));
//            textSwitcher2.setText(String.valueOf(posts.get(prevPost).votes2));
    }

    private void initializeOnClickListeners() {
        //Set Click Listeners:
        refreshPostsButton.setOnClickListener(this);
        buttonRight.setOnClickListener(this);
        buttonLeft.setOnClickListener(this);
        flagButton.setOnClickListener(this);
        imageSwitcher1.setOnClickListener(this);
        imageSwitcher2.setOnClickListener(this);
        imageSwitcher1.setOnLongClickListener(
                new Button.OnLongClickListener() {
                    public boolean onLongClick(View v) {
                        previewImage(1);
                        return true;
                    }
                }
        );

        imageSwitcher2.setOnLongClickListener(
                new Button.OnLongClickListener() {
                    public boolean onLongClick(View v) {
                        previewImage(2);
                        return true;
                    }
                }
        );
    }

    private void initializeViewElements() {
        buttonRight  = (Button) findViewById(R.id.buttonRight);
        buttonLeft = (Button) findViewById(R.id.buttonLeft);
        refreshPostsButton = (Button) findViewById(R.id.buttonRefreshPosts);
        flagButton = (ImageButton) findViewById(R.id.flagButton);

        titleTextView = (TextView) findViewById(R.id.titleTextView);
        description1TextView = (TextView) findViewById(R.id.description1TextView);
        description2TextView = (TextView) findViewById(R.id.description2TextView);
        tokens = (TextView) findViewById(R.id.tokens);
        imageSwitcher1 = (ImageSwitcher) findViewById(R.id.imageSwitcher1);
        imageSwitcher2 = (ImageSwitcher) findViewById(R.id.imageSwitcher2);
        textSwitcher1 = (TextSwitcher) findViewById(R.id.percentage1);
        textSwitcher2 = (TextSwitcher) findViewById(R.id.percentage2);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerList = (ListView) findViewById(R.id.left_drawer);
        drawerList.setBackgroundColor(Color.parseColor("#FF494949"));
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.drawer_open, R.string.drawer_close) {

            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle("Chooser");
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle("My Posts");
            }
        };

    }

    private void refresh() {
        Log.d("Chooser", "Refreshing posts");
        setPicAnimationPrev();
        posts.clear();

        restClient.getService().allPosts(
                SessionDetails.getInstance().getAccessToken().getToken(),
                new Callback<List<Post>>() {
            @Override
            public void success(List<Post> newPosts, Response response) {
                int code = response.getStatus();
                if (code == 200) {
                    Log.d("Chooser", "all posts OK.");

                    if (newPosts.size() > 0) {
                        posts.addAll(newPosts);
                        currentPost = 0;
                        Log.d("Chooser", "Recieved: " + posts.size() + " posts");
//                        loadPosts();
                    }
                    else{
                        Log.d("Chooser", "Got 0 posts.");
                    }
                } else {
                    Log.e("Chooser", "all posts - bad response code.");
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("Chooser", "all posts - Callback failure");
                error.printStackTrace();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
//        refresh();
//        getTokenCount();
//        refreshMyPosts();
//        tokens.setText(String.valueOf(sessionDetails.userTokenCount));
//        sessionDetails.updateSharedPrefs(sharedPrefs);
    }

    private void previewImage(int imageNumber) {
        final Dialog nagDialog = new Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        nagDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        nagDialog.setCancelable(false);
        nagDialog.setContentView(R.layout.preview_image);
        ImageView ivPreview = (ImageView) nagDialog.findViewById(R.id.iv_preview_image);
        switch (imageNumber) {
            case 1:
                ivPreview.setImageBitmap(image1Bitmap);
                break;
            case 2:
                ivPreview.setImageBitmap(image2Bitmap);
                break;
        }
        ivPreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                nagDialog.dismiss();
            }
        });
        nagDialog.show();
    }

    private void setTextAnimations() {
        textSwitcher1.setVisibility(View.VISIBLE);
        textSwitcher2.setVisibility(View.VISIBLE);

        Animation inAnimation1 = AnimationUtils.loadAnimation(this, android.R.anim.fade_in);
        Animation inAnimation2 = AnimationUtils.loadAnimation(this, android.R.anim.fade_in);
        inAnimation1.setDuration(1000);
        inAnimation2.setDuration(1000);

        inAnimation1.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Post post = posts.get(currentPost);
                titleTextView.setText((String)post.title);
                description1TextView.setText((String)post.description1);
                textSwitcher1.setVisibility(View.GONE);
                Glide.with(getApplicationContext()).load(CloudinaryClient.bigImageUrl(post.image1)).into((ImageView) imageSwitcher1.getCurrentView());
            }
        });

        inAnimation2.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                animating = true;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
//                PostObject post = posts.get(currentPost);
//                description2TextView.setText(post.getDescription1());
//                textSwitcher2.setVisibility(View.GONE);
//                imageSwitcher2.setImageDrawable(new BitmapDrawable(getResources(), post));
//                animating = false;
            }
        });

        Animation hideText = new AlphaAnimation(0.0f, 0.0f);
        hideText.setDuration(1000);
        textSwitcher1.setInAnimation(inAnimation1);
        textSwitcher2.setInAnimation(inAnimation2);

        textSwitcher1.setOutAnimation(hideText);
        textSwitcher2.setOutAnimation(hideText);
    }

    private void setPicAnimationPrev() {
        imageSwitcher1.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_in_left));
        imageSwitcher1.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_out_right));
        imageSwitcher2.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_in_left));
        imageSwitcher2.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_out_right));
    }

    private void setPicAnimationNext() {
        imageSwitcher1.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_in_right));
        imageSwitcher1.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_out_left));
        imageSwitcher2.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_in_right));
        imageSwitcher2.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_out_left));
    }

    @Override
    public void onClick(View v) {
        if (animating)
            return;
        Log.i("ChooserApp", "Feed OnclickListener: " + v.getId());
        switch (v.getId()) {

            case R.id.buttonRefreshPosts:
                refresh();
                //refreshMyPosts();
                break;

//            case R.id.buttonLeft:
//                prevPic();
//                break;
//
//            case R.id.buttonRight:
//                skipPic();
//                break;
//
//            case R.id.imageSwitcher1:
//                // vote(1);
//                nextPic();
//                break;
//
//            case R.id.imageSwitcher2:
//                // vote(2);
//                nextPic();
//                break;
        }
    }
//
//    private void skipPic() {
//        if (currentPost < posts.size() - 1) {
//            currentPost++;
//            setPicAnimationNext();
//            loadPosts();
//        } else
//            loadPosts();
//    }
//
//    private void nextPic() {
//        if (currentPost < posts.size() - 1) {
//            currentPost++;
//            setPicAnimationNext();
//            loadPosts();
//        } else
//            loadPosts();
//    }
//
//
//    private void prevPic() {
//        if (currentPost > 0) {
//            currentPost--;
//            setPicAnimationPrev();
//            loadPosts();
//        } else
//            loadPosts();
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case PICK_IMAGE_ID:
                Bitmap bitmap = ImagePicker.getImageFromResult(this, resultCode, data);
                // TODO use bitmap
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
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
                i = new Intent("net.cloudapp.chooser.chooser.Views.AddPost");
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
