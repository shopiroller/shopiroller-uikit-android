package com.shopiroller.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.shopiroller.R;
import com.shopiroller.R2;
import com.shopiroller.constants.Constants;
import com.shopiroller.helpers.ProgressViewHelper;
import com.shopiroller.models.CategoryResponseModel;
import com.shopiroller.models.ECommerceErrorResponse;
import com.shopiroller.models.ECommerceResponse;
import com.shopiroller.network.ECommerceRequestHelper;
import com.shopiroller.util.ErrorUtils;
import com.shopiroller.views.ProductListRecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;


public class CategoryProductListFragment extends Fragment {

    @BindView(R2.id.product_list)
    ProductListRecyclerView productListComponent;

    private CategoryResponseModel model;
    private ProgressViewHelper progressViewHelper;

    public CategoryProductListFragment() {
        // Required empty public constructor
    }

    public static CategoryProductListFragment newInstance(CategoryResponseModel model) {
        CategoryProductListFragment fragment = new CategoryProductListFragment();
        Bundle args = new Bundle();
        args.putParcelable(Constants.INTENT_EXTRA_CATEGORY_MODEL, model);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category_product_list, container, false);

        ButterKnife.bind(this, view);

        setTitle("");

        progressViewHelper = new ProgressViewHelper(getActivity());

        if (getArguments() != null) {
            model = getArguments().getParcelable(Constants.INTENT_EXTRA_CATEGORY_MODEL);
        }

        if (model != null && model.getName() != null) {
            setTitle(model.getName());
        } else {
            getCategoryName();
        }

        productListComponent.setCategoryId(model.getCategoryId());
        productListComponent.setProgressView(progressViewHelper);
        productListComponent.initialize(getActivity(),this);
        productListComponent.loadData();

        return view;
    }

    private void getCategoryName() {
        Call<ECommerceResponse<CategoryResponseModel>> responseCall = new ECommerceRequestHelper().getApiService().getCategories(model.getCategoryId());
        new ECommerceRequestHelper().enqueue(responseCall, new ECommerceRequestHelper.ECommerceCallBack<CategoryResponseModel>() {
            @Override
            public void done() {
            }

            @Override
            public void onSuccess(CategoryResponseModel result) {
                if (result != null) {
                    setTitle(result.getName());
                }
            }

            @Override
            public void onFailure(ECommerceErrorResponse result) {
                ErrorUtils.showErrorToast(getActivity());
            }

            @Override
            public void onNetworkError(String result) {
                ErrorUtils.showErrorToast(getActivity());
            }
        });
    }

    private void setTitle(String title) {
        requireActivity().setTitle(title);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        productListComponent.onFilterResult(requestCode,resultCode,data);
    }

}
