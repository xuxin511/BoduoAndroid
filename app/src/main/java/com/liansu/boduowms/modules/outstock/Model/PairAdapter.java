package com.liansu.boduowms.modules.outstock.Model;

import android.content.Context;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class PairAdapter {

  public Pair[]  pairs;

    //适配器绑定键值对数据
    public  void bindAdapter(ArrayAdapter adapter, Spinner spinner, Pair[] array, Context context) {
        adapter = new ArrayAdapter<Pair>(context, android.R.layout.simple_spinner_item, array);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setVisibility(View.VISIBLE);// 设置默认值
        adapter.notifyDataSetChanged();
    }



    public  void addPairs(int i,String key ,String  values,Pair[]  pair) {
        Pair p = new Pair(values, key);
        pair[i - 1] = p;
    }
}
