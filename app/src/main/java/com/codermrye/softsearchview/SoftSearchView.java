package com.codermrye.softsearchview;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class SoftSearchView extends LinearLayout{

    private View width;
    private View round;
    private View list;
    private View query;
    private float scale;
    private View circle;

    private Context mContext;
    // 将监听事件封装
    private EditText edit_query;
    private ImageView img_delete;
    private ImageView img_search;

    public SoftSearchView(Context context) {
        super(context);
        this.mContext = context;
        initView(context);
    }

    public SoftSearchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private ArrayList<OnSearchListener> mListeners;
    public void addTextChangedListener(OnSearchListener listener) {
        if (mListeners == null) {
            mListeners = new ArrayList<OnSearchListener>();
        }
        mListeners.add(listener);
    }
    public void removeTextChangedListener(OnSearchListener listener) {
        if (mListeners != null) {
            int i = mListeners.indexOf(listener);

            if (i >= 0) {
                mListeners.remove(i);
            }
        }
    }

    private TextWatcher textWatcher;
    public void setTextWatcher(TextWatcher textWatcher){
        this.textWatcher = textWatcher;
        edit_query.addTextChangedListener(textWatcher);
    }

    private void initView(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.view_choice_up_search, this, true);
        width = findViewById(R.id.voice);
        round = findViewById(R.id.round);
        list = findViewById(R.id.list_query);
        query = findViewById(R.id.ll_query);
        circle = findViewById(R.id.circle);

        edit_query = findViewById(R.id.edit_query);
        img_delete = findViewById(R.id.img_delete);
        img_delete.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                edit_query.setText("");
            }
        });
        img_search = findViewById(R.id.img_search);
        img_search.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // 将搜索内容记录到历史搜索中去
                String search_str = edit_query.getText().toString();
                if(TextUtils.equals(search_str,"")){
                    Toast.makeText(mContext,"没有输入搜索文本",Toast.LENGTH_LONG).show();
                }else {
                    SharedPreferences shar  = mContext.getSharedPreferences("dataSharedPreferences",MODE_PRIVATE);
                    String history1 = shar.getString("history1","");
                    String history2 = shar.getString("history2","");
                    SharedPreferences shpData = mContext.getSharedPreferences("dataSharedPreferences",MODE_PRIVATE);
                    SharedPreferences.Editor editor = shpData.edit();
                    editor.putString("history1",search_str);
                    editor.putString("history2",history1);
                    editor.putString("history3",history2);
                    editor.apply();
                    // 刷新搜索历史列表
                }
                // 将搜索的文本内容回调出去

            }
        });
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
