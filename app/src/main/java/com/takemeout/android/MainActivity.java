package com.takemeout.android;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.content.Intent;

import com.takemeout.android.event.EventOverviewActivity;
import com.takemeout.android.registration.RegistrationAreaActivity;
import com.takemeout.android.user.UserAuthenticationActivity;

public class MainActivity extends AppCompatActivity {

    private Button mTodayEvents;
    private Button mProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTodayEvents = (Button) findViewById(R.id.button_today);
        mProfile = (Button) findViewById(R.id.profile_button);

    }

    public void seeEventOverview(View view)
    {
        Intent intent = new Intent(MainActivity.this, EventOverviewActivity.class);
        startActivity(intent);
    }

    public void seeProfileOverview(View view)
    {
        ApplicationCtx ctx = (ApplicationCtx) getApplicationContext();
        if (ctx.getJWToken() == null || ctx.getJWToken().equals("")) {
            Intent intent = new Intent(MainActivity.this, UserAuthenticationActivity.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(MainActivity.this, RegistrationAreaActivity.class);
            startActivity(intent);
        }
    }
}
