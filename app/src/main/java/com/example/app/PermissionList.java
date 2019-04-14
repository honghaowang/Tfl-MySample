package com.example.app;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import java.util.ArrayList;

public class PermissionList {
    Context context;
    Activity activity;
    private ArrayList<String> permissionList = new ArrayList<String>();
    private final int PERMISSION_STATE = 123;
    private final String TAG = "SetPermission";

    public PermissionList(Context context, Activity activity){
        this.context = context;
        this.activity = activity;
    }

    public void setStoragePermission(){
        addPermissionList("android.permission.WRITE_EXTERNAL_STORAGE");
        addPermissionList("android.permission.WRITE_INTERNAL_STORAG");
        addPermissionList("android.permission.READ_EXTERNAL_STORAGE");
        addPermissionList("android.permission.READ_INTERNAL_STORAGE");
    }

    public void setCalendarPermission(){
        addPermissionList("android.permission.READ_CALENDAR");
        addPermissionList("android.permission.WRITE_CALENDAR");
    }

    public void setCameraPermission(){
        addPermissionList("android.permission.CAMERA");
    }

    public void setContextPermission(){
        addPermissionList("android.permission.READ_CONTACTS");
        addPermissionList("android.permission.WRITE_CONTACTS");
        addPermissionList("android.permission.GET_CONTACTS");
    }

    public void setBlueToothPermission(){

        addPermissionList("android.permission.BLUETOOTH_ADMIN");
        addPermissionList("android.permission.BLUETOOTH");
    }
    public void setLocationPermission(){
        addPermissionList("android.permission.ACCESS_FINE_LOCATION");
        addPermissionList("android.permission.ACCESS_COARSE_LOCATION");
    }

    public void setMicroPhonePermission(){
        addPermissionList("android.permission.RECORD_AUDIO");
    }

    public void setPhonePermission(){
        addPermissionList("android.permission.READ_PHONE_STATE");
        addPermissionList("android.permission.CALL_PHONE");
        addPermissionList("android.permission.READ_CALL_LOG");
        addPermissionList("android.permission.WRITE_CALL_LOG");
        addPermissionList("com.android.voicemail.permission.ADD_VOICEMAIL");
        addPermissionList("android.permission.USE_SIP");
        addPermissionList("android.permission.PROCESS_OUTGOING_CALLS");
    }

    public void setPhoneStatePermission(){
        addPermissionList("android.permission.READ_PHONE_STATE");
    }
    public void setSensorPermission(){
        addPermissionList("android.permission.BODY_SENSORS");
    }

    public void setSMSpermission(){
        addPermissionList("android.permission.SEND_SMS");
        addPermissionList("android.permission.RECEIVE_SMS");
        addPermissionList("android.permission.READ_SMS");
        addPermissionList("android.permission.RECEIVE_WAP_PUSH");
        addPermissionList("android.permission.RECEIVE_MMS");
    }


    public void addPermission(String permission){
        if(ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED){
            if(!ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.READ_EXTERNAL_STORAGE)){
                ActivityCompat.requestPermissions(activity, new String[]{permission}, PERMISSION_STATE);
            }
        }
    }

    public void addPermissionList(ArrayList<String> List){
        for(int i=0; i<List.size(); i++){
            if(ContextCompat.checkSelfPermission(context, List.get(i)) != PackageManager.PERMISSION_GRANTED) {
                if (!ActivityCompat.shouldShowRequestPermissionRationale(activity, List.get(i))) {
                    Log.e(TAG, "miss READ_PHONE_STATE");
                    permissionList.add("android.permission.READ_PHONE_STATE");
                }
            }
        }

        ActivityCompat.requestPermissions(activity,List.toArray(new String[permissionList.size()]), PERMISSION_STATE);
    }

    public void addPermissionList(String permission){
        if(ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED){
            //if(!ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.READ_EXTERNAL_STORAGE)){
            permissionList.add(permission);
            //}
        }
    }

    public void getPermission(){

        if(!permissionList.isEmpty()) {
            for(int i=0; i<permissionList.size(); i++)
                Log.d(TAG, "REQUEST " + permissionList.get(i));
            ActivityCompat.requestPermissions(activity, permissionList.toArray(new String[permissionList.size()]), PERMISSION_STATE);
        }
    }
}
