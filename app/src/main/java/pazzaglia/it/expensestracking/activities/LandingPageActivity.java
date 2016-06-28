package pazzaglia.it.expensestracking.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import pazzaglia.it.expensestracking.R;
import pazzaglia.it.expensestracking.adapters.DataAdapter;
import pazzaglia.it.expensestracking.models.Expense;
import pazzaglia.it.expensestracking.models.ExpensesListPOJO;
import pazzaglia.it.expensestracking.network.ApiInterface;
import pazzaglia.it.expensestracking.network.Utils;
import pazzaglia.it.expensestracking.shared.Common;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LandingPageActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final String LOGIN_NAME = "LOGIN_NAME";
    public static final String LOGIN_API_KEY = "LOGIN_API_KEY";

    public static final int REQUEST_EDIT = 0;


    public static final String MESSAGE = "MESSAGE";

    private RecyclerView recyclerView;
    private List<Expense> data;
    private DataAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_landing_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(LandingPageActivity.this, ExpenseDetailActivity.class);
                intent.putExtra(ExpenseDetailActivity.MODE, ExpenseDetailActivity.ADD);
                startActivityForResult(intent,REQUEST_EDIT);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Set the name on the drawer
        Intent intent = getIntent();
        String name = intent.getStringExtra(LOGIN_NAME);
        View headerLayout = navigationView.getHeaderView(0);
        TextView nameTextView = ((TextView) headerLayout.findViewById(R.id.textViewName));
        nameTextView.setText(name);

        //Retrieve the data and show them
        String apiKey = intent.getStringExtra(LOGIN_API_KEY);
        expensesListLoading(apiKey);
    }

    private void expensesListLoading(String apiKey){
        //initialize the view
        recyclerView = (RecyclerView)findViewById(R.id.card_recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());


        recyclerView.setLayoutManager(layoutManager);

        //Downaload the data
        downloadExpenses(apiKey);
    }

    private void downloadExpenses(String apiKey){
        //Show the loader
        final ProgressDialog progressDialog = new ProgressDialog(LandingPageActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Retrieving data...");
        progressDialog.show();

        //Retrofit expenses list download
        ApiInterface mApiService = Utils.getInterfaceService(true, apiKey);
        Call<ExpensesListPOJO> mService = mApiService.expensesGet();
        mService.enqueue(new Callback<ExpensesListPOJO>() {
            @Override
            public void onResponse(Call<ExpensesListPOJO> call, Response<ExpensesListPOJO> response) {
                ExpensesListPOJO mExpensesListObject = response.body();
                boolean expensesGetKo = mExpensesListObject!=null && mExpensesListObject.getError();
                //showProgress(false);
                if(!expensesGetKo){
                    data = mExpensesListObject.getExpenses();
                    Common.updateTotalExpenses(LandingPageActivity.this, data);
                    adapter = new DataAdapter(data, LandingPageActivity.this);
                    recyclerView.setAdapter(adapter);
                }else {
                    onDownloadListFailure("Error downloading data");
                }
                progressDialog.dismiss();
            }
            @Override
            public void onFailure(Call<ExpensesListPOJO> call, Throwable t) {
                call.cancel();
                onDownloadListFailure("Please check your network connection and internet permission");
            }
        });
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_logout) {
            //reset API Key
            SharedPreferences sharedPref = getSharedPreferences("PREF_LOGIN",Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("API_KEY", "");
            editor.putString("NAME", "");
            editor.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        finish();

        return true;
    }

    private void onDownloadListFailure(String message){
        Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_EDIT) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(getBaseContext(), data.getStringExtra(LandingPageActivity.MESSAGE), Toast.LENGTH_LONG).show();
                SharedPreferences sharedPref = getSharedPreferences("PREF_LOGIN", Context.MODE_PRIVATE);
                String apiKey = sharedPref.getString("API_KEY","");
                downloadExpenses(apiKey);
            }
        }
    }
}
