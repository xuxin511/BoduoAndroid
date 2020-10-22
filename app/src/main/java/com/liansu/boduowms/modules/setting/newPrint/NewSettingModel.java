package com.liansu.boduowms.modules.setting.newPrint;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Message;

import com.android.volley.Request;
import com.liansu.boduowms.R;
import com.liansu.boduowms.base.BaseFragment;
import com.liansu.boduowms.base.BaseFragmentModel;
import com.liansu.boduowms.bean.base.UrlInfo;
import com.liansu.boduowms.modules.qualityInspection.bill.QualityInspectionMainActivity;
import com.liansu.boduowms.ui.dialog.MessageBox;
import com.liansu.boduowms.utils.Network.NetCallBackListener;
import com.liansu.boduowms.utils.Network.NetworkError;
import com.liansu.boduowms.utils.Network.RequestHandler;
import com.liansu.boduowms.utils.hander.MyHandler;
import com.liansu.boduowms.utils.log.LogUtil;

import java.util.ArrayList;
import java.util.List;

import static com.liansu.boduowms.ui.dialog.MessageBox.MEDIA_MUSIC_ERROR;

/**
 * @ Des:
 * @ Created by yangyiqing on 2020/6/27.
 */
public class NewSettingModel extends BaseFragmentModel {
    public        String       TAG_GET_PRINT_NAME_LIST           = "SettingModel_GetPrintNameList";
    public        String       TAG_GET_QUALITY_HEAD_LIST_SYNC    = "QualityInspectionBillModel_GetT_QualityHeadListsync";
    private final int          RESULT_TAG_GET_PRINT_NAME_LIST    = 121;

    private       List<String> mLaserPrinterAddressList          = new ArrayList<>();
    private       List<String> mDesktopPrinterAddressList        = new ArrayList<>();

    public NewSettingModel(Context context, MyHandler<BaseFragment> handler) {
        super(context, handler);
    }

    @Override
    public void onHandleMessage(Message msg) {
        NetCallBackListener<String> listener = null;
        switch (msg.what) {
            case RESULT_TAG_GET_PRINT_NAME_LIST:
                listener = mNetMap.get("TAG_GET_PRINT_NAME_LIST");
                break;
            case NetworkError.NET_ERROR_CUSTOM:
                MessageBox.Show(mContext, "获取请求失败_____" + msg.obj, MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                break;
        }
        if (listener != null) {
            listener.onCallBack(msg.obj.toString());
        }
    }


    public void setLaserPrinterAddressList(List<String> list) {
        mLaserPrinterAddressList.clear();
        if (list != null && list.size() > 0) {
            mLaserPrinterAddressList.addAll(list);
        }
    }

    public List<String> getLaserPrinterAddressList() {
        return mLaserPrinterAddressList;
    }


    public void setDesktopPrinterAddressList(List<String> list) {
        mDesktopPrinterAddressList.clear();
        if (list != null && list.size() > 0) {
            mDesktopPrinterAddressList.addAll(list);
        }
    }

    public List<String> getDesktopPrinterAddressList() {
        return mDesktopPrinterAddressList;
    }

    /**
     * @desc: 获取打印机地址
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/30 14:42
     */
    public void requestPrinterAddressList(NetCallBackListener<String> callBackListener) {
        mNetMap.put("TAG_GET_PRINT_NAME_LIST", callBackListener);
        String ModelJson = "{}";
        LogUtil.WriteLog(QualityInspectionMainActivity.class, TAG_GET_PRINT_NAME_LIST, ModelJson);
        RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_GET_PRINT_NAME_LIST, mContext.getString(R.string.setting_print_request_get_print_name_list), mContext, mHandler, RESULT_TAG_GET_PRINT_NAME_LIST, null, UrlInfo.getUrl().GetPrintNameList, ModelJson, null);


    }


}
