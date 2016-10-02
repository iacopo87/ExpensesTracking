package pazzaglia.it.expensestracking.network;

import android.content.Context;

import pazzaglia.it.expensestracking.models.ExpensesCreatePOJO;
import retrofit2.Call;

/**
 * Created by IO on 02/10/2016.
 */

public class ExpensesDeleteCaller extends AbstractApiCaller<ExpensesCreatePOJO> {
    private Context context;
    private int id;

    public ExpensesDeleteCaller(Context context, int id) {
        this.id = id;
        this.context = context;
    }

    @Override
    public Call<ExpensesCreatePOJO> specificApiCall() {
        return getInterfaceService(context, true)
                .expensesDelete(id);
    }

}
