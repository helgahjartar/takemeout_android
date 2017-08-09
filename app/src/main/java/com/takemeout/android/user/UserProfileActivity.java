package com.takemeout.android.user;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.takemeout.android.R;

import org.w3c.dom.Text;

/**
 * Created by helgah on 02/04/2017.
 */

public class UserProfileActivity extends AppCompatActivity {

    private Button mRegEventButton;
    private TextView mProfileTitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        mRegEventButton = (Button) findViewById(R.id.register_event);
        mProfileTitle = (TextView) findViewById(R.id.profile_title);

        mProfileTitle.setText("Hér er þitt svæði ");
    }
}
