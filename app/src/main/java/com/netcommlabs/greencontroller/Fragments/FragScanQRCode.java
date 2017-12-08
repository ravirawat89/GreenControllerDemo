package com.netcommlabs.greencontroller.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.zxing.Result;
import com.netcommlabs.greencontroller.R;
import com.netcommlabs.greencontroller.activities.MainActivity;
import com.netcommlabs.greencontroller.activities.QRScanAct;
import com.netcommlabs.greencontroller.utilities.Constant;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

/**
 * Created by Android on 12/6/2017.
 */

public class FragScanQRCode extends Fragment implements ZXingScannerView.ResultHandler  {
    private MainActivity mContext;
    private View view;
    private TextView tvAddNewDvc;

    private ZXingScannerView zXingScannerView;
    private int REQUEST_CODE = 1001;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = (MainActivity) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        zXingScannerView = new ZXingScannerView(mContext);
        view = inflater.inflate(R.layout.dont_have_dvc, null);

        initBase(view);
        initListeners();

        return view;
    }

    private void initBase(View view) {
        zxingQRInitiateCamera();
        tvAddNewDvc = view.findViewById(R.id.tvAddNewDvc);
    }

    private void initListeners() {
      /*  tvAddNewDvc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Adding Fragment(FragAvailableDevices)
                MyFragmentTransactions.replaceFragment(mContext, new FragAvailableDevices(), Constant.AVAILABLE_DEVICES, mContext.frm_lyt_container_int, true);

            }
        });*/
    }

    private void zxingQRInitiateCamera() {
        //mContext.setContentView(zXingScannerView);
        zXingScannerView.setResultHandler(this);
        zXingScannerView.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        zXingScannerView.stopCamera();
    }

    @Override
    public void handleResult(Result result) {
        Intent intent=new Intent();
        intent.putExtra("MESSAGE",result.getText());
        mContext.setResult(2,intent);
        //finish();//finishing activity
    }
}
