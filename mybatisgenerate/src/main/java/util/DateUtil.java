package util;

import com.alibaba.dubbo.common.utils.StringUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateUtil {
    
    private static String[] week = { "星期一(Monday)", "星期二(Tuesday)", "星期三(Wednesday)", "星期四(Thursday)",
            "星期五(Friday)", "星期六(Saturday)", "星期日(Sunday)" };

    /**
     * 判断当前日期往前推的号数(如果2015-05-20,号数是20)是否是对应该月份的最后一个月
     * 
     * @return
     */
    public static boolean judgeDay(String day) {
        Calendar cal = getCal(day);
        boolean flag = false;
        int last = cal.getActualMaximum(Calendar.DATE);// 该月的最后一天
        String[] strs = day.split("-");
        if (Integer.parseInt(strs[2]) == last) {
            // //指定日期的号数等于指定月份最后一天的号数--这个时候可以查询这个月的数据
            // String[] dayStr = queryDayStr(day);
            flag = true;
            return flag;
        }
        return flag;
    }

    /**
     * 根据日期获取上个月的日期
     * 
     * @return
     * @return
     * @return
     * @throws ParseException
     */
    public static String queryLastMontyDay(String day) {
        String lastMonthDay = "";
        if (StringUtils.isEmpty(day)) {
            return lastMonthDay;
        }
        try {
            Calendar cal = getCal(day);
            cal.add(Calendar.MONTH, -1);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            lastMonthDay = sdf.format(cal.getTime());
            // System.out.println("指定月份的上个月:"+lastMonthDay);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lastMonthDay;
    }

    /**
     * 根据指定日期获取对应的该月的第一天和最后一天的日期
     * 
     * @param day
     * @return
     */
    public static String[] queryDayStr(String day) {
        String[] strs = new String[2];
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            cal.setTime(sdf.parse(day));
            int last = cal.getActualMaximum(Calendar.DATE);// 该月的最后一天
            String[] dayStr = day.split("-");
            String startDay = dayStr[0] + "-" + dayStr[1] + "-01";
            String endDay = dayStr[0] + "-" + dayStr[1] + "-" + last;
            // System.out.println("可以查询的范围:"+startDay+","+endDay);
            strs[0] = startDay;
            strs[1] = endDay;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return strs;
    }

    /**
     * 根据指定日期(yyyy-MM)获取对应的该月的第一天和最后一天的日期
     * 
     * @param day
     * @return
     */
    public static String[] queryDayStrByMonty(String day) {
        String[] strs = new String[2];
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        try {
            cal.setTime(sdf.parse(day));
            int last = cal.getActualMaximum(Calendar.DATE);// 该月的最后一天
            String[] dayStr = day.split("-");
            String startDay = dayStr[0] + "-" + dayStr[1] + "-01";
            String endDay = dayStr[0] + "-" + dayStr[1] + "-" + last;
            // System.out.println("可以查询的范围:"+startDay+","+endDay);
            strs[0] = startDay;
            strs[1] = endDay;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return strs;
    }

    /**
     * 获取当前日期往前(num<0)或者往后的日期(num>0)
     * 
     * @param num
     * @return
     */
    public static String getDay(int num) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, num);
        SimpleDateFormat adf1 = new SimpleDateFormat("yyyy-MM-dd");// 小写的mm表示的是分钟
        String date = adf1.format(cal.getTime());
        return date;
    }

    /**
     * 返回指定日期的日历
     * 
     * @param day
     * @return
     * @throws ParseException
     */
    public static Calendar getCal(String day) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        try {
            cal.setTime(sdf.parse(day));
        } catch (ParseException e) {

            e.printStackTrace();
        }
        return cal;
    }

    /**
     * 获取某一天对应的本周日期范围(需求中的本周:星期一开始,星期日结束为一周) return dayStr[0]:起始时间
     * dayStr[1]:结束时间
     */
    public static String[] queryWeekDay(int num) {
        String[] dayStr = new String[2];
        try {
            String date = DateUtil.getDay(num);
            dayStr[1] = date;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat dateFm = new SimpleDateFormat("EEEE");
            String dd = dateFm.format(sdf.parse(date));

            if ("星期日(Sunday)".contains(dd)) {
                // num=-1,dd=星期日
                date = DateUtil.getDay(0);
                dayStr[1] = date;
                dayStr[0] = date;
                return dayStr;
            }

            for (int i = 0; i < week.length; i++) {
                if ((week[i]).contains(dd)) {
                    date = DateUtil.getDay(-i + num);
                    dayStr[0] = date;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dayStr;
    }

    /**
     * 时间比较
     * 
     * @param day1
     * @param day2
     * @return 1:day1>day2 -1:day1<day2 0:day1=day2
     */
    public static int compare_date(String date1, String date2) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date dt1 = df.parse(date1);
            Date dt2 = df.parse(date2);
            if (dt1.getTime() > dt2.getTime()) {
                return 1;
            } else if (dt1.getTime() < dt2.getTime()) {
                return -1;
            } else {
                return 0;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return 0;
    }

    /**
     * 两个时间之间相差距离多少天
     * 
     * @param one 时间参数 1当前天数：
     * @param two 时间参数 2过期时间：
     * @return 相差天数
     */
    public static long getDistanceDays(String str1, String str2) throws Exception {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date one;
        Date two;
        long days = 0;
        try {
            one = df.parse(str1);
            two = df.parse(str2);
            long time1 = one.getTime();
            long time2 = two.getTime();
            long diff;
            if (time1 < time2) {
                diff = time2 - time1;
                days = diff / (1000 * 60 * 60 * 24);
            } else {
                if (str1.compareTo(str2) > 0) {
                    return 0;
                }
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return days + 1;
    }

    /**
     * 获取某一天对应的本周日期范围(需求中的本周:星期一开始,星期日结束为一周) return dayStr[0]:起始时间
     * dayStr[1]:结束时间
     */
    public static String[] queryWeekDayNew(int num) {
        String[] dayStr = new String[2];
        try {
            String date = DateUtil.getDay(num);
            dayStr[1] = date;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat dateFm = new SimpleDateFormat("EEEE");
            String dd = dateFm.format(sdf.parse(date));
            
            for (int i = 0; i < week.length; i++) {
                if ((week[i]).contains(dd)) {
                    date = DateUtil.getDay(-i + num);
                    dayStr[0] = date;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dayStr;
    }

    /**
     * 格式化日期
     * 
     * @param date 日期
     * @param format 日期规则
     * @return
     * @author 杨永亚 2016年9月20日 新建
     */
    public static String dateformat(Date date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    /**
     * 比较两个日期相差的天数
     * 
     * @param fDate
     * @param oDate
     * @return
     * @author 杨永亚 2016年12月29日 新建
     */
    public static int daysOfTwo(Date fDate, Date oDate) {
        Calendar aCalendar = Calendar.getInstance();
        aCalendar.setTime(fDate);
        int day1 = aCalendar.get(Calendar.DAY_OF_YEAR);
        aCalendar.setTime(oDate);
        int day2 = aCalendar.get(Calendar.DAY_OF_YEAR);
        return day2 - day1;
    }

    /**
     * 比较两个日期相差的天数（另外比较了时分秒）
     * 
     * @param fDate
     * @param oDate
     * @return
     * @author 杨永亚 2016年12月29日 新建
     */
    public static int getIntervalDays(Date fDate, Date oDate) {
        if (null == fDate || null == oDate) {
            return -1;
        }
        long intervalMilli = oDate.getTime() - fDate.getTime();
        return (int) (intervalMilli / (24 * 60 * 60 * 1000));
    }

    /**
     * 根据制定格式format指定日期
     * 
     * @param date
     * @param pattern
     * @return
     * @author 杨永亚 2016年11月18日 新建
     */
    public static String formatDate(Date date, String pattern) {
        if (date == null) {
            return "";
        }
        SimpleDateFormat simple = new SimpleDateFormat(pattern);
        return simple.format(date);
    }

    /**
     * 根据制定格式parse指定日期字符串
     * 
     * @param date
     * @param pattern
     * @return
     * @throws ParseException
     * @author 杨永亚 2016年11月18日 新建
     */
    public static Date parseDate(String date, String pattern) throws ParseException {
        SimpleDateFormat simple = new SimpleDateFormat(pattern);
        return simple.parse(date);
    }

    /***
     * 判断时间Date是否是整点， ， 整点规则： 分钟和秒都是0
     * 
     * @return true是整點 ， 否则 false
     * @author 罗政2017年3月23日 新建
     */
    public static boolean isFullTime(Date date) {
        if (null != date) {
            GregorianCalendar gc = new GregorianCalendar();
            gc.setTime(date);
            if ((gc.get(GregorianCalendar.MINUTE) == 0) && (gc.get(GregorianCalendar.SECOND) == 0)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * 时间加多少毫秒
     * 
     * @param date
     * @param millisecond
     * @return
     * @author 卢家伟  2017年11月28日 新建
     */
    public static Date dateAddMilli(Date date, long millisecond) {
        long curren = date.getTime();
        curren += millisecond;
        return new Date(curren);
    }
}
