package com.liansu.boduowms.base;

import android.content.Context;

import com.liansu.boduowms.bean.barcode.OutBarcodeInfo;
import com.liansu.boduowms.bean.order.OrderHeaderInfo;
import com.liansu.boduowms.bean.order.OrderType;
import com.liansu.boduowms.modules.instock.activeOtherStorage.bill.ActiveOtherBillPresenter;
import com.liansu.boduowms.modules.instock.activeOtherStorage.scan.ActiveOtherScanPresenter;
import com.liansu.boduowms.modules.instock.baseOrderBusiness.bill.BaseOrderBillChoicePresenter;
import com.liansu.boduowms.modules.instock.baseOrderBusiness.bill.IBaseOrderBillChoiceView;
import com.liansu.boduowms.modules.instock.baseOrderBusiness.scan.BaseOrderScanPresenter;
import com.liansu.boduowms.modules.instock.baseOrderBusiness.scan.IBaseOrderScanView;
import com.liansu.boduowms.modules.instock.outsourcingStorage.bill.OutsourcingStorageBillPresenter;
import com.liansu.boduowms.modules.instock.outsourcingStorage.scan.OutsourcingStorageScanPresenter;
import com.liansu.boduowms.modules.instock.productStorage.bill.ProductStorageBillPresenter;
import com.liansu.boduowms.modules.instock.productStorage.scan.IProductStoragerScanView;
import com.liansu.boduowms.modules.instock.productStorage.scan.ProductStorageScanPresenter;
import com.liansu.boduowms.modules.instock.productionReturnsStorage.bill.ProductionReturnsStorageBillPresenter;
import com.liansu.boduowms.modules.instock.purchaseStorage.bill.PurchaseStorageBillPresenter;
import com.liansu.boduowms.modules.instock.purchaseStorage.scan.PurchaseStorageScanPresenter;
import com.liansu.boduowms.modules.instock.salesReturn.bill.SalesReturnStorageBillPresenter;
import com.liansu.boduowms.modules.instock.transferToStorage.bill.TransferToStorageBillPresenter;
import com.liansu.boduowms.modules.outstock.baseOutStockBusiness.baseReviewScan.BaseReviewScanPresenter;
import com.liansu.boduowms.modules.outstock.baseOutStockBusiness.baseReviewScan.IBaseReviewScanView;
import com.liansu.boduowms.modules.outstock.baseOutStockBusiness.offScan.BaseOutStockBusinessPresenter;
import com.liansu.boduowms.modules.outstock.baseOutStockBusiness.offScan.IBaseOutStockBusinessView;
import com.liansu.boduowms.modules.outstock.purchaseInspection.reviewScan.PurchaseInspectionReviewScanPresenter;
import com.liansu.boduowms.modules.outstock.purchaseReturn.offscan.IPurchaseReturnOffScanView;
import com.liansu.boduowms.modules.outstock.purchaseReturn.offscan.PurchaseReturnOffScanPresenter;
import com.liansu.boduowms.utils.hander.MyHandler;

import java.util.List;

/**
 * @ Des:
 * @ Created by yangyiqing on 2020/7/13.
 */
public class BasePresenterFactory {
    /**
     * @desc: 入库列表工厂方法
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/13 23:53
     */
    public static <T extends IBaseOrderBillChoiceView> BaseOrderBillChoicePresenter getBaseOrderBillChoicePresenter(Context context, T view, MyHandler<BaseActivity> handler, String businssType) {
        BaseOrderBillChoicePresenter presenter = null;
        if (businssType != null) {
            if (businssType.equals(OrderType.IN_STOCK_ORDER_TYPE_PURCHASE_STORAGE)) {
                presenter = new PurchaseStorageBillPresenter(context, view, handler, businssType);
            } else if (businssType.equals(OrderType.IN_STOCK_ORDER_TYPE_OUTSOURCING_STORAGE)) {
                presenter = new OutsourcingStorageBillPresenter(context, view, handler, businssType);
            } else if (businssType.equals(OrderType.IN_STOCK_ORDER_TYPE_TRANSFER_TO_STORAGE)) {
                presenter = new TransferToStorageBillPresenter(context, view, handler, businssType);
            } else if (businssType.equals(OrderType.IN_STOCK_ORDER_TYPE_ACTIVE_OTHER_STORAGE)) {
                presenter = new ActiveOtherBillPresenter(context, view, handler, businssType);
            } else if (businssType.equals(OrderType.IN_STOCK_ORDER_TYPE_PRODUCT_STORAGE)) {
                presenter = new ProductStorageBillPresenter(context, view, handler, businssType);
            } else if (businssType.equals(OrderType.IN_STOCK_ORDER_TYPE_SALES_RETURN_STORAGE)) {
                presenter = new SalesReturnStorageBillPresenter(context, view, handler, businssType);
            } else if (businssType.equals(OrderType.IN_STOCK_ORDER_TYPE_PRODUCTION_RETURNS_STORAGE)) {
                presenter = new ProductionReturnsStorageBillPresenter(context, view, handler, businssType);
            }
        }

        return presenter;
    }

