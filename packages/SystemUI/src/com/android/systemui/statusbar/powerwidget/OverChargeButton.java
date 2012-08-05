package com.android.systemui.statusbar.powerwidget;

import com.android.systemui.R;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.net.Uri;
import android.provider.Settings;

import java.util.ArrayList;
import java.util.List;

import java.io.File;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class OverChargeButton extends PowerButton {

    public OverChargeButton() { mType = BUTTON_OVERCHARGE; }

    @Override
    protected void updateState(Context context) {
        if (getOverChargeState()) {
            mIcon = R.drawable.stat_wimax_on;
            mState = STATE_ENABLED;
        } else {
            mIcon = R.drawable.stat_wimax_off;
            mState = STATE_DISABLED;
        }
    }

    @Override
    protected void toggleState(Context context) {
        boolean state = getOverChargeState();
        setOverCharge(!state);
    }

    private void setOverCharge(boolean enableFastCharge){
        FileWriter fw = null;
        try{
             fw = new FileWriter(new File("/sys/kernel/fast_charge/force_fast_charge"));
             if(enableFastCharge){
                fw.write('1');
             } else {
                fw.write('0');
             }

         }catch (Exception e){
         }finally{
            try{
                fw.flush();
                fw.close();
            }catch(Exception e){}
         }
    }

    private static boolean getOverChargeState(){
        FileReader fr = null;
        char buf[] = new char[1];
        try{
            fr = new FileReader(new File("/sys/kernel/fast_charge/force_fast_charge"));
            fr.read(buf);
            if(buf[0]=='1')
                return true;
            fr.close();
        }catch(Exception e){return false;}
        return false;
    }

    @Override
    protected boolean handleLongClick(Context context) {
        return false;
    }

}
