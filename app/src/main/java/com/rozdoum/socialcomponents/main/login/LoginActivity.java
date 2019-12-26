/*
 * Copyright 2018 Rozdoum
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.rozdoum.socialcomponents.main.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.rozdoum.socialcomponents.R;
import com.rozdoum.socialcomponents.main.base.BaseActivity;
import com.rozdoum.socialcomponents.main.editProfile.createProfile.CreateProfileActivity;
import com.rozdoum.socialcomponents.main.login.email_login.EmailLoginFragment;
import com.rozdoum.socialcomponents.main.login.email_register.EmailRegisterFragment;
import com.rozdoum.socialcomponents.main.login.email_register.RegisterUserData;
import com.rozdoum.socialcomponents.utils.GoogleApiHelper;
import com.rozdoum.socialcomponents.utils.LogUtil;
import com.rozdoum.socialcomponents.utils.LogoutHelper;

import java.util.Arrays;
import java.util.Stack;

public class LoginActivity extends BaseActivity<LoginView, LoginPresenter> implements LoginView, GoogleApiClient.OnConnectionFailedListener, OnFragmentFinishedListener {
    private static final String TAG = LoginActivity.class.getSimpleName();
    private static final int SIGN_IN_GOOGLE = 9001;
    public static final int LOGIN_REQUEST_CODE = 10001;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private GoogleApiClient mGoogleApiClient;

    private CallbackManager mCallbackManager;
    private String profilePhotoUrlLarge;

    private Stack<OnBackPressedListener> backPressListeners = new Stack<OnBackPressedListener>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        setFragmentListener();
        changeEmailLoginFragmentVisibility(false);
        changeEmailRegisterFragmentVisibility(false);

        initGoogleSignIn();
        initFirebaseAuth();
        initFacebookSignIn();
        initEmailSignIn();
    }

    private void initGoogleSignIn() {
        mGoogleApiClient = GoogleApiHelper.createGoogleApiClient(this);
        mAuth = FirebaseAuth.getInstance();

        findViewById(R.id.googleSignInButton).setOnClickListener(view -> presenter.onGoogleSignInClick());
    }

    private void initFirebaseAuth() {
        mAuth = FirebaseAuth.getInstance();

        if (mAuth.getCurrentUser() != null) {
            LogoutHelper.signOut(mGoogleApiClient, this);
        }

        mAuthListener = firebaseAuth -> {
            final FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user != null) {
                // Profile is signed in
                LogUtil.logDebug(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                presenter.checkIsProfileExist(user.getUid());
                setResult(RESULT_OK);
            } else {
                // Profile is signed out
                LogUtil.logDebug(TAG, "onAuthStateChanged:signed_out");
            }
        };
    }

    private void initFacebookSignIn() {
        mCallbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                LogUtil.logDebug(TAG, "facebook:onSuccess:" + loginResult);
                presenter.handleFacebookSignInResult(loginResult);
            }

            @Override
            public void onCancel() {
                LogUtil.logDebug(TAG, "facebook:onCancel");
            }

            @Override
            public void onError(FacebookException error) {
                LogUtil.logError(TAG, "facebook:onError", error);
                showSnackBar(error.getMessage());
            }
        });

        findViewById(R.id.facebookSignInButton).setOnClickListener(v -> presenter.onFacebookSignInClick());
    }

    private void initEmailSignIn() {
        mAuth = FirebaseAuth.getInstance();

        findViewById(R.id.emailSignInButton).setOnClickListener(view -> presenter.onEmailSignInClick());
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);

        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }

        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.stopAutoManage(this);
            mGoogleApiClient.disconnect();
        }
    }

    @NonNull
    @Override
    public LoginPresenter createPresenter() {
        if (presenter == null) {
            return new LoginPresenter(this);
        }
        return presenter;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        mCallbackManager.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == SIGN_IN_GOOGLE) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            presenter.handleGoogleSignInResult(result);
        }
    }

    @Override
    public void startCreateProfileActivity() {
        Intent intent = new Intent(LoginActivity.this, CreateProfileActivity.class);
        intent.putExtra(CreateProfileActivity.LARGE_IMAGE_URL_EXTRA_KEY, profilePhotoUrlLarge);
        startActivity(intent);
    }

    @Override
    public void firebaseAuthWithCredentials(AuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    LogUtil.logDebug(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

                    // If sign in fails, display a message to the user. If sign in succeeds
                    // the auth state listener will be notified and logic to handle the
                    // signed in user can be handled in the listener.
                    if (!task.isSuccessful()) {
                        presenter.handleAuthError(task);
                    }
                });
    }

    @Override
    public void setProfilePhotoUrl(String url) {
        profilePhotoUrlLarge = url;
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        LogUtil.logDebug(TAG, "onConnectionFailed:" + connectionResult);
        showSnackBar(R.string.error_google_play_services);
        hideProgress();
    }

    @Override
    public void signInWithGoogle() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, SIGN_IN_GOOGLE);
    }

    @Override
    public void signInWithFacebook() {
        LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList("email", "public_profile"));
    }

    @Override
    public void signInWithEmail() {
        changeButtonContainerVisibility(false);
        changeEmailLoginFragmentVisibility(true);
        changeEmailRegisterFragmentVisibility(false);
    }

    @Override
    public void registerNewUserLink() {
        changeButtonContainerVisibility(false);
        changeEmailLoginFragmentVisibility(false);
        changeEmailRegisterFragmentVisibility(true);
    }

    @Override
    public void registerNewUser(RegisterUserData userData) {
        mAuth.createUserWithEmailAndPassword(userData.email, userData.password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful())
                    {
                        LogUtil.logInfo(TAG, "Successfully registered");
                        showSnackBar("Successfully registered in");
                    } else {
                        LogUtil.logDebug(TAG, "Failed to register");
                        showSnackBar("Failed to register");
                        hideProgress();
                    }
                });
    }

    @Override
    public void login(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(LoginActivity.this, task -> {
                    if (!task.isSuccessful()) {
                        LogUtil.logDebug(TAG, "Sign in with email failed!");
                        showSnackBar("Sign in with email failed!");
                        hideProgress();
                    }
                });
    }

    @Override
    public void OnFragmentFinished(int fragmentId) {
        if (fragmentId == EmailLoginFragment.FRAGMENT_ID)
        {
            changeEmailLoginFragmentVisibility(false);
            changeEmailRegisterFragmentVisibility(false);
            changeButtonContainerVisibility(true);

        }
        else if (fragmentId == EmailRegisterFragment.FRAGMENT_ID)
        {

            changeEmailRegisterFragmentVisibility(false);
            changeEmailLoginFragmentVisibility(true);
            changeButtonContainerVisibility(false);
        }
    }

    @Override
    public void OnRegisterNewUserLinkClicked(int fragmentId) {
        if (fragmentId == EmailLoginFragment.FRAGMENT_ID)
        {
            presenter.OnRegisterNewUserLinkClick();
        }
    }

    @Override
    public void OnLoginClicked(String email, String password) {
        presenter.OnLoginClick(email, password);
    }

    @Override
    public void OnRegisterClicked(RegisterUserData userData) {
        presenter.OnRegisterClick(userData);
    }


    private void setFragmentListener() {
        FragmentManager manager = getSupportFragmentManager();
        EmailLoginFragment emailLogin = (EmailLoginFragment) manager.findFragmentById(R.id.fragment_login_place);
        assert emailLogin != null;
        emailLogin.setOnFragmentFinishListener(this);

        EmailRegisterFragment emailRegister = (EmailRegisterFragment) manager.findFragmentById(R.id.fragment_register_place);
        assert emailRegister != null;
        emailRegister.setOnFragmentFinishListener(this);
    }

    private void changeButtonContainerVisibility(boolean isVisible)
    {
        LinearLayout buttonContainer = findViewById(R.id.buttonsContainer);
        if (isVisible)
        {
            buttonContainer.setVisibility(View.VISIBLE);
        }
        else
        {
            buttonContainer.setVisibility(View.GONE);
        }
    }

    private void changeEmailRegisterFragmentVisibility(boolean isVisible) {

        FragmentManager manager = getSupportFragmentManager();
        Fragment emailRegister = manager.findFragmentById(R.id.fragment_register_place);
        LinearLayout fragmentLoginLayout = findViewById(R.id.fragment_register_layout);
        if (emailRegister != null)
        {
            FragmentTransaction ft = manager.beginTransaction();
            if (isVisible)
            {
                addBackPressListener((OnBackPressedListener) emailRegister);
                fragmentLoginLayout.setVisibility(View.VISIBLE);
                ft.show(emailRegister);
            } else {
                removeBackPressListener((OnBackPressedListener) emailRegister);
                fragmentLoginLayout.setVisibility(View.GONE);
                ft.hide(emailRegister);
            }
            ft.commit();
        }
    }

    private void changeEmailLoginFragmentVisibility(boolean isVisible) {

        FragmentManager manager = getSupportFragmentManager();
        Fragment emailLogin = manager.findFragmentById(R.id.fragment_login_place);
        LinearLayout fragmentLoginLayout = findViewById(R.id.fragment_login_layout);
        if (emailLogin != null)
        {
            FragmentTransaction ft = manager.beginTransaction();
            if (isVisible)
            {
                addBackPressListener((OnBackPressedListener) emailLogin);
                fragmentLoginLayout.setVisibility(View.VISIBLE);
                ft.show(emailLogin);
            } else {
                removeBackPressListener((OnBackPressedListener) emailLogin);
                fragmentLoginLayout.setVisibility(View.GONE);
                ft.hide(emailLogin);
            }
            ft.commit();
        }
    }

    @Override
    public void onBackPressed() {

        for(OnBackPressedListener backPressListener : backPressListeners) {
            if(backPressListener.onBackPressed()) return;
        }

        super.onBackPressed();
    }

    public void addBackPressListener(OnBackPressedListener backPressListener) {
        backPressListeners.add(backPressListener);
    }

    public void removeBackPressListener(OnBackPressedListener backPressListener) {
        backPressListeners.remove(backPressListener);
    }
}

