package com.netcommlabs.greencontroller.Fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.netcommlabs.greencontroller.R;
import com.netcommlabs.greencontroller.activities.DeviceDetails;
import com.netcommlabs.greencontroller.activities.MainActivity;
import com.netcommlabs.greencontroller.adapters.DeviceAddressAdapter;
import com.netcommlabs.greencontroller.model.DeviceAddressModel;
import com.netcommlabs.greencontroller.model.MdlLocationAddress;
import com.netcommlabs.greencontroller.model.ModalBLEDevice;
import com.netcommlabs.greencontroller.services.BleAdapterService;
import com.netcommlabs.greencontroller.sqlite_db.DatabaseHandler;
import com.netcommlabs.greencontroller.utilities.BLEAppLevel;
import com.netcommlabs.greencontroller.utilities.Constant;
import com.netcommlabs.greencontroller.utilities.MySharedPreference;

import java.util.ArrayList;
import java.util.List;

import static com.netcommlabs.greencontroller.utilities.SharedPrefsConstants.ADDRESS;

/**
 * Created by Android on 12/6/2017.
 */

public class FragMyDevices extends Fragment implements View.OnClickListener, View.OnLongClickListener {

    private MainActivity mContext;
    private View view;
    private RecyclerView recyclerView;
    private DeviceAddressAdapter mAdapter;
    private List<DeviceAddressModel> list;
    private LinearLayout ll_add_new;
    private LinearLayout ll_1st;
    private LinearLayout ll_2st;
    private LinearLayout ll_3st;
    private LinearLayout ll_4st;
    private LinearLayout ll_5st;
    private LinearLayout ll_add_device/*, llScrnHeader*/;


    private RelativeLayout rl_1st;
    private RelativeLayout rl_2st;
    private RelativeLayout rl_3st;
    private RelativeLayout rl_4st;
    private RelativeLayout rl_5st;

    private String device_name;
    private String device_address;
    private BleAdapterService bluetooth_le_adapter;
    private boolean back_requested = false;


