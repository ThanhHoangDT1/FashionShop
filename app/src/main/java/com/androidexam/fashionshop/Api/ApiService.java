package com.androidexam.fashionshop.Api;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.androidexam.fashionshop.Fragment.LogIn.test;
import com.androidexam.fashionshop.Model.AccessTokenResponse;
import com.androidexam.fashionshop.Model.AddressItem;
import com.androidexam.fashionshop.Model.CartItem;
import com.androidexam.fashionshop.Model.Category;
import com.androidexam.fashionshop.Model.ChangePassword;
import com.androidexam.fashionshop.Model.NewPassword;
import com.androidexam.fashionshop.Model.Order;
import com.androidexam.fashionshop.Model.OrderItem;
import com.androidexam.fashionshop.Model.Product;
import com.androidexam.fashionshop.Model.ProductResponse;
import com.androidexam.fashionshop.Model.Product_Detail;
import com.androidexam.fashionshop.Model.ResponeVoucher;
import com.androidexam.fashionshop.Model.Responeurl;
import com.androidexam.fashionshop.Model.ResponseForgotPW;
import com.androidexam.fashionshop.Model.ResponseGetOrderItemDelivered;
import com.androidexam.fashionshop.Model.ResponseGetOrdered;
import com.androidexam.fashionshop.Model.ResponseOrder;
import com.androidexam.fashionshop.Model.ShippingResponse;
import com.androidexam.fashionshop.Model.User;
import com.androidexam.fashionshop.Fragment.Individual.MyApp;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.Interceptor;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import com.androidexam.fashionshop.Fragment.LogIn.test;
import java.io.IOException;
import java.util.List;

public interface ApiService {

   String BASE_URL = "http://192.168.49.39:8080";

    //String BASE_URL = "http://192.168.4.21:8080";

    Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd HH:mm:ss")
            .create();
    OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

    static void addAccessTokenToHeader(Request.Builder builder) {
        SharedPreferences preferencesac = MyApp.getAppContext().getSharedPreferences("ACCESS", Context.MODE_PRIVATE);
        String accessToken = preferencesac.getString("accessToken", null);
        if (accessToken != null) {
            builder.addHeader("Authorization", "Bearer " + accessToken);
        }
    }

    OkHttpClient clientWithToken = httpClient.addInterceptor(new Interceptor() {
        @Override
        public okhttp3.Response intercept(Chain chain) throws IOException {
            Request.Builder builder = chain.request().newBuilder();
            addAccessTokenToHeader(builder);
            return chain.proceed(builder.build());
        }
    }).build();

    Retrofit retrofitWithToken = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(clientWithToken)
            .build();

    ApiService productServiceWithToken = retrofitWithToken.create(ApiService.class);


    //  ApiService productServiceWithoutToken = retrofitWithoutToken.create(ApiService.class);

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build();

    ApiService productService = retrofit.create(ApiService.class);





    // Định nghĩa yêu cầu API để lấy danh sách sản phẩm
    @GET("/products")
    Call<ProductResponse> getAllProducts(@Query("page") int page, @Query("items_per_page") int items_per_page);
    @GET("/products/all")
    Call<ProductResponse> getProductsall();
    @GET("/api/product/product/searchAll")
    Call<ProductResponse> getProducts(@Query("page") int page, @Query("items_per_page") int items_per_page,@Query("keyword") String keyword);

    @GET("/api/category")
    Call<List<Category>> getCategory();


    @GET("/api/products/recommend-system/{userId}")
    Call<ProductResponse> getRecommend(@Path("userId") int userId);



    
    @GET("/api/product/product/searchAll")
    Call<ProductResponse> getProducts(@Query("page") int page, @Query("items_per_page") int items_per_page, @Query("keyword") String keyword, @Query("minprice") String minprice, @Query("maxprice") String maxprice, @Query("category") String category);


    @GET("/products/product_detail/{id}")
    Call<Product_Detail> Product_Detail(@Path("id") int id);


    @POST("/users/register")
    Call<AccessTokenResponse> registerUser(@Body User user);

    //@POST("/api/auth/authenticate")
    @POST("/users/login")
    Call<AccessTokenResponse> loginUser(@Body User user);



