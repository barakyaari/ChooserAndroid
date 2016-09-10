package net.cloudapp.chooser.chooser;

import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.text.format.DateFormat;
import android.widget.TimePicker;
/**
 * Created by Ben on 28/08/2016.
 */

public abstract class NotificationDialog extends DialogFragment implements View.OnClickListener {
    public enum NotificationMethod {VOTES, TIME}
    public NotificationMethod notificationMethod;
    private TextView seekBarValue;
    private Spinner methodSpinner;
    private SeekBar seekBar;
    private int maxVal, voteThreshold;
    private int minVal;
    private boolean isValidTime;
    private String stringHeadline;
    private Button confirmButton, cancelButton, timeButton;


    public NotificationDialog(String stringHeadline, int currentVoteCount) {
        this.stringHeadline = stringHeadline;
        minVal = ++currentVoteCount;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.notification_dialog, container);
        TextView headline = (TextView) view.findViewById(R.id.dialogHeadline);
        confirmButton = (Button) view.findViewById(R.id.notifyButton);
        cancelButton = (Button) view.findViewById(R.id.cancelButton);
        timeButton = (Button) view.findViewById(R.id.timeButton);
        headline.setText(stringHeadline);
        seekBar = (SeekBar) view.findViewById(R.id.intervalSeekBar);
        seekBarValue = (TextView) view.findViewById(R.id.seekBarValue);
        methodSpinner = (Spinner) view.findViewById(R.id.methodSpinner);

        methodSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        notificationMethod = NotificationMethod.TIME;
                        timeButton.setVisibility(View.VISIBLE);
                        seekBar.setVisibility(View.INVISIBLE);
                        seekBarValue.setVisibility(View.INVISIBLE);
                        break;
                    case 1:
                        notificationMethod = NotificationMethod.VOTES;
                        timeButton.setVisibility(View.INVISIBLE);
                        seekBar.setVisibility(View.VISIBLE);
                        seekBarValue.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        notificationMethod = NotificationMethod.TIME;
        maxVal = minVal + 9999;
        confirmButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);
        timeButton.setOnClickListener(this);
        addCategoriesToSpinner();
        seekBar.setMax(maxVal-minVal);
        seekBarValue.setText(String.valueOf(voteThreshold+minVal));
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progressValue, boolean fromUser) {
                voteThreshold = progressValue;
                seekBarValue.setText(String.valueOf(voteThreshold+minVal));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                seekBarValue.setText(String.valueOf(voteThreshold+minVal));
            }
        });
        return view;
    }

    public void addCategoriesToSpinner () {
        ArrayList<String> list = new ArrayList<>();
        list.add("Time");
        list.add("Votes");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        methodSpinner.setAdapter(dataAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancelButton:
                dismiss();
                break;
            case R.id.notifyButton:
                onNotificationDialogFinish();
                break;
            case R.id.timeButton:
                DialogFragment newFragment = new TimePickerFragment();
                newFragment.show(getFragmentManager(), "timePicker");
                break;
        }
    }

    public int getDuration() {
        return voteThreshold+minVal;
    }

    public abstract void onNotificationDialogFinish();



    public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute, DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            // Do something with the time chosen by the user
            //"Hour: "+view.getCurrentHour()+" Minute: "+view.getCurrentMinute();
            DialogFragment newFragment = new DatePickerFragment();
            newFragment.show(getFragmentManager(), "datePicker");

        }
    }

    public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int day = c.get(Calendar.DAY_OF_MONTH);
            int month = c.get(Calendar.MONTH);
            int year = c.get(Calendar.YEAR);

            // Create a new instance of TimePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month,day);
        }


        @Override
        public void onDateSet(DatePicker arg0, int year, int month, int day) {
            // TODO Auto-generated method stub
        }
    }
}
