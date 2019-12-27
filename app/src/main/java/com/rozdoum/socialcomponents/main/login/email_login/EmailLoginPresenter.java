package com.rozdoum.socialcomponents.main.login.email_login;

import android.content.Context;
import android.text.TextUtils;

import com.rozdoum.socialcomponents.R;
import com.rozdoum.socialcomponents.main.base.BasePresenter;
import com.rozdoum.socialcomponents.utils.ValidationUtil;

public class EmailLoginPresenter extends BasePresenter<EmailLoginView> {

    EmailLoginPresenter(Context context) {
        super(context);
    }

    public void OnLoginClicked(String email, String password) {
        ifViewAttached(view ->
        {
            if (!ValidationUtil.isEmailValid(email))
            {
                view.setEmailError(context.getString(R.string.error_invalid_email));
            }
            else if (TextUtils.isEmpty(password))
            {
                view.setPasswordError(context.getString(R.string.error_invalid_password));
            }
            else
            {
                view.login(email, password);
            }
        });
    }
}
