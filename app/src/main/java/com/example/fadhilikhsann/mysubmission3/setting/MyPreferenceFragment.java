package com.example.fadhilikhsann.mysubmission3.setting;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreference;

import com.example.fadhilikhsann.mysubmission3.R;
import com.example.fadhilikhsann.mysubmission3.notification.ReminderReceiver;

public class MyPreferenceFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {
    private static final String TAG = "MainActivity";
    ReminderReceiver alarmReceiver;
    private String DAILY_REMINDER;
    private String RELEASE_REMINDER;
    private SwitchPreference releaseReminderPreference;
    private SwitchPreference dailyReminderPreference;

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        addPreferencesFromResource(R.xml.preferences);
        init();
        setSummaries();
    }

    private void init() {
        alarmReceiver = new ReminderReceiver();
        DAILY_REMINDER = getResources().getString(R.string.key_daily_reminder);
        RELEASE_REMINDER = getResources().getString(R.string.key_release_reminder);
        dailyReminderPreference = findPreference(DAILY_REMINDER);
        releaseReminderPreference = findPreference(RELEASE_REMINDER);

    }

    private void setSummaries() {
        SharedPreferences sh = getPreferenceManager().getSharedPreferences();
        dailyReminderPreference.setChecked(sh.getBoolean(DAILY_REMINDER, false));
        releaseReminderPreference.setChecked(sh.getBoolean(RELEASE_REMINDER, false));


        if (sh.getBoolean(DAILY_REMINDER, false) == true) {
            alarmReceiver.setRepeatingAlarm(getActivity(), ReminderReceiver.TYPE_DAILY, "07:00", getResources().getString(R.string.app_name) + " " + getResources().getString(R.string.miss));
            //Log.d(TAG, "onSharedPreferenceChanged_DAILY: "+sh.getBoolean(DAILY_REMINDER, false));
        }

        if (sh.getBoolean(RELEASE_REMINDER, false) == true) {
            alarmReceiver.setRepeatingAlarm(getActivity(), ReminderReceiver.TYPE_RELEASE, "08:00", getResources().getString(R.string.release_today));
            //Log.d(TAG, "onSharedPreferenceChanged_RELEASE: "+sharedPreferences.getBoolean(RELEASE_REMINDER, false));
        }


    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {

        Log.d(TAG, "onSharedPreferenceChanged: Alo!");
        if (s.equals(DAILY_REMINDER)) {
            dailyReminderPreference.setChecked(sharedPreferences.getBoolean(DAILY_REMINDER, false));

            if (sharedPreferences.getBoolean(DAILY_REMINDER, false) == true) {
                alarmReceiver.setRepeatingAlarm(getActivity(), ReminderReceiver.TYPE_DAILY, "07:00", getResources().getString(R.string.app_name) + " " + getResources().getString(R.string.miss));
                Log.d(TAG, "onSharedPreferenceChanged_DAILY: " + sharedPreferences.getBoolean(DAILY_REMINDER, false));
            } else {
                alarmReceiver.cancelAlarm(getActivity(), ReminderReceiver.TYPE_DAILY);
                Log.d(TAG, "onSharedPreferenceChanged_DAILY_CLEAR: " + sharedPreferences.getBoolean(DAILY_REMINDER, false));
            }

        } else if (s.equals(RELEASE_REMINDER)) {
            releaseReminderPreference.setChecked(sharedPreferences.getBoolean(RELEASE_REMINDER, false));

            //tambah event alarm disini
            if (sharedPreferences.getBoolean(RELEASE_REMINDER, false) == true) {
                alarmReceiver.setRepeatingAlarm(getActivity(), ReminderReceiver.TYPE_RELEASE, "08:00", getResources().getString(R.string.release_today));
                Log.d(TAG, "onSharedPreferenceChanged_RELEASE: " + sharedPreferences.getBoolean(RELEASE_REMINDER, false));
            } else {
                alarmReceiver.cancelAlarm(getActivity(), ReminderReceiver.TYPE_RELEASE);
                Log.d(TAG, "onSharedPreferenceChanged_RELEASE_CLEAR: " + sharedPreferences.getBoolean(RELEASE_REMINDER, false));
            }


        }
    }
}
