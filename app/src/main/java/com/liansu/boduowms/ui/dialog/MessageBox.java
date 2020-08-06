package com.liansu.boduowms.ui.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.DialogInterface.OnShowListener;
import android.media.MediaPlayer;
import android.view.View;
import android.widget.EditText;

import com.liansu.boduowms.R;
import com.liansu.boduowms.utils.function.CommonUtil;


public class MessageBox {
    private static MediaPlayer music = null;
    public static  final  int MEDIA_MUSIC_ERROR=0;
    public static  final  int MEDIA_MUSIC_NONE=1;
    public static  final  int MEDIA_MUSIC_SUCCESS=2;


    /**
     * 弹出默认提示框
     *  @param context 上下文
     * @param message 需要弹出的消息
     * @param listener
     */
    public static void Show(Context context, String message, View.OnClickListener listener) {
        music = MediaPlayer.create(context, R.raw.error3);
        music.start();

        new AlertDialog.Builder(context).setTitle("提示").setCancelable(false).setMessage(message).setPositiveButton("确定", null).show();
    }
    /**
     * 弹出默认提示框
     *
     * @param context 上下文
     * @param message 需要弹出的消息
     */
    public static void Show(Context context, String message) {
        music = MediaPlayer.create(context,R.raw.error3);
        music.start();

        new AlertDialog.Builder(context).setTitle("提示").setCancelable(false).setMessage(message).setPositiveButton("确定", null).show();
    }
    /**
     * @desc: type 0 失败  1 成功
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/5/7 12:04
     */
    public static void Show(Context context, String message, int type) {
        if (type == MEDIA_MUSIC_ERROR) {
            music = MediaPlayer.create(context, R.raw.error3);
            music.start();
        }

        new AlertDialog.Builder(context).setTitle("提示").setCancelable(false).setMessage(message).setPositiveButton("确定", null).show();
    }

    public static void Show(Context context, String message, int type, DialogInterface.OnClickListener listener) {
        if (type == MEDIA_MUSIC_ERROR) {
            music = MediaPlayer.create(context, R.raw.error3);
            music.start();
        }

        new AlertDialog.Builder(context).setTitle("提示").setCancelable(false).setMessage(message).setPositiveButton("确定", listener).show();
    }

    public static void Show(Context context, int resourceID) {
        String msg = context.getResources().getString(resourceID);
        new AlertDialog.Builder(context).setTitle("提示").setCancelable(false).setMessage(msg).setPositiveButton("确定", null).show();
    }

    public static void Show(Context context, String mString, EditText togText, AlertDialog alertDialog) {
        alertDialog = new AlertDialog.Builder(context).setTitle("提示").setCancelable(false).setMessage(mString).setPositiveButton("确定", null).create();

        final EditText tagEditText = togText;
        alertDialog.setOnShowListener(new OnShowListener() {

            @Override
            public void onShow(DialogInterface dialog) {
                CommonUtil.setEditFocus(tagEditText);
            }
        });

        alertDialog.show();
    }

    public static void Show(Context context, String mString, EditText togText) {
        AlertDialog dialog = new AlertDialog.Builder(context).setCancelable(false).setTitle("提示").setMessage(mString).setPositiveButton("确定", null).create();

        final EditText tagEditText = togText;
        dialog.setOnShowListener(new OnShowListener() {

            @Override
            public void onShow(DialogInterface dialog) {
                CommonUtil.setEditFocus(tagEditText);
            }
        });
        dialog.setOnDismissListener(new OnDismissListener() {

            @Override
            public void onDismiss(DialogInterface dialog) {
                CommonUtil.setEditFocus(tagEditText);
            }
        });
        dialog.show();
    }

    public static void Show(Context context, String message, EditText recivceTEXT, EditText sendTEXT) {
        AlertDialog dialog = new AlertDialog.Builder(context).setCancelable(false).setTitle("提示").setMessage(message).setPositiveButton("是", null).create();
        final EditText RecivceTEXT = recivceTEXT;
        final EditText SendTEXT = sendTEXT;
        dialog.setOnShowListener(new OnShowListener() {

            @Override
            public void onShow(DialogInterface dialog) {
                CommonUtil.setEditFocus(RecivceTEXT);
            }
        });
        dialog.setOnDismissListener(new OnDismissListener() {

            @Override
            public void onDismiss(DialogInterface dialog) {
                CommonUtil.setEditFocus(SendTEXT);
            }
        });
        dialog.show();
    }

}