    private ImageView iv_prev;
    private ImageView iv_next;
    private DatabaseHandler databaseHandler;
    private int valvesNum;
    private TextView tvDeviceNameDyn, tvValveCount, tvShowAddressTop,toolbar_tile;
    private MdlLocationAddress mdlLocationAddress;
    private String addressComplete;
    private List<String> listLocAddressType;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = (MainActivity) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_device, null);

        initBase(view);

        return view;
    }

    private void initBase(View view) {
        FragDeviceDetails.listModalValveNameSelect=null;
        recyclerView = view.findViewById(R.id.recycler_view);
        ll_add_new = view.findViewById(R.id.ll_add_new);
//        llScrnHeader = view.findViewById(R.id.llScrnHeader);

        ll_1st = view.findViewById(R.id.ll_1st);
        ll_2st = view.findViewById(R.id.ll_2st);
        ll_3st = view.findViewById(R.id.ll_3st);
        ll_4st = view.findViewById(R.id.ll_4st);
        ll_5st = view.findViewById(R.id.ll_5st);


        ll_add_device = view.findViewById(R.id.ll_add_device);


        rl_1st = view.findViewById(R.id.rl_1st);
        rl_2st = view.findViewById(R.id.rl_2st);
        rl_3st = view.findViewById(R.id.rl_3st);
        rl_4st = view.findViewById(R.id.rl_4st);
        rl_5st = view.findViewById(R.id.rl_5st);

        iv_prev = view.findViewById(R.id.iv_prev);
        iv_next = view.findViewById(R.id.iv_next);

        tvDeviceNameDyn = view.findViewById(R.id.tvDeviceNameDyn);
        tvValveCount = view.findViewById(R.id.tvValveCount);
//        tvShowAddressTop = view.findViewById(R.id.tvShowAddressTop);
        tvShowAddressTop =mContext.tvDesc_txt;
        toolbar_tile =mContext.tvToolbar_title;
        toolbar_tile.setText("My Devices");

//        llScrnHeader.setOnClickListener(this);
        ll_add_new.setOnClickListener(this);

        ll_2st.setOnClickListener(this);
        ll_3st.setOnClickListener(this);
        ll_4st.setOnClickListener(this);
        ll_5st.setOnClickListener(this);
        iv_prev.setOnClickListener(this);
        iv_next.setOnClickListener(this);
        ll_add_device.setOnClickListener(this);

        ll_2st.setOnLongClickListener(this);
        ll_3st.setOnLongClickListener(this);
        ll_4st.setOnLongClickListener(this);
        ll_5st.setOnLongClickListener(this);

        //Getting BLE data from DB
        databaseHandler = new DatabaseHandler(mContext);
        List<ModalBLEDevice> listModalBleDevice = databaseHandler.getAllBLEDvcs();
        rl_1st.setVisibility(View.VISIBLE);

        /*device_name ="Pebble";
        device_address ="98:4F:EE:10:87:66";
        valvesNum = 8;*/
        device_name = listModalBleDevice.get(0).getName();
        device_address = listModalBleDevice.get(0).getDvcMacAddrs();
        mdlLocationAddress = listModalBleDevice.get(0).getMdlLocationAddress();
        addressComplete = mdlLocationAddress.getFlat_num() + ", " + mdlLocationAddress.getStreetName() + ", " + mdlLocationAddress.getLocality_landmark() + ", " + mdlLocationAddress.getPincode() + ", " + mdlLocationAddress.getCity() + ", " + mdlLocationAddress.getState();
        tvShowAddressTop.setText(addressComplete);
        MySharedPreference.getInstance(getActivity()).setStringData(ADDRESS,addressComplete);
        listLocAddressType = new ArrayList<>();
        listLocAddressType.add(mdlLocationAddress.getAddress_name());
        valvesNum = listModalBleDevice.get(0).getValvesNum();

        tvDeviceNameDyn.setText(device_name + "\n" + device_address);
        tvValveCount.setText(valvesNum + "");
        //initController();


        BLEAppLevel bleAppLevel = BLEAppLevel.getInstanceOnly();
        if (bleAppLevel!=null && bleAppLevel.getBLEConnectedOrNot()) {
            setConnectionIsDone();
        } else {
            Toast.makeText(mContext, "BLE lost connection", Toast.LENGTH_SHORT).show();
        }
        setRecyclerViewAdapter();
    }

    void setRecyclerViewAdapter() {
        //initList();
        mAdapter = new DeviceAddressAdapter(mContext, listLocAddressType);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.ll_add_new:
                FragAddAddress fragAddAddress = new FragAddAddress();
                //First child---then parent
                fragAddAddress.setTargetFragment(FragMyDevices.this, 101);
                //Adding Fragment(FragAddAddress)
                MyFragmentTransactions.replaceFragment(mContext, fragAddAddress, Constant.ADD_ADDRESS, mContext.frm_lyt_container_int, true);


               /* Intent intent = new Intent(mContext, AddAddressActivity.class);
                startActivityForResult(intent, 100);*/
                break;
            case R.id.ll_1st:
                FragDeviceDetails fragDeviceDetails = new FragDeviceDetails();
                Bundle bundle = new Bundle();
                bundle.putString(DeviceDetails.EXTRA_DVC_NAME, device_name);
                bundle.putString(DeviceDetails.EXTRA_DVC_MAC, device_address);
                bundle.putInt(DeviceDetails.EXTRA_DVC_VALVE_COUNT, valvesNum);
                fragDeviceDetails.setArguments(bundle);
                //Adding Fragment(FragAvailableDevices)
                MyFragmentTransactions.replaceFragment(mContext, fragDeviceDetails, Constant.DEVICE_DETAILS, mContext.frm_lyt_container_int, true);

               /* Intent intentDvcDetails = new Intent(mContext, DeviceDetails.class);
                intentDvcDetails.putExtra(DeviceDetails.EXTRA_DVC_NAME, device_name);
                intentDvcDetails.putExtra(DeviceDetails.EXTRA_DVC_MAC, device_address);
                intentDvcDetails.putExtra(DeviceDetails.EXTRA_DVC_VALVE_COUNT, valvesNum);
                mContext.startActivity(intentDvcDetails);
                mContext.finish();*/
                //Toast.makeText(this, "Clicked", Toast.LENGTH_SHORT).show();
                break;
            case R.id.ll_2st:
                break;
            case R.id.ll_3st:
                break;
            case R.id.ll_4st:
                break;
            case R.id.ll_5st:
                break;
            case R.id.iv_prev:
                Toast.makeText(mContext, "Previous", Toast.LENGTH_SHORT).show();
                break;
            case R.id.iv_next:
                Toast.makeText(mContext, "Next", Toast.LENGTH_SHORT).show();
                break;
            case R.id.ll_add_device:
                Toast.makeText(mContext, "In progress...", Toast.LENGTH_SHORT).show();
               /* Intent intent2 = new Intent(mContext, AvailableDevices.class);
                startActivity(intent2);*/
                break;
         /*   case R.id.llScrnHeader:
               *//* Intent intent1 = new Intent(mContext, DashboardPebbleHome.class);
                mContext.startActivity(intent1);
                mContext.finish();*//*
                break;*/

        }
    }

    @Override
    public boolean onLongClick(View v) {
        int id = view.getId();
        switch (id) {
            case R.id.ll_1st:
                showpopupLongClick();
                break;
            case R.id.ll_2st:
                showpopupLongClick();
                break;
            case R.id.ll_3st:
                showpopupLongClick();
                break;
            case R.id.ll_4st:
                showpopupLongClick();
                break;
            case R.id.ll_5st:
                showpopupLongClick();
                break;
        }
        return true;
    }

    public void showpopupLongClick() {
        Dialog dialog = null;
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("Options")
                .setItems(R.array.long_press_option, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        rl_1st.setVisibility(View.GONE);
                    }
                });
        dialog = builder.create();
        dialog.setCancelable(true);
        dialog.show();
    }

  /*  void initController() {
       *//* device_name = "Pebble";
        device_address = "98:4F:EE:10:87:66";*//*
        Intent gattServiceIntent = new Intent(mContext, BleAdapterService.class);
        mContext.bindService(gattServiceIntent, service_connection, BIND_AUTO_CREATE);
    }

    private final ServiceConnection service_connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            bluetooth_le_adapter = ((BleAdapterService.LocalBinder) service).getService();
            bluetooth_le_adapter.setActivityHandler(message_handler);
            if (bluetooth_le_adapter != null) {
                bluetooth_le_adapter.connect(device_address);
            } else {
                showMsg("onConnect: bluetooth_le_adapter=null");
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            bluetooth_le_adapter = null;
        }
    };

    private Handler message_handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Bundle bundle;
            String service_uuid = "";
            String characteristic_uuid = "";
            byte[] b = null;
            //message handling logic
            switch (msg.what) {
                case BleAdapterService.MESSAGE:
                    bundle = msg.getData();
                    String text = bundle.getString(BleAdapterService.PARCEL_TEXT);
                    showMsg(text);
                    break;

                case BleAdapterService.GATT_CONNECTED:

                    //((Button) findViewById(R.id.connectButton)).setEnabled(false);
                    //we're connected
                    showMsg("GATT_CONNECTED");
                    // enable the LOW/MID/HIGH alert level selection buttons
                   *//* ((Button)findViewById(R.id.lowButton)).setEnabled(true);
                    ((Button) findViewById(R.id.midButton)).setEnabled(true);
                    ((Button) findViewById(R.id.highButton)).setEnabled(true);*//*
                    bluetooth_le_adapter.discoverServices();

                    break;

                case BleAdapterService.GATT_DISCONNECT:
                    //((Button) findViewById(R.id.connectButton)).setEnabled(true);
                    //we're disconnected
                    showMsg("GATT_DISCONNECT");
                   *//* // hide the rssi distance colored rectangle
                    ((LinearLayout) findViewById(R.id.rectangle)).setVisibility(View.INVISIBLE);
                    // disable the LOW/MID/HIGH alert level selection buttons
                    ((Button) findViewById(R.id.lowButton)).setEnabled(false);
                    ((Button) findViewById(R.id.midButton)).setEnabled(false);
                    ((Button) findViewById(R.id.highButton)).setEnabled(false);*//*
                    if (back_requested) {
                        mContext.finish();
                    }
                    break;

                case BleAdapterService.GATT_SERVICES_DISCOVERED:
                    //validate services and if ok...
                    List<BluetoothGattService> slist = bluetooth_le_adapter.getSupportedGattServices();
                    boolean time_point_service_present = false;
                    boolean current_time_service_present = false;
                    boolean pots_service_present = false;
                    boolean battery_service_present = false;
                    boolean valve_controller_service_present = false;

                    for (BluetoothGattService svc : slist) {
                        Log.d(Constants.TAG, "UUID=" + svc.getUuid().toString().toUpperCase() + "INSTANCE=" + svc.getInstanceId());
                        String serviceUuid = svc.getUuid().toString().toUpperCase();
                        if (svc.getUuid().toString().equalsIgnoreCase(BleAdapterService.TIME_POINT_SERVICE_SERVICE_UUID)) {
                            time_point_service_present = true;
                            continue;
                        }
                        if (svc.getUuid().toString().equalsIgnoreCase(BleAdapterService.CURRENT_TIME_SERVICE_SERVICE_UUID)) {
                            current_time_service_present = true;
                            continue;
                        }
                        if (svc.getUuid().toString().equalsIgnoreCase(BleAdapterService.POTS_SERVICE_SERVICE_UUID)) {
                            pots_service_present = true;
                            continue;
                        }
                        if (svc.getUuid().toString().equalsIgnoreCase(BleAdapterService.BATTERY_SERVICE_SERVICE_UUID)) {
                            battery_service_present = true;
                            continue;
                        }
                        if (svc.getUuid().toString().equalsIgnoreCase(BleAdapterService.VALVE_CONTROLLER_SERVICE_UUID)) {
                            valve_controller_service_present = true;
                            continue;
                        }
                    }
                    if (time_point_service_present && current_time_service_present && pots_service_present && battery_service_present) {
                        showMsg("Device has expected services");
                        // onSetTime();
                        setConnectionIsDone();


                    } else {
                        showMsg("Device does not have expected GATT services");
                    }
                    break;

                case BleAdapterService.GATT_CHARACTERISTIC_READ:
                    bundle = msg.getData();
                    Log.d(Constants.TAG, "Service=" + bundle.get(BleAdapterService.PARCEL_SERVICE_UUID).toString().toUpperCase() + " Characteristic=" + bundle.get(BleAdapterService.PARCEL_CHARACTERISTIC_UUID).toString().toUpperCase());
                    if (bundle.get(BleAdapterService.PARCEL_CHARACTERISTIC_UUID).toString()
                            .toUpperCase().equals(BleAdapterService.ALERT_LEVEL_CHARACTERISTIC)
                            && bundle.get(BleAdapterService.PARCEL_SERVICE_UUID).toString().toUpperCase().equals(BleAdapterService.BATTERY_LEVEL_CHARACTERISTIC_UUID)) {
                        b = bundle.getByteArray(BleAdapterService.PARCEL_VALUE);
                        if (b.length > 0) {
                            showMsg("Received " + b.toString() + "from Pebble.");
                        }
                    }
                    break;

                case BleAdapterService.GATT_CHARACTERISTIC_WRITTEN:
                    bundle = msg.getData();

                    if (bundle.get(BleAdapterService.PARCEL_CHARACTERISTIC_UUID).toString().toUpperCase().equals(BleAdapterService.NEW_WATERING_TIME_POINT_CHARACTERISTIC_UUID)) {

                    }

                    if (bundle.get(BleAdapterService.PARCEL_CHARACTERISTIC_UUID).toString()
                            .toUpperCase().equals(BleAdapterService.ALERT_LEVEL_CHARACTERISTIC)
                            && bundle.get(BleAdapterService.PARCEL_SERVICE_UUID).toString().toUpperCase().equals(BleAdapterService.LINK_LOSS_SERVICE_UUID)) {
                        b = bundle.getByteArray(BleAdapterService.PARCEL_VALUE);
                        if (b.length > 0) {
                            showMsg("Received " + b.toString() + "from Pebble.");
                        }
                    }
                    break;
            }
        }
    };
    */

   /* @Override
    public void onBackPressed() {
        back_requested = true;
        if (bluetooth_le_adapter.isConnected()) {
            try {
                bluetooth_le_adapter.disconnect();
            } catch (Exception e) {
            }
        } else {
            finish();
        }
    }*/


   /* @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(service_connection);
        bluetooth_le_adapter = null;
    }*/

    void setConnectionIsDone() {
        ll_1st.setOnClickListener(this);
        ll_1st.setOnLongClickListener(this);
        ll_1st.setBackgroundResource(R.drawable.round_back_shadow_green_small);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 101 && resultCode == Activity.RESULT_OK) {
            if (data.getSerializableExtra("mdlAddressLocation") != null) {
                mdlLocationAddress = (MdlLocationAddress) data.getSerializableExtra("mdlAddressLocation");
                Toast.makeText(mContext, "Saved address is "+mdlLocationAddress.getAddress_name(), Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(mContext, "No data from address", Toast.LENGTH_SHORT).show();

            }
        }
    }

}
