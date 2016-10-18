package net.cloudapp.chooser.chooser.views.dialogs;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import net.cloudapp.chooser.chooser.R;

/**
 * Created by Ben on 19/10/2016.
 */
    public abstract class AgeFragmentIntervalDialog extends DialogFragment {
        private TextView seekBarValue, headline;
        private SeekBar seekBar;
        private int minVal;
        private int maxVal;
        private int progress;
        private String headlineText;

        public AgeFragmentIntervalDialog(String headlineText, int minVal, int maxVal, int initialValue) {
            this.headlineText = headlineText;
            this.minVal = minVal;
            this.maxVal = maxVal;
            this.progress = initialValue;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.seek_bar_dialog, container);
            seekBar = (SeekBar) view.findViewById(R.id.intervalSeekBar);
            headline = (TextView) view.findViewById(R.id.seekBarHeadline);
            seekBarValue = (TextView) view.findViewById(R.id.seekBarValue);
            headline.setText(headlineText);
            seekBar.setMax(maxVal-minVal);
            seekBarValue.setText(progress + "/" + (seekBar.getMax()+minVal));
            seekBar.setProgress(progress-minVal);
            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                boolean progressChanged = false;
                @Override
                public void onProgressChanged(SeekBar seekBar, int progressValue, boolean fromUser) {
                    progress = progressValue;
                    progressChanged = true;
                    seekBarValue.setText((progress+minVal) + "/" + (seekBar.getMax()+minVal));
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    seekBarValue.setText((progress+minVal) + "/" + (seekBar.getMax()+minVal));
                    if (progressChanged)
                        onEndOfSeekBarTracking(progress+minVal);
                    else
                        onEndOfSeekBarTracking(progress);


                    dismiss();
                }
            });
            return view;
        }

        public abstract void onEndOfSeekBarTracking(int progress);
    }

