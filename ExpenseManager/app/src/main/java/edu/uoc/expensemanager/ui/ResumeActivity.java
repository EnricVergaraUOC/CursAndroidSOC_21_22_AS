package edu.uoc.expensemanager.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;

import edu.uoc.expensemanager.R;
import edu.uoc.expensemanager.model.ExpenseInfo;
import edu.uoc.expensemanager.model.PayerInfo;
import edu.uoc.expensemanager.model.UserInfo;
import edu.uoc.expensemanager.ui.adapter.TripListAdapter;
import edu.uoc.expensemanager.ui.adapter.UserAsPayerListAdapter;

public class ResumeActivity extends AppCompatActivity {

    public ArrayList<UserInfo> users = new ArrayList<UserInfo>();
    public ArrayList<ExpenseInfo> expenses = new ArrayList<ExpenseInfo>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resume);
        //UserInfo(String name, String url_avatar
        users.add(new UserInfo("enric", "", "enric@uoc.edu"));
        users.add(new UserInfo("joan", "", "joan@uoc.edu"));
        users.add(new UserInfo("maria", "", "maria@uoc.edu"));


        //expenses hardcoded-->
        ArrayList<PayerInfo> payers1 = new ArrayList<PayerInfo>();
        payers1.add(new PayerInfo(null,"enric", "enric@uoc.edu", 101));
        ExpenseInfo exp1 = new ExpenseInfo("Expense1", "(10/17/2021)",  101, payers1);

        ArrayList<PayerInfo> payers2 = new ArrayList<PayerInfo>();
        payers2.add(new PayerInfo(null,"maria", "enric@uoc.edu", 25));
        payers2.add(new PayerInfo(null,"enric", "enric@uoc.edu", 25));
        ExpenseInfo exp2 = new ExpenseInfo("Expense1", "(10/17/2021)",  100, payers2);

        ArrayList<PayerInfo> payers3 = new ArrayList<PayerInfo>();
        payers3.add(new PayerInfo(null,"maria", "enric@uoc.edu", 170));
        payers3.add(new PayerInfo(null,"enric", "enric@uoc.edu", 30));
        ExpenseInfo exp3 = new ExpenseInfo("Expense1", "(10/17/2021)",  200, payers3);

        expenses.add(exp1);
        expenses.add(exp2);
        expenses.add(exp3);


        CreateUserListFromExpenses();


        RecyclerView list_users = findViewById(R.id.list_users);
        UserAsPayerListAdapter adapter = new UserAsPayerListAdapter(users, this);
        list_users.setHasFixedSize(true);
        list_users.setLayoutManager(new LinearLayoutManager(this));
        list_users.setAdapter(adapter);
    }

    public void CreateUserListFromExpenses(){

    }
}