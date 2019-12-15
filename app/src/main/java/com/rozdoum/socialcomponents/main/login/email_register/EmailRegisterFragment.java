package com.rozdoum.socialcomponents.main.login.email_register;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import com.rozdoum.socialcomponents.R;
import com.rozdoum.socialcomponents.main.login.OnFragmentFinishedListener;

public class EmailRegisterFragment extends Fragment {

    public static final int FRAGMENT_ID = 5002;
    private OnFragmentFinishedListener fragmentFinishedListener;
    private EditText emailDescription;
    private EditText passwordDescription;
    private EditText rePasswordDescription;

    public EmailRegisterFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_register, container, false);

        emailDescription = view.findViewById(R.id.et_email);
        passwordDescription = view.findViewById(R.id.et_password);
        rePasswordDescription = view.findViewById(R.id.et_repassword);

        ImageView buttonBack = view.findViewById(R.id.button_back);
        buttonBack.setOnClickListener(view1 -> {

            if (fragmentFinishedListener != null)
            {
                fragmentFinishedListener.OnFragmentFinished(FRAGMENT_ID);
            }
        });

        Button register = view.findViewById(R.id.btn_register);
        register.setOnClickListener(view12 -> {
            RegisterUserData user = new RegisterUserData();
            user.email = emailDescription.getText().toString();
            user.password = passwordDescription.getText().toString();
            user.rePassword = rePasswordDescription.getText().toString();
            fragmentFinishedListener.OnRegisterClicked(user);
        });

        return view;
    }

    public void setOnFragmentFinishListener(OnFragmentFinishedListener listener) {
        fragmentFinishedListener = listener;
    }
}
