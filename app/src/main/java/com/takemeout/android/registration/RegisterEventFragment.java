package com.takemeout.android.registration;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.takemeout.android.R;
import com.takemeout.android.registration.requests.RegisterEventRequest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

/**
 * Created by halldorr on 4/2/17.
 */

public class RegisterEventFragment extends Fragment implements IRegistrationFragment {

    private HashMap<String, Integer> typeMap;
    private HashMap<String, Integer> locationMap;
    private HashMap<String, Integer> performerMap;

    private EditText mEdtName;
    private EditText mEdtDescriptionEng;
    private EditText mEdtDescriptionIce;
    private static TextView mEdtDate;
    private Spinner mTypesSpinner;
    private Spinner mLocationsSpinner;
    private Spinner mPerformersSpinner;

    private IRegistrationListener mListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_register_event, container, false);
        mEdtName = (EditText) v.findViewById(R.id.edtEventName);
        mEdtDescriptionEng = (EditText) v.findViewById(R.id.edtDescriptionEng);
        mEdtDescriptionIce = (EditText) v.findViewById(R.id.edtDescriptionIce);
        mEdtDate = (TextView) v.findViewById(R.id.edtEventDate);
        mTypesSpinner = (Spinner) v.findViewById(R.id.spType);
        mLocationsSpinner = (Spinner) v.findViewById(R.id.spLocation);
        mPerformersSpinner = (Spinner) v.findViewById(R.id.spPerformers);

        Button mBtnSubmit = (Button) v.findViewById(R.id.btnSubmit);
        final IRegistrationFragment frag = this;

        mBtnSubmit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mListener.handleRegistration(frag);
            }
        });

        mEdtDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                DialogFragment newFragment = new EventCalFragment();
                newFragment.show(getFragmentManager(), "DatePicker");

            }
        });

        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof IRegistrationListener) {
            mListener = (IRegistrationListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void setTypes(HashMap<String, Integer> typeMap) {
        this.typeMap = typeMap;
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, new Vector<>(typeMap.keySet()));
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mTypesSpinner.setAdapter(dataAdapter);
    }

    public void setLocations(HashMap<String, Integer> locationMap) {
        this.locationMap = locationMap;
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, new Vector<>(locationMap.keySet()));
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mLocationsSpinner.setAdapter(dataAdapter);
    }

    public void setPerformers(HashMap<String, Integer> performerMap) {
        this.performerMap = performerMap;
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, new Vector<>(performerMap.keySet()));
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mPerformersSpinner.setAdapter(dataAdapter);
    }

    public RegisterEventRequest getRequest() {
        RegisterEventRequest req = new RegisterEventRequest();

        String typeKey= mTypesSpinner.getSelectedItem().toString();
        String locationKey = mLocationsSpinner.getSelectedItem().toString();
        String performerKey = mPerformersSpinner.getSelectedItem().toString();

        req.setName(mEdtName.getText().toString());
        req.setDescriptionEng(mEdtDescriptionEng.getText().toString());
        req.setDescriptionIce(mEdtDescriptionIce.getText().toString());
        try {
            req.setTime(convertToDate(mEdtDate.getText().toString()));
        } catch (ParseException e) {
            System.out.println(e);
        }
        req.setTypeId(typeMap.get(typeKey));
        req.setLocationId(locationMap.get(locationKey));
        List<Integer> performerIds = new Vector<>();
        performerIds.add(performerMap.get(performerKey));
        req.setPerformerIds(performerIds);

        return req;
    }

    public boolean validateRequest() {
        RegisterEventRequest req = getRequest();

        if (req.getName() == null || req.getName().equals("")) return false;
        if (req.getDescriptionEng() == null || req.getDescriptionEng().equals("")) return false;
        if (req.getDescriptionIce() == null || req.getDescriptionIce().equals("")) return false;
        if (req.getTime() == null) return false;
        if (req.getLocationId() < 1) return false;
        if (req.getTypeId() < 1 ) return false;
        if (req.getPerformerIds() == null || req.getPerformerIds().isEmpty()) return false;

        return true;
    }

    public static class EventCalFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener{

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar calendar = Calendar.getInstance();
            int yy = calendar.get(Calendar.YEAR);
            int mm = calendar.get(Calendar.MONTH);
            int dd = calendar.get(Calendar.DAY_OF_MONTH);
            return new DatePickerDialog(getActivity(), this, yy, mm, dd);
        }

        public void onDateSet(DatePicker view, int yy, int mm, int dd) {
            populateSetDate(yy, mm+1, dd);
        }
        public void populateSetDate(int year, int month, int day) {
            mEdtDate.setText(year + "-" + month + "-" + day);
        }

    }

    public java.sql.Date convertToDate(String time) throws ParseException{
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date parsed = format.parse(time);
        java.sql.Date sql = new java.sql.Date(parsed.getTime());
        return sql;
    }
}
