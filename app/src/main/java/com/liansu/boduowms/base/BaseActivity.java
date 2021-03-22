package com.liansu.boduowms.base;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.text.InputType;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.liansu.boduowms.R;
import com.liansu.boduowms.bean.CheckNumRefMaterial;
import com.liansu.boduowms.bean.warehouse.WareHouseInfo;
import com.liansu.boduowms.ui.dialog.MessageBox;
import com.liansu.boduowms.ui.dialog.ToastUtil;
import com.liansu.boduowms.utils.SharePreferUtil;
import com.liansu.boduowms.utils.function.CommonUtil;
import com.liansu.boduowms.utils.hander.IHandleMessage;
import com.liansu.boduowms.utils.hander.MyHandler;
import com.liansu.boduowms.utils.updateversion.UpdateVersionService;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.runtime.Permission;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.List;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


/**
 * Created by GHOST on 2017/3/15.
 */

public abstract class BaseActivity extends AppCompatActivity implements IHandleMessage {
    private              ToolBarHelper           mToolBarHelper;
    public               Toolbar                 toolbar;
    public static final  String                  ACTION_UPDATEUI          = "action.updateUI";
    public               MyHandler<BaseActivity> mHandler;
    private static final int                     REQUEST_CODE_APP_INSTALL = 2;
    private              String                  mTitle                   = ""; // 标题名称

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); //屏幕保持竖屏
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        AppManager.getAppManager().addActivity(this); //添加当前Activity到avtivity管理类
        mHandler = new MyHandler<>(this);
        BaseApplication.isCloseActivity = true;
        updateVersionService = new UpdateVersionService(BaseApplication.context);// 创建更新业务对象
        mTitle = "";
        initViews(); //自定义的方法
        initData();
    }


    /**
     * 初始化控件
     */
    protected void initViews() {
        String title = getIntent().getStringExtra("Title");
        if (title != null) {
            setToolBarTitle(title);
        }
    }

    /**
     * 初始化数据
     */
    protected void initData() {

//        if (BaseApplication.isCloseActivity) {
//            checkUpdate();
//        }
    }

    /**
     * @desc: 给主题加标题
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/9/18 9:13
     */
    protected void setToolBarTitle(String title) {
        if (title != null) {
            mTitle = title;
        }
    }

    /**
     * @desc: 获取主题标题
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/9/18 9:13
     */
    protected String getToolBarTitle() {
        String title = "";
        title = mTitle + "-" + BaseApplication.mCurrentWareHouseInfo.getWarehouseno();
        return title;
    }
   /**
    * @desc: 获取名称
    * @param:
    * @return:
    * @author: Nietzsche
    * @time 2020/9/18 10:45
    */
    protected String getTitleString() {
        return mTitle;
    }

    @Override
    public void onHandleMessage(Message msg) {

    }

    //
    @Override
    public void setContentView(int layoutResID) {
        if (layoutResID == R.layout.activity_login)
            getDelegate().setContentView(layoutResID);
        else {
            mToolBarHelper = new ToolBarHelper(this, layoutResID);
            toolbar = mToolBarHelper.getToolBar();
            setContentView(mToolBarHelper.getContentView());
            if (!TextUtils.isEmpty(BaseApplication.toolBarTitle.Title))
                setTitle(BaseApplication.toolBarTitle.Title);
//        if (!TextUtils.isEmpty(BaseApplication.toolBarTitle.subTitle))
//            toolbar.setSubtitle(BaseApplication.toolBarTitle.subTitle);
//        //toolbar.setLogo(R.mipmap.ic_launcher);
            if (BaseApplication.toolBarTitle.isShowBack)
                toolbar.setNavigationIcon(R.drawable.back);
            /*把 toolbar 设置到Activity 中*/
            setSupportActionBar(toolbar);
            /*自定义的一些操作*/
            if (BaseApplication.toolBarTitle != null) {
                onCreateCustomToolBar(toolbar);
            }
        }

    }

    public void onCreateCustomToolBar(Toolbar toolbar) {
        toolbar.setContentInsetsRelative(0, 0);
        toolbar.showOverflowMenu();
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (BaseApplication.isCloseActivity)
                    closeActivity();
                else
                    BackAlter();
            }
        });
    }


    public Context context;

    public void selectWareHouse(Activity activity) {
        context = activity;
        final String[] items = new String[BaseApplication.mCurrentUserInfo.getModelListWarehouse().size()];
        int i = 0;
        for (WareHouseInfo info : BaseApplication.mCurrentUserInfo.getModelListWarehouse()) {
            items[i] = info.Warehouseno;
            i++;
        }
        new AlertDialog.Builder(activity).setTitle(getResources().getString(R.string.activity_login_WareHousChoice))// 设置对话框标题
                .setIcon(android.R.drawable.ic_dialog_info)// 设置对话框图
                .setCancelable(true)
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO 自动生成的方法存根
                        String select_item = items[which].toString();
                        if (BaseApplication.mCurrentWareHouseInfo.Warehouseno != null) {
                            // mUserSettingPresenter.saveCurrentWareHouse(select_item);
                            for (WareHouseInfo info : BaseApplication.mCurrentUserInfo.getModelListWarehouse()) {
                                String sWareHouseNo = info.getWarehouseno() != null ? info.getWarehouseno() : "";
                                if (sWareHouseNo.trim().equals(select_item)) {
                                    BaseApplication.mCurrentWareHouseInfo = info;
                                    SharePreferUtil.SetWareHouseInfoShare(context, BaseApplication.mCurrentWareHouseInfo);
                                    // BaseApplication.toolBarTitle = new ToolBarTitle("1234", true);
                                    //   getToolBarHelper().getToolBar().setTitle(getToolBarHelper().getToolBar().getTitle() + BaseApplication.mCurrentWareHouseInfo.Warehouseno);
                                    getToolTitle();
                                }
                            }
                        }

                        dialog.dismiss();
                    }
                }).show();

    }


    //会写表头
    public void getToolTitle() {

    }


    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
            if (BaseApplication.isCloseActivity)
                closeActivity();
            else
                BackAlter();
        }
        return true;
    }

    //-99 可以退出  ，成功可以退出  失败允许退出
    public  boolean  ReturnActivity(){
        return true;
    }

    public void BackAlter() {
        if (ReturnActivity()) {
            new AlertDialog.Builder(BaseApplication.context).setTitle("提示").setCancelable(false).setIcon(android.R.drawable.ic_dialog_info).setMessage("是否返回上一页面？")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // TODO 自动生成的方法
                            closeActivity();
                        }
                    }).setNegativeButton("取消", null).show();
        }
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

    public void closeActivity() {
        AppManager.getAppManager().finishActivity();
        BaseApplication.isCloseActivity = true;
        if (AppManager.getAppManager().GetActivityCount() != 0)
            BaseApplication.context = AppManager.getAppManager().currentActivity();
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (BaseApplication.isCloseActivity) {
            checkUpdate();
        }
    }

    /**
     * @desc: 隐藏软键盘
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/23 15:02
     */
    public void closeKeyBoard(EditText view) {
        boolean isDebug = true;
        if (isDebug) return;
        if (android.os.Build.VERSION.SDK_INT <= 10) {
            view.setInputType(InputType.TYPE_NULL);
        } else {
            this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
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
        boolean isDebug = true;
        if (isDebug) return;
        if (editTexts.length > 0) {
            for (EditText editText : editTexts) {
                if (editText != null) {
                    closeKeyBoard(editText);
                }

            }

        }

    }

    /**
     * 左右推动跳转
     *
     * @param intent
     */
    protected void startActivityLeft(Intent intent) {
        startActivity(intent);
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }


    /*
       判断单位对应数字输入规则
     */
    public CheckNumRefMaterial CheckMaterialNumFormat(String qty, String UnitTypeCode, String DecimalLngth) {
        CheckNumRefMaterial checkNumRefMaterial = new CheckNumRefMaterial();
        try {
            int unitTypeCode = Integer.parseInt(UnitTypeCode);
            int decimalLngth = Integer.parseInt(DecimalLngth);

            if (unitTypeCode == 4) {
                if (CommonUtil.isNumeric(qty)) {
                    checkNumRefMaterial.setIscheck(true);
                    checkNumRefMaterial.setCheckQty(Float.parseFloat(qty));
                } else {
                    checkNumRefMaterial.setIscheck(false);
                    checkNumRefMaterial.setErrMsg(getString(R.string.Error_IntRequire));
                }
            } else {
                if (CommonUtil.isFloat(qty)) {
                    checkNumRefMaterial.setIscheck(true);
                    BigDecimal mData = new BigDecimal(qty).setScale(decimalLngth, BigDecimal.ROUND_HALF_UP);
                    checkNumRefMaterial.setCheckQty(mData.floatValue());
                } else {
                    checkNumRefMaterial.setIscheck(false);
                    checkNumRefMaterial.setErrMsg(getString(R.string.Error_isnotnum));
                }
            }
        } catch (Exception ex) {
            ToastUtil.show(ex.getMessage());
        }
        return checkNumRefMaterial;
    }

    public UpdateVersionService updateVersionService;

    /**
     * 检查更新
     */
    public void checkUpdate() {

        new Thread() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                super.run();
                if (updateVersionService.isUpdate()) {
                    handler.sendEmptyMessage(0);
                }// 调用检查更新的方法,如果可以更新.就更新.不能更新就提示已经是最新的版本了
                else {
                    handler.sendEmptyMessage(1);
                }
            }
        }.start();
    }

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    //适配Android8.0以上
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        boolean hasInstallPermission = isHasInstallPermissionWithO(BaseActivity.this);
                        if (!hasInstallPermission) {
                            showRequestPermissionDialog();
                        } else {
                            if (AndPermission.hasPermissions(BaseActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                                //下载apk
                                updateVersionService.showDownloadDialog();
                            } else {
                                initPermission();
                            }

                        }
                    } else {
                        //Android8.0以下
                        //下载apk
                        updateVersionService.showDownloadDialog();
                    }

                    break;
            }
        }

        ;
    };


    /**
     * @desc:
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/28 17:11
     */
    public static void setToolbarTitleViewTextSize(AppCompatActivity activity, Toolbar toolbar) {
        ActionBar actionBar = activity.getSupportActionBar();
        CharSequence actionbarTitle = null;
        if (actionBar != null)
            actionbarTitle = actionBar.getTitle();
        actionbarTitle = TextUtils.isEmpty(actionbarTitle) ? toolbar.getTitle() : actionbarTitle;
        // can't find if title not set
        for (int i = 0; i < toolbar.getChildCount(); i++) {
            View v = toolbar.getChildAt(i);
            if (v != null && v instanceof TextView) {
                TextView t = (TextView) v;
                t.setTextColor(activity.getResources().getColor(R.color.white));
                CharSequence title = t.getText();
                t.setTextSize(activity.getResources().getDimension(R.dimen.activity_app_bar_text_size));

            }
        }
    }


    public ToolBarHelper getToolBarHelper() {
        return mToolBarHelper;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private boolean isHasInstallPermissionWithO(Context context) {
        if (context == null) {
            return false;
        }
        return context.getPackageManager().canRequestPackageInstalls();
    }

    /**
     * 开启设置安装未知来源应用权限界面
     *
     * @param context
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void startInstallPermissionSettingActivity(Context context) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES);
        startActivityForResult(intent, REQUEST_CODE_APP_INSTALL);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_APP_INSTALL:
                    updateVersionService.showDownloadDialog();
                    break;
            }
        }
    }


    private void initPermission() {
        AndPermission.with(this)
                .runtime()
                .permission(Permission.WRITE_EXTERNAL_STORAGE
                )

                .onGranted(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> data) {
//                        if (BaseApplication.isCloseActivity) {
//                            checkUpdate();
//                        }
                    }
                })
                .onDenied(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> data) {
                        /**
                         * 当用户没有允许该权限时，回调该方法
                         */
                        Toast.makeText(BaseActivity.this, "没有获取相关权限，可能导致相关功能无法使用", Toast.LENGTH_SHORT).show();
                        /**
                         * 判断用户是否点击了禁止后不再询问，AndPermission.hasAlwaysDeniedPermission(MainActivity.this, data)
                         */
                        if (AndPermission.hasAlwaysDeniedPermission(BaseActivity.this, data)) {
                            //true，弹窗再次向用户索取权限
                        }
                    }
                }).start();
    }


    private void showRequestPermissionDialog() {
        new AlertDialog.Builder(BaseApplication.context).setTitle("提示").setCancelable(false).setIcon(android.R.drawable.ic_dialog_info).setMessage("您还没有允许应用安装未知应用权限,是否去允许?")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO 自动生成的方法
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            //跳转页面判断允许安卓位置应用权限
                            startInstallPermissionSettingActivity(BaseActivity.this);
                        }
                    }

                }).setNegativeButton("取消", null).show();
    }
}
