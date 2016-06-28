package pazzaglia.it.expensestracking.adapters;

/**
 * Created by IO on 28/06/2016.
 */

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import pazzaglia.it.expensestracking.R;
import pazzaglia.it.expensestracking.activities.ExpenseDetailActivity;
import pazzaglia.it.expensestracking.activities.LandingPageActivity;
import pazzaglia.it.expensestracking.models.Expense;
import pazzaglia.it.expensestracking.models.RegistrationPOJO;
import pazzaglia.it.expensestracking.network.ApiInterface;
import pazzaglia.it.expensestracking.network.Utils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder> {
    Context context;
    private List<Expense> expenses;

    public DataAdapter(List<Expense> android, Context c) {
        this.expenses = android;
        this.context = c;
    }

    @Override
    public DataAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_row, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DataAdapter.ViewHolder viewHolder, int i) {

        viewHolder.text_description.setText(expenses.get(i).getDescription());
        viewHolder.text_amount.setText(String.format("%.2f€", expenses.get(i).getAmount()));
        viewHolder.text_date.setText(expenses.get(i).getDate());
    }

    @Override
    public int getItemCount() {
        return expenses.size();
    }

    public void delete(int position) { //removes the row
        expenses.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, expenses.size());
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        private TextView text_description,text_amount,text_date;
        public ViewHolder(View view) {
            super(view);

            text_description = (TextView)view.findViewById(R.id.text_description);
            text_amount = (TextView)view.findViewById(R.id.text_amount);
            text_date = (TextView)view.findViewById(R.id.text_date);

            view.setOnClickListener(this);
            view.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            //delete(getAdapterPosition()); //calls the method above to delete
            //expenses.get(0);

            Intent intent = new Intent(context, ExpenseDetailActivity.class);
            intent.putExtra(ExpenseDetailActivity.DESCRIPTION, expenses.get(getAdapterPosition()).getDescription());
            intent.putExtra(ExpenseDetailActivity.AMOUNT, expenses.get(getAdapterPosition()).getAmount());
            intent.putExtra(ExpenseDetailActivity.DATE, expenses.get(getAdapterPosition()).getDate());
            intent.putExtra(ExpenseDetailActivity.CATEGORY, expenses.get(getAdapterPosition()).getCategory());
            intent.putExtra(ExpenseDetailActivity.ID, expenses.get(getAdapterPosition()).getId());
            intent.putExtra(ExpenseDetailActivity.MODE, ExpenseDetailActivity.EDIT);

            ((Activity) context).startActivityForResult(intent,LandingPageActivity.REQUEST_EDIT);
        }

        @Override
        public boolean onLongClick(View v) {
            AlertDialog.Builder builder = new AlertDialog.Builder((Activity) context);
            builder.setMessage("Delete expense?");

            String positiveText = "Delete";
            builder.setPositiveButton(positiveText,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            deleteItem(getAdapterPosition());
                        }
                    });

            String negativeText = "Cancel";
            builder.setNegativeButton(negativeText,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });

            AlertDialog dialog = builder.create();
            // display dialog
            dialog.show();

            return false;
        }

        public void deleteItem(final int index){

            final ProgressDialog progressDialog = new ProgressDialog(context,
                    R.style.AppTheme_Dark_Dialog);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Deleting...");
            progressDialog.show();

            //Retrofit delete
            SharedPreferences sharedPref = context.getSharedPreferences("PREF_LOGIN", Context.MODE_PRIVATE);
            String apiKey = sharedPref.getString("API_KEY","");
            ApiInterface mApiService = Utils.getInterfaceService(true, apiKey);
            Call<RegistrationPOJO> mService = mApiService.expensesDelete(expenses.get(index).getId());
            mService.enqueue(new Callback<RegistrationPOJO>() {
                @Override
                public void onResponse(Call<RegistrationPOJO> call, Response<RegistrationPOJO> response) {
                    RegistrationPOJO mDeleteObject = response.body();
                    boolean deleteKo = mDeleteObject != null && mDeleteObject.getError();
                    if(!deleteKo){
                        onDeleteSuccess(index, mDeleteObject.getMessage());
                    }else {
                        onDeleteFailed((mDeleteObject != null)?mDeleteObject.getMessage():"");
                    }
                    progressDialog.dismiss();
                }
                @Override
                public void onFailure(Call<RegistrationPOJO> call, Throwable t) {
                    call.cancel();
                    onDeleteFailed("Please check your network connection and internet permission");
                }
            });
        }
    }

    private void onDeleteSuccess(int index, String message){
        if(message!="")
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();

        delete(index);
    }

    private void onDeleteFailed(String message){
        if(message!="")
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

}