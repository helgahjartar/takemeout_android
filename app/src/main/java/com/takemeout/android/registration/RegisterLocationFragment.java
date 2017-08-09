package com.takemeout.android.registration;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.takemeout.android.R;
import com.takemeout.android.registration.requests.RegisterLocationRequest;

/**
 * Created by halldorr on 4/2/17.
 */

public class RegisterLocationFragment extends Fragment implements IRegistrationFragment {

    private EditText mEdtName;
    private EditText mEdtAddress;
    private EditText mEdtAccess;

    private IRegistrationListener mListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_register_location, container, false);
        mEdtName = (EditText) v.findViewById(R.id.edtLocationName);
        mEdtAddress = (EditText) v.findViewById(R.id.edtAddress);
        mEdtAccess = (EditText) v.findViewById(R.id.edtAccess);

        Button mBtnSubmit = (Button) v.findViewById(R.id.btnSubmit);
        final IRegistrationFragment frag = this;

        mBtnSubmit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mListener.handleRegistration(frag);
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

    public RegisterLocationRequest getRequest() {
        RegisterLocationRequest req =  new RegisterLocationRequest();

        req.setName(mEdtName.getText().toString());
        req.setAddress(mEdtAddress.getText().toString());
        req.setAccess(mEdtAccess.getText().toString());

        return req;
    }


    public boolean validateRequest() {
        RegisterLocationRequest req = getRequest();

        if(req.getName() == null || req.getName().equals("")) return false;
        if(req.getAddress() == null || req.getAddress().equals("")) return false;
        if(req.getAccess() == null || req.getAccess().equals("")) return false;

        return true;
    }
}
