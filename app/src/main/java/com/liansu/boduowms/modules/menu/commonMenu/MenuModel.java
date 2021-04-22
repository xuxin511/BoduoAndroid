package com.liansu.boduowms.modules.menu.commonMenu;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.liansu.boduowms.R;
import com.liansu.boduowms.base.BaseApplication;
import com.liansu.boduowms.bean.menu.MenuChildrenInfo;
import com.liansu.boduowms.bean.menu.MenuInfo;
import com.liansu.boduowms.bean.menu.MenuType;
import com.liansu.boduowms.bean.order.OrderType;
import com.liansu.boduowms.modules.inHouseStock.adjustStock.AdjustStock;
import com.liansu.boduowms.modules.inHouseStock.inventory.InventoryHead;
import com.liansu.boduowms.modules.inHouseStock.inventory.InventoryScann;
import com.liansu.boduowms.modules.inHouseStock.inventoryMovement.InventoryMovementScan;
import com.liansu.boduowms.modules.inHouseStock.query.QueryStock;
import com.liansu.boduowms.modules.inHouseStock.reprintLabel.ReprintPalletLabel;
import com.liansu.boduowms.modules.instock.baseInstockReturnBusiness.scan.InStockReturnStorageScan;
import com.liansu.boduowms.modules.instock.baseInstockReturnBusiness.scan.InStockReturnsStorageScanModel;
import com.liansu.boduowms.modules.instock.baseOrderBusiness.bill.BaseOrderBillChoice;
import com.liansu.boduowms.modules.instock.baseOrderBusiness.scan.BaseOrderScan;
import com.liansu.boduowms.modules.instock.batchPrint.order.BaseOrderLabelPrintSelect;
import com.liansu.boduowms.modules.instock.combinePallet.InstockCombinePallet;
import com.liansu.boduowms.modules.instock.noSourceOtherStorage.scan.NoSourceOtherScan;
import com.liansu.boduowms.modules.instock.productStorage.printPalletScan.PrintPalletScan;
import com.liansu.boduowms.modules.instock.productStorage.scan.ProductStorageScan;
import com.liansu.boduowms.modules.instock.replenishment.InStockHouseReplenishment;
import com.liansu.boduowms.modules.instock.salesReturn.print.SalesReturnPrint;
import com.liansu.boduowms.modules.instock.salesReturn.scan.SalesReturnStorageScan;
import com.liansu.boduowms.modules.instock.transferToStorage.scan.TransferToStorageScan;
import com.liansu.boduowms.modules.menu.commonMenu.subMenu.CommonBusinessSubMenu;
import com.liansu.boduowms.modules.outstock.Model.MenuOutStockModel;
import com.liansu.boduowms.modules.outstock.SalesOutstock.OutstockOrderSelect;
import com.liansu.boduowms.modules.outstock.SalesOutstock.OutstockRawmaterialActivity;
import com.liansu.boduowms.modules.outstock.SalesOutstock.OutstockWaybillStates;
import com.liansu.boduowms.modules.outstock.SalesOutstock.SalesOutStockCallback;
import com.liansu.boduowms.modules.outstock.purchaseInspection.offScan.bill.PurchaseInspectionBill;
import com.liansu.boduowms.modules.qualityInspection.bill.QualityInspectionMainActivity;
import com.liansu.boduowms.modules.qualityInspection.randomInspection.bill.RandomInspectionBill;
import com.liansu.boduowms.utils.function.GsonUtil;
import com.liansu.boduowms.utils.log.LogUtil;

import java.util.ArrayList;
import java.util.List;

import static com.liansu.boduowms.bean.order.OrderType.OUT_STOCK_ORDER_TYPE_CONSIGNMENT_PLUS_BILL;
import static com.liansu.boduowms.bean.order.OrderType.OUT_STOCK_ORDER_TYPE_OUTSTOCK_ORDERSELECT;

/**
 * @ Des:
 * @ Created by yangyiqing on 2020/8/18.
 */
