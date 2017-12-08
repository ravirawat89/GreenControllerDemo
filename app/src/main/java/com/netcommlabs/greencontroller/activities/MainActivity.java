package com.netcommlabs.greencontroller.activities;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.netcommlabs.greencontroller.Fragments.FragDashboardPebbleHome;
import com.netcommlabs.greencontroller.Fragments.MyFragmentTransactions;
import com.netcommlabs.greencontroller.Interfaces.LocationDecetor;
import com.netcommlabs.greencontroller.Interfaces.ResponseListener;
import com.netcommlabs.greencontroller.R;
import com.netcommlabs.greencontroller.adapters.NavListAdapter;
import com.netcommlabs.greencontroller.model.ModalBLEDevice;
import com.netcommlabs.greencontroller.sqlite_db.DatabaseHandler;
import com.netcommlabs.greencontroller.utilities.AppAlertDialog;
import com.netcommlabs.greencontroller.utilities.BLEAppLevel;
import com.netcommlabs.greencontroller.utilities.Constant;
import com.netcommlabs.greencontroller.utilities.GeocodingLocation;
import com.netcommlabs.greencontroller.utilities.LocationUtils;
import com.netcommlabs.greencontroller.utilities.MySharedPreference;
import com.netcommlabs.greencontroller.utilities.Navigation_Drawer_Data;
import com.netcommlabs.greencontroller.utilities.NetworkUtils;
import com.netcommlabs.greencontroller.utilities.RowDataArrays;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.netcommlabs.greencontroller.utilities.Constant.AVAILABLE_DEVICES;
import static com.netcommlabs.greencontroller.utilities.Constant.DEVICE_DETAILS;
import static com.netcommlabs.greencontroller.utilities.Constant.DEVICE_MAP;
import static com.netcommlabs.greencontroller.utilities.SharedPrefsConstants.ADDRESS;
import static com.netcommlabs.greencontroller.utilities.SharedPrefsConstants.lAST_CONNECTED;

public class MainActivity extends AppCompatActivity implements LocationDecetor {

