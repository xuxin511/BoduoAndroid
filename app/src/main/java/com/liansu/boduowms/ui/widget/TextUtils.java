package com.liansu.boduowms.ui.widget;

import android.graphics.Color;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;

/**
 * @ Des:
 * @ Created by yangyiqing on 2020/7/19.
 */
 public class TextUtils {
    /**
     * @desc: 一行数据变成两种颜色
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/19 14:02
     */
    public static  SpannableStringBuilder getColorTextString(String value1, String color1, String value2, String color2) {
        SpannableStringBuilder span1 = new SpannableStringBuilder();
        SpannableString spannableString = new SpannableString(value1);
        spannableString.setSpan(new ForegroundColorSpan(Color.parseColor(color1)), 0, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        span1.append(spannableString);
        SpannableString spannableString2 = new SpannableString(value2);
        spannableString2.setSpan(new ForegroundColorSpan(Color.parseColor(color2)), 0, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        span1.append(spannableString2);
        return  span1;
    }
}
