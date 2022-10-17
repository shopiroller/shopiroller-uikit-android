package com.shopiroller.viewholders;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.shopiroller.R;
import com.shopiroller.R2;
import com.shopiroller.models.BankAccount;
import com.shopiroller.views.legacy.ShopirollerTextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ealtaca on 8/27/17.
 */

public class BankAccountViewHolder extends RecyclerView.ViewHolder {


    @BindView(R2.id.bank_iban)
    ShopirollerTextView bankIban;
    @BindView(R2.id.bank_branch)
    ShopirollerTextView bankBranch;
    @BindView(R2.id.bank_reference)
    ShopirollerTextView bankReference;
    @BindView(R2.id.bank_account_number)
    ShopirollerTextView bankAccountName;
    @BindView(R2.id.bank_name)
    ShopirollerTextView bankName;
    @BindView(R2.id.main_layout)
    ConstraintLayout mainLayout;
    @BindView(R2.id.copy_button)
    ImageView copyButton;
    @BindView(R2.id.selected_image_view)
    ImageView selectedImageView;

    public View itemView;
    private BankAccount bankAccount;
    public BankAccountViewHolder(View itemView) {
        super(itemView);
        this.itemView = itemView;
        ButterKnife.bind(this, itemView);
    }

    public void bind(BankAccount account) {
        bankAccount = account;

        bankName.setText(account.name);
        if(account.nameSurname != null)
            bankReference.setText(account.nameSurname);
        else
            bankReference.setVisibility(View.GONE);
        bankIban.setText(bankAccountName.getContext().getString(R.string.e_commerce_payment_bank_iban ,account.accountAddress));
        bankBranch.setText(bankAccountName.getContext().getString(R.string.e_commerce_payment_bank_branch ,account.accountName + " / " + account.accountCode));
        bankAccountName.setText(bankAccountName.getContext().getString(R.string.e_commerce_payment_bank_account ,account.accountNumber));
        if (account.isSelected) {
            mainLayout.setBackground(ContextCompat.getDrawable(mainLayout.getContext(), R.drawable.e_commerce_bank_selected));
            copyButton.setVisibility(View.VISIBLE);
            selectedImageView.setVisibility(View.VISIBLE);
        }
        else {
            mainLayout.setBackground(ContextCompat.getDrawable(mainLayout.getContext(), R.drawable.e_commerce_bank_unselected));
            copyButton.setVisibility(View.GONE);
            selectedImageView.setVisibility(View.GONE);
        }
    }

    @OnClick(R2.id.copy_button)
    public void onClickCopyButton(){
        ClipboardManager clipboard = (ClipboardManager)
                itemView.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("iban", bankAccount.accountAddress);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(itemView.getContext(),R.string.e_commerce_payment_bank_copy_toast_message,Toast.LENGTH_SHORT).show();
    }

}
