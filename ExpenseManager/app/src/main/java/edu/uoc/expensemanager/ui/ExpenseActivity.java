package edu.uoc.expensemanager.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    Button btnSave;
    ArrayList<UserInfo> users;
    ArrayList<PayerInfo> payers = new ArrayList<PayerInfo>();
    PayerListAdapter adapter;
    Spinner payer_spinner;
    Integer totalAmount;
    ProgressBar progressBar;
    String expenseID;
    String tripID;
    int spinnerCurrentIndexSelected = 0;
    boolean editionMode;

    boolean changeSomething = false;

    //To debug
    boolean savedCorrectly = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense);
        editionMode = false;
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        txt_amount = findViewById(R.id.txtf_amount);
        txt_date = findViewById(R.id.txtf_date);;
        txt_description = findViewById(R.id.txtf_description);
        lbl_warning = findViewById(R.id.lbl_warning);
        lbl_warning.setVisibility(View.INVISIBLE);
        btnAddPayer = findViewById(R.id.btn_add_payer);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);
        btnSave = findViewById(R.id.btn_expense_save);
        btnSave.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                boolean showWarning = false;
                String infoWarning = "";
                if (payers.size() == 0){
                    showWarning = true;
                    infoWarning = "Before saving you need to add at least one payer";
                }else{
                    int totalAmount = 0;
                    for (PayerInfo payer :payers)
                    {
                        totalAmount += payer.amount;
                    }
                    String s_totalAmount = txt_amount.getText().toString();

                    try{
                        int number = Integer.parseInt(s_totalAmount);
                        if (totalAmount != number){
                            lbl_warning.setVisibility(View.VISIBLE);
                            infoWarning = "Be careful, the sum of all the payers ("+totalAmount +"€)";
                            infoWarning += "have to be ("+ number +"€)";
                            showWarning = true;
                        }else{
                            showWarning = false;
                        }
                    }
                    catch (NumberFormatException ex){
                        ex.printStackTrace();
                    }
                }

                if (showWarning){
                    new AlertDialog.Builder(ExpenseActivity.this)
                            .setTitle("Error saving the expanse")
                            .setMessage(infoWarning)

                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // Continue with delete operation
                                }
                            })

                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }else{
                    if (editionMode){
                        UpdateExpenseOnFirebase();
                    }else{
                        CreateNewExpenseOnFirebase();
                    }

                }
            }
        });
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String description = extras.getString("Description");
            if (description != null && description.compareTo("")!= 0){
                editionMode = true;
            }
            if (editionMode){
                btnSave.setText("Update");
                payers = extras.getParcelableArrayList("Payers");
                expenseID = extras.getString("ExpenseID");

            }
            String date = extras.getString("Date");
            totalAmount = extras.getInt("Amount");
            tripID = extras.getString("TripID");
            txt_description.setText(description);
            txt_date.setText(date);
            txt_amount.setText("" + totalAmount);

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
                    PayerInfo newPayer = new PayerInfo(user.url_avatar,user.name,user.email,amount);
                    payers.add(newPayer);
                    if (payers.size() == 1) {
                        adapter.notifyItemChanged(payers.size()-1);
                    }else{
                        adapter.notifyItemInserted(payers.size()-1);
                    }


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


        RecyclerView recyclerView = findViewById(R.id.payer_list);
        adapter = new PayerListAdapter(payers, this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

    }

    public void updateLabelWarning(){
        if (payers.size() == 0){
            lbl_warning.setVisibility(View.INVISIBLE);
        }else{
            int totalAmount = 0;
            for (PayerInfo payer :payers)
            {
                totalAmount += payer.amount;
            }
            String s_totalAmount = txt_amount.getText().toString();

            try{
                int number = Integer.parseInt(s_totalAmount);
                if (totalAmount != number){
                    lbl_warning.setVisibility(View.VISIBLE);
                    String info = "Be careful, the sum of all the payers ("+totalAmount +"€)";
                    info += "\nhave to be ("+ number +"€)";
                    lbl_warning.setText(info);
                }else{
                    lbl_warning.setVisibility(View.INVISIBLE);
                }
            }
            catch (NumberFormatException ex){
                ex.printStackTrace();
            }

        }
    }
    public void ConnectionFinished(){
        progressBar.setVisibility(View.INVISIBLE);
        btnSave.setEnabled(true);
    }

    public void UpdateExpenseOnFirebase(){
        Map<String, Object> expense = new HashMap<>();
        expense.put("tripID",tripID );
        expense.put("amount",txt_amount.getText().toString() );
        expense.put("date",txt_date.getText().toString() );
        expense.put("description",txt_description.getText().toString() );

        List payers_ = new ArrayList();
        for (PayerInfo payer :payers)
        {
            payers_.add(payer);
        }
        expense.put("payers", payers_);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference expenseRef = db.collection("expenses").document(expenseID);
        // Add a new document with a generated ID
        expenseRef
                .update(expense)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        ExpenseSavedSuccessfully();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        ShowErrorStatus(e.toString());
                    }
                });
    }
    public void CreateNewExpenseOnFirebase(){
        progressBar.setVisibility(View.VISIBLE);
        btnSave.setEnabled(false);
        // Create a new user with a first and last name
        Map<String, Object> expense = new HashMap<>();
        expense.put("tripID",tripID );
        expense.put("amount",txt_amount.getText().toString() );
        expense.put("date",txt_date.getText().toString() );
        expense.put("description",txt_description.getText().toString() );

        List payers_ = new ArrayList();
        for (PayerInfo payer :payers)
        {
            payers_.add(payer);
        }
        expense.put("payers", payers_);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        // Add a new document with a generated ID
        db.collection("expenses")
                .add(expense)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        String doc_id = documentReference.getId();
                        Log.d("TripEditActivity", "DocumentSnapshot added with ID: " + doc_id);
                        ExpenseSavedSuccessfully();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        String error = e.toString();
                        ShowErrorStatus(error);
                    }
                });
    }

    public void ShowErrorStatus(String msg){
        lbl_warning.setVisibility(View.VISIBLE);
        lbl_warning.setTextColor(Color.RED);
        lbl_warning.setText(msg);
        ConnectionFinished();
    }

    public void ExpenseSavedSuccessfully(){
        changeSomething = true;
        lbl_warning.setVisibility(View.VISIBLE);
        lbl_warning.setTextColor(Color.GREEN);
        if (editionMode){
            lbl_warning.setText("Expense Updated successfully");
        }else{
            lbl_warning.setText("Expense Saved successfully");
        }

        ConnectionFinished();
    }

    public void onBackPressed() {
        if (changeSomething){
            Intent data = getIntent();
            setResult(100, data);
        }

        finish();
    }
}