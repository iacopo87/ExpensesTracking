package pazzaglia.it.expensestracking.network;

import android.content.Context;

import pazzaglia.it.expensestracking.models.RegistrationPOJO;
import retrofit2.Call;

/**
 * Created by IO on 02/10/2016.
 */

public class RegistrationCaller extends AbstractApiCaller<RegistrationPOJO> {
    private String name, email, password;
    private Context context;

    public RegistrationCaller(Context context, String name, String email, String password){
        this.context = context;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    @Override
    public Call<RegistrationPOJO> specificApiCall() {
        return getInterfaceService(context, false)
                .registrationPost(name, email, password);
    }

}
