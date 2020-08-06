package com.liansu.boduowms.base;

import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.liansu.boduowms.utils.hander.IHandleMessage;
import com.liansu.boduowms.utils.hander.MyHandler;

import org.xutils.x;

import androidx.fragment.app.Fragment;

/**
 * Created by wyouflf on 15/11/4.
 */
public class BaseFragment extends Fragment implements IHandleMessage {

    private boolean                 injected = false;
    public  MyHandler<BaseFragment> mHandler=new MyHandler<>(this); ;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        injected = true;
//        mHandler=new MyHandler<>(this);
        return x.view().inject(this, inflater, container);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (!injected) {
            x.view().inject(this, this.getView());
        }
    }

    @Override
    public void onHandleMessage(Message message) {

    }
}
