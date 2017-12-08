package com.netcommlabs.greencontroller.Fragments;

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
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.netcommlabs.greencontroller.Constants;
import com.netcommlabs.greencontroller.InterfaceValveAdapter;
import com.netcommlabs.greencontroller.Interfaces.BLEInterface;
import com.netcommlabs.greencontroller.R;
import com.netcommlabs.greencontroller.activities.MainActivity;
import com.netcommlabs.greencontroller.adapters.AdptrAvailableDVCs;
import com.netcommlabs.greencontroller.model.DataTransferModel;
import com.netcommlabs.greencontroller.services.BleAdapterService;
import com.netcommlabs.greencontroller.utilities.BLEAppLevel;
import com.netcommlabs.greencontroller.utilities.Constant;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.SimpleTimeZone;
import java.util.TimeZone;

/**
 * Created by Android on 12/6/2017.
 */

public class FragAvailableDevices extends Fragment implements InterfaceValveAdapter, BLEInterface {

    private MainActivity mContext;
    private View view;
    private RecyclerView reViListAvailDvc;
    private BluetoothAdapter mBluetoothAdapter;
    private ProgressBar progrsBarIndetmnt;
    List<BluetoothDevice> listAvailbleDvcs;
    private TextView tvScanAgainEvent;
    private LinearLayout llNoDevice;
    private BleAdapterService bluetooth_le_adapter;
    private String deviceName = null;
    private String dvcMacAddress = null;
    private boolean back_requested = false;
    ProgressDialog progressDialog;
    private static final int REQUEST_CODE_ENABLE = 1;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = (MainActivity) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.available_devices, null);

        initBase(view);
        initListeners();

        return view;
    }

    private void initBase(View view) {
        tvScanAgainEvent = view.findViewById(R.id.tvScanAgainEvent);
        llNoDevice = view.findViewById(R.id.llNoDevice);
        //llScrnHeader = view.findViewById(R.id.llScrnHeader);
        progrsBarIndetmnt = view.findViewById(R.id.progrsBarIndetmnt);
        reViListAvailDvc = view.findViewById(R.id.reViListAvailDvc);
        startBTWork();

        LinearLayoutManager llManagerAailDvcs = new LinearLayoutManager(mContext);
        reViListAvailDvc.setLayoutManager(llManagerAailDvcs);

        listAvailbleDvcs = new ArrayList<>();


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
        tvScanAgainEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startDvcDiscovery();
            }
        });

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

                    reViListAvailDvc.setAdapter(new AdptrAvailableDVCs(mContext, FragAvailableDevices.this, listAvailbleDvcs, mBluetoothAdapter));
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

  /*  @Override
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
    }*/

    @Override
    public void clickPassDataToAct(ArrayList<DataTransferModel> listValveDataSingle, String clickedValveName,int pos) {

    }

    @Override
    public void onRecyclerItemClickedNameAdress(String name, String address) {
        dvcMacAddress = address;
        deviceName = name;

        progressDialog = new ProgressDialog(mContext);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        BLEAppLevel.getInstance(mContext, FragAvailableDevices.this, dvcMacAddress);
    }

    @Override
    public void dvcHasExptdServices() {
        progressDialog.dismiss();
        //Adding Fragment(FragConnectedQR)
        Fragment fragConnectedQR = new FragConnectedQR();
        Bundle bundle = new Bundle();
        bundle.putString(FragConnectedQR.EXTRA_ID, dvcMacAddress);
        bundle.putString(FragConnectedQR.EXTRA_NAME, deviceName);
        fragConnectedQR.setArguments(bundle);
        MyFragmentTransactions.replaceFragment(mContext, fragConnectedQR, Constant.CONNECTED_QR, mContext.frm_lyt_container_int, true);
    }

   /* public void onSetTime() {
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
*/
   /* @Override
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
    }*/

}
