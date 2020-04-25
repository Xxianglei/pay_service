/*
 * @(#) egov-powerlist Tools.java Jun 16, 2014
 *
 * Copyright 2014 CDSF Corporation, Inc. All rights reserved.
 */
package com.xianglei.reserve_service.common.utils;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xianglei.reserve_service.common.BaseJson;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 描述：工具类
 * 时间：[2019/11/27:12:14]
 * 作者：xianglei
 * params: * @param null
 */
public abstract class Tools {
    private static final String ipRegex = "^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\."
            + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
            + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
            + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)$";
    private static final String portRegex = "^([0-9]|[1-9]\\d{1,3}|[1-5]\\d{4}|6[0-5]{2}[0-3][0-5])$";

    /**
     * 判断参数是否为空，不为空返回false, 为空返回true
     *
     * @param obj 判断参数
     * @return a boolean.
     * @author wangshuang
     * @creaetime Jun 16, 2014 2:29:06 PM
     */
    public static boolean isNotNull(Object obj) {
        return obj != null && !"null".equals(obj) && !"".equals(obj);
    }

    /**
     * 方法描述：判断前台页面传递到后台来的参数
     *
     * @param obj
     * @return
     * @creator(methods创建人) yyw
     * @creaetime(生成日期) 2015年10月21日 下午5:18:39
     */
    public static boolean isNotNullHtml(Object obj) {
        return obj != null && !"null".equals(obj) && !"".equals(obj)
                && !"undefined".equals(obj);
    }

    /**
     * <p>
     * isNull.
     * </p>
     *
     * @param str a {@link String} object.
     * @return a boolean.
     */
    public static boolean isNull(String str) {
        return str == null || "".equals(str.trim());
    }

    public static boolean isNullHtml(String str) {
        return str == null || "".equals(str.trim())
                || "null".equals(str.trim()) || "undefined".equals(str.trim());
    }

    /**
     * <p>
     * isNotNull.
     * </p>
     *
     * @param str a {@link String} object.
     * @return a boolean.
     */
    public static boolean isNotNull(String str) {
        return str != null && !"".equals(str.trim());
    }

    public static boolean isNotNullAndZero(Double doub) {
        return doub != null && doub.floatValue() != 0f;
    }

    public static boolean isNotNullAndZero(BigDecimal doub) {
        return doub != null && doub.compareTo(BigDecimal.ZERO) != 0;
    }

    public static boolean isNotNullHtml(String str) {
        return str != null && !"".equals(str.trim())
                && !"null".equals(str.trim())
                && !"undefined".equals(str.trim());
    }

    /**
     * <p>
     * isEmpty.
     * </p>
     *
     * @param array an array of {@link Object} objects.
     * @return a boolean.
     */
    public static boolean isEmpty(Object[] array) {
        return array == null || array.length == 0;
    }

    /**
     * <p>
     * isNotEmpty.
     * </p>
     *
     * @param array an array of {@link Object} objects.
     * @return a boolean.
     */
    public static boolean isNotEmpty(Object[] array) {
        return array != null && array.length > 0;
    }

    public static boolean allNotEmpty(String... someString) {
        for (String str : someString) {
            if (isNull(str)) {
                return false;
            }
        }
        return true;
    }

    public static boolean allEmpty(Object... objects) {
        for (Object obj : objects) {
            if (!isNull(obj)) {
                //有值就退出
                return false;
            }
        }
        return true;
    }


    /**
     * <p>
     * isEmpty.
     * </p>
     *
     * @param list a {@link List} object.
     * @return a boolean.
     */
    public static boolean isEmpty(List<?> list) {
        if (list == null || list.isEmpty()) {
            return true;
        }
        return false;
    }

    public static boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    public static boolean isNotEmpty(List<?> list) {
        return list != null && !list.isEmpty();
    }

    public static boolean isNotEmpty(Collection<?> list) {
        return list != null && !list.isEmpty();
    }

    public static boolean isEmpty(Map<?, ?> map) {
        return map == null || map.isEmpty();
    }

    public static boolean isNotEmpty(Map<?, ?> map) {
        return map != null && !map.isEmpty();
    }

    /**
     * 判断参数是否为空，为空返回ture, 不为空返回false
     *
     * @param obj 待判断参数
     * @return a boolean.
     * @author wangshuang
     * @creaetime Jun 23, 2014 9:10:23 AM
     */
    public static boolean isNull(Object obj) {
        return obj == null || "null".equals(obj);
    }

    /**
     * 方法描述：判断前台页面传递到后台来的参数
     *
     * @param obj
     * @return
     * @creator(methods创建人) yyw
     * @creaetime(生成日期) 2015年10月21日 下午5:17:46
     */
    public static boolean isNullHtml(Object obj) {
        return obj == null || "null".equals(obj) || "undefined".equals(obj);
    }





    /**
     * 创建uuid
     *
     * @return a {@link String} object.
     * @author wangshuang
     * @creaetime Jul 2, 2014 6:12:50 PM
     */
    public static String createUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * 获取当前时间戳
     *
     * @return
     */
    public static Timestamp currentTimestamp() {
        return new Timestamp(System.currentTimeMillis());
    }


    /**
     * 方法描述：TODO
     *
     * @return a {@link String} object.
     * @creator(methods创建人) CodingBoy
     * @creaetime(生成日期) 2014年8月27日 上午11:03:35
     */
    public static String createProcessCode() {
        String ap = "0123456789QWERTYUIOPLKJHGFDSAZXCVBNM";
        char[] charArray = ap.toCharArray();
        Random r = new Random();
        int nextInt = 0;
        String code = "";
        for (int i = 0; i < 8; i++) {
            nextInt = r.nextInt(ap.length());
            code = code + charArray[nextInt];
        }
        return code + "-" + System.currentTimeMillis();
    }

    public static String changeStartTimeTOYYYYDDMMHHMMSS(String date) {
        return date == null || "".equals(date) ? null : date + " 00:00:00";
    }

    public static String changeEndTimeTOYYYYDDMMHHMMSS(String date) {
        return date == null || "".equals(date) ? null : date + " 23:59:59";
    }

    /**
     * 方法描述：获取各种编码
     *
     * @param type 类型,1:意见征集,2:网上预约
     * @param now  传入用于获取码的时间,这个时间可能要跟添加时间一致
     * @return code
     * @creator ZhengQiu
     */
    public static String getCode(int type, Date now) {
        switch (type) {
            case 1: // 意见征集
                return "YJ"
                        + new SimpleDateFormat("yyyyMMddHHmmssSSS").format(now)
                        + fixZero(new Random().nextInt(100), 2);
            case 2: // 网上预约
                return "YY"
                        + new SimpleDateFormat("yyyyMMddHHmmssSSS").format(now)
                        + fixZero(new Random().nextInt(100), 2);

            case 3: // 网上投诉
                return "TS"
                        + new SimpleDateFormat("yyyyMMddHHmmssSSS").format(now)
                        + fixZero(new Random().nextInt(100), 2);
            default:
                return "";
        }
    }

    /**
     * 方法描述:数字前面补0
     *
     * @param number 待补0的数字
     * @param size   总共多少位
     * @return 补0后的数字字符串
     * @creator ZhengQiu
     */
    public static String fixZero(int number, int size) {
        if (number < 0) {
            number = 0;
        }
        String str = String.valueOf(number);
        int times = size - str.length();
        for (int i = 0; i < times; i++) {
            str = "0" + str;
        }
        return str;
    }

    /**
     * 方法描述：根据正则，从一个字符串里面取出正则匹配的所有字符串
     *
     * @param form
     * @param regex
     * @return
     * @creator(methods创建人) ZhengQiu
     * @creaetime(生成日期) 2015年2月5日 上午10:55:41
     */
    public static List<String> findStrFormStrByRegex(String form, String regex) {
        List<String> list = new ArrayList<String>();
        String data = null;
        if (isNotNull(form) && regex != null) {
            Matcher m = Pattern.compile(regex).matcher(form);
            while (m.find()) {
                data = m.group();
                list.add(data);
            }
        }
        return list;
    }

    /**
     * 去掉html标签
     *
     * @param input
     * @return String
     * @author jiangailing
     * @createtime 2015年3月8日 上午9:48:48
     */
    public static String splitAndFilterHtmlStr(String input) {
        if (Tools.isNull(input)) {
            return "";
        }
        // 去掉所有html元素,
        String str = input.replaceAll("\\&[a-zA-Z]{1,10};", "")
                .replaceAll("<[^>]*>", "");
        str = str.replaceAll("[(/>)<]", "");
        return str;
    }

    /**
     * 方法描述：TODO
     * 校验一个字符串中，是否包含另一个字符串中的某些字符
     *
     * @param s1
     * @param s2
     * @return
     * @creator(methods创建人) Dream
     * @creaetime(生成日期) 2015年7月17日 下午4:12:11
     */
    public static boolean isContains(String s1, String s2) {
        boolean isContains = false;
        for (int i = 0; i < s1.length() - 1; i++) {
            for (int j = i + 1; j < s1.length(); j++) {
                if (s2.contains(s1.subSequence(i, j))) {
                    isContains = true;
                }
            }
        }
        return isContains;
    }

    /**
     * 方法描述：TODO
     * 删除html标签
     *
     * @param htmlStr
     * @return
     * @creator(methods创建人) chengcheng
     * @creaetime(生成日期) 2015年7月24日 下午2:07:19
     */
    public static String delHTMLTag(String htmlStr) {
        if (isNull(htmlStr)) {
            return "";
        }
        String regEx_script = "<script[^>]*?>[\\s\\S]*?<\\/script>"; // 定义script的正则表达式
        String regEx_style = "<style[^>]*?>[\\s\\S]*?<\\/style>"; // 定义style的正则表达式
        String regEx_html = "<[^>]+>"; // 定义HTML标签的正则表达式

        Pattern p_script = Pattern.compile(regEx_script,
                Pattern.CASE_INSENSITIVE);
        Matcher m_script = p_script.matcher(htmlStr);
        htmlStr = m_script.replaceAll(""); // 过滤script标签

        Pattern p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
        Matcher m_style = p_style.matcher(htmlStr);
        htmlStr = m_style.replaceAll(""); // 过滤style标签

        Pattern p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
        Matcher m_html = p_html.matcher(htmlStr);
        htmlStr = m_html.replaceAll(""); // 过滤html标签

        return htmlStr.trim(); // 返回文本字符串
    }

    /**
     * 方法描述：经常使用的w加逗号方法
     *
     * @param map
     * @creator(methods创建人) ZhengQiu
     * @creaetime(生成日期) 2016年1月21日 下午4:20:17
     */
    public static void addCommaForMapValue(Map<String, Object> map, String key) {
        Object obj = map.get(key);
        if (isNotNull(obj)) {
            String value = obj.toString();
            if (isNotNull(value)) {
                map.put(key, "," + value + ",");
            }
        }
    }

    public static boolean isInArrLow(String str, String[] arr) {
        if (isEmpty(arr) || isNull(str)) {
            return false;
        }
        String strLow = str.toLowerCase();
        for (String obj : arr) {
            if (strLow.equals(obj.toLowerCase())) {
                return true;
            }
        }
        return false;

    }


    public static String addNonEmptyCrossjoin(List<String> list) {
        if (list.size() == 1) {
            return list.get(0);
        } else {
            String mdxString = "NonEmptyCrossjoin(" + list.get(0) + "," + list.get(1) + ")";
            for (int i = 2; i < list.size(); i++) {
                mdxString = "NonEmptyCrossjoin(" + list.get(i) + "," + mdxString + ")";
            }
            return mdxString;
        }
    }



    public static String getMD5String(String param) {
        StringBuilder ret = null;
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] srcBytes = param.getBytes();
            md5.update(srcBytes);
            byte[] byteArray = md5.digest();

            ret = new StringBuilder(byteArray.length << 1);
            for (int i = 0; i < byteArray.length; i++) {
                ret.append(Character.forDigit((byteArray[i] >> 4) & 0xf, 16));
                ret.append(Character.forDigit(byteArray[i] & 0xf, 16));

            }
        } catch (Exception e) {
        }
        return ret.toString();
    }

    /**
     * 验证ip地址是否合规
     *
     * @return
     * @author lulei
     * @creaetime 2017年9月19日 上午9:07:22
     */
    public static Boolean validateIp(String addr) {
        Matcher m = Pattern.compile(ipRegex).matcher(addr);
        return m.find();
    }

    /**
     * 验证ip地址是否合规
     *
     * @return
     * @author lulei
     * @creaetime 2017年9月19日 上午9:07:22
     */
    public static Boolean validatePort(String port) {
        Matcher m = Pattern.compile(portRegex).matcher(port);
        return m.find();
    }

    public static Date string2Date(String date) {
        Date returnDate = null;
        SimpleDateFormat dateFormat;
        if (date.length() == 10) {
            dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        } else {
            dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
        try {
            returnDate = dateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return returnDate;
    }

    public static boolean containChinese(String str) {
        if (!isContainChinese(str)) {
            char[] chars = str.toCharArray();
            for (char aChar : chars) {
                if (isChinese(aChar)) {
                    return true;
                }
            }
            return false;
        }
        return true;
    }

    public static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
            return true;
        }
        return false;
    }

    // 根据UnicodeBlock方法判断中文标点符号
    public static boolean isChinesePunctuation(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_FORMS
                || ub == Character.UnicodeBlock.VERTICAL_FORMS) {
            return true;
        } else {
            return false;
        }
    }

    //判断是否包含中文标点符号
    public static boolean containChineseSign(String str) {
        char[] chars = str.toCharArray();
        for (char aChar : chars) {
            if (isChinesePunctuation(aChar)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断字符串中是否包含中文
     *
     * @param str 待校验字符串
     * @return 是否为中文
     * @warn 不能校验是否为中文标点符号
     */
    public static boolean isContainChinese(String str) {
        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m = p.matcher(str);
        if (m.find()) {
            return true;
        }
        return false;
    }


    public static Calendar getCalendar(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }

    /**
     * date2比date1多的天数
     *
     * @param date1
     * @param date2
     * @return
     */
    public static int differentDays(Date date1, Date date2) {
        Calendar cal1 = getCalendar(date1);

        Calendar cal2 = getCalendar(date2);

        int day1 = cal1.get(Calendar.DAY_OF_YEAR);
        int day2 = cal2.get(Calendar.DAY_OF_YEAR);

        int year1 = cal1.get(Calendar.YEAR);
        int year2 = cal2.get(Calendar.YEAR);

        if (year1 != year2)   //不同年
        {
            int timeDistance = 0;
            for (int i = year1; i < year2; i++) {
                if (i % 4 == 0 && i % 100 != 0 || i % 400 == 0)    //闰年
                {
                    timeDistance += 366;
                } else    //不是闰年
                {
                    timeDistance += 365;
                }
            }
            return timeDistance + (day2 - day1);
        } else  //同一年
        {
            return day2 - day1;
        }
    }

    public static boolean compareDate(Date date1, Date date2) {
        Calendar cal1 = getCalendar(date1);
        Calendar cal2 = getCalendar(date2);
        if (cal1.get(Calendar.YEAR) != cal2.get(Calendar.YEAR)) {
            return false;
        }
        if (cal1.get(Calendar.MONTH) != cal2.get(Calendar.MONTH)) {
            return false;
        }
        if (cal1.get(Calendar.DATE) != cal2.get(Calendar.DATE)) {
            return false;
        }
        if (cal1.get(Calendar.HOUR) != cal2.get(Calendar.HOUR)) {
            return false;
        }
        if (cal1.get(Calendar.MINUTE) != cal2.get(Calendar.MINUTE)) {
            return false;
        }
        if (cal1.get(Calendar.SECOND) != cal2.get(Calendar.SECOND)) {
            return false;
        }

        return true;
    }

    public static String joinDBsourceWithIP(String dbSource, String dbName, String ip) {
        return String.format("%s(%s@%s)", dbSource, dbName, ip);
    }

    public static String joinDBnameWithIP(String dbName, String ip) {
        return String.format("(%s@%s)", dbName, ip);
    }

    //add by zhoufaxuan
    public static <T> List<T> setToList(Set<T> datas) {
        List<T> data = new ArrayList<T>();
        data.addAll(datas);
        return data;
    }

    //add by zhoufaxuan
    public static <T> Set<T> listToSet(List<T> datas) {
        Set<T> data = new HashSet<T>();
        data.addAll(datas);
        return data;
    }

    /**
     * 倒序排列
     *
     * @param datas
     * @return
     */
    public static <T> List<T> setToReverList(Set<T> datas) {
        List<T> data = new ArrayList<T>();
        Object[] objs = datas.toArray();
        int len = objs.length - 1;
        while (len >= 0) {
            data.add((T) objs[len]);
            len--;
        }
        return data;
    }


    public static boolean allFieldIsNull(Object obj) {
        Class clz = obj.getClass();
        Field[] fields = clz.getDeclaredFields();
        boolean flag = true;
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                Object val = field.get(obj);
                if (field.getType().getSimpleName().equals("String")) {
                    if (null != val && !"".equals(val)) {
                        flag = false;
                        break;
                    }
                } else {
                    if (val != null) {
                        flag = false;
                        break;
                    }
                }
            } catch (IllegalAccessException e) {
                //
            }
        }

        return flag;
    }


    /**
     * 做时间度量时的简单打印工具
     *
     * @param lastTimeMil 开始时间点
     * @param desc        耗时操作描述
     * @return 当前时间，做为下次调用的 lastTimeMil
     */
    public static long printUsedTime(long lastTimeMil, String desc) {
        long now = System.currentTimeMillis();
        long usedTime = now - lastTimeMil;
        System.out.println(String.format("%s usedTime : %s (s)", desc, usedTime / 1000));
        return now;
    }


    /**
     * 数据库分批操作
     *
     * @param voList 操作的总集合
     * @param subset 批次大小 例如 1000
     * @param jdbc   匿名函数
     * @param <T>    集合类型
     */
    public static <T> void splitBatch(List<T> voList, int subset, Consumer<List<T>> jdbc) {
        if (voList.isEmpty()) {
            return;
        }
        int size = voList.size();
        int batchCount = size / subset;
        if (size % subset != 0) {
            batchCount += 1;
        }

        for (int i = 0; i < batchCount; i++) {
            int start = subset * i;
            int end = subset * (i + 1);
            if (size >= end) {
                jdbc.accept(voList.subList(start, end));
            } else {
                jdbc.accept(voList.subList(start, size));
            }
        }
    }

    /**
     * V1.0 => V1.1
     *
     * @param curVersion
     * @return
     */
    public static String addVersion(String curVersion) {
        if (Tools.isNull(curVersion)) {
            curVersion = "V1.0";
            return curVersion;
        }
        //V1.0 => 1.O
        String version = curVersion.substring(1);
        BigDecimal decimal = new BigDecimal(version);
        BigDecimal newDecimal =
                // 1.0 + 0.1
                decimal.add(new BigDecimal(0.1)).
                        setScale(1, BigDecimal.ROUND_DOWN);
        return "V" + newDecimal.toString();
    }

    public static <T> List<List<T>> splitList(List<T> list, int pageSize) {
        List<List<T>> listArray = new ArrayList<List<T>>();
        List<T> subList = null;
        for (int i = 0; i < list.size(); i++) {
            if (i % pageSize == 0) {// 每次到达页大小的边界就重新申请一个subList
                subList = new ArrayList<T>();
                listArray.add(subList);
            }
            subList.add(list.get(i));
        }
        return listArray;
    }

    /**
     * 将一组数据平均分成n组
     *
     * @param source 要分组的数据源
     * @param n      平均分成n组
     * @param <T>
     * @return
     */
    public static <T> List<List<T>> averageAssign(List<T> source, int n) {
        List<List<T>> result = new ArrayList<List<T>>();
        int remainder = source.size() % n;  //(先计算出余数)
        int number = source.size() / n;  //然后是商
        int offset = 0;//偏移量
        for (int i = 0; i < n; i++) {
            List<T> value = null;
            if (remainder > 0) {
                value = source.subList(i * number + offset, (i + 1) * number + offset + 1);
                remainder--;
                offset++;
            } else {
                value = source.subList(i * number + offset, (i + 1) * number + offset);
            }
            result.add(value);
        }
        return result;
    }

    /**
     * 将一组数据固定分组，每组n个元素
     *
     * @param source 要分组的数据源
     * @param n      每组n个元素
     * @param <T>
     * @return
     */
    public static <T> List<List<T>> fixedGrouping(List<T> source, int n) {

        if (null == source || source.size() == 0 || n <= 0) {
            return null;
        }
        List<List<T>> result = new ArrayList<List<T>>();

        int sourceSize = source.size();
        int size = (source.size() / n) + 1;
        for (int i = 0; i < size; i++) {
            List<T> subset = new ArrayList<T>();
            for (int j = i * n; j < (i + 1) * n; j++) {
                if (j < sourceSize) {
                    subset.add(source.get(j));
                }
            }
            result.add(subset);
        }
        return result;
    }


    /**
     * 将一组数据固定分组，每组n个元素
     *
     * @param source 要分组的数据源
     * @param n      每组n个元素
     * @param <T>
     * @return
     */
    public static <T> List<List<T>> fixedGrouping2(List<T> source, int n) {

        if (null == source || source.size() == 0 || n <= 0) {
            return null;
        }
        List<List<T>> result = new ArrayList<List<T>>();
        int remainder = source.size() % n;
        int size = (source.size() / n);
        for (int i = 0; i < size; i++) {
            List<T> subset = null;
            subset = source.subList(i * n, (i + 1) * n);
            result.add(subset);
        }
        if (remainder > 0) {
            List<T> subset = null;
            subset = source.subList(size * n, size * n + remainder);
            result.add(subset);
        }
        return result;
    }



    public static String getFirstVersion() {
        return "V1.0";
    }

    public static String getNextVersion(String version) {
        if (Tools.isNotNull(version)) {
            DecimalFormat df = new DecimalFormat("#.0");
            String vs = version.substring(version.lastIndexOf("V") + 1);
            double vd = Double.parseDouble(vs);
            vd = vd + 0.1;
            String nv = df.format(vd);
            return "V" + nv;
        }
        return null;
    }


    /**
     * 去除重复数据,通过HashSet的方法。<br>
     * <font color=orange>但是不会改变list顺序</font>
     *
     * @param list 去重复的数据
     */
    public static <T> void cleanListRepetition(List<T> list) {
        if (Tools.isEmpty(list)) return;
        Set<T> thisSet = new HashSet<>(list);
        list.clear();
        list.addAll(thisSet);
    }

    /**
     * <p>将字符串根据逗号切割为集合</p>
     *
     * @param str
     * @return
     */
    public static List<String> strToList(String str) {
        if (Tools.isNull(str)) return null;
        return Arrays.asList(str.split(","));
    }

    /**
     * <p>将集合数据转化成字符串以某个符号分割</p>
     *
     * @param list
     * @param str  需要添加的分割字符
     * @return
     */
    public static String collectionJoinToStr(Collection<String> list, String str) {
        if (isEmpty(list)) return null;
        StringBuilder result = new StringBuilder();
        for (String thisStr : list) {
            if (result.length() > 0) {
                result.append(str);
            }
            result.append(thisStr);
        }
        return result.toString();
    }

    public static String joinStr(String... arrs) {
        if (isEmpty(arrs)) return null;
        return collectionJoinToStr(Arrays.asList(arrs), "");
    }

    /**
     * <p>获取可变集合</p>
     *
     * @param a
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> List<T> asList(T... a) {
        return new ArrayList<>(Arrays.asList(a));
    }

    /**
     * <p>对比两个字符串 如果都为空也是true</p>
     *
     * @param str
     * @param str2
     * @return
     */
    public static boolean strEquals(String str, String str2) {
        if (str == null) {
            return str2 == null;
        }
        return str.equals(str2);
    }

    /**
     * sql 查询操作时处理 关键字 特殊字符替换 ' 和 %
     *
     * @param sql 需要转换的字符串
     * @return
     */
    public static String sqlReplaceCharacter(String sql) {
        if (sql == null) {
            return "";
        }
        return sql.replace("'", "\\'")
                .replace("%", "\\%")
                .replace("\\", "\\\\")
                .replace("_", "\\_");
    }
    public static String getJsonFormat(BaseJson baseJson) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String result = mapper.writeValueAsString(baseJson);
        return result;
    }

}
