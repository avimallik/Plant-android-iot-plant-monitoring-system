package com.armavi.plant;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Pattern;

public class LogIn extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Intent next_activity;
    EditText systemIptxt;
    Button loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        ///////////Activity State Check////////////////
        next_activity = new Intent(getApplicationContext(), Panel.class);
        final SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.user_pref),MODE_PRIVATE);
        final Boolean isloggedin = sharedPreferences.getBoolean(getString(R.string.ISLOGGEDIN),false);

        if(isloggedin){
            Intent main = new Intent(LogIn.this, Panel.class);
            startActivity(main);
        }
        //////////////////////////////////

        systemIptxt = (EditText) findViewById(R.id.ip_text_system);
        loginBtn = (Button) findViewById(R.id.login_btn);

        //Regular Expression of IP initialization
        String zeroTo255
                = "(\\d{1,2}|(0|1)\\"
                + "d{2}|2[0-4]\\d|25[0-5])";
        String regex
                = zeroTo255 + "\\."
                + zeroTo255 + "\\."
                + zeroTo255 + "\\."
                + zeroTo255;

        Pattern ipPattern = Pattern.compile(regex);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(ipPattern.matcher(systemIptxt.getText().toString()).matches()){
                    ipSave();
                }else {
                    Toast.makeText(getApplicationContext(), "Enter a Valid IP!", Toast.LENGTH_SHORT).show();
                }

                //Dummy server check
//                if(systemIptxt.getText().toString()!=""){
//                    ipSave();
//                }else {
//                    Toast.makeText(getApplicationContext(), "Enter a Valid IP!", Toast.LENGTH_SHORT).show();
//                }

            }
        });

    }

    public void ipSave(){
        sharedPreferences = getSharedPreferences(getString(R.string.user_pref),MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putString(getString(R.string.user_pref_system_ip), systemIptxt.getText().toString());
        editor.putBoolean(getString(R.string.ISLOGGEDIN),true);
        editor.putString(getString(R.string.ISRECOG),"user_login");
        editor.commit();
        startActivity(next_activity);
    }

}