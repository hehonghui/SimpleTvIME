/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.simple.simpleremoteinputmethod;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.simple.simpleremoteinputmethod.httpd.LocalServer;
import com.simple.simpleremoteinputmethod.qrcode.QRCodeGenerator;
import com.simple.simpleremoteinputmethod.utils.Utils;

import java.io.IOException;

/*
 * MainActivity class that loads {@link MainFragment}.
 */
public class MainActivity extends Activity {

    LocalServer mLocalServer ;
    String mServerAddr ;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.enable_input_method).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent("android.settings.INPUT_METHOD_SETTINGS"), 0);
            }
        });

        findViewById(R.id.set_default_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE)).showInputMethodPicker();
            }
        });

        TextView textView = findViewById(R.id.im_status_tv) ;
        textView.append("是否激活: " + Utils.myInputMethodIsActive(this) + ";  默认输入法: " + Utils.myInputMethodIsDefault(this));

        initHttpServer();
    }

    private void initHttpServer() {
        mLocalServer = new LocalServer(getApplicationContext()) ;
        try {
            mLocalServer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //        mServerAddr = "http://www.newsdog.today";
        mServerAddr = "http://" + Utils.getIpAddress(getApplicationContext()) + ":" + mLocalServer.getPort() ;
        TextView addrTv = findViewById(R.id.addr_tv) ;
        addrTv.append("连接的地址为: " + mServerAddr);

        ImageView imageView = findViewById(R.id.qrcode_imageview) ;
        imageView.setImageBitmap(QRCodeGenerator.generate(mServerAddr, Utils.dip2px(this, 150), Utils.dip2px(this, 150)));
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if ( mLocalServer != null ) {
            mLocalServer.stop();
        }
    }
}
