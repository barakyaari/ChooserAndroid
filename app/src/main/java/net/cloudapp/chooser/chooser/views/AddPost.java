package net.cloudapp.chooser.chooser.views;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import net.cloudapp.chooser.chooser.Images.ImagePicker;
import net.cloudapp.chooser.chooser.Network.RestFramework.RestClient;
import net.cloudapp.chooser.chooser.Images.ImageUploader;
import net.cloudapp.chooser.chooser.Images.ImageUploaderImpl;
import net.cloudapp.chooser.chooser.Media.ImageSelector;
import net.cloudapp.chooser.chooser.Images.CloudinaryClient;
import net.cloudapp.chooser.chooser.PostUpload.PostUploadCallback;
import net.cloudapp.chooser.chooser.R;
import net.cloudapp.chooser.chooser.Common.SessionDetails;


public class AddPost extends AppCompatActivity implements View.OnClickListener {
    private static final int SELECT_PHOTO = 100;
    private int selectedImage = 0;
    Button buttonAddPost, buttonCancel, buttonPromote, buttonNotify;
    EditText editTextTitle, editTextDescription1, editTextDescription2;
    ImageView image1, image2;
    TextView tokens, promotionText;
    Bitmap image1BitMap, image2BitMap;
    ImageSelector selector;
    CloudinaryClient cloudinaryClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setViewElements();
        setContentView(R.layout.add_post);
        selector = new ImageSelector();
        tokens = (TextView) findViewById(R.id.tokens);
        promotionText = (TextView) findViewById(R.id.promotionText);
        image1 = (ImageView) findViewById(R.id.addPostImageView1);
        image2 = (ImageView) findViewById(R.id.addPostImageView2);
        editTextTitle = (EditText) findViewById(R.id.postTitleEditText);
        editTextDescription1 = (EditText) findViewById(R.id.description1EditText);
        editTextDescription2 = (EditText) findViewById(R.id.description2EditText);
        buttonAddPost = (Button) findViewById(R.id.postButton);
        buttonCancel = (Button) findViewById(R.id.cancelButton);
        buttonPromote = (Button) findViewById(R.id.promoteButton);
        buttonNotify = (Button) findViewById(R.id.notificationButton);
        cloudinaryClient = new CloudinaryClient();

//        tokens.setText(String.valueOf(sessionDetails.userTokenCount));
        promotionText.setVisibility(View.INVISIBLE);
        buttonAddPost.setOnClickListener(this);
        buttonCancel.setOnClickListener(this);
        buttonPromote.setOnClickListener(this);
        buttonNotify.setOnClickListener(this);
        image1.setOnClickListener(this);
        image2.setOnClickListener(this);
    }

    private void setViewElements() {
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void uploadPost() {
        String image1Id, image2Id, title, description1, description2;
        ImageUploader uploader = new ImageUploaderImpl();
        image1Id = uploader.uploadImage(image1BitMap);
        image2Id = uploader.uploadImage(image2BitMap);
        title = editTextTitle.getText().toString();
        description1 = editTextDescription1.getText().toString();
        description2 = editTextDescription2.getText().toString();
        RestClient restClient = new RestClient();
        restClient.getService().addpost(
                SessionDetails.getInstance().getAccessToken().getToken(),
                title,
                image1Id,
                image2Id,
                description1,
                description2,
                new PostUploadCallback(this)
        );
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

    @Override
    public void onClick(View v) {
        Intent chooseImageIntent;
        switch (v.getId()) {
            case R.id.postButton:
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
        }
    }
}