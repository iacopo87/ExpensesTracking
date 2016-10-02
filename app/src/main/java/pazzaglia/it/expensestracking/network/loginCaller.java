package pazzaglia.it.expensestracking.network;

import android.content.Context;

import pazzaglia.it.expensestracking.models.LoginPOJO;
import retrofit2.Call;

/**
 * Created by IO on 02/10/2016.
 */

public class LoginCaller extends AbstractApiCaller<LoginPOJO> {
    private String email, password;
    private Context context;

    public LoginCaller(Context context, String email, String password){
        this.context = context;
        this.email = email;
        this.password = password;
    }

    @Override
    public Call<LoginPOJO> specificApiCall() {
        return getInterfaceService(context, false)
                .loginPost(email, password);
    }

}
