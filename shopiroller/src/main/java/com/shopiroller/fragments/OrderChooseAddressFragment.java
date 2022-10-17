package com.shopiroller.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.shopiroller.adapter.UserPopupAddressAdapter;
import com.shopiroller.interfaces.ShopirollerAddressListener;
import com.shopiroller.interfaces.ShopirollerCallBackListener;
import com.shopiroller.models.events.ValidateStepEvent;
import com.shopiroller.R;
import com.shopiroller.R2;
import com.shopiroller.Shopiroller;
import com.shopiroller.enums.MobirollerDialogType;
import com.shopiroller.helpers.LegacyProgressViewHelper;
import com.shopiroller.helpers.NetworkHelper;
import com.shopiroller.models.OrderAddressesEvent;
import com.shopiroller.models.events.AddressContinueEvent;
import com.shopiroller.models.user.BaseAddressModel;
import com.shopiroller.models.user.DefaultAddressList;
import com.shopiroller.models.user.UserBillingAddressModel;
import com.shopiroller.models.user.UserShippingAddressModel;
import com.shopiroller.util.DialogUtil;
import com.shopiroller.util.ErrorUtils;
import com.shopiroller.util.validation.RequiredFieldException;
import com.shopiroller.util.validation.Validator;
import com.shopiroller.views.legacy.ShopirollerDialog;
import com.shopiroller.views.legacy.ShopirollerTextView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTouch;
import butterknife.Unbinder;

public class OrderChooseAddressFragment extends OrderFlowBaseFragment implements ShopirollerAddressListener {

    @BindView(R2.id.delivery_title_layout)
    ConstraintLayout deliveryTitleLayout;
    @BindView(R2.id.delivery_empty_layout)
    ConstraintLayout deliveryEmptyLayout;
    @BindView(R2.id.delivery_content_layout)
    ConstraintLayout deliveryContentLayout;
    @BindView(R2.id.other_shipping_addresses)
    ShopirollerTextView otherShippingAddresses;
    @BindView(R2.id.delivery_layout)
    ConstraintLayout deliveryLayout;
    @BindView(R2.id.billing_title_layout)
    ConstraintLayout billingTitleLayout;
    @BindView(R2.id.billing_empty_layout)
    ConstraintLayout billingEmptyLayout;
    @BindView(R2.id.billing_content_layout)
    ConstraintLayout billingContentLayout;
    @BindView(R2.id.billing_layout)
    ConstraintLayout billingLayout;
    @BindView(R2.id.shipping_address_title)
    ShopirollerTextView shippingAddressTitle;
    @BindView(R2.id.shipping_address_description)
    ShopirollerTextView shippingAddressDescription;
    @BindView(R2.id.shipping_address_description_one)
    ShopirollerTextView shippingAddressDescriptionFirst;
    @BindView(R2.id.other_billing_addresses)
    ShopirollerTextView otherBillingAddresses;
    @BindView(R2.id.billing_address_title)
    TextView billingAddressTitle;
    @BindView(R2.id.billing_address_description)
    TextView billingAddressDescription;
    @BindView(R2.id.billing_address_description_one)
    ShopirollerTextView billingAddressDescriptionFirst;
    @BindView(R2.id.action_add_new_shipping_address)
    ShopirollerTextView actionAddNewShippingAddress;
    @BindView(R2.id.action_add_new_billing_address)
    ShopirollerTextView actionAddNewBillingAddress;
    @BindView(R2.id.continue_button)
    FloatingActionButton continueButton;
    @BindView(R2.id.bottom_layout)
    ConstraintLayout bottomLayout;

    Unbinder unbinder;

    private LegacyProgressViewHelper legacyProgressViewHelper;
    private DefaultAddressList defaultAddressList = null;

    private boolean isValid = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        legacyProgressViewHelper = new LegacyProgressViewHelper(getActivity());
        getDefaultAddresses();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = getView() != null ? getView() :
                inflater.inflate(R.layout.layout_choose_address, container, false);
        unbinder = ButterKnife.bind(this, view);

        setContinueButton(false);

        Shopiroller.setAddressListener(this);

        if (defaultAddressList != null) {
            setupView();
        }
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public boolean isValid() {
        try {
            if (Validator.validateForNulls(defaultAddressList, getActivity())) {
                return true;
            }
        } catch (RequiredFieldException | ClassNotFoundException | NoSuchFieldException | IllegalAccessException e) {
            if (e instanceof RequiredFieldException)
                ((RequiredFieldException) e).notifyUserWithToast(getContext());
            else
                e.printStackTrace();
            return false;
        }

        return isValid;
    }

    @OnClick({R2.id.action_add_new_shipping_address, R2.id.action_add_new_shipping_address_empty})
    public void onClickNewShipping() {
        if(Shopiroller.getListener() != null)
            Shopiroller.getListener().onNewShippingAddressClicked(requireActivity());
    }

