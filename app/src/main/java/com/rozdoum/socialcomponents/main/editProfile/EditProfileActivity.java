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

package com.rozdoum.socialcomponents.main.editProfile;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.rozdoum.socialcomponents.R;
import com.rozdoum.socialcomponents.main.pickImageBase.PickImageActivity;
import com.rozdoum.socialcomponents.utils.GlideApp;
import com.rozdoum.socialcomponents.utils.ImageUtil;

public class EditProfileActivity<V extends EditProfileView, P extends EditProfilePresenter<V>> extends PickImageActivity<V, P> implements EditProfileView {
    private static final String TAG = EditProfileActivity.class.getSimpleName();

    // UI references.
    private EditText firstNameEditText;
    private EditText lastNameEditText;
    private EditText emailEditText;
    private EditText mobilePhoneEditText;
    private EditText streetText;
    private EditText buildingNumberText;
    private EditText postCodeText;
    private EditText cityText;
    private EditText countryText;
    protected ImageView imageView;
    private ProgressBar avatarProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        avatarProgressBar = findViewById(R.id.avatarProgressBar);
        imageView = findViewById(R.id.imageView);
        firstNameEditText = findViewById(R.id.firstNameEditText);
        lastNameEditText = findViewById(R.id.lastNameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        mobilePhoneEditText = findViewById(R.id.phoneEditText);
        streetText = findViewById(R.id.streetEditText);
        buildingNumberText = findViewById(R.id.buildingNumberEditText);
        postCodeText = findViewById(R.id.postCodeEditText);
        cityText = findViewById(R.id.cityEditText);
        countryText = findViewById(R.id.countryEditText);

        imageView.setOnClickListener(this::onSelectImageClick);

        initContent();
    }

    @NonNull
    @Override
    public P createPresenter() {
        if (presenter == null) {
            return (P) new EditProfilePresenter(this);
        }
        return presenter;
    }

    protected void initContent() {
        presenter.loadProfile();
    }

    @Override
    public ProgressBar getProgressView() {
        return avatarProgressBar;
    }

    @Override
    public ImageView getImageView() {
        return imageView;
    }

    @Override
    public void onImagePikedAction() {
        startCropImageActivity();
    }

    @Override
    @SuppressLint("NewApi")
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // handle result of pick image chooser
        super.onActivityResult(requestCode, resultCode, data);
        handleCropImageResult(requestCode, resultCode, data);
    }

    @Override
    public void setProfilePhoto(String photoUrl) {
        ImageUtil.loadImage(GlideApp.with(this), photoUrl, imageView, new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                avatarProgressBar.setVisibility(View.GONE);
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                avatarProgressBar.setVisibility(View.GONE);
                return false;
            }
        });
    }

    @Override
    public void setFirstName(String firstName) {
        firstNameEditText.setText(firstName);
    }

    @Override
    public String getFirstNameText() {
        return firstNameEditText.getText().toString();
    }

    @Override
    public void setFirstNameError(String string) {
        firstNameEditText.setError(string);
        firstNameEditText.requestFocus();
    }

    @Override
    public void setLastName(String lastName) {
        lastNameEditText.setText(lastName);
    }

    @Override
    public String getLastNameText() {
        return lastNameEditText.getText().toString();
    }

    @Override
    public void setLastNameError(String string) {
        lastNameEditText.setError(string);
        lastNameEditText.requestFocus();
    }

    @Override
    public void setEmail(String email) {
        emailEditText.setText(email);
    }

    @Override
    public void setPhoneNumber(String phoneNumber) {
        mobilePhoneEditText.setText(phoneNumber);
    }

    @Override
    public String getPhoneNumberText() {
        return mobilePhoneEditText.getText().toString();
    }

    @Override
    public void setPhoneNumberError(@Nullable String string) {
        mobilePhoneEditText.setError(string);
        mobilePhoneEditText.requestFocus();
    }

    @Override
    public void setStreet(String street) {
        streetText.setText(street);
    }

    @Override
    public String getStreetText() {
        return streetText.getText().toString();
    }

    @Override
    public void setStreetError(String string) {
        streetText.setError(string);
        streetText.requestFocus();
    }

    @Override
    public void setBuildingNumber(String buildingNumber) {
        buildingNumberText.setText(buildingNumber);
    }

    @Override
    public String getBuildingNumberText() {
        return buildingNumberText.getText().toString();
    }

    @Override
    public void setBuildingNumberError(String string) {
        buildingNumberText.setError(string);
        buildingNumberText.requestFocus();
    }

    @Override
    public void setPostCode(String postCode) {
        postCodeText.setText(postCode);
    }

    @Override
    public String getPostCodeText() {
        return postCodeText.getText().toString();
    }

    @Override
    public void setPostCodeError(String string) {
        postCodeText.setError(string);
        postCodeText.requestFocus();
    }

    @Override
    public void setCity(String city) {
        cityText.setText(city);
    }

    @Override
    public String getCityText() {
        return cityText.getText().toString();
    }

    @Override
    public void setCityError(String string) {
        cityText.setError(string);
        cityText.requestFocus();
    }

    @Override
    public void setCountry(String country) {
        countryText.setText(country);
    }

    @Override
    public String getCountryText() {
        return countryText.getText().toString();
    }

    @Override
    public void setCountryError(String string) {
        countryText.setError(string);
        countryText.requestFocus();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.edit_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.save:
                presenter.attemptCreateProfile(imageUri);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

