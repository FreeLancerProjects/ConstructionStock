package com.creativeshare.constructionstock.services;


import com.creativeshare.constructionstock.models.AboutApp;
import com.creativeshare.constructionstock.models.CategoriesDataModel;
import com.creativeshare.constructionstock.models.ItemCartUploadModel;
import com.creativeshare.constructionstock.models.NotificationDataModel;
import com.creativeshare.constructionstock.models.OrderDataModel;
import com.creativeshare.constructionstock.models.OrderIdModel;
import com.creativeshare.constructionstock.models.PlaceGeocodeData;
import com.creativeshare.constructionstock.models.PlaceMapDetailsData;
import com.creativeshare.constructionstock.models.UserModel;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;


public interface Services {

    @FormUrlEncoded
    @POST("api/register")
    Call<UserModel> sign_up(
            @Field("username") String name,
            @Field("password") String password,
            @Field("phone_code") String phone_code,
            @Field("phone") String phone,
            @Field("software_type") int software_type
    );


    @FormUrlEncoded
    @POST("api/login")
    Call<UserModel> sign_in(
            @Field("phone") String phone,
            @Field("phone_code") String phone_code,
            @Field("password") String password

    );


    @GET("api/categories")
    Call<CategoriesDataModel> get_categories();


    @FormUrlEncoded
    @POST("api/contact-us")
    Call<ResponseBody> contact_us(@Field("name") String name,
                                  @Field("email") String email,
                                  @Field("subject") String subject,
                                  @Field("message") String message
    );


    @GET("api/about-us")
    Call<AboutApp> aboutApp();

    @GET("place/findplacefromtext/json")
    Call<PlaceMapDetailsData> searchOnMap(@Query(value = "inputtype") String inputtype,
                                          @Query(value = "input") String input,
                                          @Query(value = "fields") String fields,
                                          @Query(value = "language") String language,
                                          @Query(value = "key") String key
    );

    @GET("geocode/json")
    Call<PlaceGeocodeData> getGeoData(@Query(value = "latlng") String latlng,
                                      @Query(value = "language") String language,
                                      @Query(value = "key") String key);

    @POST("api/order/add")
    Call<OrderIdModel> sendOrder(@Body ItemCartUploadModel itemCartUploadModel);


    @FormUrlEncoded
    @POST("api/my-notification")
    Call<NotificationDataModel> getNotification(@Field("user_id") int user_id,
                                                @Field("page") int page
    );

    @FormUrlEncoded
    @POST("api/my-current-order")
    Call<OrderDataModel> getCurrentOrders(@Field("user_id") int user_id,
                                          @Field("page") int page
    );

    @FormUrlEncoded
    @POST("api/my-finished-order")
    Call<OrderDataModel> getPreviousOrders(@Field("user_id") int user_id,
                                           @Field("page") int page
    );

    @FormUrlEncoded
    @POST("api/offer/accept")
    Call<ResponseBody> acceptOffer(@Field("notification_id") int notification_id
    );

    @FormUrlEncoded
    @POST("api/offer/refuse")
    Call<ResponseBody> refuseOffer(@Field("notification_id") int notification_id
    );

    @FormUrlEncoded
    @POST("api/visit")
    Call<ResponseBody> updateVisit(@Field("date") String date,
                                   @Field("software_type") int software_type

    );

    @FormUrlEncoded
    @POST("api/phone-tokens")
    Call<ResponseBody> updateToken(@Field("user_id") int user_id,
                                   @Field("phone_token") String phone_token

    );


    @Multipart
    @POST("api/profile/edit")
    Call<UserModel> updateImage(@Part("user_id") RequestBody user_id,
                                @Part("username") RequestBody username,
                                @Part("phone") RequestBody phone,
                                @Part("phone_code") RequestBody phone_code,
                                @Part MultipartBody.Part image


    );

    @FormUrlEncoded
    @POST("api/profile/edit")
    Call<UserModel> updateUserData(@Field("user_id") int user_id,
                                   @Field("username") String username,
                                   @Field("phone") String phone,
                                   @Field("phone_code") String phone_code


    );

    @FormUrlEncoded
    @POST("api/order/end")
    Call<ResponseBody> endOrder(@Field("order_id") int order_id,
                                @Field("rating") int rating
    );


}