    @OnClick({R2.id.action_add_new_billing_address, R2.id.action_add_new_billing_address_empty})
    public void onClickNewBilling() {
        if(Shopiroller.getListener() != null)
            Shopiroller.getListener().onNewBillingAddressClicked(requireActivity());
    }

    private void getDefaultAddresses() {

        if(Shopiroller.getListener() != null) {
            Shopiroller.getListener().getDefaultAddresses(new ShopirollerCallBackListener<DefaultAddressList>() {
                @Override
                public void onSuccess(DefaultAddressList data) {
                    bottomLayout.setVisibility(View.VISIBLE);
                    defaultAddressList = data;
                    setupView();
                }

                @Override
                public void onError(String errorMessage) {
                    // TODO show errorMessage
                    ErrorUtils.showErrorToast(getContext());
                }
            });
        }
    }

    @OnClick(R2.id.billing_address_action_edit)
    public void onClickBillingEdit() {
        if(Shopiroller.getListener() != null)
            Shopiroller.getListener().onEditBillingAddressClicked(requireActivity(),  defaultAddressList.billingAddress);
    }

    @OnClick(R2.id.shipping_address_action_edit)
    public void onClickShippingEdit() {
        if(Shopiroller.getListener() != null)
            Shopiroller.getListener().onEditShippingAddressClicked(requireActivity(), defaultAddressList.shippingAddress);
    }

    private void setupView() {
        deliveryLayout.setVisibility(View.VISIBLE);
        billingLayout.setVisibility(View.VISIBLE);
        setBillingLayout();
        setShippingLayout();
        checkIsValid();
    }

    private void setShippingLayout() {
        if (defaultAddressList.shippingAddress != null) {
            deliveryEmptyLayout.setVisibility(View.GONE);
            deliveryContentLayout.setVisibility(View.VISIBLE);
            otherShippingAddresses.setVisibility(View.VISIBLE);
            shippingAddressTitle.setText(defaultAddressList.shippingAddress.title);
            shippingAddressDescriptionFirst.setText(defaultAddressList.shippingAddress.addressLine);
            shippingAddressDescription.setText(defaultAddressList.shippingAddress.getDescriptionArea());
            actionAddNewShippingAddress.setVisibility(View.VISIBLE);
        } else {
            deliveryEmptyLayout.setVisibility(View.VISIBLE);
            deliveryContentLayout.setVisibility(View.GONE);
            otherShippingAddresses.setVisibility(View.GONE);
            actionAddNewShippingAddress.setVisibility(View.GONE);
        }
    }

    private void setBillingLayout() {

        if (defaultAddressList.billingAddress != null) {
            billingEmptyLayout.setVisibility(View.GONE);
            billingContentLayout.setVisibility(View.VISIBLE);
            otherBillingAddresses.setVisibility(View.VISIBLE);
            billingAddressTitle.setText(defaultAddressList.billingAddress.title);
            billingAddressDescriptionFirst.setText(defaultAddressList.billingAddress.addressLine);
            billingAddressDescription.setText(defaultAddressList.billingAddress.getDescriptionArea());
            actionAddNewBillingAddress.setVisibility(View.VISIBLE);
        } else {
            billingEmptyLayout.setVisibility(View.VISIBLE);
            billingContentLayout.setVisibility(View.GONE);
            otherBillingAddresses.setVisibility(View.GONE);
            actionAddNewBillingAddress.setVisibility(View.GONE);
        }
    }

    private void checkIsValid() {
        if (billingContentLayout.getVisibility() == View.VISIBLE && deliveryContentLayout.getVisibility() == View.VISIBLE)
            isValid = true;
        if (isValid) {
            EventBus.getDefault().post(new OrderAddressesEvent(defaultAddressList.shippingAddress, defaultAddressList.billingAddress));
        }
        setContinueButton(isValid);
        int currentStep = 1;
        EventBus.getDefault().post(new ValidateStepEvent(currentStep, isValid));
    }

    private void getShippingAddressList() {

        if (NetworkHelper.isConnected(getContext())) {
            legacyProgressViewHelper.show();

            Shopiroller.getListener().getShippingAddresses(new ShopirollerCallBackListener<List<UserShippingAddressModel>>() {
                @Override
                public void onSuccess(List<UserShippingAddressModel> data) {
                    if (!getActivity().isFinishing() && legacyProgressViewHelper.isShowing())
                        legacyProgressViewHelper.dismiss();
                        List<Object> list = new ArrayList<>();
                    if (data != null && data.size() != 0) {
                        list.addAll(data);
                        UserPopupAddressAdapter adapter = new UserPopupAddressAdapter((AppCompatActivity) getActivity(), list);
                        showListDialog(adapter, getString(R.string.e_commerce_address_selection_address_shipping_popup_title), getString(R.string.e_commerce_address_selection_address_shipping_popup_description), defaultAddressList.shippingAddress);
                    } else {
                        ErrorUtils.showErrorToast(getContext());
                    }
                }

                @Override
                public void onError(@NonNull String errorMessage) {
                    if (!getActivity().isFinishing() && legacyProgressViewHelper.isShowing())
                        legacyProgressViewHelper.dismiss();
                    ErrorUtils.showErrorToast(getContext());
                }
            });
        } else
            DialogUtil.showNoConnectionInfo(getActivity());
    }

