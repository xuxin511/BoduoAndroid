package com.liansu.boduowms.modules.instock.baseOrderBusiness.upshelf.scan;

/**
 * @ Des:
 * @ Created by yangyiqing on 2020/6/27.
 */
public class UpShelfScanPresenter {
//    private Context                 mContext;
//    private UpShelfScanModel        mModel;
//    private IUpShelfScanView        mView;
//    private MyHandler<BaseActivity> mHandler;
//
//    public void onHandleMessage(Message msg) {
//        mModel.onHandleMessage(msg);
//    }
//
//    public UpShelfScanPresenter(Context context, IUpShelfScanView view, MyHandler<BaseActivity> handler, OrderHeaderInfo receipt_model, List<OutBarcodeInfo> barCodeInfos) {
//        this.mContext = context;
//        this.mView = view;
//        this.mModel = new UpShelfScanModel(context, handler);
//        this.mHandler = handler;
//        this.mModel.setReceiptModel(receipt_model);
//        this.mModel.setBarCodeInfos(barCodeInfos);
//    }
//
//    public UpShelfScanModel getModel() {
//        return mModel;
//    }
//
//    /**
//     * @desc: 获取采购订单表体明细
//     * @param:
//     * @return:
//     * @author: Nietzsche
//     * @time 2020/6/27 21:44
//     */
//    public void getReceiptDetailList(final OrderHeaderInfo receiptModel) {
//        mModel.requestReceiptDetail(receiptModel, new NetCallBackListener<String>() {
//            @Override
//            public void onCallBack(String result) {
//                LogUtil.WriteLog(UpShelfScan.class, mModel.TAG_GetT_InStockDetailListByHeaderIDADF, result);
//                try {
//                    ReturnMsgModelList<OrderDetailInfo> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<ReturnMsgModelList<ReceiptDetail_Model>>() {
//                    }.getType());
//                    if (returnMsgModel.getHeaderStatus().equals("S")) {
//                        mModel.setReceiptDetailModels(returnMsgModel.getModelJson());
//                        if (mModel.getReceiptDetailModels().size() > 0) {
//                            mView.bindListView(mModel.getReceiptDetailModels());
////
////                            //自动确认扫描箱号
////                            BindListVIew(receiptDetailModels);
////                            if (barCodeInfos != null) {
////                                isDel = false;
////                                Bindbarcode(barCodeInfos);
////                            }
//                        } else {
//                            MessageBox.Show(mContext, returnMsgModel.getMessage());
//                        }
//                    }
//                } catch (Exception ex) {
//                    MessageBox.Show(mContext, ex.getMessage());
//                }
//
//
//            }
//        });
//    }
//
//    /**
//     * @desc: 扫描托盘码
//     * @param:
//     * @return:
//     * @author: Nietzsche
//     * @time 2020/7/3 17:13
//     */
//    public void onScan(String barcode) {
//        if (barcode.equals("")) return;
//        if (mModel.getAreaInfo() == null) {
//            MessageBox.Show(mContext, "库位信息为空,请先扫描库位");
//            mView.onAreaNoFocus();
//            return;
//        }
//        mModel.requestBarcodeInfo(barcode, new NetCallBackListener<String>() {
//            @Override
//            public void onCallBack(String result) {
//                LogUtil.WriteLog(UpShelfScan.class, mModel.TAG_GetT_PalletDetailByBarCodeADF, result);
//                try {
//                    ReturnMsgModelList<OutBarcodeInfo> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<ReturnMsgModelList<OutBarcodeInfo>>() {
//                    }.getType());
//                    if (returnMsgModel.getHeaderStatus().equals("S")) {
//                        List<OutBarcodeInfo> list = returnMsgModel.getModelJson();
//                        if (list != null && list.size() == 1) {
//                            OutBarcodeInfo info = list.get(0);
//                            if (info != null) {
//                                BaseMultiResultInfo<Boolean, OrderDetailInfo> detailResult = mModel.findMaterialInfo(info); //找到物料行
//                                if (detailResult.getHeaderStatus()) {
//                                    OrderDetailInfo orderDetailInfo = detailResult.getInfo();
//                                    BaseMultiResultInfo<Boolean, Void> checkBarcodeResult = mModel.checkBarcode(orderDetailInfo, info);
//                                    if (checkBarcodeResult.getHeaderStatus()) {
//                                        BaseMultiResultInfo<Boolean, Void> updateResult = mModel.updateMaterialInfo(orderDetailInfo, info);
//                                        if (!updateResult.getHeaderStatus()) {
//                                            MessageBox.Show(mContext, detailResult.getMessage());
//                                        }
//                                    } else {
//                                        MessageBox.Show(mContext, checkBarcodeResult.getMessage());
//                                    }
//
//                                } else {
//                                    MessageBox.Show(mContext, detailResult.getMessage());
//                                }
//                            }
//
//                        }
//                    }
//                } catch (Exception e) {
//                    MessageBox.Show(mContext, "获取条码信息出现意料之外的异常:" + e.getMessage());
//                } finally {
//                    mView.onBarcodeFocus();
//                }
//            }
//
//        });
//
//
//    }
//
//    /**
//     * @desc: 获取库位信息
//     * @param:
//     * @return:
//     * @author: Nietzsche
//     * @time 2020/7/3 15:31
//     */
//    public void getAreaInfo(String areaNo) {
//        if (!areaNo.equals("")) {
//            mModel.requestAreaInfo(areaNo, new NetCallBackListener<String>() {
//                @Override
//                public void onCallBack(String result) {
//                    try {
//                        LogUtil.WriteLog(UpShelfScan.class, mModel.TAG_GetAreaModelADF, result);
//                        ReturnMsgModel<AreaInfo> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<ReturnMsgModel<AreaInfo>>() {
//                        }.getType());
//                        if (returnMsgModel.getHeaderStatus().equals("S")) {
//                            AreaInfo areaInfo = returnMsgModel.getModelJson();
//                            if (areaInfo != null) {
//                                mModel.setAreaInfo(areaInfo);
//                                mView.onBarcodeFocus();
//                            } else {
//                                MessageBox.Show(mContext, "出现预期之外的异常,获取的库位信息为空");
//                                return;
//                            }
//                        } else {
//                            MessageBox.Show(mContext, returnMsgModel.getMessage());
//                            mView.onAreaNoFocus();
//                        }
//                    } catch (Exception ex) {
//                        MessageBox.Show(mContext, ex.getMessage());
//                        mView.onAreaNoFocus();
//                    }
//
//                }
//            });
//        }
//    }
}
