package com.takemeout.android.registration;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.takemeout.android.ApplicationCtx;
import com.takemeout.android.R;
import com.takemeout.android.registration.projections.LocationProjection;
import com.takemeout.android.registration.projections.PerformerProjection;
import com.takemeout.android.registration.projections.TypeItem;
import com.takemeout.android.registration.requests.RegisterEventRequest;
import com.takemeout.android.registration.requests.RegisterLocationRequest;
import com.takemeout.android.registration.requests.RegisterPerformerRequest;
import com.takemeout.android.user.UserAuthenticationActivity;
import com.takemeout.android.user.UserRegistrationActivity;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by halldorr on 3/31/17.
 */

public class RegistrationAreaActivity extends AppCompatActivity implements IRegistrationListener {

    private final String url = "http://morning-peak-70516.herokuapp.com";

    private Button mSelectedButton;

    private Fragment regForm;
    private TextView mTxtRegister;

    private HashMap<String, Integer> typeMap;
    private HashMap<String, Integer> locationMap;
    private HashMap<String, Integer> performerMap;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_area);

        Button mBtnRegEvent = (Button) findViewById(R.id.btnRegEvent);
        mTxtRegister = (TextView) findViewById(R.id.txtRegister);
        mSelectedButton = mBtnRegEvent;

        regForm = new RegisterEventFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.ll_register, regForm).commit();
        getSupportFragmentManager().executePendingTransactions();

        new GetSpinnerDataHandler<>(TypeItem[].class).execute("/event/query/types");
        new GetSpinnerDataHandler<>(LocationProjection[].class).execute("/event/query/locations");
        new GetSpinnerDataHandler<>(PerformerProjection[].class).execute("/event/query/performers");
    }

    public void btnRegEventClick(View view)
    {
        if (regForm instanceof RegisterEventFragment) return;
        mTxtRegister.setText("Skrá Viðburð");
        RegisterEventFragment fragment = new RegisterEventFragment();
        changeRegForm(fragment, view);
        if (typeMap != null) fragment.setTypes(typeMap);
        if (locationMap != null) fragment.setLocations(locationMap);
        if (performerMap != null) fragment.setPerformers(performerMap);
    }

    public void btnRegLocationClick(View view)
    {
        if (regForm instanceof RegisterLocationFragment) return;
        mTxtRegister.setText("Skrá Staðsetningu");
        changeRegForm(new RegisterLocationFragment(), view);
    }

    public void btnRegPerformerClick(View view)
    {
        if (regForm instanceof RegisterPerformerFragment) return;
        mTxtRegister.setText("Skrá Flytjanda");
        changeRegForm(new RegisterPerformerFragment(), view);
    }

    public void handleRegistration(IRegistrationFragment view) {
        ApplicationCtx ctx  = (ApplicationCtx) getApplicationContext();
        if (ctx.getJWToken() == null || ctx.getJWToken().equals(""))  {
            loginRedirect();
        } else if (!view.validateRequest()) {
            Toast.makeText(this, "Það þarf að fylla út alla reiti fyrir skráningu", Toast.LENGTH_LONG).show();
            return;
        } else {
            if (view instanceof RegisterEventFragment) new RegistrationRequestHandler<RegisterEventRequest>().execute("/event/registration/event", view.getRequest());
            if (view instanceof RegisterLocationFragment) new RegistrationRequestHandler<RegisterLocationRequest>().execute("/event/registration/location", view.getRequest());
            if (view instanceof RegisterPerformerFragment) new RegistrationRequestHandler<RegisterPerformerRequest>().execute("/event/registration/performer", view.getRequest());
        }
    }

    private void loginRedirect() {
        Toast.makeText(getApplicationContext(), "Notandi þarf að vera innskráður til að framkvæma aðgerð",Toast.LENGTH_LONG).show();
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                finish();
                Intent registration = new Intent(RegistrationAreaActivity.this, UserAuthenticationActivity.class);
                startActivity(registration);
            }
        }, Toast.LENGTH_LONG);
    }

    private void changeRegForm(Fragment newForm, View button) {
        if (button != mSelectedButton)
            mSelectedButton.setBackgroundColor(ContextCompat.getColor(button.getContext(), R.color.darkblue));
        mSelectedButton = (Button) button;
        mSelectedButton.setBackgroundColor(ContextCompat.getColor(button.getContext(), R.color.darkorange));

        getSupportFragmentManager().beginTransaction().remove(regForm).commit();
        regForm = newForm;
        getSupportFragmentManager().beginTransaction().add(R.id.ll_register, regForm).commit();
        getSupportFragmentManager().executePendingTransactions();
    }

    private void setTypeSpinnerSource(TypeItem[] types) {
        typeMap = new HashMap<>();
        for (TypeItem t : types)
            typeMap.put(t.getDescriptionIce(), t.getId());
        if (regForm instanceof RegisterEventFragment)
            ((RegisterEventFragment)regForm).setTypes(typeMap);
    }

    private void setPerformerSpinnerSource(PerformerProjection[] performers) {
        performerMap = new HashMap<>();
        for (PerformerProjection p : performers)
            performerMap.put(p.getName(), p.getId());
        if (regForm instanceof RegisterEventFragment)
            ((RegisterEventFragment)regForm).setPerformers(performerMap);
    }

    private void setLocationSpinnerSource(LocationProjection[] locations) {
        locationMap = new HashMap<>();
        for (LocationProjection l : locations)
            locationMap.put(l.getName(), l.getId());
        if (regForm instanceof RegisterEventFragment)
            ((RegisterEventFragment)regForm).setLocations(locationMap);
    }

    private void handleSpinnerDataResponse(Object[] data) {
        if (data instanceof TypeItem[]) setTypeSpinnerSource((TypeItem[])data);
        if (data instanceof PerformerProjection[]) setPerformerSpinnerSource((PerformerProjection[])data);
        if (data instanceof LocationProjection[]) setLocationSpinnerSource((LocationProjection[])data);
    }

    private class GetSpinnerDataHandler <T> extends AsyncTask<String,Void,T[]>{

        private Class<T[]> className;

        GetSpinnerDataHandler(Class<T[]> className) {
            this.className = className;
        }

        @Override
        protected T[] doInBackground(String... endpoint) {
            try {
                final String fullUrl = url+endpoint[0];
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                return restTemplate.getForObject(fullUrl, className);
            } catch (Exception e) {
                Log.e("GetSpinnerDatasource:\n", e.getMessage(), e);
                return null;
            }
        }

        @Override
        protected void onPostExecute(T[] res) {
            handleSpinnerDataResponse(res);
        }
    }

    private class RegistrationRequestHandler <E> extends AsyncTask<Object, Void, ResponseEntity<String>> {

        ProgressDialog loading;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loading = ProgressDialog.show(RegistrationAreaActivity.this, "Skráning í vinnslu",null, true, true);
        }

        @Override
        protected ResponseEntity<String> doInBackground(Object... params) {
            try {
                final String fullUrl = url+params[0];
                final ApplicationCtx ctx = (ApplicationCtx) getApplicationContext();

                HttpHeaders requestHeaders = new HttpHeaders();
                requestHeaders.setContentType(new MediaType("application","json"));
                requestHeaders.set("token", ctx.getJWToken());
                HttpEntity<E> requestEntity = new HttpEntity<>((E)params[1], requestHeaders);

                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                restTemplate.getMessageConverters().add(new StringHttpMessageConverter());

                return restTemplate.exchange(fullUrl, HttpMethod.POST, requestEntity, String.class);
            } catch (Exception e) {
                Log.e("RegistrationHandler:\n", e.getMessage(), e);
                return null;
            }
        }

        @Override
        protected void onPostExecute(ResponseEntity<String> res) {
            super.onPostExecute(res);
            loading.dismiss();
            if (res != null && res.getStatusCode() == HttpStatus.OK) {
                if (regForm instanceof RegisterLocationFragment) {
                    changeRegForm(new RegisterLocationFragment(), mSelectedButton);
                    new GetSpinnerDataHandler<>(LocationProjection[].class).execute("/event/query/locations");
                } else if (regForm instanceof RegisterPerformerFragment) {
                    changeRegForm(new RegisterPerformerFragment(), mSelectedButton);
                    new GetSpinnerDataHandler<>(PerformerProjection[].class).execute("/event/query/performers");
                } else if (regForm instanceof RegisterEventFragment) {
                    changeRegForm(new RegisterEventFragment(), mSelectedButton);
                    ((RegisterEventFragment)regForm).setTypes(typeMap);
                    ((RegisterEventFragment)regForm).setLocations(locationMap);
                    ((RegisterEventFragment)regForm).setPerformers(performerMap);
                }
                Toast.makeText(getApplicationContext(), "Skráning tókst", Toast.LENGTH_LONG).show();
            }
            else
                Toast.makeText(getApplicationContext(), "Ekki tókst að framkvæma aðgerð", Toast.LENGTH_LONG).show();

        }
    }
}
