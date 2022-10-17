package com.shopiroller.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.shopiroller.R;
import com.shopiroller.R2;
import com.shopiroller.activities.CategoryListActivity;
import com.shopiroller.adapter.VerticalCategoryListAdapter;
import com.shopiroller.constants.Constants;
import com.shopiroller.helpers.ProgressViewHelper;
import com.shopiroller.models.CategoriesMobileSettings;
import com.shopiroller.models.CategoriesWithOptionsModel;
import com.shopiroller.models.CategoryResponseModel;
import com.shopiroller.models.ECommerceResponse;
import com.shopiroller.network.ECommerceRequestHelper;
import com.shopiroller.util.ErrorUtils;
import com.shopiroller.views.legacy.ShopirollerTextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryListFragment extends Fragment {

    @BindView(R2.id.category_list_recycler_view)
    RecyclerView categoryListRecyclerView;
    @BindView(R2.id.main_category_text_view)
    ShopirollerTextView mainCategoryTextView;

    private ProgressViewHelper progressViewHelper;
    private CategoryResponseModel subCategoryModel;
    private CategoriesMobileSettings mobileSettings;

    public CategoryListFragment() { }

    public static CategoryListFragment newInstance() {
        return new CategoryListFragment();
    }

    public static CategoryListFragment newInstance(CategoryResponseModel model, CategoriesMobileSettings mobileSettings) {
        CategoryListFragment fragment = new CategoryListFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean(Constants.INTENT_EXTRA_IS_MAIN_CATEGORY_LIST, true);
        bundle.putParcelable(Constants.INTENT_EXTRA_SUB_CATEGORY_MODEL, model);
        bundle.putSerializable(Constants.INTENT_EXTRA_CATEGORIES_MOBILE_SETTINGS, mobileSettings);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category_list, container, false);

        ButterKnife.bind(this, view);

        progressViewHelper = new ProgressViewHelper(getActivity());

        if (getArguments() != null) {
            if (getArguments().containsKey(Constants.INTENT_EXTRA_SUB_CATEGORY_MODEL) && getArguments().containsKey(Constants.INTENT_EXTRA_CATEGORIES_MOBILE_SETTINGS)) {
                this.subCategoryModel = getArguments().getParcelable(Constants.INTENT_EXTRA_SUB_CATEGORY_MODEL);
                this.mobileSettings = (CategoriesMobileSettings) getArguments().getSerializable(Constants.INTENT_EXTRA_CATEGORIES_MOBILE_SETTINGS);

                requireActivity().setTitle(subCategoryModel.getName());

                LinearLayoutManager manager = new LinearLayoutManager(getContext());
                VerticalCategoryListAdapter adapter = new VerticalCategoryListAdapter(getActivity(), this.subCategoryModel.getSubCategories(),
                        this.mobileSettings);
                categoryListRecyclerView.setLayoutManager(manager);
                categoryListRecyclerView.setAdapter(adapter);

                categoryListRecyclerView.setVisibility(View.VISIBLE);

                mainCategoryTextView.setText(String.format(getResources().getString(R.string.e_commerce_category_list_see_all_product_for_category), subCategoryModel.getName()));
                mainCategoryTextView.setVisibility(View.VISIBLE);
            }
        } else {
            getCategories();

            requireActivity().setTitle(R.string.e_commerce_category_list_categories_title);
        }

        return view;
    }

    private void getCategories() {
        progressViewHelper.show();

        Call<ECommerceResponse<CategoriesWithOptionsModel>> responseCall = new ECommerceRequestHelper().getApiService().getCategories();

        responseCall.enqueue(new Callback<ECommerceResponse<CategoriesWithOptionsModel>>() {
            @Override
            public void onResponse(Call<ECommerceResponse<CategoriesWithOptionsModel>> call, Response<ECommerceResponse<CategoriesWithOptionsModel>> response) {
                progressViewHelper.dismiss();

                LinearLayoutManager manager = new LinearLayoutManager(getContext());

                VerticalCategoryListAdapter adapter = new VerticalCategoryListAdapter(getActivity(), response.body().data.getCategories(),
                        response.body().data.getMobileSettings());
                categoryListRecyclerView.setLayoutManager(manager);
                categoryListRecyclerView.setAdapter(adapter);

                categoryListRecyclerView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(Call<ECommerceResponse<CategoriesWithOptionsModel>> call, Throwable t) {
                progressViewHelper.dismiss();
                ErrorUtils.showErrorToast(getContext());
            }
        });
    }

    @OnClick(R2.id.main_category_text_view)
    public void onMainCategoryTextViewClicked() {
        ((CategoryListActivity) getActivity()).startCategoryProductListFragment(subCategoryModel);
    }

}
