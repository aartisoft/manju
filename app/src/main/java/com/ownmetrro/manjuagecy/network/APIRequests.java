package com.ownmetrro.manjuagecy.network;


import com.ownmetrro.manjuagecy.models.address_model.AddressData;
import com.ownmetrro.manjuagecy.models.address_model.Countries;
import com.ownmetrro.manjuagecy.models.address_model.Zones;
import com.ownmetrro.manjuagecy.models.banner_model.BannerData;
import com.ownmetrro.manjuagecy.models.category_model.CategoryData;
import com.ownmetrro.manjuagecy.models.contact_model.ContactUsData;
import com.ownmetrro.manjuagecy.models.device_model.AppSettingsData;
import com.ownmetrro.manjuagecy.models.filter_model.get_filters.FilterData;
import com.ownmetrro.manjuagecy.models.language_model.LanguageData;
import com.ownmetrro.manjuagecy.models.messages_model.Message_data;
import com.ownmetrro.manjuagecy.models.news_model.all_news.NewsData;
import com.ownmetrro.manjuagecy.models.news_model.news_categories.NewsCategoryData;
import com.ownmetrro.manjuagecy.models.pages_model.PagesData;
import com.ownmetrro.manjuagecy.models.product_model.GetAllProducts;
import com.ownmetrro.manjuagecy.models.coupons_model.CouponsData;
import com.ownmetrro.manjuagecy.models.payment_model.PaymentMethodsData;
import com.ownmetrro.manjuagecy.models.product_model.ViewCountData;
import com.ownmetrro.manjuagecy.models.shipping_model.PostTaxAndShippingData;
import com.ownmetrro.manjuagecy.models.order_model.OrderData;
import com.ownmetrro.manjuagecy.models.payment_model.GetBrainTreeToken;
import com.ownmetrro.manjuagecy.models.order_model.PostOrder;
import com.ownmetrro.manjuagecy.models.product_model.ProductData;
import com.ownmetrro.manjuagecy.models.search_model.SearchData;
import com.ownmetrro.manjuagecy.models.shipping_model.ShippingRateData;
import com.ownmetrro.manjuagecy.models.user_model.UserData;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;


/**
 * APIRequests contains all the Network Request Methods with relevant API Endpoints
 **/

public interface APIRequests {


    //******************** User Data ********************//

    @FormUrlEncoded
    @POST("processregistration")
    Call<UserData> processRegistration(@Field("customers_firstname") String customers_firstname,
                                       @Field("customers_lastname") String customers_lastname,
                                       @Field("email") String customers_email_address,
                                       @Field("password") String customers_password,
                                       @Field("customers_telephone") String customers_telephone,
                                       @Field("customers_picture") String customers_picture);

    @FormUrlEncoded
    @POST("processlogin")
    Call<UserData> processLogin(@Field("email") String customers_email_address,
                                @Field("password") String customers_password);

    @FormUrlEncoded
    @POST("processotp")
    Call<UserData> processOtp(@Field("email") String customers_email_address);

    @FormUrlEncoded
    @POST("facebookregistration")
    Call<UserData> facebookRegistration(@Field("access_token") String access_token);

    @FormUrlEncoded
    @POST("googleregistration")
    Call<UserData> googleRegistration(@Field("idToken") String idToken,
                                      @Field("userId") String userId,
                                      @Field("givenName") String givenName,
                                      @Field("familyName") String familyName,
                                      @Field("email") String email,
                                      @Field("imageUrl") String imageUrl);

    @FormUrlEncoded
    @POST("processforgotpassword")
    Call<UserData> processForgotPassword(@Field("email") String customers_email_address);

    @FormUrlEncoded
    @POST("updatecustomerinfo")
    Call<UserData> updateCustomerInfo(@Field("customers_id") String customers_id,
                                      @Field("customers_firstname") String customers_firstname,
                                      @Field("customers_lastname") String customers_lastname,
                                      @Field("customers_telephone") String customers_telephone,
                                      @Field("customers_dob") String customers_dob,
                                      @Field("customers_picture") String customers_picture,
                                      @Field("customers_old_picture") String customers_old_picture,
                                      @Field("customers_password") String customers_password);
    
    
    
    //******************** Address Data ********************//
    
    @POST("getcountries")
    Call<Countries> getCountries();

    @FormUrlEncoded
    @POST("getzones")
    Call<Zones> getZones(@Field("zone_country_id") String zone_country_id);

    @FormUrlEncoded
    @POST("getalladdress")
    Call<AddressData> getAllAddress(@Field("customers_id") String customers_id);

    @FormUrlEncoded
    @POST("addshippingaddress")
    Call<AddressData> addUserAddress(@Field("customers_id") String customers_id,
                                     @Field("entry_firstname") String entry_firstname,
                                     @Field("entry_lastname") String entry_lastname,
                                     @Field("entry_street_address") String entry_street_address,
                                     @Field("entry_postcode") String entry_postcode,
                                     @Field("entry_city") String entry_city,
                                     @Field("entry_country_id") String entry_country_id,
                                     @Field("entry_zone_id") String entry_zone_id,
                                     @Field("customers_default_address_id") String customers_default_address_id);