    @POST("/api/carts/user/{userId}")
    Call<ResponseBody> addProduct(@Path("userId") int userId, @Body CartItem cart);
    @PUT("/api/carts/user/{userId}/{IdCartItem}")
    Call<CartItem> editProduct(@Path("userId") int userId,@Path("IdCartItem") int cartItemId, @Body CartItem cart);

    @GET("/api/carts/user/{userId}")
    Call<List<CartItem>> getCart(@Path("userId") int userId);

    @HTTP(method = "DELETE", path = "/api/carts/user/{userId}", hasBody = true)
    Call<Void> delete(@Path("userId") int userId, @Body List<Integer> idItem);



    @GET("/api/users/{id}")
    Call<User> getUser(@Path("id") int userId);

    @PUT("/api/users/{id}")
    Call<User> editUser(@Path("id") int userId, @Body User user);

    @Multipart
    @PUT("/api/users/avatar/{id}")
    Call<ResponseBody> uploadImage(
            @Path("id") int userId,
            @Part MultipartBody.Part avatar

    );



    //địa chỉ
    @GET("/api/users/{userId}/addresses")
    Call<List<AddressItem>> getAddress(@Path("userId") int userId);
    @POST("/api/users/{userId}/addresses")
    Call<AddressItem> addAddress(@Path("userId") int userId, @Body AddressItem addressItem);
    @PUT("/api/users/{userId}/addresses/{id}")
    Call<AddressItem> editAddress(@Path("userId") int userId,@Path("id") int id,@Body AddressItem addressItem);

    @DELETE("/api/users/{userId}/addresses/{id}")
    Call<Void> deleteAddress(@Path("userId") int userId,@Path("id") int id);
    @HTTP(method = "DELETE", path = "/api/users/{userId}/addresses/{id}", hasBody = true)
    Call<Void> deleteadd(@Path("userId") int userId,@Path("id") int id);


    //Quên pass
    @POST("/api/auth/forgot-password")
    Call<ResponseBody> sendUsername(@Query("username") String username);
    @POST("/api/auth/verify-otp")
    Call<ResponseForgotPW> verifyOTP(@Query("username") String username, @Query("otp") String otp);

   @PUT("/api/auth/reset-password")
   Call<ResponseBody>resetPassword(@Body NewPassword newPassword);



 // Thay đổi pass
   @POST("/api/users/{id}/change-password")
   Call<ResponseBody> changePassword(@Path("id") int id, @Body ChangePassword changePassword);




 //
    @GET("/api/vouchers")
    Call<ResponeVoucher> getVoucher(@Query("voucherType") String type);


    @GET("/api/orders/fee-ship")
    Call<ShippingResponse> getFeeShip(@Query("districtId") long districtId, @Query("wardCode") String wardCode);



    @POST("/api/orders")
    Call<Responeurl> postOrder(@Body Order order);


    @GET("/api/orders/users/{userId}")
    Call<ResponseGetOrdered> getOrder(@Path("userId") int userId);
    @GET("/api/orders/users/{userId}")
    Call<ResponseGetOrdered> getOrder(@Path("userId") int userId, @Query("orderStatus") String orderStatus);
    @GET("/api/orders/{orderId}")
    Call<ResponseOrder> getOrderDetail(@Path("orderId") String orderId);
    @PUT("/api/orders/{orderId}")
    Call<ResponseOrder> cancelOrder(@Path("orderId") String orderId,@Query("orderStatus") String orderStatus);

    @GET("/api/orders/users/{userId}/order-items")
    Call<ResponseGetOrderItemDelivered> getOrderItemDelivered(@Path("userId") int userId, @Query("isRate") boolean isRate);


    @POST("/api/comment")
    Call<ResponseBody> sentComment(@Query("rate") int rate, @Query("orderItemId") int orderItemId,@Query("productId") int productId,@Query("userId") int userId,@Query("content") String content);



    @POST("/api/auth/verify-gg")
    Call<AccessTokenResponse> gg(@Query("gmail") String gmail,@Query("fullName") String fullName, @Query("urlImage") String urlImage);

}
