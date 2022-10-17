package com.shopiroller.views

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.WindowManager
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.AppBarLayout
import com.shopiroller.R
import com.shopiroller.activities.FilterActivity
import com.shopiroller.adapter.ProductListAdapter
import com.shopiroller.constants.Constants
import com.shopiroller.helpers.EndlessRecyclerViewScrollListener
import com.shopiroller.helpers.LegacyProgressViewHelper
import com.shopiroller.helpers.ProgressViewHelper
import com.shopiroller.models.ECommerceResponse
import com.shopiroller.models.ProductDetailModel
import com.shopiroller.network.ECommerceRequestHelper
import com.shopiroller.network.ECommerceRequestHelper.ECommerceCallBack
import com.shopiroller.util.ErrorUtils
import com.shopiroller.util.OrderOptionOrientation
import com.shopiroller.util.OrderOptionType
import com.shopiroller.views.legacy.ShopirollerEmptyView
import com.shopiroller.views.legacy.ShopirollerTextView
import retrofit2.Call

class ProductListRecyclerView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : ConstraintLayout(context, attrs) {

    companion object {
        const val PAGE = "page"
        const val PER_PAGE = "perPage"
        const val SORT = "sort"
        const val SORT_BY = "sortBy"
        const val TITLE = "title"
        const val ITEM_PER_PAGE = 20
        const val REQUEST_CODE = 200
    }

    private var mNumberOfPage = 1

    private var filterModel: com.shopiroller.models.FilterModel =
        com.shopiroller.models.FilterModel()
    private var adapter: ProductListAdapter? = null
    private var gridLayoutManager: GridLayoutManager? = null
    private var endlessRecyclerViewScrollListener: EndlessRecyclerViewScrollListener? = null

    private var dataList: List<com.shopiroller.models.ProductDetailModel> = mutableListOf()

    private var progressViewLegacy: LegacyProgressViewHelper? = null
    private var progressView: ProgressViewHelper? = null
    private var activity: Activity? = null
    private var fragment: Fragment? = null

    private var recyclerView: RecyclerView? = null
    private var emptyView: ShopirollerEmptyView? = null
    private var filterBadgeImageView: ImageView? = null
    private var filterLayout: AppBarLayout? = null
    private var filterLinearLayout: LinearLayout? = null
    private var orderOptionsLinearLayout: LinearLayout? = null

    private var orderOptionType = OrderOptionType.PublishmentDate
    private var orderOptionOrientation = OrderOptionOrientation.Descending
    private var hasFilter = false
    private var emptyViewText = ""
    private var showcaseId: String? = null
    private var categoryId: String? = null
    private var keyword: String? = null

    init {
        LayoutInflater.from(context)
            .inflate(R.layout.product_list_component, this, true)
        recyclerView = findViewById(R.id.list)
        emptyView = findViewById(R.id.empty_view)
        filterBadgeImageView = findViewById(R.id.filter_badge_image_view)
        filterLinearLayout = findViewById(R.id.filter_linear_layout)
        orderOptionsLinearLayout = findViewById(R.id.order_options_linear_layout)
        filterLayout = findViewById(R.id.app_bar_layout)

        filterLinearLayout?.setOnClickListener {
            onFilterLinearLayoutClicked()
        }

        orderOptionsLinearLayout?.setOnClickListener {
            orderOptionsLinearLayoutClicked()
        }
    }

    fun setShowCaseId(id: String) {
        this.showcaseId = id
    }

    fun setKeyword(keyword: String) {
        this.keyword = keyword
    }

    fun setCategoryId(id: String) {
        this.categoryId = id
    }

    fun setEmptyViewText(emptyViewText: String) {
        this.emptyViewText = emptyViewText
    }

    fun setDefaultList(list: List<com.shopiroller.models.ProductDetailModel>) {
        dataList = list
    }

    fun setProgressView(progressViewHelper: LegacyProgressViewHelper) {
        this.progressViewLegacy = progressViewHelper
    }

    fun setProgressView(progressViewHelper: ProgressViewHelper) {
        this.progressView = progressViewHelper
    }

