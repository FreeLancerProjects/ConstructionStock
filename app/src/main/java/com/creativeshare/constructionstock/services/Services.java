package com.creativeshare.constructionstock.services;


import com.creativeshare.constructionstock.models.CategoriesDataModel;
import com.creativeshare.constructionstock.models.PlaceGeocodeData;
import com.creativeshare.constructionstock.models.PlaceMapDetailsData;
import com.creativeshare.constructionstock.models.TermsModel;
import com.creativeshare.constructionstock.models.UserModel;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
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
    Call<ResponseBody> contact_us(@Field("username") String name,
                                  @Field("email") String email,
                                  @Field("message") String message
    );


    @GET("api/terms-and-conditions")
    Call<TermsModel> getTerms();

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


}
