package edu.uoc.expensemanager.ui;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.uoc.expensemanager.R;
import edu.uoc.expensemanager.Utilities.DownLoadImageTask;
import edu.uoc.expensemanager.model.ExpenseInfo;
import edu.uoc.expensemanager.model.PayerInfo;
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
    ArrayList<ExpenseInfo> myListData = new ArrayList<ExpenseInfo>();
    TripInfo tripInfo;
    ImageView tripAvatar;
    UserListAdapter user_adapter;
    Button btn_add_new_user;
    ImageButton btn_delete_trip;
    ExpenseListAdapter expense_adapter;
    int numExpenses;
    public ActivityResultLauncher<Intent> mStartForResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == 100) {
                        myListData.clear();
                        GetUserInfoFromFirebase();
                    }else if (result.getResultCode() == 200){
                        Intent intent = result.getData();
                        tripInfo.description = result.getData().getStringExtra("desc");
                        tripInfo.date = result.getData().getStringExtra("date");
                        txt_Description.setText(tripInfo.description);
                        txt_Date.setText(tripInfo.date);

                    }
                }
            });
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_view);

        btnEditTrip = findViewById(R.id.btn_edit_trip);
        btnAddNewExpense = findViewById(R.id.btn_add_new_expense);
        btnResume = findViewById(R.id.btn_resume);
        tripAvatar = findViewById(R.id.img_trip);
        btn_delete_trip = findViewById(R.id.btn_delete_trip);
        btn_delete_trip.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                new android.app.AlertDialog.Builder(TripViewActivity.this)
                        .setTitle("Deleting trip: " + tripInfo.description)
                        .setMessage("Do you really want to delete this trip?")

                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                DeleteTripFromFirebase();
                            }
                        })

                        .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                //Cancel operation nothing to do.
                            }
                        })

                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });
        btn_add_new_user = findViewById(R.id.btn_add_new_user);
        btn_add_new_user.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(TripViewActivity.this);
                builder.setTitle("Enter the email of the user you want to add to the trip:");

                final EditText input = new EditText(TripViewActivity.this);
                input.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                builder.setView(input);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        CheckIfUserExistOnFirebase(input.getText().toString());

                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });
        btnResume.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent k = new Intent(TripViewActivity.this, ResumeActivity.class);
                k.putExtra("expenses",myListData);
                k.putExtra("users",users);

                startActivity(k);
            }
        });
        //Add actions to the buttons:
        btnEditTrip.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent k = new Intent(TripViewActivity.this, TripEditActivity.class);
                k.putExtra("tripInfo",tripInfo);
                mStartForResult.launch(k);

            }
        });
        btnAddNewExpense.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent k = new Intent(TripViewActivity.this, ExpenseActivity.class);
                k.putExtra("Description","");
                k.putExtra("Date","");
                k.putExtra("Amount",0);
                k.putExtra("Users", users);
                k.putExtra("TripID", tripInfo.tripID);
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

        RecyclerView recyclerView_expense = findViewById(R.id.expense_list);
        expense_adapter = new ExpenseListAdapter(myListData, this, users, tripInfo.tripID);
        recyclerView_expense.setHasFixedSize(true);
        recyclerView_expense.setLayoutManager(new LinearLayoutManager(this));
        recyclerView_expense.setAdapter(expense_adapter);


        for (String email :tripInfo.users)
        {
            users.add(new UserInfo(email));
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

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference usersRef = db.collection("users");
        List<String> _users = new ArrayList<String>();
        for (String usr:tripInfo.users) {
            _users.add(usr);
        }

        usersRef.whereIn("email", _users)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            users.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map<String,Object> usr = document.getData();
                                String email = (String) usr.get("email");
                                String imgURL = (String) usr.get("imgURL");
                                String userName = (String) usr.get("userName");
                                if (email == null) {
                                    email = "";
                                }
                                if (imgURL == null){
                                    imgURL = "";
                                }
                                if (userName == null){
                                    userName = "";
                                }

                                users.add(new UserInfo(userName, imgURL, email));
                            }
                            user_adapter.notifyDataSetChanged();
                            GetExpensesFromFirebase();
                        } else {
                            String msg_error = task.getException().toString();
                            Log.w("TripListActivity", "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    public void CheckIfUserExistOnFirebase(String email){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference usersRef = db.collection("users");
        List<String> _users = new ArrayList<String>();
        for (String usr:tripInfo.users) {
            _users.add(usr);
        }

        usersRef.whereEqualTo("email", email)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            users.clear();
                            boolean userExist = false;
                            if (task.getResult().size() != 0){
                                userExist = true;
                            }

                            if (userExist){
                                UpdateUserListOnFirebase(email);
                            }else{
                                Toast.makeText(TripViewActivity.this,"User: "+ email + "is not in the app",Toast.LENGTH_LONG).show();
                            }
                        } else {
                            String msg_error = task.getException().toString();
                            Toast.makeText(TripViewActivity.this,"Error connecting to the database: "+task.getException(),Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    public void UpdateUserListOnFirebase(String email){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference tripRef = db.collection("trips").document(tripInfo.tripID);
        tripRef.update("users", FieldValue.arrayUnion(email))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        tripInfo.users.add(email);
                        GetUserInfoFromFirebase();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(TripViewActivity.this,"Error connecting to the database: "+e.toString(),Toast.LENGTH_LONG).show();
                    }
                });
    }

    void GetExpensesFromFirebase(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference usersRef = db.collection("expenses");

        usersRef.whereEqualTo("tripID", tripInfo.tripID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map<String, Object> expense = document.getData();
                                String amount = (String) expense.get("amount");
                                int _amount = 0;
                                try{
                                    _amount = Integer.parseInt(amount);

                                }
                                catch (NumberFormatException ex){
                                    ex.printStackTrace();
                                }
                                String date = (String) expense.get("date");
                                String description = (String) expense.get("description");
                                List<HashMap<String,Object>> _payers = (List<HashMap<String,Object>>) expense.get("payers");


                                ArrayList<PayerInfo> payers = new ArrayList<PayerInfo>();
                                for(int i = 0; i < _payers.size(); i++){
                                    Long aux_amount =  (Long) _payers.get(i).get("amount");
                                    String aux_email =  (String) _payers.get(i).get("email");
                                    String aux_name =  (String) _payers.get(i).get("name");
                                    String aux_image_url =  (String) _payers.get(i).get("image_url");
                                    //public PayerInfo(String image, String name, String email, int amount){
                                    payers.add(new PayerInfo(aux_image_url, aux_name, aux_email, aux_amount.intValue()));

                                }

                                ExpenseInfo newExpense = new ExpenseInfo(document.getId(), description,date,_amount,payers);
                                myListData.add(newExpense);
                            }
                            expense_adapter.notifyDataSetChanged();
                        }
                    }
                });
    }

    public void DeleteTripFromFirebase(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("trips").document(tripInfo.tripID)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        DeleteImageFromFirebase();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toast.makeText(TripViewActivity.this,"Error trying to delete trip. Retry",Toast.LENGTH_LONG).show();
                    }
                });
    }
    public void DeleteExpensesOfTripFromFirebase() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference usersRef = db.collection("expenses");

        usersRef.whereEqualTo("tripID", tripInfo.tripID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            numExpenses = task.getResult().size();
                            if (numExpenses == 0){
                                Toast.makeText(TripViewActivity.this,"Trip deleted successfully",Toast.LENGTH_LONG).show();
                                finish();
                            }else{
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    RemoveExpense(document.getId());
                                }
                            }

                        }
                    }
                });
    }

    public void DeleteImageFromFirebase(){

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference imgRef = storageRef.child("images/trips/"+tripInfo.tripID+".jpg");


        // Delete the file
        imgRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // File deleted successfully
                DeleteExpensesOfTripFromFirebase();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Uh-oh, an error occurred!
                Toast.makeText(TripViewActivity.this,"Error deleting image from trip",Toast.LENGTH_LONG).show();
                DeleteExpensesOfTripFromFirebase();
            }
        });
    }

    public void RemoveExpense(String expenseID){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("expenses").document(expenseID)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        numExpenses--;
                        if (numExpenses == 0){
                            Toast.makeText(TripViewActivity.this,"Trip deleted successfully",Toast.LENGTH_LONG).show();
                            finish();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toast.makeText(TripViewActivity.this,"Error trying to delete trip. Retry",Toast.LENGTH_LONG).show();
                    }
                });
    }
}