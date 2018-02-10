package com.example.hector.domiciliosonline.Common;

import com.example.hector.domiciliosonline.Remote.IGoogleApi;
import com.example.hector.domiciliosonline.Remote.RetrofitClient;

/**
 * Created by Hector on 7/02/2018.
 */

public class Common {

    public static final String baseURL = "https://maps.googleapis.com";
    public static IGoogleApi getGoogleApi()
    {
        return RetrofitClient.getClient(baseURL).create(IGoogleApi.class);
    }
}
