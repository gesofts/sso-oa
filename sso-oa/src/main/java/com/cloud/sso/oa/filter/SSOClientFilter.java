package com.cloud.sso.oa.filter;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;

import com.alibaba.fastjson.JSONObject;
import com.cloud.sso.oa.util.SystemUtils;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class SSOClientFilter implements Filter {

    @Override
    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpSession session = request.getSession();
        String username = (String) session.getAttribute("username");
        String url = URLEncoder.encode(request.getRequestURL().toString(), "UTF-8");

        System.out.println("username======================="+username);
        if (request.getRequestURL().toString().indexOf("/sso-oa/ticket") > 0)
        {
        	//session.setAttribute("username", request.getParameter("loginname"));
        	/*session.setAttribute("ticket", request.getParameter("ticket"));*/
        	System.out.println("--------------------------------------1");
        	System.out.println("--------------------------------------=="+request.getParameter("gotoUrl"));
        	System.out.println("--------------------------------------=="+getFromBase64(request.getParameter("gotoUrl")));
        	
            try 
            {
            	System.out.println("--------------------------------------ticket");
            	//ticket认证，并获取token
            	Map<String, String> paramMap = new HashMap<>();
            	paramMap.put("ticket", request.getParameter("ticket"));
            	paramMap.put("appMark", "sdsdsdsfs");
            	paramMap.put("time", SystemUtils.getCurrentSystemTime("yyyyMMddHHmmss"));
            	paramMap.put("sign", SystemUtils.EncoderByMd5(paramMap.get("appMark") + "3dzhUibQ1NJs" + paramMap.get("time")));
            	String postBody = httpPostInfo("http://122.96.144.74:9799/aj-typt/web3/tyrz/validateTicket", paramMap);
            	System.out.println("--------------------------------------token");
            	
            	//根据token获取用户信息
                JSONObject jsonObject = JSONObject.parseObject(postBody);
                String token = (String) jsonObject.get("token");
                paramMap = new HashMap<>();
            	paramMap.put("token", token);
            	paramMap.put("appMark", "sdsdsdsfs");
            	paramMap.put("time", SystemUtils.getCurrentSystemTime("yyyyMMddHHmmss"));
            	paramMap.put("sign", SystemUtils.EncoderByMd5(paramMap.get("appMark") + "3dzhUibQ1NJs" + paramMap.get("time")));
                postBody = httpPostInfo("http://122.96.144.74:9799/aj-typt/web3/tyrz/findUserByToken", paramMap);
                jsonObject = JSONObject.parseObject(postBody);
                session.setAttribute("username", jsonObject.get("loginname"));
            } 
            catch (Exception e) 
            {
               e.printStackTrace();
            }
            
        	//通过token获取用户信息
        	
        	
        	
        	
        	response.sendRedirect(getFromBase64(request.getParameter("gotoUrl")));
        }
        else if (null == username) 
        {
        	
            /*if (null != ticket && !"".equals(ticket)) {
                PostMethod postMethod = new PostMecothod("http://192.168.20.133:9899//web3/tyrz/getTicket");
                HttpClient httpClient = new HttpClient();
                try {
                    httpClient.executeMethod(postMethod);
                    username = postMethod.getResponseBodyAsString();
                    postMethod.releaseConnection();
                } catch (Exception e) {
                    s	e.printStackTrace();
                }
                if (null != username && !"".equals(username)) {
                    session.setAttribute("username", username);
                    filterChain.doFilter(request, response);
                } else {
                    response.sendRedirect("http://192.168.20.133:9899/aj-typt/web3/tyrz/childsys/121");
                }
            } else {
            	System.out.println("=========================");
                
            }*/
        	System.out.println("--------------------------------------2");
            response.sendRedirect("http://122.96.144.74:9799/aj-typt/web3/tyrz/childsys/" + URLEncoder.encode(getBase64(request.getRequestURL().toString()), "UTF-8") + "/hXpuinB4RYjw");
        } 
        else 
        {
            filterChain.doFilter(request, response);
        }
    }

    public static String httpPostInfo(String url, Map<String, String> paramMap)
    {
    	PostMethod postMethod = new PostMethod(url);
    	 Iterator<Entry<String, String>> it = paramMap.entrySet().iterator();
         while (it.hasNext())
         {
             Entry entry = it.next();
             System.out.println(String.format("%s=================%s", entry.getKey(), entry.getValue()));
             postMethod.addParameter(entry.getKey().toString(), entry.getValue().toString());
         }
        HttpClient httpClient = new HttpClient();
        try 
        {
            httpClient.executeMethod(postMethod);
            String postBody = postMethod.getResponseBodyAsString();
            System.out.println("postBody=============="+postBody);
            postMethod.releaseConnection();
            return postBody;
        } 
        catch (Exception e) 
        {
           e.printStackTrace();
        }
        return "";
    }
    
    @Override
    public void init(FilterConfig arg0) throws ServletException {
    }

    
 // 加密  
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
  
    // 解密  
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