package net.cloudapp.chooser.chooser.views;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import net.cloudapp.chooser.chooser.Common.SessionDetails;
import net.cloudapp.chooser.chooser.Controller.Callbacks.PromoteCallback;
import net.cloudapp.chooser.chooser.Controller.PostsUploadController;
import net.cloudapp.chooser.chooser.Images.ImagePicker;
import net.cloudapp.chooser.chooser.Images.CloudinaryClient;
import net.cloudapp.chooser.chooser.R;


public class AddPostView extends AppCompatActivity implements View.OnClickListener {
    private static final int SELECT_PHOTO = 100;
    private int selectedImage = 0;
    CheckBox promotionCheckbox;
    Button buttonAddPost, buttonCancel;
    EditText editTextTitle, editTextDescription1, editTextDescription2;
    ImageView image1, image2;
    TextView tokens;
    Bitmap image1BitMap, image2BitMap;
    public boolean promote;
    public boolean desc1edited, desc2edited;
    CloudinaryClient cloudinaryClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_post);
        setViewControls();
        setOnClickListeners();
        cloudinaryClient = new CloudinaryClient();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        updateTokens();
        promote = false;
        desc1edited = false;
        desc2edited = false;
    }

    private void setOnClickListeners() {
        buttonAddPost.setOnClickListener(this);
        buttonCancel.setOnClickListener(this);
        image1.setOnClickListener(this);
        image2.setOnClickListener(this);
        promotionCheckbox.setOnClickListener(this);
        editTextDescription1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus && !desc1edited){
                    desc1edited = true;
                    editTextDescription1.setText("");
                }
            }
        });

        editTextDescription2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus && !desc2edited){
                    desc2edited = true;
                    editTextDescription2.setText("");
                }
            }
        });
    }

    private void setViewControls() {
        tokens = (TextView) findViewById(R.id.tokens);
        image1 = (ImageView) findViewById(R.id.addPostImageView1);
        image2 = (ImageView) findViewById(R.id.addPostImageView2);
        editTextTitle = (EditText) findViewById(R.id.postTitleEditText);
        editTextDescription1 = (EditText) findViewById(R.id.description1EditText);
        editTextDescription2 = (EditText) findViewById(R.id.description2EditText);
        buttonAddPost = (Button) findViewById(R.id.postButton);
        buttonCancel = (Button) findViewById(R.id.cancelButton);
        promotionCheckbox = (CheckBox) findViewById(R.id.checkBox);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    private void uploadPost() {
        String title, description1, description2;
        title = editTextTitle.getText().toString();
        description1 = editTextDescription1.getText().toString();
        description2 = editTextDescription2.getText().toString();

        PostsUploadController postsUploadController = new PostsUploadController();
        postsUploadController.uploadPost(this, title, image1BitMap, image2BitMap, description1, description2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_PHOTO && resultCode != RESULT_CANCELED) {
            Bitmap bitmap = ImagePicker.getImageFromResult(this, resultCode, data);
            switch (selectedImage) {
                case 1:
                    image1.setImageBitmap(bitmap);
                    image1BitMap = bitmap;
                    break;
                case 2:
                    image2.setImageBitmap(bitmap);
                    image2BitMap = bitmap;
                    break;
            }
        }
    }

    private void updateTokens() {
        tokens.setText(String.valueOf(SessionDetails.getInstance().numOfTokens));
    }

    private void deductPromotionFee() {
        tokens.setText(String.valueOf(SessionDetails.getInstance().numOfTokens-PromoteCallback.PROMOTION_COST));
    }

    public boolean checkPromotionEligibility() {
        if (PromoteCallback.PROMOTION_COST > SessionDetails.getInstance().numOfTokens) {
            Toast.makeText(this, "Not enough tokens to promote post! (Minimum: " + PromoteCallback.PROMOTION_COST + ")", Toast.LENGTH_LONG).show();
            promotionCheckbox.setChecked(false);
            return false;
        }
    return true;
    }


    @Override
    public void onClick(View v) {
        Intent chooseImageIntent;
        switch (v.getId()) {
            case R.id.postButton:
                if(editTextTitle.getText().toString().equals("") || image1BitMap == null || image2BitMap == null){
                    Toast.makeText(getApplicationContext(), "Post details missing...", Toast.LENGTH_SHORT).show();
                    break;
                }
                uploadPost();
                finish();
                break;

            case R.id.cancelButton:
                finish();
                break;

            case R.id.addPostImageView1:
                selectedImage = 1;
                chooseImageIntent = ImagePicker.getPickImageIntent(this);
                startActivityForResult(chooseImageIntent, SELECT_PHOTO);
                break;

            case R.id.addPostImageView2:
                selectedImage = 2;
                chooseImageIntent = ImagePicker.getPickImageIntent(this);
                startActivityForResult(chooseImageIntent, SELECT_PHOTO);
                break;

            case R.id.checkBox:
                promote = ((CheckBox) v).isChecked() && checkPromotionEligibility();
                if (promote)
                    deductPromotionFee();
                else
                    updateTokens();
                break;
        }
    }
}