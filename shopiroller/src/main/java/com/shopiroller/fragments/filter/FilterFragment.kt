package com.shopiroller.fragments.filter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.CompoundButton
import android.widget.EditText
import androidx.fragment.app.Fragment
import butterknife.ButterKnife
import com.shopiroller.util.ECommerceUtil
import com.shopiroller.R
import com.shopiroller.Shopiroller
import com.shopiroller.activities.FilterActivity
import com.shopiroller.activities.FilterActivity.FilterClearCallBack
import com.shopiroller.adapter.FilterMenuAdapter
import com.shopiroller.constants.Constants
import com.shopiroller.helpers.ProgressViewHelper
import com.shopiroller.models.*
import com.shopiroller.network.ECommerceRequestHelper
import com.shopiroller.util.ErrorUtils
import kotlinx.android.synthetic.main.fragment_filter.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FilterFragment : Fragment(), CompoundButton.OnCheckedChangeListener,
    FilterSelectionFragment.FilterSelectionCallBack,
    FilterMultiChoiceFragment.FilterMultiChoiceCallBack,
    FilterMenuAdapter.FilterMenuListener, FilterClearCallBack {

    private lateinit var progressViewHelper: ProgressViewHelper

    private var searchText: String? = null
    private var categoryId: String? = null
    private var showCaseId: String? = null
    private var filterOptionsResponse: FilterOptionsResponse? = null

    private var selectedCategory: CategoriesItem? = null
    private var selectedMultiChoiceItems: MutableMap<String, ArrayList<MultiChoiceItem?>?> =
        mutableMapOf()

    private var brandId: String = "100"

    private var adapter: FilterMenuAdapter? = null
    private lateinit var filterModel: FilterModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            searchText = it.getString(Constants.SEARCH_TEXT)
            categoryId = it.getString(Constants.CATEGORY_ID)
            showCaseId = it.getString(Constants.SHOW_CASE_ID)
            filterModel = it.getSerializable(FilterActivity.FILTER_MODEL_BUNDLE) as FilterModel
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_filter, container, false)
        ButterKnife.bind(this, view)
        progressViewHelper = ProgressViewHelper(requireActivity())
        progressViewHelper.notCancelable()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getFilterOptions()
        getPaymentSettings()
    }

    private fun configureSession() {
        filterOptionsResponse?.categories?.forEach { category ->
            if (category?.categoryId == filterModel.categoryIds?.get(0)) {
                category?.isSelected = true
                selectedCategory = category
                selected_categories_text_view?.text = selectedCategory?.name?.get(
                    Shopiroller.adapter?.getLocale()) ?: ""
                selected_categories_text_view?.visibility = View.VISIBLE
            }
        }
        var text = ""
        filterOptionsResponse?.brands?.forEach { brand ->
            brand?.isChecked = false
            filterModel.brandIds?.forEach { id ->
                if (brand?.id == id) {
                    brand?.isChecked = true
                    text += brand?.name?.plus(" ")
                    selected_brands_text_view?.text = text
                    if (selected_brands_text_view?.visibility == View.GONE)
                        selected_brands_text_view?.visibility = View.VISIBLE
                    return@forEach
                } else
                    brand?.isChecked = false
            }
        }

        ECommerceUtil.getSelectedVariantList(filterModel?.variantData)
        getVariationGroups()?.let { variantGroups ->
            variantGroups.forEach { variantGroup ->
                var text = ""
                variantGroup?.variations?.forEach { variation ->
                    variation?.isChecked = false
                    run loop@{
                        filterModel.variantData?.let {
                           val variantHashMap = ECommerceUtil.getSelectedVariantList(it)
                            variantHashMap?.forEach { vm ->
                                if (vm?.value?.contains(variation?.id) == true) {
                                    variation?.isChecked = true
                                    text += variation?.value?.plus(" ")
                                    return@loop
                                } else {
                                    variation?.isChecked = false
                                }
                            }
                        }
                    }
                }
                variantGroup?.selectedItems = text
                adapter?.notifyDataSetChanged()
            }
        }

        minimum_price_edit_text.setText(filterModel.minimumPrice?.toString())
        maximum_price_edit_text.setText(filterModel.maximumPrice?.toString())

        stock_switch.isChecked = filterModel.stockSwitch
        discounted_products_switch.isChecked = filterModel.discountedProductsSwitch
        free_shipping_switch.isChecked = filterModel.freeShippingSwitch
    }

    private fun getFilterOptions() {
        progressViewHelper.show()

        ECommerceRequestHelper().apiService.getFilterOptions(searchText, categoryId, showCaseId)
            .enqueue(object :
                Callback<ECommerceResponse<FilterOptionsResponse>> {
                override fun onResponse(
                    call: Call<ECommerceResponse<FilterOptionsResponse>>,
                    response: Response<ECommerceResponse<FilterOptionsResponse>>
                ) {
                    progressViewHelper.dismiss()
                    response.body()?.data?.let {
                        filterOptionsResponse = it
                        configureUI()
                    }
                }

                override fun onFailure(
                    call: Call<ECommerceResponse<FilterOptionsResponse>>,
                    t: Throwable
                ) {
                    progressViewHelper.dismiss()
                    ErrorUtils.showErrorToast(context)
                }
            })
    }

    private fun configureUI() {
        if (!isCategoriesNullOrEmpty()) {
            category_container.visibility = View.VISIBLE

            category_container.setOnClickListener {
                val selectionFragment =
                    FilterSelectionFragment.newInstance(
                        filterOptionsResponse?.categories as ArrayList<CategoriesItem?>?,
                        this
                    )
                replaceFragment(selectionFragment)
            }
        }

        if (isBrandsNullOrEmpty()) {
            brand_container.visibility = View.VISIBLE

            brand_container.setOnClickListener {
                var multiChoiceList: ArrayList<MultiChoiceItem?>?

                filterOptionsResponse?.brands?.let { brands ->
                    multiChoiceList = arrayListOf()
                    brands.forEach { brand ->
                        multiChoiceList?.add(
                            MultiChoiceItem(
                                brand?.name,
                                brand?.id,
                                brand?.isChecked ?: false
                            )
                        )
                    }
                    actionToMultiChoiceFragment(brandId, multiChoiceList)
                }
            }
        }

        if (!isVariationGroupsNullOrEmpty()) {
            adapter =
                FilterMenuAdapter(getVariationGroups() as ArrayList<VariationGroupsItem?>?, this)
            menu_list.adapter = adapter
        }

        minimum_price_edit_text.addTextChangedListener(
            EditTextChangedListener(
                minimum_price_edit_text
            )
        )
        maximum_price_edit_text.addTextChangedListener(
            EditTextChangedListener(
                maximum_price_edit_text
            )
        )
        minimum_price_edit_text.onFocusChangeListener = EditTextFocusChangeListener()
        maximum_price_edit_text.onFocusChangeListener = EditTextFocusChangeListener()

        stock_switch.setOnCheckedChangeListener(this)
        discounted_products_switch.setOnCheckedChangeListener(this)
        free_shipping_switch.setOnCheckedChangeListener(this)

        confirm_button.setOnClickListener {
            var categoryIds: ArrayList<String?>? = null
            selectedCategory?.categoryId?.let {
                categoryIds = arrayListOf(selectedCategory?.categoryId)
            }

            val brandIds: ArrayList<String?> = arrayListOf()
            filterOptionsResponse?.brands?.forEach { brand ->
                if (brand?.isChecked == true)
                    brandIds.add(brand.id)
            }

            filterModel.categoryIds = categoryIds
            filterModel.brandIds = brandIds
            filterModel.variantData = ECommerceUtil.formatVariantForQuery(getVariationGroups())

            val data = Intent()
            data.putExtra(FilterActivity.FILTER_MODEL_BUNDLE, filterModel)
            requireActivity().setResult(Activity.RESULT_OK, data)
            requireActivity().finish()
        }
        configureSession()
        filter_root.visibility = View.VISIBLE
    }

    private fun actionToMultiChoiceFragment(
        id: String?,
        multiChoiceList: ArrayList<MultiChoiceItem?>?
    ) {
        val multiChoiceFragment =
            FilterMultiChoiceFragment.newInstance(
                id,
                multiChoiceList,
                this
            )

        replaceFragment(multiChoiceFragment)
    }

    private fun isBrandsNullOrEmpty(): Boolean {
        return filterOptionsResponse?.brands.isNullOrEmpty()
    }

    private fun isCategoriesNullOrEmpty(): Boolean {
        return filterOptionsResponse?.categories.isNullOrEmpty()
    }

    private fun isVariationGroupsNullOrEmpty(): Boolean {
        return filterOptionsResponse?.variationGroups.isNullOrEmpty()
    }

    private fun getVariationGroups(): List<VariationGroupsItem?>? {
        return filterOptionsResponse?.variationGroups
    }

    override fun onCheckedChanged(compoundButton: CompoundButton?, isChecked: Boolean) {
        clearFocus()
        when (compoundButton?.id) {
            R.id.stock_switch -> filterModel.stockSwitch = isChecked
            R.id.discounted_products_switch -> filterModel.discountedProductsSwitch = isChecked
            R.id.free_shipping_switch -> filterModel.freeShippingSwitch = isChecked
        }
    }

    override fun onSelectionConfirmed(selectedItem: CategoriesItem?) {
        selectedCategory = selectedItem

        if (selectedCategory != null) {
            val name = selectedCategory?.name?.get(Shopiroller.adapter?.getLocale()) ?: ""

            if (name.isNotEmpty()) {
                selected_categories_text_view?.text = name
                selected_categories_text_view?.visibility = View.VISIBLE
            }
        } else {
            selected_categories_text_view?.visibility = View.GONE
        }
    }

    override fun onMultiChoiceConfirmed(id: String?, selectedItems: ArrayList<MultiChoiceItem?>?) {
        selectedMultiChoiceItems[id ?: ""] = selectedItems

        if (id == brandId) {
            selectedMultiChoiceItems[id]?.let { multiChoiceItemList ->
                var text = ""
                if (multiChoiceItemList.isNullOrEmpty()) {
                    selected_brands_text_view?.text = ""
                    selected_brands_text_view?.visibility = View.GONE
                } else {
                    multiChoiceItemList.forEach { multiChoiceItem ->
                        text += multiChoiceItem?.name?.plus(" ")
                        selected_brands_text_view?.text = text
                        selected_brands_text_view?.visibility = View.VISIBLE
                    }
                }
            }

            filterOptionsResponse?.brands?.forEach { brand ->
                brand?.isChecked = false
                selectedItems?.forEach { multiChoiceItems ->
                    if (brand?.id == multiChoiceItems?.id) {
                        brand?.isChecked = true
                        return
                    } else
                        brand?.isChecked = false
                }
            }
        } else {
            getVariationGroups()?.let { variantGroups ->
                variantGroups.forEach { variantGroup ->
                    variantGroup?.let {
                        var text = ""
                        variantGroup.variations?.forEach { variation ->
                            run loop@{
                                selectedMultiChoiceItems[it.id]?.forEach { multiChoiceItem ->
                                    if (variation?.id == multiChoiceItem?.id) {
                                        variation?.isChecked = true
                                        text += multiChoiceItem?.name?.plus(" ")
                                        variantGroup.selectedItems = text
                                        return@loop
                                    } else {
                                        variation?.isChecked = false
                                    }
                                }
                            }
                        }
                        adapter?.notifyDataSetChanged()
                    }
                }
            }
        }
    }

    override fun onMenuItemSelected(position: Int) {
        val variantGroup = getVariationGroups()?.get(position)

        var multiChoiceList: ArrayList<MultiChoiceItem?>?

        variantGroup?.let {
            it.variations?.let { variations ->
                multiChoiceList = arrayListOf()
                variations.forEach { variation ->
                    multiChoiceList?.add(
                        MultiChoiceItem(
                            variation?.value,
                            variation?.id,
                            variation?.isChecked ?: false
                        )
                    )
                }
                actionToMultiChoiceFragment(variantGroup.id, multiChoiceList)
            }
        }
    }

    override fun onFilterClear() {
        filterModel = FilterModel()
        configureSession()
        clearFocus()
    }

    private fun replaceFragment(fragment: Fragment) {
        clearFocus()
        val fragmentManager = requireActivity().supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()

        fragmentTransaction.add(
            R.id.filter_frame_layout,
            fragment
        )

        fragmentTransaction.addToBackStack(fragment::class.java.name)
        fragmentTransaction.commit()
    }

    private fun clearFocus() {
        minimum_price_edit_text.clearFocus()
        maximum_price_edit_text.clearFocus()
    }

    private inner class EditTextChangedListener(val editText: EditText) : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun afterTextChanged(p0: Editable?) {
            when (editText) {
                minimum_price_edit_text -> filterModel.minimumPrice =
                    p0.toString().toDoubleOrNull()
                maximum_price_edit_text -> filterModel.maximumPrice =
                    p0.toString().toDoubleOrNull()
            }
        }
    }

    private inner class EditTextFocusChangeListener : View.OnFocusChangeListener {
        override fun onFocusChange(v: View, hasFocus: Boolean) {
            if (!hasFocus) {
                val imm =
                    requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
                imm?.hideSoftInputFromWindow(view?.windowToken, 0)
            }
        }
    }

    private fun getPaymentSettings() {
        if (ECommerceUtil.getPaymentSettings() != null) {
            setCurrencies()
        } else {
            ECommerceUtil().getPaymentSettings(object :
                ECommerceRequestHelper.ECommerceCallBack<PaymentSettings> {
                override fun done() {
                }

                override fun onSuccess(result: PaymentSettings) {
                    setCurrencies()
                }

                override fun onFailure(result: ECommerceErrorResponse?) {
                }

                override fun onNetworkError(result: String?) {
                }

            })
        }
    }

    private fun setCurrencies() {
        minimum_price_currency_text_view.text = ECommerceUtil.getPaymentSettings().defaultCurrency
        maximum_price_currency_text_view.text = ECommerceUtil.getPaymentSettings().defaultCurrency
    }
}