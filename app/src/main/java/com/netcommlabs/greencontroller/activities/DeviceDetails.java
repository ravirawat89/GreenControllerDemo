package com.netcommlabs.greencontroller.activities;

import android.bluetooth.BluetoothGattService;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.netcommlabs.greencontroller.Constants;
import com.netcommlabs.greencontroller.InterfaceValveAdapter;
import com.netcommlabs.greencontroller.R;
import com.netcommlabs.greencontroller.adapters.ValvesListAdapter;
import com.netcommlabs.greencontroller.model.DataTransferModel;
import com.netcommlabs.greencontroller.model.ModalBLEValve;
import com.netcommlabs.greencontroller.services.BleAdapterService;
import com.netcommlabs.greencontroller.sqlite_db.DatabaseHandler;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.SimpleTimeZone;
import java.util.TimeZone;

public class DeviceDetails extends AppCompatActivity implements InterfaceValveAdapter {

    private static final int REQUEST_CODE_SESN_PLAN = 201;
    public static final int RESULT_CODE_VALVE_INDB = 202;
    private RecyclerView reviValvesList;
    private DeviceDetails mContext;
    private ArrayList<String> listValves;
    private LinearLayout llScrnHeader, llNoSesnPlan, llSesnPlanDetails, llControllerNameEdit, llControllerNameSave;
    private LinearLayout llEditValve, llStopValve, llPauseValve, llFlushValve, llHelpValve;
    private TextView /*tvDeviceName,*/ tvAddNewSesnPlan;
    private DatabaseHandler databaseHandler;
    private ArrayList<DataTransferModel> listValveDataSingle;
    public static final String EXTRA_DVC_NAME = "dvc_name";
    public static final String EXTRA_DVC_MAC = "dvc_mac";
    public static final String EXTRA_DVC_VALVE_COUNT = "dvc_count";
    private String dvcName;
    private String dvcMacAdd;
    private int dvcValveCount;
    private String valveConctName, clickedValveName = "Valve 1";
    private TextView tvSunFirst, tvSunSecond, tvSunThird, tvSunFourth, tvMonFirst, tvMonSecond, tvMonThird, tvMonFourth, tvTueFirst, tvTueSecond, tvTueThird, tvTueFourth, tvWedFirst, tvWedSecond, tvWedThird, tvWedFourth, tvThuFirst, tvThuSecond, tvThuThird, tvThuFourth, tvFriFirst, tvFriSecond, tvFriThird, tvFriFourth, tvSatFirst, tvSatSecond, tvSatThird, tvSatFourth;
    private TextView tvDischargePnts, tvDuration, tvQuantity;
    private ArrayList<Integer> listTimePntsSun, listTimePntsMon, listTimePntsTue, listTimePntsWed, listTimePntsThu, listTimePntsFri, listTimePntsSat;
    private BleAdapterService bluetooth_le_adapter;
    private boolean back_requested = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.device_details);

        initBase();
    }

    private void initBase() {
        mContext = this;
        initView();
        initListeners();
        //Getting sent intent
        /*dvcName = getIntent().getExtras().getString(EXTRA_DVC_NAME);
        dvcMacAdd = getIntent().getExtras().getString(EXTRA_DVC_MAC);
        dvcValveCount = getIntent().getExtras().getInt(EXTRA_DVC_VALVE_COUNT);*/

        dvcName = "PEBBLE";
        dvcMacAdd = "98:4F:EE:10:87:66";
        dvcValveCount = 8;

//        tvDeviceName.setText(dvcName);

        //Adding valve name,MAC, and valve data to DB
        databaseHandler = new DatabaseHandler(mContext);
        List<ModalBLEValve> listGotModalBLEValvesNdData = databaseHandler.getAllValvesNdData();
        if (listGotModalBLEValvesNdData.size() == 0) {
            for (int i = 1; i <= dvcValveCount; i++) {
                valveConctName = "Valve " + i;
                //modalBLEValveDataHolder = ;
                //List for adding data to DB
                databaseHandler.addValveNdData(new ModalBLEValve(dvcMacAdd, valveConctName, listValveDataSingle));
                //listMdlBLEValve.add(modalBLEValveDataHolder);
            }
            listValves = databaseHandler.getAllValvesNameWithMAC(dvcMacAdd);
        } else {
            for (ModalBLEValve modalBLEValve : listGotModalBLEValvesNdData) {
                if (modalBLEValve.getDvcMacAddrs().equalsIgnoreCase(dvcMacAdd)) {
                    //Toast.makeText(mContext, "This MAC, Valve and Valve Data alrady added", Toast.LENGTH_SHORT).show();

                    //List for Valve RecyclerView to show valves
                    //listValves = new ArrayList<>();
                    listValves = databaseHandler.getAllValvesNameWithMAC(dvcMacAdd);
                  /*  if (modalBLEValve.getValveName().equalsIgnoreCase("Valve 1")){

                    }*/
                    break;
                } else {
                    //listMdlBLEValve = new ArrayList<>();
                    for (int i = 1; i <= dvcValveCount; i++) {
                        valveConctName = "Valve " + i;
                        //modalBLEValveDataHolder = ;
                        //List for adding data to DB
                        databaseHandler.addValveNdData(new ModalBLEValve(valveConctName, dvcMacAdd, listValveDataSingle));
                        //listMdlBLEValve.add(modalBLEValveDataHolder);
                    }
               /* for (ModalBLEValve modalBLEValveLocal : listMdlBLEValve) {

                }*/
                    break;
                }
            }
        }


        if (listValves != null && listValves.size() > 0) {
            LinearLayoutManager gridLayoutManager = new LinearLayoutManager(mContext);
            reviValvesList.setLayoutManager(gridLayoutManager);
            //reviValvesList.setAdapter(new ValvesListAdapter(mContext, listValves, dvcMacAdd));
        }


        //databasHandler.deleteAllRecordFromTable();
        //listDataTransferModels = databasHandler.getListDataTM();

       /* if (listDataTransferModels != null && listDataTransferModels.size() > 0) {
            llNoSesnPlan.setVisibility(View.GONE);
            llSesnPlanDetails.setVisibility(View.VISIBLE);
        } else {
            llNoSesnPlan.setVisibility(View.VISIBLE);
            llSesnPlanDetails.setVisibility(View.GONE);
        }*/
        listValveDataSingle = databaseHandler.getValveDataWithMACValveName(dvcMacAdd, clickedValveName);
        checkValveDataUpdtUIFrmDB();

        initController();

    }

    private void initView() {
        llScrnHeader = findViewById(R.id.llScrnHeader);
        llNoSesnPlan = findViewById(R.id.llNoSesnPlan);
        llSesnPlanDetails = findViewById(R.id.llSesnPlanDetails);
        llEditValve = findViewById(R.id.llEditValve);
        llStopValve = findViewById(R.id.llStopValve);
        llPauseValve = findViewById(R.id.llPauseValve);
        llFlushValve = findViewById(R.id.llFlushValve);
        llHelpValve = findViewById(R.id.llHelpValve);
//        tvDeviceName = findViewById(R.id.tvDeviceName);
        reviValvesList = findViewById(R.id.reviValvesList);
        tvAddNewSesnPlan = findViewById(R.id.tvAddNewSesnPlan);

        tvDischargePnts = findViewById(R.id.tvDischargePnts);
        tvDuration = findViewById(R.id.tvDuration);
        tvQuantity = findViewById(R.id.tvQuantity);

        tvSunFirst = (TextView) findViewById(R.id.tvSunFirst);
        tvSunSecond = (TextView) findViewById(R.id.tvSunSecond);
        tvSunThird = (TextView) findViewById(R.id.tvSunThird);
        tvSunFourth = (TextView) findViewById(R.id.tvSunFourth);

        tvMonFirst = (TextView) findViewById(R.id.tvMonFirst);
        tvMonSecond = (TextView) findViewById(R.id.tvMonSecond);
        tvMonThird = (TextView) findViewById(R.id.tvMonThird);
        tvMonFourth = (TextView) findViewById(R.id.tvMonFourth);

        tvTueFirst = (TextView) findViewById(R.id.tvTueFirst);
        tvTueSecond = (TextView) findViewById(R.id.tvTueSecond);
        tvTueThird = (TextView) findViewById(R.id.tvTueThird);
        tvTueFourth = (TextView) findViewById(R.id.tvTueFourth);

        tvWedFirst = (TextView) findViewById(R.id.tvWedFirst);
        tvWedSecond = (TextView) findViewById(R.id.tvWedSecond);
        tvWedThird = (TextView) findViewById(R.id.tvWedThird);
        tvWedFourth = (TextView) findViewById(R.id.tvWedFourth);

        tvThuFirst = (TextView) findViewById(R.id.tvThuFirst);
        tvThuSecond = (TextView) findViewById(R.id.tvThuSecond);
        tvThuThird = (TextView) findViewById(R.id.tvThuThird);
        tvThuFourth = (TextView) findViewById(R.id.tvThuFourth);

        tvFriFirst = (TextView) findViewById(R.id.tvFriFirst);
        tvFriSecond = (TextView) findViewById(R.id.tvFriSecond);
        tvFriThird = (TextView) findViewById(R.id.tvFriThird);
        tvFriFourth = (TextView) findViewById(R.id.tvFriFourth);

        tvSatFirst = (TextView) findViewById(R.id.tvSatFirst);
        tvSatSecond = (TextView) findViewById(R.id.tvSatSecond);
        tvSatThird = (TextView) findViewById(R.id.tvSatThird);
        tvSatFourth = (TextView) findViewById(R.id.tvSatFourth);
    }

    private void initListeners() {
        llScrnHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, DeviceActivity.class);
                mContext.startActivity(intent);
                mContext.finish();
            }
        });

        llEditValve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentAddNewSesnPln = new Intent(mContext, AddEditSessionPlan.class);
                intentAddNewSesnPln.putExtra(AddEditSessionPlan.EXTRA_NAME, dvcName);
                intentAddNewSesnPln.putExtra(AddEditSessionPlan.EXTRA_ID, dvcMacAdd);
                intentAddNewSesnPln.putExtra(AddEditSessionPlan.EXTRA_VALVE_NAME_DB, clickedValveName);
                intentAddNewSesnPln.putExtra(AddEditSessionPlan.EXTRA_VALVE_EDITABLE_DATA, listValveDataSingle);
                intentAddNewSesnPln.putExtra(AddEditSessionPlan.EXTRA_OPERATION_TYPE, "Edit");
                startActivityForResult(intentAddNewSesnPln, REQUEST_CODE_SESN_PLAN);
            }
        });

        tvAddNewSesnPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentAddNewSesnPln = new Intent(mContext, AddEditSessionPlan.class);
                intentAddNewSesnPln.putExtra(AddEditSessionPlan.EXTRA_NAME, dvcName);
                intentAddNewSesnPln.putExtra(AddEditSessionPlan.EXTRA_ID, dvcMacAdd);
                intentAddNewSesnPln.putExtra(AddEditSessionPlan.EXTRA_VALVE_NAME_DB, clickedValveName);
                intentAddNewSesnPln.putExtra(AddEditSessionPlan.EXTRA_OPERATION_TYPE, "Add");
                startActivityForResult(intentAddNewSesnPln, REQUEST_CODE_SESN_PLAN);

               /* if (bluetooth_le_adapter.isConnected()) {
                    try {
                        bluetooth_le_adapter.disconnect();
                    } catch (Exception e) {
                    }
                }
                unbindService(service_connection);
                bluetooth_le_adapter = null;*/
                //super.onDestroy();
                //startActivity(intentAddNewSesnPln);
                //finish();
            }
        });

        llStopValve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogConfirmAction();
            }
        });
    }

    private void dialogConfirmAction() {
        String title, msg;
        title = "Stop Valve";
        msg = "This will delete valve saved data";

        AlertDialog.Builder builder;
       /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(mContext, android.R.style.Theme_Material_Dialog_Alert);
        } else {*/
        builder = new AlertDialog.Builder(mContext);
        //}
        builder.setTitle(title)
                .setMessage(msg)
                .setCancelable(false)
                .setPositiveButton("Stop", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        byte[] valveCommand = {3};
                        if (bluetooth_le_adapter != null) {
                            bluetooth_le_adapter.writeCharacteristic(
                                    BleAdapterService.VALVE_CONTROLLER_SERVICE_UUID,
                                    BleAdapterService.COMMAND_CHARACTERISTIC_UUID, valveCommand
                            );
                        } else {
                            Toast.makeText(mContext, "adapter connection lost", Toast.LENGTH_SHORT).show();
                        }

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    @Override
    public void clickPassDataToAct(ArrayList<DataTransferModel> listValveDataSingleLocal, String clickedValveName,int pos) {
        this.clickedValveName = clickedValveName;
        this.listValveDataSingle = listValveDataSingleLocal;
        checkValveDataUpdtUIFrmDB();
    }

    @Override
    public void onRecyclerItemClickedNameAdress(String name, String address) {

    }

    private void checkValveDataUpdtUIFrmDB() {
        if (listValveDataSingle != null && listValveDataSingle.size() > 0) {
            llNoSesnPlan.setVisibility(View.GONE);
            setValveDataToUI();
        } else {
            llNoSesnPlan.setVisibility(View.VISIBLE);
            llSesnPlanDetails.setVisibility(View.GONE);
        }
    }

    private void setValveDataToUI() {
        setTimePntsVisibilityGONE();
        llSesnPlanDetails.setVisibility(View.VISIBLE);
        //initListeners();

        DataTransferModel dataTransferModel;
        listTimePntsSun = new ArrayList<Integer>();
        listTimePntsMon = new ArrayList<Integer>();
        listTimePntsTue = new ArrayList<Integer>();
        listTimePntsWed = new ArrayList<Integer>();
        listTimePntsThu = new ArrayList<Integer>();
        listTimePntsFri = new ArrayList<Integer>();
        listTimePntsSat = new ArrayList<Integer>();
        int dischargePnts = 0, duration = 0, quantity = 0;

        for (int i = 0; i < listValveDataSingle.size(); i++) {
            dataTransferModel = listValveDataSingle.get(i);

            dischargePnts = dataTransferModel.getDischarge();
            duration = dataTransferModel.getDuration();
            quantity = dataTransferModel.getQty();

            //For Sunday
            if (dataTransferModel.getDayOfTheWeek() == 1) {
                listTimePntsSun.add(dataTransferModel.getHours());
            }
            if (dataTransferModel.getDayOfTheWeek() == 2) {
                listTimePntsMon.add(dataTransferModel.getHours());
            }
            if (dataTransferModel.getDayOfTheWeek() == 3) {
                listTimePntsTue.add(dataTransferModel.getHours());
            }
            if (dataTransferModel.getDayOfTheWeek() == 4) {
                listTimePntsWed.add(dataTransferModel.getHours());
            }
            if (dataTransferModel.getDayOfTheWeek() == 5) {
                listTimePntsThu.add(dataTransferModel.getHours());
            }
            if (dataTransferModel.getDayOfTheWeek() == 6) {
                listTimePntsFri.add(dataTransferModel.getHours());
            }
            if (dataTransferModel.getDayOfTheWeek() == 7) {
                listTimePntsSat.add(dataTransferModel.getHours());
            }

        }

        tvDischargePnts.setText(dischargePnts + " Unit");
        tvDuration.setText(duration + " Min");
        tvQuantity.setText(quantity + " ML");
        String timePntsUserFriendly = "";

        if (llEditValve.getVisibility() != View.VISIBLE) {
            llEditValve.setVisibility(View.VISIBLE);
            llStopValve.setVisibility(View.VISIBLE);
            llPauseValve.setVisibility(View.VISIBLE);
            llFlushValve.setVisibility(View.VISIBLE);
            llHelpValve.setVisibility(View.VISIBLE);
        }

        if (listTimePntsSun.size() > 0) {
            for (int i = 0; i < listTimePntsSun.size(); i++) {
                if (tvSunFirst.getVisibility() != View.VISIBLE) {
                    tvSunFirst.setVisibility(View.VISIBLE);
                    String timePntString = listTimePntsSun.get(i).toString();
                    if (timePntString.length() == 1) {
                        timePntsUserFriendly = "0" + timePntString + ":00";
                    } else {
                        timePntsUserFriendly = timePntString + ":00";
                    }
                    tvSunFirst.setText(timePntsUserFriendly);
                    continue;
                }
                if (tvSunSecond.getVisibility() != View.VISIBLE) {
                    tvSunSecond.setVisibility(View.VISIBLE);
                    String timePntString = listTimePntsSun.get(i).toString();
                    if (timePntString.length() == 1) {
                        timePntsUserFriendly = "0" + timePntString + ":00";
                    } else {
                        timePntsUserFriendly = timePntString + ":00";
                    }
                    tvSunSecond.setText(timePntsUserFriendly);
                    continue;
                }
                if (tvSunThird.getVisibility() != View.VISIBLE) {
                    tvSunThird.setVisibility(View.VISIBLE);
                    String timePntString = listTimePntsSun.get(i).toString();
                    if (timePntString.length() == 1) {
                        timePntsUserFriendly = "0" + timePntString + ":00";
                    } else {
                        timePntsUserFriendly = timePntString + ":00";
                    }
                    tvSunThird.setText(timePntsUserFriendly);
                    continue;
                }
                if (tvSunFourth.getVisibility() != View.VISIBLE) {
                    tvSunFourth.setVisibility(View.VISIBLE);
                    String timePntString = listTimePntsSun.get(i).toString();
                    if (timePntString.length() == 1) {
                        timePntsUserFriendly = "0" + timePntString + ":00";
                    } else {
                        timePntsUserFriendly = timePntString + ":00";
                    }
                    tvSunFourth.setText(timePntsUserFriendly);
                    continue;
                }
            }
        }

        if (listTimePntsMon.size() > 0) {
            for (int i = 0; i < listTimePntsMon.size(); i++) {
                if (tvMonFirst.getVisibility() != View.VISIBLE) {
                    tvMonFirst.setVisibility(View.VISIBLE);
                    String timePntString = listTimePntsMon.get(i).toString();
                    if (timePntString.length() == 1) {
                        timePntsUserFriendly = "0" + timePntString + ":00";
                    } else {
                        timePntsUserFriendly = timePntString + ":00";
                    }
                    tvMonFirst.setText(timePntsUserFriendly);
                    continue;
                }
                if (tvMonSecond.getVisibility() != View.VISIBLE) {
                    tvMonSecond.setVisibility(View.VISIBLE);
                    String timePntString = listTimePntsMon.get(i).toString();
                    if (timePntString.length() == 1) {
                        timePntsUserFriendly = "0" + timePntString + ":00";
                    } else {
                        timePntsUserFriendly = timePntString + ":00";
                    }
                    tvMonSecond.setText(timePntsUserFriendly);
                    continue;
                }
                if (tvMonThird.getVisibility() != View.VISIBLE) {
                    tvMonThird.setVisibility(View.VISIBLE);
                    String timePntString = listTimePntsMon.get(i).toString();
                    if (timePntString.length() == 1) {
                        timePntsUserFriendly = "0" + timePntString + ":00";
                    } else {
                        timePntsUserFriendly = timePntString + ":00";
                    }
                    tvMonThird.setText(timePntsUserFriendly);
                    continue;
                }
                if (tvMonFourth.getVisibility() != View.VISIBLE) {
                    tvMonFourth.setVisibility(View.VISIBLE);
                    String timePntString = listTimePntsMon.get(i).toString();
                    if (timePntString.length() == 1) {
                        timePntsUserFriendly = "0" + timePntString + ":00";
                    } else {
                        timePntsUserFriendly = timePntString + ":00";
                    }
                    tvMonFourth.setText(timePntsUserFriendly);
                    continue;
                }
            }
        }

        if (listTimePntsTue.size() > 0) {
            for (int i = 0; i < listTimePntsTue.size(); i++) {
                if (tvTueFirst.getVisibility() != View.VISIBLE) {
                    tvTueFirst.setVisibility(View.VISIBLE);
                    String timePntString = listTimePntsTue.get(i).toString();
                    if (timePntString.length() == 1) {
                        timePntsUserFriendly = "0" + timePntString + ":00";
                    } else {
                        timePntsUserFriendly = timePntString + ":00";
                    }
                    tvTueFirst.setText(timePntsUserFriendly);
                    continue;
                }
                if (tvTueSecond.getVisibility() != View.VISIBLE) {
                    tvTueSecond.setVisibility(View.VISIBLE);
                    String timePntString = listTimePntsTue.get(i).toString();
                    if (timePntString.length() == 1) {
                        timePntsUserFriendly = "0" + timePntString + ":00";
                    } else {
                        timePntsUserFriendly = timePntString + ":00";
                    }
                    tvTueSecond.setText(timePntsUserFriendly);
                    continue;
                }
                if (tvTueThird.getVisibility() != View.VISIBLE) {
                    tvTueThird.setVisibility(View.VISIBLE);
                    String timePntString = listTimePntsTue.get(i).toString();
                    if (timePntString.length() == 1) {
                        timePntsUserFriendly = "0" + timePntString + ":00";
                    } else {
                        timePntsUserFriendly = timePntString + ":00";
                    }
                    tvTueThird.setText(timePntsUserFriendly);
                    continue;
                }
                if (tvTueFourth.getVisibility() != View.VISIBLE) {
                    tvTueFourth.setVisibility(View.VISIBLE);
                    String timePntString = listTimePntsTue.get(i).toString();
                    if (timePntString.length() == 1) {
                        timePntsUserFriendly = "0" + timePntString + ":00";
                    } else {
                        timePntsUserFriendly = timePntString + ":00";
                    }
                    tvTueFourth.setText(timePntsUserFriendly);
                    continue;
                }
            }
        }

        if (listTimePntsWed.size() > 0) {
            for (int i = 0; i < listTimePntsWed.size(); i++) {
                if (tvWedFirst.getVisibility() != View.VISIBLE) {
                    tvWedFirst.setVisibility(View.VISIBLE);
                    String timePntString = listTimePntsWed.get(i).toString();
                    if (timePntString.length() == 1) {
                        timePntsUserFriendly = "0" + timePntString + ":00";
                    } else {
                        timePntsUserFriendly = timePntString + ":00";
                    }
                    tvWedFirst.setText(timePntsUserFriendly);
                    continue;
                }
                if (tvWedSecond.getVisibility() != View.VISIBLE) {
                    tvWedSecond.setVisibility(View.VISIBLE);
                    String timePntString = listTimePntsWed.get(i).toString();
                    if (timePntString.length() == 1) {
                        timePntsUserFriendly = "0" + timePntString + ":00";
                    } else {
                        timePntsUserFriendly = timePntString + ":00";
                    }
                    tvWedSecond.setText(timePntsUserFriendly);
                    continue;
                }
                if (tvWedThird.getVisibility() != View.VISIBLE) {
                    tvWedThird.setVisibility(View.VISIBLE);
                    String timePntString = listTimePntsWed.get(i).toString();
                    if (timePntString.length() == 1) {
                        timePntsUserFriendly = "0" + timePntString + ":00";
                    } else {
                        timePntsUserFriendly = timePntString + ":00";
                    }
                    tvWedThird.setText(timePntsUserFriendly);
                    continue;
                }
                if (tvWedFourth.getVisibility() != View.VISIBLE) {
                    tvWedFourth.setVisibility(View.VISIBLE);
                    String timePntString = listTimePntsWed.get(i).toString();
                    if (timePntString.length() == 1) {
                        timePntsUserFriendly = "0" + timePntString + ":00";
                    } else {
                        timePntsUserFriendly = timePntString + ":00";
                    }
                    tvWedFourth.setText(timePntsUserFriendly);
                    continue;
                }
            }
        }

        if (listTimePntsThu.size() > 0) {
            for (int i = 0; i < listTimePntsThu.size(); i++) {
                if (tvThuFirst.getVisibility() != View.VISIBLE) {
                    tvThuFirst.setVisibility(View.VISIBLE);
                    String timePntString = listTimePntsThu.get(i).toString();
                    if (timePntString.length() == 1) {
                        timePntsUserFriendly = "0" + timePntString + ":00";
                    } else {
                        timePntsUserFriendly = timePntString + ":00";
                    }
                    tvThuFirst.setText(timePntsUserFriendly);
                    continue;
                }
                if (tvThuSecond.getVisibility() != View.VISIBLE) {
                    tvThuSecond.setVisibility(View.VISIBLE);
                    String timePntString = listTimePntsThu.get(i).toString();
                    if (timePntString.length() == 1) {
                        timePntsUserFriendly = "0" + timePntString + ":00";
                    } else {
                        timePntsUserFriendly = timePntString + ":00";
                    }
                    tvThuSecond.setText(timePntsUserFriendly);
                    continue;
                }
                if (tvThuThird.getVisibility() != View.VISIBLE) {
                    tvThuThird.setVisibility(View.VISIBLE);
                    String timePntString = listTimePntsThu.get(i).toString();
                    if (timePntString.length() == 1) {
                        timePntsUserFriendly = "0" + timePntString + ":00";
                    } else {
                        timePntsUserFriendly = timePntString + ":00";
                    }
                    tvThuThird.setText(timePntsUserFriendly);
                    continue;
                }
                if (tvThuFourth.getVisibility() != View.VISIBLE) {
                    tvThuFourth.setVisibility(View.VISIBLE);
                    String timePntString = listTimePntsThu.get(i).toString();
                    if (timePntString.length() == 1) {
                        timePntsUserFriendly = "0" + timePntString + ":00";
                    } else {
                        timePntsUserFriendly = timePntString + ":00";
                    }
                    tvThuFourth.setText(timePntsUserFriendly);
                    continue;
                }
            }
        }

        if (listTimePntsFri.size() > 0) {
            for (int i = 0; i < listTimePntsFri.size(); i++) {
                if (tvFriFirst.getVisibility() != View.VISIBLE) {
                    tvFriFirst.setVisibility(View.VISIBLE);
                    String timePntString = listTimePntsFri.get(i).toString();
                    if (timePntString.length() == 1) {
                        timePntsUserFriendly = "0" + timePntString + ":00";
                    } else {
                        timePntsUserFriendly = timePntString + ":00";
                    }
                    tvFriFirst.setText(timePntsUserFriendly);
                    continue;
                }
                if (tvFriSecond.getVisibility() != View.VISIBLE) {
                    tvFriSecond.setVisibility(View.VISIBLE);
                    String timePntString = listTimePntsFri.get(i).toString();
                    if (timePntString.length() == 1) {
                        timePntsUserFriendly = "0" + timePntString + ":00";
                    } else {
                        timePntsUserFriendly = timePntString + ":00";
                    }
                    tvFriSecond.setText(timePntsUserFriendly);
                    continue;
                }
                if (tvFriThird.getVisibility() != View.VISIBLE) {
                    tvFriThird.setVisibility(View.VISIBLE);
                    String timePntString = listTimePntsFri.get(i).toString();
                    if (timePntString.length() == 1) {
                        timePntsUserFriendly = "0" + timePntString + ":00";
                    } else {
                        timePntsUserFriendly = timePntString + ":00";
                    }
                    tvFriThird.setText(timePntsUserFriendly);
                    continue;
                }
                if (tvFriFourth.getVisibility() != View.VISIBLE) {
                    tvFriFourth.setVisibility(View.VISIBLE);
                    String timePntString = listTimePntsFri.get(i).toString();
                    if (timePntString.length() == 1) {
                        timePntsUserFriendly = "0" + timePntString + ":00";
                    } else {
                        timePntsUserFriendly = timePntString + ":00";
                    }
                    tvFriFourth.setText(timePntsUserFriendly);
                    continue;
                }
            }
        }

        if (listTimePntsSat.size() > 0) {
            for (int i = 0; i < listTimePntsSat.size(); i++) {
                if (tvSatFirst.getVisibility() != View.VISIBLE) {
                    tvSatFirst.setVisibility(View.VISIBLE);
                    String timePntString = listTimePntsSat.get(i).toString();
                    if (timePntString.length() == 1) {
                        timePntsUserFriendly = "0" + timePntString + ":00";
                    } else {
                        timePntsUserFriendly = timePntString + ":00";
                    }
                    tvSatFirst.setText(timePntsUserFriendly);
                    continue;
                }
                if (tvSatSecond.getVisibility() != View.VISIBLE) {
                    tvSatSecond.setVisibility(View.VISIBLE);
                    String timePntString = listTimePntsSat.get(i).toString();
                    if (timePntString.length() == 1) {
                        timePntsUserFriendly = "0" + timePntString + ":00";
                    } else {
                        timePntsUserFriendly = timePntString + ":00";
                    }
                    tvSatSecond.setText(timePntsUserFriendly);
                    continue;
                }
                if (tvSatThird.getVisibility() != View.VISIBLE) {
                    tvSatThird.setVisibility(View.VISIBLE);
                    String timePntString = listTimePntsSat.get(i).toString();
                    if (timePntString.length() == 1) {
                        timePntsUserFriendly = "0" + timePntString + ":00";
                    } else {
                        timePntsUserFriendly = timePntString + ":00";
                    }
                    tvSatThird.setText(timePntsUserFriendly);
                    continue;
                }
                if (tvSatFourth.getVisibility() != View.VISIBLE) {
                    tvSatFourth.setVisibility(View.VISIBLE);
                    String timePntString = listTimePntsSat.get(i).toString();
                    if (timePntString.length() == 1) {
                        timePntsUserFriendly = "0" + timePntString + ":00";
                    } else {
                        timePntsUserFriendly = timePntString + ":00";
                    }
                    tvSatFourth.setText(timePntsUserFriendly);
                    continue;
                }
            }
        }


    }

    private void setTimePntsVisibilityGONE() {
        tvSunFirst.setVisibility(View.GONE);
        tvSunSecond.setVisibility(View.GONE);
        tvSunThird.setVisibility(View.GONE);
        tvSunFourth.setVisibility(View.GONE);

        tvMonFirst.setVisibility(View.GONE);
        tvMonSecond.setVisibility(View.GONE);
        tvMonThird.setVisibility(View.GONE);
        tvMonFourth.setVisibility(View.GONE);

        tvTueFirst.setVisibility(View.GONE);
        tvTueSecond.setVisibility(View.GONE);
        tvTueThird.setVisibility(View.GONE);
        tvTueFourth.setVisibility(View.GONE);

        tvWedFirst.setVisibility(View.GONE);
        tvWedSecond.setVisibility(View.GONE);
        tvWedThird.setVisibility(View.GONE);
        tvWedFourth.setVisibility(View.GONE);

        tvThuFirst.setVisibility(View.GONE);
        tvThuSecond.setVisibility(View.GONE);
        tvThuThird.setVisibility(View.GONE);
        tvThuFourth.setVisibility(View.GONE);

        tvFriFirst.setVisibility(View.GONE);
        tvFriSecond.setVisibility(View.GONE);
        tvFriThird.setVisibility(View.GONE);
        tvFriFourth.setVisibility(View.GONE);

        tvSatFirst.setVisibility(View.GONE);
        tvSatSecond.setVisibility(View.GONE);
        tvSatThird.setVisibility(View.GONE);
        tvSatFourth.setVisibility(View.GONE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_SESN_PLAN:
                if (resultCode == RESULT_CODE_VALVE_INDB) {
                    this.listValveDataSingle = databaseHandler.getValveDataWithMACValveName(dvcMacAdd, clickedValveName);
                    checkValveDataUpdtUIFrmDB();
                }
        }
    }

    private void initSTOPbtnEffectes() {
        listValveDataSingle = null;
        if (databaseHandler.updateValveDataWithMAC_ValveName(dvcMacAdd, clickedValveName, listValveDataSingle) == 1) {
            llNoSesnPlan.setVisibility(View.VISIBLE);
            llSesnPlanDetails.setVisibility(View.GONE);

            Toast.makeText(mContext, "Session Plan stopped successfully", Toast.LENGTH_LONG).show();
        }
    }

    //BLE CODE STARTS
    void initController() {
       /* device_name = "Pebble";
        device_address = "98:4F:EE:10:87:66";*/
        Intent gattServiceIntent = new Intent(getApplicationContext(), BleAdapterService.class);
        bindService(gattServiceIntent, service_connection, BIND_AUTO_CREATE);
    }

    private final ServiceConnection service_connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            bluetooth_le_adapter = ((BleAdapterService.LocalBinder) service).getService();
            bluetooth_le_adapter.setActivityHandler(message_handler);
            if (bluetooth_le_adapter != null) {
                bluetooth_le_adapter.connect(dvcMacAdd);
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
                        //setConnectionIsDone();


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

                    if (bundle.get(BleAdapterService.PARCEL_CHARACTERISTIC_UUID).toString().toUpperCase().equals(BleAdapterService.COMMAND_CHARACTERISTIC_UUID)) {
                        //Toast.makeText(mContext, "ACK STOP", Toast.LENGTH_SHORT).show();
                        initSTOPbtnEffectes();
                    } else {
                        Toast.makeText(mContext, "BLE device not reposding", Toast.LENGTH_SHORT).show();
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

    //BLE CODE ENDS


    /*private void initListeners() {
        llControllerNameEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ivEditPen.setBackgroundResource(R.drawable.circle_wo_shadow_light);

                llControllerNameEdit.setVisibility(View.GONE);
                llControllerNameSave.setVisibility(View.VISIBLE);

                etContrlrName.setText("");
                etContrlrName.requestFocus();


                Toast.makeText(mContext, "Enter Controller Name", Toast.LENGTH_SHORT).show();

            }
        });

        ivSaveCntrlrName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String enteredCNTName = etContrlrName.getText().toString();
                if (enteredCNTName.isEmpty()) {
                    Toast.makeText(mContext, "Controller name can't be empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                llControllerNameEdit.setVisibility(View.VISIBLE);
                llControllerNameSave.setVisibility(View.GONE);
                tvCntrlrName.setText(enteredCNTName);
                Toast.makeText(mContext, "Name Edited Successfully", Toast.LENGTH_SHORT).show();
            }
        });

      *//*
*//*
        llReconnectCNT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "Reconnect", Toast.LENGTH_SHORT).show();
            }
        });

        llPauseCNT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "Pause", Toast.LENGTH_SHORT).show();
            }
        });

        llDeleteCNT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "Delete", Toast.LENGTH_SHORT).show();
            }
        });
*/
       /* llPauseValve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "Pause Valve", Toast.LENGTH_SHORT).show();
            }
        });

        llFlushValve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "Flush Valve", Toast.LENGTH_SHORT).show();
            }
        });*/

      /*  tvEditProfileEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "Edit Profile", Toast.LENGTH_SHORT).show();
            }
        });

        tvChooseProfileEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "Choose Profile", Toast.LENGTH_SHORT).show();
            }
        });*/

  /*  @Override
    public void onBackPressed() {
        if (llControllerNameSave.getVisibility() == View.VISIBLE) {
            llControllerNameEdit.setVisibility(View.VISIBLE);
            llControllerNameSave.setVisibility(View.GONE);
            return;
        }
        super.onBackPressed();
    }*/

  /*  @Override
    public void clickPassDataToAct(String s) {
        tvValveNameAct.setText(s);
    }*/

}
