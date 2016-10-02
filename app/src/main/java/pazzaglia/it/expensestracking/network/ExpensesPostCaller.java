package pazzaglia.it.expensestracking.network;

import android.content.Context;

import pazzaglia.it.expensestracking.models.ExpensesCreatePOJO;
import retrofit2.Call;

/**
 * Created by IO on 02/10/2016.
 */

public class ExpensesPostCaller extends AbstractApiCaller<ExpensesCreatePOJO> {
    private String description, date;
    private int category;
    private double amount;
    private Context context;

    public ExpensesPostCaller(Context context, String description, String date, double amount, int category) {
        this.description = description;
        this.category = category;
        this.date = date;
        this.context = context;
        this.amount = amount;
    }

    @Override
    public Call<ExpensesCreatePOJO> specificApiCall() {
        return getInterfaceService(context, true)
                .expensesPost(description, date, amount, category);
    }

}
