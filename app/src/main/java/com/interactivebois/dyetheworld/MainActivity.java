package com.interactivebois.dyetheworld;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.maps.android.SphericalUtil;
import com.interactivebois.dyetheworld.HTTP.GetPlayer;
import com.interactivebois.dyetheworld.HTTP.PostPolygon;

public class MainActivity extends AppCompatActivity implements AsyncResponse
{
    private static final String TAG = "MainActivity";

    private static final int ERROR_DIALOG_REQUEST = 9001;

    private  boolean debug = false;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(isServicesOk())
        {
            init();
        }
    }

    private void init(){
        Button btnMap = (Button) findViewById(R.id.btnLogin);
        btnMap.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (debug)
                {
                    Intent intent = new Intent(MainActivity.this, MapActivity.class);
                    startActivity(intent);
                }
                else
                {
                    EditText email = findViewById(R.id.emailInput);
                    EditText password = findViewById(R.id.passwordInput);

                    new GetPlayer(MainActivity.this, email.getText().toString(), password.getText().toString()).execute();
                }
            }
        });
    }

    public boolean isServicesOk(){
        Log.d(TAG, "isServicesOk: checking google services version");

        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(MainActivity.this);

        if (available == ConnectionResult.SUCCESS)
        {
            Log.d(TAG, "isServicesOk: Google Play Services is working");
            return true;
        }
        else if (GoogleApiAvailability.getInstance().isUserResolvableError(available))
        {
            Log.d(TAG, "isServicesOk: An error occured but we can fix it");

            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(MainActivity.this, available, ERROR_DIALOG_REQUEST);
            dialog.show();
        }
        else
        {
            Toast.makeText(this, "Can't Make Map Request", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    @Override
    public void processFinish()
    {
        Intent intent = new Intent(MainActivity.this, MapActivity.class);
        startActivity(intent);
    }
}
