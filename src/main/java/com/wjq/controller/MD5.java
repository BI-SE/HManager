package com.wjq.controller;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5工具类.
 *
 * @author Jiajun, Ding
 * @version V1.0
 * @date 2016年1月29日 下午2:13:21
 */
public class MD5 {

    // 全局数组
    private static final String[] strDigits =
            {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};

    public MD5() {
    }

    // 返回形式为数字跟字符串
    private static String byteToArrayString(byte byteValue) {
        int intRet = byteValue;
        // System.out.println("iRet="+iRet);
        if (intRet < 0) {
            intRet += 256;
        }
        int intD1 = intRet / 16;
        int intD2 = intRet % 16;
        return strDigits[intD1] + strDigits[intD2];
    }

    // 返回形式只为数字
    private static String byteToNum(byte byteValue) {
        int intRet = byteValue;
        System.out.println("iRet1=" + intRet);
        if (intRet < 0) {
            intRet += 256;
        }
        return String.valueOf(intRet);
    }

    // 转换字节数组为16进制字串
    private static String byteToString(byte[] byteValue) {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < byteValue.length; i++) {
            buffer.append(byteToArrayString(byteValue[i]));
        }
        return buffer.toString();
    }

    // 获取MD5编码
    public static String GetMD5Code(String strObj) {
        String resultString = null;
        try {
            resultString = new String(strObj);
            MessageDigest md = MessageDigest.getInstance("MD5");
            // md.digest() 该函数返回值为存放哈希值结果的byte数组
            resultString = byteToString(md.digest(strObj.getBytes()));
        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
        }
        return resultString;
    }

    /**
     * 不同位数的数字MD5遍历解密.
     *
     * @param strObj 加密后信息
     * @param rate   位数
     * @return String 解密后数字
     */
    public static String decodeMD5(String strObj, int rate) {
        String resultString = null;
        resultString = new String(strObj);
        long limit = 1;
        for (int i = 0; i < rate; i++) {
            limit *= 10;
        }
        for (long i = 0; i < limit; i++) {
            StringBuffer temp = new StringBuffer();
            int num = rate - String.valueOf(i).length();
            if (num > 0) {
                for (int j = 0; j < num; j++) {
                    temp.append("0");
                }
            }
            temp.append(String.valueOf(i));
            String md5 = temp.toString();
            if (strObj.equals(GetMD5Code(temp.toString()))) {
                resultString = md5;
            }
        }

        return resultString;
    }

    public static void main(String[] args) {
//        MD5 getMD5 = new MD5();
        System.out.println(123);
       // System.out.println(decodeMD5("34f85ca80ec353d3052b8a2d3973a0c5", 8));
        System.out.println(GetMD5Code("123456"));
    }


}
