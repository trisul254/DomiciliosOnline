package com.example.hector.domiciliosonline.Remote;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

/**
 * Created by Hector on 7/02/2018.
 */

public interface IGoogleApi {
    @GET
    Call<String> getPath(@Url String url);

}
