package com.example.redoy.lynk.application;


import com.example.redoy.lynk.model.AllDealsResponse;
import com.example.redoy.lynk.model.BusinessRegistration;
import com.example.redoy.lynk.model.BusinessRegistrationResponse;
import com.example.redoy.lynk.model.CategoryResponse;
import com.example.redoy.lynk.model.DealsResponse;
import com.example.redoy.lynk.model.Example;
import com.example.redoy.lynk.model.LogIn;
import com.example.redoy.lynk.model.LogInResponse;
import com.example.redoy.lynk.model.PhotosResponse;
import com.example.redoy.lynk.model.ProfileResponse;
import com.example.redoy.lynk.model.ProfileReviewResponse;
import com.example.redoy.lynk.model.ReviewBody;
import com.example.redoy.lynk.model.SearchResponse;
import com.example.redoy.lynk.model.SignUp;
import com.example.redoy.lynk.model.SignUpResponse;
import com.example.redoy.lynk.model.SubmitReviewResponse;
import com.example.redoy.lynk.model.ThanaResponse;

import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.Query;

public interface RetrofitLynk {

    @GET("api/place/nearbysearch/json?sensor=true&key=AIzaSyDN7RJFmImYAca96elyZlE5s_fhX-MMuhk")
    Call<Example> getNearbyPlaces(@Query("type") String type, @Query("location") String location, @Query("radius") int radius);

    @POST("auth/login")
    Call<LogInResponse> getLogInOutput(@Body LogIn logIn);

    @POST("auth/signup")
    Call<SignUpResponse> getSignUpOutput(@Body SignUp signUp);

    @POST("directory/search/by/title/category?")
    Call<SearchResponse> getSearchOutput(@Query("title") String title);

    @POST("directory/details/by/id?")
    Call<ProfileResponse> getProfileOutput(@Query("id") String id);

    @POST("directory/deal/by/dirid/{id}")
    Call<DealsResponse> getDealsOutput(@Path("id") String id);

    @POST("directory/photos/by/dirid/{id}")
    Call<PhotosResponse> getPhotosOutput(@Path("id") String id);

    @GET("directory/reviews/by/dirid/{id}")
    Call<ProfileReviewResponse> getProfileReviews(@Path("id") String id);

    @POST("auth/directory/review/post/by/dirid/{id}?")
    Call<SubmitReviewResponse> submitReview(@Path("id") String id, @Query("token") String token, @Body ReviewBody reviewBody);

    @POST("auth/add/new/business/post?")
    Call<BusinessRegistrationResponse> submitBusinessRegistration(@Query("token") String token, @Body BusinessRegistration businessRegistration);

    @GET("thana/ajax?")
    Call<ThanaResponse> getThanaResponse();

    @GET("cats?")
    Call<CategoryResponse> getCategoryResponse();

    @GET("directory/deal/search/?q=")
    Call<AllDealsResponse> getAllNotifications();
}
