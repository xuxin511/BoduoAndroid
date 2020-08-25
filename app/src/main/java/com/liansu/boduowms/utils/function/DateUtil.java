package com.liansu.boduowms.utils.function;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @ Des:
 * @ Created by yangyiqing on 2020/8/16.
 */
public class DateUtil {
    /**
     * 检查时间格式是否正确
     *
     * @param str       时间格式字符
     * @param pattern 时间格式
     */

    public static boolean isValidDate(String str, String pattern) {

        boolean convertSuccess = true;

// 指定日期格式为四位年/两位月份/两位日期，注意yyyy/MM/dd区分大小写；

        SimpleDateFormat format = new SimpleDateFormat(pattern);

        try {

// 设置lenient为false. 否则SimpleDateFormat会比较宽松地验证日期，比如2007/02/29会被接受，并转换成2007/03/01

            format.setLenient(false);

            format.parse(str);

        } catch (ParseException e) {
// e.printStackTrace();
            // 如果throw java.text.ParseException或者NullPointerException，就说明格式不对
            convertSuccess = false;

        }

        return convertSuccess;

    }


    /**
     * @desc: 校验日期超过当前日期
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/8/17 11:35
     */
    public static boolean isBeforeOrCompareToday(String str, String pattern) {
        boolean convertSuccess = true;
// 指定日期格式为四位年/两位月份/两位日期，注意yyyy/MM/dd区分大小写；
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        try {
// 设置lenient为false. 否则SimpleDateFormat会比较宽松地验证日期，比如2007/02/29会被接受，并转换成2007/03/01
            format.setLenient(false);
            Date sDate = format.parse(str);
            Calendar cal = Calendar.getInstance();
            cal.setTime(sDate);
            int day = cal.get(Calendar.DAY_OF_MONTH);
            int month = cal.get(Calendar.MONTH) + 1;//Calendar里取出来的month比实际的月份少1，所以要加上
            int year = cal.get(Calendar.YEAR);
            if (isAfterToday(year, month, day) == false) {
                convertSuccess = true;
            } else {
                convertSuccess = false;
            }
        } catch (ParseException e) {
            // e.printStackTrace();
            // 如果throw java.text.ParseException或者NullPointerException，就说明格式不对
            convertSuccess = false;

        } catch (Exception e) {
            convertSuccess = false;
        }

        return convertSuccess;

    }


    /**
     * @desc: 日期是否超过今天
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/8/17 12:29
     */
    public static boolean isAfterToday(int year, int month, int day) {
        Calendar today = Calendar.getInstance();
        int currentDay = today.get(Calendar.DAY_OF_MONTH);
        int currentMonth = today.get(Calendar.MONTH) + 1;//Calendar里取出来的month比实际的月份少1，所以要加上
        int currentYear = today.get(Calendar.YEAR);
        today.set(currentYear, currentMonth, currentDay);
        Calendar myDate = Calendar.getInstance();
        myDate.set(year, month, day);
        if (myDate.before(today)) {
            return false;
        }
        if (myDate.after(today)) {
            return true;
        }
        return false;
    }
}
