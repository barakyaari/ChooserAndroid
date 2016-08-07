package net.cloudapp.chooser.chooser;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.app.ActionBar.LayoutParams;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher.ViewFactory;
import com.facebook.login.LoginManager;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
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
    private int prevPost; // needed for the text animation, (for tests purposes)
    private static final int PICK_IMAGE_ID = 234; // the number doesn't matter
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.posts_view);
        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        sessionDetails = (SessionDetails) getIntent().getSerializableExtra("SessionDetails");
        sessionDetails.updateSharedPrefs(sharedPrefs);

        //Controls initialization:
        titleTextView = (TextView) findViewById(R.id.titleTextView);
        description1TextView = (TextView) findViewById(R.id.description1TextView);
        description2TextView = (TextView) findViewById(R.id.description2TextView);
        tokens = (TextView) findViewById(R.id.tokens);
        imageSwitcher1 = (ImageSwitcher) findViewById(R.id.imageSwitcher1);
        imageSwitcher2 = (ImageSwitcher) findViewById(R.id.imageSwitcher2);
        imageSwitcher1.setFactory(new ImageSwitchFactory());
        imageSwitcher2.setFactory(new ImageSwitchFactory());
        textSwitcher1 = (TextSwitcher) findViewById(R.id.percentage1);
        textSwitcher2 = (TextSwitcher) findViewById(R.id.percentage2);

        textSwitcher1.setFactory(new TextSwitchFactory());
        textSwitcher2.setFactory(new TextSwitchFactory());

        tokens.setText(String.valueOf(sessionDetails.userTokenCount));
        //init myPosts
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerList = (ListView) findViewById(R.id.left_drawer);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.drawer_open, R.string.drawer_close){

            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle("Chooser");
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle("My Posts");
            }
        };

        drawerLayout.addDrawerListener(drawerToggle);

        Button buttonRight = (Button) findViewById(R.id.buttonRight);
        Button buttonLeft = (Button) findViewById(R.id.buttonLeft);
        Button refreshPostsButton = (Button) findViewById(R.id.buttonRefreshPosts);
        ImageButton flagButton = (ImageButton) findViewById(R.id.flagButton);

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
        animating = false;
        //Load Posts:
        posts = new ArrayList();
        myPosts = new ArrayList();
        drawerAdapter = new NavDrawerAdapter(this, myPosts);
        drawerList.setAdapter(drawerAdapter);
        drawerList.setOnItemClickListener(new DrawerItemClickListener());
        drawerList.setOnItemLongClickListener(new DrawerItemClickListener());

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private class ImageSwitchFactory implements ViewFactory {
        @Override
        public View makeView() {
            ImageView image = new ImageView(getApplicationContext());
            ImageSwitcher.LayoutParams lp = new ImageSwitcher.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
            image.setScaleType(ImageView.ScaleType.FIT_CENTER); //this doesn't work for some reason.
            image.setLayoutParams(lp);
            //image.setBackgroundResource(R.drawable.imageoutline);
            return image;
        }
    }

    private class TextSwitchFactory implements ViewFactory {
        @Override
        public View makeView() {
            TextWithStroke text = new TextWithStroke(MainActivity.this);
            text.setTypeface(null, Typeface.BOLD);
            text.setGravity(Gravity.CENTER);
            text.setTextSize(50);
            ImageSwitcher.LayoutParams lp = new ImageSwitcher.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
            text.setLayoutParams(lp);
            text.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/CooperBlackStd.otf"));
            text.setTextColor(Color.parseColor("#dddddd"));  //Light gray
            text.setStrokeSize(15);
            text.setStrokeColor(Color.BLACK);
            return text;
        }
    }

    private class TextWithStroke extends TextView {
        public TextWithStroke(Context context) {
            super(context);
        }
        private float strokeWidth;
        private int strokeColor;
        @Override
        protected void onDraw(Canvas pCanvas) {
            int textColor = getTextColors().getDefaultColor();
            setTextColor(strokeColor);
            getPaint().setStrokeWidth(strokeWidth);
            getPaint().setStyle(Paint.Style.STROKE);
            super.onDraw(pCanvas);
            setTextColor(textColor);
            getPaint().setStrokeWidth(0);
            getPaint().setStyle(Paint.Style.FILL);
            super.onDraw(pCanvas);
        }
        public void setStrokeSize (float strokeWidth) {this.strokeWidth = strokeWidth;}
        public void setStrokeColor (float strokeColor) {this.strokeColor = (int)strokeColor;}
    }

    private void refreshMyPosts() {
        Runnable doAtFinish = new Runnable() {
            @Override
            public void run() {
                myPosts.clear();
                String responseText = sessionDetails.responseString;
                sessionDetails.responseString = "";
                try {
                    JSONArray jArray = new JSONArray(responseText);
                    json2posts(jArray, true);
                    drawerAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        ConnectionManager connectionManager = new ConnectionManager(sessionDetails);
        connectionManager.getMyPosts(doAtFinish);
    }

    private void refresh() {
        setPicAnimationPrev();
        Runnable doAtFinish = new Runnable() {
            @Override
            public void run() {
                posts.clear();
                String responseText = sessionDetails.responseString;

                sessionDetails.responseString = "";
                try {
                    JSONArray jArray = new JSONArray(responseText);
                    json2posts(jArray, false);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (posts.size() > 0) {
                    currentPost = 0;
                    loadPosts(true);
                }
            }
        };
        ConnectionManager connectionManager = new ConnectionManager(sessionDetails);
        connectionManager.GetPosts(doAtFinish);
    }

    private void getTokenCount() {
        Runnable doAtFinish = new Runnable() {
            @Override
            public void run() {
                if (!sessionDetails.responseString.equals("-1")) {
                    sessionDetails.userTokenCount = Integer.valueOf(sessionDetails.responseString);
                    tokens.setText(sessionDetails.responseString);
                    sessionDetails.responseString = "";
                }
            }
        };
        ConnectionManager connectionManager = new ConnectionManager(sessionDetails);
        connectionManager.getTokenCount(sessionDetails.userId, doAtFinish);
    }

    private void reportPost () {
        Runnable doAtFinish = new Runnable() {
            @Override
            public void run() {
                boolean responseText = Boolean.parseBoolean(sessionDetails.responseString);
                sessionDetails.responseString = "";
                if (responseText) {
                    Toast.makeText(getApplicationContext(), "Report Received!", Toast.LENGTH_LONG).show();
                    nextPic();
                }

                else
                    Toast.makeText(getApplicationContext(), "Report failed!", Toast.LENGTH_LONG).show();
            }
        };
        ConnectionManager connectionManager = new ConnectionManager(sessionDetails);
        connectionManager.reportPost(posts.get(currentPost).id,doAtFinish);
    }

    @Override
    protected void onResume() {
        super.onResume();
        refresh();
        getTokenCount();
        refreshMyPosts();
        tokens.setText(String.valueOf(sessionDetails.userTokenCount));
        sessionDetails.updateSharedPrefs(sharedPrefs);
    }

    private void json2posts(JSONArray jArray, boolean isMyPosts) throws JSONException {
        Log.i("ChooserApp", "Number of Posts found: " + jArray.length());
        for (int i = 0; i < jArray.length(); i++) {
            Log.i("ChooserApp", "Creating post - Iteration: " + i);
            JSONObject jObject = jArray.getJSONObject(i);
            String title = jObject.getString("title");
            String image1 = jObject.getString("image1");
            String description1 = jObject.getString("description1");
            String image2 = jObject.getString("image2");
            String description2 = jObject.getString("description2");
            String id = jObject.getString("id");
            int votes1 = Integer.parseInt(jObject.getString("votes1"));
            int votes2 = Integer.parseInt(jObject.getString("votes2"));

            Log.i("chooserHTTP", title);

            Post newPost = new Post(title, Post.string2Bitmap(image1), description1, Post.string2Bitmap(image2), description2, id, votes1, votes2);
            if (isMyPosts) {
                newPost.setDate(jObject.getString("date"));
                myPosts.add(newPost);
            } else
                posts.add(newPost);
        }
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

    private void vote(final int vote) {
        posts.get(currentPost).addVote(vote);
        Runnable doAtFinish = new Runnable() {
            @Override
            public void run() {
                String responseText = sessionDetails.responseString;
                sessionDetails.responseString = "";
                if (Integer.valueOf(responseText) != -1) {
                    Toast.makeText(getApplicationContext(), "Vote completed", Toast.LENGTH_LONG).show();
                    tokens.setText(responseText);
                    sessionDetails.userTokenCount = Integer.valueOf(responseText);
                }
            }
        };
        ConnectionManager connectionManager = new ConnectionManager(sessionDetails);
        connectionManager.vote(String.valueOf(vote), posts.get(currentPost).id, doAtFinish);
    }


    private void loadPosts(boolean hideResults) {
        if (posts.isEmpty()) {
            Log.i("ChooserApp", "Empty posts...");
            return;
        }
        Post post = posts.get(currentPost);
        image1Bitmap = post.image1;
        image2Bitmap = post.image2;
        Log.i("ChooserApp", "Loading current post: " + currentPost + " Title: " + post.title);
        if (hideResults) {
            imageSwitcher1.setImageDrawable(new BitmapDrawable(getResources(), post.image1));
            imageSwitcher2.setImageDrawable(new BitmapDrawable(getResources(), post.image2));
            titleTextView.setText(post.title);
            description1TextView.setText(post.description1);
            description2TextView.setText(post.description2);
        } else {
            setTextAnimations(); //loads post after percentage appears on screen
            if (sessionDetails.usePercentage) {
                textSwitcher1.setText(String.valueOf(posts.get(prevPost).getPercentage(1)) + "%");
                textSwitcher2.setText(String.valueOf(posts.get(prevPost).getPercentage(2)) + "%");
            } else {
                textSwitcher1.setText(String.valueOf(posts.get(prevPost).votes1));
                textSwitcher2.setText(String.valueOf(posts.get(prevPost).votes2));
            }
        }
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
            public void onAnimationStart(Animation animation) {}

            @Override
            public void onAnimationRepeat(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) {
                Post post = posts.get(currentPost);
                titleTextView.setText(post.title);
                description1TextView.setText(post.description1);
                textSwitcher1.setVisibility(View.GONE);
                imageSwitcher1.setImageDrawable(new BitmapDrawable(getResources(), post.image1));
            }
        });

        inAnimation2.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                animating = true;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) {
                Post post = posts.get(currentPost);
                description2TextView.setText(post.description2);
                textSwitcher2.setVisibility(View.GONE);
                imageSwitcher2.setImageDrawable(new BitmapDrawable(getResources(), post.image2));
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
        Log.i("ChooserApp", "MainActivity OnclickListener: " + v.getId());
        switch (v.getId()) {

            case R.id.buttonRefreshPosts:
                refresh();
                refreshMyPosts();
                break;

            case R.id.flagButton:
                reportPost();
                break;

            case R.id.buttonLeft:
                prevPic();
                break;

            case R.id.buttonRight:
                skipPic();
                break;

            case R.id.imageSwitcher1:
                vote(1);
                nextPic();
                break;

            case R.id.imageSwitcher2:
                vote(2);
                nextPic();
                break;
        }
    }

    private void skipPic() {
        prevPost = currentPost;
        if (currentPost < posts.size() - 1) {
            currentPost++;
            setPicAnimationNext();
            loadPosts(sessionDetails.skipResults);
        } else
            loadPosts(sessionDetails.skipResults);
    }

    private void nextPic() {
        prevPost = currentPost;
        if (currentPost < posts.size() - 1) {
            currentPost++;
            setPicAnimationNext();
            loadPosts(false);
        } else
            loadPosts(false);
    }



    private void prevPic() {
        prevPost = currentPost;
        if (currentPost > 0) {
            currentPost--;
            setPicAnimationPrev();
            loadPosts(false);
        }
        else
            loadPosts(false);
    }



    private class DrawerItemClickListener implements ListView.OnItemClickListener, ListView.OnItemLongClickListener {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            /*
            // Create a new fragment and specify the post to show based on position
            Fragment fragment = new PostClass();
            Bundle args = new Bundle();
            args.putInt(PostClass.ARG_POST_NUMBER, position);
            fragment.setArguments(args);
            // Insert the fragment by replacing any existing fragment
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

            // Highlight the selected item, update the title, and close the drawer

            */
            Toast.makeText(getApplicationContext(), "Post " + position + " Selected!", Toast.LENGTH_LONG).show();
            sessionDetails.post = new PostSerializable(myPosts.get(position));
            Intent i = new Intent("net.cloudapp.chooser.chooser.Statistics");
            i.putExtra("SessionDetails", sessionDetails);
            startActivity(i);

            drawerList.setItemChecked(position, true);
            setTitle(posts.get(position).title);
            drawerLayout.closeDrawer(drawerList);
        }

        public boolean onItemLongClick(AdapterView parent, View view, int position, long id) {
            //Add confirmation screen for deleting a post.
            Toast.makeText(getApplicationContext(), "TODO: Post " + position + " Will be deleted!", Toast.LENGTH_LONG).show();
            return true;
        }
    }


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
                i = new Intent("net.cloudapp.chooser.chooser.AddPost");
                i.putExtra("SessionDetails", sessionDetails);
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
        moveTaskToBack(true);
        //blocks 'back' button from showing the login screen.
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://net.cloudapp.chooser.chooser/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }



    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://net.cloudapp.chooser.chooser/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }


    private void deleteMyPost(){
        Runnable doAtFinish = new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), "Post Deleted!", Toast.LENGTH_LONG).show();
                refresh();
            }
        };
        ConnectionManager connectionManager = new ConnectionManager(sessionDetails);
        connectionManager.deletePost(posts.get(currentPost).id, doAtFinish);
    }


}
