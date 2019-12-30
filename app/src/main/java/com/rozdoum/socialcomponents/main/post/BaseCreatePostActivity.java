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

package com.rozdoum.socialcomponents.main.post;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.rozdoum.socialcomponents.R;
import com.rozdoum.socialcomponents.main.pickImageBase.PickImageActivity;
import com.rozdoum.socialcomponents.managers.PostManager;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.rozdoum.socialcomponents.utils.StringUtils.countChar;

/**
 * Created by Alexey on 03.05.18.
 */
public abstract class BaseCreatePostActivity<V extends BaseCreatePostView, P extends BaseCreatePostPresenter<V>>
        extends PickImageActivity<V, P> implements BaseCreatePostView {

    protected ImageView imageView;
    protected ProgressBar progressBar;
    protected EditText titleEditText;
    protected EditText descriptionEditText;
    protected EditText priceEditText;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_create_post_activity);
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        titleEditText = findViewById(R.id.titleEditText);
        descriptionEditText = findViewById(R.id.descriptionEditText);
        priceEditText = findViewById(R.id.priceEditText);
        progressBar = findViewById(R.id.progressBar);

        imageView = findViewById(R.id.imageView);

        imageView.setOnClickListener(this::onSelectImageClick);
        
        priceEditText.addTextChangedListener(new TextWatcher()
        {
            NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.GERMANY);
            private String current = "";

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                if(!s.toString().equals(current))
                {
                   priceEditText.removeTextChangedListener(this);

                    int selection = priceEditText.getSelectionStart();

                    String[] parts = s.toString().split(",");
                    if (parts.length == 2) {
                        StringBuilder sb1 = new StringBuilder(parts[1]);

                        if (sb1.length() > 4) {
                            sb1.deleteCharAt(2);
                            if (selection == s.length() - 2) {
                                selection -= 1;
                            }
                        } else if (sb1.length() < 4) {
                            sb1.insert(1, '0');
                        }
                        s = parts[0] + "," + sb1.toString();
                    }
                    else if (parts.length == 1 && countChar(current,',') != 0) {
                        StringBuilder sb = new StringBuilder(s);
                        sb.deleteCharAt(s.length()-5);
                        sb.insert(s.length()-5, ',');
                        s = sb.toString();
                        selection = s.length() - 5;
                    }

                    // We strip off the currency symbol
                    String replaceable = String.format("[%s,.\\s]", currencyFormat.getCurrency().getSymbol());
                    String cleanString = s.toString().replaceAll(replaceable, "");

                    double price = 0;

                    // Parse the string
                    try
                    {
                        price = Double.parseDouble(cleanString);
                    }
                    catch(java.lang.NumberFormatException e)
                    {
                        price = 0;
                    }

                    // If we don't see a decimal, then the user must have deleted it.
                    // In that case, the number must be divided by 100, otherwise 1
                    int shrink = 1;
                    if(s.toString().contains(","))
                    {
                        shrink = 100;
                    }

                    // Reformat the number
                    String formatted = currencyFormat.format((price / shrink));

                    if (countChar(formatted, '.') > countChar(current, '.')) {
                        selection += 1;
                    } else if (countChar(formatted, '.') < countChar(current, '.')) {
                        selection -= 1;
                    }
                    current = formatted;

                    if (countChar(s.toString(), ',') == 2) {
                        selection = formatted.length() - 4;
                    }

                    priceEditText.setText(formatted);
                    priceEditText.setSelection(Math.min(selection, priceEditText.getText().length()));

                    priceEditText.addTextChangedListener(this);
                }
            }


            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }


            @Override
            public void afterTextChanged(Editable s)
            {
            }
        });

        priceEditText.setFilters(new InputFilter[] {
                new InputFilter() {
                    @Override
                    public CharSequence filter(CharSequence charSequence, int i, int i1,
                                               Spanned spanned, int i2, int i3) {
                        if(charSequence.equals("")){ // for backspace
                            return charSequence;
                        }

                        Pattern p = Pattern.compile("(?:[0-9,.]+(?:\\ )(?:\\€)$)|[0-9.,]+");
                        if (p.matcher(charSequence.toString()).find()) {
                            return charSequence;
                        }

                        return "";
                    }
                },
                new InputFilter.LengthFilter(11)
        });

        titleEditText.setOnTouchListener((v, event) -> {
            if (titleEditText.hasFocus() && titleEditText.getError() != null) {
                titleEditText.setError(null);
                return true;
            }

            return false;
        });
    }

    @Override
    protected ProgressBar getProgressView() {
        return progressBar;
    }

    @Override
    protected ImageView getImageView() {
        return imageView;
    }

    @Override
    protected void onImagePikedAction() {
        loadImageToImageView(imageUri);
    }

    @Override
    public void setDescriptionError(String error) {
        descriptionEditText.setError(error);
        descriptionEditText.requestFocus();
    }

    @Override
    public void setTitleError(String error) {
        titleEditText.setError(error);
        titleEditText.requestFocus();
    }

    @Override
    public String getTitleText() {
        return titleEditText.getText().toString();
    }

    @Override
    public String getDescriptionText() {
        return descriptionEditText.getText().toString();
    }

    @Override
    public void requestImageViewFocus() {
        imageView.requestFocus();
    }

    @Override
    public void setPriceError(String error) {
        priceEditText.setError(error);
        priceEditText.requestFocus();
    }

    @Override
    public String getPriceText() {
        return priceEditText.getText().toString();
    }

    @Override
    public void onPostSavedSuccess() {
        setResult(RESULT_OK);
        this.finish();
    }

    @Override
    public Uri getImageUri() {
        return imageUri;
    }
}
