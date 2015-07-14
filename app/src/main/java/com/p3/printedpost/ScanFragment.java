package com.p3.printedpost;

import android.os.Bundle;
import android.support.v4.app.Fragment;
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
        public void barcodeResult(final BarcodeResult result) {
            if (result.getText() != null) {
                // colocar na tela a msg lida
                //barcodeView.setStatusText(result.getText());

                ((SwipeActivity) getActivity()).seek(result.getText(), ScanFragment.this);

                Log.e("log", "SCANNER: " + result.getText());
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
    public void onStart() {
        super.onStart();
        resume();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (barcodeView != null)
            barcodeView.pause();

        Log.e("Scan", "Paused");
    }

    public void pause() {
        if (barcodeView != null)
            barcodeView.pause();

        Log.e("Scan", "Paused");
    }

    public void resume() {
        if (barcodeView != null)
            barcodeView.resume();
        Log.e("Scan", "Resumed");
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return barcodeView.onKeyDown(keyCode, event) || getActivity().onKeyDown(keyCode, event);
    }


}