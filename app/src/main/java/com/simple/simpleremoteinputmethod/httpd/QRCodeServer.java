package com.simple.simpleremoteinputmethod.httpd;

import android.content.Context;
import android.graphics.Bitmap;

import com.simple.simpleremoteinputmethod.qrcode.QRCodeGenerator;
import com.simple.simpleremoteinputmethod.utils.Utils;

import java.io.IOException;

/**
 * Created by mrsimple on 30/1/2018.
 */

public class QRCodeServer extends RouterServer {
    private volatile Bitmap mQrCodeBitmap;

    public QRCodeServer(Context context) {
        super(context);
    }

    @Override
    public void start(int timeout) throws IOException {
        super.start(timeout);
        new Thread("qrcode-thread") {
            @Override
            public void run() {
                int size = Utils.dip2px(mAppContext, 150) ;
                mQrCodeBitmap = QRCodeGenerator.generate(getLocalAddress(),  size , size) ;
            }
        }.start();
    }

    public Bitmap getQrCodeBitmap() {
        return mQrCodeBitmap;
    }
}
