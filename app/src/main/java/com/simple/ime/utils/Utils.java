package com.simple.ime.utils;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.util.Log;
import android.view.inputmethod.InputMethodInfo;
import android.view.inputmethod.InputMethodManager;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

/**
 * Created by mrsimple on 30/1/2018.
 */

public class Utils {

    public static String getIpAddress(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE) ;
        WifiInfo info = wifiManager.getConnectionInfo() ;
        if (info != null && info.getIpAddress() == 0) {
            try {
                Enumeration networkInterfaces = NetworkInterface.getNetworkInterfaces();
                while (networkInterfaces.hasMoreElements()) {
                    NetworkInterface networkInterface = (NetworkInterface) networkInterfaces.nextElement();
                    String displayName = networkInterface.getDisplayName();
                    if (displayName.equals("eth0") || displayName.equals("wlan0")) {
                        Enumeration inetAddresses = networkInterface.getInetAddresses();
                        while (inetAddresses.hasMoreElements()) {
                            InetAddress inetAddress = (InetAddress) inetAddresses.nextElement();
                            if (!inetAddress.isLoopbackAddress() && (inetAddress instanceof Inet4Address)) {
                                return inetAddress.getHostAddress();
                            }
                        }
                        continue;
                    }
                }
            } catch (Throwable e) {
                Log.e("", "获取本地IP出错", e);
            }
            return "0.0.0.0";
        }

        return String.format("%d.%d.%d.%d", new Object[]{
                Integer.valueOf(info.getIpAddress() & 255),
                Integer.valueOf((info.getIpAddress() >> 8) & 255),
                Integer.valueOf((info.getIpAddress() >> 16) & 255),
                Integer.valueOf((info.getIpAddress() >> 24) & 255)});
    }


    /**
     * dip转为 px
     */
    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }


    public static boolean myInputMethodIsActive(Context context) {
        for (InputMethodInfo packageName : ((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE)).getEnabledInputMethodList()) {
            if (packageName.getPackageName().equals(context.getPackageName())) {
                return true;
            }
        }
        return false;
    }


    public static boolean myInputMethodIsDefault(Context context) {
        String string = Settings.Secure.getString(context.getContentResolver(), "default_input_method");
        if (string == null || !string.startsWith(context.getPackageName())) {
            return false;
        }
        return true;
    }
}
