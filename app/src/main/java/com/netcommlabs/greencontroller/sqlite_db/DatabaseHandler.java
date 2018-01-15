package com.netcommlabs.greencontroller.sqlite_db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.netcommlabs.greencontroller.model.DataTransferModel;
import com.netcommlabs.greencontroller.model.MdlAddressNdLocation;
import com.netcommlabs.greencontroller.model.MdlValveNameStateNdSelect;
import com.netcommlabs.greencontroller.model.ModalBLEDevice;
import com.netcommlabs.greencontroller.model.ModalValveBirth;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Android on 7/26/2017.
 */

public class DatabaseHandler extends SQLiteOpenHelper {

    private Gson gson;

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "db_green_controller";

    // BLE DVC table name
    private static final String TABLE_BLE_DEVICE = "tbl_ble_devices";
    //BLE VALVE table name
    private static final String TABLE_BLE_VALVE = "tbl_ble_valve";

    // BLE DVC Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_DVC_NAME = "dvc_name";
    private static final String KEY_DVC_MAC = "dvc_mac";
    private static final String KEY_DVC_LOC_ADDRESS = "location_address";
    private static final String KEY_DVC_NUMBER_VALVES = "dvc_valve_num";

    // BLE Valves Table Columns names
    private static final String KEY_ID_VALVE = "id";
    private static final String KEY_VALVE_NAME = "valve_name";
    private static final String KEY_VALVE_DATA = "valve_data";
    //private static final String KEY_PLAY_PAUSE_CMD = "play_pause";
    private static final String KEY_FLUSH_CMD = "flush_cmd";
    private String KEY_SELECTED = "valveSelected";
    private String KEY_VALVE_STATE = "valveState";

    private byte[] byteArrayListValves;
    private List<String> listValvesFromDB;
    private List<DataTransferModel> listMdlValveData;

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

        gson = new Gson();
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_BLE_DVC_TABLE = "CREATE TABLE " + TABLE_BLE_DEVICE + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_DVC_NAME + " TEXT,"
                + KEY_DVC_MAC + " TEXT," + KEY_DVC_LOC_ADDRESS + " TEXT," + KEY_DVC_NUMBER_VALVES + " INTEGER" + ")";

        String CREATE_BLE_VALVE_TABLE = "CREATE TABLE " + TABLE_BLE_VALVE + "("
                + KEY_ID_VALVE + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_DVC_MAC + " TEXT," + KEY_VALVE_NAME + " TEXT," + KEY_VALVE_DATA + " TEXT," + KEY_SELECTED + " TEXT," + KEY_VALVE_STATE + " TEXT," + KEY_FLUSH_CMD + " TEXT" + ")";


        db.execSQL(CREATE_BLE_DVC_TABLE);
        db.execSQL(CREATE_BLE_VALVE_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BLE_DEVICE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BLE_VALVE);

        // Create tables again
        onCreate(db);
    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */

