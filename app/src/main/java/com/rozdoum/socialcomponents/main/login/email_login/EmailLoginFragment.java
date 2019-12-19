package com.rozdoum.socialcomponents.main.login.email_login;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.rozdoum.socialcomponents.R;
import com.rozdoum.socialcomponents.main.base.BaseFragment;
import com.rozdoum.socialcomponents.main.login.OnFragmentFinishedListener;

public class EmailLoginFragment extends BaseFragment<EmailLoginView, EmailLoginPresenter>
        implements EmailLoginView {

    public static final int FRAGMENT_ID = 5001;
    private OnFragmentFinishedListener fragmentFinishedListener;
    private EditText emailDescription;
    private EditText passwordDescription;

    public EmailLoginFragment() {

    }

    @NonNull
    @Override
    public EmailLoginPresenter createPresenter() {
        if (presenter == null) {
            return new EmailLoginPresenter(getContext());
        }
        return presenter;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        emailDescription = view.findViewById(R.id.et_email);
        passwordDescription = view.findViewById(R.id.et_password);

        ImageView buttonBack = view.findViewById(R.id.button_back);
        buttonBack.setOnClickListener(view1 -> {

            if (fragmentFinishedListener != null)
            {
                fragmentFinishedListener.OnFragmentFinished(FRAGMENT_ID);
            }
        });

        Button login = view.findViewById(R.id.btn_login);
        login.setOnClickListener(view12 -> {
            if (fragmentFinishedListener != null)
            {
                fragmentFinishedListener.OnLoginClicked(
                        emailDescription.getText().toString(),
                        passwordDescription.getText().toString()
                );
            }
        });

        TextView registerNewUser = view.findViewById(R.id.registerWithEmail);
        registerNewUser.setOnClickListener(view13 -> {
            if (fragmentFinishedListener != null)
            {
                fragmentFinishedListener.OnRegisterNewUserLinkClicked(FRAGMENT_ID);
            }
        });

        return view;
    }

    public void setOnFragmentFinishListener(OnFragmentFinishedListener listener) {
        fragmentFinishedListener = listener;
    }
}
