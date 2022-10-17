package com.shopiroller.fragments;

import static com.shopiroller.constants.Constants.ONLINE;
import static com.shopiroller.constants.Constants.ONLINE3DS;
import static com.shopiroller.constants.Constants.PAYPAL;
import static com.shopiroller.constants.Constants.PAY_AT_DOOR;
import static com.shopiroller.constants.Constants.STRIPE;
import static com.shopiroller.constants.Constants.TRANSFER;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.shopiroller.models.events.BankSelectedEvent;
import com.shopiroller.models.events.ValidateStepEvent;
import com.shopiroller.util.ECommerceUtil;
import com.shopiroller.R;
import com.shopiroller.R2;
import com.shopiroller.adapter.BankListAdapter;
import com.shopiroller.adapter.PaymentTypeAdapter;
import com.shopiroller.enums.MobirollerDialogType;
import com.shopiroller.helpers.LegacyProgressViewHelper;
import com.shopiroller.helpers.LocalizationHelper;
import com.shopiroller.helpers.NetworkHelper;
import com.shopiroller.helpers.UtilManager;
import com.shopiroller.models.ECommerceErrorResponse;
import com.shopiroller.models.OrderCard;
import com.shopiroller.models.OrderPaymentEvent;
import com.shopiroller.models.PaymentContinueEvent;
import com.shopiroller.models.PaymentSettings;
import com.shopiroller.models.SupportedPaymentType;
import com.shopiroller.network.ECommerceRequestHelper;
import com.shopiroller.util.DialogUtil;
import com.shopiroller.util.ErrorUtils;
import com.shopiroller.views.CreditCardNumberTextWatcher;
import com.shopiroller.views.legacy.ShopirollerDialog;
import com.shopiroller.views.legacy.ShopirollerEmptyView;
import com.shopiroller.views.legacy.ShopirollerTextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class OrderChoosePaymentFragment extends OrderFlowBaseFragment implements View.OnFocusChangeListener, ShopirollerDialog.DialogListCallback {

    Unbinder unbinder;

    @BindView(R2.id.e_commerce_payment_credit_card_layout_group)
    ViewStub creditCardViewStub;
    @BindView(R2.id.credit_card_layout)
    ConstraintLayout creditCardLayout;
    @BindView(R2.id.pay_at_door_layout)
    ConstraintLayout payAtDoorLayout;
    @BindView(R2.id.pay_at_door_desc)
    TextView payAtDoorDesc;
    @BindView(R2.id.bank_account_list)
    RecyclerView bankAccountList;
    @BindView(R2.id.transfer_layout)
    ConstraintLayout transferLayout;
    @BindView(R2.id.bottom_layout)
    ConstraintLayout bottomLayout;
    @BindView(R2.id.continue_button)
    FloatingActionButton continueButton;
    @BindView(R2.id.payment_text_view)
    ShopirollerTextView paymentTextView;
    @BindView(R2.id.payment_method_text_view)
    ShopirollerTextView paymentMethodTextView;
    @BindView(R2.id.empty_view)
    ShopirollerEmptyView emptyView;
    @BindView(R2.id.content_layout)
    ConstraintLayout contentLayout;

    @BindView(R2.id.paypal_layout)
    ConstraintLayout payPalLayout;
    @BindView(R2.id.paypal_desc)
    TextView payPalDescriptionTextView;
    @BindView(R2.id.stripe_layout)
    ConstraintLayout stripeLayout;
    @BindView(R2.id.stripe_desc)
    TextView stripeDescriptionTextView;

    private CreditCardLayout creditCardLayoutView;

    private LegacyProgressViewHelper legacyProgressViewHelper;
    private ECommerceRequestHelper eCommerceRequestHelper;
    private BankListAdapter adapter;
    private LocalizationHelper localizationHelper;

    private boolean isInit = false;
    private boolean isValid = false;
    private boolean isOnline = false;
    private boolean isOnline3D = false;
    private int currentStep = 2;

    private String mCardNumber = "";
    private String mCardExpireString = "";

    private SupportedPaymentType selectedPaymentType;
    private int selection = 0;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
        legacyProgressViewHelper = new LegacyProgressViewHelper(getActivity());
        eCommerceRequestHelper = new ECommerceRequestHelper();
        localizationHelper = UtilManager.localizationHelper();

        if (ECommerceUtil.getPaymentSettings() == null) {
            getPaymentOptions();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_choose_payment, container, false);
        unbinder = ButterKnife.bind(this, view);
        setContinueButton(false);
        if (ECommerceUtil.getPaymentSettings() != null) {
            bottomLayout.setVisibility(View.VISIBLE);
            setupView();
        }
        return view;
    }

    @Subscribe
    public void onPostBankSelectedEvent(BankSelectedEvent e) {
        isValid();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public boolean isValid() {
        return validate();
    }

    private void getPaymentOptions() {

        if (NetworkHelper.isConnected(getContext())) {
            if (getActivity() != null && !getActivity().isFinishing() && !legacyProgressViewHelper.isShowing())
                legacyProgressViewHelper.show();

            new ECommerceUtil().getPaymentSettings(new ECommerceRequestHelper.ECommerceCallBack<PaymentSettings>() {
                @Override
                public void done() {
                    if (!getActivity().isFinishing() && legacyProgressViewHelper.isShowing())
                        legacyProgressViewHelper.dismiss();
                }

                @Override
                public void onSuccess(PaymentSettings result) {
                    bottomLayout.setVisibility(View.VISIBLE);
                    setupView();
                }

                @Override
                public void onFailure(ECommerceErrorResponse result) {
                    ErrorUtils.showErrorToast(getContext());
                }

                @Override
                public void onNetworkError(String result) {
                    ErrorUtils.showErrorToast(getContext());
                }
            });
        } else
            DialogUtil.showNoConnectionError(getActivity());
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setupView() {
        if (isInit)
            return;
        if (ECommerceUtil.getPaymentSettings().getSupportedPaymentTypes() == null || ECommerceUtil.getPaymentSettings().getSupportedPaymentTypes().size() == 0) {
            contentLayout.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
            bottomLayout.setVisibility(View.GONE);
            return;
        } else {
            contentLayout.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
            bottomLayout.setVisibility(View.VISIBLE);
        }
        for (int i = 0; i < ECommerceUtil.getPaymentSettings().getSupportedPaymentTypes().size(); i++) {
            if (ECommerceUtil.getPaymentSettings().getSupportedPaymentTypes().get(i).paymentType.equalsIgnoreCase(ONLINE)) {
                isOnline = true;
            }
            if (ECommerceUtil.getPaymentSettings().getSupportedPaymentTypes().get(i).paymentType.equalsIgnoreCase(ONLINE3DS)) {
                isOnline3D = true;
            }
            if ((ECommerceUtil.getPaymentSettings().getSupportedPaymentTypes().get(i).paymentType.equalsIgnoreCase(ONLINE)
                    || ECommerceUtil.getPaymentSettings().getSupportedPaymentTypes().get(i).paymentType.equalsIgnoreCase(ONLINE3DS))
                    && creditCardLayout.getVisibility() == View.GONE) {    //Card layout visible
                if (creditCardLayoutView != null)
                    creditCardLayoutView = new CreditCardLayout(creditCardViewStub.inflate());
            } else if (ECommerceUtil.getPaymentSettings().getSupportedPaymentTypes().get(i).paymentType.equalsIgnoreCase(TRANSFER)) {
                transferLayout.setVisibility(View.VISIBLE);
                if (adapter == null && ECommerceUtil.getPaymentSettings().getPaymentAccounts() != null) {
                    adapter = new BankListAdapter(getActivity(), ECommerceUtil.getPaymentSettings().getPaymentAccounts());
                    bankAccountList.setAdapter(adapter);
                    bankAccountList.setLayoutManager(new LinearLayoutManager(getActivity()));
                } else if (adapter != null) {
                    bankAccountList.setAdapter(adapter);
                    bankAccountList.setLayoutManager(new LinearLayoutManager(getActivity()));
                }
            } else if (ECommerceUtil.getPaymentSettings().getSupportedPaymentTypes().get(i).paymentType.equalsIgnoreCase(PAY_AT_DOOR)) {
                payAtDoorLayout.setVisibility(View.VISIBLE);
                payAtDoorDesc.setText(localizationHelper.getLocalizedTitle(ECommerceUtil.getPaymentSettings().getSupportedPaymentTypes().get(i).description));
            } else if (ECommerceUtil.getPaymentSettings().getSupportedPaymentTypes().get(i).paymentType.equalsIgnoreCase(PAYPAL)) {
                payPalLayout.setVisibility(View.VISIBLE);
                payPalDescriptionTextView.setText(getString(R.string.e_commerce_payment_method_selection_description_text, PAYPAL));
            } else if (ECommerceUtil.getPaymentSettings().getSupportedPaymentTypes().get(i).paymentType.equalsIgnoreCase(STRIPE)) {
                stripeLayout.setVisibility(View.VISIBLE);
                stripeDescriptionTextView.setText(getString(R.string.e_commerce_payment_method_selection_description_text, STRIPE));
            }

            transferLayout.setVisibility(View.GONE);
            creditCardLayout.setVisibility(View.GONE);
            payAtDoorLayout.setVisibility(View.GONE);
            payPalLayout.setVisibility(View.GONE);
            stripeLayout.setVisibility(View.GONE);

            onSelect(selection);

        }

        isInit = true;
    }

    @OnClick(R2.id.payment_method_selection_layout)
    public void onClickPaymentTypeSelection() {
        List<SupportedPaymentType> data = new ArrayList<>();
        if (ECommerceUtil.getPaymentSettings().getSupportedPaymentTypes() != null)
            data = ECommerceUtil.getPaymentSettings().getSupportedPaymentTypes();
        PaymentTypeAdapter paymentTypeAdapter = new PaymentTypeAdapter(data);
        new ShopirollerDialog.Builder()
                .setContext(getActivity())
                .setType(MobirollerDialogType.LIST)
                .setTitle(getString(R.string.e_commerce_payment_method_selection_payment_type_popup_title))
                .setHasDivider(true)
                .setListSelectionListener(this)
                .setDescription(getString(R.string.e_commerce_payment_method_selection_payment_type_popup_description))
                .setAdapter(paymentTypeAdapter)
                .show();

    }

    private void checkIsValid() {
        Log.e("CreditCard", "checkIsValid");
//        if (DynamicConstants.MobiRoller_Stage) {
//            setContinueButton(true);
//            EventBus.getDefault().post(new ValidateStepEvent(currentStep, true));
//            return;
//        }

        isValid = selectedPaymentType.paymentType.equalsIgnoreCase(PAY_AT_DOOR) ||
                selectedPaymentType.paymentType.equalsIgnoreCase(PAYPAL) || selectedPaymentType.paymentType.equalsIgnoreCase(STRIPE) ||
                ((selectedPaymentType.paymentType.equalsIgnoreCase(ONLINE) || selectedPaymentType.paymentType.equalsIgnoreCase(ONLINE3DS)) && validateCreditCardFields()) ||
                (selectedPaymentType.paymentType.equalsIgnoreCase(TRANSFER) && adapter != null && adapter.getSelectedBank() != null);

        setContinueButton(isValid);
        if (isValid) {
            OrderPaymentEvent event = new OrderPaymentEvent();
            if (selectedPaymentType.paymentType.equalsIgnoreCase(PAY_AT_DOOR))
                event.paymentType = PAY_AT_DOOR;
            else if (((selectedPaymentType.paymentType.equalsIgnoreCase(ONLINE) || selectedPaymentType.paymentType.equalsIgnoreCase(ONLINE3DS)) && validateCreditCardFields())) {
                if (isOnline3D)
                    event.paymentType = ONLINE3DS;
                else if (isOnline)
                    event.paymentType = ONLINE;
                event.orderCard = new OrderCard();
                event.orderCard.cardHolderName = creditCardLayoutView.cardNameTextInputEditText.getText().toString();
                event.orderCard.cardNumber = mCardNumber;
                event.orderCard.expireMonth = mCardExpireString.substring(0, 2);
                event.orderCard.expireYear = "20" + mCardExpireString.substring(2, 4);
                event.orderCard.cvc = creditCardLayoutView.cvvTextInputEditText.getText().toString();
            } else if (selectedPaymentType.paymentType.equalsIgnoreCase(TRANSFER) && adapter != null && adapter.getSelectedBank() != null) {
                event.paymentType = TRANSFER;
                event.bankAccount = adapter.getSelectedBank();
            } else if (selectedPaymentType.paymentType.equalsIgnoreCase(PAYPAL)) {
                event.paymentType = PAYPAL;
            } else if (selectedPaymentType.paymentType.equalsIgnoreCase(STRIPE)) {
                event.paymentType = STRIPE;
            }
            EventBus.getDefault().post(event);
        }
        EventBus.getDefault().post(new ValidateStepEvent(currentStep, isValid));
    }

    private boolean validate() {

//        if (DynamicConstants.MobiRoller_Stage) {
//            setContinueButton(true);
//            return true;
//        }

        isValid = selectedPaymentType.paymentType.equalsIgnoreCase(PAY_AT_DOOR) ||
                selectedPaymentType.paymentType.equalsIgnoreCase(PAYPAL) || selectedPaymentType.paymentType.equalsIgnoreCase(STRIPE) ||
                ((selectedPaymentType.paymentType.equalsIgnoreCase(ONLINE) || selectedPaymentType.paymentType.equalsIgnoreCase(ONLINE3DS)) && validateCreditCardFields()) ||
                (selectedPaymentType.paymentType.equalsIgnoreCase(TRANSFER) && adapter != null && adapter.getSelectedBank() != null);

        setContinueButton(isValid);
        if (isValid) {
            OrderPaymentEvent event = new OrderPaymentEvent();
            if (selectedPaymentType.paymentType.equalsIgnoreCase(PAY_AT_DOOR))
                event.paymentType = PAY_AT_DOOR;
            else if (((selectedPaymentType.paymentType.equalsIgnoreCase(ONLINE) || selectedPaymentType.paymentType.equalsIgnoreCase(ONLINE3DS)) && validateCreditCardFields())) {
                if (isOnline3D)
                    event.paymentType = ONLINE3DS;
                else if (isOnline)
                    event.paymentType = ONLINE;
                event.orderCard = new OrderCard();
                event.orderCard.cardHolderName = creditCardLayoutView.cardNameTextInputEditText.getText().toString();
                event.orderCard.cardNumber = mCardNumber;
                event.orderCard.expireMonth = mCardExpireString.substring(0, 2);
                event.orderCard.expireYear = "20" + mCardExpireString.substring(2, 4);
                event.orderCard.cvc = creditCardLayoutView.cvvTextInputEditText.getText().toString();
            } else if (selectedPaymentType.paymentType.equalsIgnoreCase(TRANSFER) && adapter != null && adapter.getSelectedBank() != null) {
                event.paymentType = TRANSFER;
                event.bankAccount = adapter.getSelectedBank();
            } else if (selectedPaymentType.paymentType.equalsIgnoreCase(PAYPAL)) {
                event.paymentType = PAYPAL;
            } else if (selectedPaymentType.paymentType.equalsIgnoreCase(STRIPE)) {
                event.paymentType = STRIPE;
            }
            EventBus.getDefault().post(event);
        }
        return isValid;
    }

    private boolean validateCreditCardFields() {
        if (creditCardLayoutView.expirationDateTextInputEditText.getText().toString().equalsIgnoreCase("") || creditCardLayoutView.cvvTextInputEditText.getText().toString().equalsIgnoreCase("")
                || creditCardLayoutView.cardNumberTextInputEditText.getText().toString().equalsIgnoreCase("") || creditCardLayoutView.cardNameTextInputEditText.getText().toString().equalsIgnoreCase(""))
            return false;
        mCardNumber = creditCardLayoutView.cardNumberTextInputEditText.getText().toString().replace(" ", "");
        mCardExpireString = creditCardLayoutView.expirationDateTextInputEditText.getText().toString().replace("/", "");
        boolean isCardValid = validateCreditCardNumber();
        isCardValid = validateCreditCardName() && isCardValid;
        isCardValid = validateCreditCardExpireDate() && isCardValid;
        isCardValid = validateCreditCardCVV() && isCardValid;

        return isCardValid;
    }

    private boolean validateCreditCardCVV() {
        validateAllCreditCardFields();
        if (creditCardLayoutView.cvvTextInputEditText.getText().toString().trim().equals("")) {
            creditCardLayoutView.cvvTextInputLayout.setError(getString(R.string.e_commerce_payment_credit_card_validation_empty, getString(R.string.e_commerce_payment_credit_card_security_code)));
            return false;
        } else if (!(creditCardLayoutView.cvvTextInputEditText.getText().toString().trim().length() == 3 || creditCardLayoutView.cvvTextInputEditText.getText().toString().trim().length() == 4)) {
            creditCardLayoutView.cvvTextInputLayout.setError(getString(R.string.e_commerce_payment_credit_card_validation_invalid, getString(R.string.e_commerce_payment_credit_card_security_code)));
            return false;
        }
        if (creditCardLayoutView.cvvTextInputLayout.getError() != null)
            creditCardLayoutView.cvvTextInputLayout.setError(null);
        return true;
    }

    private boolean validateCreditCardExpireDate() {
        validateAllCreditCardFields();
        if (creditCardLayoutView.expirationDateTextInputEditText.getText().toString().length() == 0) {
            creditCardLayoutView.expirationDateTextInputLayout.setError(getString(R.string.e_commerce_payment_credit_card_validation_empty, getString(R.string.e_commerce_payment_credit_card_expire_date)));
            return false;
        } else if (creditCardLayoutView.expirationDateTextInputEditText.getText().toString().length() != 5) {
            creditCardLayoutView.expirationDateTextInputLayout.setError(getString(R.string.e_commerce_payment_credit_card_validation_invalid, getString(R.string.e_commerce_payment_credit_card_expire_date)));
            return false;
        }

        if (creditCardLayoutView.expirationDateTextInputLayout.getError() != null) {
            creditCardLayoutView.expirationDateTextInputLayout.setError(null);
        }
        return true;
    }

    private boolean validateCreditCardNumber() {
        validateAllCreditCardFields();
        mCardNumber = creditCardLayoutView.cardNumberTextInputEditText.getText().toString().replace(" ", "");
        if (mCardNumber.equals("")) {
            creditCardLayoutView.cardNumberTextInputLayout.setError(getString(R.string.e_commerce_payment_credit_card_validation_empty, getString(R.string.e_commerce_payment_credit_card_number)));
            return false;
        } else if (mCardNumber.length() != 16) {
            creditCardLayoutView.cardNumberTextInputLayout.setError(getString(R.string.e_commerce_payment_credit_card_validation_invalid, getString(R.string.e_commerce_payment_credit_card_number)));
            return false;
        } else if (!ECommerceUtil.validateCreditCardNumber(mCardNumber)) {
            creditCardLayoutView.cardNumberTextInputLayout.setError(getString(R.string.e_commerce_payment_credit_card_validation_invalid, getString(R.string.e_commerce_payment_credit_card_number)));
            return false;
        }
        if (creditCardLayoutView.cardNumberTextInputLayout.getError() != null) {
            creditCardLayoutView.cardNumberTextInputLayout.setError(null);
        }
        return true;
    }

    private void clearCreditCardErrors() {
        if (creditCardLayoutView != null && creditCardLayoutView.cardNameTextInputEditText != null && creditCardLayoutView.cardNameTextInputEditText.getVisibility() == View.VISIBLE) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (creditCardLayoutView.cardNumberTextInputLayout.getError() != null) {
                        creditCardLayoutView.cardNumberTextInputLayout.setError(null);
                    }
                    if (creditCardLayoutView.expirationDateTextInputLayout.getError() != null) {
                        creditCardLayoutView.expirationDateTextInputLayout.setError(null);
                    }
                    if (creditCardLayoutView.cvvTextInputLayout.getError() != null) {
                        creditCardLayoutView.cvvTextInputLayout.setError(null);
                    }
                    if (creditCardLayoutView.cardNameTextInputLayout.getError() != null) {
                        creditCardLayoutView.cardNameTextInputLayout.setError(null);
                    }
                    creditCardLayoutView.cardNameTextInputEditText.setText("");
                    creditCardLayoutView.expirationDateTextInputEditText.setText("");
                    creditCardLayoutView.cardNumberTextInputEditText.setText("");
                    creditCardLayoutView.cvvTextInputEditText.setText("");
                }
            });
        }
    }

    private boolean validateCreditCardName() {
        validateAllCreditCardFields();
        if (creditCardLayoutView.cardNameTextInputEditText.getText().toString().equals("")) {
            creditCardLayoutView.cardNameTextInputLayout.setError(getString(R.string.e_commerce_payment_credit_card_validation_empty, getString(R.string.e_commerce_payment_credit_card_name)));
            return false;
        }
        if (creditCardLayoutView.cardNameTextInputLayout.getError() != null) {
            creditCardLayoutView.cardNameTextInputLayout.setError(null);
        }
        return true;
    }

    private void validateAllCreditCardFields() {
        boolean validate = true;
        if (creditCardLayoutView.cardNameTextInputEditText == null || creditCardLayout.getVisibility() == View.GONE)
            return;
        if (creditCardLayoutView.cardNameTextInputEditText.getText().toString().equals("")) {
            validate = false;
        }

        if (validate) {
            mCardNumber = creditCardLayoutView.cardNumberTextInputEditText.getText().toString().replace(" ", "");
            if (mCardNumber.equals("")) {
                validate = false;
            } else if (mCardNumber.length() != 16) {
                validate = false;
            } else if (!ECommerceUtil.validateCreditCardNumber(mCardNumber)) {
                validate = false;
            }
        }

        if (validate) {
            if (creditCardLayoutView.expirationDateTextInputEditText.getText().toString().length() == 0) {
                validate = false;
            } else if (creditCardLayoutView.expirationDateTextInputEditText.getText().toString().length() != 5) {
                validate = false;
            }
        }

        if (validate) {
            if (creditCardLayoutView.cvvTextInputEditText.getText().toString().trim().equals("")) {
                validate = false;
            } else if (!(creditCardLayoutView.cvvTextInputEditText.getText().toString().trim().length() == 3 ||
                    creditCardLayoutView.cvvTextInputEditText.getText().toString().trim().length() == 4)) {
                validate = false;
            }
        }

        if (isValid != validate) {
            isValid = validate;
            setContinueButton(isValid);
            EventBus.getDefault().post(new ValidateStepEvent(currentStep, validate));
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (isRemoving() || isHidden() || isDetached())
            return;
        if (!hasFocus) {
            if (v.getId() == R.id.cvvTextInputEditText)
                validateCreditCardCVV();
            else if (v.getId() == R.id.cardNumberTextInputEditText)
                validateCreditCardNumber();
            else if (v.getId() == R.id.cardNameTextInputEditText)
                validateCreditCardName();
            else if (v.getId() == R.id.expirationDateTextInputEditText)
                validateCreditCardExpireDate();
        }
    }

    @Override
    public void onSelect(int position) {

        if (ECommerceUtil.getPaymentSettings().getSupportedPaymentTypes() != null) {
            selection = position;
            selectedPaymentType = ECommerceUtil.getPaymentSettings().getSupportedPaymentTypes().get(position);
            String paymentType = "";
            if (ECommerceUtil.getPaymentSettings().getSupportedPaymentTypes().get(position).paymentType.equalsIgnoreCase(TRANSFER)) {
                transferLayout.setVisibility(View.VISIBLE);
                creditCardLayout.setVisibility(View.GONE);
                payAtDoorLayout.setVisibility(View.GONE);
                payPalLayout.setVisibility(View.GONE);
                stripeLayout.setVisibility(View.GONE);
                paymentType = getString(R.string.e_commerce_payment_method_selection_transfer);
            } else if (ECommerceUtil.getPaymentSettings().getSupportedPaymentTypes().get(position).paymentType.equalsIgnoreCase(ONLINE) ||
                    ECommerceUtil.getPaymentSettings().getSupportedPaymentTypes().get(position).paymentType.equalsIgnoreCase(ONLINE3DS)) {
                transferLayout.setVisibility(View.GONE);
                creditCardLayout.setVisibility(View.VISIBLE);
                if (creditCardLayoutView == null)
                    creditCardLayoutView = new CreditCardLayout(creditCardViewStub.inflate());
                else
                    clearCreditCardErrors();
                payAtDoorLayout.setVisibility(View.GONE);
                payPalLayout.setVisibility(View.GONE);
                stripeLayout.setVisibility(View.GONE);
                paymentType = getString(R.string.e_commerce_payment_method_selection_credit_card);
                //Card layout visible
            } else if (ECommerceUtil.getPaymentSettings().getSupportedPaymentTypes().get(position).paymentType.equalsIgnoreCase(PAY_AT_DOOR)) {
                transferLayout.setVisibility(View.GONE);
                creditCardLayout.setVisibility(View.GONE);
                payPalLayout.setVisibility(View.GONE);
                payAtDoorLayout.setVisibility(View.VISIBLE);
                stripeLayout.setVisibility(View.GONE);
                paymentType = getString(R.string.e_commerce_payment_method_selection_pay_at_door);
            } else if (ECommerceUtil.getPaymentSettings().getSupportedPaymentTypes().get(position).paymentType.equalsIgnoreCase(PAYPAL)) {
                transferLayout.setVisibility(View.GONE);
                creditCardLayout.setVisibility(View.GONE);
                payAtDoorLayout.setVisibility(View.GONE);
                payPalLayout.setVisibility(View.VISIBLE);
                stripeLayout.setVisibility(View.GONE);
                paymentType = getString(R.string.e_commerce_payment_method_selection_paypal);
            } else if (ECommerceUtil.getPaymentSettings().getSupportedPaymentTypes().get(position).paymentType.equalsIgnoreCase(STRIPE)) {
                transferLayout.setVisibility(View.GONE);
                creditCardLayout.setVisibility(View.GONE);
                payAtDoorLayout.setVisibility(View.GONE);
                payPalLayout.setVisibility(View.GONE);
                stripeLayout.setVisibility(View.VISIBLE);
                paymentType = getString(R.string.e_commerce_payment_method_selection_stripe);
            }
            paymentTextView.setText(paymentType);
            paymentMethodTextView.setText(paymentType);
        }

        checkIsValid();
    }

    private class ValidateTextWatcher implements TextWatcher {

        TextInputEditText editText;

        public ValidateTextWatcher(TextInputEditText editText) {
            this.editText = editText;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s.length() < 2)
                return;
            if (editText.getId() == R.id.cardNameTextInputEditText && editText.toString().length() >= 1)
                return;
            if (editText.getId() == R.id.cvvTextInputEditText && editText.toString().length() < 3)
                return;
            validateAllCreditCardFields();
        }
    }

    private class CardNumberTextWatcher implements TextWatcher {
        private static final char space = ' ';

        private EditText mEditText;

        CardNumberTextWatcher(EditText editText) {
            mEditText = editText;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s.length() < 1)
                return;
            if (s.length() > 0 && (s.length() % 5) == 0) {
                final char c = s.charAt(s.length() - 1);
                if (space == c) {
                    s.delete(s.length() - 1, s.length());
                }
            }
            // Insert char where needed.
            if (s.length() > 0 && (s.length() % 5) == 0) {
                char c = s.charAt(s.length() - 1);
                // Only if its a digit where there should be a space we insert a space
                if (Character.isDigit(c) && TextUtils.split(s.toString(), String.valueOf(space)).length <= 3) {
                    s.insert(s.length() - 1, String.valueOf(space));
                }
            }
            validateAllCreditCardFields();
        }
    }

    private class TwoDigitsCardTextWatcher implements TextWatcher {

        private static final String INITIAL_MONTH_ADD_ON = "0";
        private static final String DEFAULT_MONTH = "01";
        private static final String SPACE = "/";
        private EditText mEditText;
        private int mLength;

        public TwoDigitsCardTextWatcher(EditText editText) {
            mEditText = editText;
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
//            mEditText.setError(null);
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            mLength = mEditText.getText().length();
        }

        @Override
        public void afterTextChanged(Editable s) {
            int currentLength = mEditText.getText().length();
            boolean ignoreBecauseIsDeleting = false;
            if (mLength > currentLength) {
                ignoreBecauseIsDeleting = true;
            }

            if (ignoreBecauseIsDeleting && s.toString().startsWith(SPACE)) {
                return;
            }

            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR) % 100;
            int month = calendar.get(Calendar.MONTH) + 1;
            if (s.length() == 1 && !isNumberChar(String.valueOf(s.charAt(0)))) {
                mEditText.setText(DEFAULT_MONTH + SPACE);
            } else if (s.length() == 1 && !isCharValidMonth(s.charAt(0))) {
                mEditText.setText(INITIAL_MONTH_ADD_ON + String.valueOf(s.charAt(0)) + SPACE);
            } else if (s.length() == 2 && String.valueOf(s.charAt(s.length() - 1)).equals(SPACE)) {
                mEditText.setText(INITIAL_MONTH_ADD_ON + String.valueOf(s));
            } else if (!ignoreBecauseIsDeleting &&
                    (s.length() == 2 && !String.valueOf(s.charAt(s.length() - 1)).equals(SPACE))) {
                mEditText.setText(mEditText.getText().toString() + SPACE);
            } else if (s.length() == 3 && !String.valueOf(s.charAt(s.length() - 1)).equals(SPACE) && !ignoreBecauseIsDeleting) {
                s.insert(2, SPACE);
                mEditText.setText(String.valueOf(s));
            } else if (s.length() > 3 && !isCharValidMonth(s.charAt(0))) {
                mEditText.setText(INITIAL_MONTH_ADD_ON + s);
            }

            if (s.length() == 3 && Integer.parseInt(mEditText.getText().toString().substring(0, 2)) > 12)
                mEditText.setText("12/");

            if (s.length() == 4 && !isNumberChar(String.valueOf(s.charAt(3))))
                mEditText.setText(mEditText.getText().toString().substring(0, 2));
            else if (s.length() == 5 && !isNumberChar(String.valueOf(s.charAt(4))))
                mEditText.setText(mEditText.getText().toString().substring(0, 3));
            if (s.length() > 3 && s.charAt(3) == '0') {
                mEditText.setText(mEditText.getText().toString().substring(0, 3) + "1");
            }
            if (s.length() == 5 && Integer.valueOf(String.valueOf(s.subSequence(3, 5))) < year) {
                if (Integer.valueOf(String.valueOf(s.subSequence(0, 2))) < month)
                    mEditText.setText(mEditText.getText().toString().substring(0, 3) + (year + 1));
                else
                    mEditText.setText(mEditText.getText().toString().substring(0, 3) + year);
            }

            if (!ignoreBecauseIsDeleting) {
                mEditText.setSelection(mEditText.getText().toString().length());
            }
            validateAllCreditCardFields();
        }

        private boolean isCharValidMonth(char charFromString) {
            int month = 0;
            if (Character.isDigit(charFromString)) {
                month = Integer.parseInt(String.valueOf(charFromString));
            }
            return month <= 1;
        }

        private boolean isNumberChar(String string) {
            return string.matches(".*\\d.*");
        }
    }

    @OnClick(R2.id.continue_button)
    public void onViewClicked() {
        EventBus.getDefault().post(new PaymentContinueEvent());
    }

    private void setContinueButton(boolean isEnabled) {
        if (continueButton == null)
            return;
        continueButton.setEnabled(isEnabled);
    }

    public class CreditCardLayout {

        @BindView(R2.id.cardNumberTextInputEditText)
        TextInputEditText cardNumberTextInputEditText;
        @BindView(R2.id.cardNumberTextInputLayout)
        TextInputLayout cardNumberTextInputLayout;
        @BindView(R2.id.cardNameTextInputEditText)
        TextInputEditText cardNameTextInputEditText;
        @BindView(R2.id.cardNameTextInputLayout)
        TextInputLayout cardNameTextInputLayout;
        @BindView(R2.id.expirationDateTextInputEditText)
        TextInputEditText expirationDateTextInputEditText;
        @BindView(R2.id.expirationDateTextInputLayout)
        TextInputLayout expirationDateTextInputLayout;
        @BindView(R2.id.cvvTextInputEditText)
        TextInputEditText cvvTextInputEditText;
        @BindView(R2.id.cvvTextInputLayout)
        TextInputLayout cvvTextInputLayout;

        public CreditCardLayout(View view) {
            ButterKnife.bind(this, view);

            creditCardLayout.setVisibility(View.VISIBLE);
            cardNumberTextInputEditText.addTextChangedListener(new CardNumberTextWatcher(cardNumberTextInputEditText));
            cardNumberTextInputEditText.addTextChangedListener(new CreditCardNumberTextWatcher(cardNumberTextInputEditText, cardNumberTextInputLayout));
            expirationDateTextInputEditText.addTextChangedListener(new TwoDigitsCardTextWatcher(expirationDateTextInputEditText));
            cvvTextInputEditText.addTextChangedListener(new ValidateTextWatcher(cvvTextInputEditText));
            cardNameTextInputEditText.addTextChangedListener(new ValidateTextWatcher(cardNameTextInputEditText));

            cardNumberTextInputEditText.setOnFocusChangeListener(OrderChoosePaymentFragment.this);
            cardNameTextInputEditText.setOnFocusChangeListener(OrderChoosePaymentFragment.this);
            expirationDateTextInputEditText.setOnFocusChangeListener(OrderChoosePaymentFragment.this);
            cvvTextInputEditText.setOnFocusChangeListener(OrderChoosePaymentFragment.this);
        }
    }

    @Override
    public void onDestroy() {
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        super.onDestroy();
    }
}
