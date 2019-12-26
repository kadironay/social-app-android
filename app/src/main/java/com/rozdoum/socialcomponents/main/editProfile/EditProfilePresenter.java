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

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;

import com.rozdoum.socialcomponents.R;
import com.rozdoum.socialcomponents.main.base.BaseView;
import com.rozdoum.socialcomponents.main.pickImageBase.PickImagePresenter;
import com.rozdoum.socialcomponents.managers.ProfileManager;
import com.rozdoum.socialcomponents.managers.listeners.OnObjectChangedListenerSimple;
import com.rozdoum.socialcomponents.model.Profile;
import com.rozdoum.socialcomponents.utils.ValidationUtil;

/**
 * Created by Alexey on 03.05.18.
 */

public class EditProfilePresenter<V extends EditProfileView> extends PickImagePresenter<V> {

    protected Profile profile;
    protected ProfileManager profileManager;

    protected EditProfilePresenter(Context context) {
        super(context);
        profileManager = ProfileManager.getInstance(context.getApplicationContext());

    }

    public void loadProfile() {
        ifViewAttached(BaseView::showProgress);
        profileManager.getProfileSingleValue(getCurrentUserId(), new OnObjectChangedListenerSimple<Profile>() {
            @Override
            public void onObjectChanged(Profile obj) {
                profile = obj;
                ifViewAttached(view -> {
                    if (profile != null) {
                        view.setFirstName(profile.getFirstName());
                        view.setLastName(profile.getLastName());
                        view.setEmail(profile.getEmail());
                        view.setPhoneNumber(profile.getPhoneNumber());
                        view.setStreet(profile.getStreet());
                        view.setBuildingNumber(profile.getBuildingNumber());
                        view.setPostCode(profile.getPostCode());
                        view.setCity(profile.getCity());
                        view.setCountry(profile.getCountry());

                        if (profile.getPhotoUrl() != null) {
                            view.setProfilePhoto(profile.getPhotoUrl());
                        }
                    }

                    view.hideProgress();
                    view.setFirstNameError(null);
                });
            }
        });
    }

    public void attemptCreateProfile(Uri imageUri) {
        if (checkInternetConnection()) {
            ifViewAttached(view -> {
                view.setFirstNameError(null);

                String firstName = view.getFirstNameText().trim();
                String lastName = view.getLastNameText().trim();
                String phoneNumber = view.getPhoneNumberText().trim();
                String street = view.getStreetText().trim();
                String buildingNumber = view.getBuildingNumberText().trim();
                String postCode = view.getPostCodeText().trim();
                String city = view.getCityText().trim();
                String country = view.getCountryText().trim();

                boolean cancel = false;

                if (TextUtils.isEmpty(firstName)) {
                    view.setFirstNameError(context.getString(R.string.error_field_required));
                    cancel = true;
                } else if (!ValidationUtil.isNameValid(firstName)) {
                    view.setFirstNameError(context.getString(R.string.error_profile_name_length));
                    cancel = true;
                }

                if (TextUtils.isEmpty(lastName)) {
                    view.setLastNameError(context.getString(R.string.error_field_required));
                    cancel = true;
                } else if (!ValidationUtil.isNameValid(lastName)) {
                    view.setLastNameError(context.getString(R.string.error_profile_name_length));
                    cancel = true;
                }

                if (TextUtils.isEmpty(phoneNumber)) {
                    view.setPhoneNumberError(context.getString(R.string.error_field_required));
                    cancel = true;
                } else if (!ValidationUtil.isPhoneNumberValid(phoneNumber)) {
                    view.setPhoneNumberError(context.getString(R.string.error_invalid_phone_number));
                    cancel = true;
                }

                if (TextUtils.isEmpty(street)) {
                    view.setStreetError(context.getString(R.string.error_field_required));
                    cancel = true;
                }

                if (TextUtils.isEmpty(buildingNumber)) {
                    view.setBuildingNumberError(context.getString(R.string.error_field_required));
                    cancel = true;
                }

                if (TextUtils.isEmpty(postCode)) {
                    view.setPostCodeError(context.getString(R.string.error_field_required));
                    cancel = true;
                }

                if (TextUtils.isEmpty(city)) {
                    view.setCityError(context.getString(R.string.error_field_required));
                    cancel = true;
                }

                if (TextUtils.isEmpty(country)) {
                    view.setCountryError(context.getString(R.string.error_field_required));
                    cancel = true;
                }

                if (!cancel) {
                    view.showProgress();
                    profile.setFirstName(firstName);
                    profile.setLastName(lastName);
                    profile.setPhoneNumber(phoneNumber);
                    profile.setStreet(street);
                    profile.setBuildingNumber(buildingNumber);
                    profile.setPostCode(postCode);
                    profile.setCity(city);
                    profile.setCountry(country);
                    createOrUpdateProfile(imageUri);
                }
            });
        }
    }

    private void createOrUpdateProfile(Uri imageUri) {
        profileManager.createOrUpdateProfile(profile, imageUri, success -> {
            ifViewAttached(view -> {
                view.hideProgress();
                if (success) {
                    onProfileUpdatedSuccessfully();
                } else {
                    view.showSnackBar(R.string.error_fail_create_profile);
                }
            });
        });
    }

    protected void onProfileUpdatedSuccessfully() {
        ifViewAttached(BaseView::finish);
    }

}
