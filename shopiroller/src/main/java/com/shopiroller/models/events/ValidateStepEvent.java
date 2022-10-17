package com.shopiroller.models.events;

import java.io.Serializable;

public class ValidateStepEvent implements Serializable {

    public int currentStep;
    public boolean isValid;

    public ValidateStepEvent(int currentStep, boolean isValid) {
        this.currentStep = currentStep;
        this.isValid = isValid;
    }
}
