package net.cloudapp.chooser.chooser;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;


public abstract class PromotionDialog extends DialogFragment implements View.OnClickListener {
    public enum Time {MINUTE, HOUR, DAY}
    public Time promotionTime;
    private TextView seekBarValue, affordNote, price;
    private Spinner durationSpinner;
    private SeekBar seekBar;
    private int progress, maxVal, promotionPrice;
    private final int MIN_VAL = 1;
    private boolean canAfford;
    private String stringHeadline;
    private SessionDetails sessionDetails;


    public PromotionDialog(SessionDetails sessionDetails, String stringHeadline) {
        this.stringHeadline = stringHeadline;
        this.sessionDetails = sessionDetails;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.promotion_dialog, container);
        TextView dialogTokens = (TextView) view.findViewById(R.id.tokens);
        TextView headline = (TextView) view.findViewById(R.id.promotionHeadline);
        Button confirmButton = (Button) view.findViewById(R.id.promoteButton);
        Button cancelButton = (Button) view.findViewById(R.id.cancelButton);
        headline.setText(stringHeadline);
        promotionPrice = 0;
        seekBar = (SeekBar) view.findViewById(R.id.intervalSeekBar);
        price = (TextView) view.findViewById(R.id.price);
        affordNote = (TextView) view.findViewById(R.id.affordNote);
        seekBarValue = (TextView) view.findViewById(R.id.seekBarValue);
        durationSpinner = (Spinner) view.findViewById(R.id.durationSpinner);

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
    public int getPrice() {
        return promotionPrice;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancelButton:
                dismiss();
                break;
            case R.id.promoteButton:
                if (canAfford) {
                    onPromoteDialogFinish();
                    dismiss();
                }
                break;
        }
    }
    public abstract void onPromoteDialogFinish();

    public int getDuration() {
        return progress+MIN_VAL;
    }

}