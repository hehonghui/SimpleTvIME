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
import android.provider.Settings;
import android.view.View;
import android.view.inputmethod.InputMethodInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

/*
 * MainActivity class that loads {@link MainFragment}.
 */
public class MainActivity extends Activity {

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
        textView.append("是否激活: " + myInputMethodIsActive() + ";  默认输入法: " + myInputMethodIsDefault());
    }


    private boolean myInputMethodIsActive() {
        for (InputMethodInfo packageName : ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).getEnabledInputMethodList()) {
            if (packageName.getPackageName().equals(getPackageName())) {
                return true;
            }
        }
        return false;
    }


    private boolean myInputMethodIsDefault() {
        String string = Settings.Secure.getString(getContentResolver(), "default_input_method");
        if (string == null || !string.startsWith(getPackageName())) {
            return false;
        }
        return true;
    }
}