    @FormUrlEncoded
    @POST("updateshippingaddress")
    Call<AddressData> updateUserAddress(@Field("customers_id") String customers_id,
                                        @Field("address_id") String address_id,
                                        @Field("entry_firstname") String entry_firstname,
                                        @Field("entry_lastname") String entry_lastname,
                                        @Field("entry_street_address") String entry_street_address,
                                        @Field("entry_postcode") String entry_postcode,
                                        @Field("entry_city") String entry_city,
                                        @Field("entry_country_id") String entry_country_id,
                                        @Field("entry_zone_id") String entry_zone_id,
                                        @Field("customers_default_address_id") String customers_default_address_id);

    @FormUrlEncoded
    @POST("updatedefaultaddress")
    Call<AddressData> updateDefaultAddress(@Field("customers_id") String customers_id,
                                           @Field("address_book_id") String address_book_id);

    @FormUrlEncoded
    @POST("deleteshippingaddress")
    Call<AddressData> deleteUserAddress(@Field("customers_id") String customers_id,
                                        @Field("address_book_id") String address_book_id);

    

    //******************** Category Data ********************//

    @FormUrlEncoded
    @POST("allcategories")
    Call<CategoryData> getAllCategories(@Field("language_id") int language_id);
    


    //******************** Product Data ********************//

    @POST("getallproducts")
    Call<ProductData> getAllProducts(@Body GetAllProducts getAllProducts);


    @FormUrlEncoded
    @POST("likeproduct")
    Call<ProductData> likeProduct(@Field("liked_products_id") int liked_products_id,
                                  @Field("liked_customers_id") String liked_customers_id);

    @FormUrlEncoded
    @POST("unlikeproduct")
    Call<ProductData> unlikeProduct(@Field("liked_products_id") int liked_products_id,
                                    @Field("liked_customers_id") String liked_customers_id);
    

    @FormUrlEncoded
    @POST("getfilters")
    Call<FilterData> getFilters(@Field("categories_id") int categories_id,
                                @Field("language_id") int language_id);


    @FormUrlEncoded
    @POST("getsearchdata")
    Call<SearchData> getSearchData(@Field("searchValue") String searchValue,
                                   @Field("language_id") int language_id);


    
    //******************** News Data ********************//

    @FormUrlEncoded
    @POST("getallnews")
    Call<NewsData> getAllNews(@Field("language_id") int language_id,
                              @Field("page_number") int page_number,
                              @Field("is_feature") int is_feature,
                              @Field("categories_id") String categories_id);

    @FormUrlEncoded
    @POST("allnewscategories")
    Call<NewsCategoryData> allNewsCategories(@Field("language_id") int language_id,
                                             @Field("page_number") int page_number);



    //******************** Order Data ********************//
    
    @POST("addtoorder")
    Call<OrderData> addToOrder(@Body PostOrder postOrder);

    @FormUrlEncoded
    @POST("getorders")
    Call<OrderData> getOrders(@Field("customers_id") String customers_id,
                              @Field("language_id") int language_id);


    @FormUrlEncoded
    @POST("getcoupon")
    Call<CouponsData> getCouponInfo(@Field("code") String code);
    
    
    @FormUrlEncoded
    @POST("getpaymentmethods")
    Call<PaymentMethodsData> getPaymentMethods(@Field("language_id") int language_id);

    @GET("generatebraintreetoken")
    Call<GetBrainTreeToken> generateBraintreeToken();



    //******************** Banner Data ********************//

    @GET("getbanners")
    Call<BannerData> getBanners();


    
    //******************** Tax & Shipping Data ********************//

    @POST("getrate")
    Call<ShippingRateData> getShippingMethodsAndTax(
            @Body PostTaxAndShippingData postTaxAndShippingData);



    //******************** Contact Us Data ********************//

    @FormUrlEncoded
    @POST("contactus")
    Call<ContactUsData> contactUs(@Field("name") String name,
                                  @Field("email") String email,
                                  @Field("message") String message);


    
    //******************** Languages Data ********************//
    
    @GET("getlanguages")
    Call<LanguageData> getLanguages();


    
    //******************** App Settings Data ********************//

    @GET("sitesetting")
    Call<AppSettingsData> getAppSetting();
    

    //******************** Static Pages Data ********************//
    
    @FormUrlEncoded
    @POST("getallpages")
    Call<PagesData> getStaticPages(@Field("language_id") int language_id);
    
    
    
    //******************** Notifications Data ********************//
    
    @FormUrlEncoded
    @POST("registerdevices")
    Call<UserData> registerDeviceToFCM(@Field("device_id") String device_id,
                                       @Field("device_type") String device_type,
                                       @Field("ram") String ram,
                                       @Field("processor") String processor,
                                       @Field("device_os") String device_os,
                                       @Field("location") String location,
                                       @Field("device_model") String device_model,
                                       @Field("manufacturer") String manufacturer,
                                       @Field("customers_id") String customers_id);


    @FormUrlEncoded
    @POST("notify_me")
    Call<ContactUsData> notify_me(@Field("is_notify") String is_notify,
                                  @Field("device_id") String device_id);

    //********************* View Data **************************//

    @FormUrlEncoded
    @POST("viewproduct")
    Call<ViewCountData> viewproductcount (@Field("products_id") String products_id,
                                        @Field("customers_id") String coustomer_id);


    @FormUrlEncoded
    @POST("productenquiry")
    Call<ViewCountData> viewmessages (@Field("products_id") String products_id,
                         @Field("customers_id") String customers_id,
                        @Field("message") String messages);


    @FormUrlEncoded
    @POST("getenquiries")
    Call<Message_data> getmyMessages (@Field("customers_id") String customers_id,
                                       @Field("language_id") int language_id);



    
}

