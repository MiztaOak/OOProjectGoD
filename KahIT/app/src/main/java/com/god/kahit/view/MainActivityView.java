package com.god.kahit.view;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.god.kahit.R;
import com.god.kahit.repository.Repository;

/**
 * responsibility: The main view that launches when the app starts.
 * used-by: AfterQuestionScorePageView, ScorePageView, ChooseGameVIew, QuestionView.
 *
 * @author Oussama Anadani, Mats Cedervall, Johan Ek
 */
public class MainActivityView extends AppCompatActivity {
    private static final String LOG_TAG = MainActivityView.class.getSimpleName();


    private static final String[] REQUIRED_PERMISSIONS =
            new String[]{
                    Manifest.permission.BLUETOOTH,
                    Manifest.permission.BLUETOOTH_ADMIN,
                    Manifest.permission.ACCESS_WIFI_STATE,
                    Manifest.permission.CHANGE_WIFI_STATE,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
            };

    private static final int REQUEST_CODE_REQUIRED_PERMISSIONS = 1;

    /**
     * Returns true if the app was granted all the permissions. Otherwise, returns false.
     */
    private static boolean hasPermissions(Context context, String... permissions) {
        for (String permission : permissions)
            if (ContextCompat.checkSelfPermission(context, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        Repository.getInstance().setupDataBaseLoader(getApplicationContext());

        Repository.getInstance().setupAppLifecycleObserver(getApplicationContext());
        Repository.getInstance().setupAudioHandler(getApplicationContext());
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!hasPermissions(this, REQUIRED_PERMISSIONS)) {
            requestPermissions(REQUIRED_PERMISSIONS, REQUEST_CODE_REQUIRED_PERMISSIONS);
        }
        Repository.getInstance().resetApp(this);
    }

    /**
     * Handles user acceptance (or denial) of our permission request.
     */
    @CallSuper
    @Override
    public void onRequestPermissionsResult(
            int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode != REQUEST_CODE_REQUIRED_PERMISSIONS) {
            return;
        }

        for (int grantResult : grantResults) {
            if (grantResult == PackageManager.PERMISSION_DENIED) {
                Toast.makeText(this, "Cannot start without required permissions", Toast.LENGTH_LONG).show();
                finish();
                return;
            }
        }
        recreate();
    }

    public void launchChooseGameClass(View view) {
        Log.d(LOG_TAG, "Button clicked!");
        Intent intent = new Intent(this, ChooseGameView.class);
        startActivity(intent);
    }

    public void launchSettingsView(View view) {
        Log.d(LOG_TAG, "Button clicked!");
        Intent intent = new Intent(this, SettingsView.class);
        startActivity(intent);
    }

    public void launchAboutKahitView(View view) {
        Log.d(LOG_TAG, "Button clicked!");
        Intent intent = new Intent(this, AboutKahitView.class);
        startActivity(intent);
    }

}
