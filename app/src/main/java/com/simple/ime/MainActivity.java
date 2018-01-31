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

package com.simple.ime;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.simple.ime.httpd.QRCodeServer;
import com.simple.ime.services.ServerInputMethodService;
import com.simple.ime.utils.Utils;

/**
 *
 */
public class MainActivity extends Activity {

    public static final int REQ_CODE = 123456 ;

    Handler mUiHandler = new Handler(Looper.getMainLooper()) ;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.enable_input_method).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent("android.settings.INPUT_METHOD_SETTINGS"), REQ_CODE);
            }
        });

        findViewById(R.id.set_default_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInputMethodPicker();
            }
        });

        TextView textView = findViewById(R.id.im_status_tv) ;
        textView.setText( getText(R.string.is_active).toString() + Utils.myInputMethodIsActive(this)
                            + getText(R.string.is_default).toString() + Utils.myInputMethodIsDefault(this));
    }

    Runnable mCheckRunnable = new Runnable() {
        @Override
        public void run() {
            Intent intent = new Intent(getApplicationContext(), ServerInputMethodService.class) ;
            startService(intent) ;
        }
    } ;

    @Override
    protected void onResume() {
        super.onResume();
        if ( Utils.myInputMethodIsDefault(getApplicationContext())
                && !QRCodeServer.getInstance(getApplicationContext()).isStarted() ) {
            mUiHandler.postDelayed(mCheckRunnable, 1000) ;
        } else {
            showTips();
        }
    }

    private void showTips() {
        if ( !Utils.myInputMethodIsActive(getApplicationContext()) ) {
            Toast.makeText(this, R.string.enable_input , Toast.LENGTH_LONG).show();
        } else if ( !Utils.myInputMethodIsDefault(getApplicationContext()) ) {
            Toast.makeText(this, R.string.setup_default , Toast.LENGTH_LONG).show();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ( requestCode == REQ_CODE ) {
            showTips();
        }
    }

    private void showInputMethodPicker() {
        ((InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE)).showInputMethodPicker();
    }
}
