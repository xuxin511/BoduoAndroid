package com.liansu.boduowms.modules.outstock.offScan;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.liansu.boduowms.R;
import com.liansu.boduowms.ui.dialog.MessageBox;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * @desc: 月台
 * @param:
 * @return:
 * @author: Nietzsche
 * @time 2020/7/26 21:24
 */
@ContentView(R.layout.dialog_balcony_info)
public class BalconyInfoDialogActivity extends AppCompatActivity {
    Context mContext = BalconyInfoDialogActivity.this;
    String  mOrderNo = "";
    @ViewInject(R.id.balcony_info_title)
    TextView mTitle;
    @ViewInject(R.id.dialog_balcony_order_no)
    EditText mEditOrderNo;
    @ViewInject(R.id.dialog_balcony_no)
    EditText mBalconyNo;
    @ViewInject(R.id.dialog_balcony_info_positiveTextView)
    TextView mPositiveTextView;
    @ViewInject(R.id.dialog_balcony_info_negativeTextView)
    TextView mNegativeTextView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN); //隐藏输入法
        x.view().inject(this);
        try {
            mTitle.setText("月台");
            mOrderNo = getIntent().getParcelableExtra("ORDER_NO");
            if (mOrderNo != null && !mOrderNo.equals("")) {
                mEditOrderNo.setText(mOrderNo);
            }
            mPositiveTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    saveData();
                }
            });

            mNegativeTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        } catch (Exception e) {
            MessageBox.Show(mContext, "出现意料之外的异常:" + e.getMessage());
        }
    }

    @Event(value = {R.id.dialog_balcony_no}, type = View.OnKeyListener.class)
    private boolean barcodeScan(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP)// 如果为Enter键
        {
            keyBoardCancle();
            switch (v.getId()) {
                case R.id.dialog_balcony_no:
                    break;


            }

            return true;
        }
        return false;
    }



    /**
     * @desc: 保存月台数据
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/2 17:32
     */
    public void saveData() {
        String balconyDesc = mBalconyNo.getText().toString().trim();
        String orderNo=mEditOrderNo.getText().toString().trim();
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString("BALCONY_DESC", balconyDesc);
        bundle.putString("ORDER_NO", orderNo);
        intent.putExtras(bundle);
        // 设置返回码和返回携带的数据
        setResult(Activity.RESULT_OK, intent);
        finish();
    }





    /**
     * 隐藏键盘
     */
    public void keyBoardCancle() {
        View view = getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager inputmanger = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

}
