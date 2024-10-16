package com.armavi.plant;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.media.MediaPlayer;
import android.os.Bundle;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.armavi.plant.model.URL_Fractor;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.core.view.WindowCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.armavi.plant.databinding.ActivityPanelBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Pattern;

public class Panel extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    URL_Fractor url_fractor;
    MediaPlayer alertDetection;
    private DrawerLayout drawerLayout;
    TextView moistureTxt, moistureStatusTxt, humidityTxt, temperatureTxt, alarmLevelTxt;
    ImageView logoutBtn;
    LinearProgressIndicator moistureProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_panel);

        //NukeSSL
        NukeSSLCerts.nuke();

        //Shared pref
        sharedPreferences = getSharedPreferences(getString(R.string.user_pref),MODE_PRIVATE);
        editor = sharedPreferences.edit();

        //Alert
        alertDetection = MediaPlayer.create(getApplicationContext(), R.raw.alarm_r_1);

        //URL Factoring
        url_fractor = new URL_Fractor();

        //UI initialization
        drawerLayout = findViewById(R.id.nav_view);
        Toolbar toolbar = findViewById(R.id.toolbar);
        moistureTxt = (TextView) findViewById(R.id.moisture_txt);
        moistureStatusTxt = (TextView) findViewById(R.id.moisture_status_txt);
        humidityTxt = (TextView) findViewById(R.id.humidity_txt);
        temperatureTxt = (TextView) findViewById(R.id.temperature_txt);
        alarmLevelTxt = (TextView) findViewById(R.id.alarm_level_txt);
        moistureProgress = (LinearProgressIndicator) findViewById(R.id.moistureProgress);
        logoutBtn = (ImageView) findViewById(R.id.logout_btn);

        //The level of alarm will be set when the activity is active.
        alarmLevelTxt.setText(String.valueOf(sharedPreferences.getInt(getString(R.string.user_pref_threshold), 0))
                +"%"+" Alarm level");

        NavigationView navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.stop_alarm) {
                    alertDetection.pause();
                }else if (id == R.id.add_threshold) {
                    alarmLevelSaveDialog(Panel.this, "Dialog");
                }

                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        //clear sharedprefs
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedPreferences.edit().clear().commit();
                Intent intentFinish = new Intent(getApplicationContext(), LogIn.class);
                startActivity(intentFinish);
                finish();
            }
        });

        //Thread of refreshing activity every 2000 milliseconds
        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(2000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                plantMonitoringDataFetch();
                            }
                        });
                    }
                } catch (InterruptedException e) {
                }
            }
        };
        thread.start();

    }

    //Level of alarm input from dialog
    public void alarmLevelSaveDialog(Activity activity, String msg){
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_threshold);

        TextView waterLevelAlarmTxtDiag = (TextView) dialog.findViewById(R.id.water_level_alarm_txt_diag);
        Button saveLevelBtnDiag = (Button) dialog.findViewById(R.id.save_level_diag_btn);
        Button cancelBtnDiag = (Button) dialog.findViewById(R.id.cancel);

        //Alarm level input regular expression;
        String digitRegEx = "[0-9]+";
        Pattern pattern = Pattern.compile(digitRegEx);

        saveLevelBtnDiag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pattern.matcher(waterLevelAlarmTxtDiag.getText().toString()).matches()){
                    int alarmVal = Integer.parseInt(waterLevelAlarmTxtDiag.getText().toString().trim());
                    editor.putInt(getString(R.string.user_pref_threshold), alarmVal);
                    editor.apply();
                    editor.commit();
                    dialog.dismiss();
                    alarmLevelTxt.setText(String.valueOf(sharedPreferences.getInt(getString(R.string.user_pref_threshold), 0))
                            +"%"+" Alarm level");
                }else{
                    Toast.makeText(getApplicationContext(), "Please type valid alarm level only in digit!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        cancelBtnDiag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    //Plant monitoring function
    void plantMonitoringDataFetch(){

        //Webserver url
        String systemIpTemp = sharedPreferences.getString(getString(R.string.user_pref_system_ip),"");
        String webServerUri = url_fractor.getHttp_() + systemIpTemp;

        RequestQueue queue = Volley.newRequestQueue(Panel.this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                webServerUri,
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    //water percentage JSON Object
                    String moistureVal = response.getString("moisture_percentage");
                    int moistureValInt = Integer.parseInt(moistureVal);
                    String humidityVal = response.getString("humidity");
                    String temperatureVal = response.getString("temperature");

                    //Setting JSON values in views
                    moistureTxt.setText(moistureVal+"%");
                    humidityTxt.setText(humidityVal+"%");
                    temperatureTxt.setText(temperatureVal+"°");

                    //Progress
                    moistureProgress.setProgress(moistureValInt);

                    //Plant condition check
                    int moistureCompareVal = sharedPreferences.getInt(getString(R.string.user_pref_threshold), 20);

                    int moistureTwiceVal = moistureCompareVal*2;
                    int moistureOneAndHalf = (int) (moistureCompareVal*1.5);

                    if(moistureValInt < moistureCompareVal){
                        alertDetection.start();
                        alertDetection.setLooping(true);
                        moistureTxt.setTextColor(getResources().getColor(R.color.warning));
                        moistureStatusTxt.setText("Dry soil needs watering");
                        moistureProgress.setIndicatorColor(getResources().getColor(R.color.warning));

                    }else if(moistureValInt <= moistureOneAndHalf){
                        if(alertDetection.isPlaying()) {
                            alertDetection.pause();
                        }
                        moistureTxt.setTextColor(getResources().getColor(R.color.param_color));
                        moistureProgress.setIndicatorColor(getResources().getColor(R.color.purple_700));
                        moistureStatusTxt.setText("Average water condition");

                    } else if(moistureValInt >= moistureTwiceVal) {
                        if(alertDetection.isPlaying()) {
                            alertDetection.pause();
                        }
                        moistureTxt.setTextColor(getResources().getColor(R.color.param_color));
                        moistureProgress.setIndicatorColor(getResources().getColor(R.color.purple_700));
                        moistureStatusTxt.setText("Look fresh");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // below line is use to display a toast message along with our error.
                Log.e("TAG", String.valueOf(error));
                if (error instanceof NetworkError) {
                } else if (error instanceof ServerError) {
                } else if (error instanceof AuthFailureError) {
                } else if (error instanceof ParseError) {
                } else if (error instanceof NoConnectionError) {
                } else if (error instanceof TimeoutError) {
                    Log.e("TAG", String.valueOf(error));
                }
            }
        });
        queue.add(jsonObjectRequest);
    }

    public void alertChecking(Activity activity){
        final MediaPlayer moistureProgress  = new MediaPlayer();
        if(moistureProgress.isPlaying()) {
            moistureProgress.pause();
        }
    }

    @Override
    public void onBackPressed()
    {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer((GravityCompat.START));
        }else {
            super.onBackPressed();
        }
    }


}