package com.p3.printedpost;

//import com.p3.printedpost.util.SystemUiHider;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 *

 */
public class ScanActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        Intent intent = new Intent("com.google.zxing.client.android.SCAN");
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        startActivityForResult(intent, 0);
    }

    public void onActivityResult(int requestCode,int resultCode,Intent intent){
        if (resultCode == RESULT_OK) {
            String code = intent.getStringExtra("SCAN_RESULT");
            String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
            // faz o que quiser aqui com a string lida

            Toast.makeText(getApplicationContext(), (code),
                    Toast.LENGTH_LONG).show();

        } else if (resultCode == RESULT_CANCELED) {
            // deu merda
            Toast.makeText(getApplicationContext(), "ERROR!",
                    Toast.LENGTH_LONG).show();
        }
    }

}
