package com.takemeout.android.event;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageButton;

import com.takemeout.android.R;
import com.takemeout.android.event.projections.EventOverviewProjection;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

public class EventOverviewActivity extends AppCompatActivity implements IOpenEventDetailsListener {

    private ImageButton mCalendarButton;

    private final String url = "http://morning-peak-70516.herokuapp.com";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        new GetEventsHandler().execute();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    public void onOpenEventDetails(int eventId) {
        Intent intent = new Intent(EventOverviewActivity.this, EventDetailsActivity.class);
        Bundle b = new Bundle();
        b.putInt("eventId", eventId);
        intent.putExtras(b);
        startActivity(intent);
    }

    private class GetEventsHandler extends AsyncTask<Void,Void,EventOverviewProjection[]> {

        @Override
        protected EventOverviewProjection[] doInBackground(Void... params) {
            try {
                final String endpoint = url+"/event/query/events";
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                return restTemplate.getForObject(endpoint, EventOverviewProjection[].class);
            } catch (Exception e) {
                Log.e("GetRequestHandler:\n", e.getMessage(), e);
                return null;
            }
        }

        @Override
        protected void onPostExecute(EventOverviewProjection[] res) {
            EventListFragment fragment = (EventListFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_event_list);
            if (fragment != null) fragment.displayEvents(res);
        }
    }
}
