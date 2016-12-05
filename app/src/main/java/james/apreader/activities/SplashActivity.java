package james.apreader.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import james.apreader.R;
import james.apreader.Supplier;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED) {
            loadStuff();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET}, 2746);
        }
    }

    private void loadStuff() {
        new Thread() {
            @Override
            public void run() {
                if (!((Supplier) getApplicationContext()).getNetworkResources()) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(SplashActivity.this, R.string.download_failed, Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });
                    return;
                }

                try {
                    sleep(1500);
                } catch (InterruptedException ignored) {
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(SplashActivity.this, MainActivity.class));
                        finish();
                    }
                });
            }
        }.start();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 2746 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            loadStuff();
        else finish();
    }
}
