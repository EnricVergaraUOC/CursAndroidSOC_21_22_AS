package edu.uoc.expensemanager.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import edu.uoc.expensemanager.R;
import edu.uoc.expensemanager.Utilities.DownLoadImageTask;
import edu.uoc.expensemanager.model.ExpenseInfo;
import edu.uoc.expensemanager.model.TripInfo;
import edu.uoc.expensemanager.model.UserInfo;
import edu.uoc.expensemanager.ui.adapter.ExpenseListAdapter;
import edu.uoc.expensemanager.ui.adapter.UserListAdapter;

public class TripViewActivity extends AppCompatActivity {

    ImageButton btnEditTrip;
    Button btnResume;
    FloatingActionButton btnAddNewExpense;
    TextView txt_Description;
    TextView txt_Date;
    public ArrayList<UserInfo> users = new ArrayList<UserInfo>();
    ExpenseInfo[] myListData = null;
    TripInfo tripInfo;
    ImageView tripAvatar;
    UserListAdapter user_adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_view);

        btnEditTrip = findViewById(R.id.btn_edit_trip);
        btnAddNewExpense = findViewById(R.id.btn_add_new_expense);
        btnResume = findViewById(R.id.btn_resume);
        tripAvatar = findViewById(R.id.img_trip);
        btnResume.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent k = new Intent(TripViewActivity.this, ResumeActivity.class);
                startActivity(k);
            }
        });
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
            tripInfo = extras.getParcelable("tripInfo");
            txt_Description.setText(tripInfo.description);
            txt_Date.setText(tripInfo.date);
            if (tripInfo.image_url != null && tripInfo.image_url.compareTo("") != 0){
                new DownLoadImageTask(tripAvatar).execute(tripInfo.image_url);
            }
        }



        myListData = new ExpenseInfo[] {
                new ExpenseInfo("Expense1", "(10/17/2021)",  101, null),
                new ExpenseInfo("Expense2", "(10/17/2021)",  102, null),
                new ExpenseInfo("Expense3", "(10/17/2021)",  103, null),
                new ExpenseInfo("Expense4", "(10/17/2021)",  104, null),
                new ExpenseInfo("Expense5", "(10/17/2021)",  105, null),
                new ExpenseInfo("Expense6", "(10/17/2021)",  106, null),
                new ExpenseInfo("Expense7", "(10/17/2021)",  107, null),
                new ExpenseInfo("Expense8", "(10/17/2021)",  108, null),
                new ExpenseInfo("Expense9", "(10/17/2021)",  109, null),
                new ExpenseInfo("Expense10", "(10/17/2021)", 110, null),
                new ExpenseInfo("Expense11", "(10/17/2021)", 111, null),
                new ExpenseInfo("Expense12", "(10/17/2021)", 112, null)
        };

        RecyclerView recyclerView_expense = findViewById(R.id.expense_list);
        ExpenseListAdapter expense_adapter = new ExpenseListAdapter(myListData, this, users);
        recyclerView_expense.setHasFixedSize(true);
        recyclerView_expense.setLayoutManager(new LinearLayoutManager(this));
        recyclerView_expense.setAdapter(expense_adapter);


        for (String user :tripInfo.users)
        {
            users.add(new UserInfo("", "", user));
        }


        RecyclerView recyclerView_user = findViewById(R.id.user_list);
        user_adapter = new UserListAdapter(users, this);
        recyclerView_user.setHasFixedSize(true);

        LinearLayoutManager layoutManager= new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView_user.setLayoutManager(layoutManager);
        recyclerView_user.setAdapter(user_adapter);

        GetUserInfoFromFirebase();
    }



    public void GetUserInfoFromFirebase(){

    }
}