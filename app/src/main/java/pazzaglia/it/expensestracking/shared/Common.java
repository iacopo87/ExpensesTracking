package pazzaglia.it.expensestracking.shared;

import android.app.Activity;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

import pazzaglia.it.expensestracking.R;
import pazzaglia.it.expensestracking.models.Expense;

/**
 * Created by IO on 29/06/2016.
 */

public class Common {

    public static void updateTotalExpenses(Activity activity,List<Expense> data){
        double totalExpenses = 0;
        for (Expense expense:data){
            totalExpenses += expense.getAmount();
        }
        ((TextView)activity.findViewById(R.id.text_totalExpenses)).setText(String.format(Locale.US, "%.2f€", totalExpenses));
    };

}
