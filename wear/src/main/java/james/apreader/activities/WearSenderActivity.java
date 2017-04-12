package james.apreader.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.wearable.activity.ConfirmationActivity;
import android.view.View;

import com.google.android.gms.common.api.Status;

import james.apreader.R;
import james.apreader.common.Supplier;

public class WearSenderActivity extends Activity {

    public static final String EXTRA_MESSAGE = "james.apreader.EXTRA_MESSAGE";

    private String message;
    private Supplier supplier;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wear_sender);
        supplier = (Supplier) getApplicationContext();

        message = getIntent().getStringExtra(EXTRA_MESSAGE);

        findViewById(R.id.action_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        findViewById(R.id.action_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                supplier.sendWearableMessage(message, new Supplier.AsyncListener<Status>() {
                    @Override
                    public void onTaskComplete(Status value) {
                        Intent intent = new Intent(WearSenderActivity.this, ConfirmationActivity.class);
                        intent.putExtra(ConfirmationActivity.EXTRA_ANIMATION_TYPE, ConfirmationActivity.SUCCESS_ANIMATION);
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onFailure() {
                        Intent intent = new Intent(WearSenderActivity.this, ConfirmationActivity.class);
                        intent.putExtra(ConfirmationActivity.EXTRA_ANIMATION_TYPE, ConfirmationActivity.FAILURE_ANIMATION);
                        startActivity(intent);
                        finish();
                    }
                });
            }
        });
    }

}
