package com.simple.simpleremoteinputmethod.services;

import android.content.Intent;
import android.inputmethodservice.InputMethodService;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.simple.simpleremoteinputmethod.R;
import com.simple.simpleremoteinputmethod.httpd.QRCodeServer;

import java.io.IOException;

import de.greenrobot.event.EventBus;

/**
 * Created by mrsimple on 30/1/2018.
 */

public class ServerInputMethodService extends InputMethodService {

    private static QRCodeServer sLocalServer;

    @Override
    public void onCreate() {
        super.onCreate();
        sLocalServer = new QRCodeServer(getApplicationContext()) ;
        try {
            sLocalServer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        EventBus.getDefault().register(this);
    }


    @Override
    public View onCreateInputView() {
        View inputView = getLayoutInflater().inflate(R.layout.activity_input, null) ;
        setupQRCode(inputView);
        return inputView;
    }

    private void setupQRCode(final View inputView) {
        TextView addrTv = inputView.findViewById(R.id.input_tv) ;
        if ( sLocalServer != null && sLocalServer.getQrCodeBitmap() != null ) {
            addrTv.setText("连接的地址为: " + sLocalServer.getLocalAddress());

            ImageView imageView = inputView.findViewById(R.id.qrcode_imageview);
            imageView.setImageBitmap(sLocalServer.getQrCodeBitmap());
        } else {
            addrTv.setText("服务器启动未完成, 请等待...");
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    setupQRCode(inputView);
                }
            }, 500);
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    public void onEventMainThread(RemoteKeyEvent event) {
        Log.e("", "@@@ " + event) ;
        if ( event.keyCode == RemoteKeyEvent.HOME_CODE ) {
            navigateToHome();
            return;
        }

        // clear all
        if ( event.keyCode == RemoteKeyEvent.CLEAR_CODE ) {
            if (getCurrentInputConnection() != null) {
                clearText();
                return;
            }
        }

        // 发送事件
        sendDownUpKeyEvents(event.keyCode);
    }


    private void clearText() {
        if (getCurrentInputConnection() != null) {
            getCurrentInputConnection().performContextMenuAction(android.R.id.selectAll);
            getCurrentInputConnection().commitText("", 1);
        }
    }

    public void onEventMainThread(RemoteInputEvent event) {
        Log.e("", "@@@ " + event) ;
        clearText();
        getCurrentInputConnection().commitText(event.text, 1) ;
    }


    private void navigateToHome() {
        Intent intent = new Intent("android.intent.action.MAIN");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addCategory("android.intent.category.HOME");
        startActivity(intent);
    }

    public static QRCodeServer getLocalServer() {
        return sLocalServer;
    }
}
