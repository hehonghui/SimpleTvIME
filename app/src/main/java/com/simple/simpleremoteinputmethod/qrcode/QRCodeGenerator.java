package com.simple.simpleremoteinputmethod.qrcode;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mrsimple on 30/1/2018.
 */

public class QRCodeGenerator {

    public static Bitmap generate(String str, int width, int height) {
        QRCodeWriter qRCodeWriter = new QRCodeWriter();
        Map hashMap = new HashMap();
        hashMap.put(EncodeHintType.CHARACTER_SET, "utf-8");
        try {
            BitMatrix encode = qRCodeWriter.encode(str, BarcodeFormat.QR_CODE, width, height, hashMap);
            int[] pixels = new int[(width * height)];
            for (int h = 0; h < height; h++) {
                for (int w = 0; w < width; w++) {
                    if (encode.get(w, h)) {
                        pixels[(h * width) + w] = 0;
                    } else {
                        pixels[(h * width) + w] = -1;
                    }
                }
            }
            return Bitmap.createBitmap(pixels, 0, width, width, height, Config.RGB_565);
        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        }
    }
}
