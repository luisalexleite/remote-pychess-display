package com.example.chess.profile;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import com.example.chess.MainActivity;
import com.example.chess.R;
import java.util.Locale;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        DarkMode();
        Language();
        back();
    }

    private void DarkMode() {
        SwitchCompat mySwitch2 = findViewById(R.id.darkmode);
        int nightModeFlags = getResources().getConfiguration().uiMode &
                Configuration.UI_MODE_NIGHT_MASK;
        switch (nightModeFlags) {
            case Configuration.UI_MODE_NIGHT_YES:
                mySwitch2.setChecked(true);
                mySwitch2.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    if (isChecked) {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

                    } else {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    }
                });
                break;

            case Configuration.UI_MODE_NIGHT_NO:
                mySwitch2.setChecked(false);
                mySwitch2.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    if (isChecked) {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

                    } else {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

                    }
                });
                break;

            case Configuration.UI_MODE_NIGHT_UNDEFINED:
                break;
        }
    }

    private void Language() {
        TextView language_dialog = findViewById(R.id.dialog_language);
        language_dialog.setOnClickListener(v -> {
            final String[] Language = {getString(R.string.english), getString(R.string.Portuguese)};
            int checkedItem = 0;

            if (getResources().getConfiguration().locale.toString().contains("pt")) {
                checkedItem = 1;
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this, R.style.AlertDialogStyle);
            builder.setTitle(R.string.selectLang)
                    .setSingleChoiceItems(Language, checkedItem, (dialog, which) -> {
                        if(Language[which].equals(getString(R.string.english)))
                        {
                            setAppLocale("en");
                            restart();
                        }
                        if(Language[which].equals(getString(R.string.Portuguese)))
                        {
                            setAppLocale("pt");
                            restart();
                        }
                    });
            builder.create().show();
        });
    }
    private void setAppLocale(String localeCode) {
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.setLocale(new Locale(localeCode.toLowerCase()));
        res.updateConfiguration(conf, dm);
    }

    private void restart() {
        Intent intent = new Intent(this, SettingsActivity.class);
        finish();
        startActivity(intent);
    }

    private void back() {
        ImageButton back = findViewById(R.id.backBtnSettings);
        back.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            finish();
            startActivity(intent);

        });
    }
}