    /**
     * @desc: 入库扫描业务工厂方法
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/13 23:54
     */
    public static <T extends IBaseOrderScanView> BaseOrderScanPresenter getBaseOrderScanPresenter(Context context, T view, MyHandler<BaseActivity> handler, OrderHeaderInfo orderHeaderInfo, List<OutBarcodeInfo> barCodeInfos, String businssType) {
        BaseOrderScanPresenter presenter = null;
        if (businssType != null) {
            if (businssType.equals(OrderType.IN_STOCK_ORDER_TYPE_PURCHASE_STORAGE)) {
                presenter = new PurchaseStorageScanPresenter(context, view, handler, orderHeaderInfo, barCodeInfos);
            } else if (businssType.equals(OrderType.IN_STOCK_ORDER_TYPE_OUTSOURCING_STORAGE)) {
                presenter = new OutsourcingStorageScanPresenter(context, view, handler, orderHeaderInfo, barCodeInfos);
            } else if (businssType.equals(OrderType.IN_STOCK_ORDER_TYPE_TRANSFER_TO_STORAGE)) {
//                presenter = new TransferToStorageScanPresenter(context, view, handler, orderHeaderInfo, barCodeInfos);
            } else if (businssType.equals(OrderType.IN_STOCK_ORDER_TYPE_ACTIVE_OTHER_STORAGE)) {
                presenter = new ActiveOtherScanPresenter(context, view, handler, orderHeaderInfo, barCodeInfos);
            } else if (businssType.equals(OrderType.IN_STOCK_ORDER_TYPE_PRODUCT_STORAGE)) {
                presenter = new ProductStorageScanPresenter(context, (IProductStoragerScanView) view, handler, orderHeaderInfo, barCodeInfos);
            }
        }

        return presenter;
    }

    /**
     * @desc: 发货复核工厂类
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/8/4 23:14
     */
    public static <T extends IBaseReviewScanView> BaseReviewScanPresenter getBaseReviewScanPresenter(Context context, T view, MyHandler<BaseActivity> handler, String businssType) {
        BaseReviewScanPresenter presenter = null;
        if (businssType != null) {
            if (businssType.equals(OrderType.OUT_STOCK_ORDER_TYPE_PURCHASE_INSPECTION)) {
                presenter = new PurchaseInspectionReviewScanPresenter(context, view, handler);
            }
        }
        return presenter;
    }

    /**
     * @desc: 出库下架工厂类
     * @param: T 要和子类的 视图接口一样
     * @return:
     * @author: Nietzsche
     * @time 2020/8/4 23:14
     */
    public static <T extends IBaseOutStockBusinessView> BaseOutStockBusinessPresenter getBaseOutStockBusinessPresenter(Context context, T view, MyHandler<BaseActivity> handler, String businssType) {
        BaseOutStockBusinessPresenter presenter = null;
        if (businssType != null) {
            if (businssType.equals(OrderType.OUT_STOCK_ORDER_TYPE_PURCHASE_RETURN)) {
                presenter = new PurchaseReturnOffScanPresenter(context, (IPurchaseReturnOffScanView) view, handler);
            }
        }
        return presenter;
    }
}
