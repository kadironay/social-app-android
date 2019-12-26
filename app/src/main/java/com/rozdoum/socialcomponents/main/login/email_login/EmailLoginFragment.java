package com.rozdoum.socialcomponents.main.login.email_login;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.rozdoum.socialcomponents.R;
import com.rozdoum.socialcomponents.main.base.BaseFragment;
import com.rozdoum.socialcomponents.main.login.OnBackPressedListener;
import com.rozdoum.socialcomponents.main.login.OnFragmentFinishedListener;

import java.util.Objects;

public class EmailLoginFragment extends BaseFragment<EmailLoginView, EmailLoginPresenter>
        implements EmailLoginView, OnBackPressedListener {

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
        buttonBack.setOnClickListener(view1 -> doBack());

        Button login = view.findViewById(R.id.btn_login);
        login.setOnClickListener(view12 -> {
            if (fragmentFinishedListener != null)
            {
                hideKeyboardFrom(Objects.requireNonNull(getContext()), view12);
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
                hideKeyboardFrom(Objects.requireNonNull(getContext()), view13);
                fragmentFinishedListener.OnRegisterNewUserLinkClicked(FRAGMENT_ID);
            }
        });

        return view;
    }

    public void setOnFragmentFinishListener(OnFragmentFinishedListener listener) {
        fragmentFinishedListener = listener;
    }

    @Override
    public boolean onBackPressed() {
        return doBack();
    }

    private static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
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
