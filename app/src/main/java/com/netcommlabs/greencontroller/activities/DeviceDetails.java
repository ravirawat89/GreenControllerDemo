package com.netcommlabs.greencontroller.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.netcommlabs.greencontroller.InterfaceValveAdapter;
import com.netcommlabs.greencontroller.R;
import com.netcommlabs.greencontroller.adapters.ValvesListAdapter;
import com.netcommlabs.greencontroller.model.DataTransferModel;
import com.netcommlabs.greencontroller.model.ModalBLEValve;
import com.netcommlabs.greencontroller.sqlite_db.DatabaseHandler;

import java.util.ArrayList;
import java.util.List;

public class DeviceDetails extends AppCompatActivity implements InterfaceValveAdapter {

    private static final int REQUEST_CODE_SESN_PLAN = 201;
    public static final int RESULT_CODE_VALVE_INDB = 202;
    private RecyclerView reviValvesList;
    private DeviceDetails mContext;
    private List<String> listValves;
    private LinearLayout llScrnHeader, llNoSesnPlan, llSesnPlanDetails, llControllerNameEdit, llControllerNameSave, llStartStopCNT, llReconnectCNT, llPauseCNT, llDeleteCNT, llPauseValve, llFlushValve;
    private EditText etContrlrName;
    private TextView tvDeviceName, tvAddNewSesnPlan;/* tvEditProfileEvent, tvChooseProfileEvent, tvValveNameAct, tvStartStop*/
    private ImageView ivSaveCntrlrName, ivEditPen;
    private DatabaseHandler databaseHandler;
    private List<DataTransferModel> listDataTransferModels;

