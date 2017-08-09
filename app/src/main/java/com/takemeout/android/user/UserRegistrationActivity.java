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
import android.widget.ImageButton;
import android.widget.Toast;

import com.takemeout.android.R;
import com.takemeout.android.user.requests.RegisterUserRequest;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.Timer;
import java.util.TimerTask;

import static android.R.attr.name;

/**
 * Created by helgah on 16/03/2017.
 */

public class UserRegistrationActivity  extends AppCompatActivity implements View.OnClickListener {


    private Button mRegisterUser;
    private EditText mEditTextUsername;
    private EditText mEditTextEmail;
    private EditText mEditTextPassword;
    private EditText mEditTextPasswordConfirm;
    private boolean mValidated;
    private final String urlWithPort = "https://morning-peak-70516.herokuapp.com/";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_reg);

        mEditTextUsername = (EditText) findViewById(R.id.user_edit);
        mEditTextEmail = (EditText) findViewById(R.id.email_edit);
        mEditTextPassword = (EditText) findViewById(R.id.password_edit);
        mEditTextPasswordConfirm = (EditText) findViewById(R.id.password_edit_2);

        mRegisterUser = (Button) findViewById(R.id.button_register_user);

        mRegisterUser.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        RegisterUserRequest userRequest = new RegisterUserRequest();
        if(v == mRegisterUser){
            registerUser(userRequest);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void registerUser(RegisterUserRequest user) {
        String username = mEditTextUsername.getText().toString();
        String email = mEditTextEmail.getText().toString();
        String password = mEditTextPassword.getText().toString();
        String passwordConfirm = mEditTextPassword.getText().toString();

        user.setUserName(username);
        user.setEmail(email);
        user.setPasswordHash(password);

        mValidated = validate(username, email, password, passwordConfirm);

       if (mValidated) {
           new RegisterUserHandler().execute(user);
        } else {
           Toast.makeText(this, "Það þarf að fylla út alla reiti fyrir skráningu", Toast.LENGTH_LONG).show();
       }
    }


        private class RegisterUserHandler extends AsyncTask<RegisterUserRequest, Void, String> {

            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(UserRegistrationActivity.this, "Vinsamlegast bíðið",null, true, true);
            }

            @Override
            protected void onPostExecute(String res) {
                super.onPostExecute(res);
                CharSequence response = res;
                loading.dismiss();
                Toast.makeText(getApplicationContext(), res, Toast.LENGTH_LONG).show();
                if (res != "Aðgerð mistókst") {
                    int timeout = 3000; // make the activity visible for 4 seconds

                    Timer timer = new Timer();
                    timer.schedule(new TimerTask() {

                        @Override
                        public void run() {
                            finish();
                            Intent homepage = new Intent(UserRegistrationActivity.this, UserAuthenticationActivity.class);
                            startActivity(homepage);
                        }
                    }, timeout);
                }
            }

            @Override
            protected String doInBackground(RegisterUserRequest... user) {
                try {
                    final String endpoint = urlWithPort+"/user/auth/register";

                    HttpHeaders requestHeaders = new HttpHeaders();
                    requestHeaders.setContentType(new MediaType("application","json"));
                    HttpEntity<RegisterUserRequest> requestEntity = new HttpEntity<>(user[0], requestHeaders);

                    RestTemplate restTemplate = new RestTemplate();
                    restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                    restTemplate.getMessageConverters().add(new StringHttpMessageConverter());

                    ResponseEntity<String> responseEntity = restTemplate.exchange(endpoint, HttpMethod.POST, requestEntity, String.class);
                    String result = responseEntity.getBody();

                    return "Notandi skráður!";
                } catch (Exception e) {
                    Log.e("GetRequestHandler:\n", e.getMessage(), e);
                    return "Aðgerð mistókst";
                }
            }
        }


    private boolean validate(String username, String email, String password, String passwordConfirm) {
        if (username.equals("") || username == null) return false;
        if (email.equals("") || email == null) return false;
        if (password.equals("") || password == null) return false;
        if (passwordConfirm.equals("") || passwordConfirm == null) return false;

        return true;
    }
}
