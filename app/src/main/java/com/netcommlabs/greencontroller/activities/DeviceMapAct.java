package com.netcommlabs.greencontroller.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.netcommlabs.greencontroller.R;
import com.netcommlabs.greencontroller.model.ModalBLEDevice;
import com.netcommlabs.greencontroller.sqlite_db.DatabaseHandler;

import java.util.List;

public class DeviceMapAct extends AppCompatActivity {

    private TextView tvDvcsOneEvent, tvValveCount;
    private DeviceMapAct mContext;
    private LinearLayout llScrnHeader;
    private DatabaseHandler databaseHandler;
    private LinearLayout llDeviceAndValveNo;
    private String dvcName, dvcMacAddrs;
    int valvesNum;
    //private List<String> listValves;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.device_map_act);

        initBase();

        initListeners();
    }

    private void initBase() {
        mContext = this;
        llScrnHeader = findViewById(R.id.llScrnHeader);
        tvDvcsOneEvent = findViewById(R.id.tvDvcsOneEvent);
        tvValveCount = findViewById(R.id.tvValveCount);

        llDeviceAndValveNo = findViewById(R.id.llDeviceAndValveNo);

        //Getting BLE data from DB
        databaseHandler = new DatabaseHandler(mContext);
        List<ModalBLEDevice> listModalBleDevice = databaseHandler.getAllBLEDvcs();
        dvcName = listModalBleDevice.get(0).getName();
        dvcMacAddrs = listModalBleDevice.get(0).getDvcMacAddrs();
        valvesNum = listModalBleDevice.get(0).getValvesNum();

        tvDvcsOneEvent.setText(dvcName + "\n" + dvcMacAddrs);
        tvValveCount.setText(valvesNum + "");
    }

    private void initListeners() {
        llScrnHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mContext.finish();
            }
        });

        llDeviceAndValveNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //databaseHandler.addValveData(ModalBLEValve(dvcMacAddrs,listValves,listValveData));

                Intent intentDvcDetails = new Intent(mContext, DeviceDetails.class);
                intentDvcDetails.putExtra(DeviceDetails.EXTRA_DVC_NAME, dvcName);
                intentDvcDetails.putExtra(DeviceDetails.EXTRA_DVC_MAC, dvcMacAddrs);
                intentDvcDetails.putExtra(DeviceDetails.EXTRA_DVC_VALVE_COUNT, valvesNum);
                mContext.startActivity(intentDvcDetails);
            }
        });
    }
}
