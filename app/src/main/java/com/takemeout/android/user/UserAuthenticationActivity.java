package com.takemeout.android.user;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.takemeout.android.ApplicationCtx;
import com.takemeout.android.R;
import com.takemeout.android.registration.RegistrationAreaActivity;
import com.takemeout.android.user.requests.LoginRequest;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by helgah on 16/03/2017.
 */

public class UserAuthenticationActivity  extends AppCompatActivity implements View.OnClickListener{


    private Button mRegistrationButton;
    private Button mLoginButton;
    private EditText mEditTextUsername;
    private EditText mEditTextPassword;
    private final String urlWithPort = "https://morning-peak-70516.herokuapp.com/";
    private boolean mValidated;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mRegistrationButton = (Button) findViewById(R.id.reg_button);
        mLoginButton = (Button) findViewById(R.id.login_button);
        mEditTextUsername = (EditText) findViewById(R.id.username);
        mEditTextPassword = (EditText) findViewById(R.id.password);

        mLoginButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        LoginRequest userLoginRequest = new LoginRequest();
        if(v == mLoginButton){
            loginUser(userLoginRequest);
        }
    }

    private void loginUser(LoginRequest userLoginRequest) {
        String username = mEditTextUsername.getText().toString();
        String password = mEditTextPassword.getText().toString();

        userLoginRequest.setUserName(username);
        userLoginRequest.setPasswordHash(password);

        mValidated = validate(username, password);

        if (mValidated) {
            new LoginUserHandler().execute(userLoginRequest);
        } else {
            Toast.makeText(this, "Það þarf að fylla út báða reiti fyrir innskráningu", Toast.LENGTH_LONG).show();
        }

    }

    private class LoginUserHandler extends AsyncTask<LoginRequest, Void, UserToken> {

        ProgressDialog loading;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loading = ProgressDialog.show(UserAuthenticationActivity.this, "Sæki notanda",null, true, true);
        }

        @Override
        protected void onPostExecute(UserToken res) {
            super.onPostExecute(res);
            loading.dismiss();
            if (res != null) {
                CharSequence response = res.getUsername();
                Toast.makeText(getApplicationContext(), "Velkomin(n) " + response, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(), "Notandi finnst ekki", Toast.LENGTH_LONG).show();
                return;
            }
            ApplicationCtx ctx = (ApplicationCtx) getApplicationContext();
            ctx.setUsername(res.getUsername());
            ctx.setJWToken(res.getToken());

            if (res != null) {
                int timeout = 2500;
                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        finish();
                        Intent registration = new Intent(UserAuthenticationActivity.this, RegistrationAreaActivity.class);
                        startActivity(registration);
                    }
                }, timeout);
            }
        }

        @Override
        protected UserToken doInBackground(LoginRequest... user) {
            try {
                final String endpoint = urlWithPort+"/user/auth/loginandroid";

                HttpHeaders requestHeaders = new HttpHeaders();
                requestHeaders.setContentType(new MediaType("application","json"));
                HttpEntity<LoginRequest> requestEntity = new HttpEntity<>(requestHeaders);

                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

                return restTemplate.getForObject(endpoint+"?username="+user[0].getUserName()+"&passwordHash="+user[0].getPasswordHash(), UserToken.class);
            } catch (Exception e) {
                Log.e("GetRequestHandler:\n", e.getMessage(), e);
                return null;
            }
        }
    }
        @Override
    protected void onStart() {
        super.onStart();
    }

    public void seeRegistrationForm(View view)
    {
        Intent intent = new Intent(UserAuthenticationActivity.this, UserRegistrationActivity.class);
        startActivity(intent);
    }

    private boolean validate(String username, String password) {
        if (username.equals("") || username == null) return false;
        if (password.equals("") || password == null) return false;

        return true;
    }

}
