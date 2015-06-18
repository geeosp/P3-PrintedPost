package com.p3.printedpost;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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


    public ScanFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {




        return inflater.inflate(R.layout.fragment_scan, container, false);


    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        barcodeView = (CompoundBarcodeView) getView().findViewById(R.id.barcode_scanner);
        barcodeView.setStatusText(getString(R.string.reader_promt));
        barcodeView.decodeContinuous(callback);

    }
    @Override
    public void onResume() {
        super.onResume();

        barcodeView.resume();
    }

    @Override
    public void onPause() {
        super.onPause();

        barcodeView.pause();
    }

    public void pause(View view) {
        barcodeView.pause();
    }

    public void resume(View view) {
        barcodeView.resume();
    }



    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return barcodeView.onKeyDown(keyCode, event) || getActivity().onKeyDown(keyCode, event);
    }

    public void previousActivity(View view) {
        Log.v("log", "clicou");
    }


}