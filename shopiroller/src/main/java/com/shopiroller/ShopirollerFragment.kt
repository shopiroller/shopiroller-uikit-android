package com.shopiroller

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.shopiroller.Shopiroller.userLoginStatus
import com.shopiroller.Shopiroller.listener
import com.shopiroller.network.ECommerceRequestHelper
import com.shopiroller.helpers.DispatchGroup
import com.shopiroller.views.legacy.ShopirollerEmptyView
import com.facebook.shimmer.ShimmerFrameLayout
import com.shopiroller.views.legacy.ShopirollerImageView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.shopiroller.helpers.EndlessRecyclerViewScrollListener
import com.shopiroller.adapter.MainPageAdapter
import com.shopiroller.helpers.ProgressViewHelper
import com.shopiroller.models.HeaderModel
import com.shopiroller.util.ECommerceUtil
import com.shopiroller.models.ProductDetailModel
import com.shopiroller.models.ECommerceResponse
import com.shopiroller.models.ShowcaseResponseModel
import com.shopiroller.util.ErrorUtils
import com.shopiroller.network.ECommerceRequestHelper.ECommerceCallBack
import com.shopiroller.models.ECommerceErrorResponse
import com.shopiroller.models.CategoriesWithOptionsModel
import com.shopiroller.models.ClientResponse
import com.shopiroller.models.SliderDataModel
import com.shopiroller.activities.ProductSearchActivity
import com.shopiroller.activities.ShoppingCartActivity
import com.shopiroller.constants.Constants
import com.shopiroller.models.ShoppingCartCountEvent
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList
import java.util.HashMap

class ShopirollerFragment : Fragment() {
    private val ecommerceRequestHelper = ECommerceRequestHelper()
    private val dispatchGroup = DispatchGroup()
    var mRecyclerView: RecyclerView? = null
    var emptyView: ShopirollerEmptyView? = null
    var mShimmerLayout: ShimmerFrameLayout? = null
    var whatsappImageView: ShopirollerImageView? = null
    var swipeRefreshLayout: SwipeRefreshLayout? = null
    var endlessRecyclerViewScrollListener: EndlessRecyclerViewScrollListener? = null
    var adapter: MainPageAdapter? = null
    var progressViewHelper: ProgressViewHelper? = null
    private val ITEM_PER_PAGE = 10
    private var gridLayoutManager: GridLayoutManager? = null
    private var whatsAppNumber: String? = null
    var mPage = 1
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.layout_ecommerce_list_view, container, false)
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout)
        SharedApplication.context = requireActivity().applicationContext
        if (!EventBus.getDefault().isRegistered(this)) EventBus.getDefault().register(this)
        mShimmerLayout = view.findViewById(R.id.shimmer)
        mRecyclerView = view.findViewById(R.id.list)
        emptyView = view.findViewById(R.id.empty_view)
        whatsappImageView = view.findViewById(R.id.whatsapp_image_view)
        whatsappImageView?.setOnClickListener(View.OnClickListener { view1: View? -> onWhatsAppImageViewClick() })
        progressViewHelper = ProgressViewHelper(requireActivity())
        showShimmer()

        //TODO
        //note sr shopiroller init
