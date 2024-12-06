package com.example.bloodbankmerafinal;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;


public class Form1Activity extends AppCompatActivity {

    //declaring components from xml file
    private Button yesButton, noButton;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.request_test1); //connecting with form 1 xml file

        //initializing components from xml file
        yesButton=findViewById(R.id.button3);
        noButton=findViewById(R.id.button4);
        sharedPreferences=getSharedPreferences("FormAnswers", MODE_PRIVATE);

        yesButton.setOnClickListener(view->
        {
            saveAnswer("form1", "YES"); //calling the func that will save this answer for later use
            navigateToForm2(); //this func will take us to the other form
        });

        noButton.setOnClickListener(view->
        {
            saveAnswer("form1", "NO"); //even if user presses no, it should move to second form
            navigateToForm2();
        });
    }

    private void saveAnswer(String formKey, String ans)
    {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(formKey, ans);
        editor.apply();
    }

    private void navigateToForm2()
    {
        Intent intent=new Intent(Form1Activity.this, Form2Activity.class);
        startActivity(intent);
        finish();
    }
}
