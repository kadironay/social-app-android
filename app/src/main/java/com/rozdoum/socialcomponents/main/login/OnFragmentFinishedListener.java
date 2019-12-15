package com.rozdoum.socialcomponents.main.login;

import com.rozdoum.socialcomponents.main.login.email_register.RegisterUserData;

public interface OnFragmentFinishedListener {

    void OnFragmentFinished(int fragmentId);

    void OnRegisterNewUserLinkClicked(int fragmentId);

    void OnLoginClicked(String email, String password);

    void OnRegisterClicked(RegisterUserData userData);
}
