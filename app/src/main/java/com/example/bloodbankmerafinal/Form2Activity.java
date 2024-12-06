package com.example.bloodbankmerafinal;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class Form2Activity extends AppCompatActivity {

    //declaring components from xml file
    private Button yesButton, noButton;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.request_test2); //connecting with form 2 xml file

        //initializing components from xml file
        yesButton=findViewById(R.id.button3);
        noButton=findViewById(R.id.button4);
        sharedPreferences=getSharedPreferences("FormAnswers", MODE_PRIVATE);

        yesButton.setOnClickListener(view->
        {
            saveAnswer("form2", "YES"); //calling the func that will save this answer for later use
            navigateToForm3(); //this func will take us to the other form
        });

        noButton.setOnClickListener(view->
        {
            saveAnswer("form2", "NO"); //even if user presses no, it should move to second form
            navigateToForm3();
        });
    }

    private void saveAnswer(String formKey, String ans)
    {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(formKey, ans);
        editor.apply();
    }

    private void navigateToForm3()
    {
        Intent intent=new Intent(Form2Activity.this, Form3Activity.class);
        startActivity(intent);
        finish();
    }
}
