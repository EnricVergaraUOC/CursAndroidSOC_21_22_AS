package edu.uoc.expensemanager.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;

import edu.uoc.expensemanager.R;
import edu.uoc.expensemanager.Utilities.DownLoadImageTask;
import edu.uoc.expensemanager.model.ExpenseInfo;
import edu.uoc.expensemanager.model.PayerInfo;
import edu.uoc.expensemanager.model.UserInfo;
import edu.uoc.expensemanager.ui.adapter.TripListAdapter;
import edu.uoc.expensemanager.ui.adapter.UserAsPayerListAdapter;

public class ResumeActivity extends AppCompatActivity {

    public ArrayList<UserInfo> users = new ArrayList<UserInfo>();
    public ArrayList<ExpenseInfo> expenses = new ArrayList<ExpenseInfo>();
    TextView txt_TripName;
    TextView txt_TripAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resume);

        txt_TripName = findViewById(R.id.text_trip_name);
        txt_TripAmount = findViewById(R.id.text_trip_amount);


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            expenses = (ArrayList<ExpenseInfo>) getIntent().getSerializableExtra("expenses");
            users = (ArrayList<UserInfo>) getIntent().getSerializableExtra("users");

        }


        CreateUserListFromExpenses();


        RecyclerView list_users = findViewById(R.id.list_users);
        UserAsPayerListAdapter adapter = new UserAsPayerListAdapter(users, this);
        list_users.setHasFixedSize(true);
        list_users.setLayoutManager(new LinearLayoutManager(this));
        list_users.setAdapter(adapter);
    }

    public void CreateUserListFromExpenses(){
        int tripTotalAmount = 0;
        for (UserInfo user:users) {
            for (ExpenseInfo expense:expenses) {
                tripTotalAmount+=expense.totalAmount;
                for (PayerInfo payer:expense.payers) {
                    if (user.email.compareTo(payer.email)== 0){
                        user.amountPayed += payer.amount;
                    }
                }
            }
        }
        tripTotalAmount /= users.size();
        txt_TripAmount.setText(tripTotalAmount + " €");

        for (UserInfo user:users) {
            user.toPayOrToReceive = user.amountPayed - (tripTotalAmount/users.size());
        }

    }
}