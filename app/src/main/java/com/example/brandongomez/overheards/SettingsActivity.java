package com.example.brandongomez.overheards;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import android.content.Context;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;
import android.widget.ProgressBar;

/**
 * Created by Darion on 3/5/2016.
 */
public class SettingsActivity extends AppCompatActivity {
    protected View mView;
    final Context context = this;
    ProgressBar spinner;
    String email;
    String pass;
    String first_name;
    String last_name;
    String user_name;

    //SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
    //final SharedPreferences.Editor e = settings.edit();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        View v = this.findViewById(R.id.content);
        fillSettings(v);
        //SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);

    }

    /*public void onViewCreated(View view, Bundle savedInstanceState) {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        fillSettings(view);

    }*/

    /*@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_five, container, false);
        mView=view;
        fillSettings();
        return view;
    }*/

    public void fillSettings(View v){
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        //final SharedPreferences.Editor e = settings.edit();
        final String pass_word = settings.getString("password", null);
        /*e.putString("email",null);
        e.putString("password",null);
        e.putString("user_id", null);
        e.commit();*/
        final Bundle b = getIntent().getExtras();
        Log.i("Fifth Fragment", "settings");
        String userId = b.getString("user_id");
        Firebase database = new Firebase("https://vivid-heat-3338.firebaseio.com/users/"+userId);
        // Attach an listener to read the data at our posts reference
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                //first name
                ((TextView) findViewById(R.id.firstNameText)).setText((String) snapshot.child("firstName").getValue());

                 //Log.i("overheards", (String) snapshot.child("firstName").getValue());
                //last name
                ((TextView) findViewById(R.id.lastNameText)).setText((String) snapshot.child("lastName").getValue());
                //user name
                ((TextView) findViewById(R.id.userNameText)).setText((String) snapshot.child("userName").getValue());
                //email address
                ((TextView) findViewById(R.id.emailText)).setText((String) snapshot.child("emailAddress").getValue());
                //password
                ((TextView) findViewById(R.id.passwordText)).setText(pass_word);

                /*email = (String) snapshot.child("emailAddress").getValue();
                pass = b.getString("password");
                first_name = (String) snapshot.child("firstName").getValue();
                last_name = (String) snapshot.child("lastName").getValue();
                user_name = (String) snapshot.child("userName").getValue();*/
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });
    }

    public void updateFirstName(View v) {
        final Firebase database = new Firebase("https://vivid-heat-3338.firebaseio.com/users");
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Firebase ref = database.child(getIntent().getExtras().getString("user_id"));
                Map<String, Object> updateSettings = new HashMap<String, Object>();
                EditText firstName = (EditText) findViewById(R.id.firstNameEtext);
                updateSettings.put("firstName", firstName.getText().toString());
                ref.updateChildren(updateSettings);
               // Toast.makeText(getApplicationContext(), "Updated!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    public void updateLastName(View v){
        final Firebase database = new Firebase("https://vivid-heat-3338.firebaseio.com/users");
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Firebase ref = database.child(getIntent().getExtras().getString("user_id"));
                Map<String, Object> updateSettings = new HashMap<String, Object>();
                EditText lastName=(EditText)findViewById(R.id.lastNameEtext);
                updateSettings.put("lastName", lastName.getText().toString());
                ref.updateChildren(updateSettings);
                //Toast.makeText(getApplicationContext(), "Updated!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    public void updateUserName(View v){
        final Firebase database = new Firebase("https://vivid-heat-3338.firebaseio.com/users");
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Firebase ref = database.child(getIntent().getExtras().getString("user_id"));
                Map<String, Object> updateSettings = new HashMap<String, Object>();
                EditText userName=(EditText)findViewById(R.id.userNameEtext);
                updateSettings.put("userName", userName.getText().toString());
                ref.updateChildren(updateSettings);
                //Toast.makeText(getApplicationContext(), "Updated!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    public void updateEmailAddress(View v){
        final Firebase database = new Firebase("https://vivid-heat-3338.firebaseio.com/users");
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                final Firebase ref = database.child(getIntent().getExtras().getString("user_id"));
                Map<String, Object> updateSettings = new HashMap<String, Object>();
                final EditText emailAddress = (EditText) findViewById(R.id.emailEtext);
                updateSettings.put("emailAddress", emailAddress.getText().toString());
                Firebase refUser = new Firebase("https://vivid-heat-3338.firebaseio.com/users/" + getIntent().getStringExtra(LoginActivity.USER_ID));
                updateUserEmailAddress();
                //send updates to server
                ref.updateChildren(updateSettings);
                //Toast.makeText(getApplicationContext(), "Updated!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    public void updateUserEmailAddress(){
        //get the old email and password
        final Firebase database = new Firebase("https://vivid-heat-3338.firebaseio.com/users");
        final Firebase ref = database.child(getIntent().getExtras().getString("user_id"));
        final EditText emailAddress=(EditText)findViewById(R.id.emailEtext);
        ref.changeEmail(getIntent().getStringExtra(LoginActivity.EMAIL), getIntent().getStringExtra(LoginActivity.PASSWORD), emailAddress.getText().toString(), new Firebase.ResultHandler() {
            @Override
            public void onSuccess() {
                // email changed
                System.out.println("email changed");
            }

            @Override
            public void onError(FirebaseError firebaseError) {
                // error encountered
            }
        });
    }
    public void updatePassword(View v) {
        final Firebase ref = new Firebase("https://vivid-heat-3338.firebaseio.com");
        final EditText password = (EditText) findViewById(R.id.passwordEtext);
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        final SharedPreferences.Editor e = settings.edit();
        e.putString("password",password.getText().toString());
        e.commit();
        ref.changePassword(getIntent().getExtras().getString("email"), getIntent().getExtras().getString("password"), password.getText().toString(), new Firebase.ResultHandler()
        {
            @Override
            public void onSuccess() {

                //Toast.makeText(getApplicationContext(), "Updated!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FirebaseError firebaseError) {
                // error encountered
            }
        });
    }

    public void updateInfo(View v){
        /*spinner = (ProgressBar) findViewById(R.id.progressBar1);
        spinner.setVisibility(v.VISIBLE);*/

        EditText firstNameEtext = (EditText)findViewById(R.id.firstNameEtext);
        EditText lastNameEtext = (EditText)findViewById(R.id.lastNameEtext);
        EditText userNameEtext = (EditText)findViewById(R.id.userNameEtext);
        EditText emailEtext = (EditText)findViewById(R.id.emailEtext);
        EditText passwordEtext = (EditText)findViewById(R.id.passwordEtext);

        TextView firstNameText = (TextView)findViewById(R.id.firstNameText);
        TextView lastNameText = (TextView)findViewById(R.id.lastNameText);
        TextView userNameText = (TextView)findViewById(R.id.userNameText);
        TextView emailText = (TextView)findViewById(R.id.emailText);
        TextView passwordText = (TextView)findViewById(R.id.passwordText);

        TextView txt2 = (TextView)findViewById(R.id.textView2);
        TextView txt3 = (TextView)findViewById(R.id.textView3);
        TextView txt4 = (TextView)findViewById(R.id.textView4);
        TextView txt5 = (TextView)findViewById(R.id.textView5);
        TextView txt6 = (TextView)findViewById(R.id.textView6);

        /*firstNameText.setVisibility(v.INVISIBLE);
        lastNameText.setVisibility(v.INVISIBLE);
        userNameText.setVisibility(v.INVISIBLE);
        emailText.setVisibility(v.INVISIBLE);
        passwordText.setVisibility(v.INVISIBLE);

        txt2.setVisibility(v.INVISIBLE);
        txt3.setVisibility(v.INVISIBLE);
        txt4.setVisibility(v.INVISIBLE);
        txt5.setVisibility(v.INVISIBLE);
        txt6.setVisibility(v.INVISIBLE);*/




        //set new user settings by getting text that user entered from editText fields
        if(!(firstNameEtext.getText().toString().equals(""))) {
            firstNameText.setText(firstNameEtext.getText().toString());
            //first_name = firstNameText.getText().toString();
            updateFirstName(v);
        }
        if(!(lastNameEtext.getText().toString().equals(""))) {
            lastNameText.setText(lastNameEtext.getText().toString());
            //last_name = lastNameText.getText().toString();
            updateLastName(v);
        }
        if(!(userNameEtext.getText().toString().equals(""))) {
            userNameText.setText(userNameEtext.getText().toString());
            System.out.println("username is " + userNameEtext.getText().toString());
            //user_name = userNameText.getText().toString();
            updateUserName(v);
        }
        if(!(emailEtext.getText().toString().equals(""))) {
            emailText.setText(emailEtext.getText().toString());
            updateEmailAddress(v);
            //email = emailText.getText().toString();
        }
        if(!(passwordEtext.getText().toString().equals(""))) {
            passwordText.setText(passwordEtext.getText().toString());
            updatePassword(v);
            //pass = passwordText.getText().toString();
        }

        Toast.makeText(getApplicationContext(), "Updated!", Toast.LENGTH_SHORT).show();

        /*if(!(firstNameEtext.getText().toString().equals(""))){
            updateFirstName(v);


        }

        if(!(lastNameEtext.getText().toString().equals(""))){
            updateLastName(v);

        }

        if(!(userNameEtext.getText().toString().equals(""))){
            updateUserName(v);

        }

        if(!(emailEtext.getText().toString().equals(""))){
            updateEmailAddress(v);

        }

        if(!(passwordEtext.getText().toString().equals(""))){
            updatePassword(v);


        }*/

        /*spinner.setVisibility(View.GONE);
        firstNameText.setVisibility(v.VISIBLE);
        lastNameText.setVisibility(v.VISIBLE);
        userNameText.setVisibility(v.VISIBLE);
        emailText.setVisibility(v.VISIBLE);
        passwordText.setVisibility(v.VISIBLE);

        txt2.setVisibility(v.VISIBLE);
        txt3.setVisibility(v.VISIBLE);
        txt4.setVisibility(v.VISIBLE);
        txt5.setVisibility(v.VISIBLE);
        txt6.setVisibility(v.VISIBLE);*/

    }

}
