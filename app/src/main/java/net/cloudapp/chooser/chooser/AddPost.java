package net.cloudapp.chooser.chooser;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;


public class AddPost extends Activity implements View.OnClickListener {
    private enum Time {MINUTE, HOUR, DAY}
    private static final int SELECT_PHOTO = 100;
    private int selectedImage = 0;
    private SessionDetails sessionDetails;
    private boolean promoted;
    private Time promotionTime;
    private int promotionDuration, promotionPrice;
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
        if (promoted) {
            connectionManager.AddPostWithBlob(title, image1, description1, image2, description2, doAtFinish, promotionDuration, promotionTime.name());

        }

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
                    PromotionDialog promotionDialog = new PromotionDialog();
                    promotionDialog.show(getFragmentManager(), "PromotionDialog");
                    break;
                }
        }
    }




    public class PromotionDialog extends DialogFragment implements View.OnClickListener {
        private TextView seekBarValue, dialogTokens, price, affordNote;
        private Spinner durationSpinner;
        private Button confirmButton, cancelButton;
        private SeekBar seekBar;
        private int progress, maxVal;
        private final int MIN_VAL = 1;
        private boolean canAfford;


        public PromotionDialog() {}

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.promotion_dialog, container);
            seekBar = (SeekBar) view.findViewById(R.id.intervalSeekBar);
            price = (TextView) view.findViewById(R.id.price);
            dialogTokens = (TextView) view.findViewById(R.id.tokens);
            affordNote = (TextView) view.findViewById(R.id.affordNote);
            seekBarValue = (TextView) view.findViewById(R.id.seekBarValue);
            durationSpinner = (Spinner) view.findViewById(R.id.durationSpinner);
            confirmButton = (Button) view.findViewById(R.id.promoteButton);
            cancelButton = (Button) view.findViewById(R.id.cancelButton);
            durationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    switch (position) {
                        case 0:
                            promotionTime = Time.MINUTE;
                            maxVal = 59;
                            break;
                        case 1:
                            promotionTime = Time.HOUR;
                            maxVal = 23;
                            break;
                        case 2:
                            promotionTime = Time.DAY;
                            maxVal = 100;
                    }
                    maxVal -= MIN_VAL;
                    calculateCost();
                    seekBar.setMax(maxVal);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            promotionTime = Time.MINUTE;
            maxVal = 59;
            price.setText(String.valueOf(50));
            dialogTokens.setText(String.valueOf(sessionDetails.userTokenCount));
            canAfford = (sessionDetails.userTokenCount >= 50);
            if (canAfford)
                affordNote.setVisibility(View.INVISIBLE);
            confirmButton.setOnClickListener(this);
            cancelButton.setOnClickListener(this);
            addDurationsToSpinner();
            seekBar.setMax(maxVal-MIN_VAL);
            seekBarValue.setText(String.valueOf(progress+MIN_VAL));
            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progressValue, boolean fromUser) {
                    progress = progressValue;
                    seekBarValue.setText(String.valueOf(progress+MIN_VAL));
                    calculateCost();
                    price.setText(String.valueOf(promotionPrice));
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    seekBarValue.setText(String.valueOf(progress+MIN_VAL));
                }
            });
            return view;
        }

        public void calculateCost() {
            if (promotionTime == Time.MINUTE)
                promotionPrice = 50*(progress+MIN_VAL);
            if (promotionTime == Time.HOUR)
                promotionPrice = 700+1800*(progress+MIN_VAL);
            if (promotionTime == Time.DAY)
                promotionPrice = 11200+28800*(progress+MIN_VAL);
            if (promotionPrice > sessionDetails.userTokenCount) {
                canAfford = false;
                affordNote.setVisibility(View.VISIBLE);
            } else {
                canAfford = true;
                affordNote.setVisibility(View.INVISIBLE);
            }

        }
        public void addDurationsToSpinner () {
            ArrayList<String> list = new ArrayList<>();
            list.add("Minutes");
            list.add("Hours");
            list.add("Days");
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, list);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            durationSpinner.setAdapter(dataAdapter);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.cancelButton:
                    dismiss();
                    break;
                case R.id.promoteButton:
                    if (canAfford) {
                        promote();
                        dismiss();
                    }
                    break;
        }
    }
        public void promote() {
            promoted = true;
            promotionDuration = progress + MIN_VAL;
            sessionDetails.userTokenCount -= promotionPrice;
            tokens.setText(String.valueOf(sessionDetails.userTokenCount));
            String pText = "Promotion enabled for " + promotionDuration + " " + promotionTime.name().toLowerCase();
            if (promotionDuration > 1)
                pText += "s";
            promotionText.setText(pText);
            promotionText.setVisibility(View.VISIBLE);
            buttonPromote.setText("Cancel Promotion");
        }


    }
}