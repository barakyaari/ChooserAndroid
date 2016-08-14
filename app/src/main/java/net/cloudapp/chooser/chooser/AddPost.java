package net.cloudapp.chooser.chooser;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


public class AddPost extends Activity implements View.OnClickListener {
    private static final int SELECT_PHOTO = 100;
    private int selectedImage = 0;
    private SessionDetails sessionDetails;
    private boolean promoted;
    private int promotionDuration, promotionPrice;
    private PromotionDialog promotionDialog;
    Button buttonAddPost, buttonCancel, buttonPromote;
    EditText editTextTitle, editTextDescription1, editTextDescription2;
    ImageView image1, image2;
    TextView tokens, promotionText;
    Bitmap image1BitMap, image2BitMap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_post);
        sessionDetails = (SessionDetails) getIntent().getSerializableExtra("SessionDetails");
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

        tokens.setText(String.valueOf(sessionDetails.userTokenCount));
        promotionText.setVisibility(View.INVISIBLE);
        buttonAddPost.setOnClickListener(this);
        buttonCancel.setOnClickListener(this);
        buttonPromote.setOnClickListener(this);
        image1.setOnClickListener(this);
        image2.setOnClickListener(this);

        promotionPrice = 0;
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

    private void uploadPost() {
        String title, description1, description2, image1, image2;
        Bitmap bitmap1 = Bitmap.createScaledBitmap(image1BitMap, image1BitMap.getWidth(), image1BitMap.getHeight(), true);
        Bitmap bitmap2 = Bitmap.createScaledBitmap(image2BitMap, image2BitMap.getWidth(), image2BitMap.getHeight(), true);

        image1 = Post.bitmap2String(bitmap1,0,0);
        image2 = Post.bitmap2String(bitmap2,0,0);

        title = editTextTitle.getText().toString();
        description1 = editTextDescription1.getText().toString();
        description2 = editTextDescription2.getText().toString();
        ConnectionManager connectionManager = new ConnectionManager(sessionDetails);
        Runnable doAtFinish = new Runnable() {
            @Override
            public void run() {
                finish();
            }
        };
        if (promoted)
            connectionManager.AddPostWithBlob(title, image1, description1, image2, description2, doAtFinish, promotionDuration, promotionDialog.promotionTime.name());
        else
            connectionManager.AddPostWithBlob(title, image1, description1, image2, description2, doAtFinish, 0, "");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.postButton: {
                Bitmap bitmap1 = ((BitmapDrawable) image1.getDrawable()).getBitmap();
                image1BitMap = bitmap1;
                Bitmap bitmap2 = ((BitmapDrawable) image2.getDrawable()).getBitmap();
                image2BitMap = bitmap2;
                uploadPost();
            }
            break;

            case R.id.cancelButton: {
                finish();
            }
            break;

            case R.id.addPostImageView1:
                selectedImage = 1;
                Intent chooseImageIntent = ImagePicker.getPickImageIntent(this);
                startActivityForResult(chooseImageIntent, SELECT_PHOTO);
                break;

            case R.id.addPostImageView2:
                selectedImage = 2;
                Intent chooseImageIntent2 = ImagePicker.getPickImageIntent(this);
                startActivityForResult(chooseImageIntent2, SELECT_PHOTO);
                break;
            case R.id.promoteButton:
                if (promoted) {
                    promoted = false;
                    buttonPromote.setText("Promote");
                    promotionText.setVisibility(View.INVISIBLE);
                    sessionDetails.userTokenCount += promotionPrice;
                    tokens.setText(String.valueOf(sessionDetails.userTokenCount));
                    promotionPrice = 0;
                } else {
                    promotionDialog = new PromotionDialog(sessionDetails, "Promote Post For:") {
                        @Override
                        public void onPromoteDialogFinish() {
                            promoted = true;
                            promotionDuration = getDuration();
                            promotionPrice = getPrice();
                            sessionDetails.userTokenCount -= promotionPrice;
                            tokens.setText(String.valueOf(sessionDetails.userTokenCount));
                            String pText = "Promotion enabled for " + promotionDuration + " " + promotionTime.name().toLowerCase();
                            if (promotionDuration > 1)
                                pText += "s";
                            promotionText.setText(pText);
                            promotionText.setVisibility(View.VISIBLE);
                            buttonPromote.setText("Cancel Promotion");
                        }
                    };
                    promotionDialog.show(getFragmentManager(), "PromotionDialog");
                    break;
                }
        }
    }





}