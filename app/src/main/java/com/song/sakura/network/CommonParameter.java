package com.song.sakura.network;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.CellLocation;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;

import com.song.sakura.app.App;
import com.ui.util.MIUIUtils;
import com.ut.device.UTDevice;

import java.util.UUID;

public class CommonParameter {
    public String os = "";
    public String osVersion = "";
    public String deviceId = "";
    public String ver = "";
    public String userAgent = "";
    public String apn = "";
    //    public String userId = "";
    public String mac = "";
    //    public String imsi = "";
//    public String imei = "";
    public String routerMac = "";
    public String station = "";
    public String authToken = "";
    public String ts = "";

    public CommonParameter() {
        init(App.Companion.getApplication());
    }

    public CommonParameter(Context context) {
        init(context);
    }

    public String getDeviceId() {
        return deviceId = UTDevice.getUtdid(App.Companion.getApplication());
    }

    private void init(Context context) {
        os = "androId";

        osVersion = android.os.Build.VERSION.RELEASE;
        userAgent = (MIUIUtils.isMIUI() ? "xiaomi_" : "") + android.os.Build.MODEL;
        getVersion(context);
        setNetwork(context);
        setLocalMacAddress(context);
    }

    public String getVersion(Context context) {
        try {
            this.ver = (context).getPackageManager().getPackageInfo(
                    (context).getPackageName(), 0).versionName;
        } catch (Exception e) {
        }
        return this.ver;
    }


    private void setNetwork(Context context) {
        // 检测网络
        ConnectivityManager connManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo.State state_wifi = null;
        NetworkInfo.State state_gprs = null;
        try {
            state_wifi = connManager.getNetworkInfo(
                    ConnectivityManager.TYPE_WIFI).getState(); // 获取网络连接状态
        } catch (Exception e) {
        }
        try {
            state_gprs = connManager.getNetworkInfo(
                    ConnectivityManager.TYPE_MOBILE).getState(); // 获取网络连接状态
        } catch (Exception e) {
        }
        if (null != state_wifi && NetworkInfo.State.CONNECTED == state_wifi) { // 判断是否正在使用WIFI网络
            this.apn = "wifi";
        } else if (null != state_gprs && NetworkInfo.State.CONNECTED == state_gprs) { // 判断是否正在使用GPRS网络
            this.apn = "wan";
        } else {
            this.apn = "";
        }
    }

    @SuppressLint({"MissingPermission", "HardwareIds"})
    public void setDeviceId(Context context) {
        TelephonyManager tm = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        String tmDevice, tmSerial, androidId;
        try {
            tmDevice = tm.getDeviceId();
            tmSerial = tm.getSimSerialNumber();
            androidId = android.provider.Settings.Secure.getString(
                    context.getContentResolver(),
                    android.provider.Settings.Secure.ANDROID_ID);
            UUID deviceUuid = new UUID(androidId.hashCode(),
                    ((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
            //MD5.toMD5(deviceUuid.toString());
//            this.imei = tm.getDeviceId();
//            this.imsi = tm.getSubscriberId();
        } catch (Exception e) {
        }
        try {
            CellLocation cellLocation = tm.getCellLocation();
            int type = tm.getPhoneType();
            if (type == TelephonyManager.PHONE_TYPE_CDMA) {
                CdmaCellLocation location = (CdmaCellLocation) cellLocation;
                int lac = location.getNetworkId();
                int cellid = location.getBaseStationId();
                this.station = "" + lac + "," + cellid;
            } else if (type == TelephonyManager.PHONE_TYPE_GSM) {
                GsmCellLocation location = (GsmCellLocation) cellLocation;
                int lac = location.getLac();
                int cellid = location.getCid();
                this.station = "" + lac + "," + cellid;
            }
        } catch (Exception e) {
        }
    }


    private void setLocalMacAddress(Context context) {
        try {
            WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            WifiInfo info = wifi.getConnectionInfo();
            if (wifi.isWifiEnabled()) {
                this.routerMac = info.getBSSID(); //获取被连接网络的mac地址
                this.mac = info.getMacAddress();// 获得本机的MAC地址
            }
        } catch (Exception e) {
        }
    }

}
