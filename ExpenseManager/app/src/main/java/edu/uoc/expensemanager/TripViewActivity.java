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

public class TripViewActivity extends AppCompatActivity {

    ImageButton btnEditTrip;
    FloatingActionButton btnAddNewExpense;
    TextView txt_Description;
    TextView txt_Date;

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

        RecyclerView recyclerView = findViewById(R.id.expense_list);
        ExpenseListAdapter adapter = new ExpenseListAdapter(myListData, this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);


    }
}