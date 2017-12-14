package com.netcommlabs.greencontroller.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.bluetooth.BluetoothGattService;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.netcommlabs.greencontroller.Constants;
import com.netcommlabs.greencontroller.R;
import com.netcommlabs.greencontroller.adapters.DeviceAddressAdapter;
import com.netcommlabs.greencontroller.model.DeviceAddressModel;
import com.netcommlabs.greencontroller.model.MdlLocationAddress;
import com.netcommlabs.greencontroller.model.ModalBLEDevice;
import com.netcommlabs.greencontroller.services.BleAdapterService;
import com.netcommlabs.greencontroller.sqlite_db.DatabaseHandler;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.SimpleTimeZone;
import java.util.TimeZone;

public class DeviceActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener {

    private DeviceActivity mContext;
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
    private TextView tvDeviceNameDyn, tvValveCount/*, tvShowAddressTop*/;
    private MdlLocationAddress mdlLocationAddress;
    private String addressComplete;
    private List<String> listLocAddressType;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device);
        mContext = this;

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        ll_add_new = (LinearLayout) findViewById(R.id.ll_add_new);
//        llScrnHeader = findViewById(R.id.llScrnHeader);

        ll_1st = (LinearLayout) findViewById(R.id.ll_1st);
        ll_2st = (LinearLayout) findViewById(R.id.ll_2st);
        ll_3st = (LinearLayout) findViewById(R.id.ll_3st);
        ll_4st = (LinearLayout) findViewById(R.id.ll_4st);
        ll_5st = (LinearLayout) findViewById(R.id.ll_5st);


        ll_add_device = (LinearLayout) findViewById(R.id.ll_add_device);


        rl_1st = (RelativeLayout) findViewById(R.id.rl_1st);
        rl_2st = (RelativeLayout) findViewById(R.id.rl_2st);
        rl_3st = (RelativeLayout) findViewById(R.id.rl_3st);
        rl_4st = (RelativeLayout) findViewById(R.id.rl_4st);
        rl_5st = (RelativeLayout) findViewById(R.id.rl_5st);

        iv_prev = (ImageView) findViewById(R.id.iv_prev);
        iv_next = (ImageView) findViewById(R.id.iv_next);

        tvDeviceNameDyn = findViewById(R.id.tvDeviceNameDyn);
        tvValveCount = findViewById(R.id.tvValveCount);
//        tvShowAddressTop = (TextView) findViewById(R.id.tvShowAddressTop);

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

        device_name = listModalBleDevice.get(0).getName();
        device_address = listModalBleDevice.get(0).getDvcMacAddrs();
        mdlLocationAddress = listModalBleDevice.get(0).getMdlLocationAddress();
        addressComplete = mdlLocationAddress.getFlat_num() + ", "+ mdlLocationAddress.getStreetName() + ", " + mdlLocationAddress.getLocality_landmark() + ", "+ mdlLocationAddress.getPincode() + ", " + mdlLocationAddress.getCity() + ", " + mdlLocationAddress.getState();