    public static final String EXTRA_DVC_NAME = "dvc_name";
    public static final String EXTRA_DVC_MAC = "dvc_mac";
    public static final String EXTRA_DVC_VALVE_COUNT = "dvc_count";
    private String dvcName;
    private String dvcMacAdd;
    private int dvcValveCount;
    private ModalBLEValve modalBLEValveDataHolder;
    private List<ModalBLEValve> listMdlBLEValve;
    private String valveConctName, clickedValveName = "Valve 1";
    private List<DataTransferModel> listValveDataSingle;
    private TextView tvSunFirst, tvSunSecond, tvSunThird, tvSunFourth, tvMonFirst, tvMonSecond, tvMonThird, tvMonFourth, tvTueFirst, tvTueSecond, tvTueThird, tvTueFourth, tvWedFirst, tvWedSecond, tvWedThird, tvWedFourth, tvThuFirst, tvThuSecond, tvThuThird, tvThuFourth, tvFriFirst, tvFriSecond, tvFriThird, tvFriFourth, tvSatFirst, tvSatSecond, tvSatThird, tvSatFourth;
    private TextView tvDischargePnts, tvDuration, tvQuantity;
    private ArrayList<Integer> listTimePntsSun, listTimePntsMon, listTimePntsTue, listTimePntsWed, listTimePntsThu, listTimePntsFri, listTimePntsSat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.device_details);

        initBase();

        initListeners();
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

        tvAddNewSesnPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentAddNewSesnPln = new Intent(mContext, AddEditSessionPlan.class);
                intentAddNewSesnPln.putExtra(AddEditSessionPlan.EXTRA_NAME, dvcName);
                intentAddNewSesnPln.putExtra(AddEditSessionPlan.EXTRA_ID, dvcMacAdd);
                intentAddNewSesnPln.putExtra(AddEditSessionPlan.EXTRA_VALVE_NAME_DB, clickedValveName);
                startActivityForResult(intentAddNewSesnPln, REQUEST_CODE_SESN_PLAN);
                //startActivity(intentAddNewSesnPln);
                //finish();
            }
        });
    }

    private void initBase() {
        mContext = this;
        llScrnHeader = findViewById(R.id.llScrnHeader);
        llNoSesnPlan = findViewById(R.id.llNoSesnPlan);
        llSesnPlanDetails = findViewById(R.id.llSesnPlanDetails);
        tvDeviceName = findViewById(R.id.tvDeviceName);
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

        //tvStartStop=(TextView)findViewById(R.id.tvStartStop);
      /*  tvValveNameAct = (TextView) findViewById(R.id.tvValveNameAct);
        tvEditProfileEvent = (TextView) findViewById(R.id.tvEditProfileEvent);
        tvChooseProfileEvent = (TextView) findViewById(R.id.tvChooseProfileEvent);*/
        //tvCntrlrName = (TextView) findViewById(R.id.tvCntrlrName);
       /* ivSaveCntrlrName = (ImageView) findViewById(R.id.ivSaveCntrlrName);
        ivEditPen = (ImageView) findViewById(R.id.ivEditPen);
        etContrlrName = (EditText) findViewById(R.id.etContrlrName);
        llControllerNameEdit = (LinearLayout) findViewById(R.id.llControllerNameEdit);
        llControllerNameSave = (LinearLayout) findViewById(R.id.llControllerNameSave);
        llStartStopCNT = (LinearLayout) findViewById(R.id.llStartStopCNT);
        llReconnectCNT = (LinearLayout) findViewById(R.id.llReconnectCNT);
        llPauseCNT = (LinearLayout) findViewById(R.id.llPauseCNT);
        llPauseValve = (LinearLayout) findViewById(R.id.llPauseValve);
        llFlushValve = (LinearLayout) findViewById(R.id.llFlushValve);*/

        //Getting sent intent
        /*dvcName = getIntent().getExtras().getString(EXTRA_DVC_NAME);
        dvcMacAdd = getIntent().getExtras().getString(EXTRA_DVC_MAC);
        dvcValveCount = getIntent().getExtras().getInt(EXTRA_DVC_VALVE_COUNT);*/

        dvcName = "PEBBLE";
        dvcMacAdd = "98:4F:EE:10:87:66";
        dvcValveCount = 8;

        tvDeviceName.setText(dvcName);

        //Adding valve name,MAC, and valve data to DB
        databaseHandler = new DatabaseHandler(mContext);
        List<ModalBLEValve> listGotModalBLEValvesNdData = databaseHandler.getAllValvesNdData();
        if (listGotModalBLEValvesNdData.size() == 0) {
            for (int i = 1; i <= dvcValveCount; i++) {
                valveConctName = "Valve " + i;
                //modalBLEValveDataHolder = ;
                //List for adding data to DB
                databaseHandler.addValveNdData(new ModalBLEValve(dvcMacAdd, valveConctName, listDataTransferModels));
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
                        databaseHandler.addValveNdData(new ModalBLEValve(valveConctName, dvcMacAdd, listDataTransferModels));
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
            reviValvesList.setAdapter(new ValvesListAdapter(mContext, listValves, dvcMacAdd));
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
        if (listValveDataSingle != null) {
            this.listValveDataSingle.clear();
            this.listValveDataSingle = null;
        }
        listValveDataSingle = databaseHandler.getValveDataWithMACValveName(dvcMacAdd, clickedValveName);
        checkValveDataNdUpdateUIFromDB();

    }

    @Override
    public void clickPassDataToAct(List<DataTransferModel> listValveDataSingleLocal, String clickedValveName) {
        this.clickedValveName = clickedValveName;
        if (listValveDataSingle != null) {
            this.listValveDataSingle.clear();
            this.listValveDataSingle = null;
        }
        this.listValveDataSingle = listValveDataSingleLocal;
        checkValveDataNdUpdateUIFromDB();
    }

    @Override
    public void onRecyclerItemClickedNameAdress(String name, String address) {

    }

    private void checkValveDataNdUpdateUIFromDB() {
        //this.listValveDataSingle = listValveDataSingle;
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
        //Clear list to avoid repeatation
        if (listValveDataSingle != null) {
            this.listValveDataSingle.clear();
            this.listValveDataSingle = null;
        }

        tvDischargePnts.setText(dischargePnts + " Unit");
        tvDuration.setText(duration + " Min");
        tvQuantity.setText(quantity + " ML");
        String timePntsUserFriendly = "";

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
                    if (listValveDataSingle != null) {
                        this.listValveDataSingle.clear();
                        this.listValveDataSingle = null;
                    }
                    this.listValveDataSingle = databaseHandler.getValveDataWithMACValveName(dvcMacAdd, clickedValveName);
                    checkValveDataNdUpdateUIFromDB();
                }
        }
    }

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

      *//*  llStartStopCNT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvStartStop=v.findViewById(R.id.tvStartStop);
                if (tvStartStop.getText().equals("Start")){
                    tvStartStop.setText("Stop");
                }else {
                    tvStartStop.setText("Start");
                }
            }
        });
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
