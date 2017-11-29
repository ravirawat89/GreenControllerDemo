package com.netcommlabs.greencontroller.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.zxing.Result;
import com.netcommlabs.greencontroller.R;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class QRScanAct extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    private ZXingScannerView zXingScannerView;
    private int REQUEST_CODE = 1001;
    private QRScanAct mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        zXingScannerView = new ZXingScannerView(mContext);
        setContentView(zXingScannerView);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            zxingQRInitiateCamera();
            //Toast.makeText(mContext, "Do nothing, Camera permission already granted", Toast.LENGTH_SHORT).show();
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                ActivityCompat.requestPermissions(mContext, new String[]{Manifest.permission.CAMERA}, REQUEST_CODE);
            }
        }
    }


    private void zxingQRInitiateCamera() {
        //mContext.setContentView(zXingScannerView);
        zXingScannerView.setResultHandler(mContext);
        zXingScannerView.startCamera();
    }

    @Override
    public void handleResult(Result result) {
        Intent intent=new Intent();
        intent.putExtra("MESSAGE",result.getText());
        setResult(2,intent);
        finish();//finishing activity
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                zxingQRInitiateCamera();
            } else {
                Toast.makeText(mContext, "Granting this permission is mandatory", Toast.LENGTH_LONG).show();
                mContext.finish();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        zXingScannerView.stopCamera();
    }

}
