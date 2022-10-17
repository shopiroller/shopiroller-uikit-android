package com.shopiroller.views;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.google.android.material.textfield.TextInputLayout;
import com.shopiroller.R;

import java.util.ArrayList;

public class CreditCardNumberTextWatcher implements TextWatcher {

    private EditText mEditText;
    private ArrayList<String> listOfPattern;
    private TextInputLayout textInputLayout;
    private boolean isEmpty = true;

    public CreditCardNumberTextWatcher(EditText editText, TextInputLayout textInputLayout) {
        mEditText = editText;
        this.textInputLayout = textInputLayout;
        listOfPattern = getListOfPattern();
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        String ccNum = s.toString();

        if (ccNum.length() > 3 || ccNum.length() == 0)
            return;
        if (ccNum.length() == 2) {
            for (int i = 0; i < listOfPattern.size(); i++) {
                if (ccNum.substring(0, 2).matches(listOfPattern.get(i))) {
                    textInputLayout.setError(null);
                    mEditText.setError(null);
                    mEditText.setCompoundDrawablesWithIntrinsicBounds(0, 0, patternLogos[i], 0);
                    isEmpty = false;
                    break;
                } else if (!isEmpty) {
                    mEditText.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                }
            }
        } else if (ccNum.length() == 1) {
            mEditText.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        }
    }

    public static Integer[] patternLogos = {R.drawable.ic_e_commerce_visa_icon, R.drawable.ic_e_commerce_master_card_icon};

    public static ArrayList<String> getListOfPattern() {
        ArrayList<String> listOfPattern = new ArrayList<>();

        String ptVisa = "^4[0-9]$";

        listOfPattern.add(ptVisa);

        String ptMasterCard = "^5[1-8]$";

        listOfPattern.add(ptMasterCard);

        return listOfPattern;
    }
}