    // Adding new BLE DVC
    public void addBLEDevice(ModalBLEDevice modalBleDevice) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        //Converting Address modal to String/byte array
        byte[] byteArrayAddress = gson.toJson(modalBleDevice.getMdlLocationAddress()).getBytes();
        values.put(KEY_DVC_LOC_ADDRESS, byteArrayAddress);
        values.put(KEY_DVC_NAME, modalBleDevice.getName());
        values.put(KEY_DVC_MAC, modalBleDevice.getDvcMacAddrs());
        values.put(KEY_DVC_NUMBER_VALVES, modalBleDevice.getValvesNum());
        // Inserting Row
        db.insert(TABLE_BLE_DEVICE, null, values);
        db.close(); // Closing database connection
    }


    // Adding new BLE Valve
    public void setValveDataNdPropertiesBirth(ModalValveBirth modalBLEValve) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_DVC_MAC, modalBLEValve.getDvcMacAddrs());
        values.put(KEY_VALVE_NAME, modalBLEValve.getValveName());
        //Converting collection into String(byteArray -BLOB)
        byteArrayListValves = gson.toJson(modalBLEValve.getListValveData()).getBytes();
        values.put(KEY_VALVE_DATA, byteArrayListValves);
        //values.put(KEY_PLAY_PAUSE_CMD, modalBLEValve.getPlayPauseStatus());
        values.put(KEY_SELECTED, modalBLEValve.getValveSelected());
        values.put(KEY_VALVE_STATE, modalBLEValve.getValveState());
        values.put(KEY_FLUSH_CMD, modalBLEValve.getFlushStatus());
        // Inserting Row
        db.insert(TABLE_BLE_VALVE, null, values);
        db.close(); // Closing database connection
    }

    // Getting All Valves and Data
    public List<ModalValveBirth> getAllValvesNdData() {
        ArrayList<ModalValveBirth> listMdlBLEDvcs = new ArrayList<>();
        ArrayList<DataTransferModel> listAddEditValveData = null;
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_BLE_VALVE;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                ModalValveBirth modalBLEValve = new ModalValveBirth();
                modalBLEValve.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_ID_VALVE))));
                modalBLEValve.setDvcMacAddrs(cursor.getString(cursor.getColumnIndex(KEY_DVC_MAC)));
                modalBLEValve.setValveName(cursor.getString(cursor.getColumnIndex(KEY_VALVE_NAME)));

                byte[] blob = cursor.getBlob(cursor.getColumnIndex(KEY_VALVE_DATA));
                String blobAsString = new String(blob);
                listAddEditValveData = gson.fromJson(blobAsString, new TypeToken<ArrayList<DataTransferModel>>() {
                }.getType());

                modalBLEValve.setListValveData(listAddEditValveData);
                // Adding BLE's to list
                listMdlBLEDvcs.add(modalBLEValve);
            } while (cursor.moveToNext());
        }

        // return contact list
        return listMdlBLEDvcs;
    }


    // Getting All BLE Dvcs
    public List<ModalBLEDevice> getAllAddressNdDeviceMapping() {
        List<ModalBLEDevice> listMdlBLEDvcs = new ArrayList<ModalBLEDevice>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_BLE_DEVICE;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                ModalBLEDevice modalBLEDevice = new ModalBLEDevice();

                modalBLEDevice.setID(Integer.parseInt(cursor.getString(0)));
                modalBLEDevice.setName(cursor.getString(1));
                modalBLEDevice.setDvcMacAddrs(cursor.getString(2));
                byte[] blob = cursor.getBlob(3);
                String blobAsString = new String(blob);
                MdlAddressNdLocation mdlLocationAddress = gson.fromJson(blobAsString, new TypeToken<MdlAddressNdLocation>() {
                }.getType());
                modalBLEDevice.setMdlLocationAddress(mdlLocationAddress);
                modalBLEDevice.setValvesNum(Integer.parseInt(cursor.getString(4)));
                // Adding BLE's to list
                listMdlBLEDvcs.add(modalBLEDevice);
            } while (cursor.moveToNext());
        }

        // return contact list
        return listMdlBLEDvcs;
    }

    public ArrayList<MdlValveNameStateNdSelect> getValveNameAndLastTwoProp(String dvcMacAdd) {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<MdlValveNameStateNdSelect> listWithOnlyValves = new ArrayList<>();

        Cursor cursor = db.query(TABLE_BLE_VALVE, new String[]{KEY_VALVE_NAME, KEY_SELECTED, KEY_VALVE_STATE}, KEY_DVC_MAC + "=?",
                new String[]{dvcMacAdd}, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                listWithOnlyValves.add(new MdlValveNameStateNdSelect(cursor.getString(cursor.getColumnIndex(KEY_VALVE_NAME)), cursor.getString(cursor.getColumnIndex(KEY_SELECTED)), cursor.getString(cursor.getColumnIndex(KEY_VALVE_STATE))));
            } while (cursor.moveToNext());
        }

        return listWithOnlyValves;
    }

  /*  // Getting single BLE device
    ModalBLEDevice getBLEDvcWithId(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_BLE_DEVICE, new String[]{KEY_ID,
                        KEY_DVC_NAME, KEY_DVC_MAC}, KEY_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

       *//* byte[] blob = cursor.getBlob(3);
        String blobAsString = new String(blob);
        listValvesFromDB = gson.fromJson(blobAsString, new TypeToken<List<String>>() {
        }.getType());*//*

        ModalBLEDevice modalBleDevice = new ModalBLEDevice(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2), cursor.getString(3), Integer.parseInt(cursor.getString(4)));
        // return contact
        return modalBleDevice;
    }*/


    /*// Getting single BLE device
    ModalBLEDevice getBLEValveDataWithMAC(String mac) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_BLE_DEVICE, new String[]{KEY_ID,
                        KEY_DVC_NAME, KEY_DVC_MAC}, KEY_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

       *//* byte[] blob = cursor.getBlob(3);
        String blobAsString = new String(blob);
        listValvesFromDB = gson.fromJson(blobAsString, new TypeToken<List<String>>() {
        }.getType());*//*

        ModalBLEDevice modalBleDevice = new ModalBLEDevice(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2), Integer.parseInt(cursor.getString(3)));
        // return contact
        return modalBleDevice;
    }*/

    // Updating single Valve Data
    public int updateValveDataAndState(String macAdd, String clkdVlvName, List<DataTransferModel> byteDataList, String valveState) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        //Converting collection into String(byteArray)
        byteArrayListValves = gson.toJson(byteDataList).getBytes();
        values.put(KEY_VALVE_DATA, byteArrayListValves);
        //values.put(KEY_PLAY_PAUSE_CMD, playPause);
        values.put(KEY_VALVE_STATE, valveState);
        int rowAffected = db.update(TABLE_BLE_VALVE, values, KEY_DVC_MAC + " = ? AND " + KEY_VALVE_NAME + " = ? ",
                new String[]{macAdd, clkdVlvName});
        Log.e("@@@ROW AFFECTED ", rowAffected + "");
        return rowAffected;
    }

    // Updating valve selection
    public int updateValveSelect(String macAdd, String valveName, String valveSelected) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_SELECTED, valveSelected);
        int rowAffected = db.update(TABLE_BLE_VALVE, values, KEY_DVC_MAC + " = ? AND " + KEY_VALVE_NAME + " = ? ",
                new String[]{macAdd, valveName});
        return rowAffected;
    }

    // Updating valve state
    public int updateValveStates(String macAdd, String valveName, String valveState) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_VALVE_STATE, valveState);
        int rowAffected = db.update(TABLE_BLE_VALVE, values, KEY_DVC_MAC + " = ? AND " + KEY_VALVE_NAME + " = ? ",
                new String[]{macAdd, valveName});
        return rowAffected;
    }

    // Updating Valve State
    public int updateValveState(String macAdd, String clkdVlvName, String valveState) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        //values.put(KEY_PLAY_PAUSE_CMD, playPause);
        values.put(KEY_VALVE_STATE, valveState);
        int rowAffected = db.update(TABLE_BLE_VALVE, values, KEY_DVC_MAC + " = ? AND " + KEY_VALVE_NAME + " = ? ",
                new String[]{macAdd, clkdVlvName});
        return rowAffected;
    }

    // Updating Play Pause
    public int updateFlushStatus(String device_address, String clickedValveName, String flushStatus) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_FLUSH_CMD, flushStatus);

        int rowAffected = db.update(TABLE_BLE_VALVE, values, KEY_DVC_MAC + " = ? AND " + KEY_VALVE_NAME + " = ? ",
                new String[]{device_address, clickedValveName});
        Log.e("@@@ROW AFFECTED ", rowAffected + "");
        // updating row
        return rowAffected;

    }

    // Deleting single BLE DVC
    public void deleteBLEDvcWithId(ModalBLEDevice modalBleDevice) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_BLE_DEVICE, KEY_ID + " = ?",
                new String[]{String.valueOf(modalBleDevice.getID())});
        db.close();
    }

    //Deleting all records from BLE DVC table
    public void deleteAllFromTableBLEDvc() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + TABLE_BLE_DEVICE);
        db.close();
    }

    // Getting BLE DVC Count
    public int getBLEDvcCount() {
        String countQuery = "SELECT  * FROM " + TABLE_BLE_DEVICE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int countReady = cursor.getCount();
        cursor.close();

        // return count
        return countReady;
    }

    public ModalValveBirth getValveDataAndProperties(String dvcMacAdd, String clickedValveName) {
        SQLiteDatabase db = this.getReadableDatabase();
        ModalValveBirth modalBLEValve = null;
        ArrayList<DataTransferModel> listDataTransferMdl;

        Cursor cursor = db.query(TABLE_BLE_VALVE, new String[]{KEY_VALVE_DATA, KEY_VALVE_STATE, KEY_FLUSH_CMD}, KEY_DVC_MAC + " = ? AND " + KEY_VALVE_NAME + " = ? ",
                new String[]{dvcMacAdd, clickedValveName}, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            byte[] blob = cursor.getBlob(cursor.getColumnIndex(KEY_VALVE_DATA));
            String blobAsString = new String(blob);
            listDataTransferMdl = gson.fromJson(blobAsString, new TypeToken<List<DataTransferModel>>() {
            }.getType());

            String valveState = cursor.getString(cursor.getColumnIndex(KEY_VALVE_STATE));
            String flushStatus = cursor.getString(cursor.getColumnIndex(KEY_FLUSH_CMD));

            modalBLEValve = new ModalValveBirth(listDataTransferMdl, valveState, flushStatus);
        }
        return modalBLEValve;
    }

    /*public int deleteSesnPlnWithMacValveName(String dvcMacAdd, String clickedValveName) {
        SQLiteDatabase db = this.getWritableDatabase();
        int intDelete=db.delete(TABLE_BLE_VALVE, KEY_DVC_MAC + " = ? AND " + KEY_VALVE_NAME + " = ? ",
                new String[]{dvcMacAdd, clickedValveName});
        db.close();
        return intDelete;
    }*/
}

