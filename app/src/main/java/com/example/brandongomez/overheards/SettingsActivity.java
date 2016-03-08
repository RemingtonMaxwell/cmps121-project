package com.example.brandongomez.overheards;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
/**
 * Created by Darion on 3/5/2016.
 */
public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

    }

    public void updateInfo(View v){
        EditText firstNameEtext = (EditText)findViewById(R.id.firstNameEtext);
        EditText lastNameEtext = (EditText)findViewById(R.id.lastNameEtext);
        EditText userNameEtext = (EditText)findViewById(R.id.userNameEtext);
        EditText emailEtext = (EditText)findViewById(R.id.emailEtext);

        TextView firstNameText = (TextView)findViewById(R.id.firstNameText);
        TextView lastNameText = (TextView)findViewById(R.id.lastNameText);
        TextView userNameText = (TextView)findViewById(R.id.userNameText);
        TextView emailText = (TextView)findViewById(R.id.emailText);

        //set new user settings by getting text that user entered from editText fields
        firstNameText.setText(firstNameEtext.getText().toString());
        lastNameText.setText(lastNameEtext.getText().toString());
        userNameText.setText(userNameEtext.getText().toString());
        emailText.setText(emailEtext.getText().toString());

        //CALL TO DATABASE TO CHANGE SETTINGS GOES HERE

    }

}
