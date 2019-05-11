package id.ac.umn.projectuts_00000012802;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private static final String PREFERENCES_FILENAME = "login";
    private static final int PREFERENCES_MODE = Context.MODE_PRIVATE;
    private static final String KEY_USERNAME = "USERNAME";
    private static final String KEY_PASS = "PASS";
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    EditText inputUsername, inputPass;
    Button btnLogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide(); // hide action bar

//      START SHARED PREF
        sharedPreferences = getSharedPreferences(PREFERENCES_FILENAME, PREFERENCES_MODE);
        editor = sharedPreferences.edit();
        editor.putString(KEY_USERNAME, "user");
        editor.putString(KEY_PASS, "useruser");
        editor.apply();
//      END SHARED PREF

//      START GET ID
        inputUsername = findViewById(R.id.inputUsername);
        inputPass = findViewById(R.id.inputPass);
        btnLogin = findViewById(R.id.buttonLogin);
//      END GET ID

//      START LOGIN ONCLICK
        btnLogin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String username = sharedPreferences.getString(KEY_USERNAME, "UNDEFINED");
                String password = sharedPreferences.getString(KEY_PASS, "UNDEFINED");

                if(username.equals(inputUsername.getText().toString()) && password.equals(inputPass.getText().toString())) {
                    Intent intent = new Intent(MainActivity.this, DataActivity.class);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(getApplicationContext(), "Invalid username or password", Toast.LENGTH_SHORT).show();
                }
            }
        });
//      END LOGIN ONCLICK
    }
}
