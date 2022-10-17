package com.shopiroller.models;

import java.io.Serializable;

public class BankAccount implements Serializable {

    public String accountAddress;
    public String accountNumber;
    public String accountCode;
    public String accountName;
    public String name;
    public String nameSurname;

    public boolean isSelected;

    @Override
    public String toString() {
        return name + " " + accountName + " " + accountCode + " " + accountNumber;
    }
}