//        if (screenModel.androidChannelID != null && !screenModel.androidChannelID.equalsIgnoreCase(""))
//            new DataStoreHelper(getActivity()).setECommerceAliasKey(screenModel.androidChannelID);
//        else
//            new DataStoreHelper(getActivity()).setECommerceAliasKey("");
        loadUI()
        client
        sliders
        categories
        showcase
        products
        paymentSettings
        dispatchGroup.notify { dismissShimmer() }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private fun loadUI() {
        gridLayoutManager = getGridLayoutManager()
        endlessRecyclerViewScrollListener =
            object : EndlessRecyclerViewScrollListener(gridLayoutManager) {
                override fun onLoadMore(page: Int, totalItemsCount: Int) {
                    products
                }
            }
        loadRecyclerView(gridLayoutManager)
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout!!.setOnRefreshListener {
                progressViewHelper!!.show()
                endlessRecyclerViewScrollListener?.resetPrevItemCountForSwipeRefresh()
                mPage = 1
                adapter!!.deleteAll()
                adapter!!.notifyDataSetChanged()
                client
                sliders
                categories
                showcase
                products
            }
        }
    }

    private fun loadRecyclerView(gridLayoutManager: GridLayoutManager?) {
        val objectList: MutableList<Any> = ArrayList()
        objectList.add(HeaderModel())
        adapter = MainPageAdapter(activity, objectList, this)
        mRecyclerView!!.adapter = adapter
        mRecyclerView!!.layoutManager = gridLayoutManager
        mRecyclerView!!.addOnScrollListener(endlessRecyclerViewScrollListener!!)
    }

    override fun onResume() {
        super.onResume()
        ECommerceUtil().getBadge()
    }

    fun addToAdapter(newItems: MutableList<ProductDetailModel?>?) {
        var newItems = newItems
        if (activity == null) return
        newItems = checkProductListIsValid(newItems)
        if (adapter!!.itemCount == 0 && (newItems == null || newItems.size == 0)) {
            emptyView!!.visibility = View.VISIBLE
            mRecyclerView!!.visibility = View.GONE
            swipeRefreshLayout!!.visibility = View.GONE
        } else {
            if (adapter!!.itemCount == 0) {
                emptyView!!.visibility = View.GONE
                swipeRefreshLayout!!.visibility = View.VISIBLE
                mRecyclerView!!.visibility = View.VISIBLE
            }
            adapter!!.addItems(newItems)
        }
    }

    private fun checkProductListIsValid(newItems: MutableList<ProductDetailModel?>?): MutableList<ProductDetailModel?>? {
        if (newItems == null) return null
        var i = 0
        while (i < newItems.size) {
            if (newItems[i] == null || !newItems[i]!!.isValid) {
                newItems.removeAt(i)
                i--
            }
            i++
        }
        return newItems
    }

    private val showcase: Unit
        private get() {
            dispatchGroup.enter()
            val responseCall = ECommerceRequestHelper().apiService.showcase
            responseCall.enqueue(object :
                Callback<ECommerceResponse<List<ShowcaseResponseModel>?>?> {
                override fun onResponse(
                    call: Call<ECommerceResponse<List<ShowcaseResponseModel>?>?>,
                    response: Response<ECommerceResponse<List<ShowcaseResponseModel>?>?>
                ) {
                    dispatchGroup.leave()
                    if (response.body() != null && response.body()!!.data != null && response.body()!!.data!!.size > 0) {
                        val list: MutableList<ShowcaseResponseModel> = ArrayList()
                        for (i in response.body()!!.data!!.indices) {
                            if (response.body()!!.data!![i].products.size > 0) {
                                list.add(response.body()!!.data!![i])
                            }
                        }
                        adapter!!.addShowcase(list)
                    }
                }

                override fun onFailure(
                    call: Call<ECommerceResponse<List<ShowcaseResponseModel>?>?>,
                    t: Throwable
                ) {
                    dispatchGroup.leave()
                    ErrorUtils.showErrorToast(requireContext())
                }
            })
        }
    val products: Unit
        get() {
            val queryMap: MutableMap<String, String> = HashMap()
            queryMap["page"] = mPage.toString()
            queryMap["perPage"] = ITEM_PER_PAGE.toString()
            dispatchGroup.enter()
            val responseCall = ecommerceRequestHelper.apiService.getProducts(queryMap)
            ecommerceRequestHelper.enqueue(
                responseCall,
                object : ECommerceCallBack<List<ProductDetailModel?>?> {
                    override fun done() {
                        if (swipeRefreshLayout == null) return
                        swipeRefreshLayout!!.isRefreshing = false
                        if (activity != null && !activity!!.isFinishing && progressViewHelper != null && progressViewHelper!!.isShowing) progressViewHelper!!.dismiss()
                    }

                    override fun onSuccess(result: List<ProductDetailModel?>?) {
                        dispatchGroup.leave()
                        addToAdapter(result?.toMutableList())
                        mPage++
                    }

                    override fun onFailure(result: ECommerceErrorResponse?) {
                        ErrorUtils.showErrorToast(activity)
                        dispatchGroup.leave()
                        addToAdapter(null)
                    }

                    override fun onNetworkError(result: String?) {
                        addToAdapter(null)
                        dispatchGroup.leave()
                        ErrorUtils.showErrorToast(activity)
                    }

                })
        }
    private val categories: Unit
        private get() {
            val responseCall = ECommerceRequestHelper().apiService.categories
            dispatchGroup.enter()
            responseCall.enqueue(object :
                Callback<ECommerceResponse<CategoriesWithOptionsModel?>?> {
                override fun onResponse(
                    call: Call<ECommerceResponse<CategoriesWithOptionsModel?>?>,
                    response: Response<ECommerceResponse<CategoriesWithOptionsModel?>?>
                ) {
                    dispatchGroup.leave()
                    if (response.body() != null && response.body()!!.data != null && response.body()!!.data!!.categories!!.size > 0) {
                        adapter!!.addCategory(
                            response.body()!!.data!!.categories,
                            response.body()!!.data!!.mobileSettings
                        )
                    } else {
                        adapter!!.addCategory(null, null)
                    }
                }

                override fun onFailure(
                    call: Call<ECommerceResponse<CategoriesWithOptionsModel?>?>,
                    t: Throwable
                ) {
                    dispatchGroup.leave()
                    ErrorUtils.showErrorToast(requireContext())
                }
            })
        }

    fun onWhatsAppImageViewClick() {
        val url = "https://api.whatsapp.com/send?phone=$whatsAppNumber"
        val intent = Intent(Intent.ACTION_VIEW)
        try {
            val pm = requireContext().packageManager
            pm.getPackageInfo(Constants.WHATSAPP_PACKAGE_NAME, PackageManager.GET_ACTIVITIES)
            intent.data = Uri.parse(url)
        } catch (e: PackageManager.NameNotFoundException) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.data = Uri.parse("market://details?id=" + Constants.WHATSAPP_PACKAGE_NAME)
        }
        startActivity(intent)
    }

    private val client: Unit
        private get() {
            dispatchGroup.enter()
            val responseCall = ECommerceRequestHelper().apiService.client
            responseCall.enqueue(object : Callback<ECommerceResponse<List<ClientResponse>?>?> {
                override fun onResponse(
                    call: Call<ECommerceResponse<List<ClientResponse>?>?>,
                    response: Response<ECommerceResponse<List<ClientResponse>?>?>
                ) {
                    dispatchGroup.leave()
                    if (response.body() != null && response.body()!!.data != null) {
                        val clientList = response.body()!!.data
                        for (i in clientList!!.indices) {
                            if (clientList[i].type.equals(
                                    Constants.WHATSAPP,
                                    ignoreCase = true
                                ) && java.lang.Boolean.TRUE == clientList[i].properties!!.whatsAppMobileIsActive
                            ) {
                                whatsAppNumber =
                                    clientList[i].properties!!.countryPhoneCode + clientList[i].properties!!.accountNumber
                                whatsappImageView!!.visibility = View.VISIBLE
                            }
                        }
                    }
                }

                override fun onFailure(
                    call: Call<ECommerceResponse<List<ClientResponse>?>?>,
                    t: Throwable
                ) {
                    dispatchGroup.leave()
                    ErrorUtils.showErrorToast(requireContext())
                }
            })
        }
    private val sliders: Unit
        private get() {
            dispatchGroup.enter()
            val responseCall = ECommerceRequestHelper().apiService.sliders
            responseCall.enqueue(object : Callback<ECommerceResponse<List<SliderDataModel>?>?> {
                override fun onResponse(
                    call: Call<ECommerceResponse<List<SliderDataModel>?>?>,
                    response: Response<ECommerceResponse<List<SliderDataModel>?>?>
                ) {
                    dispatchGroup.leave()
                    if (response.body() != null && response.body()!!.data != null && response.body()!!.data!!.size != 0 && response.body()!!.data!![0].isActive
                        && response.body()!!.data!!.size > 0
                    ) {
                        adapter!!.addSlider(response.body()!!.data)
                    } else {
                        adapter!!.addSlider(null)
                    }
                }

                override fun onFailure(
                    call: Call<ECommerceResponse<List<SliderDataModel>?>?>,
                    t: Throwable
                ) {
                    dispatchGroup.leave()
                    ErrorUtils.showErrorToast(requireContext())
                }
            })
        }

    private fun showShimmer() {
        mRecyclerView!!.visibility = View.GONE
        mShimmerLayout!!.visibility = View.VISIBLE
        mShimmerLayout!!.startShimmer()
        if (swipeRefreshLayout != null) swipeRefreshLayout!!.isEnabled = false
    }

    private fun dismissShimmer() {
        mRecyclerView!!.visibility = View.VISIBLE
        mShimmerLayout!!.visibility = View.GONE
        mShimmerLayout!!.stopShimmer()
        if (swipeRefreshLayout != null) swipeRefreshLayout!!.isEnabled = true
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        setBadgeCount(ECommerceUtil.getBadgeCount())
        paymentSettings
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val itemId = item.itemId
        if (itemId == R.id.action_search) {
            startActivity(Intent(activity, ProductSearchActivity::class.java))
            return true
        } else if (itemId == R.id.action_shopping_cart) {
            if (!userLoginStatus) {
                if (listener != null) listener!!.loginNeeded()
            } else startActivity(Intent(activity, ShoppingCartActivity::class.java))
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setBadgeCount(count: Int) {
        listener!!.onBadgeCountChanged(count)
    }

    private val paymentSettings: Unit
        private get() {
            ECommerceUtil().getPaymentSettings(null)
        }

    private fun getGridLayoutManager(): GridLayoutManager {
        val gridLayoutManager: GridLayoutManager = object : GridLayoutManager(
            context, 2
        ) {
            override fun supportsPredictiveItemAnimations(): Boolean {
                return false
            }
        }
        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (position == 0) 2 else 1
            }
        }
        return gridLayoutManager
    }

    @Subscribe
    fun onPostShoppingCartCountEvent(event: ShoppingCartCountEvent) {
        setBadgeCount(event.shoppingCartItemCount)
    }

    override fun onDestroyView() {
        if (EventBus.getDefault().isRegistered(this)) EventBus.getDefault().unregister(this)
        super.onDestroyView()
    }
}