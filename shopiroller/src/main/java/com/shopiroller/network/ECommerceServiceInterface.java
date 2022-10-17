package com.shopiroller.network;

import com.shopiroller.models.AddProductModel;
import com.shopiroller.models.CategoriesWithOptionsModel;
import com.shopiroller.models.CategoryResponseModel;
import com.shopiroller.models.ClientResponse;
import com.shopiroller.models.CompleteOrder;
import com.shopiroller.models.ECommerceResponse;
import com.shopiroller.models.FilterOptionsResponse;
import com.shopiroller.models.MakeOrder;
import com.shopiroller.models.Order;
import com.shopiroller.models.OrderDetailModel;
import com.shopiroller.models.OrderResponseInner;
import com.shopiroller.models.PaymentSettings;
import com.shopiroller.models.ProductDetailModel;
import com.shopiroller.models.ShoppingCartItemsResponse;
import com.shopiroller.models.ShoppingCartResponse;
import com.shopiroller.models.ShowcaseResponseModel;
import com.shopiroller.models.SliderDataModel;
import com.shopiroller.models.StripeOrderStatusModel;
import com.shopiroller.models.UpdateCartItemQuantity;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * Created by ealtaca on 17.01.2017.
 */

public interface ECommerceServiceInterface {

    @GET("v2/products/advanced-filtered")
    Call<ECommerceResponse<List<ProductDetailModel>>> getProductsWithAdvancedFiltered(@QueryMap Map<String, Object> options);

    @GET("v2/products")
    Call<ECommerceResponse<List<ProductDetailModel>>> getProducts(@QueryMap Map<String, String> options);

    @GET("v2/products/{path}")
    Call<ECommerceResponse<ProductDetailModel>> getProduct(@Path("path") String productId);

    @GET("v2/paymentSettings")
    Call<ECommerceResponse<PaymentSettings>> getPaymentSettings();

    @POST("v2/orders/user/{userId}")
    Call<ECommerceResponse<OrderResponseInner>> makeOrder(@Path("userId") String userId, @Body MakeOrder makeOrder);

    @POST("v2/orders/complete")
    Call<ECommerceResponse<OrderResponseInner>> tryAgain(@Body CompleteOrder completeOrder);

    @POST("v2/orders/completePayment")
    Call<ECommerceResponse> stripeCompletePayment(@Body StripeOrderStatusModel stripeOrderStatusModel);

    @POST("v2/orders/failurePayment")
    Call<ECommerceResponse> stripeFailurePayment(@Body StripeOrderStatusModel stripeOrderStatusModel);

    @GET("v2/orders?perPage=50&orderBy=OrderByDescending")
    Call<ECommerceResponse<List<Order>>> getOrderList(@Query("userId") String userId);

    @GET("v2/orders/{path}")
    Call<ECommerceResponse<OrderDetailModel>> getOrder(@Path("path") String orderId);

    @GET("v2/users/{userId}/shopping-cart")
    Call<ECommerceResponse<ShoppingCartResponse>> getShoppingCart(@Path("userId") String userId);

    @GET("v2/users/{userId}/shopping-cart/validate")
    Call<ECommerceResponse<ShoppingCartResponse>> validateShoppingCart(@Path("userId") String userId);

    @DELETE("v2/users/{userId}/shopping-cart/items/{cartItemId}")
    Call<ECommerceResponse> removeItemFromShoppingCart(@Path("userId") String userId, @Path("cartItemId") String cartItemId);

    @PUT("v2/users/{userId}/shopping-cart/items/{cartItemId}/quantity")
    Call<ECommerceResponse> updateItemQuantity(@Path("userId") String userId, @Path("cartItemId") String cartItemId,@Body UpdateCartItemQuantity quantity);

    @POST("v2/users/{userId}/shopping-cart/items")
    Call<ECommerceResponse<ShoppingCartItemsResponse>> addProductToShoppingCart(@Path("userId") String userId , @Body AddProductModel quantity);

    @DELETE("v2/users/{userId}/shopping-cart/items")
    Call<ECommerceResponse> clearShoppingCart(@Path("userId") String userId);

    @GET("v2/users/{userId}/shopping-cart/count")
    Call<ECommerceResponse<Integer>> getShoppingCartCount(@Path("userId") String userId);

    @POST("v2/orders/{orderId}/failure")
    Call<ECommerceResponse> failurePayment(@Path("orderId") String orderId);

    @GET("v2/sliders")
    Call<ECommerceResponse<List<SliderDataModel>>> getSliders();

    @GET("v2/categories/getWithOptions")
    Call<ECommerceResponse<CategoriesWithOptionsModel>> getCategories();

    @GET("v2/categories/{categoryId}")
    Call<ECommerceResponse<CategoryResponseModel>> getCategories(@Path("categoryId") String categoryId);

    @GET("v2/showcases")
    Call<ECommerceResponse<List<ShowcaseResponseModel>>> getShowcase();

    @GET("v2/filterOptions")
    Call<ECommerceResponse<FilterOptionsResponse>>  getFilterOptions(
            @Query("searchText") String searchText,
            @Query("categoryId") String categoryId,
            @Query("showcaseId") String showcaseId
    );


    @POST("v2/users/{userId}/shopping-cart/coupons/{name}")
    Call<ECommerceResponse<ShoppingCartResponse>> insertCoupon(@Path("userId") String userId, @Path("name") String couponName);

    @DELETE("v2/users/{userId}/shopping-cart/coupons/{name}")
    Call<ECommerceResponse<ShoppingCartResponse>> deleteCoupon(@Path("userId") String userId, @Path("name") String couponName);

    @GET("v2/client")
    Call<ECommerceResponse<List<ClientResponse>>> getClient();

}