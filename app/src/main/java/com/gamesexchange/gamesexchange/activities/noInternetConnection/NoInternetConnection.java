package com.gamesexchange.gamesexchange.activities.noInternetConnection;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.gamesexchange.gamesexchange.R;
import com.gamesexchange.gamesexchange.activities.login.MVPLoginActivity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Created by midomohaned on 30/09/2017.
 */

public class NoInternetConnection extends AppCompatActivity {

    private Button mRetryButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nointernetconnection);
        mRetryButton = (Button) findViewById(R.id.ID_retry_button_no_connection);
        mRetryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(NoInternetConnection.this,MVPLoginActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);    }
}
