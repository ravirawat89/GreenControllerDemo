package com.netcommlabs.greencontroller.sqlite_db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.netcommlabs.greencontroller.model.DataTransferModel;
import com.netcommlabs.greencontroller.model.MdlLocationAddress;
import com.netcommlabs.greencontroller.model.ModalBLEDevice;
import com.netcommlabs.greencontroller.model.ModalBLEValve;

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
    //private static final String KEY_MAC_ADD = "mac_address";
    private static final String KEY_VALVE_NAME = "valve_name";
    private static final String KEY_VALVE_DATA = "valve_data";

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
                + KEY_ID_VALVE + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_DVC_MAC + " TEXT," + KEY_VALVE_NAME + " TEXT," + KEY_VALVE_DATA + " TEXT" + ")";


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
        values.put(KEY_DVC_NAME, modalBleDevice.getName());
        values.put(KEY_DVC_MAC, modalBleDevice.getDvcMacAddrs());
        //Converting Address modal to String/byte array
        byte[] byteArrayAddress = gson.toJson(modalBleDevice.getMdlLocationAddress()).getBytes();
        values.put(KEY_DVC_LOC_ADDRESS, byteArrayAddress);
        values.put(KEY_DVC_NUMBER_VALVES, modalBleDevice.getValvesNum());

        // Inserting Row
        db.insert(TABLE_BLE_DEVICE, null, values);
        db.close(); // Closing database connection
    }


    // Adding new BLE Valve
    public void addValveNdData(ModalBLEValve modalBLEValve) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_DVC_MAC, modalBLEValve.getDvcMacAddrs());
        values.put(KEY_VALVE_NAME, modalBLEValve.getValveName());
        //Converting collection into String(byteArray)
        byteArrayListValves = gson.toJson(modalBLEValve.getListValveData()).getBytes();
        values.put(KEY_VALVE_DATA, byteArrayListValves);

        // Inserting Row
        db.insert(TABLE_BLE_VALVE, null, values);
        db.close(); // Closing database connection
    }

    // Getting All Valves and Data
    public List<ModalBLEValve> getAllValvesNdData() {
        List<ModalBLEValve> listMdlBLEDvcs = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_BLE_VALVE;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                ModalBLEValve modalBLEValve = new ModalBLEValve();
                modalBLEValve.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_ID_VALVE))));
                modalBLEValve.setDvcMacAddrs(cursor.getString(cursor.getColumnIndex(KEY_DVC_MAC)));
                modalBLEValve.setValveName(cursor.getString(cursor.getColumnIndex(KEY_VALVE_NAME)));

                byte[] blob = cursor.getBlob(cursor.getColumnIndex(KEY_VALVE_DATA));
                String blobAsString = new String(blob);
                listMdlValveData = gson.fromJson(blobAsString, new TypeToken<List<DataTransferModel>>() {
                }.getType());
                modalBLEValve.setListValveData(listMdlValveData);
                // Adding BLE's to list
                listMdlBLEDvcs.add(modalBLEValve);
            } while (cursor.moveToNext());
        }

        // return contact list
        return listMdlBLEDvcs;
    }


    // Getting All BLE Dvcs
    public List<ModalBLEDevice> getAllBLEDvcs() {
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
                MdlLocationAddress mdlLocationAddress = gson.fromJson(blobAsString, new TypeToken<MdlLocationAddress>() {
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

    public List<String> getAllValvesNameWithMAC(String dvcMacAdd) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<String> listWithOnlyValves = new ArrayList<>();

        Cursor cursor = db.query(TABLE_BLE_VALVE, new String[]{KEY_VALVE_NAME}, KEY_DVC_MAC + "=?",
                new String[]{dvcMacAdd}, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                listWithOnlyValves.add(cursor.getString(cursor.getColumnIndex(KEY_VALVE_NAME)));
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

    // Updating single BLE Dvc
    public int updateValveDataWithMAC_ValveName(String device_address, String clickedValveName, List<DataTransferModel> byteDataList) {
        SQLiteDatabase db = this.getWritableDatabase();

        //byteArrayListValves = gson.toJson(modalBleDevice.getListValves()).getBytes();

        ContentValues values = new ContentValues();
       /* values.put(KEY_DVC_NAME, modalBleDevice.getName());
        values.put(KEY_DVC_MAC, modalBleDevice.getDvcMacAddrs());*/
        //values.put(KEY_DVC_NUMBER_VALVES, modalBleDevice.getValvesNum());

        //Converting collection into String(byteArray)
        byteArrayListValves = gson.toJson(byteDataList).getBytes();
        values.put(KEY_VALVE_DATA, byteArrayListValves);

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

    public List<DataTransferModel> getValveDataWithMACValveName(String dvcMacAdd, String clickedValveName) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<DataTransferModel> listValveDataSingle = new ArrayList<>();

        Cursor cursor = db.query(TABLE_BLE_VALVE, new String[]{KEY_VALVE_DATA}, KEY_DVC_MAC + " = ? AND " + KEY_VALVE_NAME + " = ? ",
                new String[]{dvcMacAdd, clickedValveName}, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            byte[] blob = cursor.getBlob(cursor.getColumnIndex(KEY_VALVE_DATA));
            String blobAsString = new String(blob);
            listValveDataSingle = gson.fromJson(blobAsString, new TypeToken<List<DataTransferModel>>() {
            }.getType());
        }
        return listValveDataSingle;
    }

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
