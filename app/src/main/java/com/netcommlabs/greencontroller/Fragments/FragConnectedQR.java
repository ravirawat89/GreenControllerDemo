package com.netcommlabs.greencontroller.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

//import com.google.zxing.integration.android.IntentIntegrator;
//import com.google.zxing.integration.android.IntentResult;
import com.netcommlabs.greencontroller.R;
import com.netcommlabs.greencontroller.activities.MainActivity;
import com.netcommlabs.greencontroller.model.MdlLocationAddress;
import com.netcommlabs.greencontroller.model.ModalBLEDevice;
import com.netcommlabs.greencontroller.sqlite_db.DatabaseHandler;
import com.netcommlabs.greencontroller.utilities.Constant;

import java.util.List;

/**
 * Created by Android on 12/6/2017.
 */

public class FragConnectedQR extends Fragment {

    private MainActivity mContext;
    private View view;
    private TextView tvAddNewDvc;
    private static final int REQUEST_CODE_ADDRESS = 24;
    private static final int REQUEST_CODE_QR = 2;
    private LinearLayout llScrnHeader, llDeviceEditConncted, llAddDeviceAddressConctd;
    private TextView tvScanQREvent, tvNextConctdEvent, tvDvcName, tvTitleConctnt;
    private DatabaseHandler databaseHandler;
    private ImageView ivEditDvcName, ivSaveDvcName;
    private EditText etEditDvcName, etQRManually;

    public static final String EXTRA_NAME = "name";
    public static final String EXTRA_ID = "id";

    private String device_name;
    private String dvc_mac_address;
    int valveNum;
    private MdlLocationAddress mdlLocationAddress;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = (MainActivity) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.conted_qr_act, null);

        initBase(view);
        initListeners();

        return view;
    }

    private void initBase(View view) {
        Bundle bundle = this.getArguments();
        device_name = bundle.getString(EXTRA_NAME);
        dvc_mac_address = bundle.getString(EXTRA_ID);

//        llScrnHeader = view.findViewById(R.id.llScrnHeader);
        llDeviceEditConncted = view.findViewById(R.id.llDeviceEditConncted);
        llAddDeviceAddressConctd = view.findViewById(R.id.llAddDeviceAddressConctd);

/*
        tvTitleConctnt = view.findViewById(R.id.tvTitleConctnt);
*/
        tvTitleConctnt = mContext.tvToolbar_title;
        tvDvcName = view.findViewById(R.id.tvDvcName);
        ivEditDvcName = view.findViewById(R.id.ivEditDvcName);
        ivSaveDvcName = view.findViewById(R.id.ivSaveDvcName);
        etEditDvcName = view.findViewById(R.id.etEditDvcName);
        etQRManually = view.findViewById(R.id.etQRManually);

        tvTitleConctnt.setText(device_name + " Connected");
        tvDvcName.setText(device_name);
        tvScanQREvent = view.findViewById(R.id.tvScanQREvent);
        tvNextConctdEvent = view.findViewById(R.id.tvNextConctdEvent);

        databaseHandler = new DatabaseHandler(mContext);
    }

    private void initListeners() {
     /*   llScrnHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentAddWtrngProfile = new Intent(mContext, AvailableDevices.class);
                mContext.startActivity(intentAddWtrngProfile);
                mContext.finish();
            }
        });*/

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
                FragAddAddress fragAddAddress = new FragAddAddress();
                //First child---then parent
                fragAddAddress.setTargetFragment(FragConnectedQR.this, 101);
                //Adding Fragment(FragAddAddress)
                MyFragmentTransactions.replaceFragment(mContext, fragAddAddress, Constant.ADD_ADDRESS, mContext.frm_lyt_container_int, true);
            }
        });

        tvScanQREvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //IntentIntegrator.forSupportFragment(FragConnectedQR.this).initiateScan();
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

                if (mdlLocationAddress == null) {
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

                //Adding Fragment(FragAvailableDevices)
                MyFragmentTransactions.replaceFragment(mContext, new FragMyDevices(), Constant.DEVICE_MAP, mContext.frm_lyt_container_int, false);

                //Toast.makeText(mContext, "Count " + databaseHandler.getBLEDvcCount(), Toast.LENGTH_SHORT).show();
               /* Intent intentAddWtrngProfile = new Intent(mContext, DeviceActivity.class);
                mContext.startActivity(intentAddWtrngProfile);
                mContext.finish();*/

            }

        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 101 && resultCode == Activity.RESULT_OK) {
            if (data.getSerializableExtra("mdlAddressLocation") != null) {
                mdlLocationAddress = (MdlLocationAddress) data.getSerializableExtra("mdlAddressLocation");
                Toast.makeText(mContext, "Address Saved", Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(mContext, "No data from address", Toast.LENGTH_SHORT).show();

            }
        }
        //IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        /*if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(mContext, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(mContext, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
            }
        }*/
    }

}
