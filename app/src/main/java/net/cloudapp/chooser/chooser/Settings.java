package net.cloudapp.chooser.chooser;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.view.MenuItem;

/**
 * Created by Ben on 05/07/2016.
 */
public class Settings extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
        getActionBar().setDisplayHomeAsUpEnabled(true);
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

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Preference connectionPref;
        switch (key) {
            case "prefUnitSwitch":
                connectionPref = findPreference(key);
                connectionPref.setSummary(sharedPreferences.getString(key, ""));
                break;

            case "prefSkipResSwitch":
                connectionPref = findPreference(key);
                connectionPref.setSummary(sharedPreferences.getString(key, ""));
                break;
        }
    }


}
