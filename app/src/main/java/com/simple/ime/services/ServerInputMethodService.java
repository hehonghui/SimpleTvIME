package com.simple.ime.services;

import android.content.Intent;
import android.graphics.Color;
import android.inputmethodservice.InputMethodService;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.simple.ime.R;
import com.simple.ime.httpd.QRCodeServer;

import java.io.IOException;

import de.greenrobot.event.EventBus;

/**
 * Created by mrsimple on 30/1/2018.
 */

public class ServerInputMethodService extends InputMethodService {
    private QRCodeServer sLocalServer ;

    @Override
    public void onCreate() {
        super.onCreate();
        sLocalServer =  QRCodeServer.getInstance(getApplicationContext());
        try {
            if ( !sLocalServer.isStarted() ) {
                sLocalServer.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        EventBus.getDefault().register(this);
    }


    @Override
    public View onCreateInputView() {
        View inputView = getLayoutInflater().inflate(R.layout.activity_input, null) ;
        inputView.setBackgroundColor(Color.GRAY);
        setupQRCode(inputView);
        return inputView;
    }

    private void setupQRCode(final View inputView) {
        TextView addrTv = inputView.findViewById(R.id.addr_tv) ;
        if ( sLocalServer != null && sLocalServer.getQrCodeBitmap() != null ) {
            addrTv.setText(getText(R.string.connect_addr) + sLocalServer.getLocalAddress());

            ImageView imageView = inputView.findViewById(R.id.qrcode_imageview);
            imageView.setImageBitmap(sLocalServer.getQrCodeBitmap());
        } else {
            addrTv.setText(getText(R.string.waiting));
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    setupQRCode(inputView);
                }
            }, 50);
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


    public void onEventMainThread(RemoteInputEvent event) {
        Log.e("", "@@@ " + event) ;
        clearText();
        getCurrentInputConnection().commitText(event.text, 1) ;
    }


    private void clearText() {
        if (getCurrentInputConnection() != null) {
            getCurrentInputConnection().performContextMenuAction(android.R.id.selectAll);
            getCurrentInputConnection().commitText("", 1);
        }
    }


    private void navigateToHome() {
        Intent intent = new Intent("android.intent.action.MAIN");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addCategory("android.intent.category.HOME");
        startActivity(intent);
    }
}
