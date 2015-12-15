package net.cloudapp.chooser.chooser;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.JsonReader;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    TextView TitleTextView;
    TextView Description1TextView;
    TextView Description2TextView;
    ImageView Image1;
    ImageView Image2;
    Bitmap image1Bitmap, image2Bitmap;
    private SessionDetails sessionDetails;

    private List<Post> posts;
    private int currentPost = 0;
    private static final int PICK_IMAGE_ID = 234; // the number doesn't matter

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.posts_view);
        sessionDetails = (SessionDetails) getIntent().getSerializableExtra("SessionDetails");

        TitleTextView = (TextView) findViewById(R.id.titleTextView);
        Description1TextView = (TextView) findViewById(R.id.description1TextView);
        Description2TextView = (TextView) findViewById(R.id.description2TextView);
        Button RefreshButton = (Button) findViewById(R.id.addPostButton);
        Image1 = (ImageView) findViewById(R.id.image1ImageView);
        Image2 = (ImageView) findViewById(R.id.image2ImageView);
        Button buttonRight = (Button) findViewById(R.id.buttonRight);
        Button buttonLeft = (Button) findViewById(R.id.buttonLeft);
        Button addPostButton = (Button) findViewById(R.id.addPostButton);

        posts = new ArrayList<Post>();
        RefreshButton.setOnClickListener(this);
        buttonRight.setOnClickListener(this);
        buttonLeft.setOnClickListener(this);
        Image1.setOnClickListener(this);
        Image2.setOnClickListener(this);
        refresh();

    }