    private void getBillingAddressList() {

        if (NetworkHelper.isConnected(getContext())) {
            legacyProgressViewHelper.show();

            Shopiroller.getListener().getBillingAddresses(new ShopirollerCallBackListener<List<UserBillingAddressModel>>() {
                @Override
                public void onSuccess(List<UserBillingAddressModel> data) {
                    if (!getActivity().isFinishing() && legacyProgressViewHelper.isShowing())
                                legacyProgressViewHelper.dismiss();
                    List<Object> list = new ArrayList<>();
                    if (data != null && data.size() != 0) {
                        list.addAll(data);
                        UserPopupAddressAdapter adapter = new UserPopupAddressAdapter((AppCompatActivity) getActivity(), list);
                        showListDialog(adapter, getString(R.string.e_commerce_address_selection_address_invoice_popup_title), getString(R.string.e_commerce_address_selection_address_invoice_popup_description), defaultAddressList.billingAddress);
                    } else
                        ErrorUtils.showErrorToast(getContext());
                }

                @Override
                public void onError(@NonNull String errorMessage) {
                    if (!getActivity().isFinishing() && legacyProgressViewHelper.isShowing())
                        legacyProgressViewHelper.dismiss();
                    ErrorUtils.showErrorToast(getContext());
                }
            });

        } else
            DialogUtil.showNoConnectionInfo(getContext());
    }

    private void showListDialog(UserPopupAddressAdapter adapter, String title, String description, BaseAddressModel baseAddressModel) {
//        if (DynamicConstants.MobiRoller_Stage) {
//            return;
//        }

        new ShopirollerDialog.Builder()
                .setContext(getActivity())
                .setTitle(title)
                .setIconResource(R.drawable.ic_truck_icon)
                .setType(MobirollerDialogType.LIST)
                .setAdapter(adapter)
                .setHasDivider(true)
                .setListSelectionListener(position -> {
                    Object selectedAddress = adapter.getSelectedAddress(position);
                    if (selectedAddress instanceof UserBillingAddressModel) {
                        defaultAddressList.billingAddress = (UserBillingAddressModel) selectedAddress;
                        setBillingLayout();
                    } else {
                        defaultAddressList.shippingAddress = (UserShippingAddressModel) selectedAddress;
                        setShippingLayout();
                    }
                    checkIsValid();
                })
                .setColor(Color.parseColor("#f8e7d8"))
                .show();
    }

    @OnClick({R2.id.other_shipping_addresses, R2.id.other_billing_addresses})
    public void onViewClicked(View view) {
        int id = view.getId();
        if (id == R.id.other_shipping_addresses) {
            getShippingAddressList();
        } else if (id == R.id.other_billing_addresses) {
            getBillingAddressList();
        }
    }

    @OnClick(R2.id.continue_button)
    public void onViewClicked() {
        EventBus.getDefault().post(new AddressContinueEvent());
    }

    private void setContinueButton(boolean isEnabled) {
        continueButton.setEnabled(isEnabled);
    }

    @OnTouch({R2.id.bottom_layout, R2.id.continue_layout, R2.id.top_layout})
    public boolean onTouchEvent(MotionEvent event) {
        return false;
    }

    @Override
    public void onDestroy() {
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        super.onDestroy();
    }

    @Override
    public void onNewBillingAddress(@NonNull UserBillingAddressModel billingAddressModel) {
        defaultAddressList.billingAddress = billingAddressModel;
        setBillingLayout();
        checkIsValid();
    }

    @Override
    public void onNewShippingAddress(@NonNull UserShippingAddressModel shippingAddressModel) {
        defaultAddressList.shippingAddress = shippingAddressModel;
        setShippingLayout();
        checkIsValid();
    }

    @Override
    public void onEditBillingAddress(@NonNull UserBillingAddressModel billingAddressModel) {
        defaultAddressList.billingAddress = billingAddressModel;
        setBillingLayout();
    }

    @Override
    public void onEditShippingAddress(@NonNull UserShippingAddressModel shippingAddressModel) {
        defaultAddressList.shippingAddress = shippingAddressModel;
        setShippingLayout();
    }
}