//        tvShowAddressTop.setText(addressComplete);
        listLocAddressType = new ArrayList<>();
        listLocAddressType.add(mdlLocationAddress.getAddress_name());
        valvesNum = listModalBleDevice.get(0).getValvesNum();

        tvDeviceNameDyn.setText(device_name + "\n" + device_address);
        tvValveCount.setText(valvesNum + "");
        initController();

        setRecyclerViewAdapter();
    }


    void setRecyclerViewAdapter() {
        //initList();
        mAdapter = new DeviceAddressAdapter(this, listLocAddressType);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
    }

   /* void initList() {
        list = new ArrayList<>();

        DeviceAddressModel model = new DeviceAddressModel();
        model.setAddress("Home");
        list.add(model);

       *//* DeviceAddressModel model1 = new DeviceAddressModel();
        model1.setAddress("Office");
        list.add(model1);

        DeviceAddressModel model2 = new DeviceAddressModel();
        model2.setAddress("Farm House");
        list.add(model2);*//*


    }*/

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.ll_add_new:
                Intent intent = new Intent(this, AddAddressActivity.class);
                startActivityForResult(intent, 100);
                break;
            case R.id.ll_1st:
                Intent intentDvcDetails = new Intent(mContext, DeviceDetails.class);
                intentDvcDetails.putExtra(DeviceDetails.EXTRA_DVC_NAME, device_name);
                intentDvcDetails.putExtra(DeviceDetails.EXTRA_DVC_MAC, device_address);
                intentDvcDetails.putExtra(DeviceDetails.EXTRA_DVC_VALVE_COUNT, valvesNum);
                mContext.startActivity(intentDvcDetails);
                mContext.finish();
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
                Toast.makeText(this, "Previous", Toast.LENGTH_SHORT).show();
                break;
            case R.id.iv_next:
                Toast.makeText(this, "Next", Toast.LENGTH_SHORT).show();
                break;
            case R.id.ll_add_device:
                Intent intent2 = new Intent(this, AvailableDevices.class);
                startActivity(intent2);
                break;
        /*    case R.id.llScrnHeader:
                Intent intent1 = new Intent(mContext, DashboardPebbleHome.class);
                mContext.startActivity(intent1);
                mContext.finish();
                break;*/

        }

    }


    @Override
    public boolean onLongClick(View view) {
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

    void initController() {
       /* device_name = "Pebble";
        device_address = "98:4F:EE:10:87:66";*/
        Intent gattServiceIntent = new Intent(this, BleAdapterService.class);
        bindService(gattServiceIntent, service_connection, BIND_AUTO_CREATE);
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
                   /* ((Button)findViewById(R.id.lowButton)).setEnabled(true);
                    ((Button) findViewById(R.id.midButton)).setEnabled(true);
                    ((Button) findViewById(R.id.highButton)).setEnabled(true);*/
                    bluetooth_le_adapter.discoverServices();

                    break;

                case BleAdapterService.GATT_DISCONNECT:
                    //((Button) findViewById(R.id.connectButton)).setEnabled(true);
                    //we're disconnected
                    showMsg("GATT_DISCONNECT");
                   /* // hide the rssi distance colored rectangle
                    ((LinearLayout) findViewById(R.id.rectangle)).setVisibility(View.INVISIBLE);
                    // disable the LOW/MID/HIGH alert level selection buttons
                    ((Button) findViewById(R.id.lowButton)).setEnabled(false);
                    ((Button) findViewById(R.id.midButton)).setEnabled(false);
                    ((Button) findViewById(R.id.highButton)).setEnabled(false);*/
                    if (back_requested) {
                        finish();
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

    private void showMsg(final String msg) {
        Log.d(Constants.TAG, msg);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(DeviceActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onSetTime() {
        String[] ids = TimeZone.getAvailableIDs(+5 * 60 * 60 * 1000);
        SimpleTimeZone pdt = new SimpleTimeZone(+5 * 60 * 60 * 1000, ids[0]);

        Calendar calendar = new GregorianCalendar(pdt);
        Date trialTime = new Date();
        calendar.setTime(trialTime);

        //Set present time as data packet
        byte hours = (byte) calendar.get(Calendar.HOUR);
        if (calendar.get(Calendar.AM_PM) == 1) {
            hours = (byte) (calendar.get(Calendar.HOUR) + 12);
        }
        byte minutes = (byte) calendar.get(Calendar.MINUTE);
        byte seconds = (byte) calendar.get(Calendar.SECOND);
        byte DATE = (byte) calendar.get(Calendar.DAY_OF_MONTH);
        byte MONTH = (byte) (calendar.get(Calendar.MONTH) + 1);
        int iYEARMSB = (calendar.get(Calendar.YEAR) / 256);
        int iYEARLSB = (calendar.get(Calendar.YEAR) % 256);
        byte bYEARMSB = (byte) iYEARMSB;
        byte bYEARLSB = (byte) iYEARLSB;
        byte[] currentTime = {hours, minutes, seconds, DATE, MONTH, bYEARMSB, bYEARLSB};
        bluetooth_le_adapter.writeCharacteristic(
                BleAdapterService.CURRENT_TIME_SERVICE_SERVICE_UUID,
                BleAdapterService.CURRENT_TIME_CHARACTERISTIC_UUID, currentTime
        );
    }


    @Override
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
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(service_connection);
        bluetooth_le_adapter = null;
    }

    void setConnectionIsDone() {
        ll_1st.setOnClickListener(this);
        ll_1st.setOnLongClickListener(this);
        ll_1st.setBackgroundResource(R.drawable.round_back_shadow_green_small);
    }


    public void showpopupLongClick() {
        Dialog dialog = null;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            if (data != null) {
                String Data = data.getStringExtra("Data");
                if (Data != null && list != null) {
                    DeviceAddressModel model = new DeviceAddressModel();
                    model.setAddress(Data);
                    list.add(model);
                    mAdapter.notifyDataSetChanged();
                }
            }
        }
    }
}