/*public class DatabaseHandler extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "dbGreenController";
    // Table for Session plan info
    private static final String TABLE_SESN_PLAN_TP = "tblSesnPlan";
    private static final String COLUMN_LIST_SESN_PLAN_TP = "listSesnPlanTP";

    List<DataTransferModel> listDataTransferModels;
    Gson gson;
    byte[] byteArrayOfListDTM;
    SQLiteDatabase db;

    public DatabaseHandler(Context mContext) {
        super(mContext, DATABASE_NAME, null, DATABASE_VERSION);
        //this.listDataTransferModels = listDataTransferModels;
        gson = new Gson();
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE_SESN_PLAN_TP = "CREATE TABLE " + TABLE_SESN_PLAN_TP + "("
                + COLUMN_LIST_SESN_PLAN_TP + " TEXT" + ")";
        db.execSQL(CREATE_TABLE_SESN_PLAN_TP);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SESN_PLAN_TP);
        // Create tables again
        onCreate(db);
    }

    public void openDatabase() {
        db = this.getWritableDatabase();
    }

    public void closeDatabase() {
        if (db.isOpen()) {
            db.close();
        }
    }

    public void insertDataIntoDB(List<DataTransferModel> listDataTransferModels) {
        byteArrayOfListDTM = gson.toJson(listDataTransferModels).getBytes();
        ContentValues values = new ContentValues();
        values.put(COLUMN_LIST_SESN_PLAN_TP, byteArrayOfListDTM);
        // Inserting Row
        db.insert(TABLE_SESN_PLAN_TP, null, values);
    }

    public List<DataTransferModel> getListDataTM() {
        String selectQuery = "SELECT  * FROM " + TABLE_SESN_PLAN_TP;
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            byte[] blob = cursor.getBlob(cursor.getColumnIndex(COLUMN_LIST_SESN_PLAN_TP));
            String blobAsString = new String(blob);
            listDataTransferModels = gson.fromJson(blobAsString, new TypeToken<List<DataTransferModel>>() {
            }.getType());

            cursor.close();
            return listDataTransferModels;
        }
        return null;
    }

    public void deleteAllRecordFromTable() {
        db.execSQL("delete from " + TABLE_SESN_PLAN_TP);
    }

}*/
