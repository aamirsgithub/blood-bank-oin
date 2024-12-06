package com.example.bloodbankmerafinal;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class Form3Activity extends AppCompatActivity {

    //declaring components from xml file
    private Button yesButton, noButton;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.request_test3); //connecting with form 3 xml file

        //initializing components from xml file
        yesButton=findViewById(R.id.button3);
        noButton=findViewById(R.id.button4);
        sharedPreferences=getSharedPreferences("FormAnswers", MODE_PRIVATE);

        yesButton.setOnClickListener(view->
        {
            saveAnswer("form3", "YES"); //calling the func that will save this answer for later use
            navigateToForm4(); //this func will take us to the other form
        });

        noButton.setOnClickListener(view->
        {
            saveAnswer("form3", "NO"); //even if user presses no, it should move to second form
            navigateToForm4();
        });
    }

    private void saveAnswer(String formKey, String ans)
    {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(formKey, ans);
        editor.apply();
    }

    private void navigateToForm4()
    {
        Intent intent=new Intent(Form3Activity.this, Form4Activity.class);
        startActivity(intent);
        finish();
    }
}
