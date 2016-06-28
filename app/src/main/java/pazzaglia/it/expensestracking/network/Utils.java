package pazzaglia.it.expensestracking.network;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import pazzaglia.it.expensestracking.network.ApiInterface;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by IO on 28/06/2016.
 */

public  class Utils {

    private static final String BASE_URL = "http://iacapi.tigrimigri.com/api/v1/";
    public static ApiInterface getInterfaceService(boolean authenticate, String apiKey) {

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create());

        if (authenticate){
            builder.client(getClientWithAuth(apiKey));
        }

        Retrofit retrofit = builder.build();
        final ApiInterface mInterfaceService = retrofit.create(ApiInterface.class);
        return mInterfaceService;
    }

    private static OkHttpClient  getClientWithAuth(final String apiKey){
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Interceptor.Chain chain) throws IOException {
                Request original = chain.request();

                // Request customization: add request headers
                Request.Builder requestBuilder = original.newBuilder()
                        .header("auth", apiKey); // <-- this is the important line

                Request request = requestBuilder.build();
                return chain.proceed(request);
            }
        }).build();
        return client;
    }

}
