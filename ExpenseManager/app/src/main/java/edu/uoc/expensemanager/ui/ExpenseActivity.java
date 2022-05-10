package edu.uoc.expensemanager.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;

import edu.uoc.expensemanager.R;
import edu.uoc.expensemanager.model.PayerInfo;
import edu.uoc.expensemanager.model.UserInfo;
import edu.uoc.expensemanager.ui.adapter.PayerListAdapter;

public class ExpenseActivity extends AppCompatActivity  {
    EditText txt_amount;
    EditText txt_date;
    EditText txt_description;
    TextView lbl_warning;
    Button btnAddPayer;
    ArrayList<UserInfo> users;
    ArrayList<PayerInfo> payers = new ArrayList<PayerInfo>();
    PayerListAdapter adapter;
    Spinner payer_spinner;
    Integer totalAmount;

    int spinnerCurrentIndexSelected = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense);

        txt_amount = findViewById(R.id.txtf_amount);
        txt_date = findViewById(R.id.txtf_date);;
        txt_description = findViewById(R.id.txtf_description);
        lbl_warning = findViewById(R.id.lbl_warning);
        lbl_warning.setVisibility(View.INVISIBLE);
        btnAddPayer = findViewById(R.id.btn_add_payer);
        Button btn_save = findViewById(R.id.btn_expense_save);
        btn_save.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                int kaka = 0;
            }
        });
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String description = extras.getString("Description");
            String date = extras.getString("Date");
            totalAmount = extras.getInt("Amount");

            txt_description.setText(description);
            txt_date.setText(date);
            txt_amount.setText("" + totalAmount + " €");

            users = extras.getParcelableArrayList("Users");

        }

        ArrayAdapter<UserInfo> adapter_spinner = new ArrayAdapter<UserInfo>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, users);
        payer_spinner = findViewById(R.id.payer_spinner);
        payer_spinner.setAdapter(adapter_spinner);
        payer_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //Toast.makeText(view.getContext(),"click on item: "+ users.get(i).name,Toast.LENGTH_LONG).show();
                spinnerCurrentIndexSelected = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



        btnAddPayer.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                UserInfo user = users.get(spinnerCurrentIndexSelected);
                boolean userAdded = false;
                for (PayerInfo payer:payers)
                {
                    if (payer.name.compareTo(user.name) == 0 ){
                        userAdded = true;
                        break;
                    }
                }

                if (!userAdded){
                    int amount = 0;
                    if (payers.size() == 0){
                        amount = totalAmount;
                    }
                    PayerInfo newPayer = new PayerInfo("",user.name,"",amount);
                    payers.add(newPayer);
                    adapter.notifyItemInserted(payers.size()-1);
                    updateLabelWarning();
                }else{
                    new AlertDialog.Builder(ExpenseActivity.this)
                            .setTitle("User " + user.name + " is already added")
                            .setMessage("")

                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // Continue with delete operation
                                }
                            })

                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }

            }
        });




        String path1 = "https://m.media-amazon.com/images/M/MV5BNzUxNjM4ODI1OV5BMl5BanBnXkFtZTgwNTEwNDE2OTE@._V1_SX150_CR0,0,150,150_.jpg";
        String path2 = "https://m.media-amazon.com/images/M/MV5BMTUyMDU1MTU2N15BMl5BanBnXkFtZTgwODkyNzQ3MDE@._V1_SX150_CR0,0,150,150_.jpg";
        String path3 = "https://m.media-amazon.com/images/M/MV5BMTk1MjM5NDg4MF5BMl5BanBnXkFtZTcwNDg1OTQ4Nw@@._V1_SX150_CR0,0,150,150_.jpg";
        String path4 = "https://m.media-amazon.com/images/M/MV5BMjExNjY5NDY0MV5BMl5BanBnXkFtZTgwNjQ1Mjg1MTI@._V1_SX150_CR0,0,150,150_.jpg";


        RecyclerView recyclerView = findViewById(R.id.payer_list);
        adapter = new PayerListAdapter(payers, this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

    }

    public void updateLabelWarning(){
        lbl_warning.setVisibility(View.VISIBLE);
        lbl_warning.setText("Be careful, the sum of all the payers (200 €) have to be the total amount (100€)");
    }
}