package com.codermrye.softsearchview;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.util.ArrayList;

public class SoftSearchView extends LinearLayout{

    private View width;
    private View round;
    private View list;
    private View query;
    private float scale;
    private View circle;
    // 将监听事件封装
    private EditText mEditor;

    public SoftSearchView(Context context) {
        super(context);
        initView(context);
    }

    public SoftSearchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private ArrayList<OnSoftSearchViewListener> mListeners;
    public void addTextChangedListener(OnSoftSearchViewListener listener) {
        if (mListeners == null) {
            mListeners = new ArrayList<OnSoftSearchViewListener>();
        }
        mListeners.add(listener);
    }
    public void removeTextChangedListener(OnSoftSearchViewListener listener) {
        if (mListeners != null) {
            int i = mListeners.indexOf(listener);

            if (i >= 0) {
                mListeners.remove(i);
            }
        }
    }

    private void initView(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.view_choice_up_search, this, true);
        width = findViewById(R.id.voice);
        round = findViewById(R.id.round);
        list = findViewById(R.id.list_query);
        query = findViewById(R.id.ll_query);
        circle = findViewById(R.id.circle);

        mEditor = findViewById(R.id.edit_query);
        mEditor.addTextChangedListener(textWatcher);
    }

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            SoftSearchView.this.sendBeforeTextChanged(s, start, count, after);
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            SoftSearchView.this.sendOnTextChanged(s, start, before, count);
        }

        @Override
        public void afterTextChanged(Editable s) {
            SoftSearchView.this.sendAfterTextChanged(s);
        }
    };

    private void sendBeforeTextChanged(CharSequence text, int start, int before, int after) {
        if (mListeners != null) {
            final ArrayList<OnSoftSearchViewListener> list = mListeners;
            final int count = list.size();
            for (int i = 0; i < count; i++) {
                list.get(i).beforeTextChanged(text, start, before, after);
            }
        }
    }
    void sendOnTextChanged(CharSequence text, int start, int before, int after) {
        if (mListeners != null) {
            final ArrayList<OnSoftSearchViewListener> list = mListeners;
            final int count = list.size();
            for (int i = 0; i < count; i++) {
                list.get(i).onTextChanged(text, start, before, after);
            }
        }
    }
    void sendAfterTextChanged(Editable text) {
        if (mListeners != null) {
            final ArrayList<OnSoftSearchViewListener> list = mListeners;
            final int count = list.size();
            for (int i = 0; i < count; i++) {
                list.get(i).afterTextChanged(text);
            }
        }
    }

    public void updateShow(boolean isExpand) {
        double serchWid = round.getWidth() / 1.0;
        double wid = width.getWidth() / 1.0;
        double fenshu = wid / serchWid;
        scale = (float) fenshu;

        if (isExpand) {
            expandSearch();
        } else {
            closeSearch();
        }
    }

    private void expandSearch() {
        circle.setVisibility(View.VISIBLE);
        list.setVisibility(View.GONE);
        query.setVisibility(View.VISIBLE);
        ObjectAnimator anim1 = ObjectAnimator.ofFloat(circle, "alpha", 1f, 0f);
        ObjectAnimator anim2 = ObjectAnimator.ofFloat(round, "alpha", 0f, 1f);
        ObjectAnimator anim3 = ObjectAnimator.ofFloat(round, "scaleX", scale, 1f);
        round.setPivotX(0);
        AnimatorSet animSet2= new AnimatorSet();
        animSet2.play(anim1).with(anim2).with(anim3);
        animSet2.setDuration(100);
        animSet2.start();
    }

    private void closeSearch() {
        ObjectAnimator anim2 = ObjectAnimator.ofFloat(round, "scaleX", 1f, scale);
        ObjectAnimator anim3 = ObjectAnimator.ofFloat(circle, "alpha", 0f, 1f);
        circle.setVisibility(View.VISIBLE);
        list.setVisibility(View.GONE);
        query.setVisibility(View.GONE);
        ObjectAnimator anim4 = ObjectAnimator.ofFloat(round, "alpha", 1f, 0f);
        round.setPivotX(0);
        round.setPivotY(round.getHeight() / 2);
        AnimatorSet animSet1 = new AnimatorSet();
        animSet1.play(anim2).with(anim3).with(anim4);
        animSet1.setDuration(100);
        animSet1.start();
    }

}