    private static final int PERMISSIONS_MULTIPLE_REQUEST = 200;
    private MainActivity mContext;
    private DrawerLayout nav_drawer_layout;
    private RecyclerView nav_revi_slider;
    private List<Navigation_Drawer_Data> listNavDrawerRowDat;
    private LinearLayout llHamburgerIcon;
    public int frm_lyt_container_int;
    private static final int MY_PERMISSIONS_REQUEST_FINE_LOCATION = 1;
    private static final int REQUEST_CODE_ENABLE = 1;
    private BluetoothAdapter mBluetoothAdapter;
    private static final int REQUEST_CODE = 1001;
    private ProgressDialog progressDoalog;
    private Location mLastLocation = null;
    private String usersAddress = null;
    public TextView toolbar_title;
    public TextView desc_txt;
    private boolean exit = false;
    private Fragment myFragment;
    private String dvcMacAddress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initBase();
        initListeners();
    }

    private void initBase() {
        mContext = this;

        DatabaseHandler databaseHandler = new DatabaseHandler(mContext);
        List<ModalBLEDevice> listBLEDvcFromDB = databaseHandler.getAllBLEDvcs();
        if (listBLEDvcFromDB != null && listBLEDvcFromDB.size() > 0) {
            dvcMacAddress = listBLEDvcFromDB.get(0).getDvcMacAddrs();
            myFragment = new Fragment();
            BLEAppLevel.getInstance(mContext, myFragment, dvcMacAddress);
        }

        //Checking Marshmallow
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkPermission();
        } else {
            Toast.makeText(mContext, "No need to ask runtime permissions", Toast.LENGTH_SHORT).show();
            if (NetworkUtils.isConnected(this)) {
                //Location work starts
                getLocation();
                //Bluetooth work starts
                startBTWork();
            } else {
                AppAlertDialog.showDialogFinishWithActivity(this, "Internet", "You are not Connected to internet");
            }
        }

        frm_lyt_container_int = R.id.frm_lyt_container;
        llHamburgerIcon = findViewById(R.id.llHamburgerIcon);
        toolbar_title = findViewById(R.id.toolbar_title);
        desc_txt = findViewById(R.id.desc_txt);
        nav_drawer_layout = findViewById(R.id.nav_drawer_layout);
        nav_revi_slider = findViewById(R.id.nav_revi_slider);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        nav_revi_slider.setLayoutManager(layoutManager);

        listNavDrawerRowDat = new ArrayList<>();
        for (int i = 0; i < new RowDataArrays().flatIconArray.length; i++) {
            listNavDrawerRowDat.add(new Navigation_Drawer_Data(
                    new RowDataArrays().flatIconArray[i],
                    new RowDataArrays().labelArray[i]

            ));
        }
        nav_revi_slider.setAdapter(new NavListAdapter(mContext, listNavDrawerRowDat, nav_drawer_layout));

        //Adding first Fragment(FragDashboardPebbleHome)
        MyFragmentTransactions.replaceFragment(mContext, new FragDashboardPebbleHome(), Constant.DASHBOARD_PEBBLE_HOME, frm_lyt_container_int, true);
    }

    void build() {
        //msg.setText("");
        if (mLastLocation != null) {
            if (NetworkUtils.isConnected(this)) {
                GeocodingLocation.getAddressFromLocation(mLastLocation.getLatitude(), mLastLocation.getLongitude(), this, new GeocoderHandler());
            } else {
                AppAlertDialog.showDialogFinishWithActivity(this, "Internet", "You are not Connected to internet");
            }
        } else
            Toast.makeText(this, "Unable To get Location", Toast.LENGTH_SHORT).show();


//        getAddressNow();
    }

    public void getLocation() {
        if (checkGooglePlayServiceAvailability(this)) {
            //buildProgress();
            LocationUtils.getInstance(this, this);
        }
    }


    @Override
    public void OnLocationChange(Location location) {
      /*  if (progressDoalog.isShowing()) {
            progressDoalog.dismiss();
        }*/
        mLastLocation = location;
    }

    @Override
    public void onErrors(String msg) {
        if (progressDoalog != null) {
            progressDoalog.dismiss();
        }
        AppAlertDialog.showDialogSelfFinish(this, "Error", msg);
    }

    @Override
    protected void onStop() {
        LocationUtils.getInstance(this, this).onStop();
        super.onStop();
    }

    public boolean checkGooglePlayServiceAvailability(Context context) {
        int statusCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);
        if ((statusCode == ConnectionResult.SUCCESS)) {
            return true;
        } else {
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(statusCode, this, 10, new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    Toast.makeText(MainActivity.this, "You have to update google play service account", Toast.LENGTH_LONG).show();
                    finish();
                }
            });
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
            return false;
        }
    }

    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(mContext,
                Manifest.permission.ACCESS_FINE_LOCATION) + ContextCompat
                .checkSelfPermission(mContext,
                        Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(mContext,
                    new String[]{Manifest.permission
                            .ACCESS_FINE_LOCATION, Manifest.permission.CAMERA},
                    PERMISSIONS_MULTIPLE_REQUEST);
        } else {
            //Toast.makeText(mContext, "All permissions already granted", Toast.LENGTH_SHORT).show();
            if (NetworkUtils.isConnected(this)) {
                //Location work starts
                getLocation();
                //Bluetooth work starts
                startBTWork();
            } else {
                AppAlertDialog.showDialogFinishWithActivity(this, "Internet", "You are not Connected to internet");
            }

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_MULTIPLE_REQUEST: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0) {
                    boolean fineLocation = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean cameraPermission = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                    if (fineLocation && cameraPermission) {
                        Toast.makeText(mContext, "Thanks for granting permissions", Toast.LENGTH_SHORT).show();
                        if (NetworkUtils.isConnected(this)) {
                            //Location work starts
                            getLocation();
                            //Bluetooth work starts
                            startBTWork();
                        } else {
                            AppAlertDialog.showDialogFinishWithActivity(this, "Internet", "You are not Connected to internet");
                        }
                    } else {
                        Toast.makeText(mContext, "App needs all permissions to be granted", Toast.LENGTH_LONG).show();
                        mContext.finish();
                    }
                }
            }
        }

    }

    private void startBTWork() {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            Toast.makeText(mContext, "Device don't support Bluetooth", Toast.LENGTH_SHORT).show();
        } else {
            if (mBluetoothAdapter.isEnabled()) {
                //startDvcDiscovery();
                //Toast.makeText(mContext, "Bluetooth is enabled", Toast.LENGTH_SHORT).show();
                return;
            }
            Intent intentBTEnableRqst = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intentBTEnableRqst, REQUEST_CODE_ENABLE);
        }
    }


    private void initListeners() {
        llHamburgerIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nav_drawer_layout.openDrawer(Gravity.START);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case LocationUtils.LocationTag:
                if (resultCode == Activity.RESULT_OK) {
                    //Toast.makeText(mContext, "GPS Enabled", Toast.LENGTH_SHORT).show();
                } else if (resultCode == Activity.RESULT_CANCELED) {
                    Toast.makeText(mContext, "GPS enabling is mandatory", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            case REQUEST_CODE_ENABLE:
                if (resultCode == Activity.RESULT_OK) {
                    //Toast.makeText(mContext, "Bluetooth is enabled...", Toast.LENGTH_SHORT).show();
                } else if (resultCode == Activity.RESULT_CANCELED) {
                    Toast.makeText(mContext, "Bluetooth enabling is mandatory", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
        }
    }

    void buildProgress() {
        progressDoalog = new ProgressDialog(MainActivity.this);
        progressDoalog.setMessage("Please wait....");
        progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDoalog.setCancelable(false);
        progressDoalog.show();
    }

    private class GeocoderHandler extends Handler {
        @Override
        public void handleMessage(Message message) {
            String locationAddress;
            switch (message.what) {
                case 1:
                    Bundle bundle = message.getData();
                    locationAddress = bundle.getString("address");
                    break;
                default:
                    locationAddress = null;
            }
            usersAddress = locationAddress;
//            Toast.makeText(MainActivity.this,usersAddress,Toast.LENGTH_LONG).show();
            Log.e("ADdRESSSSSS", usersAddress);
            //markdayOffOrPunchIn();

            if (progressDoalog != null) {
                progressDoalog.dismiss();
            }
        }
    }

    @Override
    protected void onDestroy() {
        BLEAppLevel bleAppLevel = BLEAppLevel.getInstanceOnly();
        if (bleAppLevel != null) {
            bleAppLevel.disconnectBLECompletely();
        }
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.nav_drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (getSupportFragmentManager().getBackStackEntryCount() >= 2) {
                super.onBackPressed();
            } else {
                if (!exit) {
                    Toast.makeText(this, "Press back press to exit", Toast.LENGTH_SHORT).show();
                    exit = true;
                } else
                    finish();
            }

            Fragment currentFragment = getSupportFragmentManager().findFragmentById(frm_lyt_container_int);
            toolbar_title.setText(currentFragment.getTag());

            backPressHeaderHandle(currentFragment.getTag());


            Log.e("@@current Fragment ", currentFragment.getTag());
        }
    }

    private void backPressHeaderHandle(String tag) {
        switch (tag) {
         /*   case AVAILABLE_DEVICES:
                BLEAppLevel bleAppLevel = BLEAppLevel.getInstanceOnly();
                if (bleAppLevel != null) {
                    bleAppLevel.disconnectBLECompletely();
                }
                break;*/

            case DEVICE_MAP:
                if (MySharedPreference.getInstance(this).getStringData(ADDRESS).equalsIgnoreCase(""))
                    desc_txt.setText(MySharedPreference.getInstance(this).getStringData(ADDRESS));
                break;

            case DEVICE_DETAILS:
                if (MySharedPreference.getInstance(this).getStringData(lAST_CONNECTED).equalsIgnoreCase(""))
                    desc_txt.setText(MySharedPreference.getInstance(this).getStringData(lAST_CONNECTED));
                break;

            default:
                desc_txt.setText("");
                break;
        }
    }


}
