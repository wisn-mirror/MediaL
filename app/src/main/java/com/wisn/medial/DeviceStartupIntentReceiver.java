package com.wisn.medial;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Wisn on 2018/6/26 下午6:28.
 */
public class DeviceStartupIntentReceiver extends BroadcastReceiver {
    private static final String TAG = "AliveBroadcastReceiver";

    @Override
    public void onReceive(final Context context, Intent intent) {

        Intent intentAlive = new Intent(context, MainActivity.class);
        intentAlive.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intentAlive);
    }

    private void getNetworkBroadcast(Context context, Intent intent) {
       /* String action = intent.getAction();
        // wifi状态改变
        if (WifiManager.WIFI_STATE_CHANGED_ACTION.equals(action)) {
            int wifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, 0);
            switch (wifiState) {
                case WifiManager.WIFI_STATE_DISABLED:
                    Toast.makeText(context, "wifi关闭", Toast.LENGTH_SHORT).show();
                    break;
                case WifiManager.WIFI_STATE_ENABLED:
                    boolean connectServer = NetUtils.isConnectServer(Config.BASE_URL);
//                    Toast.makeText(context, "wifi开启", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
        // 连接到一个有效wifi路由器
        if (WifiManager.NETWORK_STATE_CHANGED_ACTION.equals(action)) {
            Parcelable parcelableExtra = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
            if (null != parcelableExtra) {
                NetworkInfo networkInfo = (NetworkInfo) parcelableExtra;
                NetworkInfo.State state = networkInfo.getState();
                boolean isConnected = state == NetworkInfo.State.CONNECTED;
                if (isConnected) {
//                    Toast.makeText(context, "设备连接到一个有效WIFI路由器", Toast.LENGTH_SHORT).show();
                }
            }
        }
        // 监听网络连接状态，包括wifi和移动网络数据的打开和关闭
        // 由于上面已经对wifi进行处理，这里只对移动网络进行监听(该方式检测有点慢)
        // 其中，移动网络--->ConnectivityManager.TYPE_MOBILE；
        //       Wifi--->ConnectivityManager.TYPE_WIFI
        //       不明确类型：ConnectivityManager.EXTRA_NETWORK_INFO
        if (ConnectivityManager.CONNECTIVITY_ACTION.equals(action)) {
            ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo gprs = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (gprs.isConnected()) {
                Toast.makeText(context, "移动网络打开", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "移动网络关闭", Toast.LENGTH_SHORT).show();
            }
        }*/


    }

}
