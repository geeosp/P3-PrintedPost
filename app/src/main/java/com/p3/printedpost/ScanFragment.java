package com.p3.printedpost;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.google.zxing.ResultPoint;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.CompoundBarcodeView;

import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class ScanFragment extends Fragment {
    private CompoundBarcodeView barcodeView;
    private RelativeLayout ll;
    private FragmentActivity fa;

    public ScanFragment() {
    }

    private BarcodeCallback callback = new BarcodeCallback() {
        @Override
        public void barcodeResult(BarcodeResult result) {
            if (result.getText() != null) {
                // colocar na tela a msg lida
                barcodeView.setStatusText(result.getText());
                Log.v("log", "SCANNER: " + result.getText());
                // aqui eh onde pega o texto
            }
        }

        @Override
        public void possibleResultPoints(List<ResultPoint> resultPoints) {
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        fa = super.getActivity();
        return (RelativeLayout)inflater.inflate(R.layout.fragment_scan, container, false);


    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Log.v("TESTE", "Initializing sounds...");

        barcodeView = (CompoundBarcodeView) getView().findViewById(R.id.barcode_scanner);
        barcodeView.setStatusText(getString(R.string.reader_promt));
        barcodeView.decodeContinuous(callback);




    }




    private boolean onKeyDown(int keyCode, KeyEvent event) {
        return barcodeView.onKeyDown(keyCode, event) || fa.onKeyDown(keyCode, event);
    }

    public void previousActivity(View view) {
        Log.v("log", "clicou");
    };


}
