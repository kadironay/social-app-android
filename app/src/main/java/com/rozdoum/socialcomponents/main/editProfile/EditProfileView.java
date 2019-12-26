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

import com.rozdoum.socialcomponents.main.pickImageBase.PickImageView;

/**
 * Created by Alexey on 03.05.18.
 */

public interface EditProfileView extends PickImageView {
    void setProfilePhoto(String photoUrl);

    void setFirstName(String username);

    String getFirstNameText();

    void setFirstNameError(String string);

    void setLastName(String username);

    String getLastNameText();

    void setLastNameError(String string);

    void setEmail(String email);

    void setPhoneNumber(String phone);

    String getPhoneNumberText();

    void setPhoneNumberError(String string);

    void setStreet(String street);

    String getStreetText();

    void setStreetError(String string);

    void setBuildingNumber(String buildingNumber);

    String getBuildingNumberText();

    void setBuildingNumberError(String string);

    void setPostCode(String postCode);

    String getPostCodeText();

    void setPostCodeError(String string);

    void setCity(String city);

    String getCityText();

    void setCityError(String string);

    void setCountry(String country);

    String getCountryText();

    void setCountryError(String string);
}
