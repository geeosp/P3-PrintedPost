package com.p3.printedpost;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.appevents.AppEventsLogger;
import com.parse.ParseUser;

import java.util.List;
import java.util.Vector;

import me.dm7.barcodescanner.zbar.BarcodeFormat;
import me.dm7.barcodescanner.zbar.Result;
import me.dm7.barcodescanner.zbar.ZBarScannerView;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Logs 'install' and 'app activate' App Events.
        AppEventsLogger.activateApp(this);
        setContentView(R.layout.activity_main);
        // TextView editText = (TextView) findViewById(R.id.et_hello);
        //editText.setText(ParseUser.getCurrentUser().getEmail());
        final ZBarScannerView mScannerView = (ZBarScannerView) findViewById(R.id.zbarView);
        List<BarcodeFormat> l = new Vector<BarcodeFormat>();
        l.add(BarcodeFormat.QRCODE);
        mScannerView.setFormats(l);
        mScannerView.startCamera();
        View v = findViewById(R.id.v_root);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mScannerView.startCamera();
            }
        });
        mScannerView.setResultHandler(new ZBarScannerView.ResultHandler() {
            @Override
            public void handleResult(Result result) {
                String s = result.getContents();
                Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show();
                mScannerView.startCamera();
            }
        });


    }

    @Override
    protected void onPause() {
        super.onPause();
        AppEventsLogger.deactivateApp(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.action_logout:
                logout();
                return true;

            case R.id.action_settings:
                return true;

        }

        return super.onOptionsItemSelected(item);
    }

    public void logout() {
        ParseUser.logOut();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
