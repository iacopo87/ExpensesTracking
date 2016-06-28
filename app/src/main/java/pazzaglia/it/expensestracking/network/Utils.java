package pazzaglia.it.expensestracking.network;

import pazzaglia.it.expensestracking.network.ApiInterface;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by IO on 28/06/2016.
 */

public  class Utils {

    private static final String BASE_URL = "http://iacapi.tigrimigri.com/api/v1/";
    public static ApiInterface getInterfaceService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final ApiInterface mInterfaceService = retrofit.create(ApiInterface.class);
        return mInterfaceService;
    }

}
