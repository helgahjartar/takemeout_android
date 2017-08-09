package com.takemeout.android.event;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.takemeout.android.R;
import com.takemeout.android.event.projections.EventOverviewProjection;

/**
 * Created by halldorr on 3/18/17.
 */

public class EventListEntryFragment extends Fragment {

    private int eventId;
    private String eventName;
    private String locationName;
    private String typeDescription;
    private IOpenEventDetailsListener mListener;

    public static EventListEntryFragment newInstance(EventOverviewProjection event) {
        Bundle bundle = new Bundle();
        bundle.putInt("eventId", event.getId());
        bundle.putString("eventName", event.getName());
        bundle.putString("locationName", event.getLocationName());
        bundle.putString("typeDescription", event.getTypeDescriptionIce());
        EventListEntryFragment entry = new EventListEntryFragment();
        entry.setArguments(bundle);
        return entry;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_event_list_entry, container, false);

        TextView txtEventName = (TextView) v.findViewById(R.id.txt_event_name);
        TextView txtLocationName = (TextView) v.findViewById(R.id.txt_location_name);
        TextView txtEventType = (TextView) v.findViewById(R.id.txt_event_type);
        LinearLayout entryContainer = (LinearLayout) v.findViewById(R.id.entry_container);

        entryContainer.setOnClickListener(new LinearLayout.OnClickListener() {
            public void onClick(View v) {
                mListener.onOpenEventDetails(eventId);
            }
        });

        txtEventName.setText(eventName);
        txtLocationName.setText(locationName);
        txtEventType.setText(typeDescription);

        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof IOpenEventDetailsListener) {
            mListener = (IOpenEventDetailsListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        eventId = getArguments().getInt("eventId");
        eventName = getArguments().getString("eventName");
        locationName = getArguments().getString("locationName");
        typeDescription = getArguments().getString("typeDescription");
    }
}
