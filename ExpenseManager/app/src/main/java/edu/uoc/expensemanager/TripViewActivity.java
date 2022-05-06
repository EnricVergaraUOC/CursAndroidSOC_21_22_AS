package edu.uoc.expensemanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class TripViewActivity extends AppCompatActivity {

    ImageButton btnEditTrip;
    FloatingActionButton btnAddNewExpense;
    TextView txt_Description;
    TextView txt_Date;
    ArrayList<UserInfo> users = new ArrayList<UserInfo>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_view);

        btnEditTrip = findViewById(R.id.btn_edit_trip);
        btnAddNewExpense = findViewById(R.id.btn_add_new_expense);

        //Add actions to the buttons:
        btnEditTrip.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent k = new Intent(TripViewActivity.this, TripEditActivity.class);
                startActivity(k);
            }
        });
        btnAddNewExpense.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent k = new Intent(TripViewActivity.this, ExpenseActivity.class);
                startActivity(k);
            }
        });

        txt_Description = findViewById(R.id.txt_description);
        txt_Date = findViewById(R.id.txt_date);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String description = extras.getString("Description");
            String date = extras.getString("Date");
            txt_Description.setText(description);
            txt_Date.setText(date);

            //The key argument here must match that used in the other activity
        }



        ExpenseInfo[] myListData = new ExpenseInfo[] {
                new ExpenseInfo("Expense1", "(10/17/2021)",  101),
                new ExpenseInfo("Expense2", "(10/17/2021)",  102),
                new ExpenseInfo("Expense3", "(10/17/2021)",  103),
                new ExpenseInfo("Expense4", "(10/17/2021)",  104),
                new ExpenseInfo("Expense5", "(10/17/2021)",  105),
                new ExpenseInfo("Expense6", "(10/17/2021)",  106),
                new ExpenseInfo("Expense7", "(10/17/2021)",  107),
                new ExpenseInfo("Expense8", "(10/17/2021)",  108),
                new ExpenseInfo("Expense9", "(10/17/2021)",  109),
                new ExpenseInfo("Expense10", "(10/17/2021)", 110),
                new ExpenseInfo("Expense11", "(10/17/2021)", 111),
                new ExpenseInfo("Expense12", "(10/17/2021)", 112)
        };

        RecyclerView recyclerView_expense = findViewById(R.id.expense_list);
        ExpenseListAdapter expense_adapter = new ExpenseListAdapter(myListData, this);
        recyclerView_expense.setHasFixedSize(true);
        recyclerView_expense.setLayoutManager(new LinearLayoutManager(this));
        recyclerView_expense.setAdapter(expense_adapter);

        String path1 = "https://m.media-amazon.com/images/M/MV5BNzUxNjM4ODI1OV5BMl5BanBnXkFtZTgwNTEwNDE2OTE@._V1_SX150_CR0,0,150,150_.jpg";
        String path2 = "https://m.media-amazon.com/images/M/MV5BMTUyMDU1MTU2N15BMl5BanBnXkFtZTgwODkyNzQ3MDE@._V1_SX150_CR0,0,150,150_.jpg";
        String path3 = "https://m.media-amazon.com/images/M/MV5BMTk1MjM5NDg4MF5BMl5BanBnXkFtZTcwNDg1OTQ4Nw@@._V1_SX150_CR0,0,150,150_.jpg";
        String path4 = "https://m.media-amazon.com/images/M/MV5BMjExNjY5NDY0MV5BMl5BanBnXkFtZTgwNjQ1Mjg1MTI@._V1_SX150_CR0,0,150,150_.jpg";

        users.add(new UserInfo("Enric", path1));
        users.add(new UserInfo("Joan", path2));

        RecyclerView recyclerView_user = findViewById(R.id.user_list);
        UserListAdapter user_adapter = new UserListAdapter(users, this);
        recyclerView_user.setHasFixedSize(true);
        recyclerView_user.setLayoutManager(new LinearLayoutManager(this));
        recyclerView_user.setAdapter(user_adapter);


    }
}