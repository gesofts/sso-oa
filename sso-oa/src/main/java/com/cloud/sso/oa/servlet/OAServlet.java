package com.cloud.sso.oa.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;

import com.cloud.sso.oa.util.SystemUtils;

public class OAServlet extends HttpServlet {
    private static final long serialVersionUID = 3615122544373006252L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	this.doPost(request, response);
        
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String method = request.getParameter("method");
        String params = request.getParameter("params");
        String appmark = request.getParameter("appmark");
        String appword = request.getParameter("appword");
    	String time = SystemUtils.getCurrentSystemTime("yyyyMMddHHmmss");
    	String sign = SystemUtils.EncoderByMd5(String.format("%s%s%s%s%s", 
    			appmark, appword, time, method, params));
    	
    	Map<String, String> paramMap = new HashMap<>();
    	paramMap.put("method", method);
    	paramMap.put("params", params);
    	paramMap.put("appmark", appmark);
    	paramMap.put("time", time);
    	paramMap.put("sign", sign);
    	String jsonObj = httpPostInfo("http://127.0.0.1:9000/aj-typt/web3/datapt/getExchangeData", paramMap);
    	response.setHeader("Content-type", "text/html;charset=UTF-8");  
    	response.getWriter().write(jsonObj);
        //request.getRequestDispatcher("/WEB-INF/jsp/welcome.jsp").forward(request, response);
        
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
    
}