package com.liansu.boduowms.utils.function;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
            if (isAfterToday(year, month, day,cal) == false) {
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
    public static boolean isAfterToday(int year, int month, int day,Calendar myDate) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar today = Calendar.getInstance();
        int currentDay = today.get(Calendar.DAY_OF_MONTH);
        int currentMonth = today.get(Calendar.MONTH) + 1;//Calendar里取出来的month比实际的月份少1，所以要加上
        int currentYear = today.get(Calendar.YEAR);

        String time1 = format.format(myDate.getTime());
        String time2 = format.format(today.getTime());
        System.out.println("完整的时间和日期1： " + time1);
        System.out.println("完整的时间和日期2： " + time2);

        if (year == currentYear && month == currentMonth && day == currentDay) {
            return false;
        }

        if (myDate.before(today)) {
            return false;
        }

        if (myDate.after(today)) {
            return true;
        }
        return false;
    }


    public static boolean isStartTimeBeforeAndEqualsEndTime(String startTime, String endTime) throws ParseException {
        boolean result = false;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate = sdf.parse(startTime);
        Date endDate = sdf.parse(endTime);
        Calendar start = Calendar.getInstance();
        Calendar end = Calendar.getInstance();
        start.setTime(startDate);
        end.setTime(endDate);
        if (start.before(end)) {
            result = true;
        } else if (start.equals(end)) {
            result = true;
        } else if (start.after(end)) {
            result = false;
        }
        return result;
    }

    /**
     * @desc: 获取当前时间和指定在当期之间多久之前的时间
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/9/3 10:58
     */
    public static List<String> getDateStringFromSpecifyMonthsAgoOrAfter(int specifyMonth, String dateFormat) {
        List<String> mDateList = new ArrayList<>();
        Calendar now = Calendar.getInstance();
        SimpleDateFormat sdf=new SimpleDateFormat(dateFormat);
        String startDate = sdf.format(now.getTime());
        now.add(Calendar.MONTH, specifyMonth);
        String endDate = sdf.format(now.getTime());
        mDateList.add(startDate);
        mDateList.add(endDate);
        return mDateList;
    }

    /**
     * @desc: 日期格式校验
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2021/3/13 16:37
     */
    public static  boolean  checkDate(String  dateStr){
        boolean boo=false;
        if (dateStr!=null){
            String eL = "(([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})(((0[13578]|1[02])(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)(0[1-9]|[12][0-9]|30))|(02(0[1-9]|[1][0-9]|2[0-8]))))|((([0-9]{2})(0[48]|[2468][048]|[13579][26])|((0[48]|[2468][048]|[3579][26])00))0229)";
            Pattern pat = Pattern.compile(eL);
            Matcher matcher = pat.matcher(dateStr);
            boo = matcher.matches();
        }
        return  boo;
    }
}