    fun initialize(activity: Activity, fragment: Fragment?) {
        this.activity = activity
        this.fragment = fragment
        adapter = ProductListAdapter(activity, ArrayList())
        gridLayoutManager = GridLayoutManager(context, 2)
        recyclerView?.adapter = adapter
        recyclerView?.layoutManager = gridLayoutManager
        endlessRecyclerViewScrollListener =
            object : EndlessRecyclerViewScrollListener(gridLayoutManager) {
                override fun onLoadMore(page: Int, totalItemsCount: Int) {
                    mNumberOfPage++
                    getProducts(false)
                }
            }

        endlessRecyclerViewScrollListener?.let {
            recyclerView?.addOnScrollListener(it)
        }
    }

    fun loadData() {
        getProducts(true)
    }

    fun reloadData() {
        mNumberOfPage = 1
        loadData()
    }

    internal fun getProducts(clear: Boolean = false) {
        progressView?.show()

        val queryMap = mutableMapOf<String, Any>()

        if(filterModel.hasFilter()) {
            queryMap.putAll(filterModel.getQueryMap())
        }

        queryMap[PAGE] = mNumberOfPage.toString()
        queryMap[PER_PAGE] = ITEM_PER_PAGE.toString()
        queryMap[SORT] = orderOptionType.toString()
        queryMap[SORT_BY] = orderOptionOrientation.toString()

        showcaseId?.let {
            queryMap[Constants.SHOW_CASE_ID] = it
        }

        categoryId?.let {
            queryMap[Constants.CATEGORY_ID] = it
        }

        keyword?.let {
            queryMap[TITLE] = it
        }

        filterModel.variantData?.let {
            queryMap["variantData"] = it
        }

        hasFilter = filterModel.hasFilter()
        val responseCall: Call<ECommerceResponse<List<ProductDetailModel>>?> =
            ECommerceRequestHelper().apiService.getProductsWithAdvancedFiltered(queryMap)
        ECommerceRequestHelper().enqueue(
            responseCall,
            object : ECommerceCallBack<List<com.shopiroller.models.ProductDetailModel>> {
                override fun done() {
                    if (progressView?.isShowing == true) progressView?.dismiss()
                }

                override fun onFailure(result: com.shopiroller.models.ECommerceErrorResponse?) {
                    ErrorUtils.showErrorToast(context)
                    setContent()
                }

                override fun onNetworkError(result: String?) {
                    ErrorUtils.showErrorToast(context)
                    setContent()
                }

                override fun onSuccess(result: List<com.shopiroller.models.ProductDetailModel>) {
                    if (clear) {
                        adapter!!.deleteAll()
                    }
                    setContent(result)
                }
            })
    }

    fun setContent() {
        setContent(null)
    }

    fun setContent(newItems: List<com.shopiroller.models.ProductDetailModel?>?) {
        if (adapter?.itemCount == 0 && (newItems == null || newItems.isEmpty())) {
            setEmptyView()
        } else {
            emptyView?.visibility = GONE
            recyclerView?.visibility = VISIBLE
            filterLayout?.visibility = VISIBLE
            adapter?.addItems(newItems)

            if (adapter?.itemCount == newItems?.count())
                recyclerView?.scheduleLayoutAnimation()
        }
    }

    private fun setEmptyView() {
        if (adapter?.itemCount == 0) {
            emptyView?.visibility = VISIBLE
            recyclerView?.visibility = GONE
            if (hasFilter) {
                filterLayout?.visibility = VISIBLE
            }
            emptyView?.setTitle(emptyViewText)
        }
    }

    private fun onFilterLinearLayoutClicked() {
        fragment?.let { fragment ->
            val intent = Intent(context, FilterActivity::class.java)
            categoryId?.let {
                intent.putExtra(Constants.CATEGORY_ID, it)
            }
            keyword?.let {
                intent.putExtra(Constants.SEARCHED_KEYWORD, it)
            }
            showcaseId?.let {
                intent.putExtra(Constants.SHOW_CASE_ID, it)
            }
            intent.putExtra(FilterActivity.FILTER_MODEL_BUNDLE, filterModel)

            fragment.startActivityForResult(intent, REQUEST_CODE)
            return
        }

        val intent = Intent(context, FilterActivity::class.java)
        categoryId?.let {
            intent.putExtra(Constants.CATEGORY_ID, it)
        }
        keyword?.let {
            intent.putExtra(Constants.SEARCHED_KEYWORD, it)
        }
        showcaseId?.let {
            intent.putExtra(Constants.SHOW_CASE_ID, it)
        }
        intent.putExtra(FilterActivity.FILTER_MODEL_BUNDLE, filterModel)
        activity?.startActivityForResult(intent, REQUEST_CODE)
    }

