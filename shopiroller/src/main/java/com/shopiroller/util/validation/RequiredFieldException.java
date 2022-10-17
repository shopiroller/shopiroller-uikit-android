package com.shopiroller.util.validation;

import android.content.Context;

import com.shopiroller.util.validation.exceptions.ShowableException;

public class RequiredFieldException extends ShowableException {
    private String fieldName;
    public int localisedErrorMessage;
    private Context context;

    public RequiredFieldException(int localisedErrorMessage,Context context) {
        this.context = context;
        this.localisedErrorMessage = localisedErrorMessage;
    }

    public RequiredFieldException(String fieldName) {
        this.fieldName = fieldName;
    }

    @Override
    public String toString() {
        if (localisedErrorMessage == 0)
            return fieldName + " cannot be null";
        else
            return context.getString(localisedErrorMessage);
    }

}