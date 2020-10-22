package com.liansu.boduowms.modules.qualityInspection.randomInspection.bill;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Message;

import com.android.volley.Request;
import com.liansu.boduowms.R;
import com.liansu.boduowms.base.BaseActivity;
import com.liansu.boduowms.base.BaseModel;
import com.liansu.boduowms.bean.base.UrlInfo;
import com.liansu.boduowms.bean.qualitySpection.QualityHeaderInfo;
import com.liansu.boduowms.modules.qualityInspection.bill.QualityInspectionMainActivity;
import com.liansu.boduowms.ui.dialog.MessageBox;
import com.liansu.boduowms.utils.Network.NetCallBackListener;
import com.liansu.boduowms.utils.Network.NetworkError;
import com.liansu.boduowms.utils.Network.RequestHandler;
import com.liansu.boduowms.utils.function.GsonUtil;
import com.liansu.boduowms.utils.hander.MyHandler;
import com.liansu.boduowms.utils.log.LogUtil;

import java.util.ArrayList;
import java.util.List;

import static com.liansu.boduowms.ui.dialog.MessageBox.MEDIA_MUSIC_ERROR;

/**
 * @ Des:
 * @ Created by yangyiqing on 2020/6/27.
 */
public class RandomInspectionBillModel extends BaseModel {
    public        String TAG_GET_CHECK_QUALITY_HEAD_LIST_SYNC      = "RandomInspectionBillModel_TAG_GET_CHECK_QUALITY_HEAD_LIST_SYNC"; //获取抽检列表
    private final int    RESULT_GET_T_CHECK_QUALITY_HEAD_LIST_SYNC = 118;
    ArrayList<QualityHeaderInfo> mQualityOrderList = new ArrayList<>();//单据信息
    public RandomInspectionBillModel(Context context, MyHandler<BaseActivity> handler) {
        super(context, handler);
    }

    @Override
    public void onHandleMessage(Message msg) {
        NetCallBackListener<String> listener = null;
        switch (msg.what) {
            case RESULT_GET_T_CHECK_QUALITY_HEAD_LIST_SYNC:
                listener = mNetMap.get("TAG_GET_CHECK_QUALITY_HEAD_LIST_SYNC");
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

    /**
     * @desc: 获取抽检列表
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/6/27 17:52
     */
    public void requestRandomInspectionBillInfoList(QualityHeaderInfo headerInfo, NetCallBackListener<String> callBackListener) {
        mNetMap.put("TAG_GET_CHECK_QUALITY_HEAD_LIST_SYNC", callBackListener);
        String ModelJson = GsonUtil.parseModelToJson(headerInfo);
        LogUtil.WriteLog(QualityInspectionMainActivity.class, TAG_GET_CHECK_QUALITY_HEAD_LIST_SYNC, ModelJson);
        RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_GET_CHECK_QUALITY_HEAD_LIST_SYNC, mContext.getString(R.string.message_request_get_order_header_info), mContext, mHandler, RESULT_GET_T_CHECK_QUALITY_HEAD_LIST_SYNC, null, UrlInfo.getUrl().GetT_CheckQualityHeadListsync, ModelJson, null);
    }

    /**
     * @desc: 临时存放质检订单列表数据
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/6/27 18:10
     */
    public void setQualityInspectionInfoList(List<QualityHeaderInfo> list) {
        mQualityOrderList.clear();
        if (list != null && list.size() != 0) {
            mQualityOrderList.addAll(list);
        }
    }

    public List<QualityHeaderInfo> getQualityInspectionInfoList() {
        return mQualityOrderList;
    }

    public void onReset() {

        mQualityOrderList.clear();
    }
}
