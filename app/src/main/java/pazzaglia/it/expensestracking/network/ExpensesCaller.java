package pazzaglia.it.expensestracking.network;

import android.content.Context;

import pazzaglia.it.expensestracking.models.ExpensesListPOJO;
import retrofit2.Call;

/**
 * Created by IO on 02/10/2016.
 */

public class ExpensesCaller extends AbstractApiCaller<ExpensesListPOJO> {

    Context context;

    public ExpensesCaller(Context context) {
        this.context = context;
    }

    @Override
    public Call<ExpensesListPOJO> specificApiCall() {
        return getInterfaceService(context, true)
                .expensesGet();
    }

}
