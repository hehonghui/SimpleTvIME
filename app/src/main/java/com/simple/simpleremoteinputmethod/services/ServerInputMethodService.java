package com.simple.simpleremoteinputmethod.services;

import android.inputmethodservice.InputMethodService;
import android.view.View;

import com.simple.simpleremoteinputmethod.R;

/**
 * Created by mrsimple on 30/1/2018.
 */

public class ServerInputMethodService extends InputMethodService {
    @Override
    public void onInitializeInterface() {
        super.onInitializeInterface();
    }

    @Override
    public View onCreateCandidatesView() {
        return null;
    }

    @Override
    public View onCreateInputView() {
        return getLayoutInflater().inflate(R.layout.activity_input, null);
    }
}
