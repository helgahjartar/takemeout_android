package com.takemeout.android.event;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.takemeout.android.R;
import com.takemeout.android.event.projections.EventDetailProjection;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

/**
 * Created by halldorr on 3/19/17.
 */

public class EventDetailsActivity extends AppCompatActivity {

    private final String url = "https://morning-peak-70516.herokuapp.com";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle b = getIntent().getExtras();
        if(b != null) new GetEventDetailsHandler().execute(b.getInt("eventId"));
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    public void displayDetails(EventDetailProjection event) {
        setContentView(R.layout.activity_event_details);

        TextView mEventNameView = (TextView) this.findViewById(R.id.txtEventName);
        TextView mLocationNameView = (TextView) this.findViewById(R.id.txtLocationName);
        TextView mTimeView = (TextView) this.findViewById(R.id.txtTime);
        TextView mAddressView = (TextView) this.findViewById(R.id.txtAddress);
        TextView mAccessView = (TextView) this.findViewById(R.id.txtAccess);
        TextView mDescriptionView = (TextView) this.findViewById(R.id.txtDescription);

        mEventNameView.setText(event.getName());
        mLocationNameView.setText(event.getLocationName());
        mTimeView.setText(event.getTime().toString());
        mAddressView.setText(event.getAddress());
        mAccessView.setText(event.getAccess());
        mDescriptionView.setText(event.getDescriptionIce());
    }

    private class GetEventDetailsHandler extends AsyncTask<Integer, Void, EventDetailProjection> {

        @Override
        protected EventDetailProjection doInBackground(Integer... eventId) {
            try {
                final String endpoint = url+"/event/query/details";
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                return restTemplate.getForObject(endpoint+"?eventId="+eventId[0], EventDetailProjection.class);
            } catch (Exception e) {
                Log.e("GetRequestHandler:\n", e.getMessage(), e);
                return null;
            }
        }

        @Override
        protected void onPostExecute(EventDetailProjection res) {
            displayDetails(res);
        }
    }
}
