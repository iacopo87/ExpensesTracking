package pazzaglia.it.expensestracking.network;

import android.app.ProgressDialog;
import android.content.Context;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import pazzaglia.it.expensestracking.shared.Common;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Created by IO on 18/07/2016.
 */

public abstract class AbstractApiCaller<T> {
    private static final String BASE_URL = "http://iacapi.tigrimigri.com/api/v2/";

    //define callback interface
     public interface MyCallbackInterface<S> {

        void onDownloadFinishedOK(S result);
        void onDownloadFinishedKO(S result);
        void doApiCallOnFailure();
    }

    protected ApiInterface getInterfaceService(Context context, boolean authenticate) {

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create());

        if (authenticate){
            builder.client(getClientWithAuth(Common.getApiKey(context)));
        }

        Retrofit retrofit = builder.build();
        final ApiInterface mInterfaceService = retrofit.create(ApiInterface.class);
        return mInterfaceService;
    }

    private OkHttpClient getClientWithAuth(final String apiKey){
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public okhttp3.Response intercept(Interceptor.Chain chain) throws IOException {
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

    public void doApiCall(Context context, String loadingMessage, final MyCallbackInterface<T> callback){
        //Show the spinner
        final ProgressDialog progressDialog = Common.showProgressDialog(context, loadingMessage);
        Call<T> mService = specificApiCall();
        mService.enqueue(new Callback<T>() {
            @Override
            public void onResponse(Call<T> call, Response<T> response) {
                T mObject = response.body();
                boolean responseHasError = response.errorBody()!= null && mObject==null;
                if(!responseHasError){
                    callback.onDownloadFinishedOK(mObject);
                }else {
                    callback.onDownloadFinishedKO(mObject);
                }
                progressDialog.dismiss();
            }
            @Override
            public void onFailure(Call<T> call, Throwable t) {
                call.cancel();
                callback.doApiCallOnFailure();
            }
        });
    }


    public abstract Call<T> specificApiCall();

}