/*
        @Override
        protected void onPostExecute(Void aVoid) {
            try {
                JSONArray jArray = new JSONArray(result);
                Log.i("ChooserApp", "Number of Posts found: " + jArray.length());
                for (int i = 0; i < jArray.length(); i++) {
                    Log.i("ChooserApp", "Creating post - Iteration: " + i);
                    JSONObject jObject = jArray.getJSONObject(i);
                    String title = jObject.getString("title");
                    String image1 = jObject.getString("image1");
                    String description1 = jObject.getString("description1");
                    String image2 = jObject.getString("image2");
                    String description2 = jObject.getString("description2");
                    int id = jObject.getInt("id");
                    Log.i("chooserHTTP", title);

                    //Convert Images:
                    byte[] decodedString = Base64.decode(image1, Base64.DEFAULT);
                    Bitmap image1Bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                    decodedString = Base64.decode(image2, Base64.DEFAULT);
                    Bitmap image2Bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                    Post newPost = new Post(title, image1Bitmap, description1, image2Bitmap, description2, id);
                    PostsList.add(newPost);

                }
                loadPosts(currentPost);
            }

            catch (Exception e){
                e.printStackTrace();
            }
        }

        private List<Post> getPosts(){



            String url = "http://chooser.cloudapp.net:8080/getAllPosts";
            String charset = "UTF-8";
            String param1 = "value1";
            String param2 = "value2";

            String query = null;
            try {
                query = String.format("param1=%s&param2=%s",
                        URLEncoder.encode(param1, charset),
                        URLEncoder.encode(param2, charset));
                Log.i("ChooserApp", "Sendig Http Request to: " + url + " query is: " + query);
                URLConnection urlConnection = new URL(url + "?" + query).openConnection();
                urlConnection.setRequestProperty("Accept-Charset", charset);
                InputStream response = urlConnection.getInputStream();
                String responseText = response.toString();
                Log.i("ChooserApp", responseText);

                BufferedReader bReader = new BufferedReader(new InputStreamReader(response, "utf-8"), 8);
                StringBuilder sBuilder = new StringBuilder();

                String line = null;
                while ((line = bReader.readLine()) != null) {
                    sBuilder.append(line + "\n");
                }
                response.close();
                result = sBuilder.toString();
                JSONArray jArray = new JSONArray(result);
                Log.i("ChooserApp", "Number of Posts found: " + jArray.length());
                for (int i = 0; i < jArray.length(); i++) {
                    Log.i("ChooserApp", "Creating post - Iteration: " + i);
                    JSONObject jObject = jArray.getJSONObject(i);
                    String title = jObject.getString("title");
                    String image1 = jObject.getString("image1");
                    String description1 = jObject.getString("description1");
                    String image2 = jObject.getString("image2");
                    String description2 = jObject.getString("description2");
                    int id = jObject.getInt("id");
                    Log.i("chooserHTTP", title);

                    //Convert Images:
                    byte[] decodedString = Base64.decode(image1, Base64.DEFAULT);
                    Bitmap image1Bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                    decodedString = Base64.decode(image2, Base64.DEFAULT);
                    Bitmap image2Bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);


                    Post newPost = new Post(title, image1Bitmap, description1, image2Bitmap, description2, id);
                    PostsList.add(newPost);

                }
                loadPosts(currentPost);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;

        }
    }
    */

    private void refresh(){
        Runnable doAtFinish = new Runnable() {
            @Override
            public void run() {
                posts.clear();
                String responseText = sessionDetails.responseString;
                try {
                    JSONArray jArray = new JSONArray(responseText);
                    Log.i("ChooserApp", "Number of Posts found: " + jArray.length());
                    for (int i = 0; i < jArray.length(); i++) {
                        Log.i("ChooserApp", "Creating post - Iteration: " + i);
                        JSONObject jObject = jArray.getJSONObject(i);
                        String title = jObject.getString("title");
                        String image1 = jObject.getString("image1");
                        String description1 = jObject.getString("description1");
                        String image2 = jObject.getString("image2");
                        String description2 = jObject.getString("description2");
                        int id = jObject.getInt("id");
                        Log.i("chooserHTTP", title);

                        //Convert Images:
                        Bitmap image1Bitmap = null;
                        Bitmap image2Bitmap = null;
                        try {
                            byte[] decodedString = Base64.decode(image1, Base64.DEFAULT);
                            image1Bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                            decodedString = Base64.decode(image2, Base64.DEFAULT);
                            image2Bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }
                        Post newPost = new Post(title, image1Bitmap, description1, image2Bitmap, description2, id);
                        posts.add(newPost);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if(posts.size()>0)
                    loadPosts(0);
            }
        };
        ConnectionManager connectionManager = new ConnectionManager(sessionDetails);
        connectionManager.GetPosts(doAtFinish);
    }

    private void previewImage(int imageNumber){
        final Dialog nagDialog = new Dialog(this, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        nagDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        nagDialog.setCancelable(false);
        nagDialog.setContentView(R.layout.preview_image);
        Button btnClose = (Button)nagDialog.findViewById(R.id.btnIvClose);
        ImageView ivPreview = (ImageView)nagDialog.findViewById(R.id.iv_preview_image);
        switch (imageNumber){
            case 1:
                ivPreview.setImageBitmap(image1Bitmap);
                break;
            case 2:
                ivPreview.setImageBitmap(image2Bitmap);
                break;
        }
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                nagDialog.dismiss();
            }
        });
        nagDialog.show();
    }

    @Override
    public void onClick(View v) {
        Log.i("ChooserApp", "MainActivity OnclickListener: " + v.getId());
        switch(v.getId()){
            case R.id.addPostButton:{
                Intent i = new Intent("net.cloudapp.chooser.chooser.AddPost");
                startActivity(i);
            }
            break;

            case R.id.buttonLoad:{
                loadPosts(currentPost);
            }
            break;

            case R.id.buttonLeft:{
                if(currentPost>0){
                    currentPost--;
                    loadPosts(currentPost);
                }
            }
            break;

            case R.id.buttonRight:{
                if(currentPost<posts.size()-1){
                    currentPost++;
                    loadPosts(currentPost);
                }
            }
            break;

            case R.id.image1ImageView:{
                previewImage(1);
            }
            break;

            case R.id.image2ImageView:{
                previewImage(2);
            }
            break;
            }


        }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode) {
            case PICK_IMAGE_ID:
                Bitmap bitmap = ImagePicker.getImageFromResult(this, resultCode, data);
                // TODO use bitmap
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }
    private void loadPosts(int postNumber) {
        if(posts.isEmpty()){
            Log.i("ChooserApp", "Empty posts...");
            return;
        }
        Post post = posts.get(postNumber);
        Log.i("ChooserApp", "Loading current post: " + postNumber + " Title: " + post.title);
        TitleTextView.setText(post.title);
        Description1TextView.setText(post.description1);
        Description2TextView.setText(post.description2);
        image1Bitmap = post.image1;
        image2Bitmap = post.image2;
        Image1.setImageBitmap(post.image1);
        Image2.setImageBitmap(post.image2);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
