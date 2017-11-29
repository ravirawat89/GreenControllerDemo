package com.netcommlabs.greencontroller.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.netcommlabs.greencontroller.R;

public class DontHaveDvc extends AppCompatActivity {

    private DontHaveDvc mContext;
    private TextView tvAddNewDvc;
    private LinearLayout llScrnHeader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dont_have_dvc);

        initBase();

        initListeners();
    }

    private void initBase() {
        mContext = this;

        tvAddNewDvc = (TextView) findViewById(R.id.tvAddNewDvc);
        llScrnHeader = (LinearLayout) findViewById(R.id.llScrnHeader);
    }

    private void initListeners() {
        llScrnHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentMyDevcs=new Intent(mContext,DashboardPebbleHome.class);
                startActivity(intentMyDevcs);
                mContext.finish();
            }
        });

        tvAddNewDvc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentAvailableDbc = new Intent(mContext, AvailableDevices.class);
                startActivity(intentAvailableDbc);
                mContext.finish();
            }
        });
    }
}
