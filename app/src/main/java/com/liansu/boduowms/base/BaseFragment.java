package com.liansu.boduowms.base;

import android.os.Bundle;
import android.os.Message;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;

import com.liansu.boduowms.utils.hander.IHandleMessage;
import com.liansu.boduowms.utils.hander.MyHandler;

import org.xutils.x;

import java.lang.reflect.Method;

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

    /**
     * @desc: 隐藏软键盘
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/23 15:02
     */
    public void closeKeyBoard(EditText view) {
        if (android.os.Build.VERSION.SDK_INT <= 10) {
            view.setInputType(InputType.TYPE_NULL);
        } else {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            try {
                Class<EditText> cls = EditText.class;
                Method setSoftInputShownOnFocus;
                setSoftInputShownOnFocus = cls.getMethod("setShowSoftInputOnFocus", boolean.class);
                setSoftInputShownOnFocus.setAccessible(true);
                setSoftInputShownOnFocus.invoke(view, false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @desc: 批量关闭软键盘
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/9/1 9:27
     */
    public void closeKeyBoard(EditText... editTexts) {
        if (editTexts.length > 0) {
            for (EditText editText : editTexts) {
                if (editText != null) {
                    closeKeyBoard(editText);
                }

            }

        }

    }

}
