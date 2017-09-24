package com.cloud.sso.oa.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * Created by WCL on 2017/7/6.
 */
public class SystemUtils
{
    private SystemUtils()
    {

    }
    private static volatile SystemUtils instance=null;
    private String ssoUrl = "";
    private String appmark = "";
    private String appword = "";
    private String uuid = "";

    public static SystemUtils getInstance()
    {
        synchronized(SystemUtils.class)
        {
            if(instance==null)
            {
                instance=new SystemUtils();
            }
        }
        return instance;
    }

    public String getSsoUrl() {
        return ssoUrl;
    }

    public void setSsoUrl(String ssoUrl) {
        this.ssoUrl = ssoUrl;
    }

    public String getAppmark() {
        return appmark;
    }

    public void setAppmark(String appmark) {
        this.appmark = appmark;
    }

    public String getAppword() {
        return appword;
    }

    public void setAppword(String appword) {
        this.appword = appword;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    /**
     * @param format
     * @return
     */
    public static String getCurrentSystemTime(String format)
    {
        SimpleDateFormat dateFormatter =new SimpleDateFormat(format);
        Calendar c = Calendar.getInstance();
        return dateFormatter.format(c.getTime());
    }

    /**
     * @param src
     * @return
     */
    public static String EncoderByMd5(String src)
    {
        MessageDigest md = null;
        String des = "";
        try
        {
            md = MessageDigest.getInstance("MD5");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        md.update(src.getBytes());
        byte[] b = md.digest();
        des = bytes2Hexs(b);
        return des;
    }


    /**
     * @param src
     * @return
     */
    public static String bytes2Hexs(byte[] src)
    {
        StringBuffer hs = new StringBuffer();
        String stmp = "";
        hs.setLength(src.length * 2);
        int t = 0;
        try
        {
            for (int n = 0; n < src.length; n++)
            {
                stmp = (java.lang.Integer.toHexString(src[n] & 0XFF));
                if (stmp.length() == 1)
                {
                    hs.setCharAt(t, '0');
                    t++;
                    hs.setCharAt(t, stmp.charAt(0));
                    t++;
                }
                else
                {
                    hs.setCharAt(t, stmp.charAt(0));
                    t++;
                    hs.setCharAt(t, stmp.charAt(1));
                    t++;
                }
            }
        }
        catch (Exception ex)
        {
            return null;
        }
        String strTemp = hs.toString().toUpperCase();
        hs = null;
        return strTemp;
    }

    public static String getBase64(String str) {  
        byte[] b = null;  
        String s = null;  
        try {  
            b = str.getBytes("utf-8");  
        } catch (UnsupportedEncodingException e) {  
            e.printStackTrace();  
        }  
        if (b != null) {  
            s = new BASE64Encoder().encode(b);  
        }  
        return s;  
    }  
  
    public static String getFromBase64(String s) {  
        byte[] b = null;  
        String result = null;  
        if (s != null) {  
            BASE64Decoder decoder = new BASE64Decoder();  
            try {  
                b = decoder.decodeBuffer(s);  
                result = new String(b, "utf-8");  
            } catch (Exception e) {  
                e.printStackTrace();  
            }  
        }  
        return result;  
    }  
}
