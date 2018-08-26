package com.tsingning.sevenmall.goods.core.model.utils;

import com.tsingning.sevenmall.common.core.model.RequestEntity;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.springframework.ui.ModelMap;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.tsingning.sevenmall.goods.constant.LocalConstant.GOODS_BOOLEAN_YES;

/**
 * Created by Administrator on 2017/9/8 0008.
 */
public final class LocalCommUtils {

    public static void main(String[] args) {

    }

    public static boolean isNotNull(Object obj) {
        if (obj != null && !obj.toString().equals("")) {
            return true;
        } else
            return false;
    }

    public static boolean isNumeric(String str){
        for (int i = 0; i < str.length(); i++){
            if (!Character.isDigit(str.charAt(i))){ //非数字
                return false;
            }
        }
        return true;
    }

    public static<T> boolean checkIsRepeat(List<T> list) {
        HashSet<T> hashSet = new HashSet();
        for (int i = 0; i < list.size(); i++) {
            hashSet.add(list.get(i));
        }

        return hashSet.size() != list.size();
    }

    static {
        ConvertUtils.register(new DateConvertUtils(), java.util.Date.class);
        ConvertUtils.register(new DateConvertUtils(), java.sql.Date.class);
        ConvertUtils.register(new DateConvertUtils(), java.sql.Timestamp.class);
    }

