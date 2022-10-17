package com.shopiroller.activities

import android.os.Bundle
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import butterknife.ButterKnife
import com.shopiroller.R
import com.shopiroller.constants.Constants
import com.shopiroller.fragments.filter.FilterFragment
import com.shopiroller.helpers.ToolbarHelper
import com.shopiroller.models.FilterModel
import com.shopiroller.views.legacy.ShopirollerToolbar

class FilterActivity : ECommerceBaseActivity(),
    FragmentManager.OnBackStackChangedListener {

    companion object {
        const val FILTER_MODEL_BUNDLE = "filterModelBundle"
    }

    interface FilterClearCallBack {
        fun onFilterClear()
    }

    private lateinit var toolbar: ShopirollerToolbar
    private var isOpened = false

    private var searchText: String? = null
    private var categoryId: String? = null
    private var showCaseId: String? = null
    private lateinit var filterModel: FilterModel

    private var filterClearCallBack: FilterClearCallBack? = null

    private lateinit var clearText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_filter)
        ButterKnife.bind(this)

        clearText = findViewById(R.id.clear_text)

        if ((intent.hasExtra(Constants.SEARCH_TEXT) ||
                    intent.hasExtra(Constants.CATEGORY_ID) ||
                    intent.hasExtra(Constants.SHOW_CASE_ID)) &&
            intent.hasExtra(FILTER_MODEL_BUNDLE)
        ) {
            searchText = intent.getStringExtra(Constants.SEARCH_TEXT)
            categoryId = intent.getStringExtra(Constants.CATEGORY_ID)
            showCaseId = intent.getStringExtra(Constants.SHOW_CASE_ID)
            filterModel = intent.getSerializableExtra(FILTER_MODEL_BUNDLE) as FilterModel
            toolbar = findViewById(R.id.toolbar_top)

            ToolbarHelper().setStatusBar(this)
            toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp)
            setSupportActionBar(toolbar)
            toolbar.setTitleTypeface()
            toolbar.setNavigationOnClickListener { onBackPressed() }

            supportFragmentManager.addOnBackStackChangedListener(this)

            clearText.setOnClickListener {
                filterClearCallBack?.onFilterClear()
            }

            startFilterFragment()
        } else {
            finish()
        }
    }

    private fun startFilterFragment() {
        val bundle = Bundle()
        bundle.putString(Constants.SEARCH_TEXT, searchText)
        bundle.putString(Constants.CATEGORY_ID, categoryId)
        bundle.putString(Constants.SHOW_CASE_ID, showCaseId)
        bundle.putSerializable(FILTER_MODEL_BUNDLE, filterModel)

        val filterFragment = FilterFragment()
        filterFragment.arguments = bundle
        filterClearCallBack = filterFragment

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()

        fragmentTransaction.replace(
            R.id.filter_frame_layout,
            filterFragment
        )

        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
        isOpened = true
    }

    fun setClearButtonVisibility(visibility: Int) {
        clearText.visibility = visibility
    }

    override fun onBackStackChanged() {
        if (supportFragmentManager.findFragmentById(R.id.filter_frame_layout) == null && isOpened) {
            finish()
        }
    }
}