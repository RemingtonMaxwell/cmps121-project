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
    final Firebase ref = new Firebase("https://vivid-heat-3338.firebaseio.com");



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        View v = this.findViewById(R.id.content);
        fillSettings(v);

    }


    public void fillSettings(View v){
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        //final SharedPreferences.Editor e = settings.edit();
        final String pass_word = settings.getString("password", null);
        final Bundle b = getIntent().getExtras();
        Log.i("Fifth Fragment", "settings");
        String userId = b.getString("user_id");
        Firebase database = new Firebase("https://vivid-heat-3338.firebaseio.com/users/"+userId);
        // Attach an listener to read the data at our posts reference
        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                //first name
                ((TextView) findViewById(R.id.firstNameText)).setText((String) snapshot.child("firstName").getValue());
                //last name
                ((TextView) findViewById(R.id.lastNameText)).setText((String) snapshot.child("lastName").getValue());
                //user name
                ((TextView) findViewById(R.id.userNameText)).setText((String) snapshot.child("userName").getValue());
                //email address
                ((TextView) findViewById(R.id.emailText)).setText((String) snapshot.child("emailAddress").getValue());

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });
    }



    public void updateUserEmailAddress(){
        //get the old email and password
        final Firebase database = new Firebase("https://vivid-heat-3338.firebaseio.com/users");
        final Firebase ref = database.child(getIntent().getExtras().getString("user_id"));
        final EditText emailAddress=(EditText)findViewById(R.id.emailEtext);
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        final SharedPreferences.Editor e = settings.edit();
        e.putString("email",emailAddress.getText().toString());
        e.commit();
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
    public void updatePassword() {
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
            }

            @Override
            public void onError(FirebaseError firebaseError) {
                // error encountered
            }
        });
    }

    public void updateInfo(View v){

        final EditText firstNameEtext = (EditText)findViewById(R.id.firstNameEtext);
        final EditText lastNameEtext = (EditText)findViewById(R.id.lastNameEtext);
        final EditText userNameEtext = (EditText)findViewById(R.id.userNameEtext);
        final EditText emailEtext = (EditText)findViewById(R.id.emailEtext);
        final EditText passwordEtext = (EditText)findViewById(R.id.passwordEtext);

        final TextView firstNameText = (TextView)findViewById(R.id.firstNameText);
        final TextView lastNameText = (TextView)findViewById(R.id.lastNameText);
        final TextView userNameText = (TextView)findViewById(R.id.userNameText);
        final TextView emailText = (TextView)findViewById(R.id.emailText);

        TextView txt2 = (TextView)findViewById(R.id.textView2);
        TextView txt3 = (TextView)findViewById(R.id.textView3);
        TextView txt4 = (TextView)findViewById(R.id.textView4);
        TextView txt5 = (TextView)findViewById(R.id.textView5);

        final Firebase ref = new Firebase("https://vivid-heat-3338.firebaseio.com/users");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Firebase refUpdate = ref.child(getIntent().getExtras().getString("user_id"));
                Map<String, Object> updateSettings = new HashMap<String, Object>();
                EditText userName=(EditText)findViewById(R.id.userNameEtext);
                updateSettings.put("userName", userName.getText().toString());

                if(!(firstNameEtext.getText().toString().equals(""))) {
                    firstNameText.setText(firstNameEtext.getText().toString());
                    updateSettings.put("firstName", firstNameEtext.getText().toString());
                }
                if(!(lastNameEtext.getText().toString().equals(""))) {
                    lastNameText.setText(lastNameEtext.getText().toString());
                    updateSettings.put("lastName", lastNameEtext.getText().toString());

                }
                if(!(userNameEtext.getText().toString().equals(""))) {
                    userNameText.setText(userNameEtext.getText().toString());
                    updateSettings.put("userName", userNameEtext.getText().toString());
                }
                if(!(emailEtext.getText().toString().equals(""))) {
                    emailText.setText(emailEtext.getText().toString());
                    updateSettings.put("emailAddress", emailEtext.getText().toString());
                }
                refUpdate.updateChildren(updateSettings);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        //set new user settings by getting text that user entered from editText fields

        if(!(emailEtext.getText().toString().equals(""))) {
            emailText.setText(emailEtext.getText().toString());
            updateUserEmailAddress();
        }
        if(!(passwordEtext.getText().toString().equals(""))) {
            updatePassword();
        }

        Toast.makeText(getApplicationContext(), "Updated!", Toast.LENGTH_SHORT).show();


    }

}
