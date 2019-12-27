package com.rozdoum.socialcomponents.main.login.email_register;

import com.rozdoum.socialcomponents.main.base.BaseFragmentView;

public interface EmailRegisterView extends BaseFragmentView {
    void register(RegisterUserData userData);

    void setEmailError(String string);

    void setPasswordError(String string);

    void setConfirmPasswordError(String string);
}
