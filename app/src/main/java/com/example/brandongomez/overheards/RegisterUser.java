package com.example.brandongomez.overheards;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class RegisterUser extends AppCompatActivity {
    private int ACTIVITY_SELECT_IMAGE = 100;
    private String mEmail;
    private String mPassword;
    private boolean validUser;
    private boolean validEmail;
    private double mLatitude;
    private double mLongitude;
    public static final String EMAIL="email";
    public static final String USER_ID="user_id";
    public static final String PASSWORD="password";
    public boolean checkDone=false;
    private String imageFile="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);
        Intent intent=getIntent();
        mEmail=intent.getStringExtra("email");
        mPassword=intent.getStringExtra("password");
        mLatitude=intent.getDoubleExtra("latitude",0);
        mLongitude=intent.getDoubleExtra("longitude",0);
        ((EditText) findViewById(R.id.register_email_address)).setText(mEmail);
        ((EditText) findViewById(R.id.register_password_1)).setText(mPassword);
        System.out.println("in registeruser " + mEmail + " " + mPassword);
    }

    public void addProfilePic(View v){
        Intent i = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, ACTIVITY_SELECT_IMAGE);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        if(resultCode == RESULT_OK && requestCode==ACTIVITY_SELECT_IMAGE){
            Uri selectedImage = imageReturnedIntent.getData();
            ImageView pic =(ImageView) findViewById(R.id.profile_pic);
            pic.setImageURI(selectedImage);
            try {
                Bitmap bmp = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                //bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
                bmp.compress(Bitmap.CompressFormat.JPEG,0,stream);
                bmp.recycle();
                byte[] byteArray = stream.toByteArray();
                imageFile = Base64.encodeToString(byteArray, Base64.DEFAULT);
                Log.i("overheards logging",imageFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public void createAccount(View v) {
        String user_name=((EditText) findViewById(R.id.register_user_name)).getText().toString();
        String email_address=((EditText) findViewById(R.id.register_email_address)).getText().toString();
        validUserName(user_name, email_address);
    }
     public void createUser(final String firstName, final String lastName, final String userName){
        final Firebase database = new Firebase("https://vivid-heat-3338.firebaseio.com/");
         final String email=((EditText) findViewById(R.id.register_email_address)).getText().toString();
         final String password=((EditText) findViewById(R.id.register_password_1)).getText().toString();
         Log.i("overheards", "createUser");
        database.createUser(email, password, new Firebase.ValueResultHandler<Map<String, Object>>() {
            @Override
            public void onSuccess(Map<String, Object> result) {
                Log.i("overheards", "success in creatuser");
                // Simulate network access.
                database.authWithPassword(email, password, new Firebase.AuthResultHandler() {
                    @Override
                    public void onAuthenticated(AuthData authData) {
                        Log.i("overheards", "success in creatuser");
                        callMainActivity(email, authData.getUid(), password, firstName, lastName, userName);
                    }

                    @Override
                    public void onAuthenticationError(FirebaseError firebaseError) {
                        Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                        startActivity(intent);
                    }
                });
                //callMainActivity(mEmail, result.get("uid").toString(),result.g);
            }

            @Override
            public void onError(FirebaseError firebaseError) {
                // there was an error
                Log.i("overheards", "error in creatuser "+ firebaseError);

            }
        });
    }

    public boolean isEmpty (String text){
        return (text.compareTo("")==0);
    }

    public void validUserName(String userName, String userEmail){
        String first_name=((EditText) findViewById(R.id.register_first_name)).getText().toString();
        String last_name=((EditText) findViewById(R.id.register_last_name)).getText().toString();
        String user_name=((EditText) findViewById(R.id.register_user_name)).getText().toString();
        String email_address=((EditText) findViewById(R.id.register_email_address)).getText().toString();
        String password1=((EditText) findViewById(R.id.register_password_1)).getText().toString();
        String password2=((EditText) findViewById(R.id.register_password_2)).getText().toString();
        final Firebase database = new Firebase("https://vivid-heat-3338.firebaseio.com/users");
        final String uName=userName;
        final String uEmail=userEmail;
        Log.i("overheards","uname is "+uName);
        if(checkDone==false) {
            database.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    boolean vUser = true;
                    boolean vEmail = true;
                    /*for (DataSnapshot users : snapshot.getChildren()) {
                        User user = users.getValue(User.class);
                        Log.i("overheards", "user name " + user.getUserName());
                       /* if ((user.getUserName()).compareTo(uName) == 0) {
                            vUser = false;
                        }*/
                       /*if (user.getEmailAddress().compareTo(uEmail) == 0) {
                            vEmail = false;
                        }
                    }*/
                    setValidUser(vUser, vEmail);
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {
                }

            });
            checkDone=true;
        }

    }

    public void setValidUser(boolean isValidUser, boolean isValidEmail){
        String first_name=((EditText) findViewById(R.id.register_first_name)).getText().toString();
        String last_name=((EditText) findViewById(R.id.register_last_name)).getText().toString();
        String user_name=((EditText) findViewById(R.id.register_user_name)).getText().toString();
        String email_address=((EditText) findViewById(R.id.register_email_address)).getText().toString();
        String password1=((EditText) findViewById(R.id.register_password_1)).getText().toString();
        String password2=((EditText) findViewById(R.id.register_password_2)).getText().toString();
        Log.i("overheards", "valid user is"+isValidUser);
        if(isValidUser){
            validUser=true;
        }else{
            validUser=false;
        }
        if(isValidEmail){
            validEmail=true;
        }else{
            validEmail=false;
        }
        Log.i("overheards", "check validuser name " + user_name);
        Log.i("overheards", "check validuser valid " + validUser);
        if(isEmpty(first_name)) {
            Toast.makeText(getApplicationContext(), "Please Enter a First Name", Toast.LENGTH_SHORT).show();
        }else if (isEmpty (last_name)){
            Toast.makeText(getApplicationContext(), "Please Enter a Last Name", Toast.LENGTH_SHORT).show();
        }else if (isEmpty(email_address)||!email_address.contains("@")){
            Toast.makeText(getApplicationContext(), "Please Enter a Valid Email Address.", Toast.LENGTH_SHORT).show();
        }else if (isEmpty(password1)||isEmpty(password2)||!(password1.compareTo(password2)==0)){
            Toast.makeText(getApplicationContext(), "Passwords Do Not Match.", Toast.LENGTH_SHORT).show();
        }else if (validUser==false){
            //Toast.makeText(getApplicationContext(), "User Name is Already in Use.", Toast.LENGTH_SHORT).show();
        } else if (validEmail==false){
            Toast.makeText(getApplicationContext(), "This Email Address is Already in Use.", Toast.LENGTH_SHORT).show();
        }else if (isEmpty(user_name)){
            Toast.makeText(getApplicationContext(), "Please Enter a User Name.", Toast.LENGTH_SHORT).show();
        }else{
            createUser(first_name,last_name,user_name);
        }
    }

    public void callMainActivity(String email, final String user_id, String password,
                                 final String firstName, final String lastName, final String userName){
        final Intent intent = new Intent(this,MainActivity.class);
        intent.putExtra(mEmail,email);
        intent.putExtra(USER_ID,user_id);
        intent.putExtra(PASSWORD,password);
        final String emailAddress=email;
        //save user id in preferences
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        final SharedPreferences.Editor e = settings.edit();
        String id = settings.getString("user_id", null);
        if (id == null) {
            e.putString("user_id", user_id);
            e.commit();
        }
        //fill in user information
        final Firebase database = new Firebase("https://vivid-heat-3338.firebaseio.com/users");
        //first see if user already exists in database
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Log.i("overheards", "new user");
                User user = new User(emailAddress, user_id, new Location(mLatitude,mLongitude),
                                    firstName,lastName, userName,imageFile);
                Firebase ref = database.child(user_id);
                ref.setValue(user);
                startActivity(intent);
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

    }

}
