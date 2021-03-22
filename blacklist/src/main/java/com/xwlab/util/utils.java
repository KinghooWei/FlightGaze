package com.xwlab.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class utils {

    // 根据出生日期计算年龄
    public static int getAgeByBirthday(String birthday) {
        if ("".equals(birthday) || birthday == null) {
            return -1;
        }
        // System.out.println("birthday>>>" + birthday);
        // 此处调用了获取当前日期，以yyyy-MM-dd格式返回的日期字符串方法
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String nowDate = sdf.format(new Date());

        String[] nowDates = nowDate.split("-");// 当前时间
        String[] dates = birthday.split("-");// 分割时间线 - 年[0],月[1],日[2]

        int age = Integer.parseInt(nowDates[0]) - Integer.parseInt(dates[0]);// 年龄默认为当前时间和生日相减

        if (dates.length >= 2) {// 根据月推算出年龄是否需要增加
            // 如果当前月份大于生日月份，岁数不变，否则加一
            Integer nowMonth = Integer.parseInt(nowDate.substring(5, 7));
            Integer birthMonth = Integer.parseInt(birthday.substring(5, 7));
            if (nowMonth < birthMonth)
                age++;
            if (dates.length >= 3 && nowMonth == birthMonth) {// 月份相同才计算对应年龄
                // 如果天数大于当前生日月份,岁数不变,否则加一
                Integer nowDay = Integer.parseInt(nowDates[2]);// 当前天数
                Integer birDay = Integer.parseInt(dates[2]);// 生日天数
                if (nowDay < birDay)
                    age++;
            }

        }

        return age;
    }
}
