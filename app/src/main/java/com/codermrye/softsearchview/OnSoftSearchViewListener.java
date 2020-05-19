package com.codermrye.softsearchview;

import android.text.Editable;

public interface OnSoftSearchViewListener {

    void beforeTextChanged(CharSequence s, int start, int count, int after);
    void onTextChanged(CharSequence s, int start, int before, int count);
    void afterTextChanged(Editable s);
}