    private fun orderOptionsLinearLayoutClicked() {
        activity?.let {
            val dialog = Dialog(it, R.style.AppCompatTransparent)
            dialog.setContentView(R.layout.dialog_ecommerce_order_options)
            dialog.window!!.setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT
            )
            configureDoneImages(dialog)
            val orderCountDescendingTextView: ShopirollerTextView =
                dialog.findViewById(R.id.order_count_descending_order_text_view)
            val priceAscendingTextView: ShopirollerTextView =
                dialog.findViewById(R.id.price_ascending_order_text_view)
            val priceDescendingTextView: ShopirollerTextView =
                dialog.findViewById(R.id.price_descending_order_text_view)
            val datePublishmentDescendingTextView: ShopirollerTextView =
                dialog.findViewById(R.id.date_publishment_date_descending_text_view)
            orderCountDescendingTextView.setOnClickListener {
                orderOptionType = OrderOptionType.StatsOrderCount
                orderOptionOrientation = OrderOptionOrientation.Descending
                configureDoneImages(dialog)
                dialog.dismiss()
                updateProductWithOrderOptions()
            }
            priceAscendingTextView.setOnClickListener {
                orderOptionType = OrderOptionType.CalculatedPrice
                orderOptionOrientation = OrderOptionOrientation.Ascending
                configureDoneImages(dialog)
                dialog.dismiss()
                updateProductWithOrderOptions()
            }
            priceDescendingTextView.setOnClickListener {
                orderOptionType = OrderOptionType.CalculatedPrice
                orderOptionOrientation = OrderOptionOrientation.Descending
                configureDoneImages(dialog)
                dialog.dismiss()
                updateProductWithOrderOptions()
            }
            datePublishmentDescendingTextView.setOnClickListener {
                orderOptionType = OrderOptionType.PublishmentDate
                orderOptionOrientation = OrderOptionOrientation.Descending
                configureDoneImages(dialog)
                dialog.dismiss()
                updateProductWithOrderOptions()
            }
            dialog.show()
        }
    }

    private fun configureDoneImages(dialog: Dialog) {
        val orderCountDescendingDoneImageView =
            dialog.findViewById<ImageView>(R.id.order_count_descending_done_image_view)
        val priceAscendingDoneImageView =
            dialog.findViewById<ImageView>(R.id.price_ascending_done_image_view)
        val priceDescendingDoneImageView =
            dialog.findViewById<ImageView>(R.id.price_descending_done_image_view)
        val datePublishmentDateDescendingDoneImageView =
            dialog.findViewById<ImageView>(R.id.date_publishment_date_descending_done_image_view)
        orderCountDescendingDoneImageView.visibility = GONE
        priceAscendingDoneImageView.visibility = GONE
        priceDescendingDoneImageView.visibility = GONE
        datePublishmentDateDescendingDoneImageView.visibility = GONE
        if (orderOptionType == OrderOptionType.StatsOrderCount && orderOptionOrientation == OrderOptionOrientation.Descending) {
            orderCountDescendingDoneImageView.visibility = VISIBLE
        } else if (orderOptionType == OrderOptionType.CalculatedPrice && orderOptionOrientation == OrderOptionOrientation.Ascending) {
            priceAscendingDoneImageView.visibility = VISIBLE
        } else if (orderOptionType == OrderOptionType.CalculatedPrice && orderOptionOrientation == OrderOptionOrientation.Descending) {
            priceDescendingDoneImageView.visibility = VISIBLE
        } else if (orderOptionType == OrderOptionType.PublishmentDate && orderOptionOrientation == OrderOptionOrientation.Descending) {
            datePublishmentDateDescendingDoneImageView.visibility = VISIBLE
        }
    }

    private fun updateProductWithOrderOptions() {
        mNumberOfPage = 1
        getProducts(true)
    }

    fun onFilterResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 200) {
                mNumberOfPage = 1
                filterModel = data?.getSerializableExtra(FilterActivity.FILTER_MODEL_BUNDLE) as com.shopiroller.models.FilterModel
                getProducts(true)
                filterBadgeImageView?.visibility =
                    if (filterModel.hasFilter()) VISIBLE else INVISIBLE
            }
        }
    }

    fun setEmptyViewVisibility(visibility: Int) {
        emptyView?.visibility = visibility
    }

}