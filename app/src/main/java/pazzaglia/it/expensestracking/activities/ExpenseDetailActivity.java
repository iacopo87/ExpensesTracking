package pazzaglia.it.expensestracking.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import pazzaglia.it.expensestracking.R;
import pazzaglia.it.expensestracking.models.ExpensesCreatePOJO;
import pazzaglia.it.expensestracking.network.ApiInterface;
import pazzaglia.it.expensestracking.network.Utils;
import pazzaglia.it.expensestracking.shared.Common;
import pazzaglia.it.expensestracking.shared.Validator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExpenseDetailActivity extends AppCompatActivity {
    private static final String TAG = "ExpenseDetailActivity";

    public static final String MODE = "MODE";
    public static final String ADD = "ADD";
    public static final String EDIT = "EDIT";

    public static final String DESCRIPTION = "DESCRIPTION";
    public static final String AMOUNT = "AMOUNT";
    public static final String DATE = "DATE";
    public static final String CATEGORY = "CATEGORY";
    public static final String ID = "ID";

    @Bind(R.id.text_detail_title) TextView _text_detail_title;
    @Bind(R.id.edit_description) EditText _edit_description;
    @Bind(R.id.edit_amount) EditText _edit_amount;
    @Bind(R.id.edit_date) EditText _edit_date;
    @Bind(R.id.btn_cancel) Button _btn_cancel;
    @Bind(R.id.btn_modify) Button _btn_modify;

    private int category;
    private int id;
    private String description;
    private double amount;
    private String date;
    private String mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_detail);
        ButterKnife.bind(this);


        Intent intent = getIntent();
        mode = intent.getStringExtra(MODE);

        if(mode.equals(EDIT)) {
            //Set the editText with the expense datail
            category = intent.getIntExtra(CATEGORY, 1);
            id = intent.getIntExtra(ID, 0);
            description = intent.getStringExtra(DESCRIPTION);
            amount = intent.getDoubleExtra(AMOUNT, 0);
            date = intent.getStringExtra(DATE);

            _edit_description.setText(description);
            _edit_amount.setText(String.format(Locale.US, "%.2f", amount));
            _edit_date.setText(date);
        } else{
            _text_detail_title.setText("Insert Expense");
            _btn_modify.setText("Insert");

        }

        _btn_cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });

        _btn_modify.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                editOrAddExpense();
            }
        });


    }


    private void editOrAddExpense() {
        Call<ExpensesCreatePOJO> mService = null;

        if (!validate()) {
            onEditFailed("");
            return;
        }

        _btn_cancel.setEnabled(false);
        _btn_modify.setEnabled(false);

        final ProgressDialog progressDialog = Common.showProgressDialog(ExpenseDetailActivity.this, "Saving...");

        //Retrofit editExpense
        ApiInterface mApiService = Utils.getInterfaceService(this, true);

        _edit_description.setText(description);
        _edit_amount.setText(String.format(Locale.US, "%.2f", amount));
        _edit_date.setText(date);

        if(mode.equals(EDIT)) {
            mService = mApiService.expensesPut(id, description, date, amount, category);
        } else {
            mService = mApiService.expensesPost(description, date, amount, category);
        }

        mService.enqueue(new Callback<ExpensesCreatePOJO>() {
            @Override
            public void onResponse(Call<ExpensesCreatePOJO> call, Response<ExpensesCreatePOJO> response) {
                ExpensesCreatePOJO mExpenseObject = response.body();
                boolean editKo = mExpenseObject!= null && mExpenseObject.getError();
                if(!editKo){
                    onEditSuccess(mExpenseObject.getMessage());
                }else {
                    onEditFailed((mExpenseObject!= null)?mExpenseObject.getMessage():"");
                }
                progressDialog.dismiss();
            }
            @Override
            public void onFailure(Call<ExpensesCreatePOJO> call, Throwable t) {
                call.cancel();
                onEditFailed("Please check your network connection and internet permission");
            }
        });

    }

    public boolean validate() {
        boolean valid = true;

        //check description
        String description = _edit_description.getText().toString();
        valid &= Validator.isStringValid(description,_edit_description);
        if(valid) this.description = description;

        //Check amount
        String amount = _edit_amount.getText().toString();
        valid &= Validator.isAmountValid(amount,_edit_amount);
        if(valid) this.amount = Double.parseDouble(amount);

        //checkDate
        String date = _edit_date.getText().toString();
        valid &= Validator.isDateValid(date,_edit_date);
        if(valid) this.date = date;

        return valid;
    }
    public void onEditFailed(String message) {
        if(message!="")
            Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG).show();
        _btn_cancel.setEnabled(true);
        _btn_modify.setEnabled(true);
    }

    public void onEditSuccess(String message) {

        _btn_cancel.setEnabled(true);
        _btn_modify.setEnabled(true);

        Intent output = new Intent();
        output.putExtra(LandingPageActivity.MESSAGE,  message);
        setResult(RESULT_OK, output);

       finish();
    }



}
