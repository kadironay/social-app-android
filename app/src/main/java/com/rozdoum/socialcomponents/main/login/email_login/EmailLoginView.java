package com.rozdoum.socialcomponents.main.login.email_login;

import com.rozdoum.socialcomponents.main.base.BaseFragmentView;

public interface EmailLoginView extends BaseFragmentView {
    void login(String email, String password);

    void setEmailError(String error);

    void setPasswordError(String error);
}