    public static <T> T map2Obj(Map paramMap, Class<T> beanClass) {
        try {
            T obj = beanClass.newInstance();
            BeanUtils.populate(obj, paramMap);
            return obj;
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {

        }
        return null;
    }

    public static <T> List<T> map2Array(List paramList, Class<T> beanClass) {
        List objList = new ArrayList();
        for (Map map : (List<Map>)paramList) {
            objList.add(map2Obj(map, beanClass));
        }

        return objList;
    }

    public static int null2Int(Object s) {
        int v = 0;
        if (s != null)
            try {
                v = Integer.parseInt(s.toString());
            } catch (Exception e) {
            }
        return v;
    }

    public static float null2Float(Object s) {
        float v = 0.0f;
        if (s != null)
            try {
                v = Float.parseFloat(s.toString());
            } catch (Exception e) {
            }
        return v;
    }

    public static double null2Double(Object s) {
        double v = 0.0;
        if (s != null)
            try {
                v = Double.parseDouble(null2String(s));
            } catch (Exception e) {
            }
        return v;
    }

    public static boolean null2Boolean(Object s) {
        boolean v = false;
        if (s != null)
            try {
                v = Boolean.parseBoolean(s.toString());
            } catch (Exception e) {
            }
        return v;
    }

    public static String null2String(Object s) {
        return s == null ? "" : s.toString().trim();
    }

    public static String null2String(byte[] bs) {
        return bs == null ? "" : new String(bs);
    }

    public static Long null2Long(Object s) {
        Long v = -1l;
        if (s != null)
            try {
                v = Long.parseLong(s.toString());
            } catch (Exception e) {
            }
        return v;
    }

    public static BigDecimal null2BigDecimal(Object s) {
        if (s != null)
            try {
                return new BigDecimal(s.toString());
            } catch (Exception e) {
            }
        return BigDecimal.ZERO;
    }

    public static Integer equalReturnNull(Object s, int n){
        if(s == null)
            return null;

        int val = null2Int(s);
        return val == n ? null : val;
    }

    /**
     * 浮点数据加法
     *
     * @param a
     * @param b
     * @return
     */
    public static double add(Object a, Object b) {
        double ret = 0.0;
        BigDecimal e = new BigDecimal(null2Double(a));
        BigDecimal f = new BigDecimal(null2Double(b));
        ret = e.add(f).doubleValue();
        DecimalFormat df = new DecimalFormat("0.00");
        return Double.valueOf(df.format(ret));
    }

    /**
     * 浮点数乘法
     *
     * @param a
     * @param b
     * @return
     */
    public static double mul(Object a, Object b) {// 乘法
        BigDecimal e = new BigDecimal(null2Double(a));
        BigDecimal f = new BigDecimal(null2Double(b));
        double ret = e.multiply(f).doubleValue();
        DecimalFormat df = new DecimalFormat("0.00");
        return Double.valueOf(df.format(ret));
    }

    /**
     * 浮点数除法运算，保证数据的精确度
     *
     * @param a
     * @param b
     * @return
     */
    public static double div(Object a, Object b) {
        double ret = 0.0;
        if (!null2String(a).equals("") && !null2String(b).equals("")) {
            BigDecimal e = new BigDecimal(null2String(a));
            BigDecimal f = new BigDecimal(null2String(b));
            if (null2Double(f) > 0)
                ret = e.divide(f, 3, BigDecimal.ROUND_DOWN).doubleValue();
        }
        DecimalFormat df = new DecimalFormat("0.00");
        return Double.valueOf(df.format(ret));
    }


    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    public static Date formatDate(String s) {
        Date d = null;
        try {
            d = dateFormat.parse(s);
        } catch (Exception e) {
        }
        return d;
    }

    public static Date formatDate(String s, String format) {
        Date d = null;
        try {
            SimpleDateFormat dFormat = new SimpleDateFormat(format);
            d = dFormat.parse(s);
        } catch (Exception e) {
        }
        return d;
    }

    public static String formatTime(String format, Object v) {
        if (v == null)
            return null;
        if (v.equals(""))
            return "";
        SimpleDateFormat df = new SimpleDateFormat(format);
        return df.format(v);
    }

    public static String formatLongDate(Object v) {
        if (v == null || v.equals(""))
            return "";
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return df.format(v);
    }

    public static String formatLongDate2(Object v) {
        if (v == null || v.equals(""))
            return "";
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return df.format(v);
    }

    public static String formatShortDate(Object v) {
        if (v == null)
            return null;
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        return df.format(v);
    }

    public static String formatShortDateChina(Object v) {
        if (v == null)
            return null;
        SimpleDateFormat df = new SimpleDateFormat("yyyy年MM月dd日");
        return df.format(v);
    }

    public static String decode(String s) {
        String ret = s;
        try {
            ret = URLDecoder.decode(s.trim(), "UTF-8");
        } catch (Exception e) {
        }
        return ret;
    }

    public static String encode(String s) {
        String ret = s;
        try {
            ret = URLEncoder.encode(s.trim(), "UTF-8");
        } catch (Exception e) {
        }
        return ret;
    }

    public static String convert(String str, String coding) {
        String newStr = "";
        if (str != null)
            try {
                newStr = new String(str.getBytes("ISO-8859-1"), coding);
            } catch (Exception e) {
                return newStr;
            }
        return newStr;
    }

    /**
     * 获取当前页与页的尺寸
     * @param requestEntity
     * @return
     */
    public static List<Integer> getCurPageAndPageSize(RequestEntity requestEntity){
        int currentPage = 1;
        int pageSize = 4;
        if(requestEntity.getParamMap().containsKey("page_num") && !"".equals(requestEntity.getParamMap().get("page_num")) && !"null".equals(requestEntity.getParamMap().get("page_num"))) {
            currentPage = Integer.valueOf(requestEntity.getParamMap().get("page_num").toString());
        }
        if(requestEntity.getParamMap().containsKey("page_size") && !"".equals(requestEntity.getParamMap().get("page_size")) && !"null".equals(requestEntity.getParamMap().get("page_size"))) {
            pageSize = Integer.valueOf(requestEntity.getParamMap().get("page_size").toString());
        }
        List<Integer> pageInfo = new ArrayList<>();
        pageInfo.add(currentPage);
        pageInfo.add(pageSize);
        return pageInfo;

    }

    /**
     * 获取接口查询组合参数
     * */
    public static Map getSearchParams(Map requestParamMap, ModelMap modelMap){
        Map paramMap = new HashMap();

        final String findGoodsName = (String) requestParamMap.get("findGoodsName");
        final String findMerchantCode = (String) requestParamMap.get("findMerchantCode");
        final String findSupplierId = (String) requestParamMap.get("findSupplierId");
        final Integer findBrandId = equalReturnNull(requestParamMap.get("findBrandId"), -1);
        final Integer findGcId = equalReturnNull(requestParamMap.get("findGcId"), -1);
        final Integer findGoodsStatus = equalReturnNull(requestParamMap.get("findGoodsStatus"), -1);
        final Integer findGoodsMark = equalReturnNull(requestParamMap.get("findGoodsMark"), -1);

        if (findGoodsName != null && findGoodsName.length() > 0) {
            paramMap.put("goodsName", findGoodsName);
        }
        if (findMerchantCode != null && findMerchantCode.length() > 0) {
            paramMap.put("merchantCode", findMerchantCode);
        }
        if (findSupplierId != null && findSupplierId.length() > 0) {
            paramMap.put("supplierId", findSupplierId);
        }
        if (findBrandId != null) {
            paramMap.put("brandId", findBrandId);
        }
        if (findGcId != null) {
            paramMap.put("gcId", findGcId);
        }
        if (findGoodsStatus != null) {
            paramMap.put("goodsStatus", findGoodsStatus);
        }
        if (findGoodsMark != null) {
            switch (findGoodsMark){
                case 0:
                    paramMap.put("isNew", GOODS_BOOLEAN_YES);
                    break;
                case 1:
                    paramMap.put("isHot", GOODS_BOOLEAN_YES);
                    break;
                case 2:
                    paramMap.put("isRecommend", GOODS_BOOLEAN_YES);
                    break;
            }
        }

        if(modelMap != null){
            modelMap.put("findGoodsName", findGoodsName);
            modelMap.put("findMerchantCode", findMerchantCode);
            modelMap.put("findSupplierId", findSupplierId);
            modelMap.put("findBrandId", findBrandId);
            modelMap.put("findGcId", findGcId);

            modelMap.put("findGoodsStatus", findGoodsStatus);
            modelMap.put("findGoodsMark", findGoodsMark);
        }

        return paramMap;
    }

    public static String Map2String(Map<String,Object> paramMap){
        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String,Object> entry : paramMap.entrySet()) {
            sb.append("{").append(entry.getKey()).append(":").append(entry.getValue()).append("}");
        }
        return sb.toString();
    }


    public static void setSessionValue(String sKey, String sVal){

        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpSession session = servletRequestAttributes.getRequest().getSession();
        session.setAttribute(sKey, sVal);
    }

    public static String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }


}
