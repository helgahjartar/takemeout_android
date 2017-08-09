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
import com.takemeout.android.registration.requests.RegisterPerformerRequest;

/**
 * Created by halldorr on 4/2/17.
 */

public class RegisterPerformerFragment extends Fragment implements IRegistrationFragment {

    private EditText mEdtName;
    private EditText mEdtDescriptionEng;
    private EditText mEdtDescriptionIce;

    private IRegistrationListener mListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_register_performer, container, false);
        mEdtName = (EditText) v.findViewById(R.id.edtPerformerName);
        mEdtDescriptionEng = (EditText) v.findViewById(R.id.edtDescriptionEng);
        mEdtDescriptionIce = (EditText) v.findViewById(R.id.edtDescriptionIce);

        Button mBtnSubmit = (Button) v.findViewById(R.id.btnSubmit);
        final IRegistrationFragment frag = this;

        mBtnSubmit.setOnClickListener(new View.OnClickListener(){
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

    public RegisterPerformerRequest getRequest() {
        RegisterPerformerRequest req =  new RegisterPerformerRequest();

        req.setName(mEdtName.getText().toString());
        req.setDescriptionEng(mEdtDescriptionEng.getText().toString());
        req.setDescriptionIce(mEdtDescriptionIce.getText().toString());

        return req;
    }


    public boolean validateRequest() {
        RegisterPerformerRequest req = getRequest();

        if(req.getName() == null || req.getName().equals("")) return false;
        if(req.getDescriptionEng() == null || req.getDescriptionEng().equals("")) return false;
        if(req.getDescriptionIce() == null || req.getDescriptionIce().equals("")) return false;

        return true;
    }
}
