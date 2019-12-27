package com.rozdoum.socialcomponents.main.login.email_register;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.rozdoum.socialcomponents.R;
import com.rozdoum.socialcomponents.main.base.BaseFragment;
import com.rozdoum.socialcomponents.main.login.OnBackPressedListener;
import com.rozdoum.socialcomponents.main.login.OnFragmentFinishedListener;

public class EmailRegisterFragment extends BaseFragment<EmailRegisterView, EmailRegisterPresenter>
        implements EmailRegisterView, OnBackPressedListener {

    public static final int FRAGMENT_ID = 5002;
    private OnFragmentFinishedListener fragmentFinishedListener;
    private EditText emailDescription;
    private EditText passwordDescription;
    private EditText rePasswordDescription;

    public EmailRegisterFragment() {
        // Required empty public constructor
    }

    @NonNull
    @Override
    public EmailRegisterPresenter createPresenter() {
        if (presenter == null) {
            return new EmailRegisterPresenter(getContext());
        }
        return presenter;
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
        buttonBack.setOnClickListener(view1 -> doBack());

        Button register = view.findViewById(R.id.btn_register);
        register.setOnClickListener(view12 -> {
            RegisterUserData user = new RegisterUserData();
            user.email = emailDescription.getText().toString();
            user.password = passwordDescription.getText().toString();
            user.rePassword = rePasswordDescription.getText().toString();
            presenter.OnRegisterClicked(user);
        });

        return view;
    }

    public void setOnFragmentFinishListener(OnFragmentFinishedListener listener) {
        fragmentFinishedListener = listener;
    }

    public void register(RegisterUserData userData)
    {
        fragmentFinishedListener.OnRegisterClicked(userData);
    }

    @Override
    public void setEmailError(String string) {
        emailDescription.setError(string);
        emailDescription.requestFocus();
    }

    @Override
    public void setPasswordError(String string) {
        passwordDescription.setError(string);
        passwordDescription.requestFocus();
    }

    @Override
    public void setConfirmPasswordError(String string) {
        rePasswordDescription.setError(string);
        rePasswordDescription.requestFocus();
    }

    @Override
    public boolean onBackPressed() {
        return doBack();
    }

    private boolean doBack()
    {
        if (fragmentFinishedListener != null)
        {
            fragmentFinishedListener.OnFragmentFinished(FRAGMENT_ID);
            return true;
        }
        return false;
    }
}
