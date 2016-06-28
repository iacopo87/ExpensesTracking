package pazzaglia.it.expensestracking.network;

/**
 * Created by C305445 on 6/27/2016.
 */
import pazzaglia.it.expensestracking.models.ExpensesCreatePOJO;
import pazzaglia.it.expensestracking.models.ExpensesGetPOJO;
import pazzaglia.it.expensestracking.models.ExpensesListPOJO;
import pazzaglia.it.expensestracking.models.LoginPOJO;
import pazzaglia.it.expensestracking.models.RegistrationPOJO;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiInterface {

    @FormUrlEncoded
    @POST("register")
    Call<RegistrationPOJO> registrationPost(@Field("name") String name, @Field("email") String email, @Field("password") String password);

    @FormUrlEncoded
    @POST("login")
    Call<LoginPOJO> loginPost(@Field("email") String email, @Field("password") String password);

    @GET("expenses")
    Call<ExpensesListPOJO> expensesGet();

    @FormUrlEncoded
    @POST("expenses")
    Call<ExpensesCreatePOJO> expensesPost(@Field("description") String description, @Field("date") String date, @Field("amount") double amount, @Field("category") int category);

    @GET("expenses/{id}")
    Call<ExpensesGetPOJO> expensesGetId(@Path("id") int groupId);

    @FormUrlEncoded
    @PUT("expenses")
    Call<ExpensesCreatePOJO> expensesPut(@Field("description") String description, @Field("date") String date, @Field("amount") double amount, @Field("category") int category);

    @DELETE("expenses/{id}")
    Call<ExpensesGetPOJO> expensesDelete(@Path("id") int groupId);

}