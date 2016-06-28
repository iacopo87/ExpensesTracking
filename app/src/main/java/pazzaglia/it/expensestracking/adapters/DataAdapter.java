package pazzaglia.it.expensestracking.adapters;

/**
 * Created by IO on 28/06/2016.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.List;

import pazzaglia.it.expensestracking.activities.ExpenseDetailActivity;
import pazzaglia.it.expensestracking.activities.LandingPageActivity;
import pazzaglia.it.expensestracking.models.Expense;
import pazzaglia.it.expensestracking.R;

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


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView text_description,text_amount,text_date;
        public ViewHolder(View view) {
            super(view);

            text_description = (TextView)view.findViewById(R.id.text_description);
            text_amount = (TextView)view.findViewById(R.id.text_amount);
            text_date = (TextView)view.findViewById(R.id.text_date);

            view.setOnClickListener(this);
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

    }

}