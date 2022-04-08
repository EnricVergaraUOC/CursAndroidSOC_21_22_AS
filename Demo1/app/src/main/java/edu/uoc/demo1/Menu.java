package edu.uoc.demo1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.*;

import java.io.IOException;
import java.util.Date;
public class Menu extends AppCompatActivity {


    public final static String AI_ENABLED = "AI_ENABLED";





    private ImageView imgTitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        Expense expense = new Expense("rest. ca la pagesa", 100, "2010/2/1");
        expense.AddNewPayer("Enric", 50);
        expense.AddNewPayer("Jose Miguel", 50);


        User user1 = new User("Enric", "evergaraca@uoc.edu", null);
        User user2 = new User("Jose Miguel", "jm@uoc.edu", null);
        Trip newTrip = new Trip(null,"2010/1/1","la volta al món");
        newTrip.AddNewUser(user1);
        newTrip.AddNewUser(user2);
        newTrip.AddNewExpense(expense);

        ObjectMapper mapper = new ObjectMapper();
        String jsonStr = null;
        try {
            jsonStr = mapper.writeValueAsString(newTrip);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }


        try {
            Trip tripAux = mapper.readValue(jsonStr, Trip.class);
            String kk = "";
        } catch (IOException e) {
            e.printStackTrace();
        }

        Button register = findViewById(R.id.goRgister);
        Button tresEnRaya = findViewById(R.id.go3EnRaya);
        Switch switchIA = findViewById(R.id.switchIA);
        imgTitle = findViewById(R.id.imgTitle);
        //imgTitle.setImageResource(R.drawable.tictactoe_title);
        switchIA.setChecked(false);

        EditText idUser = findViewById(R.id.edit_text_user_id);
        Button create_user = findViewById(R.id.btn_create_user);
        create_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int userID = 0;
                try {
                    userID = Integer.parseInt(idUser.getText().toString());
                }
                catch(NumberFormatException e){
                    Toast.makeText(getApplicationContext(),
                            "El user id debe ser numérico!", Toast.LENGTH_SHORT)
                            .show();
                }
                Intent intent = new Intent(Menu.this, UserProfile.class);
                intent.putExtra(UserProfile.NEW_USER, true);
                intent.putExtra(UserProfile.EDIT_MODE, true);
                intent.putExtra(UserProfile.USER_ID, userID);
                startActivity(intent);
            }
        });

        Button view_user = findViewById(R.id.btn_view_user);
        view_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int userID = 0;
                try {
                    userID = Integer.parseInt(idUser.getText().toString());
                }
                catch(NumberFormatException e){
                    Toast.makeText(getApplicationContext(),
                            "El user id debe ser numérico!", Toast.LENGTH_SHORT)
                            .show();
                }
                Intent intent = new Intent(Menu.this, UserProfile.class);
                intent.putExtra(UserProfile.NEW_USER, false);
                intent.putExtra(UserProfile.EDIT_MODE, false);
                intent.putExtra(UserProfile.USER_ID, userID);
                startActivity(intent);
            }
        });

        Button edit_user = findViewById(R.id.btn_edit_user);
        edit_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int userID = 0;
                try {
                    userID = Integer.parseInt(idUser.getText().toString());
                }
                catch(NumberFormatException e){
                    Toast.makeText(getApplicationContext(),
                            "El user id debe ser numérico!", Toast.LENGTH_SHORT)
                            .show();
                }
                Intent intent = new Intent(Menu.this, UserProfile.class);
                intent.putExtra(UserProfile.NEW_USER, false);
                intent.putExtra(UserProfile.EDIT_MODE, true);
                intent.putExtra(UserProfile.USER_ID, userID);
                startActivity(intent);
            }
        });



        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Menu.this, MainActivity.class);
                startActivity(intent);
            }
        });
        tresEnRaya.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Menu.this, TresEnRaya.class);
                intent.putExtra(AI_ENABLED, switchIA.isChecked());
                startActivity(intent);

            }
        });


    }



}