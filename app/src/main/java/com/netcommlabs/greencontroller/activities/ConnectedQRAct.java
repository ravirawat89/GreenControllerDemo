package com.netcommlabs.greencontroller.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.netcommlabs.greencontroller.R;
import com.netcommlabs.greencontroller.model.MdlLocationAddress;
import com.netcommlabs.greencontroller.model.ModalBLEDevice;
import com.netcommlabs.greencontroller.sqlite_db.DatabaseHandler;

import java.util.List;

public class ConnectedQRAct extends AppCompatActivity {

    private static final int REQUEST_CODE_ADDRESS = 24;
    private static final int REQUEST_CODE_QR = 2;
    private ConnectedQRAct mContext;
    private LinearLayout llScrnHeader, llDeviceEditConncted, llAddDeviceAddressConctd;
    private TextView tvScanQREvent, tvNextConctdEvent, tvDvcName/*, tvTitleConctnt*/;
    private DatabaseHandler databaseHandler;
    private ImageView ivEditDvcName, ivSaveDvcName;
    private EditText etEditDvcName, etQRManually;

    public static final String EXTRA_NAME = "name";
    public static final String EXTRA_ID = "id";

    private String device_name;
    private String dvc_mac_address;
    int valveNum;
    private MdlLocationAddress mdlLocationAddress;
    //private List<String> listValves;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.conted_qr_act);

        initBase();

        initListeners();
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        mContext = this;
    }

    private void initBase() {
        mContext = this;

        final Intent intent = getIntent();
        device_name = intent.getStringExtra(EXTRA_NAME);
        dvc_mac_address = intent.getStringExtra(EXTRA_ID);

        databaseHandler = new DatabaseHandler(mContext);
        llScrnHeader = (LinearLayout) findViewById(R.id.llScrnHeader);
        llDeviceEditConncted = (LinearLayout) findViewById(R.id.llDeviceEditConncted);
        llAddDeviceAddressConctd = (LinearLayout) findViewById(R.id.llAddDeviceAddressConctd);

   /*     tvTitleConctnt=findViewById(R.id.tvTitleConctnt);*/
        tvDvcName = findViewById(R.id.tvDvcName);
        ivEditDvcName = findViewById(R.id.ivEditDvcName);
        ivSaveDvcName = findViewById(R.id.ivSaveDvcName);
        etEditDvcName = findViewById(R.id.etEditDvcName);
        etQRManually = findViewById(R.id.etQRManually);
       /* tvTitleConctnt.setText(device_name+" Connected");*/
        tvDvcName.setText(device_name);
        tvScanQREvent = findViewById(R.id.tvScanQREvent);
        tvNextConctdEvent = findViewById(R.id.tvNextConctdEvent);

        //listValves = new ArrayList<>();
    }

    private void initListeners() {
        llScrnHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentAddWtrngProfile = new Intent(mContext, AvailableDevices.class);
                mContext.startActivity(intentAddWtrngProfile);
                mContext.finish();
            }
        });

        ivEditDvcName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String dvcNameStrng = tvDvcName.getText().toString();
                tvDvcName.setVisibility(View.GONE);
                ivEditDvcName.setVisibility(View.GONE);
                etEditDvcName.setVisibility(View.VISIBLE);
                ivSaveDvcName.setVisibility(View.VISIBLE);
                etEditDvcName.setText(dvcNameStrng);
            }
        });

        ivSaveDvcName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String dvcNameStrngEdited = etEditDvcName.getText().toString();
                if (dvcNameStrngEdited.isEmpty()) {
                    Toast.makeText(mContext, "Device name can't be empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                tvDvcName.setVisibility(View.VISIBLE);
                ivEditDvcName.setVisibility(View.VISIBLE);
                etEditDvcName.setVisibility(View.GONE);
                ivSaveDvcName.setVisibility(View.GONE);
                tvDvcName.setText(dvcNameStrngEdited);
                Toast.makeText(mContext, "Device name edited", Toast.LENGTH_SHORT).show();
            }
        });

       /* llDeviceEditConncted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "Device connected", Toast.LENGTH_SHORT).show();
            }
        });*/

        llAddDeviceAddressConctd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentAddWtrngProfile = new Intent(mContext, AddAddressActivity.class);
                mContext.startActivityForResult(intentAddWtrngProfile, REQUEST_CODE_ADDRESS);
                //mContext.startActivity(intentAddWtrngProfile);
                //mContext.finish();
            }
        });

        tvScanQREvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*databaseHandler.getContactsCount();
                Toast.makeText(mContext, "", Toast.LENGTH_SHORT).show();
                */
                Intent intent = new Intent(mContext, QRScanAct.class);
                startActivityForResult(intent, REQUEST_CODE_QR);

                //zxingQRInitiateCamera();
            }
        });

        etQRManually.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                etQRManually.setCursorVisible(true);
                etQRManually.setFocusableInTouchMode(true);
                return false;
            }
        });

        tvNextConctdEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String etQRManuallyInput = etQRManually.getText().toString();

                if (mdlLocationAddress==null){
                    Toast.makeText(mContext, "Please provide at least ADDRESS NAME from address", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (etQRManuallyInput.isEmpty()) {
                    Toast.makeText(mContext, "Please provide QR information", Toast.LENGTH_SHORT).show();
                    return;
                } /*else if (!valveNum.isEmpty()) {
                    valveNum = "8";
                }*/ else if (!etQRManuallyInput.isEmpty() && etQRManuallyInput.equalsIgnoreCase("QR8")) {
                    valveNum = 8;
                    /*for (int i=1;i<=8;i++){
                        listValves.add("Valve "+i);
                    }*/
                } else {
                    Toast.makeText(mContext, "Please enter a valid input", Toast.LENGTH_SHORT).show();
                    return;
                }

                databaseHandler = new DatabaseHandler(mContext);
                //databaseHandler.deleteAllFromTableBLEDvc();
                //Toast.makeText(mContext, "Count " + databaseHandler.getBLEDvcCount(), Toast.LENGTH_SHORT).show();

                List<ModalBLEDevice> listBLEDvcFromDB = databaseHandler.getAllBLEDvcs();
                for (int i = 0; i < listBLEDvcFromDB.size(); i++) {
                    if (listBLEDvcFromDB.get(i).getDvcMacAddrs().equalsIgnoreCase(dvc_mac_address)) {
                        Toast.makeText(mContext, "This device is already added", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                ModalBLEDevice modalBleDevice = new ModalBLEDevice(device_name, dvc_mac_address, mdlLocationAddress, valveNum);
                databaseHandler.addBLEDevice(modalBleDevice);
                //Toast.makeText(mContext, "Count " + databaseHandler.getBLEDvcCount(), Toast.LENGTH_SHORT).show();
                Intent intentAddWtrngProfile = new Intent(mContext, DeviceActivity.class);
                mContext.startActivity(intentAddWtrngProfile);
                mContext.finish();

            }

        });

    }

    public void qrCodeDialog(String text) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ConnectedQRAct.this);
        builder.setTitle("QR Result my custom");
        builder.setMessage(text);
        builder.create().show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_ADDRESS:
                if (resultCode == AddAddressActivity.RESULT_CODE_ADDRESS && data != null) {
                    mdlLocationAddress = (MdlLocationAddress) data.getSerializableExtra("Data");
                    //Toast.makeText(mContext, "check", Toast.LENGTH_SHORT).show();
                }
                break;
            case REQUEST_CODE_QR:
                if (data != null) {
                    String message = data.getStringExtra("MESSAGE");
                    qrCodeDialog(message);
                }
                break;
        }
    }

}
