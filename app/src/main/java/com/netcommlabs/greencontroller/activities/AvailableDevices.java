package com.netcommlabs.greencontroller.activities;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattService;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.netcommlabs.greencontroller.Constants;
import com.netcommlabs.greencontroller.InterfaceValveAdapter;
import com.netcommlabs.greencontroller.R;
import com.netcommlabs.greencontroller.adapters.AdptrAvailableDVCs;
import com.netcommlabs.greencontroller.model.DataTransferModel;
import com.netcommlabs.greencontroller.services.BleAdapterService;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.SimpleTimeZone;
import java.util.TimeZone;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

public class AvailableDevices extends AppCompatActivity implements InterfaceValveAdapter {

    private static final int MY_PERMISSIONS_REQUEST_FINE_LOCATION = 1;
    private RecyclerView reViListAvailDvc;
    private AvailableDevices mContext;
    private BluetoothAdapter mBluetoothAdapter;
    private static final int REQUEST_CODE_ENABLE = 1;
    private ProgressBar progrsBarIndetmnt;
    List<BluetoothDevice> listAvailbleDvcs;
    private TextView tvScanAgainEvent;
    private LinearLayout llScrnHeader, llNoDevice;
    private BleAdapterService bluetooth_le_adapter;
    private String deviceName = null;
    private String deviceAddress = null;
    private boolean back_requested = false;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.available_devices);

        initBase();

        initListeners();

    }

    private void initBase() {
        mContext = this;

        tvScanAgainEvent = (TextView) findViewById(R.id.tvScanAgainEvent);
        llNoDevice = (LinearLayout) findViewById(R.id.llNoDevice);
        llScrnHeader = (LinearLayout) findViewById(R.id.llScrnHeader);
        progrsBarIndetmnt = (ProgressBar) findViewById(R.id.progrsBarIndetmnt);

        reViListAvailDvc = (RecyclerView) findViewById(R.id.reViListAvailDvc);

        LinearLayoutManager llManagerAailDvcs = new LinearLayoutManager(this);
        reViListAvailDvc.setLayoutManager(llManagerAailDvcs);

        listAvailbleDvcs = new ArrayList<>();

        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            startBTWork();
        } else {
            ActivityCompat.requestPermissions(mContext,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_FINE_LOCATION);
        }


    }

    private void startBTWork() {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            Toast.makeText(mContext, "Device don't support Bluetooth", Toast.LENGTH_SHORT).show();
        } else {
            if (mBluetoothAdapter.isEnabled()) {
                startDvcDiscovery();
                return;
            }
            Intent intentBTEnableRqst = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intentBTEnableRqst, REQUEST_CODE_ENABLE);
        }
    }

    private void initListeners() {
        llScrnHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentAvailableDbc = new Intent(mContext, DontHaveDvc.class);
                startActivity(intentAvailableDbc);
                mContext.finish();
            }
        });

        tvScanAgainEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startDvcDiscovery();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //------ If bluetooth is enabled-------
        if (requestCode == REQUEST_CODE_ENABLE && resultCode == Activity.RESULT_OK) {
            startDvcDiscovery();
        } else {
            Toast.makeText(this, "Bluetooth enabling is mandatory", Toast.LENGTH_SHORT).show();
           /* Intent intentAddWtrngProfile = new Intent(mContext, DontHaveDvc.class);
            mContext.startActivity(intentAddWtrngProfile);*/
            finish();
        }
    }

    private void startDvcDiscovery() {
        if (mBluetoothAdapter.isEnabled()) {

            IntentFilter intentFilterActnFound = new IntentFilter();
            intentFilterActnFound.addAction(BluetoothDevice.ACTION_FOUND);
            intentFilterActnFound.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
            intentFilterActnFound.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
            mContext.registerReceiver(mBroadcastReceiver, intentFilterActnFound);

            mBluetoothAdapter.startDiscovery();

        } else {
            Toast.makeText(mContext, "Kindly turn BT ON", Toast.LENGTH_SHORT).show();
        }
    }

    BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (action.equals(BluetoothAdapter.ACTION_DISCOVERY_STARTED)) {
                progrsBarIndetmnt.setVisibility(View.VISIBLE);
                listAvailbleDvcs.clear();

                reViListAvailDvc.setVisibility(View.VISIBLE);
                llNoDevice.setVisibility(View.GONE);
            }

            if (action.equals(BluetoothDevice.ACTION_FOUND)) {

                //Toast.makeText(mContext, "Device Found", Toast.LENGTH_SHORT).show();
                BluetoothDevice availbleDvc = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                if (!listAvailbleDvcs.contains(availbleDvc)) {
                    listAvailbleDvcs.add(availbleDvc);

                    //reViListAvailDvc.setAdapter(new AdptrAvailableDVCs(mContext, listAvailbleDvcs, mBluetoothAdapter));
                }

            }

            if (action.equals(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)) {
                progrsBarIndetmnt.setVisibility(View.GONE);

                if (listAvailbleDvcs.size() < 1) {
                    reViListAvailDvc.setVisibility(View.GONE);
                    llNoDevice.setVisibility(View.VISIBLE);
                }

                //mBluetoothAdapter.cancelDiscovery();
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mBluetoothAdapter != null && mBluetoothAdapter.isEnabled()) {
            mBluetoothAdapter.disable();

        }
        try {
            unregisterReceiver(mBroadcastReceiver);

        } catch (Exception e) {
            Log.e("!!!!!BCR ", e.toString());
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    startBTWork();

                } else {
                    Toast.makeText(mContext, "Location permission is required...", Toast.LENGTH_SHORT).show();

                    /*Intent intentAddWtrngProfile = new Intent(mContext, DontHaveDvc.class);
                    mContext.startActivity(intentAddWtrngProfile);*/

                    mContext.finish();
                }
                return;
            }

        }
    }

    @Override
    public void clickPassDataToAct(ArrayList<DataTransferModel> listValveDataSingle, String clickedValveName,int pos) {

    }

    @Override
    public void onRecyclerItemClickedNameAdress(String name, String address) {
        deviceAddress = address;
        deviceName = name;

        progressDialog = new ProgressDialog(mContext);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        Intent gattServiceIntent = new Intent(this, BleAdapterService.class);
        bindService(gattServiceIntent, service_connection, BIND_AUTO_CREATE);


    }

    private final ServiceConnection service_connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            bluetooth_le_adapter = ((BleAdapterService.LocalBinder) service).getService();
            bluetooth_le_adapter.setActivityHandler(message_handler);
            if (bluetooth_le_adapter != null) {
                bluetooth_le_adapter.connect(deviceAddress);
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
                    // showMsg("CONNECTED");
                    // enable the LOW/MID/HIGH alert level selection buttons
                   /* ((Button)findViewById(R.id.lowButton)).setEnabled(true);
                    ((Button) findViewById(R.id.midButton)).setEnabled(true);
                    ((Button) findViewById(R.id.highButton)).setEnabled(true);*/
                    bluetooth_le_adapter.discoverServices();

                    break;

                case BleAdapterService.GATT_DISCONNECT:
                    //((Button) findViewById(R.id.connectButton)).setEnabled(true);
                    //we're disconnected
                    showMsg("DISCONNECTED");
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
                    progressDialog.dismiss();
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


                        Intent intentAddWtrngProfile = new Intent(mContext, ConnectedQRAct.class);
                        intentAddWtrngProfile.putExtra(AddEditSessionPlan.EXTRA_NAME, deviceName);
                        intentAddWtrngProfile.putExtra(AddEditSessionPlan.EXTRA_ID, deviceAddress);
                        mContext.startActivity(intentAddWtrngProfile);

                        mContext.unbindService(service_connection);
                        bluetooth_le_adapter = null;
                        mContext.finish();
                        //onSetTime();

                    } else {
                        bluetooth_le_adapter.disconnect();
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
                        Log.e("@@@@@@@@@@@@", "Ack Received For");

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
                Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
                //((TextView) findViewById(R.id.msgTextView)).setText(msg);
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
        if(bluetooth_le_adapter!=null)
        if (bluetooth_le_adapter.isConnected()) {
            try {
                bluetooth_le_adapter.disconnect();
            } catch (Exception e) {
            }
        } else {
            finish();
        }
    }


}
