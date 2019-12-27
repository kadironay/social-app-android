package com.rozdoum.socialcomponents.main.login.email_register;

import android.content.Context;

import com.rozdoum.socialcomponents.R;
import com.rozdoum.socialcomponents.main.base.BasePresenter;
import com.rozdoum.socialcomponents.utils.ValidationUtil;

public class EmailRegisterPresenter extends BasePresenter<EmailRegisterView> {

    EmailRegisterPresenter(Context context) {
        super(context);
    }

    public void OnRegisterClicked(RegisterUserData userData) {
        ifViewAttached(view ->
        {
            if (!ValidationUtil.isEmailValid(userData.email))
            {
                view.setEmailError(context.getString(R.string.error_invalid_email));
            }
            else if (!ValidationUtil.isPasswordValid(userData.password))
            {
                view.setPasswordError(context.getString(R.string.error_invalid_password));
            }
            else if (!userData.password.equals(userData.rePassword))
            {
                view.setConfirmPasswordError(context.getString(R.string.error_invalid_re_password));
            }
            else
            {
                view.register(userData);
            }
        });
    }
}