public class MenuModel {
    private List<MenuChildrenInfo> mMenuChildrenInfoList = new ArrayList<>();
    private Context                mContext;
    private int                    mMenuType;

    public MenuModel() {
    }


    public MenuModel(Context context, int menuType) {
        mContext = context;
        mMenuType = menuType;
        setMenuChildrenInfoList(getMenuChildrenList(BaseApplication.mCurrentMenuList, menuType));
    }


    public void setMenuChildrenInfoList(List<MenuChildrenInfo> list) {
        mMenuChildrenInfoList.clear();
        if (list != null && list.size() > 0) {
            mMenuChildrenInfoList.addAll(list);
        }

    }


    public List<MenuChildrenInfo> getMenuChildrenInfoList() {
        return mMenuChildrenInfoList;
    }

    public int getMenuType() {
        return mMenuType;
    }

    /**
     * @desc: 获取订单类型
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/8/18 23:25
     */
    public int getVoucherType(String moduleName) {
        int voucherType = -1;
        for (MenuChildrenInfo info : mMenuChildrenInfoList) {
            if (moduleName.equals(info.getTitle())) {
                voucherType = Integer.parseInt(info.getPath());
                break;
            }
        }

        return voucherType;
    }

    /**
     * @desc: 获取指定模块的名称
     * @param: list 全局menuList 对象集合  {@link BaseApplication.mCurrentMenuList}, menuType 业务类型 入库,出库，库内, menuStyleType 菜单数据等级 二级菜单数据，
     * @param: 还是三级菜单数据，moduleKey 如果是三级菜单需要提供 功能标识,二级菜单在未来可能要提供 例如 {@link MenuType.MENU_MODULE_TYPE_OUT_STOCK_OFF_SHELF}
     * @return:
     * @author: Nietzsche
     * @time 2020/9/18 11:04
     */
    public static String getModuleTitle(List<MenuInfo> list, int menuType, int menuStyleType, int voucherType, String moduleKey) {
        String voucherTypeStr = voucherType + "";
        String title = "";
        List<MenuChildrenInfo> childrenInfoList = getMenuChildrenList(list, menuType);
        if (childrenInfoList != null && childrenInfoList.size() > 0) {
            for (MenuChildrenInfo item : childrenInfoList) {
                String sVoucherType = item.getPath() != null ? item.getPath() : "";
                if (sVoucherType.trim().equals(voucherTypeStr)) {
                    if (menuStyleType == MenuType.MENU_STYLE_TYPE_SECONDARY_MENU) {
                        if (moduleKey == MenuType.MENU_MODULE_TYPE_NONE) {
                            title = item.getTitle();
                            break;
                        } else {
                            String sModuleKey = item.getComponent() != null ? item.getComponent() : "";
                            if (sModuleKey.trim().equals(moduleKey)) {
                                title = item.getTitle();
                                break;
                            }
                        }

                    } else if (menuStyleType == MenuType.MENU_STYLE_TYPE_THREE_LEVEL_MENU) {
                        List<MenuChildrenInfo> thirdChildrenList = item.getChildren();
                        if (thirdChildrenList != null && thirdChildrenList.size() > 0) {
                            for (MenuChildrenInfo info : thirdChildrenList) {
                                String sModuleKey = info.getComponent() != null ? info.getComponent() : "";
                                String thirdTitle = info.getTitle() != null ? info.getTitle() : "";
                                if (sModuleKey.trim().equals(moduleKey)) {
                                    title = thirdTitle;
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
        return title;

    }

    /**
     * @desc: 获取二级菜单title
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/9/18 11:19
     */
    public static String getSecondaryMenuModuleTitle(int menuType, int voucherType) {
        return getModuleTitle(BaseApplication.mCurrentMenuList, menuType, MenuType.MENU_STYLE_TYPE_SECONDARY_MENU, voucherType, MenuType.MENU_MODULE_TYPE_NONE);
    }

    /**
     * @desc: 获取三级菜单title
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/9/18 11:56
     */
    public static String getThirdLevelMenuModuleTitle(int menuType, int voucherType, String moduleKey) {
        return getModuleTitle(BaseApplication.mCurrentMenuList, menuType, MenuType.MENU_STYLE_TYPE_THREE_LEVEL_MENU, voucherType, moduleKey);
    }

    /**
     * @desc: 获取菜单入库模块
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/9 9:09
     */
    public static List<MenuChildrenInfo> getMenuChildrenList(List<MenuInfo> list, int menuType) {
        if (list != null && list.size() > 0) {
            for (MenuInfo menuInfo : list) {
                if (menuInfo.getComponent().equals(Integer.toString(menuType))) {
                    List<MenuChildrenInfo> menuChildrenInfos = menuInfo.getChildren();
                    return menuChildrenInfos;
                }
            }
        }
        return null;
    }

    /**
     * @desc: 加载入库数据
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/9/16 15:04
     */
    public void loadInStockMenuData(MenuChildrenInfo item) {
        if (item != null) {
            String path = item.getPath();
            int voucherType = Integer.parseInt(path);
            int icon = -1;
            //入库
            //41 批量打印
            if (voucherType == OrderType.IN_HOUSE_STOCK_ORDER_TYPE_BATCH_PRINT_VALUE) {
                icon = R.drawable.b_batch_print;
            }
            //22  采购入库
            else if (voucherType == OrderType.IN_STOCK_ORDER_TYPE_PURCHASE_STORAGE_VALUE) {
                icon = R.drawable.b_purchase_storage_icon;
            }
            //47 到货抽检
            else if (voucherType == OrderType.IN_STOCK_ORDER_TYPE_RANDOM_INSPECTION_STORAGE_VALUE) {
                icon = R.drawable.b_spot_check_icon;
            }
            //45  生产入库
            else if (voucherType == OrderType.IN_STOCK_ORDER_TYPE_PRODUCT_STORAGE_VALUE) {
                icon = R.drawable.b_instock;
            }
            //68 生产入库打印
            else if (voucherType == OrderType.IN_STOCK_ORDER_TYPE_PRODUCT_STORAGE_PRINT_VALUE) {
                icon = R.drawable.b_batch_print;
            }
            //52 工单退料入库
            else if (voucherType == OrderType.IN_STOCK_ORDER_TYPE_PRODUCTION_RETURNS_STORAGE_VALUE) {
                icon = R.drawable.b_work_order_return;
            }
            //24 二阶段调拨入库
            else if (voucherType == OrderType.IN_STOCK_ORDER_TYPE_TWO_STAGE_TRANSFER_TO_STORAGE_VALUE) {
                icon = R.drawable.b_transfer_to_storage;
            }

            //58 一阶段调拨入库
            else if (voucherType == OrderType.IN_STOCK_ORDER_TYPE_ONE_STAGE_TRANSFER_TO_STORAGE_VALUE) {
                icon = R.drawable.b_transfer_to_storage;
            }

            //44 有源杂入
            else if (voucherType == OrderType.IN_STOCK_ORDER_TYPE_ACTIVE_OTHER_STORAGE_VALUE) {
                icon = R.drawable.b_instock;
            }

            //66 无源杂入
            else if (voucherType == OrderType.IN_STOCK_ORDER_TYPE_NO_SOURCE_OTHER_STORAGE_VALUE) {
                icon = R.drawable.b_scan_other_storage;
            }
            //72 销售退货打印
            else if (voucherType == OrderType.IN_STOCK_ORDER_TYPE_SALES_RETURN_PRINT_STORAGE_VALUE) {
                icon = R.drawable.b_batch_print;
            }
            //26 有源销售退货
            else if (voucherType == OrderType.IN_STOCK_ORDER_TYPE_SALES_RETURN_STORAGE_VALUE) {
                icon = R.drawable.b_sales_return;
            }
            //69 无源销售退货
            else if (voucherType == OrderType.IN_STOCK_ORDER_TYPE_NO_SOURCE_SALES_RETURN_STORAGE_VALUE) {
                icon = R.drawable.b_sales_return;
            }
            //70 质检合格
            else if (voucherType == OrderType.IN_STOCK_ORDER_TYPE_QUALITY_INSPECTION_VALUE) {
                icon = R.drawable.b_qualified;
            }

            if (icon != -1) {
                item.setIcon(icon);
            }

        }
    }

    /**
     * @desc: 加载出库数据
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/9/16 15:04
     */
    public void loadOutStockMenuData(MenuChildrenInfo item) {
        if (item != null) {
            String path = item.getPath();
            int voucherType = Integer.parseInt(path);
            int icon = -1;
            switch (voucherType) {
                case OrderType.OUT_STOCK_ORDER_TYPE_SALES_OUT_STOCK_VALUE://29 销售出库
                    icon = R.drawable.b_sales_out_of_stock;
                    break;
                case OrderType.OUT_STOCK_ORDER_TYPE_PURCHASE_INSPECTION_VALUE://28 采购验退
                case OrderType.OUT_STOCK_ORDER_TYPE_PURCHASE_PRODUCT_VALUE://61 成品验退
                case OrderType.OUT_STOCK_ORDER_TYPE_PURCHASE_SALERETURN_VALUE://62 销售验退
                    icon = R.drawable.b_outstock_return;
                    break;
                case OrderType.OUT_STOCK_ORDER_TYPE_PURCHASE_RETURN_VALUE://27 仓退
                    icon = R.drawable.b_outsourcing_dispatch;
                    break;
                case OrderType.OUT_STOCK_ORDER_TYPE_PURCHASE_ROWMATERIAL_VALUE://46 原材料发货
                case OrderType.OUT_STOCK_ORDER_TYPE_PURCHASE_OUTSOURC_VALUE://57 委外发料
                case OrderType.OUT_STOCK_ORDER_TYPE_PURCHASE_SENDCARSORDER_VALUE://36派车单
                    icon = R.drawable.b_stock_out;
                    break;
                case OrderType.OUT_STOCK_ORDER_TYPE_PURCHASE_ALLOCATION_VALUE://25 一阶段调拨
                case 30://30 二阶段调拨
                case   OUT_STOCK_ORDER_TYPE_CONSIGNMENT_PLUS_BILL:
                    icon = R.drawable.b_transfer_out_to_storage;
                    break;
                case 55://55 杂出
                    icon = R.drawable.b_other_out;
                    break;
                case 63://63 二阶段回调
                    icon = R.drawable.b_allocation_approval;
                    break;
                case 56://56 行政领用单
                    icon = R.drawable.b_administrative_requisition;
                    break;
                case OUT_STOCK_ORDER_TYPE_OUTSTOCK_ORDERSELECT://发货通知单查询
                    icon = R.drawable.b_inventory_scan;
                    break;
            }
            if (icon != -1) {
                item.setIcon(icon);
            }

        }
    }

    /**
     * @desc: 加载库内数据
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/9/16 15:04
     */
    public void loadInHouseStockMenuData(MenuChildrenInfo item) {
        if (item != null) {
            String path = item.getPath();
            int voucherType = Integer.parseInt(path);
            int icon = -1;
            //库内
            //34 移库
            if (voucherType == OrderType.IN_HOUSE_STOCK_ORDER_TYPE_INVENTORY_MOVEMENT) {
                icon = R.drawable.b_mobile_warehouse_inventory;
            }
            //35  库存调整
            else if (voucherType == OrderType.IN_HOUSE_STOCK_ORDER_TYPE_ADJUST_STOCK) {
                icon = R.drawable.b_inventory_adjustment;
            }
            //67 盘点
            else if (voucherType == OrderType.IN_HOUSE_STOCK_ORDER_TYPE_INVENTORY) {
                icon = R.drawable.b_inventory_scan;
            }
            //38  库存查询
            else if (voucherType == OrderType.IN_HOUSE_STOCK_ORDER_TYPE_INVENTORY_INQUIRY) {
                icon = R.drawable.b_inventory_inquiry;
            }

            //71 拼托
            else if (voucherType == OrderType.IN_HOUSE_STOCK_ORDER_TYPE_COMBINE_PALLET_VALUE) {
                icon = R.drawable.b_combine_pallet;
            }
            else if (voucherType == OrderType.IN_HOUSE_STOCK_ORDER_TYPE_OPEN_INVENTORY) {
                icon = R.drawable.b_inventory_scan;
            }
            else if(voucherType == OrderType.IN_HOUSE_STOCK_ORDER_TYPE_BEGINNING_INVENTORY){
                icon = R.drawable.b_inventory_scan;
            }
            //75
            else if(voucherType == OrderType.IN_HOUSE_STOCK_ORDER_TYPE_REPLENISHMENT){
                icon = R.drawable.b_in_stock_house_replenishment;
            }
            //77 标签补打
            else if(voucherType == OrderType.IN_HOUSE_STOCK_ORDER_TYPE_REPRINT_PALLET_LABEL){
                icon = R.drawable.b_batch_print;
            }


            if (icon != -1) {
                item.setIcon(icon);
            }

        }
    }

    /**
     * @desc: 加载入库业务
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/9/16 15:24
     */
    protected Intent loadInStockBusiness(MenuChildrenInfo info, int voucherType) {
        Intent intent = new Intent();
        if (voucherType != -1) {
            //入库
            //41 批量打印  1
            if (voucherType == OrderType.IN_HOUSE_STOCK_ORDER_TYPE_BATCH_PRINT_VALUE) {
                intent.setClass(mContext, BaseOrderLabelPrintSelect.class);
                intent.putExtra("BusinessType", OrderType.IN_STOCK_ORDER_TYPE_BATCH_PRINTING);
                intent.putExtra("Title", info.getTitle());
                intent.putExtra("VOUCHER_TYPE", voucherType);
            }
            //22  采购入库
            else if (voucherType == OrderType.IN_STOCK_ORDER_TYPE_PURCHASE_STORAGE_VALUE) {
                intent.setClass(mContext, BaseOrderBillChoice.class);
                intent.putExtra("BusinessType", OrderType.IN_STOCK_ORDER_TYPE_PURCHASE_STORAGE);
                intent.putExtra("Title", info.getTitle());
                intent.putExtra("VoucherType", voucherType);
            }
            //47 到货抽检   1
            else if (voucherType == OrderType.IN_STOCK_ORDER_TYPE_RANDOM_INSPECTION_STORAGE_VALUE) {
                intent.setClass(mContext, RandomInspectionBill.class);
                intent.putExtra("Title", info.getTitle());
                intent.putExtra("VoucherType", voucherType);
            }
            //45  生产入库
            else if (voucherType == OrderType.IN_STOCK_ORDER_TYPE_PRODUCT_STORAGE_VALUE) {
                intent.setClass(mContext, ProductStorageScan.class);
                intent.putExtra("BusinessType", OrderType.IN_STOCK_ORDER_TYPE_PRODUCT_STORAGE);
                intent.putExtra("Title", info.getTitle());
                intent.putExtra("VoucherType", voucherType);
            }
            //68 生产入库打印  1
            else if (voucherType == OrderType.IN_STOCK_ORDER_TYPE_PRODUCT_STORAGE_PRINT_VALUE) {
                intent.setClass(mContext, PrintPalletScan.class);
                intent.putExtra("BusinessType", OrderType.IN_STOCK_ORDER_TYPE_PRODUCT_STORAGE);
                intent.putExtra("Title", info.getTitle());
                intent.putExtra("VoucherType", voucherType);
            }
            //52 工单退料入库
            else if (voucherType == OrderType.IN_STOCK_ORDER_TYPE_PRODUCTION_RETURNS_STORAGE_VALUE) {
                intent.setClass(mContext, InStockReturnStorageScan.class);
                intent.putExtra("Title", info.getTitle());
                intent.putExtra("BusinessType", InStockReturnsStorageScanModel.IN_STOCK_RETURN_TYPE_ACTIVE);
                intent.putExtra("VoucherType", voucherType);
            }
            //24 二阶段调拨入库
            else if (voucherType == OrderType.IN_STOCK_ORDER_TYPE_TWO_STAGE_TRANSFER_TO_STORAGE_VALUE) {
                intent.setClass(mContext, TransferToStorageScan.class);
                intent.putExtra("VoucherType", OrderType.IN_STOCK_ORDER_TYPE_TWO_STAGE_TRANSFER_TO_STORAGE_VALUE);
                intent.putExtra("BusinessType", OrderType.IN_STOCK_ORDER_TYPE_TRANSFER_TO_STORAGE);
                intent.putExtra("Title", info.getTitle());
            }
            //58 一阶段调拨入库
            else if (voucherType == OrderType.IN_STOCK_ORDER_TYPE_ONE_STAGE_TRANSFER_TO_STORAGE_VALUE) {
                intent.setClass(mContext, TransferToStorageScan.class);
                intent.putExtra("VoucherType", OrderType.IN_STOCK_ORDER_TYPE_ONE_STAGE_TRANSFER_TO_STORAGE_VALUE);
                intent.putExtra("BusinessType", OrderType.IN_STOCK_ORDER_TYPE_TRANSFER_TO_STORAGE);
                intent.putExtra("Title", info.getTitle());
            }
            //44 有源入库2
            else if (voucherType == OrderType.IN_STOCK_ORDER_TYPE_ACTIVE_OTHER_STORAGE_VALUE) {
                intent.setClass(mContext, BaseOrderScan.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable("OrderHeaderInfo", null);
                bundle.putParcelableArrayList("barCodeInfo", null);
                intent.putExtra("BusinessType", OrderType.IN_STOCK_ORDER_TYPE_ACTIVE_OTHER_STORAGE);
                intent.putExtra("Title", info.getTitle());
                intent.putExtra("VoucherType", voucherType);
                intent.putExtras(bundle);
            }
            //66 无源入库
            else if (voucherType == OrderType.IN_STOCK_ORDER_TYPE_NO_SOURCE_OTHER_STORAGE_VALUE) {
                intent.setClass(mContext, NoSourceOtherScan.class);
                intent.putExtra("Title", info.getTitle());
                intent.putExtra("VoucherType", voucherType);
            }
            //26 有源销售退货
            else if (voucherType == OrderType.IN_STOCK_ORDER_TYPE_SALES_RETURN_STORAGE_VALUE) {
                intent.setClass(mContext, InStockReturnStorageScan.class);
                intent.putExtra("Title", info.getTitle());
                intent.putExtra("BusinessType", InStockReturnsStorageScanModel.IN_STOCK_RETURN_TYPE_ACTIVE);
                intent.putExtra("VoucherType", voucherType);
            }
            //69 无源销售退货
            else if (voucherType == OrderType.IN_STOCK_ORDER_TYPE_NO_SOURCE_SALES_RETURN_STORAGE_VALUE) {
                intent.setClass(mContext, SalesReturnStorageScan.class);
//                intent.setClass(mContext, InStockReturnStorageScan.class);
                intent.putExtra("Title", info.getTitle());
                intent.putExtra("BusinessType", InStockReturnsStorageScanModel.IN_STOCK_RETURN_TYPE_NO_SOURCE);
                intent.putExtra("VoucherType", voucherType);
            }
            //72 销售退货打印 1
            else if (voucherType == OrderType.IN_STOCK_ORDER_TYPE_SALES_RETURN_PRINT_STORAGE_VALUE) {
                intent.setClass(mContext, SalesReturnPrint.class);
                intent.putExtra("BusinessType", OrderType.IN_STOCK_ORDER_TYPE_SALES_RETURN_STORAGE);
                intent.putExtra("Title", info.getTitle());
                intent.putExtra("VoucherType", voucherType);
            }
            //70 质检合格2
            else if (voucherType == OrderType.IN_STOCK_ORDER_TYPE_QUALITY_INSPECTION_VALUE) {
                intent.setClass(mContext, QualityInspectionMainActivity.class);
                intent.putExtra("Title", info.getTitle());
            }
        }
        return intent;
    }

    /**
     * @desc: 加载出库业务
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/9/16 16:06
     */
    protected Intent loadOutStockBusiness(MenuChildrenInfo info, int voucherType) {
        Intent intent = new Intent();
        List<MenuChildrenInfo> secondMenuList = info.getChildren();
        MenuOutStockModel model = new MenuOutStockModel();
        model.Title = info.getTitle();
        model.VoucherType = String.valueOf(voucherType);
        model.secondMenuList=info.getChildren();
        String json = GsonUtil.parseModelToJson(model);
        Uri data = Uri.parse(json);
        try {
            switch (voucherType) {
                case OrderType.OUT_STOCK_ORDER_TYPE_SALES_OUT_STOCK_VALUE://销售出库
                case OrderType.OUT_STOCK_ORDER_TYPE_PURCHASE_INSPECTION_VALUE://28 采购验退
                case OrderType.OUT_STOCK_ORDER_TYPE_PURCHASE_RETURN_VALUE://27 仓退
                case OrderType.OUT_STOCK_ORDER_TYPE_PURCHASE_SENDCARSORDER_VALUE://36派车单
                    intent.setData(data);
                    intent.setClass(mContext, CommonBusinessSubMenu.class);
                    break;
                case OrderType.OUT_STOCK_ORDER_TYPE_PURCHASE_PRODUCT_VALUE://61 成品验退
                case OrderType.OUT_STOCK_ORDER_TYPE_PURCHASE_SALERETURN_VALUE://62 销售验退
                case OrderType.OUT_STOCK_ORDER_TYPE_PURCHASE_ROWMATERIAL_VALUE://46 原材料发货
                    intent.setData(data);
                    intent.setClass(mContext, PurchaseInspectionBill.class);
                    break;
                case OrderType.OUT_STOCK_ORDER_TYPE_PURCHASE_OUTSOURC_VALUE://57 委外发料
                case OrderType.OUT_STOCK_ORDER_TYPE_PURCHASE_ALLOCATION_VALUE://25 一阶段调拨

                case 30://30 二阶段调拨
                case 55://55 杂出
                case 56://56 行政领用单
                    intent.setData(data);
                    intent.setClass(mContext, OutstockRawmaterialActivity.class);
                    break;
                case 63://63 二阶段回调
                    intent.setData(data);
                    intent.setClass(mContext, SalesOutStockCallback.class);
                    break;
                case OUT_STOCK_ORDER_TYPE_CONSIGNMENT_PLUS_BILL:
                    intent.setData(data);
                    intent.setClass(mContext, OutstockWaybillStates.class);
                    break;
                case OUT_STOCK_ORDER_TYPE_OUTSTOCK_ORDERSELECT:
                    intent.setData(data);
                    intent.setClass(mContext, OutstockOrderSelect.class);
                    break;
            }
        } catch (Exception ex) {
            //  MessageBox.Show(this,ex.toString());
            LogUtil.WriteLog(MenuModel.class, "加载菜单出错", ex.toString());
        }
        return intent;
    }

    /**
     * @desc: 加载库内业务
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/9/16 16:05
     */
    protected Intent loadInHouseStockBusiness(MenuChildrenInfo info, int voucherType) {
        Intent intent = new Intent();
        if (voucherType != -1) {
            //库内
            //71 拼托
            if (voucherType == OrderType.IN_HOUSE_STOCK_ORDER_TYPE_COMBINE_PALLET_VALUE) {
                intent.setClass(mContext, InstockCombinePallet.class);
                intent.putExtra("Title", info.getTitle());
            }
            //34 移库
            else if (voucherType == OrderType.IN_HOUSE_STOCK_ORDER_TYPE_INVENTORY_MOVEMENT) {
                intent.setClass(mContext, InventoryMovementScan.class);
                intent.putExtra("Title", info.getTitle());
            }
            //35  库存调整
            else if (voucherType == OrderType.IN_HOUSE_STOCK_ORDER_TYPE_ADJUST_STOCK) {
                intent.setClass(mContext, AdjustStock.class);
                intent.putExtra("Title", info.getTitle());
            }
            //67 盘点
            else if (voucherType == OrderType.IN_HOUSE_STOCK_ORDER_TYPE_INVENTORY) {
                MenuOutStockModel model = new MenuOutStockModel();
                model.Title = getThirdLevelMenuModuleTitle(MenuType.MENU_TYPE_IN_HOUSE_STOCK,OrderType.IN_HOUSE_STOCK_ORDER_TYPE_INVENTORY,MenuType.MENU_MODULE_TYPE_IN_HOUSE_STOCK_INVENTORY_LIST);
                model.VoucherType =String.valueOf( OrderType.IN_HOUSE_STOCK_ORDER_TYPE_INVENTORY);
                String json = GsonUtil.parseModelToJson(model);
                Uri data = Uri.parse(json);
                intent.setData(data);
                intent.setClass(mContext, InventoryHead.class);
            }
            //38  库存查询
            else if (voucherType == OrderType.IN_HOUSE_STOCK_ORDER_TYPE_INVENTORY_INQUIRY) {
                intent.setClass(mContext, QueryStock.class);
                intent.putExtra("Title", info.getTitle());
            }else if(voucherType == OrderType.IN_HOUSE_STOCK_ORDER_TYPE_OPEN_INVENTORY){
                //明盘
                MenuOutStockModel model = new MenuOutStockModel();
                model.Title = getThirdLevelMenuModuleTitle(MenuType.MENU_TYPE_IN_HOUSE_STOCK,OrderType.IN_HOUSE_STOCK_ORDER_TYPE_OPEN_INVENTORY,MenuType.MENU_MODULE_TYPE_IN_HOUSE_STOCK_OPEN_INVENTORY_LIST);
                model.VoucherType =String.valueOf( OrderType.IN_HOUSE_STOCK_ORDER_TYPE_OPEN_INVENTORY);
                String json = GsonUtil.parseModelToJson(model);
                Uri data = Uri.parse(json);
                intent.setData(data);
                intent.setClass(mContext, InventoryHead.class);
            }else if(voucherType==OrderType.IN_HOUSE_STOCK_ORDER_TYPE_BEGINNING_INVENTORY) {
                //期初盘点
                MenuOutStockModel model = new MenuOutStockModel();
                model.VoucherType = String.valueOf(OrderType.IN_HOUSE_STOCK_ORDER_TYPE_BEGINNING_INVENTORY);
                model.Title = info.getTitle();
                String json = GsonUtil.parseModelToJson(model);
                Uri data = Uri.parse(json);
                intent.setData(data);
                intent.setClass(mContext, InventoryScann.class);
            }
            //75 补货
            else if (voucherType == OrderType.IN_HOUSE_STOCK_ORDER_TYPE_REPLENISHMENT) {
                intent.setClass(mContext, InStockHouseReplenishment.class);
                intent.putExtra("Title", info.getTitle());
                intent.putExtra("VoucherType", voucherType);
            }

            //77 标签补打
            else if(voucherType == OrderType.IN_HOUSE_STOCK_ORDER_TYPE_REPRINT_PALLET_LABEL){
                intent.setClass(mContext, ReprintPalletLabel.class);
                intent.putExtra("Title", info.getTitle());
                intent.putExtra("VoucherType", voucherType);
            }

        }
        return intent;
    }

}
