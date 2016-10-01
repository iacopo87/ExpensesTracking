package pazzaglia.it.expensestracking.shared;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

import pazzaglia.it.expensestracking.R;
import pazzaglia.it.expensestracking.models.Expense;

/**
 * Created by IO on 29/06/2016.
 */

public class Common {

    public static final String SHARED_PREF_NAME = "PREF_LOGIN";
    public static final String PREF_NAME = "NAME";
    public static final String PREF_API_KEY = "API_KEY";


    public static void updateTotalExpenses(Activity activity,List<Expense> data){
        double totalExpenses = 0;
        for (Expense expense:data){
            totalExpenses += expense.getAmount();
        }
        ((TextView)activity.findViewById(R.id.text_totalExpenses)).setText(String.format(Locale.US, "%.2f€", totalExpenses));
    };

    public static ProgressDialog showProgressDialog(Context context,String message){

        ProgressDialog progressDialog = new ProgressDialog(context,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(message);
        progressDialog.show();

        return progressDialog;
    }

    public static String getApiKey(Context context){
        SharedPreferences sharedPref = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPref.getString(PREF_API_KEY,"");
    }
    public static String getName(Context context){
        SharedPreferences sharedPref = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPref.getString(PREF_NAME,"");
    }
    public static void setApiKeyAndName(Context context, String apiKey, String name){
        //save API Key and name
        SharedPreferences sharedPref = context.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(PREF_API_KEY, apiKey);
        editor.putString(PREF_NAME, name);
        editor.commit();
    }

    public static void clearApiKeyAndName(Context context){
        setApiKeyAndName(context, "", "");
    }


}
